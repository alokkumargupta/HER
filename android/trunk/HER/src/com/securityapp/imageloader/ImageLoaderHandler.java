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



import com.her.utils.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoaderHandler extends Handler {

    private ImageView imageView;
    private String imageUrl;
    private boolean isRounded;
    private Drawable errorDrawable;
    private Context mContext;

    public ImageLoaderHandler(ImageView imageView, String imageUrl) {
        this.imageView = imageView;
        this.imageUrl = imageUrl;
    }
    public ImageLoaderHandler(Context context,ImageView imageView, String imageUrl,Drawable errorDrawable, boolean isRounded) {
        this.imageView = imageView;
        this.imageUrl = imageUrl;
        this.errorDrawable = errorDrawable;
        this.isRounded = isRounded;
        mContext = context;
    }

    public ImageLoaderHandler(ImageView imageView, String imageUrl,Drawable errorDrawable) {
        this(imageView, imageUrl);
        this.errorDrawable = errorDrawable;
    }

    @Override
    public final void handleMessage(Message msg) {
        if (msg.what == ImageLoader.HANDLER_MESSAGE_ID) {
            handleImageLoadedMessage(msg);
        }
    }

    protected final void handleImageLoadedMessage(Message msg) {
        Bundle data = msg.getData();
        Bitmap bitmap = data.getParcelable(ImageLoader.BITMAP_EXTRA);
        handleImageLoaded(bitmap, msg);
    }

    /**
     * Override this method if you need custom handler logic. Note that this method can actually be
     * called directly for performance reasons, in which case the message will be null
     *
     * @param bitmap
     *            the bitmap returned from the image loader
     * @param msg
     *            the handler message; can be null
     * @return true if the view was updated with the new image, false if it was discarded
     */
    protected boolean handleImageLoaded(Bitmap bitmap, Message msg) {
        // If this handler is used for loading images in a ListAdapter,
        // the thread will set the image only if it's the right position,
        // otherwise it won't do anything.
    	Log.d("handleImageLoaded",imageUrl);
        String forUrl = (String) imageView.getTag();
        if (imageUrl.equals(forUrl)) {
            Bitmap image = bitmap != null || errorDrawable == null ? bitmap: ((BitmapDrawable) errorDrawable).getBitmap();
            if (image != null) {
            	//imageView.setImageResource(0);
            	//imageView.setBackgroundResource(0);
            	
            	/*if(isRounded){
            	imageView.setBackgroundDrawable(new BitmapDrawable(UiUtils.getCroppedBitmap(mContext,image,isRounded)));
            	}else 
                imageView.setBackgroundDrawable(new BitmapDrawable(UiUtils.getCroppedBitmap(mContext,image,isRounded)));*/
                //imageView.setImageDrawable(new BitmapDrawable(image));
                //imageView.setBackgroundDrawable(new BitmapDrawable(UiUtils.getCroppedBitmap(mContext,image,isRounded)));
            	 
            	 imageView.setBackgroundDrawable(new BitmapDrawable(image));
            	
            	image=null;
            	bitmap=null;
            	//System.gc();
                //imageView.setBackgroundDrawable(new BitmapDrawable(image));
                //imageView.setImageBitmap(image);
            }

            return true;
        }

        return false;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
