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

import com.ncr.ATMMonitoring.dao.ScheduledUpdateDAO;
import com.ncr.ATMMonitoring.pojo.ScheduledUpdate;
import com.ncr.ATMMonitoring.pojo.Terminal;
import com.ncr.ATMMonitoring.service.QueryService;
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
	private static final String CRON_CONF = "30 * * * * *";

	
	/* *********Injected objects**** */

	/* The socket service. */
	@Autowired
	private SocketService socketService;

	/* The query service. */
	@Autowired
	private QueryService queryService;

	/* The scheduled update dao. */
	@Autowired
	private ScheduledUpdateDAO scheduledUpdateDAO;

	/**
	 * Method that checks if there are some Scheduled updates to execute and if there are some, 
	 * runs execute the current updates.
	 */
	@Scheduled(cron = CRON_CONF)
	public void checkCurrentUpdates() {

		Set<String> ips = new HashSet<String>();
		Calendar currentDate = Calendar.getInstance();

		logger.info("Checking scheduled updates...:)");

		List<ScheduledUpdate> updates = scheduledUpdateDAO
				.listValidScheduledUpdates(currentDate);

		for (ScheduledUpdate update : updates) {

			if (update.getQuery() == null) {
				logger.info("General update found for instant "
						+ DateFormat.getDateTimeInstance(DateFormat.SHORT,
								DateFormat.SHORT).format(currentDate.getTime()));

				this.socketService.updateAllTerminalsSocket();
				return;
			}
		}

		for (ScheduledUpdate update : updates) {

			List<Terminal> terminals = queryService.executeQuery(update
					.getQuery());

			for (Terminal terminal : terminals) {
				ips.add(terminal.getIp());
			}
		}
		this.socketService.updateTerminalsSocket(ips);
	}
}
