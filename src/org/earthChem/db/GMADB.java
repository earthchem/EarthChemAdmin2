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

public class GMADB {
	private static Map<String, Integer> variableTypeMap = new HashMap<String, Integer>();
	private static Map<String, Integer> materialMap = new HashMap<String, Integer>();
	private static Map<String, Integer> rockclassMap = new HashMap<String, Integer>();

	
	
/*	
Query for stations_new pl part 1:
select station_num, count(*) ct, array_to_string(array_agg(specimen_num),',') specimen_nums from mv_specimen_summary group by station_num order by station_num

Query for stations_new pl part 2:
select st.station_num, st.station_code, st.expedition_num, st.station_num, 0 as "samp_technique_num",  mat.material_code, vt.variable_type_code, 0 as "ALTERATION_NUM", rt.taxonomic_classifier_name
		 from mv_station_summary st 
		 left join (select  distinct station_num,material_code from mv_dataset_result_summary where material_code in ('ROCK','WR','GL','MIN')) mat  on mat.station_num= st.station_num  
	 	 left join (select distinct station_num, vt.variable_type_code from mv_dataset_result_summary drs,  variable v, variable_type vt  
		 	where v.variable_num = drs.variable_num and v.variable_type_num = vt.variable_type_num) vt on st.station_num= vt.station_num 
		
		 left join (select distinct ss.station_num, tcp.taxonomic_classifier_name 
		 	 from mv_specimen_summary ss, sampling_feature_taxonomic_classifier ftc, taxonomic_classifier tc,  taxonomic_classifier tcp 
		 	 where ss.specimen_num = ftc.sampling_feature_num and ftc.taxonomic_classifier_num = tc.taxonomic_classifier_num  
		 	 and tc.taxonomic_classifier_type_cv = 'Rock Class' and tc.parent_taxonomic_classifier_num = tcp.taxonomic_classifier_num ) rt on rt.station_num = st.station_num order by station_num;

		 	 
Query for pdb_dataC_new pl:
select distinct drs.specimen_num, drs.station_num, drs.specimen_code, rt.taxonomic_classifier_name, mat.material_num,  drs.citation_num, 0 as "DATA_QUALITY_NUM",
v.variable_code, vt.variable_type_code, drs.unit, drs.value_meas, 'null' as "STDEV"
from mv_dataset_result_summary drs  
join variable v on v.variable_num = drs.variable_num 
join variable_type vt on  v.variable_type_num = vt.variable_type_num
join material mat on mat.material_code = drs.material_code and mat.material_num in (3,7,8) 
left join (select distinct ss.specimen_num, tcp.taxonomic_classifier_name 
		 	 from mv_specimen_summary ss, sampling_feature_taxonomic_classifier ftc, taxonomic_classifier tc,  taxonomic_classifier tcp 
		 	 where ss.specimen_num = ftc.sampling_feature_num and ftc.taxonomic_classifier_num = tc.taxonomic_classifier_num  
		 	 and tc.taxonomic_classifier_type_cv = 'Rock Class' and tc.parent_taxonomic_classifier_num = tcp.taxonomic_classifier_num ) rt on rt.specimen_num = drs.specimen_num
where  drs.specimen_num between 76001 and 80000
order by drs.specimen_num;
		 	 
*/
	
	private static DataSource dataSource;

    static {
        try {
           // dataSource = (DataSource) new InitialContext().lookup( "java:/comp/env/jdbc/postgres" );
        	dataSource = (DataSource) new InitialContext().lookup( "java:jboss/datasources/earthchemDBDev" );
        }
        catch (NamingException e) { 
        	 System.err.println(e);
        }
        
        materialMap.put("WR", 1); 
        materialMap.put("GL",2);
        materialMap.put("ROCK",4);
        materialMap.put("MIN",8);
        
        variableTypeMap.put("A",1);
    	variableTypeMap.put("MAJ",2);
    	variableTypeMap.put("TE",4);
    	variableTypeMap.put("REE",8);
    	variableTypeMap.put("IR",16);
    	variableTypeMap.put("IS",32);
    	variableTypeMap.put("NGAS",64);
    	variableTypeMap.put("VO",128);
    	variableTypeMap.put("US",256);
    	variableTypeMap.put("EM ",512);
    	
    	rockclassMap.put("IPF",1);
    	rockclassMap.put("IPM",2);
    	rockclassMap.put("IPU",4);
    	rockclassMap.put("IVC",8);
    	rockclassMap.put("IVF",16);
    	rockclassMap.put("IVI",32);
    	rockclassMap.put("IVM",64);
    	rockclassMap.put("MET",128);
    	rockclassMap.put("SDUN",256);
    	rockclassMap.put("VEI",512);
    }
    
    public static StringTable item_codeA_new( ) {    	
    	 String[] heads = {"/*ROWNUM","ITEM_CODE","ITEM_TYPE_CODE","UNIT","DISPLAY_ORDER8*/"};
    	 StringTable table = new StringTable();
    	 table.setHeads(heads);
    	 String q = "select distinct mdrs.variable_code as item_code, vt.variable_type_code as item_type_code, mdrs.unit,mdrs.variable_order "+
    			" from mv_dataset_result_summary mdrs, variable v, variable_type vt "+
    			" where mdrs.variable_num=v.variable_num and v.variable_type_num=vt.variable_type_num";
    	 table.setData(listWithRowNum(q));
    	 return table;
    }
  
      
    public static StringTable stations_new( ) {    	
	   	 String[] heads = {"station num","station id","expedition number","location number","total samples","sample number list",
	   	 		 "sample technique number","material_nums","item_types","ALTERATION","rocktype"};  	 
	  
	     StringTable table = new StringTable();
	  	 table.setHeads(heads);
	 	 String q = "select st.station_num, st.station_code, st.expedition_num, st.station_num, sc.ct, sc.specimen_nums, stc.method_num, mat.material_codes, vt.variable_types, 0, rt.type_arr "+
	 			" from mv_station_summary st "+
	 			" left join (select station_num, count(*) ct, array_to_string(array_agg(specimen_num),',') specimen_nums from mv_specimen_summary group by station_num) sc on st.station_num= sc.station_num "+
	 			" left join (select distinct ss.station_num, m.method_num from mv_specimen_summary ss, method m where m.method_code = ss.sampling_technique_code) stc on stc.station_num= st.station_num "+
	 			" left join (select station_num, array_to_string(array_agg(distinct material_code),',') material_codes from mv_dataset_result_summary where material_code in ('ROCK','WR','GL','MIN') group by station_num ) mat on mat.station_num= st.station_num "+ 			 			 			
	 			" left join (select station_num, array_to_string(array_agg(distinct vt.variable_type_code),',') variable_types from mv_dataset_result_summary drs,  variable v, variable_type vt " + 
	 				" where v.variable_num = drs.variable_num and v.variable_type_num = vt.variable_type_num group by station_num) vt on st.station_num= vt.station_num "+
	 	 		" left join (select distinct ss.station_num, array_to_string(array_agg(distinct tcp.taxonomic_classifier_name),',') type_arr " + 
	 			 	" from mv_specimen_summary ss, sampling_feature_taxonomic_classifier ftc, taxonomic_classifier tc,  taxonomic_classifier tcp " + 
	 			 	" where ss.specimen_num = ftc.sampling_feature_num and ftc.taxonomic_classifier_num = tc.taxonomic_classifier_num " + 
	 			 	" and tc.taxonomic_classifier_type_cv = 'Rock Class' and tc.parent_taxonomic_classifier_num = tcp.taxonomic_classifier_num group by ss.station_num) rt on rt.station_num = st.station_num "+
	 				" order by station_num ";
	 
	 	 ArrayList<Object[]> list = (ArrayList<Object[]>)list(q);
	 	 System.out.println("bc-size "+list.size());
	 	 ArrayList<Object[]> list2 = new ArrayList<Object[]>();
	 	 int i = 0;
	 	 for(Object [] a: list) {
	 		 Object[] a2 = new Object[a.length];
	// 		 System.out.println("bc-m "+a[7]+";"+a[8]+";"+a[10]);
	 		 for(int j =0; j < a.length; j++) a2[j]=a[j];
	 		if(a[7]!=null) {
	 			String[] va = (""+a[7]).split(",");
	 			int vv=0;
	 			for(String s: va) {
	 				if(materialMap.containsKey(s)) {
	 					vv += materialMap.get(s);
	 				//	System.out.println("bc-m "+vv);
	 				}
	 			}
	 			a2[7]=""+vv;
	 		}
	 		 
	 		if(a[8]!=null) {
	 			String[] va = (""+a[8]).split(",");
	 			int vv=0;
	 			for(String s: va) {
	 				if(variableTypeMap.containsKey(s)) {
	 					vv += variableTypeMap.get(s);
	 				//	System.out.println("bc-v2 "+vv);
	 				}
	 			}
	 			a2[8]=""+vv;
	 		}
	 		
	 		if(a[10]!=null) {
	 			String[] va = (""+a[10]).split(",");
	 			int vv=0;
	 			for(String s: va) {
	 				if(rockclassMap.containsKey(s)) {
	 					vv += rockclassMap.get(s);
	 				//	System.out.println("bc-v2 "+vv);
	 				}
	 			}
	 			a2[10]=""+vv;
	 		}
	 		
	 		
	 		list2.add(a2);
	 		list=null;
	 	 }
	 	 table.setData(list2);
	  	 return table;
    }

    public static StringTable expeditions_new( ) {  
	    String[] heads = {"EXPEDITION_NUM","EXP_YEAR_FROM","INSTITUTION_NUM","EXPEDITION_CODE","CHIEFS_NUM"};  	 
	    StringTable table = new StringTable();
	   	 table.setHeads(heads);
	  	 String q = " select action_num as expedition_num, date_part('year', begin_date_time)::text as exp_year_from, organization_num as institution_num, "+
	  			" action_name as expedition_code, 0 as chiefs_num "+
	  			" from action where action_type_num = any (array[3,11,12,25]) order by action_num asc";
	  	 table.setData((ArrayList<Object[]>)list(q));
	   	 return table;
   }
    
    public static StringTable pdb_dataC_new( ) {    	
      	 String[] heads = {"specimen_num","station_num","specimen_code","rockclass","material_num","citation_num","DATA_QUALITY","variable_code","variable_type_code","unit","value_meas","STDEV"};    	 
      	 StringTable table = new StringTable();
      	 table.setHeads(heads);
    	 String q = "select distinct drs.specimen_num, drs.station_num, drs.specimen_code, rt.taxonomic_classifier_name, mat.material_num,  drs.citation_num, 0 as DATA_QUALITY, "+
    			 " v.variable_code, vt.variable_type_code, drs.unit, drs.value_meas, 'null' as STDEV "+
    			 " from mv_dataset_result_summary drs  "+
    			 " join variable v on v.variable_num = drs.variable_num "+ 
    			 " join variable_type vt on  v.variable_type_num = vt.variable_type_num "+
    			 " join material mat on mat.material_code = drs.material_code and mat.material_num in (3,7,8) "+ 
    			 " left join (select distinct ss.specimen_num, tcp.taxonomic_classifier_name "+
    			 	"  from mv_specimen_summary ss, sampling_feature_taxonomic_classifier ftc, taxonomic_classifier tc,  taxonomic_classifier tcp "+
    				"  where ss.specimen_num = ftc.sampling_feature_num and ftc.taxonomic_classifier_num = tc.taxonomic_classifier_num  "+
    			 	"  and tc.taxonomic_classifier_type_cv = 'Rock Class' and tc.parent_taxonomic_classifier_num = tcp.taxonomic_classifier_num ) rt on rt.specimen_num = drs.specimen_num "+
    			" where  drs.specimen_num < 50000 "+
    			 	" order by drs.specimen_num";
     	 table.setData((ArrayList<Object[]>) list(q));
      	 return table;
      }
    
    public static StringTable locations_new( ) {    	
     	 String[] heads = {"LOCATION_NUM","LONGITUDE","LATITUDE","ELEVATION_MIN","ELEVATION_MAX"};    	 
     	 StringTable table = new StringTable();
     	 table.setHeads(heads);
    	 String q = "select distinct s.station_num, split_part(split_part(s.geometry_text,'(',2), ' ', 1) \"LONGITUDE\", split_part( split_part(split_part(split_part(s.geometry_text,'(',2), ' ', 2), ')', 1), ',',1) \"LATITUDE\", s.elevation_min, s.elevation_max from mv_station_summary s order by s.station_num";     	
    	 table.setData((ArrayList<Object[]>) list(q));
     	 return table;
     }
    
    public static StringTable sampleDownload(String materialCode, String variableType, String condition) {	 
		 String[] heads = {"SAMPLE ID","IGSN","SOURCE","DOI","TITLE","JOURNAL","AUTHOR","EXPEDITION ID","LATITUDE","LONGITUDE","ELEVATION_MIN","ELEVATION_MAX","MIN AGE","AGE","MAX AGE","METHOD","SAMPLE TYPE","ROCK NAME"};
		
		 String select = "select d.specimen_code \"SAMPLE ID\", m.igsn \"IGSN\", 'EARTHCHEMDB' \"SOURCE\", c.doi \"DOI\", c.title \"TITLE\",  c.journal \"JOURNAL\", c.authors \"AUTHOR\", d.expedition_code \"EXPEDITION ID\", " + 
				"split_part( split_part(split_part(split_part(m.geometry_text,'(',2), ' ', 2), ')', 1), ',',1) \"LATITUDE\", split_part(   split_part(m.geometry_text,'(',2), ' ', 1) \"LONGITUDE\",  m.elevation_min, m.elevation_max, " + 
				" split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',3) \"MIN AGE\", split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',2) \"AGE\", " + 
				"split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',4) \"MAX AGE\", d.method_code \"METHOD\", d.material_name \"SAMPLE TYPE\",  " + 
				"split_part(array_to_string(m.taxon,','),'|',2) \"ROCK NAME\", d.variable_code \"VARIABLE\", d.value_meas \"VALUE\", d.specimen_num " ;
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
    
    
    public static ArrayList<Object[]> listWithRowNum(String query) {
    	ArrayList<Object[]> records=new ArrayList<Object[]>();
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		con = dataSource.getConnection();
    		stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            int cols = rs.getMetaData().getColumnCount();
            int k = 0;
            while(rs.next()){
            	 Object[] arr = new Object[cols+1]; 
            	 arr[0] = ++k;
            	 for(int i=0; i<cols; i++){ 
            		 arr[i+1] = rs.getObject(i+1); 
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
    
  /* 
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
*/	 
	 
}
