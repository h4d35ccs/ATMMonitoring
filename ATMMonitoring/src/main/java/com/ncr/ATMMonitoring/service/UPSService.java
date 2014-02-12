/**
 * 
 */
package com.ncr.ATMMonitoring.service;

import java.io.InputStream;
import java.util.List;

/**
 * Interface that define the operations related to the UPS
 * 
 * @author Otto Abreu
 * 
 */

public interface UPSService {
	/**
	 * Generates the XML files, extract the information and store it in the database
	 * @param xmlFiles List<String> with valid file paths
	 */
	void storeUPSinfo(List<String> xmlFiles);

	/**
	 * Generates the XML file, extract the information and store it in the database
	 * @param xmlFile InputStream with a valid XML file
	 */
	void storeUPSinfo(InputStream xmlFile);

	/**
	 * Generates the XML files, extract the information and store it in the database
	 * @param xmlFiles String with a valid file path
	 */
	void storeUPSinfo(String xmlFile);

	/**
	 * Deletes the information related to an UPS
	 * @param id
	 */
	void deleteUPS(int id);

}
