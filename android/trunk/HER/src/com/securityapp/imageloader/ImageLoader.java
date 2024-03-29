/* Copyright (c) 2009 Matthias Kaeppler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.securityapp.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

import com.her.utils.UiUtils;
import com.securityapp.cachefu.ImageCache;
import com.securityapp.imageloader.ImageDownloaderGoogle.FlushedInputStream;

/**
 * Realizes an background image loader backed by a two-level FIFO cache. If the
 * image to be loaded is present in the cache, it is set immediately on the
 * given view. Otherwise, a thread from a thread pool will be used to download
 * the image in the background and set the image on the view as soon as it
 * completes.
 * 
 * @author Matthias Kaeppler
 */
public class ImageLoader implements Runnable {
	public static final int UNKNOWN = 0;
	public static final int HANDLER_MESSAGE_ID = 0;
	public static final String BITMAP_EXTRA = "droidfu:extra_bitmap";
	public static final String IMAGE_URL_EXTRA = "droidfu:extra_image_url";

	private static final String LOG_TAG = "Droid-Fu/ImageLoader";
	// the default thread pool size
	private static final int DEFAULT_POOL_SIZE = 5;
	// expire images after a day
	// TODO: this currently only affects the in-memory cache, so it's quite
	// pointless
	private static final int DEFAULT_TTL_MINUTES = 5*24 *60; // 5day
	private static final int DEFAULT_RETRY_HANDLER_SLEEP_TIME = 1000;
	private static final int DEFAULT_NUM_RETRIES = 1;
	private static ThreadPoolExecutor executor;
	private static ImageCache imageCache;
	private static int numRetries = DEFAULT_NUM_RETRIES;

	private static long expirationInMinutes = DEFAULT_TTL_MINUTES;
	private static Context mContext;
	private static Set<String> mDownloadAttemptsDoneList; 

	/**
	 * @param numThreads
	 *            the maximum number of threads that will be started to download
	 *            images in parallel
	 */
	public static void setThreadPoolSize(int numThreads) {
		executor.setMaximumPoolSize(numThreads);
	}

	/**
	 * @param numAttempts
	 *            how often the image loader should retry the image download if
	 *            network connection fails
	 */
	public static void setMaxDownloadAttempts(int numAttempts) {
		ImageLoader.numRetries = numAttempts;
	}

	/**
	 * This method must be called before any other method is invoked on this
	 * class. Please note that when using ImageLoader as part of
	 * {@link WebImageView} or {@link WebGalleryAdapter}, then there is no need
	 * to call this method, since those classes will already do that for you.
	 * This method is idempotent. You may call it multiple times without any
	 * side effects.
	 * 
	 * @param context
	 *            the current context
	 */
	public static synchronized void initialize(Context context) {
		mContext=context;
        mDownloadAttemptsDoneList=	new HashSet<String>();
		if (executor == null) {
			executor = (ThreadPoolExecutor) Executors
					.newFixedThreadPool(DEFAULT_POOL_SIZE);
		}
		if (imageCache == null) {
			// Changed by Atul Bhardwaj (from 25 t0 250)
			imageCache = new ImageCache(250, expirationInMinutes,DEFAULT_POOL_SIZE);
			imageCache.enableDiskCache(context, ImageCache.DISK_CACHE_SDCARD);
		}
	}

	public static synchronized void initialize(Context context,	long expirationInMinutes) {
		ImageLoader.expirationInMinutes = expirationInMinutes;
		initialize(context);
	}

	private String imageUrl;

	private ImageLoaderHandler handler;
	private int reqWidth;
	private int reqHeight;

	private ImageLoader(String imageUrl, ImageLoaderHandler handler,int requiredWidth, int requiredHeight) {
		this.imageUrl = imageUrl;
		this.handler = handler;
		this.reqHeight = requiredHeight;
		this.reqWidth = requiredWidth;
	}

	/**
	 * Triggers the image loader for the given image and view. The image loading
	 * will be performed concurrently to the UI main thread, using a fixed size
	 * thread pool. The loaded image will be posted back to the given ImageView
	 * upon completion.
	 * 
	 * @param imageUrl
	 *            the URL of the image to download
	 * @param imageView
	 *            the ImageView which should be updated with the new image
	 */
	public static void start(String imageUrl, ImageView imageView) {
		start(imageUrl, imageView, new ImageLoaderHandler(imageView, imageUrl),
				null, null);
	}

	/**
	 * Triggers the image loader for the given image and view and sets a dummy
	 * image while waiting for the download to finish. The image loading will be
	 * performed concurrently to the UI main thread, using a fixed size thread
	 * pool. The loaded image will be posted back to the given ImageView upon
	 * completion.
	 * 
	 * @param imageUrl
	 *            the URL of the image to download
	 * @param imageView
	 *            the ImageView which should be updated with the new image
	 * @param dummyDrawable
	 *            the Drawable set to the ImageView while waiting for the image
	 *            to be downloaded
	 * @param errorDrawable
	 *            the Drawable set to the ImageView if a download error occurs
	 */
	public static void start(String imageUrl, ImageView imageView,Drawable dummyDrawable, Drawable errorDrawable, int reqWidth,
			int reqHeight) {
		start(imageUrl, imageView, new ImageLoaderHandler(imageView, imageUrl,errorDrawable), dummyDrawable, errorDrawable, reqWidth,
				reqHeight);
	}
	
	public static void start(Context context,String imageUrl, ImageView imageView,Drawable dummyDrawable, Drawable errorDrawable, int reqWidth,
			int reqHeight, boolean isRounded) {
		start(imageUrl, imageView, new ImageLoaderHandler(context,imageView, imageUrl,errorDrawable, isRounded), dummyDrawable, errorDrawable, reqWidth,
				reqHeight);
	}
	public static void start(Context context,String imageUrl, ImageView imageView,Drawable dummyDrawable, Drawable errorDrawable, boolean isRounded) {
		start(imageUrl, imageView, new ImageLoaderHandler(context,imageView, imageUrl,errorDrawable, isRounded), dummyDrawable, errorDrawable, 0,
				0);
	}


	public static void start(String imageUrl, ImageView imageView,Drawable dummyDrawable, Drawable errorDrawable) {
		start(imageUrl, imageView, new ImageLoaderHandler(imageView, imageUrl,
				errorDrawable), dummyDrawable, errorDrawable, 0, 0);
	}

	/**
	 * Triggers the image loader for the given image and handler. The image
	 * loading will be performed concurrently to the UI main thread, using a
	 * fixed size thread pool. The loaded image will not be automatically posted
	 * to an ImageView; instead, you can pass a custom
	 * {@link ImageLoaderHandler} and handle the loaded image yourself (e.g.
	 * cache it for later use).
	 * 
	 * @param imageUrl
	 *            the URL of the image to download
	 * @param handler
	 *            the handler which is used to handle the downloaded image
	 */
	public static void start(String imageUrl, ImageLoaderHandler handler) {
		start(imageUrl, handler.getImageView(), handler, null, null);
	}

	/**
	 * Triggers the image loader for the given image and handler. The image
	 * loading will be performed concurrently to the UI main thread, using a
	 * fixed size thread pool. The loaded image will not be automatically posted
	 * to an ImageView; instead, you can pass a custom
	 * {@link ImageLoaderHandler} and handle the loaded image yourself (e.g.
	 * cache it for later use).
	 * 
	 * @param imageUrl
	 *            the URL of the image to download
	 * @param handler
	 *            the handler which is used to handle the downloaded image
	 * @param dummyDrawable
	 *            the Drawable set to the ImageView while waiting for the image
	 *            to be downloaded
	 * @param errorDrawable
	 *            the Drawable set to the ImageView if a download error occurs
	 */
	public static void start(String imageUrl, ImageLoaderHandler handler,Drawable dummyDrawable, Drawable errorDrawable) {
		start(imageUrl, handler.getImageView(), handler, dummyDrawable,	errorDrawable);
	}

	private static void start(String imageUrl, ImageView imageView,ImageLoaderHandler handler, Drawable dummyDrawable,
			Drawable errorDrawable) {
		if (imageView != null) {
			if (imageUrl == null) {
				// In a ListView views are reused, so we must be sure to remove
				// the tag that could
				// have been set to the ImageView to prevent that the wrong
				// image is set.
				imageView.setTag(null);
				/**
				 * Changed by Rupesh Rastogi setImageDrawable to
				 * setBackgroundDrawable
				 */
				//imageView.setImageDrawable(dummyDrawable);
				 imageView.setBackgroundDrawable(dummyDrawable);
				return;
			}
			String oldImageUrl = (String) imageView.getTag();
			if (imageUrl.equals(oldImageUrl)) {
				// nothing to do
				return;
			} else {
				// Set the dummy image while waiting for the actual image to be
				// downloaded.
				/**
				 * Changed by Rupesh Rastogi, setImageDrawable to
				 * setBackgroundDrawable
				 */
				//imageView.setImageDrawable(dummyDrawable);
				 imageView.setBackgroundDrawable(dummyDrawable);
				imageView.setTag(imageUrl);
			}
		}

		if (imageCache.containsKeyInMemory(imageUrl)) {
			// do not go through message passing, handle directly instead
			handler.handleImageLoaded(imageCache.getBitmap(imageUrl), null);
		}
			else if(!mDownloadAttemptsDoneList.contains(imageUrl)){
				executor.execute(new ImageLoader(imageUrl, handler, UNKNOWN,UNKNOWN));
			}
			else imageView.setBackgroundDrawable(errorDrawable); //imageView.setImageDrawable(errorDrawable); 
			
		
	}

	public static void start(String imageUrl, ImageView imageView,ImageLoaderHandler handler, Drawable dummyDrawable,
			Drawable errorDrawable, int reqWidth, int reqHeight) {
		if (imageView != null) {
			if (imageUrl == null) {
				// In a ListView views are reused, so we must be sure to remove
				// the tag that could
				// have been set to the ImageView to prevent that the wrong
				// image is set.
				imageView.setTag(null);
				/**
				 * Changed by Rupesh Rastogi setImageDrawable to
				 * setBackgroundDrawable
				 */
				//imageView.setImageDrawable(dummyDrawable);
				 imageView.setBackgroundDrawable(dummyDrawable);
				return;
			}
			String oldImageUrl = (String) imageView.getTag(); 
			if (imageUrl.equals(oldImageUrl)) {
				// nothing to do
				return;
			} else {
				// Set the dummy image while waiting for the actual image to be
				// downloaded.
				/**
				 * Changed by Rupesh Rastogi, setImageDrawable to
				 * setBackgroundDrawable
				 */
				//imageView.setImageDrawable(dummyDrawable);
				 imageView.setBackgroundDrawable(dummyDrawable);
				imageView.setTag(imageUrl);
			}
		}

		Log.d(LOG_TAG,"imageUrllll"+imageUrl);
		if (imageCache.containsKeyInMemory(imageUrl)) {
			// do not go through message passing, handle directly instead
			handler.handleImageLoaded(imageCache.getBitmap(imageUrl), null);
		} else if(!mDownloadAttemptsDoneList.contains(imageUrl)){
			executor.execute(new ImageLoader(imageUrl, handler, reqWidth,reqHeight));
		}
		else imageView.setBackgroundDrawable(errorDrawable); //imageView.setImageDrawable(errorDrawable);
	}

	/**
	 * Clears the 1st-level cache (in-memory cache). A good candidate for
	 * calling in {@link android.app.Application#onLowMemory()}.
	 */
	public static void clearCache() {
		imageCache.clear();
	}

	/**
	 * Returns the image cache backing this image loader.
	 * 
	 * @return the {@link ImageCache}
	 */
	public static ImageCache getImageCache() {
		return imageCache;
	}

	/**
	 * The job method run on a worker thread. It will first query the image
	 * cache, and on a miss, download the image from the Web.
	 */
	public void run() {
		// TODO: if we had a way to check for in-memory hits, we could improve
		// performance by
		// fetching an image from the in-memory cache on the main thread
		Bitmap bitmap = imageCache.getBitmap(imageUrl);

		if (bitmap == null) {
			bitmap = downloadImage();
			Log.d("***Downloading... ",imageUrl);
		}

		// TODO: gracefully handle this case.
		notifyImageLoaded(imageUrl, bitmap);

	}

	// TODO: we could probably improve performance by re-using connections
	// instead of closing them
	// after each and every download
	protected Bitmap downloadImage() {
		int timesTried = 1;

		while (timesTried <= numRetries) {
			try {
				byte[] imageData = retrieveImageData();

				if (imageData != null) {
					imageCache.put(imageUrl, imageData);
				} else {
					break;
				}

				return BitmapFactory.decodeByteArray(imageData, 0,imageData.length);

			} catch (Throwable e) {
				Log.w(LOG_TAG, "download for  " + imageUrl + "  failed (attempt "+ timesTried + ")");
				e.printStackTrace();
				SystemClock.sleep(DEFAULT_RETRY_HANDLER_SLEEP_TIME);
				timesTried++;
			}
		}
		//mDownloadAttemptsDoneList.add(imageUrl);
		return null;
	}

	protected byte[] retrieveImageData() throws IOException {
		URL url = new URL(imageUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// determine the image size
		int fileSize = connection.getContentLength();
		if (fileSize < 0) {
			return null;
		} // download the file
		Bitmap bitmap = null; //
		if (reqWidth == UNKNOWN || reqHeight == UNKNOWN){
			InputStream inputStream = (InputStream) url.getContent();
			bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
			inputStream.close();
		}
		else {
			reqWidth=(int) (UiUtils.getDeviceResolution(mContext)*reqWidth);
			reqHeight=(int) (UiUtils.getDeviceResolution(mContext)*reqHeight);
			bitmap = ScaledDownBitmapLoader.decodeSampledImageUrl(imageUrl,reqWidth,reqHeight);
		}
		byte[] imageData = null;
		if (bitmap != null) {
			imageData = ScaledDownBitmapLoader.getByteArrayFromBitmap(bitmap);
			bitmap.recycle();
			bitmap=null;
		}
		return imageData;
	}

	public void notifyImageLoaded(String url, Bitmap bitmap) {
		Message message = new Message();
		message.what = HANDLER_MESSAGE_ID;
		Bundle data = new Bundle();
		data.putString(IMAGE_URL_EXTRA, url);
		Bitmap image = bitmap;
		data.putParcelable(BITMAP_EXTRA, image);
		message.setData(data);

		handler.sendMessage(message);
	}
}
