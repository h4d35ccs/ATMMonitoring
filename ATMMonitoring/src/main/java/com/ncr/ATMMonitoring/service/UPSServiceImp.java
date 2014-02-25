/**
 * 
 */
package com.ncr.ATMMonitoring.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ncr.ATMMonitoring.dao.UpsDAO;
import com.ncr.ATMMonitoring.handler.FileInDiskHandler;
import com.ncr.ATMMonitoring.parser.ParseUPSChainBuilder;
import com.ncr.ATMMonitoring.parser.dto.UPSInfo;
import com.ncr.ATMMonitoring.parser.exception.NoParserFoundException;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;
import com.ncr.ATMMonitoring.pojo.Ups;

/**
 * Concrete class that implements {@link UPSService}
 * 
 * @author Otto Abreu
 * 
 */
@Service
@Transactional
public class UPSServiceImp implements UPSService {

	// logger
	private static final Logger logger = Logger.getLogger(UPSServiceImp.class);

	@Autowired
	private UpsDAO upsDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.util.List)
	 */
	@Override
	public List<String> storeUPSinfo(List<String> xmlFiles) {
		List<String> errors = new ArrayList<String>();

		for (String file : xmlFiles) {

			if (!this.storeUPSinfo(file)) {
				errors.add(file);
			}

		}
		logger.info("XML parsing process end normally: " + errors.isEmpty());
		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.util.Collection
	 * )
	 */
	@Override
	public List<InputStream> storeUPSinfo(Collection<InputStream> xmlFiles) {
		List<InputStream> errors = new ArrayList<InputStream>();

		for (InputStream is : xmlFiles) {

			if (!this.storeUPSinfo(is)) {
				errors.add(is);
			}
		}

		logger.info("XML parsing process end normally: " + errors.isEmpty());
		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.io.InputStream
	 * )
	 */
	@Override
	public boolean storeUPSinfo(InputStream xmlFile) {
		boolean parsed = false;
		try {

			this.parseFile(xmlFile);
			parsed = true;

		} catch (ParserException e) {
			logger.error("Can not parse the file: " + xmlFile
					+ " due an error: ", e);
		} catch (XMLNotReadableException e) {
			logger.error("Can not Read the file: " + xmlFile, e);
		} catch (NoParserFoundException e) {
			logger.error("The file " + xmlFile
					+ " can not be processed by any configured parser ", e);
		}
		logger.info("XML parsing process end normally: " + parsed);
		return parsed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.lang.String)
	 */
	@Override
	public boolean storeUPSinfo(String xmlFile) {
		boolean parsed = false;

		InputStream xml = FileInDiskHandler.getFileInputStream(xmlFile);
		parsed = this.storeUPSinfo(xml);

		parsed = true;
		logger.info("XML parsing process end normally: " + parsed);
		return parsed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ncr.ATMMonitoring.service.UPSService#deleteUPS(int)
	 */
	@Override
	public void deleteUPS(int id) {

		this.upsDao.removeUps(id);
		logger.info("removed ups with id: " + id);
	}

	/**
	 * Save the ups info in the Database
	 * 
	 * @param xmlFiles
	 *            UPSInfo
	 */

	private void handleParserSucess(UPSInfo file) {

		String seriesNumber = file.getSeriesNumber();
		Ups ups = this.getEntity(file);
		// verify if is an update or a new information
		Ups toUpdate = this.upsDao.getUpsBySerialNumber(seriesNumber);

		if (toUpdate == null) {
			logger.info("adding new UPS to the Database with series number: "
					+ seriesNumber);
			this.upsDao.addUps(ups);

		} else {
			logger.info("Updating ups with: " + seriesNumber + " and id: "
					+ toUpdate.getId());
			ups.setId(toUpdate.getId());
			this.upsDao.updateUps(ups);
		}

		if (file.getExtraInfo() != null) {
			// TODO calls the class/method responsible to handle this part of
			// the object
		}

	}

	/**
	 * Execute the parser to the given Inputstream
	 * 
	 * @param xml
	 *            {@link InputStream} with the XML
	 * @return UPSInfo with the extracted info
	 * @throws ParserException
	 * @throws XMLNotReadableException
	 * @throws NoParserFoundException
	 */
	private void parseFile(InputStream xml) throws ParserException,
			XMLNotReadableException, NoParserFoundException {

		UPSInfo xmlInfo = null;
		ParseUPSChainBuilder chainBuilder = ParseUPSChainBuilder.getInstance();
		xmlInfo = chainBuilder.parse(xml);
		logger.debug("parsed " + xmlInfo);
		this.handleParserSucess(xmlInfo);
	}

	/**
	 * Fills the entity with the data from the DTO
	 * 
	 * @param info
	 *            UPSInfo
	 * @return Ups
	 */
	private Ups getEntity(UPSInfo info) {

		Ups ups = new Ups(info.getIp(), info.getFirmware(),
				info.getRunningStatus(), info.getChargePercentage(),
				info.getExpensePercentage(), info.getAlarmMsg(),
				info.getUpsType(), info.getUpsModel(), info.getSeriesNumber(),
				info.getRunningTimeMilisec(), info.getAutonomyMilisec(),
				info.getNumPosition(), info.getAudFmo(),
				info.getGeneralStatusMsg(), info.getLastExecutionDate(),
				info.getOriginalXML());
		return ups;

	}
}
