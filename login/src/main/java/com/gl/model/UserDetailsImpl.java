package com.gl.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The Class UserDetailsImpl.
 *
 * @author dharamveer.singh
 */
public class UserDetailsImpl implements UserDetails {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 549285773358148945L;
	
	/** The username. */
	private String username;
	
	/** The password. */
	private String password;
	
	/** The granted authorities. */
	private List<GrantedAuthority> grantedAuthorities;
	
	/** The enabled. */
	private boolean enabled;
	
	/**
	 * Instantiates a new user details impl.
	 *
	 * @param username the username
	 * @param password the password
	 * @param authorities the authorities
	 * @param enabled the enabled
	 */
	public UserDetailsImpl(String username, String password, String[] authorities, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled=enabled;
		this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}