package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.db.postgresql.hbm.Station;
import org.earthChem.db.postgresql.hbm.Annotation;
import org.earthChem.db.postgresql.hbm.FeatureOfInterest;
import org.earthChem.db.postgresql.hbm.Organization;

public class StationDB {
	
	public static List<Station> getStationList(String search) {
		
		List<Station> list = new ArrayList<Station>();
		 String	q = "select s.sampling_feature_num, s.sampling_feature_code, s.sampling_feature_geotype, st_astext(feature_geometry), s.elevation_min, s.elevation_max, s.sampling_feature_name, s.sampling_feature_description, array_agg(a.annotation_text) from sampling_feature  s "+ 
		 " left join sampling_feature_annotation fa on fa.sampling_feature_num = s.sampling_feature_num "+
		 " left join annotation a on a.annotation_num = fa.annotation_num  where s.sampling_feature_type_num = 3 and (a.annotation_type_num=62 or a.annotation_type_num is null) "+
		 " and upper(sampling_feature_code) like '%"+search.toUpperCase()+"%' "+
		 " group by s.sampling_feature_num, s.sampling_feature_code, s.sampling_feature_geotype, st_astext(feature_geometry), s.elevation_min, s.elevation_max, s.sampling_feature_name, s.sampling_feature_description "+
		 " order by s.sampling_feature_code";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Station e = new Station();
			e.setSamplingFeatureNum((Integer)arr[0]);
			e.setSamplingFeatureCode(""+arr[1]);
			e.setSamplingFeatureGeotype(""+arr[2]);
			e.setGeometry(""+arr[3]);
			if(arr[3] != null) convertGeometry(e, ""+arr[3]);
			if(arr[4] != null) e.setElevationMin((Double)arr[4]);
			if(arr[5] != null) e.setElevationMax((Double)arr[5]);
			if(arr[6] != null) e.setSamplingFeatureName(""+arr[6]);
			if(arr[7] != null) e.setSamplingFeatureDescription(""+arr[7]);
			if(arr[8] != null) e.setTectonicSettingName(""+arr[8]);
			list.add(e);
		}
		return list;
	}
	
	public static Station getStation(String search) {
		
		List<Station> list = new ArrayList<Station>();
		 String	q = "select s.sampling_feature_num, s.sampling_feature_code, s.sampling_feature_geotype, st_astext(feature_geometry), s.elevation_min, s.elevation_max, s.sampling_feature_name, s.sampling_feature_description, a.annotation_text, a.data_source_num from sampling_feature  s "+ 
		 " left join sampling_feature_annotation fa on fa.sampling_feature_num = s.sampling_feature_num "+
		 " left join annotation a on a.annotation_num = fa.annotation_num  where s.sampling_feature_type_num = 3 and (a.annotation_type_num=62 or a.annotation_type_num is null) "+
		 " and upper(sampling_feature_code) = '"+search+"' order by s.sampling_feature_code";
		List<Object[]> olist = DBUtil.getECList(q);
		Station e = new Station();
		for(Object[] arr: olist) {			
			e.setSamplingFeatureNum((Integer)arr[0]);
			e.setSamplingFeatureCode(""+arr[1]);
			e.setSamplingFeatureGeotype(""+arr[2]);
			e.setGeometry(""+arr[3]);
			if(arr[3] != null) convertGeometry(e, ""+arr[3]);
			if(arr[4] != null) e.setElevationMin((Double)arr[4]);
			if(arr[5] != null) e.setElevationMax((Double)arr[5]);
			if(arr[6] != null) e.setSamplingFeatureName(""+arr[6]);
			if(arr[7] != null) e.setSamplingFeatureDescription(""+arr[7]);
			if(arr[8] != null) e.setTectonicSettingName(""+arr[8]);
			list.add(e);
		}
		return e;
	}
	
	public static List<FeatureOfInterest> getGeograph(Integer stationNum) {
		String q = "select cv.feature_of_interest_cv_name,cv.feature_of_interest_description,t.feature_of_interest_type_name, fi.feature_of_interest_num "+
		" from sampling_feature  s "+
		" left join feature_of_interest fi on fi.sampling_feature_num = s.sampling_feature_num "+
		" left join feature_of_interest_cv cv on cv.feature_of_interest_cv_num = fi.feature_of_interest_cv_num "+
		" left join feature_of_interest_type t on t.feature_of_interest_type_num = fi.feature_of_interest_type_num "+
		" where s.sampling_feature_type_num = 3 and s.sampling_feature_num = "+stationNum;
		List<FeatureOfInterest> fois = new ArrayList<FeatureOfInterest>();
		List<Object[]> list = DBUtil.getECList(q);
		for(Object[] arr: list) {
			FeatureOfInterest f = new FeatureOfInterest();
			if(arr[0] != null) f.setFeatureOfInterestCvName(""+arr[0]);
			if(arr[1] != null) f.setFeatureOfInterestCvDescription(""+arr[1]);
			if(arr[2] != null) f.setFeatureOfInterestTypeName(""+arr[2]);
			f.setFeatureOfInterestNum((Integer)arr[3]);
			fois.add(f);
		}
		return fois;
	}
	
	
	public static String addGeograph(Integer stationNum, FeatureOfInterest geograph) {
		String error = null;
		String cv = geograph.getFeatureOfInterestCvName();
		Integer typeNum = geograph.getFeatureOfInterestTypeNum();
		Integer cvNum = DBUtil.getNumber("SELECT feature_of_interest_cv_num FROM earthchem.feature_of_interest_cv "+
				" where upper(feature_of_interest_cv_name) = upper("+DBUtil.StringValue(cv)+")");
		if(cvNum ==null) {
			cvNum = DBUtil.getNumber("select nextVal('feature_of_interest_cv_feature_of_interest_cv_num_seq')");
			String q ="INSERT INTO feature_of_interest_cv "+
					 "(feature_of_interest_cv_num, feature_of_interest_cv_name, feature_of_interest_description) "+
					 " VALUES ("+cvNum+","+DBUtil.StringValue(cv)+","+DBUtil.StringValue(geograph.getFeatureOfInterestCvDescription())+")";
			error = DBUtil.update(q);
		}
	
		if(error == null) {
			String q ="INSERT INTO earthchem.feature_of_interest (feature_of_interest_num, sampling_feature_num, feature_of_interest_type_num, feature_of_interest_cv_num) "+
					" VALUES (nextVal('feature_of_interest_feature_of_interest_num_seq'),"+stationNum+","+typeNum+","+cvNum+")";
			error = DBUtil.update(q);
		}
		return error;
	}
	
	
	public static void convertGeometry(Station e, String g) {
		g = g.substring(g.indexOf("(")+1, g.indexOf(")"));
		if("POINT".equals(e.getSamplingFeatureGeotype()) ){			
			String arr[] = g.split(" ");
			e.setLong1(new Double(arr[0]));
			e.setLat1(new Double(arr[1]));
		} else {
			String points[] = g.split(",");
			String arr[] = points[0].split(" ");
			e.setLong1(new Double(arr[0]));
			e.setLat1(new Double(arr[1]));
			arr = points[1].split(" ");
			e.setLong2(new Double(arr[0]));
			e.setLat2(new Double(arr[1]));
		}
		
	}
	
	public static String update(Station e) {
			String error = null;
			Integer num = e.getSamplingFeatureNum();	
			String samplingFeatureCode = DBUtil.StringValue(e.getSamplingFeatureCode().toUpperCase());
			String samplingFeatureName = DBUtil.StringValue(e.getSamplingFeatureName());				
			String geotype ="'"+ e.getSamplingFeatureGeotype()+"'";
			String desc = DBUtil.StringValue(e.getSamplingFeatureDescription());
			Double elevationMin = e.getElevationMin();
			Double elevationMax = e.getElevationMax();
			String description = DBUtil.StringValue(e.getSamplingFeatureDescription());
			String geometry = getGeometryQuery(e); 
			String q = null;
			if(num==null) {		
				num = DBUtil.getNumber("select nextval('sampling_feature_sampling_feature_num_seq')");
				 q ="INSERT into sampling_Feature (sampling_Feature_num, sampling_Feature_type_num, sampling_Feature_code, sampling_Feature_name, sampling_Feature_description,"+
					" sampling_feature_geotype,elevation_Min,elevation_Max,feature_geometry) VALUES "+
					" ("+num+",3,"+ samplingFeatureCode+","+samplingFeatureName+","+ description+","+
					geotype+","+elevationMin+","+elevationMax+","+geometry+")";		
				 error= DBUtil.update(q);
				 if(error != null) return error;
				 e.setSamplingFeatureNum(num);
				 List<FeatureOfInterest> foiList = e.getFoiList();
				 for(FeatureOfInterest f: foiList)
				 {
					 error = addGeograph(num, f);
					 if(error!=null) break;
				 }
				 if(error != null) return error;
				 List<Annotation> list = e.getTsList();
				 if(list == null || list.size()==0) return null;
				 for(Annotation a: list) {	
					 error = AnnotationDB.addSamplingFeatureAnnotation(num,62,a.getDataSourceNum(), a.getAnnotationText());
				 }
					
			} else {
				q = "update sampling_Feature set "+
					" sampling_Feature_code="+samplingFeatureCode+","+
					" sampling_Feature_name="+samplingFeatureName+","+
					" sampling_Feature_description="+description+","+
					" sampling_feature_geotype="+geotype+","+
					" elevation_Min ="+ e.getElevationMin()+","+
					" elevation_Max ="+ e.getElevationMax()+","+
					" feature_geometry="+geometry+
					" where sampling_Feature_type_num= 3 and sampling_Feature_num="+num;
				error= DBUtil.update(q);
			}
			
			String text = e.getSamplingFeatureName();
			if(text == null || "".equals(text)) return null;
			return error;
		}
	
	    private static String getGeometryQuery(Station e) {
	    	String p1 = ""+e.getLong1()+","+e.getLat1();
	    	 if("POINT".equals(e.getSamplingFeatureGeotype())) {
	       		 return "ST_SetSRID(ST_MakePoint("+p1+"), 4326)";
	       	 } else {
	       		 return "ST_SetSRID(ST_MakeLINE(ST_MakePoint("+p1+"), ST_MakePoint("+e.getLong2()+","+e.getLat2()+")), 4326)";
	       	 }   		 
	    }
}
