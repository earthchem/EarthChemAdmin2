package org.earthChem.presentation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.earthChem.rest.CitationRest;
import org.earthChem.db.CitationDB;
import org.earthChem.db.CitationList;
import org.earthChem.db.CitationPurge;
import org.earthChem.db.DBUtil;
import org.earthChem.db.EquipmentDB;
import org.earthChem.db.ExpeditionDB;
import org.earthChem.db.FeatureOfInterestTypeDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.model.Affiliation;
import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.model.EcStatusInfo;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
import org.earthChem.model.FeatureOfInterestType;
import org.earthChem.model.Organization;
import org.earthChem.rest.InvalidDoiException;
import org.earthChem.presentation.jsf.theme.CommentService;
import org.earthChem.presentation.jsf.theme.EquipmentService;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

 
@ManagedBean(name="featureOfInterestTypeBean")
@SessionScoped
public class FeatureOfInterestTypeBean implements Serializable {
	
	//---auto complete---
	
		public void selectFeatureOfInterestType() {
			
				featureOfInterestType = FeatureOfInterestTypeDB.getFeatureOfInterestType(featureOfInterestType.getFeatureOfInterestTypeNum()); 
				isNew =false;
		}
		
		public void createNew() {
			featureOfInterestType = new FeatureOfInterestType();
			featureOfInterestType.setFeatureOfInterestTypeNum(DBUtil.getNumber("SELECT nextval('feature_Of_interest_cv_feature_Of_interest_cv_num_seq')"));
			isNew = true;
		}
		

	@SuppressWarnings("deprecation")
	public void updateFeatureOfInterestType(){
		String status = FeatureOfInterestTypeDB.update(featureOfInterestType, isNew);
		if(status == null) {
			featureOfInterestType = new FeatureOfInterestType();		
			PrimeFaces.current().executeScript("PF('featureOfInterestTypeDialog').hide()");
			PrimeFaces.current().executeScript("PF('featureOfInterestTypeTableWidgetVar').filter()");
			featureOfInterestTypeList = FeatureOfInterestTypeDB.getFeatureOfInterestTypeList();
			isNew = false;
		}
		else {
			FacesContext.getCurrentInstance().addMessage("featureOfInterestTypeEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}

	public void cancelEditFeatureOfInterestType() {		
		featureOfInterestType = new FeatureOfInterestType();
		PrimeFaces.current().executeScript("PF('featureOfInterestTypeDialog').hide()");		
	}

	//get set 
	public boolean isNew() {
		return isNew;
	}


	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public FeatureOfInterestType getFeatureOfInterestType() {
		if(featureOfInterestType == null) {
			featureOfInterestType = new FeatureOfInterestType();
			featureOfInterestType.setFeatureOfInterestTypeNum(DBUtil.getNumber("SELECT nextval('feature_Of_interest_cv_feature_Of_interest_cv_num_seq')"));
		}
		return featureOfInterestType;
	}


	public List<FeatureOfInterestType> getFeatureOfInterestTypeList() {
		if(featureOfInterestTypeList == null) featureOfInterestTypeList = FeatureOfInterestTypeDB.getFeatureOfInterestTypeList();
		return featureOfInterestTypeList;
	}


	public void setFeatureOfInterestTypeList(List<FeatureOfInterestType> featureOfInterestTypeList) {
		this.featureOfInterestTypeList = featureOfInterestTypeList;
	}


	public void setFeatureOfInterestType(FeatureOfInterestType featureOfInterestType) {
		this.featureOfInterestType = featureOfInterestType;
	}

	private List<FeatureOfInterestType> featureOfInterestTypeList;
	private FeatureOfInterestType featureOfInterestType;
	private boolean isNew;	
 }
