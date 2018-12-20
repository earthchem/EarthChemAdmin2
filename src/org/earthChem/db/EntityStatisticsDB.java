package org.earthChem.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.earthChem.model.EntityStatistics;

public class EntityStatisticsDB {
	
	
	public static List<EntityStatistics> getStatisticsList() {
		String q ="SELECT" + 
				"	statistics_num," + 
				"	citations," + 
				"	datasets," + 
				"	stations," + 
				"	expeditions," + 
				"	specimens," + 
				"	total_chem_values," + 
				"	rock_values," + 
				"	mineral_values," + 
				"	volc_glass_values," + 
				"	inclusion_values," + 
				"	ts" + 
				" FROM " + 
				"   entity_statistics ";
		List<EntityStatistics> list = new ArrayList<EntityStatistics>();
		List<Object[]> la =		DBUtil.list(q);
		for(Object[] a: la) {
			EntityStatistics e  = new EntityStatistics();
			e.setStatistics_num((Integer)a[0]);
			e.setCitations((Integer)a[1]);
			e.setDatasets((Integer)a[2]);
			e.setStations((Integer)a[3]);
			e.setExpeditions((Integer)a[4]);
			e.setSpecimens((Integer)a[5]);
			e.setTotal_chem_values((Integer)a[6]);
			e.setRock_values((Integer)a[7]);
			e.setMineral_values((Integer)a[8]);
			e.setVolc_glass_values((Integer)a[9]);
			e.setInclusion_values((Integer)a[10]);
			e.setTs((Timestamp)a[11]);
			list.add(e);
		}
		return list;
	}
}
