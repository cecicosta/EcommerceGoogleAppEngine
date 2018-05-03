package com.cloud.smartvendas.gae.helpers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.amazonaws.http.HttpResponse;

public class HttpRequestUtils {
	public static final String SERVER_URL = "";
	public static int lastRequestStatus = HttpURLConnection.HTTP_OK;
	
	
	public static String httpPOST(String urlAddress, Map<String, String> properties, byte[] payload) throws Exception {
        URL url = new URL(SERVER_URL + urlAddress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
   
		if(properties != null){
			for (String key : properties.keySet()) {
				connection.addRequestProperty(key, properties.get(key));
			}
		}
        
        connection.setRequestMethod("POST");
		connection.setConnectTimeout(50000);
		connection.setReadTimeout(50000);
		connection.setDoOutput(true);
		connection.setDoInput(true);
        
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.write(payload);
		outputStream.flush();
		outputStream.close();
        
        connection.connect();
        
        System.err.println(connection.getResponseCode());
        lastRequestStatus = connection.getResponseCode();
        
        String response = getResponse(connection);
        connection.disconnect();
        return response;
    }
	
	public static String httpPOST(String urlAddress, Map<String, String> properties, String payload) throws Exception {
        URL url = new URL(SERVER_URL + urlAddress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
   
		if(properties != null){
			for (String key : properties.keySet()) {
				connection.addRequestProperty(key, properties.get(key));
			}
		}
        
        connection.setRequestMethod("POST");
		connection.setConnectTimeout(50000);
		connection.setReadTimeout(50000);
		connection.setDoOutput(true);
		connection.setDoInput(true);
        
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(payload);
		outputStream.flush();
		outputStream.close();
        
        connection.connect();
        
        System.err.println(connection.getResponseCode());
        lastRequestStatus = connection.getResponseCode();
        
        String response = getResponse(connection);
        connection.disconnect();
        return response;
    }
	
	public static String httpPOST(String urlAddress, Map<String, String> properties, 
			Map<String, String> parameters) throws IOException{
		
		URL url = new URL(SERVER_URL + urlAddress);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		if(properties != null){
			for (String key : properties.keySet()) {
				connection.addRequestProperty(key, properties.get(key));
			}
		}
		connection.setRequestMethod("POST");		
		connection.setConnectTimeout(50000);
		connection.setReadTimeout(50000);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		addParametersPOST(connection, parameters);

		connection.connect();
		
		String responseBody = getResponse(connection);		
		lastRequestStatus = connection.getResponseCode();
		
		connection.disconnect();
		return responseBody;
	}
	
	public static final String httpGET(String urlAddress, Map<String, String> properties, 
			Map<String, String> parameters) throws IOException{
		if(parameters != null){
			urlAddress += "?" + ParameterStringBuilder.getParamsString(parameters);
		}
		
		URL url = new URL(SERVER_URL + urlAddress);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");		
		connection.setConnectTimeout(50000);
		connection.setReadTimeout(50000);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		if(properties != null){
			for (String key : properties.keySet()) {
				connection.addRequestProperty(key, properties.get(key));
			}
		}

		connection.connect();
		
		System.out.println(connection.getResponseCode());
		lastRequestStatus = connection.getResponseCode();
		
		String responseBody = getResponse(connection);
		connection.disconnect();
		return responseBody;
	}
	
	private static final void addParametersPOST(HttpURLConnection connection, Map<String, String> parameters)
			throws IOException, UnsupportedEncodingException {
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		outputStream.flush();
		outputStream.close();
	}

	private static final String getResponse(HttpURLConnection connection) throws IOException {
		String responseBody = "";
		int status = connection.getResponseCode();
		if(status == HttpURLConnection.HTTP_OK){
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();
			responseBody = content.toString();
		}
		return responseBody;
	}

	
}