/**
 * 
 */
package com.ncr.ATMMonitoring.parser.exception;

/**
 * Indicate that the given XML can be processed by any parser found in the
 * classpath
 * 
 * @author ottoabreu
 * 
 */
public class NoParserFoundException extends Exception {

	/**
	 * NO_PARSER_FOUND =
	 * "The given XML can not be processed by any parser present in the classpath, check the configured parsers or add a new one to the classpath"
	 * ;
	 */
	public static final String NO_PARSER_FOUND = "The given XML can not be processed by any parser present in the classpath, check the configured parsers or add a new one to the classpath, file:  \n";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public NoParserFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}



}