package org.earthChem.db;
/**
 * This class is in business layer, which receives request from controller (PersonBean.java), 
 * creates SQL statements, gets data from DBUtil.java, and return data to controller.
 * 
 * @author      Bai-Hao Chen 
 * @version     1.0               
 * @since       1.0     (9/15/2017)
 */
import java.util.ArrayList;
import java.util.List;
import org.earthChem.db.postgresql.hbm.Affiliation;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.Person;
import org.earthChem.db.postgresql.hbm.PersonExternalIdentifier;
import org.earthChem.presentation.jsf.theme.Theme;

public class PersonDB {

	public static List<Person> getPersonList() {
		String q = "select p.person_num, p.first_name, p.middle_name, p.last_name, p.pnum, o.organization_name, o.department, array_agg(al.citation_num) "+
				" from person p left join affiliation a on p.person_num = a.person_num "+
				" left join organization o on a.organization_num = o.organization_num "+
				" left join author_list al on p.person_num = al.person_num "+
				" group by  p.person_num, p.first_name, p.middle_name, p.last_name, p.pnum, o.organization_name, o.department "+				
				" order by p.last_name ";
		List<Object[]> list = DBUtil.getECList(q);
		List<Person> ps = new ArrayList<Person>();
		for(Object[] arr: list) {
			Person p = new Person();
			p.setPersonNum((Integer)arr[0]);
			p.setFirstName((String)arr[1]);
			p.setMiddleName((String)arr[2]);
			p.setLastName((String)arr[3]);
			p.setPnum((Integer)arr[4]);
			Organization org = new Organization();
			if(arr[5] != null) org.setOrganizationName(""+arr[5]);
			if(arr[6] != null) org.setDepartment(""+arr[6]);
			if(arr[7] != null) p.setCitations(""+arr[7]);
			p.setOrganization(org);
			ps.add(p);
		}
		return ps;
	}
	
	public static Person getPerson(Integer item) {
		String q = "select * from person where person_num="+item;;
		List<Object[]> list = DBUtil.getECList(q);
		Object[] arr = list.get(0);
		Person p = new Person();
			p.setPersonNum((Integer)arr[0]);
			p.setFirstName((String)arr[1]);
			p.setMiddleName((String)arr[2]);
			p.setLastName((String)arr[3]);
			p.setPnum((Integer)arr[4]);
		return p;
	}

	public static List<Affiliation> getAffiliactions(Integer item) {
		String q = "select p.person_num, a.affiliation_Num, a.organization_num, concat(organization_name,(', '||department)), a.primary_email from person p, affiliation a, organization o "+
			"where p.person_num = a.person_num and a.organization_num = o.organization_num and p.person_num = "+item;
		List<Object[]> list = DBUtil.getECList(q);
		List<Affiliation> affs = new ArrayList<Affiliation>(); 
		for(Object[] arr: list) {
			Affiliation a = new Affiliation();
			a.setPersonNum((Integer)arr[0]);
			a.setAffiliationNum((Integer)arr[1]);
			a.setOrganizationNum((Integer)arr[2]);
			a.setOrganizationName((String)arr[3]);
			a.setPrimaryEmail((String)arr[4]);
			affs.add(a);
		}
		return affs;
	}
	
	public static List<PersonExternalIdentifier> getPersonExternalIdentifier(Integer item) {
		String q = "select e.external_identifier_system_name, p.person_external_identifier, p.bridge_Num "+
				" from person_external_identifier p, external_identifier_system e "+
				" where p.external_identifier_system_num = e.external_identifier_system_num and p.person_num = "+item;
		List<Object[]> list = DBUtil.getECList(q);
		List<PersonExternalIdentifier> peis = new ArrayList<PersonExternalIdentifier>(); 
		for(Object[] arr: list) {
			PersonExternalIdentifier a = new PersonExternalIdentifier();
			a.setExternalIdentifierSystemName((String)arr[0]);
			a.setPersonExternalIdentifier((String)arr[1]);
			a.setBridgeNum((Integer)arr[2]);
			peis.add(a);
		}
		return peis;
	}
	
	public static String inactivePerson(Integer personNum) {
		String q = "update person set status = 0 where person_num = "+personNum;
		return DBUtil.update(q);
	}
		
	public static String addPersonAndAuthorList(Person person, List<Affiliation> affs, List<PersonExternalIdentifier> peis, Integer citationNum) {
		String status = addPerson(person,affs, peis);
		Integer personNum = person.getPersonNum();
		if(status == null) {
			Integer authorOrder = DBUtil.getNumber("SELECT max(author_order) from author_list where citation_num ="+citationNum);
			if(authorOrder == null) authorOrder = 1;
			else authorOrder++;
			String q = "INSERT INTO author_list values ("+
					"nextval('author_list_author_list_num_seq'),"+citationNum+","+personNum+","+authorOrder+")";
			status = DBUtil.update(q);
		}
		return status;
	}
		
	public static String addPerson(Person person, List<Affiliation> affs, List<PersonExternalIdentifier> peis) {
		List<String> queries = new ArrayList<String>();
		Integer personNum = DBUtil.getNumber("select nextval('person_person_num_seq')");
		String q = "select max(pnum) from person where last_name = '"+person.getLastName()+"' and first_name ='"+person.getFirstName()+"'";
		Integer pnum = DBUtil.getNumber(q);
		if(pnum !=null) person.setPnum(pnum+1);
		queries.add(insertPersonQuery(person,personNum)); 
		
		for(PersonExternalIdentifier e:peis) { 
			String ei = e.getPersonExternalIdentifier();
			if(ei != null && !"".equals(ei.trim())) {
				q = "insert into person_external_identifier values (nextval('person_external_identifier_bridge_num_seq'),"+personNum+
				",'" + ei+"',null,"+ e.getExternalIdentifierSystemNum()+")";		
				queries.add(q);
			}
		}
		
		for(Affiliation a:affs) { 
			Integer orgNum = a.getOrganizationNum();
			if(orgNum != null) {
				String email = a.getPrimaryEmail();
				if(email != null) email = "'"+email+"'";
				q = "insert into affiliation (affiliation_num,person_num, organization_num, primary_email) "+
				" values (nextval('affiliation_affiliation_num_seq'),"+personNum+","+orgNum+","+email+")";			
				queries.add(q);
			}
		}
		person.setPersonNum(personNum);
		return DBUtil.updateList(queries);		
	}
	
	private static String insertPersonQuery(Person p, Integer personNum) {	
		return "INSERT INTO person VALUES ("+personNum+","+DBUtil.StringValue(p.getFirstName())+","+DBUtil.StringValue(p.getMiddleName())+
				","+DBUtil.StringValue(p.getLastName())+","+p.getPnum()+")";
	}
	
	public static String updatePerson(Person person, List<Affiliation> affs, List<PersonExternalIdentifier> peis) {
		List<String> queries = new ArrayList<String>();
		queries.add(updatePersonQuery(person));
		String q = "";
		for(PersonExternalIdentifier e:peis) { 
			String ei = e.getPersonExternalIdentifier();
			if(ei != null && !"".equals(ei.trim())) {
				q = "insert into person_external_identifier values (nextval('person_external_identifier_bridge_num_seq'),"+person.getPersonNum()+
				",'" + ei+"',null,"+ e.getExternalIdentifierSystemNum()+")";		
				queries.add(q);
			}
		}
		
		for(Affiliation a:affs) { 
			Theme org = a.getSelectedOrg();	
			if(org != null) {
				String email = a.getPrimaryEmail();
				if(email != null) email = "'"+email+"'";
				q = "insert into affiliation (affiliation_num,person_num, organization_num, primary_email) "+
				" values (nextval('affiliation_affiliation_num_seq'),"+person.getPersonNum()+","+org.getId()+","+email+")";			
				queries.add(q);
			}
		}
		return DBUtil.updateList(queries);		
	}
	
	private static String updatePersonQuery(Person p) {
		return "UPDATE person SET first_name = "+DBUtil.StringValue(p.getFirstName())+
				", middle_name = "+DBUtil.StringValue(p.getMiddleName())+
				", last_name = "+DBUtil.StringValue(p.getLastName())+
				", pnum = "+p.getPnum() +"WHERE person_num = "+p.getPersonNum();	
	}
}