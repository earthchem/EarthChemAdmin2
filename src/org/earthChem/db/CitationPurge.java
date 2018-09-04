package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.earthChem.db.postgresql.hbm.AuthorList;
import org.earthChem.db.postgresql.hbm.Citation;
import org.earthChem.presentation.jsf.theme.Theme;


public class CitationPurge {

	private List<String> queries = new ArrayList<String>();
	private String actions;
	
	public String purge(Integer citationNum) {
	//	removeAnnotation(citationNum);
		removeAction(citationNum); 
		removeResult(citationNum);
		removeDataset(citationNum);
		return DBUtil.updateList(queries);
	}

	/*
	private void removeAnnotation(Integer citationNum) {
		queries.add("DELETE from action_annotation where annotation_num in (select annotation_num from annotation where data_source_num ="+ citationNum+") ");
		queries.add("DELETE from sampling_feature_annotation where annotation_num in (select annotation_num from annotation where data_source_num ="+ citationNum+") ");
		queries.add("DELETE from annotation where data_source_num ="+ citationNum);
	}
*/	
	private void removeAction(Integer citationNum) {
		String q = "select action_num from action where dataset_num in (select dataset_num from citation_dataset where citation_num = "+citationNum+")";
		List<Integer> actionList = DBUtil.getIntegerList(q);
		actions = DBUtil.listToNumbers(actionList);
		if(actions != null) {
			q = "delete from action_annotation where action_num in ("+actions+")";
			queries.add(q);
			q = "delete from equipment_action where action_num in ("+actions+")";
			queries.add(q);
			q = "delete from action_external_identifier where action_num in ("+actions+")";
			queries.add(q);
		}
	}
	
	private void removeResult(Integer citationNum) {
		String q = "select result_num from dataset_result where dataset_num in (select dataset_num from citation_dataset where citation_num = "+citationNum+")";
		List<Integer> resultList = DBUtil.getIntegerList(q);
		String results = DBUtil.listToNumbers(resultList);

		if(results != null) {
			queries.add("delete from result_dataquality where result_num in ("+results+")");			
			queries.add("delete from numeric_data where result_num in ("+results+")");	
			queries.add("delete from text_data where result_num in ("+results+")");	
			queries.add("delete from related_result where result_num in ("+results+") or related_result_num in ("+results+")");		
			queries.add("delete from dataset_result where result_num in ("+results+")");
			queries.add("delete from result where result_num in ("+results+")");	
		}
		queries.add("delete from feature_action where action_num in ("+actions+")");
		queries.add("delete from action where action_num in ("+actions+")");
		
	}
	
	private void removeDataset(Integer citationNum) {
		String q = "select dataset_num from citation_dataset where citation_num = "+citationNum;
		List<Integer> datasetList = DBUtil.getIntegerList(q);
		String datasets = DBUtil.listToNumbers(datasetList);
		queries.add("delete from citation_dataset where citation_num = "+citationNum);
		queries.add("delete from dataset where dataset_num in ("+datasets+")");
	}
	
}
