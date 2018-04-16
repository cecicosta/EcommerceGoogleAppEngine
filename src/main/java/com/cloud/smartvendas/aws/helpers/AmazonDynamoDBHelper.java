package com.cloud.smartvendas.aws.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.RangeKeyCondition;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cloud.smartvendas.aws.helpers.AwsProperties.Properties;

public class AmazonDynamoDBHelper {
	public static <T> void updateItem(T item){
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(Properties.dynamodbregion.getValue())).
				withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		DynamoDBMapper mapper = new DynamoDBMapper(client);

		mapper.save(item); 
	}
	
	public static <T> T retrieveItem(Class<T> clazz, Object key) {
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(Properties.dynamodbregion.getValue())).
				withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		DynamoDBMapper mapper = new DynamoDBMapper(client);

		return mapper.load(clazz, key);
	}
	
	public static <T> List<T> findRange(Class<T> clazz, String parameter, int maxLimit, boolean forward) {
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(Properties.dynamodbregion.getValue())).
				withCredentials(new AWSStaticCredentialsProvider(creds)).build();

		DynamoDBMapper mapper = new DynamoDBMapper(client);
		
		HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":v1", new AttributeValue().withS(parameter));
		       
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		    .withFilterExpression("date_time < :v1")
		    .withExpressionAttributeValues(eav);

		List<T> replies =  mapper.scan(clazz, scanExpression);

	    return replies;
	}
	
	public static <T> List<T> findAll(Class<T> clazz, String value, boolean forward) throws IOException {
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(Properties.dynamodbregion.getValue())).
				withCredentials(new AWSStaticCredentialsProvider(creds)).build();
				
		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable("Log");

		if(table == null)
			return null;
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<T> objectItems = new ArrayList<T>();
		QuerySpec spec = new QuerySpec()
			    .withKeyConditionExpression("Id = :v_id and Time < :v_reply_dt_tm")
			    .withFilterExpression("PostedBy = :v_posted_by")
			    .withValueMap(new ValueMap()
			        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1")
			        .withString(":v_reply_dt_tm", value))
			    .withConsistentRead(true);

			ItemCollection<QueryOutcome> items = table.query(spec);

			Iterator<Item> iterator = items.iterator();
			while (iterator.hasNext()) {
			    try {
			    	Item item = iterator.next();
					objectItems.add(mapper.readValue(item.toJSON(), clazz));
				} catch (IOException e) {
					throw e;
				}
			}
		return objectItems;
	}
}
