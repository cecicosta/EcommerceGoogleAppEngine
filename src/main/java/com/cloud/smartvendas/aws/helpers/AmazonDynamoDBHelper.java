package com.cloud.smartvendas.aws.helpers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cloud.smartvendas.aws.helpers.AwsProperties.Properties;

public class AmazonDynamoDBHelper {
	public static <T> void updateItem(T item){
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(Properties.dynamodbregion.getValue())).
				withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		DynamoDBMapper mapper = new DynamoDBMapper(client);

		mapper.save(item); 
	}
	
	public static <T> T RetrieveItem(Class<T> clazz, Object key) {
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(Properties.dynamodbregion.getValue())).
				withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		DynamoDBMapper mapper = new DynamoDBMapper(client);

		return mapper.load(clazz, key);
	}
}
