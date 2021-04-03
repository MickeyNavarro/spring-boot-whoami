//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//January 12, 2021 
//I used the source code from the following website: https://github.com/MickeyNavarro/KaraokeMachine, https://github.com/Artur-Wisniewski/minesweeper
package com.SeniorCapstone.WhoAmI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	
	//method to map to home page
	@GetMapping("/home")
	public ModelAndView home() 
	{		
		// Create a ModelAndView and then set an attribute named message and with a View named home
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
	    return mv;
	}

	//method to map to join page
	@GetMapping("/join")
	public ModelAndView join() 
	{		
		// Create a ModelAndView and then set an attribute named message and with a View named join
		ModelAndView mv = new ModelAndView();
		mv.setViewName("join");
	    return mv;
	}
	
	//method to map to create page
	@GetMapping("/create")
	public ModelAndView create() 
	{		
		// Create a ModelAndView and then set an attribute named message and with a View named create
		ModelAndView mv = new ModelAndView();
		mv.setViewName("create");
	    return mv;
	}


}
