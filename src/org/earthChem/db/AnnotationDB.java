package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.earthChem.model.Annotation;
import org.earthChem.model.FeatureOfInterest;
import org.earthChem.model.Organization;
import org.earthChem.model.SamplingFeature;
import org.earthChem.model.Station;

public class AnnotationDB {
	
	public static String addSamplingFeatureAnnotation(Integer samplingFeatureNum, Integer typeNum, Integer sourceNum, String text) {
		String error = null;
		
		Integer annotationNum = DBUtil.getNumber("select annotation_num from annotation where annotation_type_num="+typeNum+" and annotation_text='"+text+"' and data_source_num ="+sourceNum);
		if(annotationNum==null) {		
			annotationNum=	DBUtil.getNumber("select nextVal('annotation_annotation_num_seq')");
			String q = "INSERT INTO annotation (annotation_num, annotation_type_num, annotation_text, data_source_num, annotation_entered_time) "+
					" VALUES ("+annotationNum+","+typeNum+",'"+text+"',"+sourceNum+",now())";
			error = DBUtil.update(q);
		}
		if(error == null) error = DBUtil.update("INSERT INTO sampling_feature_annotation (sampling_feature_annotation_num, sampling_feature_num, annotation_num) "+
				" VALUES (nextVal('sampling_feature_annotation_sampling_feature_annotation_num_seq'),"+samplingFeatureNum+"," +annotationNum+")");		
		return error;
	}
	
	
	public static String addTephraAnnotation(Integer samplingFeatureNum,  Integer sourceNum, List<Annotation> list ) {
		String error = null;
		String q = null;
		Integer annotationNum = null;
		for(Annotation a:list) {
			String t = a.getAnnotationText();
			if(t !=null && !"".equals(t)) {
				annotationNum=	DBUtil.getNumber("select nextVal('annotation_annotation_num_seq')");
				q = "INSERT INTO annotation (annotation_num, annotation_type_num, annotation_text, data_source_num, annotation_entered_time) "+
						" VALUES ("+annotationNum+","+a.getAnnotationTypeNum()+",'"+t+"',"+sourceNum+",now())";
				error = DBUtil.update(q);
			
				if(error == null) error = DBUtil.update("INSERT INTO sampling_feature_annotation (sampling_feature_annotation_num, sampling_feature_num, annotation_num) VALUES (nextVal('sampling_feature_annotation_sampling_feature_annotation_num_seq'),"+samplingFeatureNum+"," +annotationNum+")");		
		    }
		}
			return error;
	}
	
	public static String deleteSamplingFeatureAnnotation(Integer sfNum, Integer typeNum, Integer sourceNum) {
		String q = "delete from sampling_feature_annotation s where  s.sampling_feature_num = "+sfNum+
				" and s.annotation_num in (select annotation_num from annotation where data_source_num ="+sourceNum+" and annotation_type_num = "+typeNum+")";
		return DBUtil.update(q);
	}
	
	public static List<Annotation> getAnnotationList(Integer samplingFeatureNum, Integer annotationTypeNum) {
		List<Annotation> list = new ArrayList<Annotation>();
		 String	q = "select  fa.sampling_feature_annotation_num, a.annotation_text, a.data_source_num from sampling_feature_annotation fa, annotation a, annotation_type  t "+
				 " where a.annotation_num = fa.annotation_num and a.annotation_type_num = t.annotation_type_num "+
				 " and a.annotation_type_num = "+annotationTypeNum+" and fa.sampling_feature_num = "+samplingFeatureNum;
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Annotation a = new Annotation();
			a.setSfAnnotationNum((Integer)arr[0]);
			a.setAnnotationText(""+arr[1]);
			a.setDataSourceNum((Integer)arr[2]);
			list.add(a);
		}
		return list;
	}
	
	public static List<Annotation>  getTephraAnnotationView(Integer samplingFeatureNum) {
		List<Annotation> list = new ArrayList<Annotation>();
		 String	q = "select  fa.sampling_feature_annotation_num, a.data_source_num, t.annotation_type_name, a.annotation_text from sampling_feature_annotation fa, annotation a, annotation_type  t " + 
		 		"where a.annotation_num = fa.annotation_num and a.annotation_type_num = t.annotation_type_num and fa.sampling_feature_num = "+samplingFeatureNum;
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Annotation a = new Annotation();
			a.setSfAnnotationNum((Integer)arr[0]);
			a.setDataSourceNum((Integer)arr[1]);
			a.setAnnotationTypeName(""+arr[2]);
			a.setAnnotationText(""+arr[3]);	
			list.add(a);
		}
		return list;
	}

/*	
	public static List<Annotation> getAnnotationList(Integer samplingFeatureNum, Integer annotationTypeNum) {
		List<Annotation> list = new ArrayList<Annotation>();
		 String	q = "select  fa.sampling_feature_annotation_num, a.annotation_text, a.data_source_num from sampling_feature_annotation fa, annotation a, annotation_type  t "+
				 " where a.annotation_num = fa.annotation_num and a.annotation_type_num = t.annotation_type_num "+
				 " and a.annotation_type_num = "+annotationTypeNum+" and fa.sampling_feature_num = "+samplingFeatureNum;
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Annotation a = new Annotation();
			a.setSfAnnotationNum((Integer)arr[0]);
			a.setAnnotationText(""+arr[1]);
			a.setDataSourceNum((Integer)arr[2]);
			list.add(a);
		}
		return list;
	}
*/	
	public static String deleteTephraAnnotation(Integer sfAnnotationNum) {
		String q = "delete from sampling_feature_annotation s where  s.sampling_feature_annotation_num ="+ sfAnnotationNum;
		return DBUtil.update(q);
	}
	
	public static	List<Annotation> getTephraAnnotations() {
		List<Annotation> list = new ArrayList<Annotation>();
		List<Object[]> olist = DBUtil.getECList("select t.annotation_type_num, t.annotation_type_name from annotation_type t, annotation_type_group g where t.annotation_type_num = g.annotation_type_num "+
				" and g.curation_entity_num =2 order by t.annotation_type_name");
		for(Object[] arr: olist) list.add(new Annotation((Integer)arr[0], ""+arr[1]));
		return list;
	}

	
}
