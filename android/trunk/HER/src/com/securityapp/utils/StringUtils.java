package com.securityapp.utils;

import java.util.regex.Pattern;

/**
 * Utility class useful when dealing with string objects. 
 * This class is a collection of static functions
 * it is not allowed to create instances of this class
 */
public abstract class StringUtils {
	
	public static final String EMAIL_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	/**
	 * @param pStr String object to be tested.
     * @returns true if the given string is null or empty or contains spaces only.
     */
    public static boolean isNullOrEmpty(final String pStr) {
    	return pStr == null || pStr.trim().length() == 0;
    }
    
    /**
     * @param pEmail
     * @return true if pEmail matches with {@link StringUtils#EMAIL_REGEX}, false otherwise
     */
    public static boolean isValidEmail(String pEmail) {
    	Pattern validRegexPattern = Pattern.compile(EMAIL_REGEX);
    	return validRegexPattern.matcher(pEmail).matches();
    }
  
    /**
     * @param pStr
     * @param startIndex
     * @param pEndIndex
     * @return int value, parsed from a substring of pStr 
     */
    public static int parseInt(String pStr, int startIndex, int pEndIndex) {
    	if( pStr == null || pStr.length() < pEndIndex ) {
    		return 0;
    	}
    	try { return Integer.parseInt(pStr.substring(startIndex, pEndIndex)); }
    	catch(Exception e) { e.printStackTrace(); return 0; }
    }
}
