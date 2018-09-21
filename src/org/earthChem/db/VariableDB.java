package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.Variable;
import org.earthChem.db.postgresql.hbm.Organization;

public class VariableDB {
	
	public static List<Variable> getVariableList(Integer typeNum) {
		String clause = "";
		if(typeNum != 0) clause = " and t.variable_type_num="+typeNum;
		List<Variable> list = new ArrayList<Variable>();
		 String	q = "select v.variable_num, v.variable_type_num,  v.variable_code, t.variable_type_code, v.variable_definition, v.display_order,  v.variable_name  "+
				 " from variable v, variable_type t "+
				 " where v.variable_type_num = t.variable_type_num "+clause+ " order by v.display_order";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Variable e = new Variable();
			e.setVariableNum((Integer)arr[0]);
			e.setVariableTypeNum((Integer)arr[1]);
			e.setVariableCode(""+arr[2]);
			e.setVariableTypeCode(""+arr[3]);		
			e.setVariableDefinition(""+arr[4]);
			e.setDisplayOrder((Integer)arr[5]);
			e.setVariableName(""+arr[6]);
			list.add(e);
		}
		return list;
	}
	
	public static Variable getVariable(Integer variableNum) {
		String	q = "select v.variable_num, v.variable_type_num,  v.variable_name, t.variable_type_code, v.variable_definition, v.display_order, v.variable_code  "+
				" from variable v, variable_type t "+
				" where v.variable_type_num = t.variable_type_num and variable_num ="+variableNum;

		Object[] arr = DBUtil.uniqueResult(q);
		Variable e = new Variable();
		e.setVariableNum((Integer)arr[0]);
		e.setVariableTypeNum((Integer)arr[1]);
		e.setVariableName(""+arr[2]);
		e.setVariableTypeCode(""+arr[3]);		
		e.setVariableDefinition(""+arr[4]);
		e.setDisplayOrder((Integer)arr[5]);
		e.setVariableCode(""+arr[6]);
		return e;
	}
	
	public static String update(Variable e, boolean isNew) {	
		String q = null;
		List<String> list = new ArrayList<String>();
		Integer variableNum= e.getVariableNum();
		Integer variableTypeNum = e.getVariableTypeNum();	
		Integer order =  e.getDisplayOrder();
		String variableCode = DBUtil.StringValue(e.getVariableCode());
		String variableName = DBUtil.StringValue(e.getVariableName());				
		String variableDefinition = DBUtil.StringValue(e.getVariableDefinition());	
		q = "update variable set display_order = (display_order+1) where display_order >= "+order;
	    list.add(q);
		if(isNew) {
			
			 q ="INSERT INTO variable (variable_num, variable_type_num, variable_code, variable_name,"+
			 " variable_definition, display_order) VALUES ("+variableNum+","+variableTypeNum+","+variableCode+","+variableName+","+variableDefinition+","+order+")";		
		} else {
			q = "update variable set "+
				" variable_type_num="+variableTypeNum+","+
				" variable_code="+variableCode+","+
				" variable_name="+variableName+","+
				" variable_definition="+variableDefinition+","+
				" display_order="+order+" where variable_num="+variableNum;
		}
		list.add(q);
		return DBUtil.updateList(list);
	}
}
