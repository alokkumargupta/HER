package com.securityapp.database;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.securityapp.application.BaseApplication;
import com.securityapp.model.IModel;

/**
 * 
 */
public abstract class BaseTable
{
	protected SQLiteDatabase mWritableDatabase;
	protected String		 mTableName;

	/**
	 * Get the global instance of the SQLiteDatabase.
	 * @param callerActivity
	 */
	public BaseTable( BaseApplication pBaseApplication, String pTableName ) {
		mWritableDatabase = pBaseApplication.getWritableDbInstance();
		mTableName=pTableName;
	}

	/**
	 * @return the pPrimaryKey of newly inserted row
	 */
	public abstract Object insertData( IModel pModel );
	
	/**
	 * @return true if row with pPrimaryKey is found and deleted
	 */
	public abstract boolean deleteData( Object pPrimaryKey );
	
	/**
	 * @return array list of all data in table
	 */
	public abstract ArrayList<IModel> getAllData();
	
	/**
	 * @return the number of rows deleted
	 */
	public int deleteAll() {
		try { return mWritableDatabase.delete(mTableName, "1", null); }
		catch( Exception e ) { Log.e( mTableName, "" + e ); return 0; }
	}
	
	/**
	 * @return count of total rows in table, -1 in case of any exception.
	 */
	public int getCount() {
		String columnName = "rowsCount";
		String query =  "select count(*) as " + columnName + "  from " + mTableName;
		int rowsCount = -1;
		Cursor cursor = null;
		try { 
			cursor = mWritableDatabase.rawQuery( query, null);
			if( cursor.moveToNext() ) {
				rowsCount = cursor.getInt(cursor.getColumnIndex(columnName));
			}
		} catch( Exception e ) { Log.e( mTableName, "" + e ); }
		finally { closeCursor(cursor); }
		return rowsCount;
	}
	
	/**
	 * Closes the pCursor.
	 * @param pCursor
	 */
	protected final void closeCursor( Cursor pCursor ) {
		if( pCursor != null && ! pCursor.isClosed() ) pCursor.close();
	}
}