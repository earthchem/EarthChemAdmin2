package org.earthChem.db;

import java.util.List;

import org.earthChem.db.postgresql.hbm.Citation;

public class CitationList {
	private static List<Citation> queueList;
	private static List<Citation> progressList;
	private static List<Citation> completedList;
	private static List<Citation> dataLoadedList;
	private static List<Citation> alertList;
	private static List<Citation> allList;
	
	public static List<Citation> getCitationList(String status) {
		if("IN_QUEUE".equals(status)) return getQueueList();
		else if ("IN_PROGRESS".equals(status)) return getProgressList();		
		else if ("DATA_LOADED".equals(status)) return getDataLoadedList();
		else if ("COMPLETED".equals(status)) return getCompletedList();
		else if ("ALERT".equals(status)) return getAlertList();
		else return getAllList();
	}
	
	
	
	public static List<Citation> getAllList() {
		allList=CitationDB.getCitationsWithStatus("ALL");
		return allList;
	}



	public static void setAllList(List<Citation> allList) {
		CitationList.allList = allList;
	}



	public static List<Citation> getQueueList() {
		//if(queueList==null) 
		queueList=CitationDB.getCitationsWithStatus("IN_QUEUE");
		return queueList;
	}
	
	public static void setQueueList(List<Citation> queueList) {		
		CitationList.queueList = queueList;
	}
	
	public static List<Citation> getProgressList() {
	//	if(progressList==null) 
			progressList=CitationDB.getCitationsWithStatus("IN_PROGRESS");
		return progressList;
	}
	public static void setProgressList(List<Citation> progressList) {
		CitationList.progressList = progressList;
	}
	public static List<Citation> getCompletedList() {	
	//	if(completedList==null) 
			completedList=CitationDB.getCitationsWithStatus("COMPLETED");		
		return completedList;
	}
	public static void setCompletedList(List<Citation> completedList) {
		CitationList.completedList = completedList;
	}
	public static List<Citation> getAlertList() {
	//	if(alertList==null) 
			alertList=CitationDB.getCitationsWithStatus("ALERT");
		return alertList;
	}
	public static void setAlertList(List<Citation> alertList) {
		CitationList.alertList = alertList;
	}



	public static List<Citation> getDataLoadedList() {
		dataLoadedList=CitationDB.getCitationsWithStatus("DATA_LOADED");		
		return dataLoadedList;
	}



	public static void setDataLoadedList(List<Citation> dataLoadedList) {
		CitationList.dataLoadedList = dataLoadedList;
	}
	
	
	

}
