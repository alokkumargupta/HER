package com.securityapp.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.securityapp.database.BaseDbHelper;

/**
* This class holds some application-global instances.
*/
public class BaseApplication extends Application
{
	private final String LOG_TAG = "BaseApplication";
	
	private SQLiteDatabase mWritableDatabase;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i( LOG_TAG, "onCreate()");
	}

	/**
	 * Get the database instance.
	 * @return mWritableDatabase
	 */
	public SQLiteDatabase getWritableDbInstance() {
		if( mWritableDatabase == null ) {
			BaseDbHelper dbHelper = new BaseDbHelper(this);
			mWritableDatabase = dbHelper.getWritableDatabase();
		}
		return mWritableDatabase;
	}

	@Override
	public void onTerminate() {
		Log.i( LOG_TAG, "onTerminate()");
		if( mWritableDatabase != null ) {
			mWritableDatabase.close();
		}
		super.onTerminate();
	}
}
