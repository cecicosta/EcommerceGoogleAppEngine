package com.cloud.smartvendas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class SimplePageController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestAttribute ("login") String login, Model model) {
		return "Page Called...";
	}
}
