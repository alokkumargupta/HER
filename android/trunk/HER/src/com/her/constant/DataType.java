package com.her.constant;

import com.securityapp.controller.IController;
import com.securityapp.network.HttpClientConnection;
import com.securityapp.network.Response;
import com.securityapp.network.ServiceRequest;
import com.securityapp.ui.IScreen;

/**
 * Constant values that are 
 * 1. Sent in {@link IController#getData(int, Object)} by {@link IScreen}
 * 2. Set in {@link ServiceRequest} by {@link IController#getData(int, Object)}
 * 3. Set in {@link Response} by {@link HttpClientConnection}
 * 4. Used in {@link IScreen#handleUiUpdate(Response)}
 */
public class DataType {

	public static final int REGISTER	= 0;
	public static final int LOGIN	= 1;
	
	
	
	
	//---------Album Services-----------------------------//
	public static final int ALBUM_SERVICE	= 100;
	public static final int GET_ALBUM_BY_EVENT_ID	= ALBUM_SERVICE + 2;
	public static final int GET_ALBUMS	= ALBUM_SERVICE + 1;
	public static final int GET_ALBUM_NAME	= ALBUM_SERVICE + 3;
	public static final int UPDATE_ALBUM	= ALBUM_SERVICE + 4;
	public static final int ADD_ALBUM	= ALBUM_SERVICE + 5;
	public static final int DELETE_ALBUM	= ALBUM_SERVICE + 6;
	public static final int GET_PHOTO_BY_ALBUM_ID	= ALBUM_SERVICE + 7;
	public static final int GET_PHOTO_BY_EVENT_ID	= ALBUM_SERVICE + 8;
	public static final int UPLOAD_PHOTO	= ALBUM_SERVICE + 9;
	public static final int SET_COVER_PHOTO	= ALBUM_SERVICE + 10;
	public static final int DELETE_PHOTO	= ALBUM_SERVICE + 11;
	
	
	
	
	

}

