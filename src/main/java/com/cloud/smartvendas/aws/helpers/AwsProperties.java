package com.cloud.smartvendas.aws.helpers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.amazonaws.auth.AWS3Signer;

public class AwsProperties {
	public enum Properties{
		secretKey("secretkey"),
		accessKey("accesskey"),
		s3Url("s3url"),
		s3Bucket("s3bucket"),
		s3Region("s3region"),
		rdsUrl("rdsurl"),
		rdsUserName("rdsusername"),
		rdsPassword("rdspassword"),
		rdsDatabase("rdsdatabase"),
		rdsRegion("rdsregion"),
		sesRegion("sesregion"),
		dynamodbregion("dynamodbregion");
	
		public final String identifier;
		
		Properties(final String name) {			
			this.identifier = name;
		}
		public String getValue(){
			initiate();
			return values.get(identifier);
		}
	}
	
	private static Map<String, String> values = new HashMap<String, String>();
	private static AwsProperties instance = null;

	private AwsProperties(){
		parseFile();
	}
	
	private void parseFile() {		
		BufferedReader br = null;		
		try {
			Resource resource = new ClassPathResource("awsproperties.cfg");
			File file = resource.getFile();
						
			br = new BufferedReader(new FileReader(file));
		    String line = br.readLine();

		    while (line != null) {
		    	String[] property = line.split("=");
		        values.put(property[0], property[1]);
		    	line = br.readLine();
		    }
		}catch (Exception e){
			System.out.println("Error trying to read file awsproperties.cfg - message: " + e.getMessage());
		}finally {
		    try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	private static AwsProperties initiate(){
		if(instance == null)
			instance = new AwsProperties();
		
		return instance;
	}
}
