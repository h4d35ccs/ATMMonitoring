package com.ncr.ATMMonitoring.test;

import com.ncr.ATMMonitoring.parser.ParseUPSXML;
import com.ncr.ATMMonitoring.parser.annotation.UPSParser;
import com.ncr.ATMMonitoring.parser.dto.UPSInfo;
import com.ncr.ATMMonitoring.parser.exception.NoParserFoundException;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;
@UPSParser
public class OtherParsersLilnk2 extends ParseUPSXML {

	private String xml;
	@Override
	protected boolean canParseXML() {
		this.xml = this.getOriginalXmlString();
		System.out.println("Link 2 evaluating:"+this.xml);
		boolean parse = this.xml.equals("<link2></link2>");
		System.out.println("link2 will parse?"+parse);
		return parse;
		
	}

	@Override
	protected UPSInfo applyParser() throws ParserException,
			XMLNotReadableException, NoParserFoundException {
		
		return null;
	}

}
