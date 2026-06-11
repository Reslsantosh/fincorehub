package com.resustainability.fincorehub.commons;

public class StringUtils {
	
	 private StringUtils() {}

	    public static boolean isBlank(String str) {
	        return null == str || str.isBlank();
	    }

	    public static boolean isNotBlank(String str) {
	        return !isBlank(str);
	    }
	    
	    public static boolean isEmpty(CharSequence cs) {
	        return cs == null || cs.isEmpty();
	    }

	    public static boolean isNumeric(CharSequence cs) {
	        if (isEmpty(cs)) {
	            return false;
	        } else {
	            int sz = cs.length();

	            for(int i = 0; i < sz; ++i) {
	                if (!Character.isDigit(cs.charAt(i))) {
	                    return false;
	                }
	            }

	            return true;
	        }
	    }

	    public static int length(CharSequence cs) {
	        return cs == null ? 0 : cs.length();
	    }

	    public static String capitalize(String str) {
	        int strLen = length(str);
	        if (strLen == 0) {
	            return str;
	        } else {
	            int firstCodepoint = str.codePointAt(0);
	            int newCodePoint = Character.toTitleCase(firstCodepoint);
	            if (firstCodepoint == newCodePoint) {
	                return str;
	            } else {
	                int[] newCodePoints = new int[strLen];
	                int outOffset = 0;
	                newCodePoints[outOffset++] = newCodePoint;

	                int codePoint;
	                for(int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; inOffset += Character.charCount(codePoint)) {
	                    codePoint = str.codePointAt(inOffset);
	                    newCodePoints[outOffset++] = codePoint;
	                }

	                return new String(newCodePoints, 0, outOffset);
	            }
	        }
	    }

}
