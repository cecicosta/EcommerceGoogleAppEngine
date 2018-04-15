package com.cloud.smartvendas.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="User")
public class User {

	@Id
	@Column(name = "login")	
	private String login;
	private String password;
	private String name;
	private String email;
	private String birthDate;
	private String phoneNumber;
	private String photo;
	
	@Transient
	private MultipartFile photoFile;

	public String getName() { return name; }
	public void setName(String name){ this.name = name; }
	
	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public String getBirthDate() { return birthDate; }
	public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
	
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

	public String getPhoto() { return photo; }
	public void setPhoto(String photo) { this.photo = photo; }
	
	public MultipartFile getPhotoFile() { return photoFile; }
	public void setPhotoFile(MultipartFile configFile) { this.photoFile = configFile; }

	
	@Override
	public String toString(){
		return "name="+name+", birthdate="+birthDate;
	}

}
