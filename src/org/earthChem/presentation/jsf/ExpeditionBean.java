package org.earthChem.presentation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.earthChem.db.DBUtil;
import org.earthChem.db.EquipmentDB;
import org.earthChem.db.ExpeditionDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
import org.earthChem.model.Organization;
import org.earthChem.presentation.jsf.theme.EquipmentService;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;


 
@ManagedBean(name="expeditionBean")
@SessionScoped
public class ExpeditionBean implements Serializable {
	//actions
	public void lookup (){
		expeditionList = ExpeditionDB.getExpeditionList(search);
	}
	
	public void showAll (){
		expeditionList = ExpeditionDB.getExpeditionList(null);
	}
	
	public void addNewOrganization() {
		String status = OrganizationDB.save(organization, true);
		if(status == null) {
			OrganizationService.update();
			expedition.setOrganizationName(organization.getOrganizationName());
			expedition.setOrganizationNum(organization.getOrganizationNum());
			isNewOrg = false;
			organization = new Organization();
			PrimeFaces.current().executeScript("PF('organizationDialog').hide()");
		} else {
			FacesContext.getCurrentInstance().addMessage("newOrganizationMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}				
	}
	
	public void cancleNewOrganization() {
		PrimeFaces.current().executeScript("PF('organizationDialog').hide()");
		organization = new Organization();
	}
	
	public void addNewEquipment() {		
		equipment.setEquipmentNum(((Long) DBUtil.uniqueObject("select nextval('equipment_equipment_num_seq')")).intValue());
		String status = EquipmentDB.save(equipment, true);
		if(status == null) {
			EquipmentService.update();
			expedition.setEquipmentCode(equipment.getEquipmentCode());
			expedition.setEquipmentNum(equipment.getEquipmentNum());
			isNewEq = false;
			equipment = new Equipment();
			PrimeFaces.current().executeScript("PF('equipmentDialog').hide()");
		} else {
			FacesContext.getCurrentInstance().addMessage("newOrganizationMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}				
	}
	
	public void cancleNewEquipment() {
		equipment = new Equipment();
		PrimeFaces.current().executeScript("PF('equipmentDialog').hide()");
	}
	
	public void selectExpedition() {
		expedition = ExpeditionDB.getExpedition(expedition.getActionNum());		
		isNew = false;
		isNewOrg = false;
		isNewEq = false;
	}

	public void createNew() {
		expedition = new Expedition();
		expedition.setActionNum(((Long) DBUtil.uniqueObject("SELECT nextval('action_action_num_seq')")).intValue());
		isNew = true;
		isNewOrg = true;
		isNewEq = true;
		selectedOrganization=null;
		selectedEquipment=null;
	}
	
	public void updateExpedition(){
		String status = ExpeditionDB.saveExpedition(expedition, isNew);
		if(status == null) {
			expedition = new Expedition();
			PrimeFaces.current().executeScript("PF('expeditionDialog').hide()");
			PrimeFaces.current().executeScript("PF('dataTableWidgetVar').filter()");
			expeditionList = ExpeditionDB.getExpeditionList(search);
		} else {
			expedition.setErrorMsg("ERROR: "+status);
		}
	}

	public void cancelEditExpediton() {
		expedition = new Expedition();
		PrimeFaces.current().executeScript("PF('expeditionDialog').hide()");
	}
	
	public void createOrganization() {
		organization= new Organization();
	}
	
	public void createEquipment() {
		equipment= new Equipment();
	}
	
	//---auto complete---
	public void onItemSelectOrg(SelectEvent event) {
		if(expedition == null) getExpedition();
		expedition.setOrganizationNum(selectedOrganization.getId());
	}	
	
	public void onItemSelectEquip(SelectEvent event) {
		expedition.setEquipmentNum(selectedEquipment.getId());
	}	
	
	public List<org.earthChem.presentation.jsf.theme.Theme> completeThemeOrg(String query) {
        List<org.earthChem.presentation.jsf.theme.Theme> allThemes = OrganizationService.getThemes();
        List<org.earthChem.presentation.jsf.theme.Theme> filteredThemes = new ArrayList<Theme>();
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        }        
        return filteredThemes;
    }
	
	public List<org.earthChem.presentation.jsf.theme.Theme> completeThemeEquip(String query) {
        List<org.earthChem.presentation.jsf.theme.Theme> allThemes = EquipmentService.getThemes();
        List<org.earthChem.presentation.jsf.theme.Theme> filteredThemes = new ArrayList<Theme>();
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        }        
        return filteredThemes;
    }
	
	//--get and set---
	public Expedition getExpedition() {
		if(expedition==null) {
			expedition = new Expedition();
			expedition.setActionNum(((Long) DBUtil.uniqueObject("SELECT nextval('action_action_num_seq')")).intValue());
		}
		return expedition;
	}

	public void setExpedition(Expedition expedition) {
		this.expedition = expedition;
	}

	public List<Expedition> getExpeditionList() {
		if(expeditionList == null) {
			expeditionList = new ArrayList<Expedition>();
		}
		return expeditionList;
	}

	public void setExpeditionList(List<Expedition> expeditionList) {
		this.expeditionList = expeditionList;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	

	public Theme getSelectedOrganization() {
		return selectedOrganization;
	}
	public void setSelectedOrganization(Theme selectedOrganization) {
		this.selectedOrganization = selectedOrganization;
	}
	public Organization getOrganization() {
		if(organization== null)
			organization= new Organization();
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	

	public Theme getSelectedEquipment() {
		return selectedEquipment;
	}

	public void setSelectedEquipment(Theme selectedEquipment) {
		this.selectedEquipment = selectedEquipment;
	}



	public Equipment getEquipment() {
		if(equipment==null) equipment= new Equipment();
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	

	public boolean isNewOrg() {
		return isNewOrg;
	}

	public void setNewOrg(boolean isNewOrg) {
		this.isNewOrg = isNewOrg;
	}

	public boolean isNewEq() {
		return isNewEq;
	}

	public void setNewEq(boolean isNewEq) {
		this.isNewEq = isNewEq;
	}

	

	public String getSearch() {
		return search;
	}


	public void setSearch(String search) {
		this.search = search;
	}



	//@ManagedProperty("#{organizationService}")
	//private OrganizationService service;
	private Theme selectedOrganization;
	private Theme selectedEquipment;
	private Expedition expedition = new Expedition();
	private List<Expedition> expeditionList;
	private Organization organization;
	private Equipment equipment;
	private boolean isNew;
	private boolean isNewOrg;
	private boolean isNewEq;
	private String search;

 }
