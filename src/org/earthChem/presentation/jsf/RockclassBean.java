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
import org.earthChem.db.MethodDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.db.RockclassDB;
import org.earthChem.model.Affiliation;
import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.model.EcStatusInfo;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
import org.earthChem.model.Method;
import org.earthChem.model.Organization;
import org.earthChem.model.Taxonomic;
import org.earthChem.rest.InvalidDoiException;
import org.earthChem.presentation.jsf.theme.CommentService;
import org.earthChem.presentation.jsf.theme.EquipmentService;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.RockclassService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

 
@ManagedBean(name="rockclassBean")
@SessionScoped
public class RockclassBean implements Serializable {

	public void selectRockclass() {
		rockclass = RockclassDB.getRockclass(rockclass.getTaxonomicClassifierNum()); 
		isNew =false;
	}
	
	public void createNew() {
		rockclass = new Taxonomic();
		rockclass.setTaxonomicClassifierNum(((Integer) DBUtil.getNumber("SELECT nextval('taxonomic_classifier_taxonomic_classifier_num_seq')")).intValue());
		isNew = true;
	}
	
	public void selectTypeCv() {
		rockclassList= RockclassDB.getRockclassList(typeCv);
	}
	
	public void selectType() {}
	
	public List<org.earthChem.presentation.jsf.theme.Theme> completeTheme(String query) {
        List<org.earthChem.presentation.jsf.theme.Theme> allThemes = RockclassService.getThemes();
        List<org.earthChem.presentation.jsf.theme.Theme> filteredThemes = new ArrayList<Theme>();
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        }        
        return filteredThemes;
	}
	
	public void update(){
		String status = RockclassDB.update(rockclass, isNew);
		if(status == null) {
			rockclass = new Taxonomic();	
			PrimeFaces.current().executeScript("PF('rockclassDialog').hide()");
			PrimeFaces.current().executeScript("PF('rockclassTableWidgetVar').filter()");
			rockclassList = RockclassDB.getRockclassList(typeCv);
		}
		else {
			FacesContext.getCurrentInstance().addMessage("rockclassEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}

	public void cancelUpdate() {	
		rockclass = new Taxonomic();
		PrimeFaces.current().executeScript("PF('rockclassDialog').hide()");		
	}

	
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public List<Taxonomic> getRockclassList() {
		if(rockclassList==null) rockclassList = new ArrayList<Taxonomic>();  
		return rockclassList;
	}

	public void setRockclassList(List<Taxonomic> rockclassList) {
		this.rockclassList = rockclassList;
	}

	public Taxonomic getRockclass() {
		if(rockclass == null) createNew();
		return rockclass;
	}
	public void setRockclass(Taxonomic rockclass) {
		this.rockclass = rockclass;
	}
	
	
	
	public String getTypeCv() {
		return typeCv;
	}

	public void setTypeCv(String typeCv) {
		this.typeCv = typeCv;
	}



	private boolean isNew;	
	private Taxonomic rockclass; 
	private List<Taxonomic> rockclassList;
	private String typeCv;

 }
