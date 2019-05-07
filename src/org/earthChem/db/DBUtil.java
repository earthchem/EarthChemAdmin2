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

public class DBUtil {
	
	private static DataSource dataSource;

    static {
        try {
           // dataSource = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        	dataSource = (DataSource) new InitialContext().lookup( "java:jboss/datasources/earthchemDBDev" );
        }
        catch (NamingException e) { 
        	 System.err.println(e);
        }
    }
    
   
	//Used fro JSF
	public static List<org.earthChem.presentation.jsf.theme.Theme> getThemeList(String q) {
		List<org.earthChem.presentation.jsf.theme.Theme> items = new ArrayList<Theme>();	
		List<Object[]> list = list(q);		
		for(Object[] s: list) {
				items.add(new org.earthChem.presentation.jsf.theme.Theme((Integer)s[0], (String)s[1], (String)s[2]));			
		}
		return items;
	}
	
	//Used fro JSF
	public static List<Theme> getSimpleThemeList(String q) {
		List<Theme> items = new ArrayList<Theme>();	
		List<Object[]> list = list(q);		
		int i = 0;
		for(Object[] s: list) {
				items.add(new Theme(i++, (String)s[0], (String)s[0]));			
		}
		return items;
	}
	
	//Used fro JSF, get dropdown selected key and value
	public static SelectItem[] getSelectItems(String q) {
		SelectItem[] items = null;
		List<Object[]> list = list(q);		
		int size = list.size();
		items = new SelectItem[size];
		int i = 0;		
		for(Object[] s: list) {
			String label = ""+s[1];	
			items[i++]= new SelectItem(s[0], label);			
		}
		return items;
	}
	
	//used for deleting, updating and inserting 	
	 public static String update(String q) {
	    	String error = null;
	    	Connection con = null;
	    	Statement stmt = null;
	    	
	    	try {
	    		con = dataSource.getConnection();
	    		stmt = con.createStatement();
	    		con.setAutoCommit(false);
	    		stmt.executeUpdate(q);
	    		con.commit();
	    	} catch (SQLException e) { 
	       	 	System.err.println(e);
	       	 	error = e.getMessage();
	       	 	
	        } finally {
	        	try {
	        	/*	if(error != null) {
	        			addErrorMsg("insert into ec_admin_msg (msg, update_time) values ('"+error+"',now())");
	        		}
	        		*/
	        		if(stmt != null) stmt.close();
	        		if(con != null) con.close();   	
	        	} catch (SQLException e) {
	        		System.err.println(e);
	        	}
	        }  
	    	return error;       	
	 }
	 
	 
	 public static String addErrorMsg(String msg) {
	    	Connection con = null;
	    	Statement stmt = null;
	    	String error = null;
	    	try {
	    		con = dataSource.getConnection();
	    		stmt = con.createStatement();
	    		stmt.executeUpdate(msg);
	    	} catch (SQLException e) { 
	       	 	System.err.println(e);
	       	 	error = e.getMessage();
	       	 	
	        } finally {
	        	try {
	        		if(stmt != null) stmt.close();
	        		if(con != null) con.close();   	
	        	} catch (SQLException e) {
	        		System.err.println(e);
	        	}
	        }  
	    	return error;       	
	 }
	 
	 
	 

	/* generate text value for insert sql:
	 * 1. if empty string, return null; 
	 * 2. to escape a single quote "'", change it to two single quotes "''";
	 * 3. finally, add single quotes for text;
	 */
	 public static String StringValue(String v) {
	    	if(v != null) {
	    		if("".equals(v=v.trim())) return null;
	    		v = v.replaceAll("'", "''");
	    		v = "'"+v+"'"; 		
	    	} 
	    return v;
	 }

	public static Map<String, Integer> getMap(String query){
		Map<String, Integer> m  =new HashMap <String, Integer>();		
		List<Object[]> list = list(query);		
			for(Object[] s: list) {
			Integer num = null;
			if (s[1] instanceof Long) num = ((Long)s[1]).intValue();
			else num = (Integer)s[1];
			m.put((String)s[0], num);
		}
		return m;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<Integer, String> getIntegerStringMap(String query){
		Map<Integer, String> m  =new HashMap <Integer, String>();
		List<Object[]> list = list(query);				
		for(Object[] s: list){
			m.put((Integer)s[0], (String)s[1]);
		}
		return m;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<Integer, Integer> getIntegerMap(String query){
		Map<Integer, Integer> m  =new HashMap <Integer, Integer>();
		List<Object[]> list = list(query);				
		for(Object[] s: list) m.put((Integer)s[0], (Integer)s[1]);
		return m;
	}
	

	public static List<Object[]> getECList(String query){
		return list(query);
	}
	
	public static List<Integer> getIntegerList(String query){
		List<Integer> list = new ArrayList<Integer>();
		List<Object[]> alist = list(query);		
		for(Object[] arr :alist) list.add((Integer)arr[0]);		
		return list;
	}
	
	public static List<Object> getObjectList(String query){
		List<Object> list = new ArrayList<Object>();
		List<Object[]> alist = list(query);		
		for(Object[] arr :alist) list.add(arr[0]);		
		return list;
	}
	
	
	
	public static String integerInClause(String q) {
		List<Integer> list = getIntegerList(q);
		String in = "=null ";
			int size = list.size();
			if(size>0) {
				in = " in ("+list.get(0);
				for(int i = 1; i < size; i++) in += ","+list.get(i);	
				in +=") ";
			}
		return in;
	}
	
	//get number such as person_num
	public static Integer getNumber(String query){
		Integer num  = null;	
		Object obj = uniqueObject(query);					
		if(obj instanceof BigInteger) num = ((BigInteger)obj).intValue();
		else if (obj instanceof Long) num = ((Long)obj).intValue();
		else {
			num = (Integer) obj;
		}
		return num;
	}

	public static Object uniqueObject(String query) {
    	Object record =null;
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		con = dataSource.getConnection();
    		stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next())
            	 record = rs.getObject(1);
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
    	return record;
    }
	
	 public static String updateList(List<String> queries) {
	    	String error = null;
	    	Connection con = null;
	    	Statement stmt = null;
	    	
	    	try {
	    		con = dataSource.getConnection();
	    		stmt = con.createStatement();
	    		con.setAutoCommit(false);
	    		for(String q: queries) {
	    			stmt.executeUpdate(q);
	    		}
	    		con.commit();
	    	} catch (SQLException e) { 
	       	 	System.err.println(e);
	       	 	error = e.getMessage();
	        } finally {
	        	try {
	        		if(stmt != null) stmt.close();
	        		if(con != null) con.close();   	
	        	} catch (SQLException e) {
	        		System.err.println(e);
	        	}
	        }  
	    	return error;       	
	 }
	
	 
	 public static Object[] uniqueResult(String query) {
		 List<Object[]> records = list(query);
		 Object[] arr = null;
		 if(records != null && records.size() > 0) arr = records.get(0);
	     return arr;
	 } 
	 
	 public static List<Object[]> list(String query) {
	    	List<Object[]> records=new ArrayList<Object[]>();
	    	Connection con = null;
	    	Statement stmt = null;
	    	ResultSet rs = null;
	    	
	    	try {
	    		con = dataSource.getConnection();
	    		stmt = con.createStatement();
	            rs = stmt.executeQuery(query);
	            int cols = rs.getMetaData().getColumnCount();
	            while(rs.next()){
	            	 Object[] arr = new Object[cols]; 
	            	 for(int i=0; i<cols; i++){ 
	            		 arr[i] = rs.getObject(i+1); 
	            	 } 
	            	 records.add(arr); }
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
	    	return records;
	    }
	 
	 public static String listToNumbers(List<Integer> list) {
		 if(list != null && list.size() > 0) {
			 String array = list.toString();
			 array = array.substring(1,array.length()-1);
			 return array;
		 }
		 return null;		
	 }
	 
	 public static String[] stringArray(String query) {
		 String[] items = null;
			List<Object[]> list = list(query);		
			int size = list.size();
			items = new String[size];
			int i = 0;		
			for(Object[] s: list) {
				items[i++]=""+ s[0];			
			}
			return items;
	 }
	 
	 public static StringTable download(String query) {
		 StringTable table = new StringTable();
		 ArrayList<Object[]> records=new ArrayList<Object[]>();
	    	Connection con = null;
	    	Statement stmt = null;
	    	ResultSet rs = null;
	    	
	    	try {
	    		con = dataSource.getConnection();
	    		stmt = con.createStatement();
	            rs = stmt.executeQuery(query);	            
	            ResultSetMetaData metadata = rs.getMetaData();
	            int cols = metadata.getColumnCount();
	            String[] columns = new String[cols];
	            for (int i = 0; i < cols; i++) {
	            	columns[i] = metadata.getColumnName(i+1);  
	            }
	            table.setHeads(columns);	            
	            while(rs.next()){
	            	 Object[] arr = new Object[cols]; 
	            	 for(int i=0; i<cols; i++){ 
	            		 arr[i] = rs.getObject(i+1); 
	            	 } 
	            	 records.add(arr); }
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
		
		 List<Object[]> list = list(q);			
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
