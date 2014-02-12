/**
 * 
 */
package com.ncr.ATMMonitoring.handler.exception;

import com.ncr.ATMMonitoring.handler.FileInDiskHandler;

/**
 * Indicates an error while performing an operation in the
 * {@link FileInDiskHandler}
 * 
 * @author Otto Abreu
 * 
 */
public class FileHandlerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7451665440382160410L;
	/**
	 * GET_INPUTSTREAM_ERROR ="Can not get the inputstream for file: ";
	 */
	public static final String GET_INPUTSTREAM_ERROR = "Can not get the inputstream for file: ";
	/**
	 * TOUCH_ERROR ="Can not execute the touch operation  for file:";
	 */
	public static final String TOUCH_ERROR = "Can not execute the touch operation for file: ";
	/**
	 * DELETE_ERROR ="Can not execute the delete operation for file: ";
	 */
	public static final String DELETE_ERROR = "Can not execute the delete operation for file: ";
	

	/**
	 * @param message
	 * @param cause
	 */
	public FileHandlerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
