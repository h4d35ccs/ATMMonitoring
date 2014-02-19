/**
 * 
 */
package com.ncr.ATMMonitoring.serviceFacade.imp;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ncr.ATMMonitoring.pojo.BankCompany;
import com.ncr.ATMMonitoring.pojo.Installation;
import com.ncr.ATMMonitoring.pojo.ScheduledUpdate;
import com.ncr.ATMMonitoring.pojo.Terminal;
import com.ncr.ATMMonitoring.pojo.TerminalConfig;
import com.ncr.ATMMonitoring.pojo.TerminalModel;
import com.ncr.ATMMonitoring.serviceFacade.ATMFacade;

/**
 * @author Otto Abreu
 *
 */
@Service
public class ATMFacadeImp implements ATMFacade{

	/**
	 * 
	 */
	public ATMFacadeImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ScheduledUpdate> listScheduledUpdates(int updateType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addScheduledUpdate(ScheduledUpdate scheduledUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeScheduledUpdate(int updateId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existScheduledUpdate(int updateId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addATMMachine(TerminalConfig terminalConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TerminalConfig> listATMMachines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TerminalConfig getATMMachine(int atmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Terminal> listATM(List<BankCompany> banksCompanies,
			BankCompany bank, List<Integer> atmId, String sort, String order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Terminal getATM(int atmId, String serialNumber, String matricula,
			String ip, String macAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUpdateATM(Terminal terminal, int operType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addATMByFile(InputStream file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TerminalModel> listATMModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<TerminalModel>> listATMModelsByManufacturer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUpdateATMModel(TerminalModel terminalModel, int operType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeATMModel(int atmModelId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUpdateATMInstallation(Installation installation, int operType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Installation> listATMInstallations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Installation getATMInstallation(int installationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeATMInstallation(int installationId) {
		// TODO Auto-generated method stub
		
	}

}
