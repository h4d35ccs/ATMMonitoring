/**
 * 
 */
package com.ncr.ATMMonitoring.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ncr.ATMMonitoring.handler.FileInDiskHandler;
import com.ncr.ATMMonitoring.handler.exception.FileHandlerException;
import com.ncr.ATMMonitoring.parser.ParseUPSChainBuilder;
import com.ncr.ATMMonitoring.parser.dto.UPSInfo;
import com.ncr.ATMMonitoring.parser.exception.NoParserFoundException;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;

/**
 * Concrete class that implements {@link UPSService}
 * 
 * @author Otto Abreu
 * 
 */
@Service
public class UPSServiceImp implements UPSService {

	// logger
	private static final Logger logger = Logger.getLogger(UPSServiceImp.class);

	/**
	 */
	public UPSServiceImp() {

	}

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

			try {

				InputStream xml = FileInDiskHandler.getFileInputStream(file);

				this.parseFile(xml);

			} catch (FileHandlerException e) {
				logger.error("Can not get the Inputstream of file: " + file, e);
				errors.add(file);
			} catch (ParserException e) {
				logger.error("Can not parse the file: " + file
						+ " due an error: ", e);
				errors.add(file);
			} catch (XMLNotReadableException e) {
				logger.error("Can not Read the file: " + file, e);
				errors.add(file);
			} catch (NoParserFoundException e) {
				logger.error("The file " + file
						+ " can not be processed by any configured parser ", e);
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

			try {

				this.parseFile(is);

			} catch (ParserException e) {
				logger.error("Can not parse the inputstream due an error: ", e);
				errors.add(is);
			} catch (XMLNotReadableException e) {
				logger.error("Can not Read the file (inputstream)", e);
			} catch (NoParserFoundException e) {
				logger.error(
						"The file can not be processed by any configured parser ",
						e);
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

		try {

			InputStream xml = FileInDiskHandler.getFileInputStream(xmlFile);
			this.parseFile(xml);

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
	 * @see com.ncr.ATMMonitoring.service.UPSService#deleteUPS(int)
	 */
	@Override
	public void deleteUPS(int id) {
		// TODO implement when the dao is ready

	}

	/**
	 * 
	 * @param xmlFiles
	 * @return
	 */

	private void handleParserSucess(UPSInfo file) {
		if (file.getExtraInfo() != null) {
			// TODO calls the class/method responsible to handdle this part of
			// the object
		}
		// TODO generates Entities
		// TODO calls the dao
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
}
