package com.cloud.smartvendas.aws.helpers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import com.cloud.smartvendas.aws.helpers.AwsProperties.Properties;

public class AmazonSESServiceHelper {
	
	
	public static void SendEmail(String to, String from, String subject, String htmlbody, String textbody){
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonSimpleEmailService client = 
		          AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Properties.sesRegion.getValue()).
		          withCredentials(new AWSStaticCredentialsProvider(creds)).build();
		
		try{
		SendEmailRequest request = new SendEmailRequest()
		          .withDestination(
		              new Destination().withToAddresses(to))
		          .withMessage(new Message()
		              .withBody(new Body()
		                  .withHtml(new Content()
		                      .withCharset("UTF-8").withData(htmlbody))
		                  .withText(new Content()
		                      .withCharset("UTF-8").withData(textbody)))
		              .withSubject(new Content()
		                  .withCharset("UTF-8").withData(subject)))
		          .withSource(from);
		client.sendEmail(request);
		}catch(Exception e){
			System.out.println("The email was not sent. Error message: " + e.getMessage());
			try{
				SendEmailRequest request = new SendEmailRequest().withDestination(
			              new Destination().withToAddresses(from))
			          .withMessage(new Message()
			              .withBody(new Body()
			                  .withText(new Content()
			                      .withCharset("UTF-8").withData("The email was not sent. Error message: " + e.getMessage())))
			              .withSubject(new Content()
			                  .withCharset("UTF-8").withData("Err: " + subject)))
			          .withSource(from);
				client.sendEmail(request);
			}catch(Exception e2){
				System.out.println("The email was not sent. Error message: " + e2.getMessage());
			}
		}
	}
	
	public static void SendEmailVerification(String email) {
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		AmazonSimpleEmailService client = 
		          AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.fromName(Properties.sesRegion.getValue())).
		          withCredentials(new AWSStaticCredentialsProvider(creds)).build();
		
		client.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(email));
	}
}
