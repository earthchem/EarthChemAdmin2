package org.earthChem.db.postgresql.hbm;

import java.util.Date;

public class Expedition implements java.io.Serializable {

	private Integer actionNum;	
	private Integer organizationNum;
	private Integer actionTypeNum;
	private Integer equipmentNum;
	private Integer brige;    //equipmentActionNum;
	private String equipmentName;
	private String equipmentCode;
	private String actionName;
	private String actionTypeName;
	private String organizationName;
	private String description;
	private Date beginDateTime;
	private Date endDateTime;
	private String expeditionName;
	private String cruiseLeg;
	private String expeditionAlias;
	private Equipment equipment;
	private Organization organization;
	private String errorMsg;
	
	
	
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Integer getActionTypeNum() {
		return actionTypeNum;
	}
	public void setActionTypeNum(Integer actionTypeNum) {
		this.actionTypeNum = actionTypeNum;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public Equipment getEquipment() {
		return equipment;
	}
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	public Integer getEquipmentNum() {
		return equipmentNum;
	}
	public void setEquipmentNum(Integer equipmentNum) {
		this.equipmentNum = equipmentNum;
	}
	public String getExpeditionName() {
		return expeditionName;
	}
	public void setExpeditionName(String expeditionName) {
		this.expeditionName = expeditionName;
	}
	public String getCruiseLeg() {
		return cruiseLeg;
	}
	public void setCruiseLeg(String cruiseLeg) {
		this.cruiseLeg = cruiseLeg;
	}
	public String getExpeditionAlias() {
		return expeditionAlias;
	}
	public void setExpeditionAlias(String expeditionAlias) {
		this.expeditionAlias = expeditionAlias;
	}
	
	public String getActionTypeName() {
		return actionTypeName;
	}
	public void setActionTypeName(String actionTypeName) {
		this.actionTypeName = actionTypeName;
	}
	public Integer getActionNum() {
		return actionNum;
	}
	public void setActionNum(Integer actionNum) {
		this.actionNum = actionNum;
	}
	public Integer getOrganizationNum() {
		return organizationNum;
	}
	public void setOrganizationNum(Integer organizationNum) {
		this.organizationNum = organizationNum;
	}
	public Integer getBrige() {
		return brige;
	}
	public void setBrige(Integer brige) {
		this.brige = brige;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getBeginDateTime() {
		return beginDateTime;
	}
	public void setBeginDateTime(Date beginDateTime) {
		this.beginDateTime = beginDateTime;
	}
	public Date getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	

	
	
}
