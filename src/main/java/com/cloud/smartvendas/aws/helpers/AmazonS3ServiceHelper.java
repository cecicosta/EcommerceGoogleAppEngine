package com.cloud.smartvendas.aws.helpers;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;



public class AmazonS3ServiceHelper{
	
	private String NomeBucketS3;
	private String diretorioPathS3;
	
	//TODO: Turn the secret key, the access key, the Endpoint and bucket region into configuration parameters
	
	public void enviarArquivoS3(String nomeArquivo, String pathArquivo)
			{
        enviarArquivoS3(nomeArquivo, new File(pathArquivo));
	}
	
	public void enviarArquivoS3(String nomeArquivo, File arquivo)
			{
		
		if(arquivo == null || !arquivo.exists() || arquivo.length() <= 0){
			System.out.println("Falha ao enviar arquivo para AWS S3. Arquivo inexistente: " + 
        		arquivo.getAbsolutePath());
			return;
		}
		
		BasicAWSCredentials creds = new BasicAWSCredentials("accesskey", "secretkey");
		EndpointConfiguration confg = new EndpointConfiguration("https://s3-sa-east-1.amazonaws.com", "sa-east-1");
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
       
        try {
        	System.out.println("Arquivo '" + this.getDiretorioPathDoAquivoS3(nomeArquivo) + "' enviado com sucesso para " + 
            		(this.getDiretorioPathS3() != null && this.getDiretorioPathS3().length() > 0 ? this.getDiretorioPathS3() : " a raiz do Bucket " + 
            	    		this.getNomeBucketS3()));
            s3.putObject(this.getNomeBucketS3(),this.getDiretorioPathDoAquivoS3(nomeArquivo), arquivo);
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao enviar arquivo para AWS S3. Path: " + 
        		(this.getNomeBucketS3() + "/" + this.getDiretorioPathDoAquivoS3(nomeArquivo)));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
		
	}
	
	public boolean verificarArquivoS3(String nomeArquivo)
		{
		
		BasicAWSCredentials creds = new BasicAWSCredentials("accesskey", "secretkey");
		EndpointConfiguration confg = new EndpointConfiguration("https://s3-sa-east-1.amazonaws.com", "sa-east-1");
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        System.out.println("Verificando existenia do arquivo '" + nomeArquivo + "' em " + this.getDiretorioPathS3());
        try {
//        	boolean arquivoExiste = s3.doesObjectExist(this.getNomeBucketS3(), this.getDiretorioPathS3() + nomeArquivo);
//        	System.out.println("Arquivo '" + nomeArquivo + "' existe? " + (arquivoExiste ? "Sim" : "Não"));
        	
        	ObjectMetadata omd = s3.getObjectMetadata(nomeArquivo, this.getDiretorioPathS3());
        	if(omd != null && omd.getContentLength() > 0){
        		return true;
        	}
        	
//        	return arquivoExiste;
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao verificar exist�ncia do arquivo no AWS S3. Path: " + 
        		(this.getNomeBucketS3() + "/" + this.getDiretorioPathDoAquivoS3(nomeArquivo)));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
        
        return false;
	}
	
	public void excluirArquivoS3(String nomeArquivo)
			{
		
		BasicAWSCredentials creds = new BasicAWSCredentials("accesskey", "secretkey");
		EndpointConfiguration confg = new EndpointConfiguration("https://s3-sa-east-1.amazonaws.com", "sa-east-1");
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        System.out.println("Excluindo arquivo '" + nomeArquivo + "' em " + this.getDiretorioPathS3());
        try {
            s3.deleteObject(this.getNomeBucketS3(), this.getDiretorioPathDoAquivoS3(nomeArquivo));
            System.out.println("Arquivo '" +this.getDiretorioPathDoAquivoS3(nomeArquivo) + "' excluido com sucesso.");
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao excluir arquivo no AWS S3. Path: " + 
        		(this.getNomeBucketS3() + "/" + this.getDiretorioPathDoAquivoS3(nomeArquivo)));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
		
	}
	
	public S3Object recuperarArquivoS3(String nomeArquivo)
	{
		
		BasicAWSCredentials creds = new BasicAWSCredentials("accesskey", "secretkey");
		EndpointConfiguration confg = new EndpointConfiguration("https://s3-sa-east-1.amazonaws.com", "sa-east-1");
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        System.out.println("Recuperando arquivo '" + nomeArquivo + "' em " + this.getDiretorioPathS3());
        try {
        	S3Object arquivo = s3.getObject(this.getNomeBucketS3(), this.getDiretorioPathDoAquivoS3(nomeArquivo));
            System.out.println("Arquivo '" + nomeArquivo + "' recuperado com sucesso.");
            return arquivo;
        }
        catch (AmazonServiceException e) {
            System.out.println("Falha ao recuperar arquivo no AWS S3. Path: " + 
        		(this.getNomeBucketS3() + "/" + this.getDiretorioPathS3() + nomeArquivo));
            System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
            System.out.println("[AWS] " + e.getStackTrace().toString());
        }
        
		return null;
	}
	
	
	public ObjectListing listFilesS3()
	{

		BasicAWSCredentials creds = new BasicAWSCredentials("accesskey", "secretkey");
		EndpointConfiguration confg = new EndpointConfiguration("https://s3-sa-east-1.amazonaws.com", "sa-east-1");
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
		
		System.out.println("Recuperando arquivos em " + this.getDiretorioPathS3());
		try {
			ObjectListing arquivos = s3.listObjects(this.getNomeBucketS3(), this.getDiretorioPathS3());
		    System.out.println("Arquivos recuperados com sucesso.");
		    return arquivos;
		}
		catch (AmazonServiceException e) {
		    System.out.println("Falha ao recuperar arquivos no AWS S3. Path: " + 
				(this.getNomeBucketS3() + "/" + this.getDiretorioPathS3()));
		    System.out.println("[AWS] " + e.getErrorCode() + " - " + e.getErrorType() + " - " + e.getErrorMessage());
		    System.out.println("[AWS] " + e.getStackTrace().toString());
		}
		
		return null;
	}
		
	
	@Deprecated
	public void criarDiretorioS3(String nomeDiretorio)
	{
		
		BasicAWSCredentials creds = new BasicAWSCredentials("accesskey", "secretkey");
		EndpointConfiguration confg = new EndpointConfiguration("https://s3-sa-east-1.amazonaws.com", "sa-east-1");
		
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withEndpointConfiguration(confg).build();
        
        try {
        	 // Montando metadados para criação de diretório
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0);

            // Arquivo vazio
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

            // Criando requisição: nome do diretório seguido de barra (padrão S3)
            PutObjectRequest putObjectRequest = new PutObjectRequest(
        		this.getNomeBucketS3(),
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

	public String getNomeBucketS3() {
		return NomeBucketS3;
	}

	public void setNomeBucketS3(String nomeBucketS3) {
		NomeBucketS3 = nomeBucketS3;
	}

	public String getDiretorioPathS3() {
		return diretorioPathS3;
	}

	public void setDiretorioPathS3(String diretorioPathS3) {
		this.diretorioPathS3 = diretorioPathS3 + "/";
	}
	public String getURLArquivoS3(String nomeArquivo) {
		return this.getNomeBucketS3()+(this.getDiretorioPathS3() != null && this.getDiretorioPathS3().length() > 0 ? this.getDiretorioPathS3() : "") + nomeArquivo;
	}
	public String getDiretorioPathDoAquivoS3(String nomeArquivo) {
		return (this.getDiretorioPathS3() != null && this.getDiretorioPathS3().length() > 0 ? this.getDiretorioPathS3() : "") + nomeArquivo;
	}

}
	
