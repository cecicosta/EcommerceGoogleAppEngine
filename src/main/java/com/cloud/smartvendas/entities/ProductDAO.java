package com.cloud.smartvendas.entities;

import java.util.List;

public interface ProductDAO {

	public void addProduct(Product p);
	public void updateProduct(Product p);
	public List<Product> listProducts();
	public Product getProductById(String id);
	public void removeProduct(String id);
}