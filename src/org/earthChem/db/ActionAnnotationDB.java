package org.earthChem.db;




public class ActionAnnotationDB {
	
	
	public static String save(Integer actionNum, Integer annotationTypeNum, String text) {
		String error = null;
		String q = "select annotation_num from annotation where annotation_type_num = "+annotationTypeNum+
				" and data_source_num = 0 and annotation_text = "+DBUtil.StringValue(text);
	
		Integer annotationNum = DBUtil.getNumber(q);

		if(annotationNum==null) {
			annotationNum = DBUtil.getNumber("select nextval('annotation_annotation_num_seq')");

			q = "INSERT INTO annotation (annotation_num, annotation_type_num, annotation_text, data_source_num, annotation_entered_time) "+
				" VALUES ("+annotationNum+","+annotationTypeNum+","+DBUtil.StringValue(text)+",0, now())";
			error=DBUtil.update(q);
		}

		if(error != null) return error;
		
		
		q = "select aa.action_annotation_num, a.annotation_num from action_annotation aa, annotation a "+
				" where a.annotation_type_num = "+annotationTypeNum+" and aa.annotation_num = a.annotation_num and aa.action_num ="+ actionNum;
		
		Integer actionAnnotationNum = DBUtil.getNumber(q);

		if (actionAnnotationNum==null){
			 actionAnnotationNum = DBUtil.getNumber("select nextval('action_annotation_action_annotation_num_seq')");
			 q = "INSERT INTO action_annotation VALUES ("+actionAnnotationNum+","+actionNum+","+annotationNum+")";
			 error= DBUtil.update(q);
		} else {
			q ="update action_annotation set annotation_num = "+annotationNum+" where action_annotation_num = "+actionAnnotationNum; 
			 error= DBUtil.update(q);
		}
		
		return error;
	}
}
