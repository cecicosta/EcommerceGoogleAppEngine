package com.cloud.smartvendas.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="Product")
public class Product {

	@Id
	@Column(name = "name")
	private String name;
	private String description;
	private String vendor;
	private int quantity;
	private String photo;
	
	@Transient
	private MultipartFile photoFile;

	public String getName() { return name; }
	public void setName(String name){ this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public String getVendor() { return vendor; }
	public void setVendor(String vendor) { this.vendor = vendor; }

	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	
	public String getPhoto() { return photo; }
	public void setPhoto(String photo) { this.photo = photo; }
	
	@Override
	public String toString(){
		return "name="+name+", description="+description;
	}

	public MultipartFile getPhotoFile() { return photoFile; }
	public void setPhotoFile(MultipartFile configFile) { this.photoFile = configFile; }
}
