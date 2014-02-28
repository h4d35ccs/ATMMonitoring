package com.ncr.ATMMonitoring.controller;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ncr.ATMMonitoring.pojo.User;
import com.ncr.ATMMonitoring.service.UserService;

//@Controller
public class externalreportsController extends GenericController {  
//    @Autowired
//    private UserService userService;
   

//    @RequestMapping(value = "/externalreports", method = RequestMethod.GET)
    public void requestTerminalsUpdate(Map<String, Object> map, HttpServletRequest request, Principal principal) {
		
    	String userMsg = "";
//    	Locale locale = RequestContextUtils.getLocale(request);
    	if (principal != null) {
//    	    User loggedUser = userService
//    		    .getUserByUsername(principal.getName());
//    	    userMsg = loggedUser.getHtmlWelcomeMessage(locale);
    		 userMsg = this.getUserGreeting(principal, request);
    	}
    	map.put("userMsg", userMsg);
		return;// "/externalreports.jsp";
    }
}