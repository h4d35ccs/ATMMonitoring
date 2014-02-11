/**
 * 
 */
package com.ncr.ATMMonitoring.parser.dto;

import java.util.Date;

/**
 * DTO that holds the basic information retrieved from an UPS XML
 * @author ottoabreu
 *
 */
public class UPSInfo {
	
	private String ip;
	private String firmware;
	private String runningStatus;
	private float chargePercentage;
	private float expensePercentage;
	private String alarmMsg;
	private String upsType;
	private String upsModel;
	private String seriesNumber;
	private long runningTimeMilisec;
	private long autonomyMilisec;
	private String numPosition;
	private Date audFmo;
	private String generalStatusMsg;
	private Date lastExecutionDate;
	private String originalXML;
	private UPSExtraInfo extraInfo;
	
	
	
	
	/**
	 * Constructor with all the parameters
	 * @param ip
	 * @param firmware
	 * @param runningStatus
	 * @param chargePercentage
	 * @param expensePercentage
	 * @param alarmMsg
	 * @param upsType
	 * @param upsModel
	 * @param seriesNumber
	 * @param runningTimeMilisec
	 * @param autonomyMilisec
	 * @param numPosition
	 * @param audFmo
	 * @param generalStatusMsg
	 * @param lastExecutionDate
	 * @param originalXML
	 * @param extraInfo
	 */
	public UPSInfo(String ip, String firmware, String runningStatus,
			float chargePercentage, float expensePercentage, String alarmMsg,
			String upsType, String upsModel, String seriesNumber,
			long runningTimeMilisec, long autonomyMilisec, String numPosition,
			Date audFmo, String generalStatusMsg, Date lastExecutionDate,
			String originalXML, UPSExtraInfo extraInfo) {
		super();
		this.ip = ip;
		this.firmware = firmware;
		this.runningStatus = runningStatus;
		this.chargePercentage = chargePercentage;
		this.expensePercentage = expensePercentage;
		this.alarmMsg = alarmMsg;
		this.upsType = upsType;
		this.upsModel = upsModel;
		this.seriesNumber = seriesNumber;
		this.runningTimeMilisec = runningTimeMilisec;
		this.autonomyMilisec = autonomyMilisec;
		this.numPosition = numPosition;
		this.audFmo = audFmo;
		this.generalStatusMsg = generalStatusMsg;
		this.lastExecutionDate = lastExecutionDate;
		this.originalXML = originalXML;
		this.extraInfo = extraInfo;
	}
	
	/**
	 * COnstructor no param
	 */
	public UPSInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getFirmware() {
		return firmware;
	}
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	public String getRunningStatus() {
		return runningStatus;
	}
	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}
	public float getChargePercentage() {
		return chargePercentage;
	}
	public void setChargePercentage(float chargePercentage) {
		this.chargePercentage = chargePercentage;
	}
	public float getExpensePercentage() {
		return expensePercentage;
	}
	public void setExpensePercentage(float expensePercentage) {
		this.expensePercentage = expensePercentage;
	}
	public String getAlarmMsg() {
		return alarmMsg;
	}
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
	public String getUpsType() {
		return upsType;
	}
	public void setUpsType(String upsType) {
		this.upsType = upsType;
	}
	public String getUpsModel() {
		return upsModel;
	}
	public void setUpsModel(String upsModel) {
		this.upsModel = upsModel;
	}
	public String getSeriesNumber() {
		return seriesNumber;
	}
	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	public long getRunningTimeMilisec() {
		return runningTimeMilisec;
	}
	public void setRunningTimeMilisec(long runningTimeMilisec) {
		this.runningTimeMilisec = runningTimeMilisec;
	}
	public long getAutonomyMilisec() {
		return autonomyMilisec;
	}
	public void setAutonomyMilisec(long autonomyMilisec) {
		this.autonomyMilisec = autonomyMilisec;
	}
	public String getNumPosition() {
		return numPosition;
	}
	public void setNumPosition(String numPosition) {
		this.numPosition = numPosition;
	}
	public Date getAudFmo() {
		return audFmo;
	}
	public void setAudFmo(Date audFmo) {
		this.audFmo = audFmo;
	}
	public String getGeneralStatusMsg() {
		return generalStatusMsg;
	}
	public void setGeneralStatusMsg(String generalStatusMsg) {
		this.generalStatusMsg = generalStatusMsg;
	}
	public Date getLastExecutionDate() {
		return lastExecutionDate;
	}
	public void setLastExecutionDate(Date lastExecutionDate) {
		this.lastExecutionDate = lastExecutionDate;
	}
	public String getOriginalXML() {
		return originalXML;
	}
	public void setOriginalXML(String originalXML) {
		this.originalXML = originalXML;
	}
	public UPSExtraInfo getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(UPSExtraInfo extraInfo) {
		this.extraInfo = extraInfo;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer("[");
		sb.append("| ip= "+ this.ip);
		sb.append("| firmware= "+ this.firmware);
		sb.append("| runningStatus= "+ this.runningStatus);
		sb.append("| chargePercentage= "+ this.chargePercentage);
		sb.append("| expensePercentage= "+ this.expensePercentage);
		sb.append("| alarmMsg= "+ this.alarmMsg);
		sb.append("| upsType= "+ this.upsType);
		sb.append("| upsModel= "+ this.upsModel);
		sb.append("| seriesNumber= "+ this.seriesNumber);
		sb.append("| runningTimeMilisec= "+ this.runningTimeMilisec);
		sb.append("| autonomyMilisec= "+ this.autonomyMilisec);
		sb.append("| numPosition= "+ this.numPosition);
		sb.append("| audFmo= "+ this.audFmo);
		sb.append("| generalStatusMsg= "+ this.generalStatusMsg);
		sb.append("| lastExecutionDate= "+ this.lastExecutionDate);
		sb.append("| originalXML= "+ this.originalXML);
		sb.append("| extraInfo= "+ this.extraInfo);
		sb.append(" ]");
		return sb.toString();
	}
	
	
	
}
