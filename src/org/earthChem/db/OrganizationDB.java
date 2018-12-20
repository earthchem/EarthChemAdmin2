package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.model.Organization;

public class OrganizationDB {
	
	
	public static List<Organization> getOrganizationList() {
		String q = "SELECT organization_num, o.organization_type_num, organization_name, department, organization_link, "+
			 " organization_description, city, o.state_num, o.country_num, address_part1, organization_type_name, country_name, state_name "+
			 " FROM organization o "+
			 " join  country c on c.country_num = o.country_num "+
			 " join organization_type t on o.organization_type_num = t.organization_type_num "+
			 " left join state s on s.state_num = o.state_num order by organization_name";
		List<Object[]> list = DBUtil.getECList(q);
		List<Organization> orgList = new ArrayList<Organization>();
		for(Object[] o: list) {
			Organization org = new Organization();
			org.setOrganizationNum((Integer)o[0]);
			org.setOrganizationTypeNum((Integer)o[1]);
			org.setOrganizationName((String)o[2]);
			org.setDepartment((String)o[3]);
			org.setOrganizationLink((String)o[4]);
			org.setOrganizationDescription((String)o[5]);
			org.setCity((String)o[6]);	
			org.setStateNum((Integer)o[7]);
			org.setCountryNum((Integer)o[8]);
			org.setAddressPart1((String)o[9]);
			org.setOrgTypeName((String)o[10]);
			org.setCountryName((String)o[11]);
			org.setStateName((String)o[12]);
			orgList.add(org);
		}
		return orgList;
	}
	
	
	public static List<Organization> getOrganizations(String orgName) {
		String where = "";
		if(orgName != null) where = " where upper(organization_name) like upper('%"+orgName+"%') ";
		String q = "SELECT organization_num, o.organization_type_num, organization_name, department, organization_link, "+
			 " organization_description, city, o.state_num, o.country_num, address_part1, organization_type_name, country_name, state_name "+
			 " FROM organization o "+
			 " join  country c on c.country_num = o.country_num "+
			 " join organization_type t on o.organization_type_num = t.organization_type_num "+
			 " left join state s on s.state_num = o.state_num "+where+" order by organization_name";
		List<Object[]> list = DBUtil.getECList(q);
		List<Organization> orgList = new ArrayList<Organization>();
		for(Object[] o: list) {
			Organization org = new Organization();
			org.setOrganizationNum((Integer)o[0]);
			org.setOrganizationTypeNum((Integer)o[1]);
			org.setOrganizationName((String)o[2]);
			org.setDepartment((String)o[3]);
			org.setOrganizationLink((String)o[4]);
			org.setOrganizationDescription((String)o[5]);
			org.setCity((String)o[6]);	
			org.setStateNum((Integer)o[7]);
			org.setCountryNum((Integer)o[8]);
			org.setAddressPart1((String)o[9]);
			org.setOrgTypeName((String)o[10]);
			org.setCountryName((String)o[11]);
			org.setStateName((String)o[12]);
			orgList.add(org);
		}
		return orgList;
	}
	
	@SuppressWarnings("unchecked")
	public static Organization getOrganization(int orgNum) {
		Organization org = new Organization();
		String q = "SELECT organization_num, o.organization_type_num, organization_name, department, organization_link, "+
			 " organization_description, city, o.state_num, o.country_num, address_part1, organization_type_name, country_name, state_name "+
			 " FROM organization o "+
			 " join  country c on c.country_num = o.country_num "+
			 " join organization_type t on o.organization_type_num = t.organization_type_num "+
			 " left join state s on s.state_num = o.state_num where organization_Num="+orgNum;
		List<Object[]> list = DBUtil.getECList(q);
		Object[] o = list.get(0);
		org.setOrganizationNum((Integer)o[0]);
		org.setOrganizationTypeNum((Integer)o[1]);
		org.setOrganizationName((String)o[2]);
		org.setDepartment((String)o[3]);
		org.setOrganizationLink((String)o[4]);
		org.setOrganizationDescription((String)o[5]);
		org.setCity((String)o[6]);	
		org.setStateNum((Integer)o[7]);
		org.setCountryNum((Integer)o[8]);
		org.setAddressPart1((String)o[9]);
		org.setOrgTypeName((String)o[10]);
		org.setCountryName((String)o[11]);
		org.setStateName((String)o[12]);
		return org;
	}
	
	
	public static String save(Organization  org, boolean isnew) {	
		Integer orgNum = org.getOrganizationNum();
		String orgName = DBUtil.StringValue(org.getOrganizationName());
		Integer orgType = org.getOrganizationTypeNum();
		Integer stateNum = null;
		if(org.getStateNum() != null && org.getStateNum() != 0) stateNum=org.getStateNum();
		int country = org.getCountryNum();
		String dept = DBUtil.StringValue(org.getDepartment());
		String desc = DBUtil.StringValue(org.getOrganizationDescription());
		String address1 = DBUtil.StringValue(org.getAddressPart1());
		String city = DBUtil.StringValue(org.getCity());
		String link = DBUtil.StringValue(org.getOrganizationLink());
		String q =null;
		if(isnew) {
			q = "select nextval('organization_organization_num_seq')";
			Integer newNum = DBUtil.getNumber(q);			
			q = "INSERT INTO organization (organization_num, organization_type_num, organization_code, organization_name,organization_description,"+
				"organization_link,country_num, department,address_part1, city, state_num) "+
			" VALUES ("+newNum+","+orgType+",'NA',"+orgName+","+desc+
			","+link+","+country+","+dept+","+address1+","+city+","+stateNum+")"; 
			org.setOrganizationNum(newNum);
		} else {
			q ="UPDATE organization SET "+
					"organization_type_num = "+orgType+
					",organization_code = 'NA'"+
					",organization_name =" +orgName+
					",organization_description ="+desc+
					",organization_link ="+link+
					",parent_organization_num = null"+
					", country_num ="+country+ 
					",department = "+dept+
					",organization_unique_id =null"+
					",organization_unique_id_type = null"+
					",address_part1 = "+address1+
					",address_part2 = null"+
					",city = "+city+
					",state_num = "+stateNum+
					",zip = null where organization_num = "+orgNum;
		}
		return DBUtil.update(q);
	}

	public static String inactiveOrganization(Integer orgNum) {
		String q = "update organization set status = 0 where organization_num = "+orgNum;
		return DBUtil.update(q);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Organization> getSavedOrganizations(int citationNum) {
		List<Organization> orgs = new ArrayList<Organization>();
		org.earthChem.model.Organization org2 = null;

			String q = "select o.organization_id, o.organization_num, t.organization_type_name,"+
				" o.organization_name, o.department,o.address_part1, o.address_part2, o.city, s.state_abbrv, o.zip, c.country_name "+
				" FROM t_organization o "+
				" inner join country c on c.country_num =  o.country_num  "+
				" left join organization_type t on t.organization_type_num = o.organization_type_num "+
				" left join state s on s.state_num = o.state_num "+ 
				" where citation_Num="+citationNum+" order by organization_Id";
				List<Object[]> list = DBUtil.list(q);
			for(Object[] o : list) {
				Organization org = new Organization();
				org.setOrgId((Integer)o[0]);
				if(o[1]!=null) org.setOrganizationNum((Integer)o[1]);
				org.setOrgTypeName((String)o[2]);
				org.setOrganizationName((String)o[3]);
				org.setDepartment((String)o[4]);
				org.setAddressPart1((String)o[5]);
				org.setAddressPart2((String)o[6]);
				org.setCity((String)o[7]);	
				org.setStateName((String)o[8]);
				org.setZip((String)o[9]);			
				org.setCountryName((String)o[10]);
				orgs.add(org);
			}
	
		return orgs;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Organization> search(String name, String dept, String city) {
		List<Organization> list = null;
		String q = null;
		if(name != null && !name.equals("")) q =" where lower(organizationName) like lower('%"+name+"%')";		
		if(dept != null && !dept.equals("")) {
			if(q == null) q =" where";
			else q += " and";
			q += " lower(department) like lower('%"+dept+"%')";
		}
		if(city != null && !city.equals("")) {
			if(q == null) q =" where";
			else q += " and";
			q += " lower(city) like lower('%"+city+"%')";
		}
		list = (List<Organization>)(List<?>) DBUtil.getObjectList(q);	
		return list;
	}

}
