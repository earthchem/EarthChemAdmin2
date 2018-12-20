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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.earthChem.rest.CitationRest;
import org.earthChem.db.CitationDB;
import org.earthChem.db.CitationList;
import org.earthChem.db.CitationPurge;
import org.earthChem.db.DBUtil;
import org.earthChem.db.EquipmentDB;
import org.earthChem.db.ExpeditionDB;
import org.earthChem.db.DatasetDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.model.Affiliation;
import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.model.Dataset;
import org.earthChem.model.EcStatusInfo;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
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

 
@ManagedBean(name="homeBean")
@SessionScoped
public class HomeBean implements Serializable {
	
	public void selectDatabase(AjaxBehaviorEvent event) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("database", database);
	 }
	
	public void onTabChange(TabChangeEvent event) {
		 tab = event.getTab().getTitle();
	 }
	
	 
	 
	 public String getTab() {
		return tab;
	}



	public void setTab(String tab) {
		this.tab = tab;
	}

	

	public String getDatabase() {
		return database;
	}



	public void setDatabase(String database) {
		this.database = database;
	}



	private String tab;
	private String database;
}				
