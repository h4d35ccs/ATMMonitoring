/**
 * 
 */
package com.ncr.ATMMonitoring.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.ncr.ATMMonitoring.pojo.Ups;

/**
 * Dao interface class that interact with the UPS table
 * 
 * @author Otto Abreu
 * 
 */
public interface UpsDAO {
	/**
	 * returns the UPS entity that matches the given id
	 * 
	 * @param id
	 *            int
	 */
	Ups getUps(int id);

	/**
	 * Removes from the DB the UPS that matches with the given id
	 * 
	 * @param id
	 */
	void removeUps(int id);

	/**
	 * Updates the data of an ups
	 * 
	 * @param ups
	 */
	void updateUps(Ups ups);

	/**
	 * Returns all the ups from the db
	 * 
	 * @return
	 */
	List<Ups> listAllUps();

	/**
	 * Adds a new Ups to the database
	 * 
	 * @param ups
	 */
	void addUps(Ups ups);

	/**
	 * Returns the ups that matches the given criterions. To add a criterion
	 * please use {@link Restrictions} or another class that implement
	 * {@link Criterion}
	 * 
	 * @param criterions
	 *             {@link Criterion}
	 * @return List<Ups>
	 */
	public List<Ups> listUps(Criterion...criterions);

	/**
	 * Returns a Ups by the given series number
	 * 
	 * @param seriesNumber
	 *            String
	 * @return Ups
	 */
	public Ups getUpsBySerialNumber(String seriesNumber);
	 
	 /** Returns a Ups by the given series number and model
	 * 
	 * @param seriesNumber
	 *            String
	 * @param model
	 *            String
	 *            
	 * @return Ups
	 */
	public Ups getUpsBySerialNumberAndModel(String seriesNumber, String model);
	
	

}
