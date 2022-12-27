package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@RequestMapping("/sbb")
	@ResponseBody
	public String index() {
		return "Hello";
	}
	
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	// root url 매핑
	@RequestMapping("/")
	public String root() {
		return "redirect:/home";
	}
}
