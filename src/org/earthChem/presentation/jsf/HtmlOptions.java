package org.earthChem.presentation.jsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.earthChem.db.DBUtil;

/**
 * This contains reused constants for this application.
 * 
 * @author      Bai-Hao Chen 
 * @version     1.0               
 * @since       1.0     (9/22/2017)
 */
@ManagedBean(name="optionBean")
@ViewScoped
public class HtmlOptions implements Serializable {	
	private SelectItem[] orgTypes;
	private SelectItem[] externalIdentifierSystems;
	private SelectItem[] states;
	private SelectItem[] countries;
	private SelectItem[] equipmentTypes;
	private SelectItem[] equipments;
	private SelectItem[] methodTypes;
	private SelectItem[] methods;
	private SelectItem[] variableTypes;
	private SelectItem[] featureOfInterestCvs;
	private SelectItem[] featureOfInterestTypes;
	private SelectItem[] tectonicSettings;
	private SelectItem[] rockTypes;
	private SelectItem[] rockClasses;
	private SelectItem[] samplingFeatureTypes;
	private SelectItem[] annotationGroups;
	private SelectItem[] actionTypes;
	private SelectItem[] materials;

	public SelectItem[] getMaterials() {
		if(materials==null) {
			String q ="select material_num, material_code from material order by material_code";
			materials= DBUtil.getSelectItems(q); 
		}
		return materials;
	}

	
	public SelectItem[] getActionTypes() {
		if(actionTypes==null) {
			String q ="select action_type_num, action_type_name from action_type order by action_type_name";
			actionTypes= DBUtil.getSelectItems(q); 
		}
		return actionTypes;
	}

	public SelectItem[] getAnnotationGroups() {
		if(annotationGroups==null) {
			String q ="select annotation_group_num, annotation_group_description from annotation_group order by annotation_group_code";
			annotationGroups= DBUtil.getSelectItems(q); 
		}
		return annotationGroups;
	}
		
	public SelectItem[] getSamplingFeatureTypes() {
		if(samplingFeatureTypes==null) {
			String q ="select sampling_feature_type_num, sampling_feature_type_name from sampling_feature_type where sampling_feature_type_num in (1,3)";
			samplingFeatureTypes= DBUtil.getSelectItems(q); 
		}
		return samplingFeatureTypes;
	}
	
	public SelectItem[] getRockClasses() {
		if(rockClasses==null) {
			String q ="select distinct t.taxonomic_classifier_name, t.taxonomic_classifier_name, t.parent_taxonomic_classifier_num from taxonomic_classifier t where t.taxonomic_classifier_type_cv = 'Rock Class' order by t.taxonomic_classifier_name";
			rockClasses= DBUtil.getSelectItems(q); 
		}
		return rockClasses;
	}
	
	public SelectItem[] getRockTypes() {
		if(rockTypes==null) {
			String q ="select t.taxonomic_classifier_num, t.taxonomic_classifier_name, t.parent_taxonomic_classifier_num from taxonomic_classifier t where t.taxonomic_classifier_type_cv = 'Rock Type' order by t.taxonomic_classifier_name";
			rockTypes= DBUtil.getSelectItems(q); 
		}
		return rockTypes;
	}
	
	
	public SelectItem[] getTectonicSettings() {
		if(tectonicSettings==null) {
			String q ="select distinct annotation_text, annotation_text from annotation where annotation_text <> '' and annotation_type_num = 62 order by annotation_text";
			tectonicSettings= DBUtil.getSelectItems(q); 
		}
		return tectonicSettings;
	}
			
			
	public SelectItem[] getFeatureOfInterestCvs() {
		if(featureOfInterestCvs==null) {
			String q ="select feature_of_interest_cv_num, feature_of_interest_cv_name from feature_of_interest_cv order by feature_of_interest_cv_name";
			featureOfInterestCvs= DBUtil.getSelectItems(q); 
		}
		return featureOfInterestCvs;
	}
	
	public SelectItem[] getFeatureOfInterestTypes() {
		if(featureOfInterestTypes==null) {
			String q ="select feature_of_interest_type_num, feature_of_interest_type_name from feature_of_interest_type where feature_of_interest_type_description <> 'sesar' order by feature_of_interest_type_name";
			featureOfInterestTypes= DBUtil.getSelectItems(q); 
		}
		return featureOfInterestTypes;
	}
	
	public SelectItem[] getVariableTypes() {
		if(variableTypes==null) {
			String q ="select variable_type_num, variable_type_code from variable_type order by variable_type_code";
			variableTypes= DBUtil.getSelectItems(q); 
		}
		return variableTypes;
	}
	
	public SelectItem[] getMethods() {
		if(methods==null) {
			String q ="select method_num, method_code from method order by method_code";
			methods= DBUtil.getSelectItems(q); 
		}
		return methods;
	}
	
	public SelectItem[] getMethodTypes() {
		if(methodTypes==null) {
			String q ="select method_type_num, method_type_name from method_type order by method_type_name";
			methodTypes= DBUtil.getSelectItems(q); 
		}
		return methodTypes;
	}
	
			
	public SelectItem[] getEquipmentTypes() {
		if(equipmentTypes==null) {
			String q ="select equipment_type_num, equipment_type_name from equipment_type order by equipment_type_name";
			equipmentTypes= DBUtil.getSelectItems(q); 
		}
		return equipmentTypes;
	}
	
	public SelectItem[] getEquipments() {
		if(equipments==null) {
			String q ="select equipment_num, equipment_code from equipment order by equipment_code";
			equipments= DBUtil.getSelectItems(q); 
		}
		return equipments;
	}
	
	public SelectItem[] getOrgTypes() {
		if(orgTypes==null) {
			String q ="select organization_type_num, organization_type_name from organization_type";
			orgTypes = DBUtil.getSelectItems(q); 
		}
		return orgTypes;
	}

	public SelectItem[] getExternalIdentifierSystems() {
		if(externalIdentifierSystems ==null) {
			String q ="SELECT external_identifier_system_num, external_identifier_system_name FROM external_identifier_system order by external_identifier_system_name";
			externalIdentifierSystems = DBUtil.getSelectItems(q); 
		}
		return externalIdentifierSystems;
	}
	
	public SelectItem[] getStates() {
		if(states==null) {
			String q ="select state_num, state_name from state where state_name <> '' order by state_name";
			states = DBUtil.getSelectItems(q);	
		}
		return states;
	}
	
	public SelectItem[] getCountries() {
		if(countries==null) {
			String q ="select country_num, country_name from country order by country_name";
			countries = DBUtil.getSelectItems(q);	
		}
		return countries;
	}
	

}
	

	
	
	
	
	
	
	
	
	
	
