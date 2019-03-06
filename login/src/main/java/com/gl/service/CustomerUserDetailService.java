package com.gl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gl.model.User;
import com.gl.model.UserDetailsImpl;
import com.gl.repository.UserRepository;

/**
 * @author dharamveer.singh
 *
 */
@Service
public class CustomerUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	/*
	 * (non-Javadoc) get User by email
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<User> document = userRepo.findById("USER::" + email);
		UserDetailsImpl userDetails = null;
		if (document.isPresent()) {

			User u = document.get();
			List<String> authorities = new ArrayList<String>();
			authorities.add("ADMIN");
			userDetails = new UserDetailsImpl(u.getId(), u.getPassword(),
					authorities.toArray(new String[authorities.size()]), u.isActive());
		}

		return userDetails;
	}

}