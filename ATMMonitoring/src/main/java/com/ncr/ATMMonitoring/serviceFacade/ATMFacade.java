/**
 * 
 */
package com.ncr.ATMMonitoring.serviceFacade;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.ncr.ATMMonitoring.pojo.BankCompany;
import com.ncr.ATMMonitoring.pojo.Installation;
import com.ncr.ATMMonitoring.pojo.ScheduledUpdate;
import com.ncr.ATMMonitoring.pojo.Terminal;
import com.ncr.ATMMonitoring.pojo.TerminalConfig;
import com.ncr.ATMMonitoring.pojo.TerminalModel;
import com.ncr.ATMMonitoring.service.InstallationService;
import com.ncr.ATMMonitoring.service.ScheduledUpdateService;
import com.ncr.ATMMonitoring.service.TerminalConfigService;
import com.ncr.ATMMonitoring.service.TerminalModelService;
import com.ncr.ATMMonitoring.service.TerminalService;

/**
 * Service that is an implementation of the Facade pattern that provides a
 * simplified interface for the interaction with the services
 * {@link TerminalService}, {@link TerminalModelService},
 * {@link ScheduledUpdateService}, {@link TerminalConfigService} and
 * {@link InstallationService}.<br>
 *  Holds all the operations related to the ATMs, such as scheduling updates and Cruds operations
 * 
 * @author Otto Abreu
 * 
 */
public interface ATMFacade {
	/**
	 * constant that specify an update operation UPDATE = 0;
	 */
	int UPDATE = 0;
	/**
	 * constant that specify an add operation ADD = 1;
	 */
	int ADD = 1;
	/**
	 * to define a search for monthly updates MONTHLY = 0;
	 */
	int MONTHLY = 0;
	/**
	 * to define a search for weekly updates WEEKLY = 1;
	 */
	int WEEKLY = 1;
	/**
	 * indicate that the search will return the elements order in ascendent
	 * order
	 */
	String ORDER_ASC = "asc";
	/**
	 * indicate that the search will return the elements order in descendent
	 * order
	 */
	String ORDER_DESC = "desc";

	/**
	 * Returns a list with all the ScheduledUpdate that matches the given update
	 * type <br>
	 * To return a list with weekly updates please use {@link ATMFacade#WEEKLY},
	 * To return a list with monthly updates please use
	 * {@link ATMFacade#MONTHLY}
	 * 
	 * @param updateType
	 *            int indicate the type of update to fetch ( weekly or monthly)
	 * @return List<ScheduledUpdate>
	 */
	List<ScheduledUpdate> listScheduledUpdates(int updateType);

	/**
	 * Adds the given {@link ScheduledUpdate}
	 * 
	 * @param scheduledUpdate
	 *            ScheduledUpdate to be added
	 */
	void addScheduledUpdate(ScheduledUpdate scheduledUpdate);

	/**
	 * Removes the {@link ScheduledUpdate} that matches the given id
	 * 
	 * @param updateId
	 */
	void removeScheduledUpdate(int updateId);

	/**
	 * Tells if exist in the Database a {@link ScheduledUpdate} with the given
	 * Id<br>
	 * if a {@link ScheduledUpdate} is found returns true, false otherwise
	 * 
	 * @param updateId
	 *            int
	 * @return boolean
	 */
	boolean existScheduledUpdate(int updateId);

	/**
	 * Adds a physical ATM machine to the Database<br>
	 * <i>Note:</i> An ATM machine refers to the physical configuration of the
	 * ATM, such as memory, Hard drive, etc. see ({@link TerminalConfig}
	 * 
	 * @param terminalConfig
	 */
	void addATMMachine(TerminalConfig terminalConfig);

	/**
	 * Returns a list with all the ATM machines in the Database<br>
	 * <i>Note:</i> An ATM machine refers to the physical configuration of the
	 * ATM, such as memory, Hard drive, etc. see ({@link TerminalConfig}
	 * 
	 * @return List<TerminalConfig>
	 */
	List<TerminalConfig> listATMMachines();

	/**
	 * Return the {@link TerminalConfig} that matches with the given id
	 * 
	 * @param atmId
	 *            int
	 * @return TerminalConfig
	 */
	TerminalConfig getATMMachine(int atmId);

	/**
	 * Method that returns a list of ATM ({@link Terminal}) using the criteria
	 * given<br>
	 * <b><i>Important</i>: ONLY ONE PARAMETER CAN BE USED OF
	 * banksCompanies,bank AND atmId , THIS MEANS ONLY ONE CAN NOT BE NULL,
	 * OTHERWISE WILL EXECUTE THE FIRST NOT NULL PARAM RECEIVED</b><br>
	 * Examples of use:<br>
	 * <ul>
	 * <li>Search by several Bank companies: listATM(banksCompanies,null,
	 * null,sort, order);</li>
	 * <li>Search by ONE Bank company: listATM(null,bankCompany, null,sort,
	 * order);</li>
	 * <li>Search by Terminal ID: listATM(null,null, ids,null, null);</li>
	 * <li>Return all the Terminals: listATM(null,null, null,null, null);</li>
	 * </ul>
	 * <i>Note:</i> the params sort and order only will affect the search by
	 * Bank Companies or by a single bank<br>
	 * To specify the param order please use: {@link ATMFacade#ORDER_ASC} for an
	 * ascendant order and {@link ATMFacade#ORDER_DESC} for descendant
	 * 
	 * @param banksCompanies
	 *            List<BankCompany> if not null will search based on the given
	 *            Bank companies
	 * @param bank
	 *            BankCompany if not null will search based on the given Bank
	 * @param atmId
	 *            if not null will search based on the given list of ids
	 * @param sort
	 *            String optional param used when listing by Banks, will be the
	 *            value to sort the result
	 * @param order
	 *            String optional param used when listing by Banks. give the
	 *            order, can be ascendant or descendant
	 * @return List<Terminal>
	 */
	List<Terminal> listATM(List<BankCompany> banksCompanies, BankCompany bank,
			List<Integer> atmId, String sort, String order);

	/**
	 * Method that returns an ATM ({@link Terminal}) using the criteria given<br>
	 * <b><i>Important</i>: ONLY ONE PARAMETER CAN BE USED OF
	 * atmId,serialNumber, matricula, ip AND macAdress , THIS MEANS ONLY ONE CAN
	 * NOT BE NULL, OTHERWISE WILL EXECUTE THE FIRST NOT NULL PARAM RECEIVED</b><br>
	 * Examples of use:<br>
	 * <ul>
	 * <li>Search by terminals ID: getATM(atmId,null, null, null,null);</li>
	 * <li>Search by serial number: getATM(null,serialNumber, null, null,null);</li>
	 * <li>Search by matricula: getATM(null,null, matricula, null,null);</li>
	 * *
	 * <li>Search by IP: getATM(null,null, null, ip,null);</li>
	 * <li>Search by MAC Address: getATM(null,null, null, null,macAddress);</li>
	 * </ul>
	 * 
	 * @param atmId
	 *            int if not null will search based on the given list of ids
	 * @param serialNumber
	 *            String if not null will search based on the given serial
	 *            number
	 * @param matricula
	 *            String if not null will search based on the given matricula
	 * @param ip
	 *            String if not null will search based on the given ip
	 * @param macAddress
	 *            String if not null will search based on the given Mac Address
	 * @return Terminal
	 */
	Terminal getATM(int atmId, String serialNumber, String matricula,
			String ip, String macAddress);

	/**
	 * Execute an add or update operation on the given {@link Terminal}<br>
	 * To execute an add operation please use {@link ATMFacade#ADD}, To update
	 * please use {@link ATMFacade#UPDATE} in the operType param
	 * 
	 * @param terminal
	 *            Terminal to be updated or added
	 * @param operType
	 *            int that indicate if the given Terminal will be added or
	 *            updated
	 */
	void addUpdateATM(Terminal terminal, int operType);

	/**
	 * Adds the info of the ATM using the given {@link InputStream}<br>
	 * The file must be a valid JSON string used by the ATMs
	 * 
	 * @param file
	 *            Inputstream
	 */
	void addATMByFile(InputStream file);

	/**
	 * Returns all the models of ATMs registered in the Database
	 * 
	 * @return List<TerminalModel>
	 */
	List<TerminalModel> listATMModels();

	/**
	 * Returns all the models of ATMs registered in the Database but organized
	 * by the manufacturer<br>
	 * The key of the returning map is the name of the manufacturer
	 * 
	 * @return Map<String,List<TerminalModel>
	 */
	Map<String, List<TerminalModel>> listATMModelsByManufacturer();

	/**
	 * Execute an add or update operation on the given {@link TerminalModel}<br>
	 * <br>
	 * To execute an add operation please use {@link ATMFacade#ADD}, To update
	 * please use {@link ATMFacade#UPDATE} in the operType param
	 * 
		 * @param terminalModel
	 *            TerminalModel to be updated or added
	 * @param operType
	 *            int that indicate if the given Terminal will be added or
	 *            updated
	 */
	void addUpdateATMModel(TerminalModel terminalModel, int operType);
	
	/**
	 * Removes the {@link TerminalModel} that matches the given id
	 * 
	 * @param atmModelId
	 */
	void removeATMModel(int atmModelId);
	
	/**
	 * Execute an add or update operation on the given {@link Installation}<br>
	 * <br>
	 * To execute an add operation please use {@link ATMFacade#ADD}, To update
	 * please use {@link ATMFacade#UPDATE} in the operType param
	 * 
		 * @param installation
	 *            Installation to be updated or added
	 * @param operType
	 *            int that indicate if the given Terminal will be added or
	 *            updated
	 */
	void addUpdateATMInstallation(Installation installation, int operType);
	
	
	/**
	 * Returns all the installations of ATMs registered in the Database
	 * 
	 * @return List<Installation>
	 */
	List<Installation> listATMInstallations();
	
	/**
	 * Return the {@link Installation} that matches with the given id
	 * 
	 * @param installationId
	 *            int
	 * @return Installation
	 */
	Installation getATMInstallation(int installationId);
	
	/**
	 * Removes the {@link Installation} that matches the given id
	 * 
	 * @param installationId
	 */
	void removeATMInstallation(int installationId);
	
}
