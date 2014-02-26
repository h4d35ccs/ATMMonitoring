/**
 * 
 */
package com.ncr.ATMMonitoring.scheduledTasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ncr.ATMMonitoring.handler.FileInDiskHandler;
import com.ncr.ATMMonitoring.handler.exception.FileHandlerException;
import com.ncr.ATMMonitoring.service.UPSService;

/**
 * Scheduled task that reads a system directory and determine which file is
 * going to be processed. To know which file is going to be eligible, this class<br>
 * This class uses the following properties:
 * <ul>
 * <li><b>config.upsfolder</b>: <U>Required</u>, holds the system path where the
 * xml are</li>
 * <li><i>config.upstask.behavior</I></li> Optional, define what to do with
 * successfully processed files. values are: delete,copy,move. if is not present
 * delete will be used
 * <li><i>config.upstask.copyto</i></li> Optional only if the
 * config.upstask.behaviuor is null or delete, otherwise is required to
 * determine where to put the processed files
 * </ul>
 * <br>
 * 
 * The default behavior of this class is to delete the processed xml files<br>
 * 
 * this class uses {@link IOFileFilter} and FileUtils
 * 
 * @author Otto Abreu
 * 
 */
@Component
public class UPSGetXMLTask {

	// file extension to check
	private static final String FILE_EXTENSION = ".xml";
	/* The logger. */
	private static final Logger logger = Logger.getLogger(UPSGetXMLTask.class);
	// folder system path
	@Value("${config.upsfolder}")
	private String xmlFoilderPath;
	@Value("${config.upstask.behavior:}")
	private String behaviuor= null;
	@Value("${config.upstask.copyto:}")
	private String copyFolder = null;
	// will run at 0 minutes every hour every day
	private static final String CRON_CONF = "0 * * * * *";
	// by default deletes the file
	private int copyDeleteMove = 0;
	// each successfully processed file will be copied to a folder maintaining
	// the original
	private static final int COPY_SUCCESSFULL_FILE = 1;
	// each successfully processed file will be permanent deleted
	private static final int DELETE_SUCCESSFULL_FILE = 0;
	// each successfully processed file will be copied to a folder deleting the
	// original
	private static final int MOVE_SUCCESSFULL_FILE = 2;

	@Autowired
	private UPSService upsService;

	/**
	 * Scheduled task that checks the folder for xml, and call the service to
	 * begin the XML processing
	 */
	@Scheduled(cron = CRON_CONF)
	public void checkForUPSUpdates() {

		List<String> filesPath = new ArrayList<String>();
		List<String> filesPathError = new ArrayList<String>();

		logger.info("reading folder: " + this.xmlFoilderPath);

		try {

			filesPath = FileInDiskHandler.getFilespath(FILE_EXTENSION,
					this.xmlFoilderPath);

			if (!filesPath.isEmpty()) {

				// call the service
				filesPathError = this.upsService.storeUPSinfo(filesPath);
				// if has errors, i will only manage those who was processed
				if (!filesPathError.isEmpty()) {
					// i get only those proceed
					@SuppressWarnings("unchecked")
					Collection<String> toProcess = (Collection<String>) CollectionUtils
							.disjunction(filesPathError, filesPath);
					this.handleSuccess(new ArrayList<String>(toProcess));

				} else {
					// all the files where processed
					this.handleSuccess(filesPath);
				}

			} else {

				logger.info(" UPS XML folder( " + xmlFoilderPath
						+ " ) is empty, nothing to process");
			}
		} catch (FileHandlerException e) {

			logger.error("Can not read the UPS XML folder( " + xmlFoilderPath
					+ " ) because it is not a directory");
		}

	}

	/**
	 * Method that create the folder in the object instance
	 */
	@PostConstruct
	public void initTask() {

		if (!StringUtils.isEmpty(this.behaviuor)) {
			
			this.behaviuor = this.behaviuor.trim();
			logger.info("Success file behaviuor: " + this.behaviuor);
			if (this.behaviuor.equalsIgnoreCase("delete")) {

				this.copyDeleteMove = DELETE_SUCCESSFULL_FILE;

			} else if (this.behaviuor.equalsIgnoreCase("copy")
					&& !StringUtils.isEmpty(this.copyFolder)) {

				this.copyDeleteMove = COPY_SUCCESSFULL_FILE;

			} else if (this.behaviuor.equalsIgnoreCase("move")
					&& !StringUtils.isEmpty(this.copyFolder)) {

				this.copyDeleteMove = MOVE_SUCCESSFULL_FILE;

			} else {
				// make the log and avoid execution
				// i make sure that the given values are delete, copy or move
				// if move or copy where set, must be a value in copyfolder
				if (!this.behaviuor.equalsIgnoreCase("move")
						&& !this.behaviuor.equalsIgnoreCase("copy")
						&& !this.behaviuor.equalsIgnoreCase("delete")) {
					String errorMsg = "The given value to the property config.upstask.behaviuor does not have a valid format "
							+ "( delete | move | copy) given:  "
							+ this.behaviuor;
					logger.error(errorMsg);
					throw new IllegalArgumentException(errorMsg);
				} else {

					String errorMsg = "While using copy or move it is required to set the property config.upstask.copyto "
							+ "with a valid folder path";
					logger.error(errorMsg);
					throw new IllegalArgumentException(errorMsg);
				}
			}
		}
		logger.info("XML folder: " + this.xmlFoilderPath);

	}

	/**
	 * handle the processed files
	 * 
	 * @param filesPath
	 */
	private void handleSuccess(List<String> filesPath) {

		switch (this.copyDeleteMove) {

		case DELETE_SUCCESSFULL_FILE:

			FileInDiskHandler
					.delete(filesPath, FileInDiskHandler.IGNORES_ERROR);
			break;

		case COPY_SUCCESSFULL_FILE:
			FileInDiskHandler.moveToFolder(filesPath, this.copyFolder,
					FileInDiskHandler.KEEP_FILE,
					FileInDiskHandler.IGNORES_ERROR);
			break;

		case MOVE_SUCCESSFULL_FILE:
			FileInDiskHandler.moveToFolder(filesPath, this.copyFolder,
					FileInDiskHandler.REMOVE_FILE,
					FileInDiskHandler.IGNORES_ERROR);
			break;
		}

	}


}
