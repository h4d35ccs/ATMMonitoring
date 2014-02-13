/**
 * 
 */
package com.ncr.ATMMonitoring.service;

import java.io.InputStream;
import java.util.List;

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

	private FileInDiskHandler fileHandler = FileInDiskHandler.getInstance();
	
	/**
	 */
	public UPSServiceImp() {
		
	}

	/* (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.util.List)
	 */
	@Override
	public void storeUPSinfo(List<String> xmlFiles) {
		try {
			
			
			for(String file: xmlFiles) {
				
				try {
					
					InputStream xml = this.fileHandler.getFile(file);
					
					UPSInfo xmlInfo = this.getXMLInfo(xml);
					System.out.println("parsed :)"+xmlInfo);
					this.handleSuccess(file);
					
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XMLNotReadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoParserFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		} catch (FileHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.io.InputStream)
	 */
	@Override
	public void storeUPSinfo(InputStream xmlFile) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.service.UPSService#storeUPSinfo(java.lang.String)
	 */
	@Override
	public void storeUPSinfo(String xmlFile) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ncr.ATMMonitoring.service.UPSService#deleteUPS(int)
	 */
	@Override
	public void deleteUPS(int id) {
		// TODO Auto-generated method stub

	}
	/**
	 * Calls the parser
	 * @param xmlfile
	 * @return UPSInfo
	 * @throws ParserException
	 * @throws XMLNotReadableException
	 * @throws NoParserFoundException
	 */
	private UPSInfo getXMLInfo(InputStream xmlfile) throws ParserException, XMLNotReadableException, NoParserFoundException{
		
		ParseUPSChainBuilder chainBuilder = ParseUPSChainBuilder.getInstance();
		return chainBuilder.parse(xmlfile);
	}
	/**
	 * RE
	 * @param xmlFiles
	 * @return
	 */
	private void handleSuccess(String file){
		try {
			
			this.fileHandler.delete(file);
			
		} catch (FileHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
