package com.cloud.smartvendas.aws.helpers;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.cloud.smartvendas.aws.helpers.AwsProperties.Properties;



public class AmazonS3ServiceHelper{
	
	private String NomeBucketS3;
	private String directoryPathS3;
	
	public AmazonS3ServiceHelper() {
		this.NomeBucketS3 = Properties.s3Bucket.getValue();
		this.directoryPathS3 = "";
	}

	public void sendFile(String nomeArquivo, String pathArquivo, boolean makePublic) {
        sendFile(nomeArquivo, new File(pathArquivo), makePublic);
	}
		
	public void sendFile(String nomeArquivo, File arquivo, boolean makePublic) {
		
		if(arquivo == null || !arquivo.exists() || arquivo.length() <= 0){
			System.out.println("Falha ao enviar arquivo para AWS S3. Arquivo inexistente: " + 
        		arquivo.getAbsolutePath());
			return;
		}
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
       
        try {
        	s3.putObject(getBucketName(),this.getFilePath(nomeArquivo), arquivo);
            if(makePublic)
            	s3.setObjectAcl(getBucketName(), getFilePath(nomeArquivo), CannedAccessControlList.PublicRead);
            System.out.println("Arquivo '" + getFilePath(nomeArquivo) + "' enviado com sucesso para " + 
					getDirectoryPath() + getBucketName());
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao enviar arquivo para AWS S3. Path: " + 
        		(getBucketName() + "/" + this.getFilePath(nomeArquivo)));
            System.out.println("[AWS] " + e.getMessage());
        }
	}
	
	public boolean fileExists(String fileName) {
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        System.out.println("Verificando existenia do arquivo '" + fileName + "' em " + this.getDirectoryPath());
        try {
        	ObjectMetadata omd = s3.getObjectMetadata(fileName, this.getDirectoryPath());
        	if(omd != null && omd.getContentLength() > 0){
        		return true;
        	}
        }catch (AmazonServiceException e) {
            System.out.println("Falha ao verificar existência do arquivo no AWS S3. Path: " + 
        		(getBucketName() + "/" + getFilePath(fileName)));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
        return false;
	}
	
	public void deleteFile(String nomeArquivo)
			{
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        System.out.println("Excluindo arquivo '" + nomeArquivo + "' em " + this.getDirectoryPath());
        try {
            s3.deleteObject(this.getBucketName(), this.getFilePath(nomeArquivo));
            System.out.println("Arquivo '" +this.getFilePath(nomeArquivo) + "' excluido com sucesso.");
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao excluir arquivo no AWS S3. Path: " + 
        		(this.getBucketName() + "/" + this.getFilePath(nomeArquivo)));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
		
	}
	
	public S3Object retrieveFile(String nomeArquivo)
	{
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        System.out.println("Recuperando arquivo '" + nomeArquivo + "' em " + this.getDirectoryPath());
        try {
        	S3Object arquivo = s3.getObject(this.getBucketName(), this.getFilePath(nomeArquivo));
            System.out.println("Arquivo '" + nomeArquivo + "' recuperado com sucesso.");
            return arquivo;
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao recuperar arquivo no AWS S3. Path: " + 
        		(this.getBucketName() + "/" + this.getDirectoryPath() + nomeArquivo));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
        
		return null;
	}
	
	
	public ObjectListing listFiles() {

		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
		
		System.out.println("Recuperando arquivos em " + this.getDirectoryPath());
		try {
			ObjectListing arquivos = s3.listObjects(this.getBucketName(), this.getDirectoryPath());
		    System.out.println("Arquivos recuperados com sucesso.");
		    return arquivos;
		}
		catch (AmazonServiceException e) {
		    System.out.println("Falha ao recuperar arquivos no AWS S3. Path: " + 
				(this.getBucketName() + "/" + this.getDirectoryPath()));
		    System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
		    System.out.println("[AWS] " + e.getStackTrace().toString());
		}
		
		return null;
	}
		
	
	@Deprecated
	public void createDirectory(String nomeDiretorio) {
		
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        try {
        	 // Montando metadados para criação de diretório
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0);

            // Arquivo vazio
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

            // Criando requisição: nome do diretório seguido de barra (padrão S3)
            PutObjectRequest putObjectRequest = new PutObjectRequest(
        		this.getBucketName(),
                nomeDiretorio + "/", emptyContent, metadata);

            // Submentendo o pedido de criação do diretorio
            s3.putObject(putObjectRequest);
            System.out.println("Diretorio '" + nomeDiretorio + "' criado.'");
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao criar diretorio no AWS S3. Diret�rio: " + 
            		nomeDiretorio + ".\n" + e.getErrorMessage());
        }
		
	}

	public String getBucketName() {
		return NomeBucketS3;
	}

	public void setBucketName(String nomeBucketS3) {
		NomeBucketS3 = nomeBucketS3;
	}

	public String getDirectoryPath() {
		return nullOrEmpty() ? "" : this.directoryPathS3;
	}
	
	public void setDirectoryPath(String diretorioPathS3) {
		this.directoryPathS3 = diretorioPathS3 + "/";
	}

	public String getFilePath(String nomeArquivo) {
		return (nullOrEmpty() ? "" : this.directoryPathS3) + nomeArquivo;
	}
	
	public URL getFileURL(String fileName){
		BasicAWSCredentials creds = new BasicAWSCredentials(Properties.accessKey.getValue(), Properties.secretKey.getValue());
		EndpointConfiguration confg = new EndpointConfiguration(Properties.s3Url.getValue(), Properties.s3Region.getValue());
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
		
		return s3.getUrl(NomeBucketS3, getDirectoryPath() + fileName);
	}
	
	private boolean nullOrEmpty(){
		return this.directoryPathS3 == null || this.directoryPathS3.length() == 0;
	}
}
	
