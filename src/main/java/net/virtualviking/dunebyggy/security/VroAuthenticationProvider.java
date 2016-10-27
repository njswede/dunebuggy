package net.virtualviking.dunebyggy.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class VroAuthenticationProvider implements AuthenticationProvider {
	
	private String vroUrl;
		
	private static Log log = LogFactory.getLog(VroAuthenticationProvider.class);
	
	
	public VroAuthenticationProvider(String vroUrl) {
		this.vroUrl = vroUrl;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if(auth instanceof UsernamePasswordAuthenticationToken) {
			try {
				return new VroToken(vroUrl, auth.getPrincipal().toString(), auth.getCredentials().toString());
			} catch (Exception e) {
				log.error("Trouble logging in", e);
				throw new AuthenticationServiceException("Internal error", e);
			} 
		}
		return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		System.out.println(clazz);
		return clazz.equals(UsernamePasswordAuthenticationToken.class) || clazz.equals(VroToken.class);
	}

}
