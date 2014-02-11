package com.ncr.ATMMonitoring.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.ncr.ATMMonitoring.parser.dto.UPSInfo;
import com.ncr.ATMMonitoring.parser.exception.NoParserFoundException;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;

/**
 * Interface that define a XML UPS Parser ( a Link in the  responsibility chain) <br>
 * To start the chain and the parsing process call the {@link ParseUPSChainBuilder#parse(InputStream)} 
 * 
 * @author ottoabreu
 * 
 */
public abstract class ParseUPSXML {

	// next link in the chain
	private ParseUPSXML nextParser;

	// logger
	protected static final Logger logger = Logger.getLogger(ParseUPSXML.class);

	/**
	 * Method that parse an XML from an specific UPS<br>
	 * If the XML can not be parsed by this method, the next parser will be
	 * called ( Chain of responsibility pattern)
	 * 
	 * @param xmlFile
	 *            {@link InputStream}
	 * @return {@link UPSInfo}
	 * @throws ParserException
	 *             if occurs a general error
	 * @throws XMLNotReadableException if
	 *             can not read the XML or the content of a node
	 * 
	 * 
	 * @throws NoParserFoundException
	 *             if the end of the chain is reached an no suitable parser was
	 *             found to the given XML
	 */
	public abstract UPSInfo parseXML(InputStream xmlFile)
			throws ParserException, XMLNotReadableException,
			NoParserFoundException;

	/**
	 * Indicate if the parser is in charge of read the XML<br>
	 * Return true if the parser will process the file, false if will delegate
	 * the process to other parser
	 * 
	 * @return boolean
	 */
	protected abstract boolean canParseXML();

	/**
	 * Sets the next parser in the chain
	 * 
	 * @param nextParser
	 *            {@link ParseUPSXML}
	 */
	public void setNextParser(ParseUPSXML nextParser) {
		this.nextParser = nextParser;

	}

	/**
	 * Transform an InputStream in String<br>
	 * If can not read the InputStream return an empty string
	 * 
	 * @param is
	 *            {@link InputStream}
	 * @return String
	 */
	protected String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder("");

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			// can read the inputStrem will return the empty string
			logger.warn(
					"can not get the XML in string returning empty string, will be not possible to see the XML in the logs",
					e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.warn(
							"can not close the buffer reader while getting the XML in String",
							e);
				}
			}
		}

		return sb.toString();

	}

	/**
	 * Calls the {@link ParseUPSXML#parseXML(InputStream)} method in the next
	 * parser
	 * 
	 * @param xmlFile
	 *            InputStream
	 * @throws XMLNotReadableException
	 *             , NoParserFoundException
	 * @throws ParserException
	 */

	public void callNextParser(InputStream xmlFile) throws ParserException,
			XMLNotReadableException, NoParserFoundException {
		if (this.nextParser != null) {

			this.nextParser.parseXML(xmlFile);

		} else {
			//no parser can read the xml
			throw new NoParserFoundException(
					NoParserFoundException.NO_PARSER_FOUND
							+ this.getStringFromInputStream(xmlFile));
		}
	}

}
