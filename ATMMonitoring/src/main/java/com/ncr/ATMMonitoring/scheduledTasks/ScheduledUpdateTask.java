package com.ncr.ATMMonitoring.scheduledTasks;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ncr.ATMMonitoring.dao.ScheduledUpdateDAO;
import com.ncr.ATMMonitoring.pojo.ScheduledUpdate;
import com.ncr.ATMMonitoring.pojo.Terminal;
import com.ncr.ATMMonitoring.service.QueryService;
import com.ncr.ATMMonitoring.service.ScheduledUpdateService;
import com.ncr.ATMMonitoring.socket.SocketService;

/**
 * Class that execute a scheduled task related to check if there are some Scheduled updates to execute
 * @author Jorge López Fernández (lopez.fernandez.jorge@gmail.com)
 * 
 */
@Component
public class ScheduledUpdateTask {

	/* The logger. */
	static final private Logger logger = Logger
			.getLogger(ScheduledUpdateTask.class);

	/* atributes* */
	
	//will run at 0 minutes every hour every day
	private static final String CRON_CONF = "59 * * * * *";

	
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
