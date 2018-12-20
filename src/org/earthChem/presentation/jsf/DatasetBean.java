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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

 
@ManagedBean(name="datasetBean")
@SessionScoped
public class DatasetBean implements Serializable {
	
	public void onRowEdit(RowEditEvent event) {
		dataset = (Dataset) event.getObject();
        if(dataset.getTypeNum()==1) dataset.setType("Reference Table");       
        else dataset.setType("Not Reference Table");
        save();
    }
	
	public void selectDataset() {
		//dataset = DatasetDB.getDataset(dataset.getDatasetNum()); 		
	}

	public void createNew() {
		dataset = new Dataset();
	}
	
	public void lookup() {
		datasetList = DatasetDB.getDatasetList(citationNum);
		
	}
	
	public void save() {
		dataset.setCitationNum(citationNum);
		String status = DatasetDB.save(dataset);
		if(status == null) {
			dataset = new Dataset();	
			datasetList = DatasetDB.getDatasetList(citationNum);
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
		
	}
	
	public List<Dataset> getDatasetList() {
		if(datasetList==null) datasetList = new ArrayList<Dataset>();
		return datasetList;
	}


	public void setDatasetList(List<Dataset> datasetList) {
		this.datasetList = datasetList;
	}

	
	public Dataset getDataset() {
		if(dataset == null) dataset = new Dataset();
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Integer getCitationNum() {
		return citationNum;
	}

	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}

	public SelectItem[] getDatasetTypes() {
		if(datasetTypes==null) {
			String q ="select t.dataset_type_num, t.dataset_type_code from dataset_type t";
			datasetTypes= DBUtil.getSelectItems(q); 
		}
		return datasetTypes;
	}

	private List<Dataset> datasetList;
	private Dataset dataset;
	private Integer citationNum;
	private SelectItem[] datasetTypes;
	
	
 }
