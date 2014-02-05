/**
 * 
 */
package com.her.constant;

import org.apache.http.protocol.HTTP;

/**
 * @author alok.gupta
 *
 */
public class WebServiceConstants {
	/**
	 * Base URLs
	 */

	
	//private static final String		BASE_URL_STAGING	= "http://192.168.12.207:84/UserService.svc";
	//private static final String		BASE_URL_STAGING_WEDDING_SERVICES   = "http://192.168.12.207:84/WeddingService.svc";
	
	public static final String USER_SERVICES_CONST = "/UserService.svc";
	public static final String WEDDING_SERVICES_CONST = "/WeddingService.svc";
	public static final String WEDDING_EVENT_TODO = "/WeddingEventToDoService.svc";
	public static final String WEDDING_ALBUM_SERVICE = "/CarrieWildesAlbumService.svc";
	
	private static final String		BASE_URL_QA		= "http://herapp.pdotsolutions.com/HerAppService.svc/";

	private static final String		BASE_URL_PRODUCTION		= "";

	private static final String		BASE_URL				= BASE_URL_QA;

	/**
	 * WS header names and arrays
	 */
	public static final String		WS_HEADER_NAME_X_TOKEN	= "X-Token";

	public static final String[]	WS_HEADERS_ARRAY_AUTH	= { WS_HEADER_NAME_X_TOKEN };

	/**
	 * WebService URLs
	 */
	public static final String		WS_URL_LOGIN			= BASE_URL;
	
	
	
	/**
	 * HTTP header names and values for new web-services - used in SendQueryController
	 */
	public static final String		WS_HEADER_NAME_USERCODE	= "USERCODE";
	
	public static final String[] WS_HEADER_NAMES = 
		{ "useragent", HTTP.CONTENT_TYPE, "Accept"};
	
	public static final String[] WS_HEADER_NAMES_WEDDING = 
		{ "useragent", HTTP.CONTENT_TYPE, "Accept",WS_HEADER_NAME_USERCODE};
	
	/**
	 * WS header names and arrays
	 */
/*	public static final String		WS_HEADER_NAME_X_TOKEN	= "X-Token";

	public static final String[]	WS_HEADERS_ARRAY_AUTH	= { WS_HEADER_NAME_X_TOKEN };*/
	
	public static final String CONTENT_TYPE_JSON = "application/json";
	
	public static final String[] WS_HEADER_VALUES = { "mobile", CONTENT_TYPE_JSON, CONTENT_TYPE_JSON ,""};
	
	public static String USERCODE;
}
