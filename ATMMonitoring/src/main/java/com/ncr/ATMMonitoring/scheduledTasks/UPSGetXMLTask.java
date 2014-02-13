/**
 * 
 */
package com.ncr.ATMMonitoring.scheduledTasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ncr.ATMMonitoring.service.UPSService;

/**
 * Scheduled task that reads a system directory and determine which file is
 * going to be processed. To know which file is going to be eligible, this class
 * uses {@link IOFileFilter}
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
	// folder object
	private File xmlfolder;
	// file filter to validate a file
	private IOFileFilter fileFilter;
	
	//will run at 0 minutes every hour every day
	private static final String CRON_CONF = "30 * * * * *";
	
	@Autowired
	private UPSService upsService;

	/**
	 * Scheduled task that checks the folder for xml, and call the service to
	 * begin the XML processing
	 */
	@Scheduled(cron = CRON_CONF)
	public void checkForUPSUpdates() {
		
		List<String> filesPath = new ArrayList<String>();
		logger.info("reading folder: "+this.xmlfolder);
		//TODO check why is entering the if when the folder is empty
		//only execute the get if is a directory and is not empty
		if (this.xmlfolder.exists() && this.xmlfolder.isDirectory() && this.xmlfolder.list().length > 0) {
			logger.info("Starting the XML folder check");

			// im not interest in checking subdirectories for that reason i put
			// the null
			@SuppressWarnings("unchecked")
			Collection<File> xmlFiles = (Collection<File>) FileUtils.listFiles(
					this.xmlfolder, this.fileFilter, null);

			// add all the paths to call the service
			for (File xmlFile : xmlFiles) {

				filesPath.add(xmlFile.getAbsolutePath());
			}
			logger.info("Obtained files: " + filesPath);
			//call the service
			this.upsService.storeUPSinfo(filesPath);

		} else if (!this.xmlfolder.isDirectory()) {

			logger.error("Can not read the UPS XML folder( " + xmlFoilderPath
					+ " ) because it is not a directory");

		} else {
			logger.error("Can not read the UPS XML folder( " + xmlFoilderPath
					+ " ) because it does not exist");
		}

	}

	/**
	 * Method that create the folder in the object instance
	 */
	@PostConstruct
	public void initTask() {
		// get the folder
		this.xmlfolder = new File(xmlFoilderPath);
		this.fileFilter = new SuffixFileFilter(FILE_EXTENSION);
		logger.info("XML folder: " + this.xmlFoilderPath);
	}

}
