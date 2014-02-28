package com.ncr.ATMMonitoring.scheduledTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ncr.ATMMonitoring.service.ScheduledUpdateService;

/**
 * Class that execute a scheduled task related to check if there are some Scheduled updates to execute
 * @author Otto Abreu
 * 
 */
@Component
public class ScheduledUpdateTask {

	/* The logger. */
	static final private Logger logger = Logger
			.getLogger(ScheduledUpdateTask.class);

	/* atributes* */
	
	//will run at 0 minutes every hour every day
	private static final String CRON_CONF = "0 * * * * *";

	
	/* *********Injected objects**** */

	@Autowired
	private ScheduledUpdateService scheduledUpdateService;
	


	/**
	 * Method that checks if there are some Scheduled updates to execute and if there are some, 
	 * runs execute the current updates.
	 */
	@Scheduled(cron = CRON_CONF)
	@Transactional
	public void callToCheckCurrentUpdates() {
		logger.info("Calling service for checking Current Updates ");
		this.scheduledUpdateService.checkCurrentUpdates();
		logger.info("Calling service done");
	}
}
