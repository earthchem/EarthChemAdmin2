package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

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
}
