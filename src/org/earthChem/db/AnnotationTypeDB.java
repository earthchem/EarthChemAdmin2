package org.earthChem.db;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.Annotation;
import org.earthChem.db.postgresql.hbm.AnnotationGroup;
import org.earthChem.db.postgresql.hbm.AnnotationType;
import org.earthChem.db.postgresql.hbm.FeatureOfInterest;
import org.earthChem.db.postgresql.hbm.Method;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.db.postgresql.hbm.Station;

public class AnnotationTypeDB {
	
	public static List<AnnotationType> getAnnotationTypeList() {
		String q  = "select t.annotation_type_num, t.annotation_type_name, array_agg(g.annotation_group_description) "+
			" from annotation_type t "+
			" left join annotation_type_group a on a.annotation_type_num = t.annotation_type_num "+
			" left join annotation_group g on g.annotation_group_num = a.annotation_group_num "+
			" group by t.annotation_type_num, t.annotation_type_name order by annotation_type_name";		
		List<AnnotationType> list = new ArrayList<AnnotationType>();
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			AnnotationType e = new AnnotationType();
			e.setAnnotationTypeNum((Integer)arr[0]);
			e.setAnnotationTypeName(""+arr[1]);
			String group = ""+arr[2];	
			if(!group.contains("NULL")) e.setGroupNames(""+arr[2]);
			list.add(e);
		}
		return list;
	}
	
	
	public static List<AnnotationGroup> getAnnotationGroup(Integer typeNum) {
		String q  = "select g.* from annotation_type_group a, annotation_group g "+
				" where g.annotation_group_num = a.annotation_group_num and a.annotation_type_num = "+typeNum;		
		List<AnnotationGroup> list = new ArrayList<AnnotationGroup>();
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			AnnotationGroup e = new AnnotationGroup();
			e.setGroupNum((Integer)arr[0]);
			e.setGroupDesc(""+arr[2]);
			list.add(e);
		}
		return list;
	}
	
	public static List<Integer> getGroupNum(Integer typeNum) {
		String q  = "select a.annotation_group_num from annotation_type_group a where  a.annotation_type_num = "+typeNum;		
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> olist = DBUtil.getIntegerList(q);
		for(Integer e: olist) {
			list.add((Integer)e);
		}
		return list;
	}
	
	/*	
	public static String update(AnnotationType at) {
		String error = null;
		Integer groupNum = at.getGroupNum();	
		Integer typeNum = at.getAnnotationTypeNum(); 
		String q = null;		
		if(groupNum == null) {
			q="delete from annotation_type_group where annotation_type_num = "+ group 
		}
		if(groupId != null) groupId=groupId.toUpperCase();
		q = "update annotation_type set annotation_type_name ="+DBUtil.StringValue(at.getAnnotationTypeName())+",group_id ="+
				DBUtil.StringValue(at.getGroupId())+" where annotation_type_num="+at.getAnnotationTypeNum();
		error = DBUtil.update(q);
	
		return error;
	}
*/		
	public static String create(AnnotationType at) {
		String error = null;
		Integer annotationTypeNum = DBUtil.getNumber("select nextVal('annotation_type_annotation_type_num_seq')");
		String q = "INSERT INTO annotation_type (annotation_type_num, annotation_type_name) "+
				" VALUES (	"+annotationTypeNum+","+ DBUtil.StringValue(at.getAnnotationTypeName())+")";	
		error = DBUtil.update(q);
		List<AnnotationGroup> gl = at.getGroups();
		
		if(error == null && gl !=null ) {
			for(AnnotationGroup ag: gl) {
				q = "INSERT INTO annotation_type_group VALUES ("+ag.getGroupNum()+","+annotationTypeNum+")";
				error = DBUtil.update(q);
				if(error != null) break;
			}
		}
	
		return error;
	}
}
