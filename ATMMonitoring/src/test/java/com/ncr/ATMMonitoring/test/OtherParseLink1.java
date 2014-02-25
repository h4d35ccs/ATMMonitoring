package com.ncr.ATMMonitoring.test;

import com.ncr.ATMMonitoring.parser.annotation.UPSParser;
import com.ncr.ATMMonitoring.parser.dto.UPSInfo;
import com.ncr.ATMMonitoring.parser.exception.NoParserFoundException;
import com.ncr.ATMMonitoring.parser.exception.ParserException;
import com.ncr.ATMMonitoring.parser.exception.XMLNotReadableException;
import com.ncr.ATMMonitoring.parser.imp.ParseUPSDom;
@UPSParser
public class OtherParseLink1  extends ParseUPSDom {

	
	private String xml;
	

	@Override
	protected UPSInfo applyParser() throws ParserException,
			XMLNotReadableException, NoParserFoundException {
		System.out.println("Link1 parsing: "+this.xml);
		return new UPSInfo() ;
	}

	
	@Override
	protected boolean canParseXML() throws ParserException{
		this.xml = this.getOriginalXmlString();
		System.out.println("Link 1 evaluating:"+this.xml);
		boolean parse = xml.equals("<link1></link1>");
		System.out.println("link1 will parse?"+parse);
		return parse;
	
	}

}
