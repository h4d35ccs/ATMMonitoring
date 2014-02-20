/**
 * 
 */
package com.ncr.ATMMonitoring.scheduledTasks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ncr.ATMMonitoring.socket.SocketService;

/**
 * @author Otto Abreu
 * 
 */
@Component
public class ProcessIpsTask {

	/* The logger. */
	static private Logger logger = Logger.getLogger(ProcessIpsTask.class);

	@Autowired
	private SocketService socketService;
	
	//will run at 0 minutes every hour every day
	private static final String CRON_CONF = "30 * * * * *";

	@Scheduled(cron = CRON_CONF)
	public void processIps() {
		logger.info("Calling service for check the IPs waiting for update...");
		this.socketService.processAwaitingIps();
		logger.info("done");
	}

}
