package com.cloud.smartvendas.nosql.entities;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Log")
public class Log {
    
    private long id;
    private String user;
    private String operation; 
    private String affectedType;
    private String affectedName;
    private String time;
    
	public enum Operations{
		insert("insert"),
		delete("delete"),
		alter("alter"),
		view("view"),
		search("search"),
		list("list");

		public final String identifier;
		Operations(final String name) {			
			this.identifier = name;
		}
	}
	
	public enum AffectedType{
		user("user"),
		product("product");
		
		public final String identifier;
		AffectedType(final String name) {			
			this.identifier = name;
		}
	}
	
	public Log(){}
	
	public Log(String user, Operations operation, AffectedType affectedType, String affectedName){
		this.user = user;
		this.operation = operation.identifier;
		this.affectedType = affectedType.identifier;
		this.affectedName = affectedName;
		this.id = generateId();
		this.time = getCurrentTime();
	}
    
	@DynamoDBHashKey(attributeName="id")  
    public long getId() { return id; }
    public void setId(long id) {this.id = id; }
   
    @DynamoDBAttribute(attributeName="operation")  
    public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }
	
	@DynamoDBAttribute(attributeName="user_name")  
	public String getUser() { return user; }
	public void setUser(String user) { this.user = user; }
	
	@DynamoDBAttribute(attributeName="affected_type")  
	public String getAffectedType() { return affectedType; }
	public void setAffectedType(String affectedType) { this.affectedType = affectedType; }

	@DynamoDBAttribute(attributeName="affected_name")  
	public String getAffectedName() { return affectedName; }
	public void setAffectedName(String affectedName) { this.affectedName = affectedName; }
	
	@DynamoDBRangeKey(attributeName="date_time")  
	public String getTime() { return time; }
	public void setTime(String time) { this.time = time; }
	
	@DynamoDBIgnore
	public static Long generateId(){
		return System.currentTimeMillis();
	}

	@DynamoDBIgnore
	public static String getCurrentTime() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).toString();
	}
	
	@DynamoDBIgnore
	@Override
	public String toString(){
		return "id:" + id 
				+ " user:" + user 
				+ " operation:" + operation 
				+ " affectedType:" + affectedType 
				+ " affectedName:" + affectedName 
				+ " time:" + time;
	}
}