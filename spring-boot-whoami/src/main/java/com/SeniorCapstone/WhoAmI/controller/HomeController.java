package com.SeniorCapstone.WhoAmI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	
	
	@GetMapping("/home")
	public ModelAndView home() 
	{		
		// Create a ModelAndView and then set an attribute named message and with a View named home
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
	    return mv;
	}

	@GetMapping("/join")
	public ModelAndView join() 
	{		
		// Create a ModelAndView and then set an attribute named message and with a View named join
		ModelAndView mv = new ModelAndView();
		mv.setViewName("join");
	    return mv;
	}
	
	@GetMapping("/create")
	public ModelAndView create() 
	{		
		// Create a ModelAndView and then set an attribute named message and with a View named create
		ModelAndView mv = new ModelAndView();
		mv.setViewName("create");
	    return mv;
	}


}
