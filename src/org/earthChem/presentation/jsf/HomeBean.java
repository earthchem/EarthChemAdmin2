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
import org.earthChem.db.postgresql.hbm.Affiliation;
import org.earthChem.db.postgresql.hbm.AuthorList;
import org.earthChem.db.postgresql.hbm.Citation;
import org.earthChem.db.postgresql.hbm.Dataset;
import org.earthChem.db.postgresql.hbm.EcStatusInfo;
import org.earthChem.db.postgresql.hbm.Equipment;
import org.earthChem.db.postgresql.hbm.Expedition;
import org.earthChem.db.postgresql.hbm.Organization;
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
	
	
	public void changeTab(Integer tabNum) {
		PrimeFaces.current().executeScript("PF('tabPanel').select("+tabNum+")");
	}
	
	
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
