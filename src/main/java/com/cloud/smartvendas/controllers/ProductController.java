package com.cloud.smartvendas.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

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

import com.cloud.smartvendas.aws.helpers.AmazonS3ServiceHelper;
import com.cloud.smartvendas.aws.helpers.AmazonSESServiceHelper;
import com.cloud.smartvendas.entities.Product;
import com.cloud.smartvendas.entities.ProductDAO;
import com.cloud.smartvendas.entities.User;
import com.cloud.smartvendas.entities.UserDAO;

@Controller
public class ProductController {

	@Autowired
	ProductDAO productDao;

	@RequestMapping(value = "/product_menu", method = RequestMethod.GET)
	public ModelAndView productMenuRedirect(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model) {
		
		ModelAndView modelView = new ModelAndView("views/index");
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}

		if(request.getParameter("action").compareTo("list_product") == 0){
			model.addAttribute("productList", productDao.listProducts());
		}
	
		model.addAttribute("product", new Product());
		modelView.addObject("mainPanel", "./"+ request.getParameter("action") + ".jsp");
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
			}catch(Exception e){
				SetMessagePage(modelView, "", "Erro ao adicionar produto. Verifique se o produto já existe.");
			}
		}else{
			productDao.updateProduct(product);
			SetMessagePage(modelView, "", "Produto atualizado com sucesso!");
		}
				
		return modelView;
	}
	
	/*
	 * Method to remove ausers
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
				//TODO: Log operation
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
	
		modelView.addObject("mainPanel", "./list_product.jsp");
		return modelView;
	}
	
	/*
	 * Method to find products
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
}

