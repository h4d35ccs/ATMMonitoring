/**
 * 
 */
package com.ncr.ATMMonitoring.parser.imp;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.ncr.ATMMonitoring.parser.ParseUPSChainBuilder;
import com.ncr.ATMMonitoring.parser.ParseUPSXML;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;

/**
 * Class that define a parse based on w3c DOM<br>
 * Holds the common logic to parse a XML based on DOM parser<br>
 * <b><i>Do not call the parser directly, call {@link ParseUPSChainBuilder#parse(InputStream)}</i></b>
 * 
 * @author ottoabreu
 * 
 */
public abstract class ParseUPSDom extends ParseUPSXML {

	/**
	 * Generate a {@link Document} from the {@link InputStream}
	 * 
	 * @param xmlFile
	 *            {@link InputStream} with the XML
	 * @return Document
	 * @throws ParserException
	 *             if can't get the document builder or a general error occurs
	 * @throws XMLNotReadableException
	 *             if can not read the file while executing the
	 *             {@link DocumentBuilder#parse(java.io.File)}
	 */
	protected Document getDocument(InputStream xmlFile) throws ParserException,
			XMLNotReadableException {

		Document upsXml = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			upsXml = dBuilder.parse(xmlFile);
			upsXml.getDocumentElement().normalize();

		} catch (ParserConfigurationException e) {
			throw new ParserException(ParserException.PARSER_CONFIGURATION_ERROR, e);
		} catch (SAXException e) {
			throw new XMLNotReadableException(
					XMLNotReadableException.PARSE_ERROR, e);
		} catch (IOException e) {
			throw new XMLNotReadableException(XMLNotReadableException.IO_ERROR,
					e);
		} catch (Exception e) {
			throw new ParserException(ParserException.GENERAL_ERROR
					+ e.getMessage(), e);
		}

		return upsXml;
	}

	/**
	 * Returns the XML in String format
	 * 
	 * @param doc
	 *            Document
	 * @return String
	 */
	protected String getStringFromDoc(Document doc) {
		DOMImplementationLS domImplementation = (DOMImplementationLS) doc
				.getImplementation();
		LSSerializer lsSerializer = domImplementation.createLSSerializer();
		return lsSerializer.writeToString(doc);
	}

}
