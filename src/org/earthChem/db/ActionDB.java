package org.earthChem.db;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.earthChem.db.postgresql.hbm.Action;


public class ActionDB {
	
	public static List<Action> getActionList(Integer citationNum) {
		List<Action> list = new ArrayList<Action>();
		 String	q = "select a.action_num, a.action_name, t.action_type_num, t.action_type_name, m.method_num, m.method_code, d.dataset_num, d.dataset_code, "+
				 " a.begin_date_time, o.organization_num,  concat(o.organization_name,',',o.department), a.action_description "+
				 " from action a, action_type t, citation_dataset cd, organization o, method m, dataset d "+
				 " where a.action_type_num = t.action_type_num and a.dataset_num = d.dataset_num and cd.dataset_num = d.dataset_num "+
				 " and a.action_type_num = 20 and m.method_num = a.method_num and o.organization_num =  a.organization_num and cd.citation_num ="+citationNum+" order by dataset_code";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Action a = new Action();
			a.setActionNum((Integer)arr[0]);
			a.setActionName(""+arr[1]);
			a.setActionTypeNum((Integer)arr[2]);
			a.setActionTypeName(""+arr[3]);
			a.setMethodNum((Integer)arr[4]);
			a.setMethodCode(""+arr[5]);
			a.setDatasetNum((Integer)arr[6]);
			a.setDatasetCode(""+arr[7]);
			a.setDate((Timestamp)arr[8]);
			a.setOrganizationNum((Integer)arr[9]);
			a.setOrganizationName(""+arr[10]);
			a.setDescription(""+arr[11]);
			list.add(a);
		}
		return list;
	}
	
	public static String delete(Integer actionNum) {
		String error = null;
		String q = null;
		Integer prepNum = DBUtil.getNumber("select action_num from related_action where related_action_num = "+actionNum);
		if(prepNum != null) {
			error = DBUtil.update("delete from related_action where related_action_num = "+actionNum);
			if(error != null) return error;
			error = DBUtil.update("delete from action where action_num = "+prepNum);
		}
		error = DBUtil.update("delete from equipment_action where action_num = "+actionNum);
		if(error != null) return error;
		error = DBUtil.update("delete from action where action_num = "+actionNum);
		return error;
	}
	
	
	public static String Add(Action a) {
		String error = null;
		String date = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(a.getDate() != null) date = "'"+format.format(a.getDate())+"'";
		Integer eq1 = a.getEquipNum1();
		Integer eq2 = a.getEquipNum2();
		String preparation = a.getPreperation();
		String comment = a.getComment();
		String desc = getDescription(eq1, eq2, preparation, comment);	

		Integer actionNum=	DBUtil.getNumber("select nextVal('action_action_num_seq')");
		String q="INSERT INTO action (action_num, action_type_num, method_num, begin_date_time, action_description, organization_num, dataset_num) "+
		" VALUES("+actionNum+",20,"+a.getMethodNum()+","+date+","+desc+","+a.getOrgTheme().getId() +","+a.getDatasetNum()+")";
		error=DBUtil.update(q);
		if(error != null) return error;
		//insert preparation
		if(!preparation.trim().equals("")) {
			Integer prepNum=DBUtil.getNumber("select nextVal('action_action_num_seq')");
			q="INSERT INTO action (action_num, action_type_num, method_num, begin_date_time, action_description, organization_num, dataset_num) "+
			" VALUES("+prepNum+",23,"+a.getMethodNum()+","+date+","+DBUtil.StringValue(a.getPreperation())+","+a.getOrgTheme().getId()+","+a.getDatasetNum()+")";
			error=DBUtil.update(q);
			if(error != null) return error;
				q="INSERT INTO related_action (action_num, relationship_type_num, related_action_num) VALUES "+
				  "("+prepNum+",14,"+actionNum+")";
				error=DBUtil.update(q);
		}
		if(error != null) return error;
		//insert eq
		if(eq1 != null) {
			q = "INSERT INTO equipment_action (equipment_num, action_num) VALUES ("+eq1+","+actionNum+")";
			error=DBUtil.update(q);
		} 
		if(error != null) return error;
		if(eq2 != null) {
			q = "INSERT INTO equipment_action (equipment_num, action_num) VALUES ("+eq2+","+actionNum+")";
			error=DBUtil.update(q);
		}

		return error;
	}
	
	private static String getDescription(Integer e1,  Integer e2, String prep, String comment) {
		String desc = "";
		if(e1 != null) desc += DBUtil.uniqueObject("select equipment_code from equipment where equipment_num ="+e1);
		if(e2 != null) desc +=", "+DBUtil.uniqueObject("select equipment_code from equipment where equipment_num ="+e2);
		if(!"".equals(prep.trim())) {
			if(!desc.equals("")) desc += ", "+prep;
			else desc = prep;
		}
		if(!"".equals(comment.trim())) {
			if(!desc.equals("")) desc += ", "+comment;
			else desc = prep;
		}
		if(!desc.equals("")) desc = DBUtil.StringValue(desc);
		else desc = "'N/A'";
		
		return desc; 
	}
}
