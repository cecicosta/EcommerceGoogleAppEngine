package com.cloud.smartvendas.nosql.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Log")
public class Log {
    
    private Long id;
    private String user;
    private String operation; 
    private String affectedType; //USER || PRODUCT
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
    
	@DynamoDBHashKey(attributeName="Id")  
    public long getId() { return id; }
    public void setId(Long id) {this.id = id; }
   
    @DynamoDBAttribute(attributeName="Operation")  
    public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }
	
	@DynamoDBAttribute(attributeName="User")  
	public String getUser() { return user; }
	public void setUser(String user) { this.user = user; }
	
	@DynamoDBAttribute(attributeName="AffectedType")  
	public String getAffectedType() { return affectedType; }
	public void setAffectedType(String affectedType) { this.affectedType = affectedType; }

	@DynamoDBAttribute(attributeName="AffectedName")  
	public String getAffectedName() { return affectedName; }
	public void setAffectedName(String affectedName) { this.affectedName = affectedName; }
	
	@DynamoDBAttribute(attributeName="Time")  
	public String getTime() { return time; }
	public void setTime(String time) { this.time = time; }
}