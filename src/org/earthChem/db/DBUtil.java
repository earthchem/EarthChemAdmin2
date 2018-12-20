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
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.earthChem.presentation.jsf.theme.Theme;

public class DBUtil {
	
	private static DataSource dataSource;

    static {
        try {
           // dataSource = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        	dataSource = (DataSource) new InitialContext().lookup( "java:jboss/datasources/tephraDBDev" );
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
		for(Object[] s: list) m.put((String)s[0], (Integer)s[1]);
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
}
