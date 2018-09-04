package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.earthChem.presentation.jsf.theme.Theme;


public class Action implements java.io.Serializable {

	private Integer actionNum;
	private Integer actionTypeNum;
	private String actionTypeName;
	private Integer methodNum;
	private String methodCode;
	private Integer datasetNum;	
	private String datasetCode;	
	private Integer organizationNum;
	private String actionName ="N/A";
	private String description="N/A";	
	private String organizationName;
	private Integer equipNum1;
	private Integer equipNum2;
	private String equipName1;
	private String equipName2;
	private String preperation;
	private String comment;
	private String analyst;
	private Date date;  //analysisDate;
	private Theme orgTheme;
	
	
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getActionTypeName() {
		return actionTypeName;
	}
	public void setActionTypeName(String actionTypeName) {
		this.actionTypeName = actionTypeName;
	}
	public String getMethodCode() {
		return methodCode;
	}
	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}
	public String getDatasetCode() {
		return datasetCode;
	}
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public Theme getOrgTheme() {
		return orgTheme;
	}
	public void setOrgTheme(Theme orgTheme) {
		this.orgTheme = orgTheme;
	}
	public String getAnalyst() {
		return analyst;
	}
	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}
	
	public String getPreperation() {
		return preperation;
	}
	public void setPreperation(String preperation) {
		this.preperation = preperation;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getActionNum() {
		return actionNum;
	}
	public void setActionNum(Integer actionNum) {
		this.actionNum = actionNum;
	}
	public Integer getActionTypeNum() {
		return actionTypeNum;
	}
	public void setActionTypeNum(Integer actionTypeNum) {
		this.actionTypeNum = actionTypeNum;
	}
	public Integer getMethodNum() {
		return methodNum;
	}
	public void setMethodNum(Integer methodNum) {
		this.methodNum = methodNum;
	}
	public Integer getDatasetNum() {
		return datasetNum;
	}
	public void setDatasetNum(Integer datasetNum) {
		this.datasetNum = datasetNum;
	}
	public Integer getOrganizationNum() {
		return organizationNum;
	}
	public void setOrganizationNum(Integer organizationNum) {
		this.organizationNum = organizationNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public Integer getEquipNum1() {
		return equipNum1;
	}
	public void setEquipNum1(Integer equipNum1) {
		this.equipNum1 = equipNum1;
	}
	public Integer getEquipNum2() {
		return equipNum2;
	}
	public void setEquipNum2(Integer equipNum2) {
		this.equipNum2 = equipNum2;
	}
	public String getEquipName1() {
		return equipName1;
	}
	public void setEquipName1(String equipName1) {
		this.equipName1 = equipName1;
	}
	public String getEquipName2() {
		return equipName2;
	}
	public void setEquipName2(String equipName2) {
		this.equipName2 = equipName2;
	}
	
	

	
	
}
