/**
 * 
 */
package com.ncr.ATMMonitoring.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ncr.ATMMonitoring.pojo.Ups;

/**
 * 
 * Dao concrete class that interact with the UPS table
 * 
 * @author Otto Abreu
 * 
 */
@Repository
public class UpsDAOImp extends AbstractGenericDAO<Ups> implements UpsDAO {

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ncr.ATMMonitoring.dao.UpsDAO#getUps(int)
	 */
	public Ups getUps(int id) {
		return this.get(id);

	}

	// logger
	private static final Logger logger = Logger.getLogger(UpsDAOImp.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.dao.UpsDAO#getUpsBySerialNumber(java.lang.String)
	 */
	@Override
	public Ups getUpsBySerialNumber(String seriesNumber) {

		Ups ups = null;
		logger.debug("series number: "+seriesNumber);
		List<Ups> fetch = this.listUps(Restrictions.eq("seriesNumber",
				seriesNumber));
		if (fetch != null && !fetch.isEmpty()) {

			ups = fetch.get(0);

		}

		return ups;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ncr.ATMMonitoring.dao.UpsDAO#removeUps(int)
	 */
	public void removeUps(int id) {
		this.delete(id);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.dao.UpsDAO#updateUps(com.ncr.ATMMonitoring.pojo
	 * .Ups)
	 */
	public void updateUps(Ups ups) {
		this.update(ups);

	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ncr.ATMMonitoring.dao.UpsDAO#listAllUps()
	 */
	public List<Ups> listAllUps() {
		return this.list();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.dao.UpsDAO#addUps(com.ncr.ATMMonitoring.pojo.Ups)
	 */
	public void addUps(Ups ups) {
		this.add(ups);

	}

	@SuppressWarnings("unchecked")
	public List<Ups> listUps(Criterion... criterions) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(
				Ups.class);
		for (Criterion rest : criterions) {
			crit.add(rest);
		}
		return (List<Ups>) crit.list();
	}
}
