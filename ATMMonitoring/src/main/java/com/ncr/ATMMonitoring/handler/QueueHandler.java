/**
 * 
 */
package com.ncr.ATMMonitoring.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ncr.ATMMonitoring.handler.exception.QueueHandlerException;

/**
 * Class that encapsulate the ATM ip to be updated queue <br>
 * To use this class is mandatory to load first the queue using the
 * {@link QueueHandler#loadQueue()} method, then execute the operations (add,
 * remove) and finally save the status using {@link QueueHandler#save()}. If the
 * save method is not called, the actual status of the queue will not be save in
 * disk<br>
 * This class need that the properties <b>config.queue.filestore.path</b> and
 * <b>config.queue.file.name</b> are configured as follows<br>
 * <ul>
 * <li><b>config.queue.filestore.path</b>: must specify a valid system path and
 * end with a /</li>
 * <li><b>config.queue.file.name</b> must specify a file name with extension,
 * example: queue.bin</li>
 * </ul>
 * 
 * @author Otto Abreu
 * 
 */
@Component
public class QueueHandler {

	private final static Logger logger = Logger.getLogger(QueueHandler.class);
	// the "queue" of ips linkedHashSet remembers the order
	private Set<String> terminalsIpQueue;
	// holds the system path where the queue is stored

	@Value("${config.queue.filestore.path}")
	private String filestorePath;
	@Value("${config.queue.file.name}")
	private String queueName;

	/**
	 * Returns the queue Object, this method perform the deserialization process
	 * 
	 * @return Set<String>
	 * @throws QueueHandlerException
	 *             if can 't execute the operation
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized void loadQueue() throws QueueHandlerException {

		FileInputStream fis = null;
		ObjectInputStream in = null;

		Set<String> queue = null;

		try {
			fis = new FileInputStream(returnQueuePath());
			in = new ObjectInputStream(fis);
			queue = (Set<String>) in.readObject();
			this.terminalsIpQueue = Collections.synchronizedSet(queue);
			logger.info("Queue Loaded: " + this.terminalsIpQueue);

		} catch (ClassNotFoundException e) {
			// its impossible to throw a CNFE with Set<String> that's the reason
			// why this try-catch is "swallowing" the exception

		} catch (FileNotFoundException e) {
			//if can not find the file asumes that the queue must be created
			logger.info("Creating new Queue");
			this.terminalsIpQueue = Collections.synchronizedSet(new LinkedHashSet<String>());
		} catch (IOException e) {
			logger.error(QueueHandlerException.READ_IO_ERROR, e);
			throw new QueueHandlerException(
					QueueHandlerException.READ_IO_ERROR, e);
		} catch (Exception e) {
			logger.error(QueueHandlerException.GENERAL_ERROR, e);
			throw new QueueHandlerException(
					QueueHandlerException.GENERAL_ERROR, e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.warn("can not close the resouce", e);
			}
		}

	}

	/**
	 * Saves the queue in filesystem. This method perform the serialization
	 * process
	 * 
	 * @param terminalsIpQueue
	 * @throws QueueHandlerException
	 *             if can not save the current status of the queue
	 */
	public synchronized void save() throws QueueHandlerException {

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(returnQueuePath());
			out = new ObjectOutputStream(fos);
			out.writeObject(this.terminalsIpQueue);
			// written

			logger.info("Queue status was written in disk");

		} catch (FileNotFoundException e) {
			logger.error(QueueHandlerException.FILE_PATH_NOT_FOUND, e);
			throw new QueueHandlerException(
					QueueHandlerException.FILE_PATH_NOT_FOUND, e);
		} catch (IOException e) {
			logger.error(QueueHandlerException.SAVE_IO_ERROR, e);
			throw new QueueHandlerException(
					QueueHandlerException.SAVE_IO_ERROR, e);
		} catch (Exception e) {
			logger.error(QueueHandlerException.GENERAL_ERROR, e);
			throw new QueueHandlerException(
					QueueHandlerException.GENERAL_ERROR, e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.warn("can not close the resouce", e);
			}
		}

	}
	
	/***
	 * Clean the queue and then delete the file from the filesystem
	 * @throws QueueHandlerException if can not delete the file or remove all the elements
	 */
	public synchronized void destroy() throws QueueHandlerException{
		String path = this.returnQueuePath();
		try {
			this.removeAll();
			FileUtils.forceDelete(new File(path));
		} catch (IOException e) {
			logger.error(QueueHandlerException.DESTROY_ERROR, e);
			throw new QueueHandlerException(
					QueueHandlerException.DESTROY_ERROR, e);
		}
		
	}

	/**
	 * Adds an ip to the queue<br>
	 * this will <B>NOT</b> save the queue state in filesystem
	 * 
	 * @param ip
	 * @throws QueueHandlerException
	 *             if the queue is null
	 */
	public void add(String ip) throws QueueHandlerException {
		this.checkNullQueue();
		this.validateipString(ip);
		this.terminalsIpQueue.add(ip);
		logger.debug("added:" + ip);
	}

	/**
	 * Adds all the ips from the given collection. <br>
	 * This will <B>NOT</b> save the queue state in filesystem
	 * 
	 * @param ips
	 *            Collection
	 * @throws QueueHandlerException
	 *             if the queue is null
	 */
	public void addAll(Collection<String> ips) throws QueueHandlerException  {
		this.checkNullQueue();
		// make sure that a valid ip is being added
		for (String ip : ips) {
			try {
				this.validateipString(ip);
			} catch (QueueHandlerException e) {
				// will try to add all and only exclude those who are not ips
				logger.warn(e.getMessage());
			}
		}
		this.terminalsIpQueue.addAll(ips);
		logger.debug("added:" + ips);
	}

	/**
	 * Tells if the queue is empty e<br>
	 * this will <B>NOT</b> save the queue state in filesystem
	 * 
	 * @return boolean
	 * @throws QueueHandlerException
	 *             if the queue is null
	 */
	public boolean isEmpty() throws QueueHandlerException {
		this.checkNullQueue();
		return this.terminalsIpQueue.isEmpty();
	}

	/**
	 * removes all the elements of the queue, this will not execute the save
	 * process <br>
	 * this will <B>NOT</b> save the queue state in filesystem
	 * 
	 * @throws QueueHandlerException
	 *             if the queue is null
	 */
	public void removeAll() throws QueueHandlerException {
		this.checkNullQueue();
		this.terminalsIpQueue.clear();
		logger.debug("queue clean: " + this.terminalsIpQueue.isEmpty());
	}

	/**
	 * Removes the given IP from the queue <br>
	 * this will <B>NOT</b> save the queue state in filesystem
	 * 
	 * @param ip
	 *            String
	 * @throws QueueHandlerException
	 *             if the queue is null
	 */
	public void removeElement(String ip) throws QueueHandlerException {
		this.checkNullQueue();
		this.terminalsIpQueue.remove(ip);
	}

	/**
	 * Removes all the given IP from the queue <br>
	 * this will <B>NOT</b> save the queue state in filesystem
	 * 
	 * @param ip
	 *            String
	 * @throws QueueHandlerException
	 *             if the queue is null
	 */
	public void removeElements(Collection<String> ips)
			throws QueueHandlerException {
		this.checkNullQueue();
		this.terminalsIpQueue.removeAll(ips);
	}

	/**
	 * Returns the full qualified path where the queue is saved or read
	 * 
	 * @return String
	 */
	private String returnQueuePath() {
		String path = filestorePath + filestorePath;
		logger.debug("filepath:" + path);
		return path;
	}

	/**
	 * 
	 * Method that check if is possible to execute an operation over the queue
	 * 
	 * @throws QueueHandlerException
	 */
	private void checkNullQueue() throws QueueHandlerException {
		if (this.terminalsIpQueue == null) {
			logger.error(QueueHandlerException.NULL_OPERATION_ERROR);
			throw new QueueHandlerException(
					QueueHandlerException.NULL_OPERATION_ERROR);
		}
	}

	/**
	 * Validates that the value beeing add is a valid ip
	 * 
	 * @param ip
	 *            String
	 * @throws QueueHandlerException
	 *             if the string is not a valid ipv4 or ipv6
	 */
	private void validateipString(String ip) throws QueueHandlerException {

		String ipv4Regex = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
		//http://stackoverflow.com/questions/53497/regular-expression-that-matches-valid-ipv6-addresses
		String ipv6Regex = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|"
				+ "([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|"
				+ "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|"
				+ "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|"
				+ "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|"
				+ "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|"
				+ "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)"
				+ "|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|"
				+ "::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])"
				+ "|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";

		Pattern patIpv4 = Pattern.compile(ipv4Regex);
		Pattern patIpv6 = Pattern.compile(ipv6Regex);

		Matcher matcherIpv4 = patIpv4.matcher(ip);
		Matcher matcherIpv6 = patIpv6.matcher(ip);

		if (!matcherIpv4.find() && !matcherIpv6.find()) {
			logger.error(QueueHandlerException.NOT_AN_IP + ip);
			throw new QueueHandlerException(QueueHandlerException.NOT_AN_IP
					+ ip);
		}

	}

}
