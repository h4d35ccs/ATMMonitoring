package com.ncr.ATMMonitoring.service;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ncr.ATMMonitoring.dao.ScheduledUpdateDAO;
import com.ncr.ATMMonitoring.pojo.ScheduledUpdate;
import com.ncr.ATMMonitoring.pojo.Terminal;
import com.ncr.ATMMonitoring.socket.SocketService;

/**
 * The Class ScheduledUpdateServiceImpl.
 * 
 * Default implementation of the ScheduledUpdateService.
 * 
 * @author Jorge López Fernández (lopez.fernandez.jorge@gmail.com)
 */

@Service("scheduledUpdateService")
@Transactional
public class ScheduledUpdateServiceImpl implements ScheduledUpdateService {

    /** The logger. */
    static private Logger logger = Logger
	    .getLogger(ScheduledUpdateServiceImpl.class.getName());

    /** The scheduled update dao. */
    @Autowired
    private ScheduledUpdateDAO scheduledUpdateDAO;
    
    /** The socket service. */
    @Autowired
    private SocketService socketService;
    
    /** The query service. */
    @Autowired
    private QueryService queryService;

    /* (non-Javadoc)
     * @see com.ncr.ATMMonitoring.service.ScheduledUpdateService#addScheduledUpdate(com.ncr.ATMMonitoring.pojo.ScheduledUpdate)
     */
    @Override
    public void addScheduledUpdate(ScheduledUpdate scheduledUpdate) {
	scheduledUpdateDAO.addScheduledUpdate(scheduledUpdate);
    }

    /* (non-Javadoc)
     * @see com.ncr.ATMMonitoring.service.ScheduledUpdateService#listWeeklyScheduledUpdates()
     */
    @Override
    public List<ScheduledUpdate> listWeeklyScheduledUpdates() {
	return scheduledUpdateDAO.listWeeklyScheduledUpdates();
    }

    /* (non-Javadoc)
     * @see com.ncr.ATMMonitoring.service.ScheduledUpdateService#listMonthlyScheduledUpdates()
     */
    @Override
    public List<ScheduledUpdate> listMonthlyScheduledUpdates() {
	return scheduledUpdateDAO.listMonthlyScheduledUpdates();
    }

    /* (non-Javadoc)
     * @see com.ncr.ATMMonitoring.service.ScheduledUpdateService#removeScheduledUpdate(java.lang.Integer)
     */
    @Override
    public void removeScheduledUpdate(Integer id) {
	scheduledUpdateDAO.removeScheduledUpdate(id);
    }

    /* (non-Javadoc)
     * @see com.ncr.ATMMonitoring.service.ScheduledUpdateService#checkCurrentUpdates()
     */

    public void checkCurrentUpdates() {


		Set<String> ips = new HashSet<String>();
		Calendar currentDate = Calendar.getInstance();
		String logMsg = "No Updates founds";
				
		logger.info("Checking scheduled updates...");

		List<ScheduledUpdate> updates = scheduledUpdateDAO
				.listValidScheduledUpdates(currentDate);
		logger.info("updates found: "+updates.size());
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
		
		if(!ips.isEmpty()){
			logMsg = "Updates founds: "+ips;
			this.socketService.updateTerminalsSocket(ips);
		}
		
		logger.info(logMsg);
	
    }

    /* (non-Javadoc)
     * @see com.ncr.ATMMonitoring.service.ScheduledUpdateService#existsScheduledUpdate(com.ncr.ATMMonitoring.pojo.ScheduledUpdate)
     */
    @Override
    public boolean existsScheduledUpdate(ScheduledUpdate scheduledUpdate) {
	if (scheduledUpdate.getMonthDay() != null) {
	    return scheduledUpdateDAO
		    .existsMonthlyScheduledUpdate(scheduledUpdate);
	}
	if (scheduledUpdate.getWeekDay() != null) {
	    return scheduledUpdateDAO
		    .existsWeeklyScheduledUpdate(scheduledUpdate);
	}
	return false;
    }
}