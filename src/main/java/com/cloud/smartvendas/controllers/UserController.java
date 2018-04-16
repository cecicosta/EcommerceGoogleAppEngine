package com.cloud.smartvendas.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
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

import com.cloud.smartvendas.aws.helpers.AmazonDynamoDBHelper;
import com.cloud.smartvendas.aws.helpers.AmazonS3ServiceHelper;
import com.cloud.smartvendas.aws.helpers.AmazonSESServiceHelper;
import com.cloud.smartvendas.entities.ProductDAO;
import com.cloud.smartvendas.entities.User;
import com.cloud.smartvendas.entities.UserDAO;
import com.cloud.smartvendas.nosql.entities.Log;
import com.cloud.smartvendas.nosql.entities.Log.AffectedType;
import com.cloud.smartvendas.nosql.entities.Log.Operations;

@Controller
public class UserController {

	@Autowired
	UserDAO userDAO;
	
	private String deletedUserMessage = "Seu usuário foi deletado do sistema SmartVendas. Se deseja saber o motivo, responda este email.";

	@RequestMapping(value = "/user_menu", method = RequestMethod.GET)
	public ModelAndView userMenuRedirect(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model) {
		
		ModelAndView modelView = new ModelAndView("views/index");
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}

		if(request.getParameter("action").compareTo("list_user") == 0){
			model.addAttribute("userList", userDAO.listUsers());
			AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.list, AffectedType.user, ""));
		}
	
		model.addAttribute("user", new User());
		modelView.addObject("mainPanel", "./"+ request.getParameter("action") + ".jsp");
		model.addAttribute("authUser", userDAO.getUserById(authentication));
		System.out.println("Already authenticated as " + authentication);
	
		return modelView;
	}
	
	/*
	 * Method to update and add users
	 */
	@RequestMapping(value = "/add_user_submit", method = RequestMethod.POST)
	public ModelAndView addUserSubmit(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("user") User user, 
			HttpServletRequest request, 
			BindingResult result) throws IllegalStateException, IOException {
		
		ModelAndView modelView = new ModelAndView("views/index");		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(result.hasErrors()){
			SetMessagePage(modelView, "", "Erro ao tentar adicionar usuário.");
			return modelView;
		}
		
		MultipartFile photoFile = user.getPhotoFile();
		if(photoFile != null){
			AmazonS3ServiceHelper s3 = new AmazonS3ServiceHelper();
			s3.sendFile(photoFile.getOriginalFilename(), multipartToFile(photoFile), true);
			user.setPhoto(s3.getFileURL(photoFile.getOriginalFilename()).toString());
		}
		
		if(emptyOrNull(request.getParameter("update"))){
			try{
				userDAO.addUser(user);
				if(!emptyOrNull(user.getEmail()))
					AmazonSESServiceHelper.SendEmailVerification(user.getEmail());
				SetMessagePage(modelView, user.getName(), "Usuário adicionado com sucesso! <br />Um email de confirmação foi enviado para " + user.getEmail());
				AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.insert, AffectedType.user, user.getLogin()));
			}catch(Exception e){
				SetMessagePage(modelView, "", "Erro ao adicionar usuário. Login informado já existe.");
			}
		}else{
			userDAO.updateUser(user);
			SetMessagePage(modelView, user.getName(), "Usuário atualizado com sucesso!");
			AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.alter, AffectedType.user, user.getLogin()));
		}
				
		return modelView;
	}
	
	/*
	 * Method to remove users
	 */
	@RequestMapping(value = "/delete_user_submit", method = RequestMethod.POST)
	public ModelAndView removeUsers(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("user") User user, 
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
		
		if(!emptyOrNull(user.getLogin()) && user.getLogin().compareTo(authentication) != 0){
			try{
				userDAO.removeUser(user.getLogin());
				User authUser = userDAO.getUserById(authentication);
				if(!emptyOrNull(user.getEmail()))
					AmazonSESServiceHelper.SendEmail(user.getEmail(), authUser.getEmail(), "Usuário Deletado", "", deletedUserMessage );
				SetMessagePage(modelView, "", "Usuário " + user.getName() + " foi removido do sistema SmartVendas" );
				AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.delete, AffectedType.user, user.getLogin()));
			}catch(Exception e){
				SetMessagePage(modelView, "", "Erro ao remover usuário.");
			}
		}else{
			SetMessagePage(modelView, "", "Não é possível remover o próprio usuário.");
			return modelView;
		}
		
		return modelView;
	}
	
	
	/*
	 * Method to find an users
	 */
	@RequestMapping(value = "/find_user_by_name", method = RequestMethod.POST)
	public ModelAndView findUsers(
			@CookieValue(value = "authentication", defaultValue = "") String authentication,
			@ModelAttribute("user") User user, 
			HttpServletRequest request, Model model) throws IllegalStateException, IOException {
				
		ModelAndView modelView = new ModelAndView("views/index");		
		
		if(authentication.compareTo("") == 0){
			SetMessagePage(modelView, "", "Disponível apenas para usuários autenticados.");
			return modelView;
		}
		
		if(emptyOrNull(user.getName())){
			SetMessagePage(modelView, authentication, "Nenhum nome foi digitado como critério de busca.");
		}
		Stream<User> users = userDAO.listUsers().stream().filter(x -> x.getName().contains(user.getName()));
		model.addAttribute("userList", users.toArray());
	
		AmazonDynamoDBHelper.updateItem(new Log(authentication, Operations.search, AffectedType.user, ""));
		modelView.addObject("mainPanel", "./list_user.jsp");
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

