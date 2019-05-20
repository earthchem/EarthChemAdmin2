package org.earthChem.db;
import java.util.ArrayList;
/**
 * This is database utility class.
 * 
 * @author      Bai-Hao Chen 
 * @version     1.0               
 * @since       1.0     (8/23/2017)
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.earthChem.db.postgresql.hbm.StringTable; 
import org.earthChem.presentation.jsf.theme.Theme;

public class SampleDownload {
	
	private static DataSource dataSource;
//	private static String[] dataHead = {"Sample_ID","IGSN",	"Material", "Latitude","Longitude","Elevation","Tectonic",	"Rock","Reference",	"Method","Expedition"};
	
		

	//Reference: last name of the first author, year[citation_num]
		//Method: method_code[action_num]
			

    static {
        try {
           // dataSource = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        	dataSource = (DataSource) new InitialContext().lookup( "java:jboss/datasources/earthchemDBDev" );
        }
        catch (NamingException e) { 
        	 System.err.println(e);
        }
    }
    
    public static StringTable getReferences(String materialCode, String condition, String queryType) {	
   	    StringTable table = new StringTable();
   	    String[] refHead = {"Author","Year","Title","Journal","Volume","Book_Title","Pages","Editors","Publishers","doi"};
	   	String q = "select distinct c.authors, c.publication_year, c.title,  c.journal, c.volume, c.book_title,c.pages,'', c.publisher, c.doi "+
	   			" from mv_dataset_result_summary d, mv_specimen_summary m,  mv_citation_summary c " + 
		 		" where d.specimen_num=m.specimen_num " +materialCode+" and d.citation_num = c.citation_num "+condition;
	    table.setHeads(refHead);	 
	    System.out.println("download query: "+q);
	    table.setData(( ArrayList<Object[]> ) DBUtil.list(q));
   
		 return table;
	 }
    
    
    public static StringTable getMethods(String materialCode, String condition, String queryType) {	
	   	 StringTable table = new StringTable();
	   	 String[] methodHead = {"Method_Code","Name", "Location","Reference","Comment"};
		
	   	String q = "select distinct d.method_code||'['||d.action_num||']', d.method_name, d.laboratory, c.citation_code||'['||c.citation_num||']', d.method_comment "+
	   			" from mv_dataset_result_summary d, mv_specimen_summary m,  mv_citation_summary c " + 
		 		" where d.specimen_num=m.specimen_num " +materialCode+" and d.citation_num = c.citation_num "+condition;
	    table.setHeads(methodHead);	 
	    System.out.println("download query: "+q);
	    table.setData(( ArrayList<Object[]> ) DBUtil.list(q));
    
		 return table;
	 }
    
     public static StringTable getData(String materialCode, String variableType, String condition, String queryType) {	
    	 StringTable table = new StringTable();
    	 String[] dataHead = {"SAMPLE_ID","SAMPLE_NAME","IGSN","SAMPLE_TYPE","LATITUDE","LONGITUDE","ELEVATION_MIN","ELEVATION_MAX","TECTONIC_SETTING","ROCK NAME","REFERENCE","METHOD","EXPEDITION ID"};

		 
    	 String select = "select d.specimen_code \"SAMPLE ID\",d.specimen_name, m.igsn \"IGSN\", d.material_name \"SAMPLE TYPE\"," +		 
				 "split_part(split_part(split_part(split_part(m.geometry_text,'(',2), ' ', 2), ')', 1), ',',1) \"LATITUDE\","+
				 "split_part(split_part(m.geometry_text,'(',2), ' ', 1) \"LONGITUDE\", m.elevation_min, m.elevation_max," +
				 "array_to_string(m.tectonic_settings,',') \"Tectonic\", split_part(array_to_string(m.taxon,','),'|',2) \"ROCK NAME\","+
				 "c.citation_code||'['||c.citation_num||']' \"Reference\", d.method_code||'['||d.action_num||']'  \"METHOD\","+
				 "case when d.expedition_code ='nr' THEN '' else d.expedition_code end \"EXPEDITION ID\","+
				 "d.variable_code \"VARIABLE\", d.value_meas \"VALUE\", d.specimen_num" ;
						 
		 		
				
		 String body = " from mv_dataset_result_summary d, mv_specimen_summary m,  mv_citation_summary c, variable v, variable_type t " + 
		 			" where d.specimen_num=m.specimen_num " +materialCode+" and d.citation_num = c.citation_num "+
		 			" and d.variable_num = v.variable_num and v.variable_type_num = t.variable_type_num "+ variableType+condition;
		 	//		condition+variableType;

		 //create column names with variable
		 Map<String, Integer> map  =new HashMap <String, Integer>();	
		 String q = "select d.variable_code, d.variable_order "+body+ " group by d.variable_code, d.variable_order order by d.variable_order";
		
		 List<Object[]> list = DBUtil.list(q);			
		 String [] titles = new String[dataHead.length+list.size()]; 
		 int k=0; 
		 for(;k < dataHead.length; k++) titles[k] = dataHead[k];
		 for(Object[] s: list) {
			 titles[k] = (String)s[0];
			 map.put((String)s[0], k++);
		}
		 
		 table.setHeads(titles);
		 ArrayList<Object[]> records=new ArrayList<Object[]>();
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
   
    	try {
    		con = dataSource.getConnection();
    		stmt = con.createStatement();
    		System.out.println("download query: "+select+body+ " order by d.specimen_code, d.specimen_num");
            rs = stmt.executeQuery(select+body+ " order by d.specimen_code, d.specimen_num");	            
            ResultSetMetaData metadata = rs.getMetaData();
            int cols = metadata.getColumnCount();
            int prev= 0;
            Object[] arr = null;
            while(rs.next()) {
            	int current =  (Integer) rs.getObject(dataHead.length+3);
        
            	if(prev==0 || prev != current) {
            		if(prev != 0) records.add(arr);
            		arr = new Object[titles.length]; 
            		 for(int i=0; i<cols; i++){ 
                		 if(i < dataHead.length) arr[i] = rs.getObject(i+1); 
                		 if(i==dataHead.length) {
                			 Integer index = map.get((String)rs.getObject(i+1));
                			 arr[index] = rs.getObject(i+2); 
                		 }
            		 }
            	} else {
            		for(int i=0; i<cols; i++){ 
	               		 if(i==dataHead.length) {
	               			 Integer index = map.get((String)rs.getObject(i+1));
	               			 arr[index] = rs.getObject(i+2); 
	               		 }
            		}
            	}
            	prev =current;
            }
            	 records.add(arr);
    	} catch (SQLException e) { 
       	 	System.err.println(e);
        } finally {
        	try {
        		if(rs != null) rs.close();
        		if(stmt != null) stmt.close();
        		if(con != null) con.close();   
        	} catch (SQLException e) {
        		System.err.println(e);
        	}
        }
    	table.setData(records);
    	return table;
	 }
    	 
  
	 public static StringTable sampleDownload(String materialCode, String variableType, String condition, String queryType) {	 
		 String[] h1 = {"SAMPLE ID","IGSN","DOI","TITLE","JOURNAL","AUTHOR","EXPEDITION ID","LATITUDE","LONGITUDE","ELEVATION_MIN","ELEVATION_MAX","MIN AGE","AGE","MAX AGE","METHOD","SAMPLE TYPE","ROCK NAME"};
		 String[] h2 = {"SAMPLE ID","IGSN","DOI","TITLE","JOURNAL","AUTHOR","EXPEDITION ID","LATITUDE","LONGITUDE","ELEVATION_MIN","ELEVATION_MAX","MIN AGE","AGE","MAX AGE","METHOD","SAMPLE TYPE","ROCK NAME","HOST MINERAL"};
		 String[] heads = null;
		 String hostMineral = "";
		 if(!"MeltInclusions".equals(queryType)) heads = h1;
		 else {
			 heads = h2;
			 hostMineral = "d.inclusion_host_mineral,";
		 }
		 
		 String select = "select d.specimen_code \"SAMPLE ID\", m.igsn \"IGSN\", c.doi \"DOI\", c.title \"TITLE\",  c.journal \"JOURNAL\", c.authors \"AUTHOR\", case when d.expedition_code ='nr' THEN '' else d.expedition_code end \"EXPEDITION ID\", " + 
				"split_part( split_part(split_part(split_part(m.geometry_text,'(',2), ' ', 2), ')', 1), ',',1) \"LATITUDE\", split_part(   split_part(m.geometry_text,'(',2), ' ', 1) \"LONGITUDE\",  m.elevation_min, m.elevation_max, " + 
				" split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',3) \"MIN AGE\", split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',2) \"AGE\", " + 
				"split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',4) \"MAX AGE\", d.method_code \"METHOD\", d.material_name \"SAMPLE TYPE\",  " + 
				"split_part(array_to_string(m.taxon,','),'|',2) \"ROCK NAME\", "+ hostMineral+" d.variable_code \"VARIABLE\", d.value_meas \"VALUE\", d.specimen_num " ;
		 String body = " from mv_dataset_result_summary d, mv_specimen_summary m,  mv_citation_summary c, variable v, variable_type t " + 
		 			" where d.specimen_num=m.specimen_num " +materialCode+" and d.citation_num = c.citation_num "+
		 			" and d.variable_num = v.variable_num and v.variable_type_num = t.variable_type_num "+ variableType+
		 			condition+variableType;

		 //create column names with variable
		 Map<String, Integer> map  =new HashMap <String, Integer>();	
		 String q = "select d.variable_code, d.variable_order "+body+ " group by d.variable_code, d.variable_order order by d.variable_order";
		
		 List<Object[]> list = DBUtil.list(q);			
		 String [] titles = new String[heads.length+list.size()]; 
		 int k=0; 
		 for(;k < heads.length; k++) titles[k] = heads[k];
		 for(Object[] s: list) {
			 titles[k] = (String)s[0];
			 map.put((String)s[0], k++);
		}
		 
		 StringTable table = new StringTable();
		 table.setHeads(titles);
		 ArrayList<Object[]> records=new ArrayList<Object[]>();
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
   
    	try {
    		con = dataSource.getConnection();
    		stmt = con.createStatement();
    		System.out.println("download query: "+select+body+ " order by d.specimen_code, d.specimen_num");
            rs = stmt.executeQuery(select+body+ " order by d.specimen_code, d.specimen_num");	            
            ResultSetMetaData metadata = rs.getMetaData();
            int cols = metadata.getColumnCount();
            int prev= 0;
            Object[] arr = null;
            while(rs.next()) {
            	int current =  (Integer) rs.getObject(heads.length+3);
        
            	if(prev==0 || prev != current) {
            		if(prev != 0) records.add(arr);
            		arr = new Object[titles.length]; 
            		 for(int i=0; i<cols; i++){ 
                		 if(i < heads.length) arr[i] = rs.getObject(i+1); 
                		 if(i==heads.length) {
                			 Integer index = map.get((String)rs.getObject(i+1));
                			 arr[index] = rs.getObject(i+2); 
                		 }
            		 }
            	} else {
            		for(int i=0; i<cols; i++){ 
	               		 if(i==heads.length) {
	               			 Integer index = map.get((String)rs.getObject(i+1));
	               			 arr[index] = rs.getObject(i+2); 
	               		 }
            		}
            	}
            	prev =current;
            }
            	 records.add(arr);
    	} catch (SQLException e) { 
       	 	System.err.println(e);
        } finally {
        	try {
        		if(rs != null) rs.close();
        		if(stmt != null) stmt.close();
        		if(con != null) con.close();   
        	} catch (SQLException e) {
        		System.err.println(e);
        	}
        }
    	table.setData(records);;
    	return table;
	 }
	 
	 
}
