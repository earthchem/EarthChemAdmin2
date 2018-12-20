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
import org.earthChem.db.FeatureOfInterestCvDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.model.Affiliation;
import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.model.EcStatusInfo;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
import org.earthChem.model.FeatureOfInterestCv;
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

 
@ManagedBean(name="featureOfInterestCvBean")
@SessionScoped
public class FeatureOfInterestCvBean implements Serializable {
	
	//---auto complete---
	
	public void lookup() {
		featureOfInterestCvList = FeatureOfInterestCvDB.getFeatureOfInterestCvList(search);
	}
	
		public void selectFeatureOfInterestCv() {
			
				featureOfInterestCv = FeatureOfInterestCvDB.getFeatureOfInterestCv(featureOfInterestCv.getFeatureOfInterestCvNum()); 
				isNew =false;
		}
		
		public void createNew() {
			featureOfInterestCv = new FeatureOfInterestCv();
			featureOfInterestCv.setFeatureOfInterestCvNum(DBUtil.getNumber("SELECT nextval('feature_Of_interest_cv_feature_Of_interest_cv_num_seq')"));
			isNew = true;
		}
		

	@SuppressWarnings("deprecation")
	public void updateFeatureOfInterestCv(){
		String status = FeatureOfInterestCvDB.update(featureOfInterestCv, isNew);
		if(status == null) {
			featureOfInterestCv = new FeatureOfInterestCv();		
			PrimeFaces.current().executeScript("PF('featureOfInterestCvDialog').hide()");
			PrimeFaces.current().executeScript("PF('featureOfInterestCvTableWidgetVar').filter()");
			featureOfInterestCvList = FeatureOfInterestCvDB.getFeatureOfInterestCvList(search);
			isNew = false;
		}
		else {
			FacesContext.getCurrentInstance().addMessage("featureOfInterestCvEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}

	public void cancelEditFeatureOfInterestCv() {		
		featureOfInterestCv = new FeatureOfInterestCv();
		PrimeFaces.current().executeScript("PF('featureOfInterestCvDialog').hide()");		
	}

	//get set 
	public boolean isNew() {
		return isNew;
	}


	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public FeatureOfInterestCv getFeatureOfInterestCv() {
		if(featureOfInterestCv == null) {
			featureOfInterestCv = new FeatureOfInterestCv();
			featureOfInterestCv.setFeatureOfInterestCvNum(DBUtil.getNumber("SELECT nextval('feature_Of_interest_cv_feature_Of_interest_cv_num_seq')"));
		}
		return featureOfInterestCv;
	}


	public List<FeatureOfInterestCv> getFeatureOfInterestCvList() {
		if(featureOfInterestCvList == null) featureOfInterestCvList = new ArrayList<FeatureOfInterestCv>();
		return featureOfInterestCvList;
	}


	public void setFeatureOfInterestCvList(List<FeatureOfInterestCv> featureOfInterestCvList) {
		this.featureOfInterestCvList = featureOfInterestCvList;
	}


	public void setFeatureOfInterestCv(FeatureOfInterestCv featureOfInterestCv) {
		this.featureOfInterestCv = featureOfInterestCv;
	}

	
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}



	private List<FeatureOfInterestCv> featureOfInterestCvList;
	private FeatureOfInterestCv featureOfInterestCv;
	private boolean isNew;	
	private String search;
 }
