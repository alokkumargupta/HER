package com.securityapp.compatibility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * Application is compiled with minSdkVersion API.
 * So methods/fields added in newer versions are not available at compile time.
 * But as these are available in devices with newer platforms versions,
 * this class uses reflection to check and access such methods/fields. 
 */
public abstract class ReflectionUtils {
	
	private static final String TAG = "ReflectionUtils";
	
	/**
	 * This method searches for {@link Method} in any class by method-name and arg-types.
	 * @param pTargetClass
	 * @param pMethodName
	 * @param pArgTypes
	 * @return object of required {@link Method} or <code>null</code>
	 */
	public static Method getMethod(Class<?> pTargetClass, String pMethodName, Class<?>... pArgTypes) {
        try {
           return pTargetClass.getMethod(pMethodName, pArgTypes);
        } catch (NoSuchMethodException e) {
        	Log.e( TAG, "getMethod("+pMethodName+"): " + e );
        } catch (Exception e) {
        	Log.e( TAG, "getField("+pMethodName+"): " + e );
        }
        return null;
	}
	
	/**
	 * This method searches for {@link Field} in any class by field-name.
	 * @param pTargetClass
	 * @param pFieldName
	 * @return object of required {@link Field} or <code>null</code>
	 */
	public static Field getField(Class<?> pTargetClass, String pFieldName ) {
        try {
           return pTargetClass.getField(pFieldName);
        } catch (NoSuchFieldException e) {
        	Log.e( TAG, "getField("+pFieldName+"): " + e );
        } catch (Exception e) {
        	Log.e( TAG, "getField("+pFieldName+"): " + e );
        }
        return null;
	}
}
