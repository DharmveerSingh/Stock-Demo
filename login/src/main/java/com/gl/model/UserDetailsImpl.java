package com.gl.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author dharamveer.singh
 *
 */
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 549285773358148945L;
	private String username;
	private String password;
	private List<GrantedAuthority> grantedAuthorities;
	private boolean enabled;
	public UserDetailsImpl(String username, String password, String[] authorities, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled=enabled;
		this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}