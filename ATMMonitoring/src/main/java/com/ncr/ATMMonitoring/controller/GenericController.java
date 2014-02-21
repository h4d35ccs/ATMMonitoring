/**
 * 
 */
package com.ncr.ATMMonitoring.controller;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ncr.ATMMonitoring.pojo.User;
import com.ncr.ATMMonitoring.service.UserService;

/**
 * Holds the common method between the controllers
 * 
 * @author Otto Abreu
 *
 */
@Controller
public abstract class GenericController {

	@Autowired
	private UserService userService;
	
	private static final String USER_GREETING = "userGreeting";
	
	
	/**
	 * Obtains the greeting for the user
	 * @param principal
	 * @param request
	 * @return
	 */
	protected String getUserGreeting(Principal principal, HttpServletRequest request){
		
		String greeting  = (String) request.getSession().getAttribute(USER_GREETING);
		
		if(greeting == null){
			
			Locale locale = RequestContextUtils.getLocale(request);
			User user = this.userService.getUserByUsername(principal.getName());
			greeting = user.getHtmlWelcomeMessage(locale);
			request.getSession().setAttribute(USER_GREETING,greeting);
		}
		return greeting;
	}
	
	/**
	 * clear a value from the session
	 * @param request
	 * @param sessionKey
	 */
	protected void clearSession(HttpServletRequest request, String sessionKey){
		request.getSession().setAttribute(sessionKey,null);
	}
}
