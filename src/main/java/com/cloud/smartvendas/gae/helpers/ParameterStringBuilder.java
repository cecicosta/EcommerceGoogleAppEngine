package com.cloud.smartvendas.gae.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public class ParameterStringBuilder {
    public static String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
    
    public static String getParamsStrings(Map<String, String[]> params) 
    	      throws UnsupportedEncodingException{
    	        StringBuilder result = new StringBuilder();
    	 
    	        for (Entry<String, String[]> entry : params.entrySet()) {
    	          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
    	          result.append("=");
    	          result.append(URLEncoder.encode(Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", ""), "UTF-8"));
    	          result.append("&");
    	        }
    	 
    	        String resultString = result.toString();
    	        return resultString.length() > 0
    	          ? resultString.substring(0, resultString.length() - 1)
    	          : resultString;
    	    }
}
