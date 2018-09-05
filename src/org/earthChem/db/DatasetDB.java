package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import org.earthChem.db.postgresql.hbm.Dataset;
import org.earthChem.db.postgresql.hbm.Dataset;
import org.earthChem.db.postgresql.hbm.Method;


public class DatasetDB {
	
	
	public static List<Dataset> getDatasetList(Integer citationNum) {
		List<Dataset> list = new ArrayList<Dataset>();
		 String	q = "select d.dataset_num, t.dataset_type_num, dataset_code, dataset_title, dataset_abstract, t.dataset_type_code "+
				 " from dataset d, citation_dataset cd , dataset_type t where d.dataset_type_num =t.dataset_type_num "+
				 " and cd.dataset_num = d.dataset_num and citation_num="+citationNum+" order by dataset_code";

		 List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Dataset e = new Dataset();
			e.setNum((Integer)arr[0]);
			e.setCitationNum(citationNum);
			e.setTypeNum((Integer)arr[1]);
			e.setCode(""+arr[2]);
			e.setTitle(""+arr[3]);
			e.setAbstr(""+arr[4]);		
			e.setType(""+arr[5]);	
			
			list.add(e);
		}
		return list;
	}
	
	public static String save(Dataset d) {
		String error = null;
		Integer num=d.getNum();
		Integer citationNum=d.getCitationNum();
	
		Integer typeNum = d.getTypeNum();
		String code=DBUtil.StringValue(d.getCode());
		String title=DBUtil.StringValue(d.getTitle());
		String abstr = DBUtil.StringValue(d.getAbstr());	
		
		if(num == null){
			num = DBUtil.getNumber("select nextval('dataset_dataset_num_seq')");
		/*	if(typeNum==2 && citationNum == null)  {
				citationNum = DBUtil.getNumber("select max(citation_num+1) from citation");
				String q = "INSERT INTO citation (citation_num, title, publication_year) values ("+citationNum+","+title+","+d.getYear()+")";
				error = DBUtil.update(q);
				if(error != null) return error;
			}
	     */
	        code = "'"+citationNum+"-"+d.getCode()+"'";
			String q = "INSERT INTO dataset (dataset_num, dataset_code, dataset_type_num, dataset_title, dataset_abstract) "+
					" VALUES ("+num+","+code+","+typeNum+","+title+","+abstr+")";
			error = DBUtil.update(q);
			
		
			if(error != null) return error;
		}	
		else {
			String q ="UPDATE dataset SET dataset_type_num = "+typeNum+
					", dataset_code = "+code+
					", dataset_title = "+title+
					", dataset_abstract = "+abstr+
					" WHERE dataset_num="+num;
			error = DBUtil.update(q);
			if(error != null) return error;
		}
		
		String q = "select citation_dataset_num from citation_dataset where citation_num =" +citationNum+ " and dataset_num = "+num;
		if(citationNum !=null && DBUtil.getNumber(q)==null) {
			q ="INSERT INTO citation_dataset (citation_num, dataset_num, relationship_type_num) "+
					" VALUES ("+citationNum+","+num+",6 )";
			error = DBUtil.update(q);
		}
		return error;
	}


}
