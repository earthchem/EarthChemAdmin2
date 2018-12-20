package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import org.earthChem.model.FeatureOfInterestCv;

public class FeatureOfInterestCvDB {

	public static List<FeatureOfInterestCv> getFeatureOfInterestCvList(String name) {
		List<FeatureOfInterestCv> list = new ArrayList<FeatureOfInterestCv>();
		 String	q = "select * from feature_of_interest_cv "+
				 " where feature_of_interest_cv_name like upper('%"+name+"%') order by feature_of_interest_cv_name ";				
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			FeatureOfInterestCv e = new FeatureOfInterestCv();
			e.setFeatureOfInterestCvNum((Integer)arr[0]);
			e.setFeatureOfInterestCvName(""+arr[1]);		
			if(arr[2] !=null) e.setFeatureOfInterestDescription(""+arr[2]);
			list.add(e);
		}
		return list;
	}
	
	public static FeatureOfInterestCv getFeatureOfInterestCv(Integer featureOfInterestCvNum) {
		 String	q = "select * from feature_of_interest_cv where feature_of_interest_cv_num= "+featureOfInterestCvNum;				
		 Object[] arr = DBUtil.uniqueResult(q);
			FeatureOfInterestCv e = new FeatureOfInterestCv();
			e.setFeatureOfInterestCvNum((Integer)arr[0]);
			e.setFeatureOfInterestCvName(""+arr[1]);	
			if(arr[2] !=null) e.setFeatureOfInterestDescription(""+arr[2]);
		return e;
	}
	
	public static String update(FeatureOfInterestCv e, boolean isNew) {
	
	//	FeatureOfInterestCv e = new FeatureOfInterestCv();
		Integer featureOfInterestCvNum= e.getFeatureOfInterestCvNum();
		String featureOfInterestCvName = DBUtil.StringValue(e.getFeatureOfInterestCvName());				
		String description = DBUtil.StringValue(e.getFeatureOfInterestDescription());
		String q = null;
		if(isNew) {	
			 q ="INSERT INTO feature_of_interest_cv "+
					 "(feature_of_interest_cv_num, feature_of_interest_cv_name, feature_of_interest_description) "+
					 " VALUES ("+featureOfInterestCvNum+","+featureOfInterestCvName+","+description+")";
		} else {
			q = "update feature_of_interest_cv set "+
				" feature_of_interest_cv_name="+featureOfInterestCvName+","+
				" feature_of_interest_description="+description+" where feature_of_interest_cv_num="+featureOfInterestCvNum;
		}
		
		return DBUtil.update(q);
	}
}