package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.Method;
import org.earthChem.db.postgresql.hbm.Organization;

public class MethodDB {
	
	public static List<Method> getMethodList() {
		List<Method> list = new ArrayList<Method>();
		 String	q = "select m.method_num, m.method_code, m.method_name, t.method_type_name,o.organization_name, o.department, m.method_link, m.method_description "+
				 " from method m "+
				 " join method_type t on  m.method_type_num = t.method_type_num "+
				 " left join organization o on o.organization_num=m.organization_num order by m.method_code";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Method e = new Method();
			e.setMethodNum((Integer)arr[0]);
			e.setMethodCode(""+arr[1]);
			e.setMethodName(""+arr[2]);
			e.setMethodTypeName(""+arr[3]);		
			if(arr[4] == null) e.setOrganizationName("");
			else {
				String orgName =""+arr[4];
				if(arr[5] != null) orgName +=", "+arr[5]; 
				e.setOrganizationName(orgName);
			}
			if(arr[6] !=null) e.setMethodLink(""+arr[6]);
			if(arr[7] !=null) e.setMethodDescription(""+arr[7]);
			list.add(e);
		}
		return list;
	}
	
	public static Method getMethod(Integer methodNum) {
		Method e = new Method();
		String	q = "select m.method_num, m.method_code, m.method_name, t.method_type_name,o.organization_name, o.department, m.method_link,t.method_type_num, o.organization_num, m.method_description "+
				 " from method m "+
				 " join method_type t on  m.method_type_num = t.method_type_num "+
				 " left join organization o on o.organization_num=m.organization_num where m.method_num="+methodNum;
		Object[] arr = DBUtil.uniqueResult(q);
		e.setMethodNum(methodNum);
		e.setMethodCode(""+arr[1]);
		e.setMethodName(""+arr[2]);
		e.setMethodTypeName(""+arr[3]);		
		
		if(arr[4] == null) e.setOrganizationName("");
		else {
			String orgName =""+arr[4];
			if(arr[5] != null) orgName +=", "+arr[5]; 
			e.setOrganizationName(orgName);
		}
		if(arr[6] !=null) e.setMethodLink(""+arr[6]);
		e.setMethodTypeNum((Integer)arr[7]);
		e.setOrganizationNum((Integer)arr[8]);
		if(arr[9] !=null) e.setMethodDescription(""+arr[9]);
		
		return e;
	}
	
	public static String update(Method e, boolean isNew) {
	
	//	Method e = new Method();
		Integer methodNum= e.getMethodNum();
		Integer methdTypeNum = e.getMethodTypeNum();	
		String methodCode = DBUtil.StringValue(e.getMethodCode());
		String methodName = DBUtil.StringValue(e.getMethodName());				
		String link =  DBUtil.StringValue(e.getMethodLink());
		Integer orgNum = e.getOrganizationNum();
		String description = DBUtil.StringValue(e.getMethodDescription());
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
}
