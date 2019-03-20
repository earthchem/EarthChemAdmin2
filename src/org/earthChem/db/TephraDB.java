package org.earthChem.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.earthChem.db.postgresql.hbm.FeatureOfInterest;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.RelatedFeature;
import org.earthChem.db.postgresql.hbm.Sample;
import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.db.postgresql.hbm.Station;
import org.earthChem.db.postgresql.hbm.TaxonomicClassifier;
import org.earthChem.presentation.jsf.SampleBean.ColumnModel;

public class TephraDB {
	
//------ relationship -------------------	
	public static SelectItem[] getRelationList(Integer type, String search) {
		String	q = "select s.sampling_feature_num, s.sampling_feature_code "+
				" from sampling_feature s "+
				" where s.sampling_feature_type_num = "+type+" and s.sampling_feature_code like '%"+search+"%' "+
				" order by s.sampling_feature_code";

			return DBUtil.getSelectItems(q);
	}
	
	public static String saveRelations(Integer sfNum, Integer sfType, List<Integer> list) {
		Integer relationType = (sfType == 3)? 22:14;
		List<String> queries = new ArrayList<String>();
		for(Integer n: list) {
			String	q = "insert into related_feature ( sampling_feature_num, relationship_type_num, related_sampling_feature_num) values " + 
				" ("+sfNum+","+relationType+","+n+")";
			queries.add(q);			
		}
		return DBUtil.updateList(queries);
	}
	
	
	public static List<RelatedFeature> getRelations(Integer num) {
		
		List<RelatedFeature> list = new ArrayList<RelatedFeature>();				
		String	q = "  select r.related_feature_num, sp.sampling_feature_code, t.relationship_type_name" + 
				" from related_feature r, sampling_feature sp, relationship_type t  where r.sampling_feature_num = " + num+
				" and t.relationship_type_num = r.relationship_type_num and sp.sampling_feature_num = r.related_sampling_feature_num "+
				" order by  r.relationship_type_num ";		
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			RelatedFeature e = new RelatedFeature();
			e.setRelatedFeatureNum((Integer)arr[0]);
			e.setRelatedSamplingFeatureCode(""+arr[1]);
			e.setRelationshipTypeCode(""+arr[2]);
			list.add(e);
		}
		return list;
	}
	
	public static String deleteRelation(Integer relatedFeatureNum) {
		return DBUtil.update("delete from related_feature where related_feature_num="+relatedFeatureNum);
	}
	
	//------------------------ end of relationship 
	
public static List<Sample> getSampleList(String search) {
		
		List<Sample> list = new ArrayList<Sample>();				
		String	q = "select s.sampling_feature_num, s.sampling_feature_code, ei.sampling_feature_external_id, p.material_num "+
		 " from sampling_feature s 		"+					
		 " left join sampling_feature_external_identifier ei on ei.sample_feature_num = s.sampling_feature_num and ei.external_identifier_system_num = 2 "+
		 " left join specimen p on p.sampling_feature_num = s.sampling_feature_num "+
		 " where s.sampling_feature_type_num = 1 and s.sampling_feature_code like upper('%"+search+"%') order by  s.sampling_feature_code";
		
		 List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Sample e = new Sample();
			e.setSampleNum((Integer)arr[0]);
			e.setSamplingFeatureCode(""+arr[1]);
			if(arr[2] != null) e.setIgsn(""+arr[2]);
			e.setMaterialNum((Integer)arr[3]);
			list.add(e);
		}
		return list;
	}

public static List<Sample> getSampleListByAlias(String search) {
	
	List<Sample> list = new ArrayList<Sample>();				
	String	q = "select s.sampling_feature_num, s.sampling_feature_code, ei.sampling_feature_external_id, p.material_num "+
	 " from sampling_feature s 		"+					
	 " left join sampling_feature_external_identifier ei on ei.sample_feature_num = s.sampling_feature_num and ei.external_identifier_system_num = 2 "+
	 " left join specimen p on p.sampling_feature_num = s.sampling_feature_num "+
	 " join sampling_feature_annotation sa on sa.sampling_feature_num = s.sampling_feature_num " + 
	 " join annotation a on a.annotation_num = sa.annotation_num "+
	 " where s.sampling_feature_type_num = 1 and a.annotation_text  like upper('%"+search+"%') order by  s.sampling_feature_code";
	
	 List<Object[]> olist = DBUtil.getECList(q);
	for(Object[] arr: olist) {
		Sample e = new Sample();
		e.setSampleNum((Integer)arr[0]);
		e.setSamplingFeatureCode(""+arr[1]);
		if(arr[2] != null) e.setIgsn(""+arr[2]);
		e.setMaterialNum((Integer)arr[3]);
		list.add(e);
	}
	return list;
}



public static Sample getSample(String code) {
		String	q = "select s.sampling_feature_num, s.sampling_feature_name, s.sampling_feature_description, ei.sampling_feature_external_id "+
				" from sampling_feature s "+				
				" left join sampling_feature_external_identifier ei on ei.sample_feature_num = s.sampling_feature_num and ei.external_identifier_system_num = 2 "+
				" where s.sampling_feature_type_num = 1 and s.sampling_feature_code = '"+code+"'";
		 List<Object[]> olist = DBUtil.getECList(q);
			Sample e = null;
		for(Object[] arr: olist) {
			e = new Sample();
			e.setSampleNum((Integer)arr[0]);
			if(arr[1] != null) e.setSamplingFeatureName(""+arr[1]);
			if(arr[2] != null) e.setSamplingFeatureDescription(""+arr[2]);
			if(arr[3] != null) e.setIgsn(""+arr[3]);
		}
		return e;
	}
	
	public static void getAnnotation(Sample e) {
		 String	q = "select distinct a.annotation_type_num, a.annotation_text from sampling_feature_annotation s, annotation a "+
				 " where a.annotation_num = s.annotation_num and a.data_source_num <> 0 "+
				 " and a.data_source_num = "+e.getCitationNum()+" and s.sampling_feature_num = "+e.getSampleNum();
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {			
			int num = (Integer) arr[0];
			if(num == Sample.Alias) e.setAliasText(""+arr[1]);
			else if (num == Sample.Alteration) e.setAlterationText(""+arr[1]);			
			else if (num == Sample.AlterationType) e.setAlterationTypeText(""+arr[1]);
			else if (num == Sample.GeologicAge) e.setGeologicAgeText(""+arr[1]);
			else if (num == Sample.RockclassDetail) e.setRockclassDetailText(""+arr[1]);
			else if (num == Sample.Rocktexture) e.setRocktextureText(""+arr[1]);
			else if (num == Sample.SampleComment) e.setSampleCommentText(""+arr[1]);
		}
	}
	
	public static List<TaxonomicClassifier> getTaxonomicClassifier(Integer sampleNum, Integer materialNum) {		
		List<TaxonomicClassifier> list = new ArrayList<TaxonomicClassifier>();
		String q = null;
		if(materialNum == 6) {
			q = "select s.bridge_num, s.citation_num, t.taxonomic_classifier_name from sampling_feature_taxonomic_classifier s, taxonomic_classifier t " + 
				" where s.taxonomic_classifier_num = t.taxonomic_classifier_num and s.sampling_feature_num = "+sampleNum+" order by citation_num ";
		} else {
			q =	"select s.bridge_num, s.citation_num, concat(t.taxonomic_classifier_name,' (',tp.taxonomic_classifier_name,')') "+
				" from sampling_feature_taxonomic_classifier s, taxonomic_classifier t, taxonomic_classifier tp "+
				" where s.taxonomic_classifier_num = t.taxonomic_classifier_num and tp.taxonomic_classifier_num = t.parent_taxonomic_classifier_num "+
				" and s.sampling_feature_num = "+sampleNum+" order by citation_num";
		}
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {	
			TaxonomicClassifier t = new TaxonomicClassifier();
			t.setBridgeNum((Integer)arr[0]);
			t.setCitationNum((Integer)arr[1]);
			t.setClassifierName(""+arr[2]);
			list.add(t);
		}
		
		return list;
	}

	public static String deleteSamplingTaxonomicClassifier(Integer bridgeNum) {		
		String q = "delete from sampling_feature_taxonomic_classifier where bridge_num="+bridgeNum;		
		return DBUtil.update(q);
	}
	
	public static String addTaxonomicClassifier(TaxonomicClassifier t, Integer sampleNum) {		
		String error = null;
		String q = "INSERT INTO sampling_feature_taxonomic_classifier (sampling_feature_num, taxonomic_classifier_num, citation_num) "+
			" VALUES ("+sampleNum+","+t.getClassifierNum()+","+t.getCitationNum()+")";
		error = DBUtil.update(q);
		if(error != null) return error; 
		return DBUtil.update("");
	}
	
	public static String update(Sample e) {
		String error = null;
		Integer num = e.getSampleNum(); 
		String code = DBUtil.StringValue(e.getSamplingFeatureCode());
		String igsn = DBUtil.StringValue(e.getIgsn());
		if(num == null) {
			num = DBUtil.getNumber("select nextVal('sampling_feature_sampling_feature_num_seq')");
			String q = "insert into sampling_feature (sampling_feature_num, sampling_feature_type_num, sampling_feature_code) VALUES ("+num+",1,"+code+")";
			error = DBUtil.update(q);
			if(error != null) return error;
			
			q = "INSERT INTO specimen values ("+num+","+e.getMaterialNum()+")";
			error = DBUtil.update(q);		
			if(error != null) return error; 
			if(igsn != null) error = insertIGSN(igsn,num) ;
		} 	
		else { 	
			String q = "UPDATE sampling_feature set sampling_feature_code ="+code+" where sampling_feature_num="+num;	
			error = DBUtil.update(q);
			if(error != null) return error; 	
			
			if(e.getMaterialNum() != null) 
				q = "UPDATE specimen set material_num ="+e.getMaterialNum()+" where sampling_feature_num="+num;
		 	error = DBUtil.update(q);
		 	
			if(error != null) return error; 
			if(igsn != null) {
				Integer igsnBridgeNum = getIGSN(num);
				if(igsnBridgeNum == null) error = insertIGSN(igsn,num) ;
				else {
					q = "UPDATE sampling_feature_external_identifier SET sampling_feature_external_id =" +igsn+
							" WHERE external_identifier_system_num =2 and sample_feature_num ="+num;
					error = DBUtil.update(q);
					if(error != null) return error; 		
				}					
			}
		}
		return error; 		
	}

	/*
	private static Integer getTaxonomicClassifierNum(String name, Integer parentNum) {
		String q ="select c.taxonomic_classifier_num from taxonomic_classifier c where c.taxonomic_classifier_type_cv = 'Rock Class' "+
				" and c.parent_taxonomic_classifier_num = "+parentNum+" and c.taxonomic_classifier_name ="+name;
		Integer tcNum = DBUtil.getNumber(q);

		if(tcNum == null) {
			tcNum = DBUtil.getNumber("select nextVal('taxonomic_classifier_taxonomic_classifier_num_seq')");
			q ="INSERT INTO taxonomic_classifier (taxonomic_classifier_num, taxonomic_classifier_type_cv, taxonomic_classifier_name,taxonomic_classifier_common_name, parent_taxonomic_classifier_num) VALUES "+
			" ("+tcNum+",'Rock Class',"+name+","+name+","+parentNum+")";
			DBUtil.update(q);
		}
		return tcNum;
	}
	*/
	private static Integer getIGSN(Integer sampleNum) {
		return DBUtil.getNumber("select bridge_num from sampling_feature_external_identifier where external_identifier_system_num =  2 and sample_feature_num ="+sampleNum);
	}
	
	private static String insertIGSN(String igsn, Integer num) {
		String q = "INSERT INTO sampling_feature_external_identifier "+
				" (bridge_num, sample_feature_num, sampling_feature_external_id, sampling_feature_external_identifier_uri, external_identifier_system_num) "+
				" VALUES (nextVal('sampling_feature_external_identifier_bridge_num_seq'),"+num+","+igsn+",'http://data.geosamples.org/sample/igsn/',2)";
		return DBUtil.update(q);
	}
	
	
	public static List<String> getAnnotationHeads(Integer sampleNum) {
		String q = "select distinct t.annotation_type_name "+
		" from sampling_feature s,  sampling_feature_annotation f, annotation a, annotation_type t, annotation_type_group g "+
		" where s.sampling_feature_num = f.sampling_feature_num and a.annotation_num = f.annotation_num and a.annotation_type_num = t.annotation_type_num "+
		" and g.annotation_type_num = a.annotation_type_num and g.annotation_group_num = 2 "+
		" and s.sampling_feature_num = "+sampleNum+" order by annotation_type_name ";
		List<Object> ol = DBUtil.getObjectList(q);
		List<String> list = null;
		if(ol != null) {
			list = new ArrayList<String>();
			list.add("Citation Num");
			
			for(Object s: ol) {
				list.add(""+s);
			}
		}
		return list;
	} 
	
	public static List<String[]> getAnnotationList(List<String> heads, Integer sampleNum) {
		String q = "select a.data_source_num, t.annotation_type_name, a.annotation_text   "+
		" from sampling_feature s,  sampling_feature_annotation f, annotation a, annotation_type t, annotation_type_group g "+
		" where s.sampling_feature_num = f.sampling_feature_num and a.annotation_num = f.annotation_num and a.annotation_type_num = t.annotation_type_num "+
		" and g.annotation_type_num = a.annotation_type_num and g.annotation_group_num = 2 "+
		" and s.sampling_feature_num = "+sampleNum+" order by a.data_source_num, annotation_type_name ";

		List<Object[]> olist = DBUtil.getECList(q);
		List<String[]> slist = new ArrayList<String[]>();
		String prev = null;
		String [] s = null;
		for(Object[] arr: olist) {
			String curr = ""+ arr[0];
			if(!curr.equals(prev)) { 
				if(prev != null) {
					slist.add(s);
				}

				s = new String[heads.size()];
				s[0]= curr;
			}
		
			int i = 0;
			for(String h: heads) {
				if(h.equals(""+arr[1])) {
					s[i] = ""+arr[2];
					break;
				}
				i++;
			}
			prev = s[0];
		}

		slist.add(s);
		return slist;
	} 

	

}
