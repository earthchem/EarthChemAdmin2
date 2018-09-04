package org.earthChem.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.db.postgresql.hbm.Station;
import org.earthChem.db.postgresql.hbm.TaxonomicClassifier;
import org.earthChem.presentation.jsf.SampleBean.ColumnModel;
import org.earthChem.db.postgresql.hbm.FeatureOfInterest;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.Sample;

public class SampleDB {
	
public static List<Sample> getSampleList(String search) {
		
		List<Sample> list = new ArrayList<Sample>();
		String	q = "select sp.sampling_feature_num, sp.sampling_feature_code, s.sampling_feature_num, s.sampling_feature_code, ei.sampling_feature_external_id "+
				" from sampling_feature s "+
				" left join related_feature r on  s.sampling_feature_num = r.sampling_feature_num  "+
				" left join sampling_feature sp on sp.sampling_feature_num =  r.related_sampling_feature_num "+							
				" left join sampling_feature_external_identifier ei on ei.sample_feature_num = s.sampling_feature_num and ei.external_identifier_system_num = 2 "+
				" where s.sampling_feature_type_num = 1 and sp.sampling_feature_type_num = 3 and s.sampling_feature_code like '%"+search+"%' "+
				" order by  s.sampling_feature_code";
		 List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Sample e = new Sample();
			if(arr[0] != null) e.setStationNum((Integer)arr[0]);
			if(arr[1] != null) e.setStationName(""+arr[1]);
			e.setSampleNum((Integer)arr[2]);
			e.setSamplingFeatureCode(""+arr[3]);
			if(arr[4] != null) e.setIgsn(""+arr[4]);
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
	
	public static List<TaxonomicClassifier> getTaxonomicClassifier(Integer sampleNum) {		
		List<TaxonomicClassifier> list = new ArrayList<TaxonomicClassifier>();
		String q = "select s.bridge_num, s.citation_num, t.taxonomic_classifier_name, tp.taxonomic_classifier_name "+
				" from sampling_feature_taxonomic_classifier s, taxonomic_classifier t, taxonomic_classifier tp "+
				" where s.taxonomic_classifier_num = t.taxonomic_classifier_num and tp.taxonomic_classifier_num = t.parent_taxonomic_classifier_num "+
				" and s.sampling_feature_num = "+sampleNum+" order by citation_num";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {	
			TaxonomicClassifier t = new TaxonomicClassifier();
			t.setBridgeNum((Integer)arr[0]);
			t.setCitationNum((Integer)arr[1]);
			t.setClassifierName(""+arr[2]);
			t.setParentName(""+arr[3]);
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
		String q ="delete from sampling_feature_taxonomic_classifier s where s.sampling_feature_num = "+sampleNum+" and s.citation_num ="+t.getCitationNum();
		error = DBUtil.update(q);
		if(error != null) return error; 
		Integer	tcNum = getTaxonomicClassifierNum(DBUtil.StringValue(t.getClassifierName()), t.getParentNum());		
		q = "INSERT INTO sampling_feature_taxonomic_classifier "+
			" (bridge_num, sampling_feature_num, taxonomic_classifier_num, citation_num) "+
			" VALUES (nextVal('sampling_feature_taxonomic_classifier_bridge_num_seq'),"+sampleNum+","+tcNum+","+t.getCitationNum()+")";
		error = DBUtil.update(q);
		if(error != null) return error; 
		return DBUtil.update("");
	}
	
	public static String update(Sample e) {
		String error = null;
		Integer num = e.getSampleNum(); 
		String code = DBUtil.StringValue(e.getSamplingFeatureCode());
		String name = DBUtil.StringValue(e.getSamplingFeatureName());
		String desc = DBUtil.StringValue(e.getSamplingFeatureDescription());
		String igsn = DBUtil.StringValue(e.getIgsn());
		Integer stationNum = e.getStationNum();
		String aliasText = DBUtil.StringValue(e.getAliasText());
		String sampleCommentText = DBUtil.StringValue(e.getSampleCommentText());
		String rockclassDetailText = DBUtil.StringValue(e.getRockclassDetailText());
		String rocktextureText = DBUtil.StringValue(e.getRocktextureText());
		String geologicAgeText = DBUtil.StringValue(e.getGeologicAgeText());
		String alterationText = DBUtil.StringValue(e.getAlterationText());
		String alterationTypeText = DBUtil.StringValue(e.getAlterationTypeText());
		if(num == null) {
			num = DBUtil.getNumber("select nextVal('sampling_feature_sampling_feature_num_seq')");
			String q = "insert into sampling_feature (sampling_feature_num, sampling_feature_type_num, sampling_feature_code, sampling_feature_name, sampling_feature_description) "+
					" VALUES ("+num+",1,"+code+","+name+","+desc+")";
			error = DBUtil.update(q);
			if(error != null) return error;
			
			q = "INSERT INTO specimen values ("+num+",7)";
			error = DBUtil.update(q);		
			if(error != null) return error; 
			
			
			if(stationNum != null) {
				q = "insert into related_feature (related_feature_num, sampling_feature_num, relationship_type_num, related_sampling_feature_num) values "+
						" (nextVal('related_feature_related_feature_num_seq'),"+num+",22,"+stationNum+")";
					error = DBUtil.update(q);
			}

			if(igsn != null) error = insertIGSN(igsn,num) ;
		} 	
		else { 	
			String q = "UPDATE sampling_feature set sampling_feature_code ="+code+" where sampling_feature_num="+num;	
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
			
			if(stationNum != null){
				q = null;
				Integer relatedNum = DBUtil.getNumber("SELECT related_feature_num FROM related_feature where  relationship_type_num = 22 and sampling_feature_num ="+num);
				if(relatedNum==null) {
					q = "insert into related_feature (related_feature_num, sampling_feature_num, relationship_type_num, related_sampling_feature_num) values "+
							" (nextVal('related_feature_related_feature_num_seq'),"+num+",22,"+stationNum+")";
					error = DBUtil.update(q);
				}
				else {
					q = "UPDATE related_feature SET related_sampling_feature_num = "+stationNum+" WHERE related_feature_num ="+num;
				}
				error = DBUtil.update(q);				
			}
		}
		return error; 		
	}

	
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

	
/*	
 public static String update(Sample e) {
		String error = null;
		Integer num = e.getSampleNum(); 
		String code = DBUtil.StringValue(e.getSamplingFeatureCode());
		Integer citationNum = e.getCitationNum();		
		String name = DBUtil.StringValue(e.getSamplingFeatureName());
		String desc = DBUtil.StringValue(e.getSamplingFeatureDescription());
		String tcName= DBUtil.StringValue(e.getTcName());
		Integer tcParentNum = e.getTcParentNum();
		Integer tcNum = null;
		if(tcName != null) {
			tcNum = getTaxonomicClassifierNum(tcName, tcParentNum);
		}
		String igsn = DBUtil.StringValue(e.getIgsn());
		Integer igsnBridgeNum = null;
		if(igsn != null) {
			igsnBridgeNum = getIGSN(num);
		}
		String aliasText = DBUtil.StringValue(e.getAliasText());
		String sampleCommentText = DBUtil.StringValue(e.getSampleCommentText());
		String rockclassDetailText = DBUtil.StringValue(e.getRockclassDetailText());
		String rocktextureText = DBUtil.StringValue(e.getRocktextureText());
		String geologicAgeText = DBUtil.StringValue(e.getGeologicAgeText());
		String alterationText = DBUtil.StringValue(e.getAlterationText());
		String alterationTypeText = DBUtil.StringValue(e.getAlterationTypeText());
		if(num == null) {
			num = DBUtil.getNumber("select nextVal('sampling_feature_sampling_feature_num_seq')");
			String q = "insert into sampling_feature (sampling_feature_num, sampling_feature_type_num, sampling_feature_code, sampling_feature_name, sampling_feature_description) "+
					" VALUES ("+num+",1,"+code+","+name+","+desc+")";
			error = DBUtil.update(q);
			if(error != null) return error;
			q = "INSERT INTO specimen values ("+num+",7)";
			error = DBUtil.update(q);
			if(error != null) return error;
			q = "insert into related_feature (related_feature_num, sampling_feature_num, relationship_type_num, related_sampling_feature_num) values "+
				" (nextVal('related_feature_related_feature_num_seq'),"+num+",22,"+e.getStationNum()+")";
			error = DBUtil.update(q);
			if(error != null) return error; 
			if(tcNum != null) {
				q = "INSERT INTO sampling_feature_taxonomic_classifier "+
					" (bridge_num, sampling_feature_num, taxonomic_classifier_num, citation_num) "+
					" VALUES (nextVal('sampling_feature_taxonomic_classifier_bridge_num_seq'),"+num+","+tcNum+","+e.getCitationNum()+")";
				error = DBUtil.update(q);
			}
			if(error != null) return error; 
			if(igsn != null) {
				q = "INSERT INTO sampling_feature_external_identifier "+
					" (bridge_num, sample_feature_num, sampling_feature_external_id, sampling_feature_external_identifier_uri, external_identifier_system_num) "+
					" VALUES (nextVal('sampling_feature_external_identifier_bridge_num_seq'),"+num+","+igsn+",'http://data.geosamples.org/sample/igsn/',2)";
				error = DBUtil.update(q);
			}			
			if(error != null) return error; 
			if(aliasText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.Alias, citationNum, aliasText);
			if(alterationTypeText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.AlterationType, citationNum, alterationTypeText);
			if(rocktextureText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.Rocktexture, citationNum, rocktextureText);
			if(sampleCommentText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.SampleComment, citationNum, sampleCommentText);
			if(rockclassDetailText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.RockclassDetail, citationNum, rockclassDetailText);
			if(alterationText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.Alteration, citationNum, alterationText);
			if(geologicAgeText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.GeologicAge, citationNum, geologicAgeText);			
		} 	
		else { 
			if(tcNum != null) {
				String q ="delete from sampling_feature_taxonomic_classifier s where s.sampling_feature_num = "+num+" and s.citation_num ="+citationNum;
				error = DBUtil.update(q);
				if(error != null) return error; 
				q = "INSERT INTO sampling_feature_taxonomic_classifier "+
					" (bridge_num, sampling_feature_num, taxonomic_classifier_num, citation_num) "+
					" VALUES (nextVal('sampling_feature_taxonomic_classifier_bridge_num_seq'),"+num+","+tcNum+","+e.getCitationNum()+")";
				error = DBUtil.update(q);
				if(error != null) return error; 
			}	
			error = DBUtil.update("delete from sampling_feature_annotation where sampling_feature_annotation_num in "+
			" (select sampling_feature_annotation_num from sampling_feature_annotation s, annotation a where s.annotation_num = a.annotation_num "+ 
			" and a.data_source_num = "+citationNum+" and s.sampling_feature_num = "+num+")");
			
			//	error = DBUtil.update("delete from sampling_feature_annotation where sampling_feature_num="+num+" and s.annotation_num = a.annotation_num and a.data_source_num ="+citationNum);
			if(error != null) return error; 
			if(aliasText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.Alias, citationNum, aliasText);
			if(alterationTypeText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.AlterationType, citationNum, alterationTypeText);
			if(rocktextureText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.Rocktexture, citationNum, rocktextureText);
			if(sampleCommentText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.SampleComment, citationNum, sampleCommentText);
			if(rockclassDetailText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.RockclassDetail, citationNum, rockclassDetailText);
			if(alterationText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.Alteration, citationNum, alterationText);
			if(geologicAgeText != null) AnnotationDB.addSamplingFeatureAnnotation(num, Sample.GeologicAge, citationNum, geologicAgeText);			
		}
		return error;
	}

 
  public static List<Sample> getSampleList(String search) {
		
		List<Sample> list = new ArrayList<Sample>();
		String	q = "select sp.sampling_feature_num, sp.sampling_feature_code, s.sampling_feature_num, s.sampling_feature_code, sa.data_source_num, s.sampling_feature_name, s.sampling_feature_description, "+
				" ei.sampling_feature_external_id, t.taxonomic_classifier_name, t.parent_taxonomic_classifier_num "+
				" from sampling_feature sp "+
				" join related_feature r on sp.sampling_feature_num =  r.related_sampling_feature_num "+
				" join sampling_feature s on s.sampling_feature_num = r.sampling_feature_num "+				
				" left join sampling_feature_external_identifier ei on ei.sample_feature_num = s.sampling_feature_num and ei.external_identifier_system_num = 2 "+
				" left join (select distinct a.data_source_num, s.sampling_feature_num from sampling_feature_annotation s, annotation a where a.annotation_num = s.annotation_num and a.data_source_num <>0) sa "+				
					" on s.sampling_feature_num = sa.sampling_feature_num "+	
				" left join sampling_feature_taxonomic_classifier f on s.sampling_feature_num = f.sampling_feature_num and f.citation_num = sa.data_source_num "+
				" left join taxonomic_classifier t on t.taxonomic_classifier_num = f.taxonomic_classifier_num "+
				" where s.sampling_feature_type_num = 1 and sp.sampling_feature_type_num = 3 and sp.sampling_feature_code = '"+search+"' "+
				" order by  sp.sampling_feature_code, sa.data_source_num";

		 List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Sample e = new Sample();
			e.setStationNum((Integer)arr[0]);
			e.setStationName(""+arr[1]);
			e.setSampleNum((Integer)arr[2]);
			e.setSamplingFeatureCode(""+arr[3]);
			if(arr[4] != null) e.setCitationNum((Integer)arr[4]);
			if(arr[5] != null) e.setSamplingFeatureName(""+arr[5]);
			if(arr[6] != null) e.setSamplingFeatureDescription(""+arr[6]);
			if(arr[7] != null) e.setIgsn(""+arr[7]);
			if(arr[8] != null) e.setTcName(""+arr[8]);
			if(arr[9] != null) e.setTcParentNum((Integer)arr[9]);
			e.setIndex(((Integer)arr[2]).intValue()*10000+((Integer)arr[4]).intValue());
			list.add(e);
		}
		return list;
	}
*/
}
