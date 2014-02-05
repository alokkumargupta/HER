package com.securityapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * Utility class for UI related methods.
 */
public class UiUtils {
	
	/**
	 * @param pView
	 * @param pWidth
	 * @param pHeight
	 * @return
	 */
	public static Bitmap getBitmapFromView( View pView, int pWidth, int pHeight ) {
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth,MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.EXACTLY);
		
		pView.measure(widthMeasureSpec,heightMeasureSpec);
		pView.layout(0, 0, pView.getMeasuredWidth(), pView.getMeasuredHeight());
		/*
		Bitmap bitmap = Bitmap.createBitmap( pView.getWidth(), pView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		pView.draw(canvas); 
		return bitmap;
		*/
		pView.setDrawingCacheEnabled(true);
		return pView.getDrawingCache();
	}

	/**
	 * @param event
	 * @return
	 */
	public static String getEventDetails( MotionEvent event ) {
		String action = "";
		switch( event.getAction() ) {
		case MotionEvent.ACTION_DOWN: action = "ACTION_DOWN"; break;
		case MotionEvent.ACTION_UP: action = "ACTION_UP"; break;
		case MotionEvent.ACTION_MOVE: action = "ACTION_MOVE"; break;
		case MotionEvent.ACTION_CANCEL: action = "ACTION_CANCEL"; break;
		default: action = "ACTION_" + event.getAction(); break;
		}
		return action + ": " + event.getX() + "-" + event.getY();
	}
	
	/**
	 * @param event
	 * @return
	 */
	public static float getPixels( Context context, float dp ) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dp * dm.density;
	}
	

}
