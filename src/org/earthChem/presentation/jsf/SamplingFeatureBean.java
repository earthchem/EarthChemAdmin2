package org.earthChem.presentation.jsf;
// This ManagedBean is used for person data
/**
 * @author      Bai-Hao Chen
 * @version     1.0
 * @since       1.0
 */
import java.io.IOException;
/**
 * This class is a controller. It receives request from client, gets data from business layer and returns data to client. 
 * It also implement action lisenner of xhtml. 
 * 
 * @author      Bai-Hao Chen 
 * @version     1.0               
 * @since       1.0     (8/23/2017)
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.earthChem.db.AnnotationDB;
import org.earthChem.db.DBUtil;
import org.earthChem.db.StationDB;
import org.earthChem.model.Annotation;
import org.earthChem.model.FeatureOfInterest;
import org.earthChem.model.Method;
import org.earthChem.model.SamplingFeature;
import org.earthChem.model.Station;
import org.earthChem.presentation.jsf.theme.Theme;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

 
@ManagedBean(name="sfBean")
@SessionScoped
public class SamplingFeatureBean implements Serializable {
	
	public void lookup() {
		String code = search.getSamplingFeatureCode();
		String alias = search.getAlias();
		System.out.println("bc-look "+code+":"+alias);
		if(!"".equals(code.trim())) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("sfCode",code);
		else if	(!"".equals(alias.trim())) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("sfAlias",alias);
		else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("WARN!",  "The code is required!") );
		
		database =(String)	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("database");
	}	
	
	public void createNew() {
   
		if(search.getSamplingFeatureTypeNum() == 1) {
			if("petdb".equals(database))
				PrimeFaces.current().executeScript("PF('sampleDialog').show()");
			else 
				PrimeFaces.current().executeScript("PF('sampleDialog2').show()");
		} else {
			PrimeFaces.current().executeScript("PF('stationDialog').show()");
		}		
	}	
	
	public SamplingFeature getSearch() {
		if(search == null) search = new SamplingFeature();
		return search;
	}


	public void setSearch(SamplingFeature search) {
		this.search = search;
	}

	

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	


	public Integer getSfNum() {
		return sfNum;
	}

	public void setSfNum(Integer sfNum) {
		this.sfNum = sfNum;
	}


	

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}




	private SamplingFeature search;
	private String page="empty";
	private Integer sfNum;
	private String database;
	
 }
