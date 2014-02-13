/**
 * 
 */
package com.ncr.ATMMonitoring.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ncr.ATMMonitoring.handler.exception.FileHandlerException;

/**
 * Class that facilitate the manipulation of files in the fileSystem<br>
 * This class uses {@link FileUtils} to execute the IO Operations
 * 
 * @author Otto Abreu
 * 
 */
public class FileInDiskHandler {

	// logger
	private static final Logger logger = Logger
			.getLogger(FileInDiskHandler.class);

	/**
	 * Indicate that the method will throw an exception if an operation fails
	 * for one File
	 */
	public static final boolean FAILS_ON_ERROR = true;
	/**
	 * Indicate that the method will continue even if an operation fails for one
	 * or more Files
	 */
	public static final boolean IGNORES_ERROR = false;

	/**
	 * From a valid system path, returns the {@link InputStream}
	 * 
	 * @param file
	 *            String with a valid path in the system
	 * @return {@link InputStream}
	 * @throws FileHandlerException
	 *             If can not obtain the {@link InputStream}
	 */
	public InputStream getFile(String file) throws FileHandlerException {
		File fileIo = FileInDiskHandler.getFileIO(file);
		InputStream is = null;
		try {

			is = FileUtils.openInputStream(fileIo);

		} catch (IOException e) {
			throw new FileHandlerException(
					FileHandlerException.GET_INPUTSTREAM_ERROR, e);
		}
		return is;
	}

	/**
	 * From a valid list of system paths, returns the {@link InputStream}
	 * associated to each path<br>
	 * To set the failsOnError please use
	 * {@link FileInDiskHandler#FAILS_ON_ERROR}, that will make that this method
	 * throw an exception if an error occurs<br>
	 * If {@link FileInDiskHandler#IGNORES_ERROR} is set, this method will not
	 * stop if an error occurs, only will return the {@link InputStream} that
	 * was possible to obtain.<br>
	 * this method calls {@link FileInDiskHandler#getFile(String)}
	 * 
	 * @param files
	 *            List<String> with valid systems path
	 * @param failsOnError
	 *            indicate if this method will throw an exception if can not
	 *            obtain an {@link InputStream}. use
	 *            {@link FileInDiskHandler#FAILS_ON_ERROR}, or
	 *            {@link FileInDiskHandler#IGNORES_ERROR}
	 * @return List<InputStream>
	 * @throws FileHandlerException
	 *             if can not obtain a {@link InputStream} ( only if is set
	 *             (true) the fails on error)
	 */
	public List<InputStream> getFiles(List<String> files, boolean failsOnError)
			throws FileHandlerException {

		List<InputStream> fileIs = new ArrayList<InputStream>();

		for (String filePath : files) {

			try {
				InputStream is = this.getFile(filePath);
				fileIs.add(is);
			} catch (FileHandlerException e) {
				// only throws the exception if the boolean is true
				FileInDiskHandler.handleException(failsOnError, e,
						"can not get Inputstream from file: " + filePath
								+ " Operation will continue");

			}
		}
		return fileIs;
	}

	/**
	 * Executes the touch operation on the given file path
	 * 
	 * @param file
	 *            String with a valid path in the system
	 * @throws FileHandlerException
	 *             if can not execute the touch operation
	 */
	public void touch(String file) throws FileHandlerException {
		File fileIo = FileInDiskHandler.getFileIO(file);
		try {
			FileUtils.touch(fileIo);
		} catch (IOException e) {
			throw new FileHandlerException(FileHandlerException.TOUCH_ERROR, e);
		}
	}

	/**
	 * Executes the touch operation on the given files paths<br>
	 * To set the failsOnError please use
	 * {@link FileInDiskHandler#FAILS_ON_ERROR}, that will make that this method
	 * throw an exception if an error occurs<br>
	 * If {@link FileInDiskHandler#IGNORES_ERROR} is set, this method will not
	 * stop if an error occurs, only will touch the files that not generate an
	 * error <br>
	 * 
	 * this method calls {@link FileInDiskHandler#touch(String)}
	 * 
	 * 
	 * @param files
	 *            List<String> with valid systems path
	 * @param failsOnError
	 *            indicate if this method will throw an exception if can not
	 *            obtain an {@link InputStream}. use
	 *            {@link FileInDiskHandler#FAILS_ON_ERROR}, or
	 *            {@link FileInDiskHandler#IGNORES_ERROR}
	 * @throws FileHandlerException
	 *             if can not execute the touch operation on a file ( only if is
	 *             set (true) the fails on error)
	 */
	public void touch(List<String> files, boolean failsOnError)
			throws FileHandlerException {

		for (String filePath : files) {

			try {
				this.touch(filePath);
			} catch (FileHandlerException e) {
				// only throws the exception if the boolean is true
				FileInDiskHandler.handleException(failsOnError, e,
						"can not touch file: " + filePath
								+ " Operation will continue");
			}
		}
	}

	/**
	 * Method that deletes the given file<BR>
	 * It will try to delete using the {@link FileUtils#forceDelete(File)} and
	 * if it fails ( throws an IOException), will try to execute the
	 * {@link FileUtils#forceDeleteOnExit(File)}<br>
	 * 
	 * <b>IMPORTANT:<i> This method only deletes a file, if the path belongs to
	 * a directory nothing will be done</i></b>
	 * 
	 * @param file
	 *            String with a valid path in the system
	 * @throws FileHandlerException
	 *             if can not execute the delete operation
	 */
	public void delete(String file) throws FileHandlerException {
		File fileIo = FileInDiskHandler.getFileIO(file);
		if (!fileIo.isDirectory()) {
			try {

				FileUtils.forceDelete(fileIo);

			} catch (IOException e) {
				try {

					FileUtils.forceDeleteOnExit(fileIo);
				} catch (IOException e1) {
					throw new FileHandlerException(
							FileHandlerException.DELETE_ERROR, e);
				}

			}
		} else {

		}
	}

	/**
	 * 
	 * Method that deletes the given files<BR>
	 * To set the failsOnError please use
	 * {@link FileInDiskHandler#FAILS_ON_ERROR}, that will make that this method
	 * throw an exception if an error occurs<br>
	 * If {@link FileInDiskHandler#IGNORES_ERROR} is set, this method will not
	 * stop if an error occurs, only will delete the files that do not throw an
	 * exception<br>
	 * 
	 * <b>IMPORTANT:<i> This method only deletes files, if a path belongs to a
	 * directory nothing will be done to that path</i></b> <br>
	 * this method calls {@link FileInDiskHandler#delete(String)}
	 * 
	 * @param files
	 *            List<String> with valid systems path
	 * @param failsOnError
	 *            indicate if this method will throw an exception if can not
	 *            obtain an {@link InputStream}. use
	 *            {@link FileInDiskHandler#FAILS_ON_ERROR}, or
	 *            {@link FileInDiskHandler#IGNORES_ERROR}
	 * @throws FileHandlerException
	 *             if can not execute the delete operation on a file ( only if
	 *             is set (true) the fails on error)
	 */
	public void delete(List<String> files, boolean failsOnError)
			throws FileHandlerException {

		for (String filePath : files) {
			try {
				this.delete(filePath);
			} catch (FileHandlerException e) {
				// only throws the exception if the boolean is true
				FileInDiskHandler.handleException(failsOnError, e,
						"can not delete file: " + filePath
								+ " Operation will continue");
			}
		}
	}
	
	/**
	 * Returns a new Instance
	 * @return FileInDiskHandler
	 */
	public static FileInDiskHandler getInstance(){
		return new FileInDiskHandler();
	}

	/**
	 * Returns a {@link File} from the given path
	 * 
	 * @param file
	 *            String
	 * @return {@link File}
	 */
	private static File getFileIO(String file) {
		File fileIo = new File(file);
		return fileIo;
	}
	
	/**
	 * Rise the exception only if failsOnError was set to true, otherwise log the error as warning
	 * @param failsOnError
	 * @param e
	 * @param logMsg
	 * @throws FileHandlerException 
	 */
	private static void handleException(boolean failsOnError,
			FileHandlerException e, String logMsg) throws FileHandlerException {
		if (failsOnError) {
			throw e;

		} else {
			logger.warn(logMsg);
		}
	}
	
	/**
	 * private constructor
	 */
	private FileInDiskHandler(){
		
	}
	

}
