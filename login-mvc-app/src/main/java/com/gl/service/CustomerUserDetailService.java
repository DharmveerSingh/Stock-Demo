package com.gl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gl.feign.user.UserServiceProxy;
import com.gl.model.User;
import com.gl.model.UserDetailsImpl;

/**
 * The Class CustomerUserDetailService.
 *
 * @author dharamveer.singh
 */
@Service
public class CustomerUserDetailService implements UserDetailsService {

	/** The user service proxy. */
	@Autowired
	private UserServiceProxy userServiceProxy;

	/*
	 * (non-Javadoc) get User by email
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String email){

		User user = userServiceProxy.getUser("USER::" + email);
		UserDetailsImpl userDetails = null;
		if (user != null && user.getEmail() != null) {
			List<String> authorities = new ArrayList<>();
			authorities.add("ADMIN");
			userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword(),
					authorities.toArray(new String[authorities.size()]), user.isActive());
		}

		return userDetails;
	}

}