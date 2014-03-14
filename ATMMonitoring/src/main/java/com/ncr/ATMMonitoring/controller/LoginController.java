package com.ncr.ATMMonitoring.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class LoginController.
 * 
 * Controller for handling login related HTTP petitions.
 * 
 * @author Jorge López Fernández (lopez.fernandez.jorge@gmail.com)
 */

@Controller
public class LoginController extends GenericController{

    /**
     * Render a page for redirect on the client to the correct login page. Is
     * used for avoid login page on iframes
     * 
     * @return The request result
     */
    @RequestMapping(value = "/preLogin", method = RequestMethod.GET)
    public String preLogin() {
	return "preLogin";
    }

    /**
     * Index URL.
     * 
     * @return the petition response
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String printWelcome() {
	return "redirect:/login";
    }

    /**
     * Base URL.
     * 
     * @return the petition response
     */
    @RequestMapping("/")
    public String redirectToIndex() {
	return "redirect:/login";
    }

    /**
     * Login URL.
     * 
     * @return the petition response
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request,Principal principal) {
    	String redirect = "login";
    	
    	if(principal != null){
    		redirect = "mainFrame";
    	}
    	return redirect;
    }

    /**
     * Login failed URL.
     * 
     * @param map
     *            the map
     * @return the petition response
     */
    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginFailed(Map<String, Object> map) {
	map.put("error", true);
	return "login";
    }
    
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String showDashboard(Map<String, Object> map, Principal principal,
	    HttpServletRequest request) {
    	String userMsg = "";
    	String redirect = "";
    	
    	if (principal != null) {
    	    
    		userMsg = this.getUserGreeting(principal, request);
    		redirect = "mainFrame";
    		
    	}else{
    		
    		redirect = "/login";
    	}
    	map.put("userMsg", userMsg);
    	return redirect;
    }
}