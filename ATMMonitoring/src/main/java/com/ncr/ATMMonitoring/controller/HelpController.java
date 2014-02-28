package com.ncr.ATMMonitoring.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class HelpController.
 * 
 * Controller for handling help related HTTP petitions.
 * 
 * @author Rafael Luque (rafael.luque@osoco.es)
 */
@Controller
public class HelpController extends GenericController{

    /** The user service. */
//    @Autowired
//    private UserService userService;

    /**
     * Show default help URL.
     * 
     * @param map
     *            the map
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the petition response
     */
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String showHelp(Map<String, Object> map, Principal principal,
	    HttpServletRequest request) {
	String userMsg = "";
//	Locale locale = RequestContextUtils.getLocale(request);
	if (principal != null) {
//	    User loggedUser = userService
//		    .getUserByUsername(principal.getName());
//	    userMsg = loggedUser.getHtmlWelcomeMessage(locale);
		 userMsg = this.getUserGreeting(principal, request);
	}
	map.put("userMsg", userMsg);
	return "redirect:/help/dashboard";
    }

    /**
     * Show dashboard help URL.
     * 
     * @param map
     *            the map
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the petition response
     */
    @RequestMapping(value = "/help/dashboard", method = RequestMethod.GET)
    public String showDashboardHelp(Map<String, Object> map,
	    Principal principal, HttpServletRequest request) {
	String userMsg = "";
//	Locale locale = RequestContextUtils.getLocale(request);
	if (principal != null) {
//	    User loggedUser = userService
//		    .getUserByUsername(principal.getName());
//	    userMsg = loggedUser.getHtmlWelcomeMessage(locale);
		 userMsg = this.getUserGreeting(principal, request);
	}
	map.put("userMsg", userMsg);
	return "helpDashboard";
    }

    /**
     * Show terminals help URL.
     * 
     * @param map
     *            the map
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the petition response
     */
    @RequestMapping(value = "/help/terminals", method = RequestMethod.GET)
    public String showTerminalsHelp(Map<String, Object> map,
	    Principal principal, HttpServletRequest request) {
	String userMsg = "";
//	Locale locale = RequestContextUtils.getLocale(request);
	if (principal != null) {
//	    User loggedUser = userService
//		    .getUserByUsername(principal.getName());
//	    userMsg = loggedUser.getHtmlWelcomeMessage(locale);
		 userMsg = this.getUserGreeting(principal, request);
	}
	map.put("userMsg", userMsg);
	return "helpTerminals";
    }

}
