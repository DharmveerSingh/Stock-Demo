package com.gl.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gl.constants.AppConstants;
import com.gl.feign.user.UserServiceProxy;
import com.gl.model.User;
import com.gl.model.User2;
import com.gl.model.UserResponse;

/**
 * The login controller.
 *
 * @author dharamveer.singh
 */
@Controller
public class LoginController {

	/** The Constant ERROR_USER. */
	private static final String ERROR_USER = "error.user";

	/** The Constant EMAIL. */
	private static final String EMAIL = "email";

	/** The Constant REGISTRATION. */
	private static final String REGISTRATION = "registration";

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	/** The b crypt password encoder. */
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/** The user service proxy. */
	@Autowired
	private UserServiceProxy userServiceProxy;

	/**
	 * Login.
	 *
	 * @param principal the principal
	 * @return the model and view
	 */
	@GetMapping(value = { "/", "/login", "user/login" })
	public ModelAndView login(Principal principal) {

		ModelAndView modelAndView = new ModelAndView();
		if (principal != null && principal.getName() != null)
			modelAndView.setViewName("redirect:user/home");
		else
			modelAndView.setViewName("login");
		return modelAndView;
	}

	/**
	 * Registration.
	 *
	 * @param principal the principal
	 * @return the model and view
	 */
	@GetMapping(value = "/registration")
	public ModelAndView registration(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName(REGISTRATION);
		if (principal != null && principal.getName() != null)
			modelAndView.setViewName("redirect:user/home");
		return modelAndView;
	}

	/**
	 * Creates the new user.
	 *
	 * @param user          the user
	 * @param bindingResult the binding result
	 * @return the model and view
	 */
	@PostMapping(value = "/registration")
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userServiceProxy.getUser("USER::" + user.getEmail());
		if (userExists != null && userExists.getError() == null) {
			bindingResult.rejectValue(EMAIL, ERROR_USER, "There is already a user registered with the email provided");
		}
		if (!bindingResult.hasErrors()) {

			user.setId("USER::" + user.getEmail());
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setActive(Boolean.TRUE);
			user.setAccountBalance(100000.00);
			UserResponse u = userServiceProxy.signup(user);
			if (u.getError() == null) {
				modelAndView.addObject("successMessage", "User has been registered successfully");
				modelAndView.addObject("user", new User());
				modelAndView.setViewName(REGISTRATION);
			} else {
				bindingResult.rejectValue(EMAIL, ERROR_USER, u.getError().getMessage());
			}
		}
		modelAndView.setViewName(REGISTRATION);
		return modelAndView;
	}

	/**
	 * Edits the user.
	 *
	 * @param principal the principal
	 * @return the model and view
	 */
	@GetMapping("/edit")
	public ModelAndView editUser(Principal principal) {
		ModelAndView mv = new ModelAndView();
		User user = userServiceProxy.getUser(AppConstants.USER_PREFIX + principal.getName());
		mv.addObject("user", user);
		mv.setViewName("edit");
		return mv;
	}

	/**
	 * Edits the user.
	 *
	 * @param user          the user
	 * @param bindingResult the binding result
	 * @param principal     the principal
	 * @return the model and view
	 */
	@PostMapping(value = "/edit")
	public ModelAndView editUser(@Valid User2 user, BindingResult bindingResult, Principal principal) {
		log.info("editUser {}", principal.getName());
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userServiceProxy.getUser(AppConstants.USER_PREFIX + principal.getName());

		if (userExists == null) {
			bindingResult.rejectValue(EMAIL, ERROR_USER, "Cannot find this user");
		}

		if (userExists != null && userExists.getError() == null) {
			userExists.setLastName(user.getLastName());
			userExists.setName(user.getName());
			userExists.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userExists.setId(AppConstants.USER_PREFIX + principal.getName());
			userExists.setAccountBalance(user.getAccountBalance());
			UserResponse ur = userServiceProxy.update(userExists);
			if (ur.getError() == null) {
				modelAndView.addObject("successMessage", "User has been Edited successfully");
			} else {
				bindingResult.rejectValue(EMAIL, ERROR_USER, ur.getErrorMsg());
			}
			modelAndView.setViewName("edit");
			modelAndView.addObject("user", userExists);
		} else {
			user.setEmail(userExists.getEmail());
			modelAndView.addObject("user", user);
		}
		modelAndView.setViewName("edit");
		return modelAndView;
	}

	/**
	 * Delete account.
	 *
	 * @param principal the principal
	 * @return the model and view
	 */
	@GetMapping("/delete")
	public ModelAndView deleteAccount(Principal principal) {
		ModelAndView mv = new ModelAndView();
		Boolean flag = userServiceProxy.disableUser(AppConstants.USER_PREFIX + principal.getName());
		if (flag)
			mv.setViewName("redirect:logout");
		else
			mv.setViewName("redirect:login");
		return mv;
	}
}
