package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.MethodInfo;
import org.earthChem.db.postgresql.hbm.Organization;

public class MethodInfoDB {
	
	
	public static List<MethodInfo> getMethodInfoList(Integer citationNum) {
		List<MethodInfo> list = new ArrayList<MethodInfo>();
		 String	q = "select a.additional_method_info_num, m.method_code, a.action_num, variable_code,  a.variable_num, "+
			" content::json->'NORM_STANDARD_NAME',content::json->'NORM_STANDARD_VALUE', "+
			" content::json->'FCORR_RATIO', content::json->'FCORR_STANDARD_VALUE', "+
			" content::json->'METHOD_PRECISION_MIN', content::json->'METHOD_PRECISION_MAX', "+
			" content::json->'METHOD_PRECISION_TYPE', content::json->'STANDARD_DATA', content::json->'FCORR_STANDARD_NAME' "+
			" from additional_method_info2 a, variable v, citation_dataset d, action ac, method m   "+
			" where a.variable_num = v.variable_num and d.dataset_num = ac.dataset_num and a.action_num = ac.action_num "+
			" and m.method_num= ac.method_num and d.citation_num ="+citationNum+" order by method_code, variable_code ";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			MethodInfo e = new MethodInfo();
			e.setMethod_info_num((Integer)arr[0]);
			e.setMethodCode(""+arr[1]);
			e.setAction_num((Integer)arr[2]);
			e.setVariableCode(""+arr[3]);
			e.setVariable_num((Integer)arr[4]);
			if(arr[5] != null)e.setNORM_STANDARD_NAME(""+arr[5]);
			if(arr[6] != null)e.setNORM_STANDARD_VALUE(""+arr[6]);
			if(arr[7] != null)e.setFCORR_RATIO(""+arr[7]);
			if(arr[8] != null)e.setFCORR_STANDARD_VALUE(""+arr[8]);
			if(arr[9] != null)e.setMETHOD_PRECISION_MIN(""+arr[9]);
			if(arr[10] != null)e.setMETHOD_PRECISION_MAX(""+arr[10]);
			if(arr[11] != null)e.setMETHOD_PRECISION_TYPE(""+arr[11]);
			if(arr[12] != null)e.setSTANDARD_DATA(""+arr[12]);
			if(arr[13] != null)e.setFCORR_STANDARD_NAME(""+arr[13]);
			list.add(e);
		}
		return list;
	}
	
	
	
/*	public static List<MethodInfo> getMethodInfoList() {
		
		 String	q = "select m.method_num, m.method_code, m.method_name, t.method_type_name,o.organization_name, o.department, m.method_link, m.method_description "+
				 " from method m "+
				 " join method_type t on  m.method_type_num = t.method_type_num "+
				 " left join organization o on o.organization_num=m.organization_num order by m.method_code";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			MethodInfo e = new MethodInfo();
			e.setMethodInfoNum((Integer)arr[0]);
			e.setMethodInfoCode(""+arr[1]);
			e.setMethodInfoName(""+arr[2]);
			e.setMethodInfoTypeName(""+arr[3]);		
			if(arr[4] == null) e.setOrganizationName("");
			else {
				String orgName =""+arr[4];
				if(arr[5] != null) orgName +=", "+arr[5]; 
				e.setOrganizationName(orgName);
			}
			if(arr[6] !=null) e.setMethodInfoLink(""+arr[6]);
			if(arr[7] !=null) e.setMethodInfoDescription(""+arr[7]);
			list.add(e);
		}
		return list;
	}
	
	public static MethodInfo getMethodInfo(Integer methodNum) {
		MethodInfo e = new MethodInfo();
		String	q = "select m.method_num, m.method_code, m.method_name, t.method_type_name,o.organization_name, o.department, m.method_link,t.method_type_num, o.organization_num, m.method_description "+
				 " from method m "+
				 " join method_type t on  m.method_type_num = t.method_type_num "+
				 " left join organization o on o.organization_num=m.organization_num where m.method_num="+methodNum;
		Object[] arr = DBUtil.uniqueResult(q);
		e.setMethodInfoNum(methodNum);
		e.setMethodInfoCode(""+arr[1]);
		e.setMethodInfoName(""+arr[2]);
		e.setMethodInfoTypeName(""+arr[3]);		
		
		if(arr[4] == null) e.setOrganizationName("");
		else {
			String orgName =""+arr[4];
			if(arr[5] != null) orgName +=", "+arr[5]; 
			e.setOrganizationName(orgName);
		}
		if(arr[6] !=null) e.setMethodInfoLink(""+arr[6]);
		e.setMethodInfoTypeNum((Integer)arr[7]);
		e.setOrganizationNum((Integer)arr[8]);
		if(arr[9] !=null) e.setMethodInfoDescription(""+arr[9]);
		
		return e;
	}
	
	public static String update(MethodInfo e, boolean isNew) {
	
	//	MethodInfo e = new MethodInfo();
		Integer methodNum= e.getMethodInfoNum();
		Integer methdTypeNum = e.getMethodInfoTypeNum();	
		String methodCode = DBUtil.StringValue(e.getMethodInfoCode());
		String methodName = DBUtil.StringValue(e.getMethodInfoName());				
		String link =  DBUtil.StringValue(e.getMethodInfoLink());
		Integer orgNum = e.getOrganizationNum();
		String description = DBUtil.StringValue(e.getMethodInfoDescription());
		String q = null;
		if(isNew) {
			
			 q ="INSERT into method (method_num, method_type_num, method_code, method_name, method_description, method_link, organization_num) VALUES "+
				" ("+methodNum+","+methdTypeNum+","+ methodCode+","+methodName+","+ description+","+link+","+orgNum+")";		
		} else {
			q = "update method set "+
				" method_type_num="+methdTypeNum+","+
				" method_code="+methodCode+","+
				" method_name="+methodName+","+
				" method_description="+description+","+
				" method_link="+link+","+
				" organization_num="+orgNum+" where method_num="+methodNum;
		}
		
		return DBUtil.update(q);
	}
	*/
}
