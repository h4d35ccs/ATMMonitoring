package com.ncr.ATMMonitoring.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The Class LoginController.
 * 
 * Controller for handling login related HTTP petitions.
 * 
 * @author Jorge López Fernández (lopez.fernandez.jorge@gmail.com)
 */

@Controller
public class LoginController extends GenericController {

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
	public String login(RedirectAttributes redirectAttributes ,HttpServletRequest request, Principal principal) {
		String redirect = "login";
		String userMsg = "";
		long loginTime = 0;
		if (principal != null) {
			userMsg = this.getUserGreeting(principal, request);
			loginTime = this.getUserLastLogin( principal,
						 request);
			redirect = "mainFrame";
		}
		redirectAttributes.addFlashAttribute("userMsg", userMsg);
		redirectAttributes.addFlashAttribute("loginTime", loginTime);
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
		long loginTime = 0;
		if (principal != null) {

			userMsg = this.getUserGreeting(principal, request);
			loginTime = this.getUserLastLogin( principal,
					 request);
			redirect = "mainFrame";

		} else {

			redirect = "/login";
		}
		map.put("loginTime", loginTime);
		map.put("userMsg", userMsg);
		return redirect;
	}
	
	
	@RequestMapping(value = "/mapTest", method = RequestMethod.GET)
	public String mapTest() {
		return "mapTest";
	}
}