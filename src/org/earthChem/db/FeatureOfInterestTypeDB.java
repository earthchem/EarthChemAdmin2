package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import org.earthChem.db.postgresql.hbm.FeatureOfInterestType;

public class FeatureOfInterestTypeDB {

	public static List<FeatureOfInterestType> getFeatureOfInterestTypeList() {
		List<FeatureOfInterestType> list = new ArrayList<FeatureOfInterestType>();
		 String	q = "select * from feature_of_interest_type order by feature_of_interest_type_name ";				
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			FeatureOfInterestType e = new FeatureOfInterestType();
			e.setFeatureOfInterestTypeNum((Integer)arr[0]);
			e.setFeatureOfInterestTypeName(""+arr[1]);		
			if(arr[2] !=null) e.setFeatureOfInterestTypeDescription(""+arr[2]);
			list.add(e);
		}
		return list;
	}
	
	public static FeatureOfInterestType getFeatureOfInterestType(Integer featureOfInterestTypeNum) {
		 String	q = "select * from feature_of_interest_type where feature_of_interest_type_num= "+featureOfInterestTypeNum;				
		 Object[] arr = DBUtil.uniqueResult(q);
			FeatureOfInterestType e = new FeatureOfInterestType();
			e.setFeatureOfInterestTypeNum((Integer)arr[0]);
			e.setFeatureOfInterestTypeName(""+arr[1]);	
			if(arr[2] !=null) e.setFeatureOfInterestTypeDescription(""+arr[2]);
		return e;
	}
	
	public static String update(FeatureOfInterestType e, boolean isNew) {
	
	//	FeatureOfInterestType e = new FeatureOfInterestType();
		Integer featureOfInterestTypeNum= e.getFeatureOfInterestTypeNum();
		String featureOfInterestTypeName = DBUtil.StringValue(e.getFeatureOfInterestTypeName());				
		String description = DBUtil.StringValue(e.getFeatureOfInterestTypeDescription());
		String q = null;
		if(isNew) {	
			 q ="INSERT INTO feature_of_interest_type "+
					 "(feature_of_interest_type_num, feature_of_interest_type_name, feature_of_interest_type_description) "+
					 " VALUES ("+featureOfInterestTypeNum+","+featureOfInterestTypeName+","+description+")";
		} else {
			q = "update feature_of_interest_type set "+
				" feature_of_interest_type_name="+featureOfInterestTypeName+","+
				" feature_of_interest_Type_description="+description+" where feature_of_interest_type_num="+featureOfInterestTypeNum;
		}
		
		return DBUtil.update(q);
	}
}