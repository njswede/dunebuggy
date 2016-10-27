package net.virtualviking.dunebuggy.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

import net.virtualviking.dunebuggy.security.VroToken;
import net.virtualviking.dunebuggy.vro.VroClient;

public class AbstractController {

	public AbstractController() {
		super();
	}
	
	@Scope("session")
	protected String getUser() {
		 VroToken auth = (VroToken) SecurityContextHolder.getContext().getAuthentication();
		 try { 
			 return auth.getPrincipal().toString();
		 } catch(Exception e) {
			 // Exceptions here are unrecoverable, so throw a RuntimeException
			 //
			 throw new RuntimeException(e);
		 }
	}
	
	@Scope("session")
	protected VroClient getVro() {
		 VroToken auth = (VroToken) SecurityContextHolder.getContext().getAuthentication();
		 try { 
			 return auth.getVraClient();
		 } catch(Exception e) {
			 // Exceptions here are unrecoverable, so throw a RuntimeException
			 //
			 throw new RuntimeException(e);
		 }
	}
}