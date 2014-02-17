/**
 * 
 */
package com.ncr.ATMMonitoring.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity that holds the info of an UPS
 * 
 * @author Otto Abreu
 *
 */
@Entity
@Table(name = "ups")
public class Ups implements Serializable{

	
	 /*The Constant serialVersionUID. */
	private static final long serialVersionUID = 3914511485325636325L;
	
	
	/* The id. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ups_id_seq")
    @SequenceGenerator(name = "ups_id_seq", sequenceName = "ups_id_seq", allocationSize = 1)
    private Integer id;
    private String ip;
    @Column(name = "firmware_revision")
	private String firmware;
	@Column(name = "running_status")
	private String runningStatus;
	@Column(name = "charge_percentage")
	private Float chargePercentage;
	@Column(name = "expense_percentage")
	private Float expensePercentage;
	@Column(name = "alarm_msg")
	private String alarmMsg;
	private String type;
	private String model;
	@Column(name = "series_number")
	private String seriesNumber;
	@Column(name = "running_time_milisec")
	private Long runningTimeMilisec;
	@Column(name = "autonomy_milisec")
	private Long autonomyMilisec;
	@Column(name = "num_position")
	private String numPosition;
	@Column(name = "aud_fmo")
	private Date audFmo;
	@Column(name = "general_status_msg")
	private String generalStatusMsg;
	@Column(name = "last_execution")
	private Date lastExecutionDate;
	@Column(name = "xml")
	private String originalXML;
	/**
	 * Constructor with all params
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
	public Ups(String ip, String firmware, String runningStatus,
			float chargePercentage, float expensePercentage, String alarmMsg,
			String upsType, String upsModel, String seriesNumber,
			long runningTimeMilisec, long autonomyMilisec, String numPosition,
			Date audFmo, String generalStatusMsg, Date lastExecutionDate,
			String originalXML){
		
		this.ip = ip;
		this.firmware = firmware;
		this.runningStatus = runningStatus;
		this.chargePercentage = chargePercentage;
		this.expensePercentage = expensePercentage;
		this.alarmMsg = alarmMsg;
		this.type = upsType;
		this.model = upsModel;
		this.seriesNumber = seriesNumber;
		this.runningTimeMilisec = runningTimeMilisec;
		this.autonomyMilisec = autonomyMilisec;
		this.numPosition = numPosition;
		this.audFmo = audFmo;
		this.generalStatusMsg = generalStatusMsg;
		this.lastExecutionDate = lastExecutionDate;
		this.originalXML = originalXML;
	}
	/**
	 * default constructor
	 */
	public Ups() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public void setChargePercentage(Float chargePercentage) {
		this.chargePercentage = chargePercentage;
	}
	
	public float getExpensePercentage() {
		return expensePercentage;
	}
	public void setExpensePercentage(Float expensePercentage) {
		this.expensePercentage = expensePercentage;
	}
	public String getAlarmMsg() {
		return alarmMsg;
	}
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
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
	public void setRunningTimeMilisec(Long runningTimeMilisec) {
		this.runningTimeMilisec = runningTimeMilisec;
	}
	public long getAutonomyMilisec() {
		return autonomyMilisec;
	}
	public void setAutonomyMilisec(Long autonomyMilisec) {
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
}
