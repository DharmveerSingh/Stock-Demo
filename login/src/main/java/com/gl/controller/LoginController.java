package com.gl.controller;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gl.model.User;
import com.gl.model.User2;
import com.gl.service.UserService;

/**
 * @author dharamveer.singh
 *
 */
@Controller
public class LoginController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/", "/login" ,"user/login" }, method = RequestMethod.GET)
	public ModelAndView login(Principal principal) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(principal!=null && principal.getName()!=null)
			modelAndView.setViewName("redirect:user/home");
		else
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		if(principal!=null && principal.getName()!=null)
			modelAndView.setViewName("redirect:user/home");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Optional<User> userExists = userService.findUserByEmail("USER::" + user.getEmail());
		if (userExists.isPresent()) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (!bindingResult.hasErrors()) {

			user.setId("USER::" + user.getEmail());
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
		}
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@GetMapping("/edit")
	public ModelAndView editUser(Principal principal) {
		ModelAndView mv= new ModelAndView();
		User user=userService.getUser(principal.getName());
		mv.addObject("user", user);
		mv.setViewName("edit");
		 return mv;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editUser(@Valid User2 user, BindingResult bindingResult, Principal principal) {
		System.out.println("Principal USer: "+ principal.getName()+", user's user: "+ user.getEmail());
		//user.setEmail(principal.getName());
		ModelAndView modelAndView = new ModelAndView();
		Optional<User> userExists = userService.findUserByEmail( principal.getName());
		
		  if (!userExists.isPresent()) { bindingResult.rejectValue("email",
		  "error.user", "Cannot find this user");
		  }
		  User u= userExists.get();
		if (!bindingResult.hasErrors()) {
				u= userExists.get();
				u.setLastName(user.getLastName());
				u.setName(user.getName());
				u.setPassword(user.getPassword());
			userService.saveUser(u);
			modelAndView.addObject("successMessage", "User has been Edited successfully");
			modelAndView.setViewName("edit");
			modelAndView.addObject("user", u);
		}
		else {
			user.setEmail(u.getEmail());
			modelAndView.addObject("user", user);
		}
		modelAndView.setViewName("edit");
		return modelAndView;
	}
	
	@GetMapping("/delete")
	public ModelAndView deleteAccount(Principal principal) {
		ModelAndView mv= new ModelAndView();
		Boolean flag =userService.disable(principal.getName());
		if(flag)
		mv.setViewName("redirect:logout");
		else
			mv.setViewName("redirect:login");
		return mv;
	}
}


