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
    @Column(name = "ip")
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
	@Column(name = "type")
	private String type;
	@Column(name = "model")
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
	 * default constructor
	 */
	public Ups() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Getter method for id
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * Setter method for the id
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * Getter method for ip
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * Setter method for the ip
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * Getter method for firmware
	 * @return the firmware
	 */
	public String getFirmware() {
		return firmware;
	}
	/**
	 * Setter method for the firmware
	 * @param firmware the firmware to set
	 */
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	/**
	 * Getter method for runningStatus
	 * @return the runningStatus
	 */
	public String getRunningStatus() {
		return runningStatus;
	}
	/**
	 * Setter method for the runningStatus
	 * @param runningStatus the runningStatus to set
	 */
	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}
	/**
	 * Getter method for chargePercentage
	 * @return the chargePercentage
	 */
	public Float getChargePercentage() {
		return chargePercentage;
	}
	/**
	 * Setter method for the chargePercentage
	 * @param chargePercentage the chargePercentage to set
	 */
	public void setChargePercentage(Float chargePercentage) {
		this.chargePercentage = chargePercentage;
	}
	/**
	 * Getter method for expensePercentage
	 * @return the expensePercentage
	 */
	public Float getExpensePercentage() {
		return expensePercentage;
	}
	/**
	 * Setter method for the expensePercentage
	 * @param expensePercentage the expensePercentage to set
	 */
	public void setExpensePercentage(Float expensePercentage) {
		this.expensePercentage = expensePercentage;
	}
	/**
	 * Getter method for alarmMsg
	 * @return the alarmMsg
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}
	/**
	 * Setter method for the alarmMsg
	 * @param alarmMsg the alarmMsg to set
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
	/**
	 * Getter method for type
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * Setter method for the type
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Getter method for model
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * Setter method for the model
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * Getter method for seriesNumber
	 * @return the seriesNumber
	 */
	public String getSeriesNumber() {
		return seriesNumber;
	}
	/**
	 * Setter method for the seriesNumber
	 * @param seriesNumber the seriesNumber to set
	 */
	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	/**
	 * Getter method for runningTimeMilisec
	 * @return the runningTimeMilisec
	 */
	public Long getRunningTimeMilisec() {
		return runningTimeMilisec;
	}
	/**
	 * Setter method for the runningTimeMilisec
	 * @param runningTimeMilisec the runningTimeMilisec to set
	 */
	public void setRunningTimeMilisec(Long runningTimeMilisec) {
		this.runningTimeMilisec = runningTimeMilisec;
	}
	/**
	 * Getter method for autonomyMilisec
	 * @return the autonomyMilisec
	 */
	public Long getAutonomyMilisec() {
		return autonomyMilisec;
	}
	/**
	 * Setter method for the autonomyMilisec
	 * @param autonomyMilisec the autonomyMilisec to set
	 */
	public void setAutonomyMilisec(Long autonomyMilisec) {
		this.autonomyMilisec = autonomyMilisec;
	}
	/**
	 * Getter method for numPosition
	 * @return the numPosition
	 */
	public String getNumPosition() {
		return numPosition;
	}
	/**
	 * Setter method for the numPosition
	 * @param numPosition the numPosition to set
	 */
	public void setNumPosition(String numPosition) {
		this.numPosition = numPosition;
	}
	/**
	 * Getter method for audFmo
	 * @return the audFmo
	 */
	public Date getAudFmo() {
		return audFmo;
	}
	/**
	 * Setter method for the audFmo
	 * @param audFmo the audFmo to set
	 */
	public void setAudFmo(Date audFmo) {
		this.audFmo = audFmo;
	}
	/**
	 * Getter method for generalStatusMsg
	 * @return the generalStatusMsg
	 */
	public String getGeneralStatusMsg() {
		return generalStatusMsg;
	}
	/**
	 * Setter method for the generalStatusMsg
	 * @param generalStatusMsg the generalStatusMsg to set
	 */
	public void setGeneralStatusMsg(String generalStatusMsg) {
		this.generalStatusMsg = generalStatusMsg;
	}
	/**
	 * Getter method for lastExecutionDate
	 * @return the lastExecutionDate
	 */
	public Date getLastExecutionDate() {
		return lastExecutionDate;
	}
	/**
	 * Setter method for the lastExecutionDate
	 * @param lastExecutionDate the lastExecutionDate to set
	 */
	public void setLastExecutionDate(Date lastExecutionDate) {
		this.lastExecutionDate = lastExecutionDate;
	}
	/**
	 * Getter method for originalXML
	 * @return the originalXML
	 */
	public String getOriginalXML() {
		return originalXML;
	}
	/**
	 * Setter method for the originalXML
	 * @param originalXML the originalXML to set
	 */
	public void setOriginalXML(String originalXML) {
		this.originalXML = originalXML;
	}

	
	
}
