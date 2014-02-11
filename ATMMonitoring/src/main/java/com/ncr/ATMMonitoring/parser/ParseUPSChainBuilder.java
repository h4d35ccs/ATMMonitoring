/**
 * 
 */
package com.ncr.ATMMonitoring.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.ncr.ATMMonitoring.parser.dto.UPSInfo;
import com.ncr.ATMMonitoring.parser.exception.NoParserFoundException;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;
import com.ncr.ATMMonitoring.parser.imp.ParseEmersonUPS;

/**
 * Class that builds and executes the chain of responsibility of UPS Parsers <BR>
 * Is possible to add new parsers just adding classes to the package
 * com.ncr.ATMMonitoring.parse.otherParsers, all the classes that extends from
 * {@link ParseUPSXML} will be added automatically<br>
 * The new classes can be added in jar format, is only required that all the new
 * classes are in the classpath.<br>
 * 
 * To start the chain just call the method
 * {@link ParseUPSChainBuilder#parse(InputStream)},
 * 
 * @author ottoabreu
 * 
 */
public class ParseUPSChainBuilder {

	// logger
	protected static final Logger logger = Logger
			.getLogger(ParseUPSChainBuilder.class);

	// holds the classes
	private static final List<Class<? extends ParseUPSXML>> PARSERS_CLASS_LIST;
	// holds the instances
	private final static List<ParseUPSXML> PARSERS_INSTANCES;
	// package where the new parsers class must be
	private static final String OTHER_PARSERS_PACKAGE = "com.ncr.ATMMonitoring.parser.otherParser";

	static {

		PARSERS_CLASS_LIST = new ArrayList<Class<? extends ParseUPSXML>>();

		// Add classes here until the first version of ATMMonitoring is released
		// After the first release add new parsers using reflexion
		PARSERS_CLASS_LIST.add(ParseEmersonUPS.class);

		PARSERS_INSTANCES = new ArrayList<ParseUPSXML>();
		
		// add the classes present in the otherParser packages
		PARSERS_CLASS_LIST.addAll(ParseUPSChainBuilder.addNewParsers());
		// create an instance of each class
		ParseUPSChainBuilder.createInstances();
		// create the chain adding the next parser to each instance
		ParseUPSChainBuilder.buildChain();

	}

	/**
	 * Extract the information from the given xml in InputStream format
	 * 
	 * @param xmlFile
	 *            {@link InputStream}
	 * @return {@link UPSInfo}
	 * @throws ParserException
	 *             if occurs a general error
	 * @throws XMLNotReadableException
	 *             if can not read the XML or the content of a node
	 * 
	 * 
	 * @throws NoParserFoundException
	 *             if the end of the chain is reached an no suitable parser was
	 *             found to the given XML
	 */
	public UPSInfo parse(InputStream xmlFile) throws ParserException,
			XMLNotReadableException, NoParserFoundException {
		ParseUPSXML firstLink = this.getParser();
		return firstLink.parseXML(xmlFile);
	}

	/**
	 * Returns a new instance of the class
	 * 
	 * @return ParseUPSChainBuilder
	 */
	public static ParseUPSChainBuilder getInstance() {
		return new ParseUPSChainBuilder();
	}

	/**
	 * Reads the package com.ncr.ATMMonitoring.parse.otherParsers to add new
	 * parsers to the chain
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static List<Class<? extends ParseUPSXML>> addNewParsers() {

		Reflections reflections = new Reflections(OTHER_PARSERS_PACKAGE);

		Set<Class<? extends ParseUPSXML>> subTypes = reflections
				.getSubTypesOf(ParseUPSXML.class);

		return new ArrayList<Class<? extends ParseUPSXML>>(subTypes);
	}

	/**
	 * Execute the {@link Class#newInstance()} in each class element to generate
	 * a new instance, then add it into the Parser Instance list<br>
	 * If the object can not be instantiated will do nothing
	 */
	private static void createInstances() {

		for (int i = 0; i < PARSERS_CLASS_LIST.size(); i++) {

			Class<? extends ParseUPSXML> parserClass = (Class<? extends ParseUPSXML>) PARSERS_CLASS_LIST
					.get(i);

			ParseUPSXML instance = null;
			try {

				instance = parserClass.newInstance();
				PARSERS_INSTANCES
						.add((com.ncr.ATMMonitoring.parser.ParseUPSXML) instance);

			} catch (InstantiationException e) {
				logger.warn("Can not instantiate the given parser: "
						+ parserClass + "  and will not be added to the chain",
						e);
				;
			} catch (IllegalAccessException e) {
				logger.warn(
						"Can not instantiate the given parser: "
								+ parserClass
								+ "  because represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; ",
						e);
			} catch (Throwable t) {
				logger.warn("Can not instantiate the given parser: "
						+ parserClass + " due  " + t.getMessage()
						+ "  and will not be added to the chain", t);
			}

		}

	}

	/**
	 * Builds the chain. Iterates over the list of instances and add the next
	 * parser to each parser
	 */
	private static void buildChain() {

		// only iterate to the i-1 element of the list because the last does not
		// need a next parser
		for (int i = 0; i < (PARSERS_INSTANCES.size() - 1); i++) {

			ParseUPSXML instance = PARSERS_INSTANCES.get(i);

			ParseUPSXML nextInstance = PARSERS_INSTANCES.get(i + 1);
			instance.setNextParser(nextInstance);

		}

	}

	/**
	 * Returns the first link in the chain
	 * 
	 * @return ParseUPSXML
	 */
	private ParseUPSXML getParser() {

		return PARSERS_INSTANCES.get(0);
	}

	/**
	 * Private constructor
	 */
	private ParseUPSChainBuilder() {
		super();
	}

}
