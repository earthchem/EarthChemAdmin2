package org.earthChem.db.postgresql.hbm;

public class Equipment implements java.io.Serializable {

	private Integer equipmentNum;
	private Integer equipmentTypeNum;
	private String equipmentName;
	private String equipmentCode;
	private String equipmentDescription;
	
	public Integer getEquipmentNum() {
		return equipmentNum;
	}
	public void setEquipmentNum(Integer equipmentNum) {
		this.equipmentNum = equipmentNum;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public Integer getEquipmentTypeNum() {
		return equipmentTypeNum;
	}
	public void setEquipmentTypeNum(Integer equipmentTypeNum) {
		this.equipmentTypeNum = equipmentTypeNum;
	}
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getEquipmentDescription() {
		return equipmentDescription;
	}
	public void setEquipmentDescription(String equipmentDescription) {
		this.equipmentDescription = equipmentDescription;
	}
	
	
	

	
	
	
}
