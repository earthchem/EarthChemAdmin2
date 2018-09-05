package org.earthChem.presentation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.earthChem.db.ActionDB;
import org.earthChem.db.DBUtil;
import org.earthChem.db.MethodInfoDB;
import org.earthChem.db.postgresql.hbm.Action;
import org.earthChem.db.postgresql.hbm.MethodInfo;

@ManagedBean(name="methodInfoBean")
@SessionScoped
/*
 1. 209 action_num are missing  (select count(distinct m.action_num) from additional_method_info m where  m.action_num not in (select action_num from action) 
 2.  null is 'null' ((content::json->'STANDARD_DATA')::text = 'null')  
 3.  many METHOD_PRECISION_TYPE = "null";
 */

// testing citation_num => 3468 after migration
// migrate citation_num <= 3467

public class MethodInfoBean implements Serializable {
	private List<MethodInfo> methodInfoList;
	private Integer citationNum;
	private List<Action> actionList;
	private SelectItem[] irs; //Radiogenic_Isotopes
	
	public void createNew() {
		 actionList = ActionDB.getActionList(citationNum);		
	}
	
	
	public void lookup() {
		methodInfoList = MethodInfoDB.getMethodInfoList(citationNum);		
	}

	public List<MethodInfo> getMethodInfoList() {
		if(methodInfoList==null) 	methodInfoList=new ArrayList<MethodInfo>();
		return methodInfoList;
	}

	public void setMethodInfoList(List<MethodInfo> methodInfoList) {
		this.methodInfoList = methodInfoList;
	}

	public Integer getCitationNum() {
		return citationNum;
	}

	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}

	public List<Action> getActionList() {
		if(actionList==null) actionList = new  ArrayList<Action>();
		return actionList;
	}

	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}
	

	
	
	public SelectItem[] getIrs() {
		if(irs==null) {
			String q ="select v.variable_num, v.variable_code from variable v  where v.variable_type_num=7 order by variable_code";
			irs= DBUtil.getSelectItems(q); 
		}
		return irs;
	}
	

/*	private String migrate="";
	private Map<String, Integer> sfMap = new HashMap<String, Integer>();
	private Map<String, Integer> actionMap = new HashMap<String, Integer>();
	private Integer infoNum = 0;
		
	public String getMigrate()
	{
	    return migrate;
	}
    
	public void setMigrate(String ms)
	{
		migrate = ms;
	}
 
    @SuppressWarnings("deprecation")
	public void doMigrate()
	{	
    	ECUtil.addMessage("AdditionalMethodInfoBean");
    	createMap(); 
    	fractCorrect(); 
    	methodPrecis(); 
    	normalization(); 
    	standard();
    	ECUtil.addMessage("end of AdditionalMethodInfoBean");
		migrate = "success, Check EarthChemDB additional_method_info table.";
	}
     
    @SuppressWarnings("unchecked")
	 private void standard() {
		 Session ps = ConfigurationHelper.getPetDBSessionFactory().openSession();
		 Session es = ConfigurationHelper.getEarthChemDBSessionFactory().openSession();
		Transaction tx = null;
		try 
		{	
			String q = "select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM, i.ITEM_CODE, s.STANDARD_NAME, s.STANDARD_VALUE, s.UNIT "+
			" from standard s, data_quality d, analysis a, METHOD m,  BATCH b, ITEM_MEASURED i "+
			" where d.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM and a.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM "+
			" and m.METHOD_NUM = d.METHOD_NUM and b.BATCH_NUM = a.BATCH_NUM and i.ITEM_MEASURED_NUM = s.ITEM_MEASURED_NUM order by m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM ";
		  		List<Object []> list =  ps.createSQLQuery(q).list();	
		  	   tx = es.beginTransaction();
		  		for(Object [] item: list) {		  			
		  			Integer actionNum = actionMap.get((String)item[0]);
		  			if(actionNum == null) continue;
		  			String name = ((String)item[2]).replaceAll("'", "''");
		  			q = "insert into additional_method_info (additional_method_info_num, action_num, variable_code, content) "+
		  				" values ("+(++infoNum)+","+actionNum+",'"+item[1]+"',"+
		  				"'{\"STANDARD_NAME\":\""+name+"\","+
		  				 "\"STANDARD_VALUE\":\""+item[3]+"\","+
		  				 "\"UNIT\":\""+item[4]+"\"}')";		  
		  			es.createSQLQuery(q).executeUpdate();
		  		}	
		  		tx.commit();
			}
			catch(HibernateException e)
			{
				if(tx !=null) tx.rollback();
				e.printStackTrace();
				migrate=e.getMessage();
			}
			finally
			{
				ps.close();
				es.close();
				ECUtil.addMessage("standard():"+migrate);
			}
	}
   
  
    @SuppressWarnings("unchecked")
	 private void normalization() {
		 Session ps = ConfigurationHelper.getPetDBSessionFactory().openSession();
		 Session es = ConfigurationHelper.getEarthChemDBSessionFactory().openSession();
		Transaction tx = null;
		try 
		{	
			String q = "select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM, i.ITEM_CODE,  n.NORM_STANDARD_NAME, n.NORM_VALUE "+
			" from normalization n, NORMALIZATION_LIST l, data_quality d, analysis a, BATCH b, method m, ITEM_MEASURED i "+
			" where n.NORMALIZATION_NUM = l.NORMALIZATION_NUM and d.DATA_QUALITY_NUM = l.DATA_QUALITY_NUM and a.DATA_QUALITY_NUM = l.DATA_QUALITY_NUM "+
			" and b.BATCH_NUM = a.BATCH_NUM and i.ITEM_MEASURED_NUM = n.ITEM_MEASURED_NUM "+
			" and m.METHOD_NUM = d.METHOD_NUM order by m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM ";
		  		List<Object []> list =  ps.createSQLQuery(q).list();	
		
		  	   tx = es.beginTransaction();
		  		for(Object [] item: list) {		  			
		  			Integer actionNum = actionMap.get((String)item[0]);
		  			if(actionNum == null) continue;
		  			q = "insert into additional_method_info (additional_method_info_num, action_num, variable_code, content) "+
		  				" values ("+(++infoNum)+","+actionNum+",'"+item[1]+"',"+
		  				"'{\"NORM_STANDARD_NAME\":\""+item[2]+"\","+
		  				 "\"NORM_STANDARD_VALUE\":\""+item[3]+"\"}')";		  
		  			es.createSQLQuery(q).executeUpdate();
		  			//System.out.println("bc-q1: "+q);
		  		}	
		  		tx.commit();
			}
			catch(HibernateException e)
			{
				if(tx !=null) tx.rollback();
				e.printStackTrace();
				migrate=e.getMessage();
			}
			finally
			{
				ps.close();
				es.close();
				ECUtil.addMessage("normalization():"+migrate);
			}
	}
    
    
    @SuppressWarnings("unchecked")
	 private void methodPrecis() {
		 Session ps = ConfigurationHelper.getPetDBSessionFactory().openSession();
		 Session es = ConfigurationHelper.getEarthChemDBSessionFactory().openSession();
		Transaction tx = null;
		try 
		{	
			String q = "select  distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM, i.ITEM_CODE, precision_min, precision_max, precision_type "+
			" from method_precis s, data_quality d, analysis a, METHOD m,  BATCH b, ITEM_MEASURED i "+
			" where d.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM and a.DATA_QUALITY_NUM = s.DATA_QUALITY_NUM "+
			"and m.METHOD_NUM = d.METHOD_NUM and b.BATCH_NUM = a.BATCH_NUM and i.ITEM_MEASURED_NUM = s.ITEM_MEASURED_NUM order by  m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM";
		  		List<Object []> list =  ps.createSQLQuery(q).list();	
		 
		  	   tx = es.beginTransaction();
		  		for(Object [] item: list) {		  			
		  			Integer actionNum = actionMap.get((String)item[0]);
		  			if(actionNum == null) continue;
		  			q = "insert into additional_method_info (additional_method_info_num, action_num, variable_code, content) "+
		  				" values ("+(++infoNum)+","+actionNum+",'"+item[1]+"',"+
		  				"'{\"METHOD_PRECISION_MIN\":\""+item[2]+"\","+
		  				 "\"METHOD_PRECISION_MAX\":\""+item[3]+"\","+
		  				 "\"METHOD_PRECISION_TYPE\":\""+item[4]+"\"}')";		  
		  			es.createSQLQuery(q).executeUpdate();
		  			
		  		}	
		  		tx.commit();
			}
			catch(HibernateException e)
			{
				if(tx !=null) tx.rollback();
				e.printStackTrace();
				migrate=e.getMessage();
			}
			finally
			{
				ps.close();
				es.close();
				ECUtil.addMessage("methodPrecis():"+migrate);
			}
	}
    
    @SuppressWarnings("unchecked")
 	 private void fractCorrect() {
		 Session ps = ConfigurationHelper.getPetDBSessionFactory().openSession();
		 Session es = ConfigurationHelper.getEarthChemDBSessionFactory().openSession();
			Transaction tx = null;
			try 
			{	
				String q = "select distinct m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM,  si.ITEM_code item_code, s.fcorr_standard_name, s.fcorr_value, fi.ITEM_code FCORR_code "+
				 " from fract_correct s, fract_correct_list l, data_quality d, METHOD m, ITEM_MEASURED fi, ITEM_MEASURED si,  BATCH b, analysis a "+
				 " where d.DATA_QUALITY_NUM = l.DATA_QUALITY_NUM  and b.BATCH_NUM = a.BATCH_NUM and a.DATA_QUALITY_NUM = d.DATA_QUALITY_NUM "+
				 " and m.METHOD_NUM = d.METHOD_NUM and  s.FRACT_CORRECT_NUM = l.FRACT_CORRECT_NUM "+
				 " and si.ITEM_MEASURED_NUM = s.ITEM_MEASURED_NUM and fi.ITEM_MEASURED_NUM = s.ITEM_FCORR_NUM order by  m.METHOD_CODE||'-'||d.INSTITUTION_NUM||'-'||b.TABLE_IN_REF_NUM";
		  		List<Object []> list =  ps.createSQLQuery(q).list();	
		  	//	int i = 0;
		  	   tx = es.beginTransaction();
		  		for(Object [] item: list) {
		  			
		  			Integer actionNum = actionMap.get((String)item[0]);
		  			if(actionNum == null) continue;
		  			q = "insert into additional_method_info (additional_method_info_num, action_num, variable_code, content) "+
		  				" values ("+(++infoNum)+","+actionNum+",'"+item[1]+"',"+
		  				"'{\"FCORR_RATIO\":\""+item[4]+"\","+
		  				 "\"FCORR_STANDARD_NAME\":\""+item[2]+"\","+
		  				 "\"FCORR_STANDARD_VALUE\":\""+item[3]+"\"}')";		  
		  			es.createSQLQuery(q).executeUpdate();
		  			//System.out.println("bc-q1: "+q);
		  		}	
		  		tx.commit();
			}
			catch(HibernateException e)
			{
				if(tx !=null) tx.rollback();
				e.printStackTrace();
				migrate=e.getMessage();
			}
			finally
			{
				ps.close();
				es.close();
				ECUtil.addMessage("fractCorrect():"+migrate);
			}
	}
    	
    
     @SuppressWarnings("unchecked")
	 private void createMap() {
		 Session mys = ConfigurationHelper.getEarthChemDBSessionFactory().openSession();
		
			try 
			{			
			
				List<Object []> list =  mys.createSQLQuery("select m.method_code||'-'||a.organization_num||'-'||a.dataset_num, a.action_num "+
						" from action a, method m where m.method_num = a.method_num").list();		 
				
				for (Object [] item : list  ) {
	  			
				   actionMap.put((String)item[0], (Integer)item[1]);
			   } 
			   infoNum = (Integer)mys.createSQLQuery("select max(additional_method_info_num) from additional_method_info").uniqueResult();
			   if(infoNum == null) infoNum = 0;
			}
			catch(HibernateException e)
			{
				e.printStackTrace();
				migrate=e.getMessage();
			}
			finally
			{
				mys.close();
			}
	}
	*/
}
