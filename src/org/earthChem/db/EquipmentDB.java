package org.earthChem.db;

import org.earthChem.db.postgresql.hbm.Equipment;


public class EquipmentDB {
	
	
	
	public static String save(Equipment eq, boolean isnew) {
		String name  = eq.getEquipmentName();
		if(name == null ||"".equals(name)) name = eq.getEquipmentCode();
		String q = "INSERT INTO equipment (equipment_num, equipment_code, equipment_name, equipment_type_num, equipment_description) "+
				" VALUES ("+eq.getEquipmentNum()+
				","+DBUtil.StringValue(eq.getEquipmentCode())+
				","+DBUtil.StringValue(name)+
				","+eq.getEquipmentTypeNum()+
				","+DBUtil.StringValue(eq.getEquipmentDescription())+") ";
		return DBUtil.update(q);
	}

	public static Equipment getEquipment(Integer id) {
		Equipment eq = new Equipment();
		eq.setEquipmentNum(id);
		String q = "select equipment_code, equipment_name, equipment_type_num, equipment_description from equipment where equipment_num= "+id;
		Object[] arr = DBUtil.uniqueResult(q);
		for(int i = 0; i< arr.length;++i) {
		 eq.setEquipmentCode(""+arr[0]);	
		 eq.setEquipmentName(""+arr[1]);	
		 eq.setEquipmentTypeNum((Integer)arr[2]);	
		 eq.setEquipmentDescription(""+arr[3]);	
		}
		return eq;
	}
}
