package com.cloud.smartvendas.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cloud.smartvendas.entities.ProductDAO;
import com.cloud.smartvendas.entities.User;
import com.cloud.smartvendas.entities.UserDAO;

@Controller
public class LoginController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	ProductDAO productDao;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(Model model) {
		ModelAndView modelView = new ModelAndView("views/index");
		modelView.addObject("mainPanel", "./index2.jsp");
		
		return modelView;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(Model model) {
		model.addAttribute("user", new User()); 
		ModelAndView modelView = new ModelAndView("views/index");
		modelView.addObject("mainPanel", "./login.jsp");
		
		return modelView;
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ModelAndView authenticate(@ModelAttribute("user") User user, HttpServletResponse response, Model model) {
		ModelAndView modelView = new ModelAndView("views/index");
		modelView.addObject("mainPanel", "./index2.jsp");
		
		User registered = userDAO.getUserById(user.getLogin());
		if(registered != null && !emptyOrNull(registered.getPassword()) && 
				!emptyOrNull(user.getPassword()) && registered.getPassword().compareTo(user.getPassword()) == 0){
			Cookie cookie = new Cookie("authentication", registered.getLogin());
			response.addCookie(cookie);
			cookie.setMaxAge(600);
			modelView.addObject("userName", registered.getName() + " ");
			System.out.println("Authenticated as " + cookie.getValue());
		}else{
			SetMessagePage(modelView, "", "Falha de autenticação. Confira o nome de usuário e senha.");
			System.out.println("Authentication failed");
		}
		
		return modelView;
	}
	
	private boolean emptyOrNull(String str){
		return str == null || str.compareTo("") == 0;
	}
	
	private void SetMessagePage(ModelAndView modelAndView, String userName, String message){
		modelAndView.addObject("mainPanel", "./message.jsp");
		modelAndView.addObject("userName", userName);
		modelAndView.addObject("message", message);
	}
}



//SAMPLE SHIT CODE

//Log log = new Log();
//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//log.setId(System.currentTimeMillis());
//log.setTime(timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).toString());
//AmazonDynamoDBHelper.updateItem(log);

//Product p = new Product();
//p.setName("Caneta");
//productDao.addProduct(p);
//System.out.println(productDao.getProductById("Caneta"));
//User u = personDAO.getUserById("cecicosta");
//System.out.println(u.toString());

//CatalogItem item = new CatalogItem();
//item.setId(102);
//item.setTitle("Book 102 Title");
//item.setISBN("222-2222222222");
//item.setBookAuthors(new HashSet<String>(Arrays.asList("Author 1", "Author 2")));
//item.setSomeProp("Test");

//ddb.updateItem(item);
//CatalogItem item = AmazonDynamoDBHelper.RetrieveItem(CatalogItem.class, 102);
//System.out.println(item.getBookAuthors());

//AmazonSESServiceHelper.SendEmail("caezar.jc@gmail.com", "cecier.costa@gmail.com", "AmazonTestEmail", "Aren't you such a gorgeous?!", "<b>YES YOU ARE</b>");

//AmazonS3ServiceHelper s3 = new AmazonS3ServiceHelper("ecommerce-ck0205", "");
//s3.sendFile("lead.png", "C:\\Users\\Cesar\\lead_workspace\\ecommerce\\lead.png", true);

//System.out.println("Accesskey: " + Properties.accessKey.getValue());
