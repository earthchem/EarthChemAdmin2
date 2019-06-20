package org.earthChem.db;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.earthChem.db.postgresql.hbm.Equipment;
import org.earthChem.db.postgresql.hbm.Expedition;

public class ExpeditionDB {
	
	public static List<Expedition> getExpeditionList(String code) {
		String clause = "";
		if(code != null) clause = " upper(a.action_name) like upper('%"+code+"%') and ";
		List<Expedition> list = new ArrayList<Expedition>();
		String q = "select distinct a.action_num, a.action_name, e.equipment_name, o.organization_name||'-'||o.department, a.action_description, a.begin_date_time, a.end_date_time, at.action_type_name, "+ 
				"anname.annotation_text, anleg.annotation_text, analias.annotation_text  "+
				 "from action a  "+
				 "join action_type at on at.action_type_num = a.action_type_num  "+
				 "join organization o on o.organization_num = a.organization_num  "+
				 "left join equipment_action ea  on ea.action_num = a.action_num  "+
				 "left join equipment e on ea.equipment_num = e.equipment_num   "+
				 "left join action_annotation ename on ename.action_num = a.action_num "+
				"left join (select aname.annotation_text, ename.action_num, aname.annotation_type_num  "+
				"from action_annotation ename left join annotation aname on  "+
				 "aname.annotation_num =ename.annotation_num ) anname on anname.action_num = a.action_num  and anname.annotation_type_num =89 "+
				"left join (select aleg.annotation_text, eleg.action_num, aleg.annotation_type_num  from action_annotation eleg left join annotation aleg on "+
				 "aleg.annotation_num =eleg.annotation_num ) anleg on anleg.action_num = a.action_num and anleg.annotation_type_num =90 "+
				 "left join (select aalias.annotation_text, ealias.action_num, aalias.annotation_type_num  from action_annotation ealias left join annotation aalias on "+
				 "aalias.annotation_num =ealias.annotation_num ) analias on analias.action_num = a.action_num and analias.annotation_type_num =40 "+
				"where "+clause+" a.action_type_num in (3,11,12,25) order by a.action_name "; 
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Expedition e = new Expedition();
			e.setActionNum((Integer)arr[0]);
			e.setActionName(""+arr[1]);
			e.setActionTypeName(""+arr[7]);
			if(arr[2] != null) e.setEquipmentName(""+arr[2]);
			if(arr[3] != null)e.setOrganizationName(""+arr[3]);
			if(arr[4] != null) e.setDescription(""+arr[4]);	
			if(arr[5] != null) e.setBeginDateTime((Timestamp)arr[5]);
			if(arr[6] != null) e.setEndDateTime((Timestamp)arr[6]);
			if(arr[8] != null) e.setExpeditionName(""+arr[8]);
			if(arr[9] != null) e.setCruiseLeg(""+arr[9]);
			if(arr[10] != null) e.setExpeditionAlias(""+arr[10]);
			list.add(e);
		}
		return list;
	}
	
	public static Expedition getExpedition(Integer actionNum) {
		String q = "select a.action_num, a.action_name, e.equipment_code, o.organization_name||'-'||o.department, a.action_description, a.begin_date_time, a.end_date_time, at.action_type_num, "+
				" a.organization_num from action a "+
			" join action_type at on at.action_type_num = a.action_type_num "+
			" join organization o on o.organization_num = a.organization_num "+
			" left join equipment_action ea  on ea.action_num = a.action_num "+
			" left join equipment e on ea.equipment_num = e.equipment_num "+ 
			" where a.action_num ="+actionNum;
		List<Object[]> olist = DBUtil.getECList(q);
		Expedition e = new Expedition();
		for(Object[] arr: olist) {			
			e.setActionNum((Integer)arr[0]);
			e.setActionName(""+arr[1]);
			e.setActionTypeNum((Integer)arr[7]);
			if(arr[2] != null) e.setEquipmentCode(""+arr[2]);
			e.setOrganizationName(""+arr[3]);
			if(arr[4] != null) e.setDescription(""+arr[4]);	
			if(arr[5] != null) e.setBeginDateTime((Timestamp)arr[5]);
			if(arr[6] != null) e.setEndDateTime((Timestamp)arr[6]);
			e.setOrganizationNum((Integer)arr[7]);
		}
		q = "select  t.annotation_type_name, a.annotation_text from annotation a, action_annotation aa, annotation_type t "+
		" where a.annotation_num = aa.annotation_num  and t.annotation_type_num = a.annotation_type_num and aa.action_num = "+actionNum;
		List<Object[]> objs = DBUtil.list(q);
		for(Object[] a: objs) {
			if("ExpeditionName".equals(""+a[0])) e.setExpeditionName(""+a[1]);
			else if("ExpeditionAlias".equals(""+a[0])) e.setExpeditionAlias(""+a[1]);
			else if("CruiseLeg".equals(""+a[0])) e.setCruiseLeg(""+a[1]);					
		}
		return e;
	}

	public static String saveExpedition(Expedition ex, boolean isNew) {
		String error = null;
		if(isNew) ex.setActionNum(((Long) DBUtil.uniqueObject("SELECT nextval('action_action_num_seq')")).intValue());
		Integer actionNum = ex.getActionNum();
		Integer actionTypeNum = ex.getActionTypeNum();
		Integer orgNum = ex.getOrganizationNum();
		if(orgNum == null) orgNum = 0;
		String actionName = DBUtil.StringValue(ex.getActionName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String beginDate = null;
		String endDate = null;
		if(ex.getBeginDateTime() != null) beginDate = "'"+format.format(ex.getBeginDateTime())+"'";
		if(ex.getEndDateTime() != null) endDate = "'"+format.format(ex.getEndDateTime())+"'";
		String desc =  ex.getDescription();
        if(!"".equals(desc)) {
        	desc =  DBUtil.StringValue(desc);
        } else {
        	desc = "'N/A'";
        }
		String q = null;
		if(isNew) {
			
			q = "INSERT INTO action (action_num, action_type_num, method_num, begin_date_time,"+
					" end_date_time, action_description, organization_num, action_name, dataset_num) "+
			" VALUES ("+actionNum+","+actionTypeNum+",1,"+ beginDate+","+endDate +","+desc+","+
			  orgNum+","+actionName+",0)";
			
		} else {
			q = " update action set action_type_num= "+actionTypeNum +","+
				" begin_date_time="+beginDate+","+
				" end_date_time="+endDate+","+
				" action_description="+	desc+","+
				" organization_num="+orgNum+","+
				" action_name="+actionName+" where action_num="+actionNum;
	
		}
		error = DBUtil.update(q);
	    if(error != null) return error;
		error = updateActionAnnotation(ex, actionNum);

		if(error == null && ex.getEquipmentNum()!=null) updateEquipmentAction(ex.getEquipmentNum(), actionNum);
		return error;
	}
	
	public static String updateActionAnnotation(Expedition ex, Integer actionNum) {
		String name = ex.getExpeditionName();
		String alias = ex.getExpeditionAlias();
		String leg =  ex.getCruiseLeg();
		String error = null;
		if(!"".equals(name)) {
			error = ActionAnnotationDB.save(actionNum, 89, name);
		}
		if(error == null && !"".equals(alias)) {
			error = ActionAnnotationDB.save(actionNum, 40, alias);
		}
		
		if(error == null && !"".equals(leg)) {
			error = ActionAnnotationDB.save(actionNum, 90, leg);
		}
	
		return error;
		
	}
	
	public static String updateEquipmentAction(Integer eqNum, Integer actionNum) {
		String q = "select bridge_num from equipment_action where action_num ="+actionNum;
		Integer bridgeNum = DBUtil.getNumber(q);
		if(bridgeNum == null) {
			q = "select nextval('equipment_action_bridge_num_seq')";
			bridgeNum = DBUtil.getNumber(q);
			q = "insert into equipment_action values ("+bridgeNum+","+eqNum+","+actionNum+")";			
		} else {
			q = "update equipment_action set equipment_num = from equipment_action where action_num = "+actionNum;			
		}
		return DBUtil.update(q);
	}
}
