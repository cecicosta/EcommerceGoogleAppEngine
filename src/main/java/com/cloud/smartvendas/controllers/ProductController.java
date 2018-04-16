package com.cloud.smartvendas.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.util.StringUtils;
import com.cloud.smartvendas.aws.helpers.AmazonDynamoDBHelper;
import com.cloud.smartvendas.aws.helpers.AmazonS3ServiceHelper;
import com.cloud.smartvendas.aws.helpers.AmazonSESServiceHelper;
import com.cloud.smartvendas.entities.Product;
import com.cloud.smartvendas.entities.ProductDAO;
import com.cloud.smartvendas.entities.User;
import com.cloud.smartvendas.entities.UserDAO;
import com.cloud.smartvendas.nosql.entities.Log;
import com.cloud.smartvendas.nosql.entities.Log.AffectedType;
import com.cloud.smartvendas.nosql.entities.Log.Operations;

@Controller
public class ProductController {

	@Autowired
	ProductDAO productDao;
	
	@Autowired
	UserDAO userDao;

	private String systemEmail = "cecier.costa@gmail.com";

	@RequestMapping(value = "/product_menu", method = RequestMethod.GET)
	public ModelAndView productMenuRedirect(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@CookieValue(value = "shopping_cart", defaultValue = "") String shoppingCart,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model) {
		
		ModelAndView modelView = new ModelAndView("views/index");
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}

		String parameter = request.getParameter("action");
		if(parameter.compareTo("list_product") == 0 || parameter.compareTo("ecommerce") == 0){
			model.addAttribute("productList", productDao.listProducts());
			AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.list, AffectedType.product, ""));
		}else if(parameter.compareTo("purchase_products") == 0){
			ArrayList<Product> selectedProducts = selectProductsToPurchase(shoppingCart);
			model.addAttribute("productList", selectedProducts);
		}
		
		model.addAttribute("product", new Product());
		modelView.addObject("mainPanel", "./"+ parameter + ".jsp");
		return modelView;
	}
	
	/*
	 * Method to update and add products
	 */
	@RequestMapping(value = "/add_product_submit", method = RequestMethod.POST)
	public ModelAndView addProductSubmit(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("product") Product product, 
			HttpServletRequest request, 
			BindingResult result) throws IllegalStateException, IOException {
		
		ModelAndView modelView = new ModelAndView("views/index");		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(result.hasErrors()){
			SetMessagePage(modelView, "", "Erro ao tentar adicionar produto.");
			return modelView;
		}
		
		MultipartFile photoFile = product.getPhotoFile();
		if(photoFile != null){
			AmazonS3ServiceHelper s3 = new AmazonS3ServiceHelper();
			s3.sendFile(photoFile.getOriginalFilename(), multipartToFile(photoFile), true);
			product.setPhoto(s3.getFileURL(photoFile.getOriginalFilename()).toString());
		}
		
		if(emptyOrNull(request.getParameter("update"))){
			try{
				productDao.addProduct(product);
				SetMessagePage(modelView, "", "Produto adicionado com sucesso!");
				AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.insert, AffectedType.product, product.getName()));
			}catch(Exception e){
				SetMessagePage(modelView, "", "Erro ao adicionar produto. Verifique se o produto já existe.");
			}
		}else{
			productDao.updateProduct(product);
			SetMessagePage(modelView, "", "Produto atualizado com sucesso!");
			AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.alter, AffectedType.product, product.getName()));
		}
				
		return modelView;
	}
	
	/*
	 * Method to remove products
	 */
	@RequestMapping(value = "/delete_product_submit", method = RequestMethod.POST)
	public ModelAndView removeProducts(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("product") Product product, 
			HttpServletRequest request, 
			HttpServletResponse response, BindingResult result) throws IllegalStateException, IOException {
		
		ModelAndView modelView = new ModelAndView("views/index");		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(result.hasErrors()){
			SetMessagePage(modelView, "", "Erro ao tentar adicionar usuário.");
			return modelView;
		}
		
		if(!emptyOrNull(product.getName())){
			try{
				productDao.removeProduct(product.getName());
				SetMessagePage(modelView, "", "Produto " + product.getName() + " foi removido do sistema SmartVendas" );
				AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.delete, AffectedType.product, product.getName()));
			}catch(Exception e){
				SetMessagePage(modelView, "", "Erro ao remover produto.");
			}
		}else{
			SetMessagePage(modelView, "", "Não é possível remover produto. Nome não informado");
			return modelView;
		}
		
		return modelView;
	}
	
	/*
	 * Method to find products
	 */
	@RequestMapping(value = "/find_product_by_name", method = RequestMethod.POST)
	public ModelAndView findProduct(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("product") Product product, 
			Model model) throws IllegalStateException, IOException {
				
		ModelAndView modelView = new ModelAndView("views/index");		
		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(emptyOrNull(product.getName())){
			SetMessagePage(modelView, "", "Nenhum nome foi digitado como critério de busca.");
		}
		
		Stream<Product> users = productDao.listProducts().stream().filter(x -> x.getName().contains(product.getName()));
		model.addAttribute("productList", users.toArray());
		AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.search, AffectedType.product, ""));
		modelView.addObject("mainPanel", "./list_product.jsp");
		return modelView;
	}
	
	/*
	 * Method to update products
	 */
	@RequestMapping(value = "/update_product", method = RequestMethod.POST)
	public ModelAndView updateProduct(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("product") Product product, 
			Model model) throws IllegalStateException, IOException {
				
		ModelAndView modelView = new ModelAndView("views/index");		
		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(emptyOrNull(product.getName())){
			SetMessagePage(modelView, "", "Nenhum nome foi digitado como critério de busca.");
		}
		
		if(!emptyOrNull(product.getName())){
			try{
				Product p = productDao.getProductById(product.getName());
				model.addAttribute("productEdit", p);
				modelView.addObject("mainPanel", "./update_product.jsp");
				//TODO: Log operation
			}catch(Exception e){
				SetMessagePage(modelView, "", "Erro ao remover produto.");
			}
		}
		
		return modelView;
	}
	
	/*
	 * Method to select products to buy
	 */
	@RequestMapping(value = "/select_product", method = RequestMethod.POST)
	public ModelAndView selectProducts(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@CookieValue(value = "shopping_cart", defaultValue = "") String shoppingCart,
			@ModelAttribute("product") Product product, 
			HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalStateException, IOException {
		
		ModelAndView modelView = new ModelAndView("views/index");		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(!emptyOrNull(product.getName())){
			String toAdd = (shoppingCart.isEmpty()? "" : ",") + product.getName();
			shoppingCart += toAdd;
			Cookie cookie = new Cookie("shopping_cart", shoppingCart);
			response.addCookie(cookie);
			cookie.setMaxAge(600);
		}
		
		ArrayList<Product> selectedProducts = selectProductsToPurchase(shoppingCart);
		
		model.addAttribute("productList", selectedProducts);
		modelView.addObject("mainPanel", "./purchase_products.jsp");
		return modelView;
	}

	private ArrayList<Product> selectProductsToPurchase(String shoppingCart) {
		String selectedProductNames = (shoppingCart);
		List<Product> availableProducts = productDao.listProducts();
		ArrayList<Product> selectedProducts = new ArrayList<Product>();
		for (Product p : availableProducts) {
			int matches = countMatches(selectedProductNames, p.getName());
			for (int i = 0; i < matches; i++) {
				selectedProducts.add(p);
			}
		}
		return selectedProducts;
	}
	
	
	@RequestMapping(value = "/finish_purchase", method = RequestMethod.POST)
	public ModelAndView finishPurchase(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@CookieValue(value = "shopping_cart", defaultValue = "") String shoppingCart,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView modelView = new ModelAndView("views/index");		
		if(authentication.isEmpty()){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(shoppingCart.isEmpty()){
			SetMessagePage(modelView, "", "Nenhum produto foi adicionado ao carrinho.");
			return modelView;
		}
		
		if(request.getParameter("action") != null && request.getParameter("action").compareTo("clear_shopping_cart") == 0){
			Cookie cookie = new Cookie("shopping_cart", "");
			response.addCookie(cookie);
			cookie.setMaxAge(600);
			SetMessagePage(modelView, "", "O carrinho de compras foi esvaziado.");
			return modelView;
		}
		
		String selectedProductNames = (shoppingCart);
		List<Product> availableProducts = productDao.listProducts();
		for (Product p : availableProducts) {
			int matches = countMatches(selectedProductNames, p.getName());
			for (int i = 0; i < matches; i++) {
				p.setQuantity(p.getQuantity() - 1);
			}
			productDao.updateProduct(p);
		}
		
		
		User user = userDao.getUserById(authentication);
		if(user != null){
			AmazonSESServiceHelper.SendEmail(
					user.getEmail(), 
					systemEmail , 
					"Compra Finalizada", 
					"Você adquiriu os seguintes produtos: " + shoppingCart, 
					"");
		}

		Cookie cookie = new Cookie("shopping_cart", "");
		response.addCookie(cookie);
		cookie.setMaxAge(600);
		
		SetMessagePage(modelView, "", "Parabens, você finalizou a compra dos seguintes produtos: " + shoppingCart);
		return modelView;
	}
	
	/*
	 * AUXILIAR METHODS
	 */
	
	private void SetMessagePage(ModelAndView modelAndView, String userName, String message){
		modelAndView.addObject("mainPanel", "./message.jsp");
		modelAndView.addObject("userName", userName.isEmpty()? "" : " ");
		modelAndView.addObject("message", message);
	}
	
	private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
	{
		
		CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipart;
	    FileItem fileItem = commonsMultipartFile.getFileItem();
	    DiskFileItem diskFileItem = (DiskFileItem) fileItem;
	    String absPath = diskFileItem.getStoreLocation().getAbsolutePath();
	    File file = new File(absPath);

	    String name = new SimpleDateFormat("ddMMyy-hhmmss.SSS").format(new Date());
	    //trick to implicitly save on disk small files (<10240 bytes by default)
	    if (!file.exists()) {
	    	file = new File(System.getProperty("java.io.tmpdir")+"/" + name + multipart.getOriginalFilename());
	    	multipart.transferTo(file);
	    }

	    return file;
	}
	
	private boolean emptyOrNull(String str){
		return str == null || str.compareTo("") == 0;
	}
	
	public int countMatches(String str, String sub) {
		if (str.isEmpty() || sub.isEmpty()) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}
}

