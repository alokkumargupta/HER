package com.her.utils;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;


public class ApplicationUtil {

/*	public static boolean logout(final BaseMainActivity activity){

		try{



			
			 *  Delete user specific Favorite data.
			 *  Also delete Booking History and Notifications.
			 
			if(AppSharedPreference.getString(AppSharedPreference.FB_USER_ID,"", activity).length()>0){
				GCMRegistrar.unregister(activity);
				AppSharedPreference.putString(AppSharedPreference.FB_USER_ID,"", activity);
				final FBLoginManager  fbManager = new FBLoginManager(activity,0,AppConstants.FACEBOOK_APP_ID, AppConstants.permissions);
				new Thread(new Runnable() {
					@Override
					public void run()  {
						try {
							fbManager.logout(null);
						} catch (Throwable e) {

						}
					}
				}).start();
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					new FavoriteTable(activity).deleteAll();
					new StaticContentTable(activity).deleteAll();
					//new UserTable(activity).deleteAll();
					new MoviesTable(activity).deleteAll();
					new DealsTable(activity).deleteAll();
					new CinemaTable(activity).deleteAll();
					new GenreTable(activity).deleteAll();
					new ExclusiveGiftTable(activity).deleteAll();
					new BookingHistoryTable(activity).deleteAll();
					new GiftsDenominationsTable(activity).deleteAll();
					new CinemaRegionCodeTable(activity).deleteAll();
				}
			}).start();
			AppSharedPreference.clearALL(activity);
			Intent intent=new Intent(activity,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(intent);
			activity.finish();
			return true;
		}catch(Throwable e){
			return false;
		}

	}
*/
	/**
	 * Method to fetched Device Id (IMEI on GSM, MEID for CDMA)
	 * @return return whatever string uniquely identifies the device
	 */
	public static String findDeviceID(Context contex)
	{
		String deviceID = "";
		deviceID = ((TelephonyManager) contex.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return deviceID;
	}


	public static String getAppVersion(Context context){
		String appVersion="";
		try {

			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			appVersion=pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appVersion;
	}

	public static String getResolution ( Context activity ) {
		DisplayMetrics _Displaymetrics = activity.getResources().getDisplayMetrics();
		int _ScreenWidth = _Displaymetrics.widthPixels;
		int _ScreenHeight = _Displaymetrics.heightPixels;

		if ( _ScreenWidth <= 240 ) {
			_ScreenWidth = 240;
			_ScreenHeight = 432;
		}
		else if ( _ScreenWidth < 400 ) {
			_ScreenWidth = 320;
			_ScreenHeight = 480;
		}
		else if ( _ScreenWidth > 400 && _ScreenWidth <= 600 ){
			_ScreenWidth = 480;
			_ScreenHeight = 854;
		}
		else{
			_ScreenWidth = 720;
			_ScreenHeight = 1184;
		}
		String res = _ScreenWidth + "_" + _ScreenHeight;

		//		  activity.getSharedPreferences(AppSettings.BUBBLE_MES_PREF, 0).edit().putString(AppSettings.APP_RESOLUTION, res).commit();
		return  res;
	}

	public static String getDensity ( Activity activity ){

		String retVal = "";
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch(metrics.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			retVal = "low";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			retVal = "medium";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			retVal = "high";
			break;
		default:
			retVal = "extra large";
			break;
		}
		return retVal;
	}

	public static String getDensityInNumeric ( Activity activity ){

		String retVal = "";
		DisplayMetrics metrics = new DisplayMetrics();

		Display display = activity.getWindowManager().getDefaultDisplay();
		int _ScreenWidth  = display.getWidth();  // deprecated

		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch(metrics.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			retVal = "1";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			retVal = "2";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			retVal = "3";
			break;
		default:
			retVal = "4";
			break;
		}
		if(_ScreenWidth>= 600){
			return "4";
		}else{
			return retVal;
		}

	}

	public static String getTwoPrecisionDoubleStr(double dbl){

		String returnValue ="";
		String value=dbl+"";
		int index=value.indexOf(46);
		if(index==-1){
			returnValue=value+".00";
		}else{

			String precisionStr=value.substring(index+1);
			if(precisionStr.length()==1)
				returnValue=value+"0";
			else if(precisionStr.length()==2)
				returnValue=value;
			else if(precisionStr.length()>2)
				returnValue=value.substring(0, index)+value.substring(index,index+3);
		}

		return returnValue;
	}
	public static String convertMSToDeviceTime(Context context,long millisec)
	{
		if (millisec == 0) return null;
		String time =null;
		String value = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.TIME_12_24);
		if(null != value && (value.equals("24")))
			time =android.text.format.DateFormat.format("kk:mm",millisec).toString();
		else {time =android.text.format.DateFormat.format("hh:mm aa",millisec).toString(); time = time.toUpperCase();}
		return time;
	}

	public static String getIMEI(Context context){
		String IMEI="";
		String serviceName = Context.TELEPHONY_SERVICE;
		TelephonyManager m_telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
		
		IMEI = m_telephonyManager.getDeviceId();
		return IMEI;
	}
	public static String getOS(Context context){
		String os="";
		
		return os;
	}
	public static String getOSVersion(Context context){
	  return android.os.Build.VERSION.RELEASE; 
	}
	public static String getModel(Context context){
	    return android.os.Build.MODEL;
	}
	public static String getServiceProvider(Context context){
		String serviceName = Context.TELEPHONY_SERVICE;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(serviceName);  
		return tm.getNetworkOperatorName();
		
	}

	public static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	public static boolean checkSpecialChar(String text) {
		return SPECIAL_CHAR_PATTERN.matcher(text).matches();
	}
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
					")+"
			);
	public static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("^((\\+91?)|0|)?[0-9]{10}$");  //�^[0-9]{10}�
	
	public static boolean checkMobileNumber(String mobileNumber) {
		return MOBILE_NUMBER_PATTERN.matcher(mobileNumber).matches();
	}
	public static boolean checkPassword(String password) {
		return password.length()>=6;
	}
	 public static boolean HasSpecialCharacter(String value)
     {
            for (int i = 0; i < value.length(); i++) {
                   if (!(Character.isLetter(value.charAt(i))||Character.isSpace(value.charAt(i))))
                         return true;
            }
            return false;
     }
	 public static boolean HasNotOnlyCharacter(String value)
     {
            for (int i = 0; i < value.length(); i++) {
                   if (!(Character.isLetter(value.charAt(i))||Character.isSpace(value.charAt(i))))
                         return true;
            }
            return false;
     }
	 public static boolean HasNotOnlyCharacterAndIntegers(String value)
     {
            for (int i = 0; i < value.length(); i++) {
                   if (!(Character.isLetter(value.charAt(i))||Character.isDigit(value.charAt(i))||Character.isSpace(value.charAt(i))))
                         return true;
            }
            return false;
     }
	 public static boolean HasNotOnlyNumbers(String value)
     {
            for (int i = 0; i < value.length(); i++) {
            	  if (!(Character.isDigit(value.charAt(i))||Character.isSpace(value.charAt(i))))
                         return true;
            }
            return false;
     }


	public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("^[^!@#=_()?<>%$*-+!/\\.\":;,0123456789]*$");
}
