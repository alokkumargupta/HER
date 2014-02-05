package com.securityapp.compatibility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Application is compiled with minSdkVersion API.
 * So methods/fields added in newer versions are not available at compile time.
 * But as these are available in devices with newer platforms versions,
 * this class uses reflection to allow invocation of few such methods of View class. 
 */
public abstract class ViewCompatibility {
	
	private static final String TAG = "ViewCompatibility";
	
	/**
	 * Method-names
	 */
	public interface Methods {
		String SET_LAYER_TYPE = "setLayerType";
	}
	
	/*
	 * Following interface and methods are for {@link View#setLayerType()}
	 * It is introduced in Honeycomb(API-11).
	 */
	
	/**
	 * This interface has string equivalents for {@link View#LAYER_TYPE_xxx} int constants. 
	 */ 
	public interface LayerType {
		String SOFTWARE = "LAYER_TYPE_SOFTWARE";
		String HARDWARE = "LAYER_TYPE_HARDWARE";
		String NONE 	= "LAYER_TYPE_NONE";
	}
    
    /**
     * This method will first convert string pLayerType to its int equivalent,
     * and then invoke setLayerType(int) on pTargetView.
     * @param pTargetView
     * @param pLayerType 
     * Currently only {@link ViewCompatibility.LayerType#SOFTWARE} is supported.
     * Possible values are-
     * <ul>
     * <li>{@link ViewCompatibility.LayerType#SOFTWARE}</li>
     * <li>{@link ViewCompatibility.LayerType#HARDWARE}</li>
     * <li>{@link ViewCompatibility.LayerType#NONE}</li>
     * </ul>
     * @param pPaint
     */
    public static void setLayerType(View pTargetView, String pLayerType, Paint pPaint) {
    	if( Build.VERSION.SDK_INT < 11 ) {
    		return;
    	}
    	Field fieldLayerType = ReflectionUtils.getField(View.class,pLayerType);
    	Method methodSetLayerType = ReflectionUtils.getMethod(View.class, Methods.SET_LAYER_TYPE, int.class, Paint.class);
    	try {
    		int layerType = fieldLayerType.getInt(pTargetView);
    		methodSetLayerType.invoke(pTargetView, layerType, pPaint);
    	} catch (Exception e) {
    		Log.e( TAG, Methods.SET_LAYER_TYPE + "(String): " + e );
    	}
    }
}
