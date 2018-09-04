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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.earthChem.db.OrganizationDB;
import org.earthChem.db.postgresql.hbm.Method;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

 
@ManagedBean(name="orgBean")
@SessionScoped
public class OrganizationBean implements Serializable {
	
	public void lookup() {
		organizationList = OrganizationDB.getOrganizations(search);		
	}
	
	public void showAll() {
		organizationList = OrganizationDB.getOrganizations(null);		
	}
	
	public void createNew() {
		organization = new Organization();
		isNew = true;
		
	}
	
	public void selectOrganization() {
		isNew = false;
	}
	
	public void selectCountry() {
	}
	
	public void addNewOrganization() {
		String status = OrganizationDB.save(newOrg, isNew);
		if(status == null) {
			OrganizationService.update();
			FacesContext.getCurrentInstance().addMessage("organizationNewMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "New organization, "+newOrg.getOrganizationName()+", was successfully created!"));
			newOrg = new Organization();
			PrimeFaces.current().executeScript("PF('organizationDialog2').hide()");
			PrimeFaces.current().executeScript("PF('organizationTableWidgetVar').filter()");
			organizationList = OrganizationDB.getOrganizationList();
		} else {
			FacesContext.getCurrentInstance().addMessage("organizationNewMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}		
	}
	
	public void cancelEdit() {
		organization = OrganizationDB.getOrganization(organization.getOrganizationNum());
		PrimeFaces.current().executeScript("PF('organizationDialog2').hide()");
	}
	
	public void cancelNew() {
		organization = new Organization();
	}
	
	public void saveOrg() {
		if(organization.getOrganizationNum() != 0) update();
		else { 
			newOrg = organization;
			addNewOrganization();
		}
	}

	public void update() {
		String status = OrganizationDB.save(organization,isNew);
//		String status = OrganizationDB.save(organization,false);
		organization = OrganizationDB.getOrganization(organization.getOrganizationNum());
		if(status == null) {
			OrganizationService.update();
			FacesContext.getCurrentInstance().addMessage("orgEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "The organiation data were successfully updated!"));
			PrimeFaces.current().executeScript("PF('organizationDialog2').hide()");
			organizationList = OrganizationDB.getOrganizationList();
		} else {
			FacesContext.getCurrentInstance().addMessage("orgEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	
	 // get and set methods
	public Theme getSelectedTheme() {
		Theme newTheme = (Theme)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("selectedSearch");
		if(newTheme != null) {
			if(selectedTheme==null || newTheme.getId() != selectedTheme.getId()) {
				selectedTheme = newTheme;
				organization = OrganizationDB.getOrganization(selectedTheme.getId());
			}
		}		
		return selectedTheme;
	}

	public void setSelectedTheme(org.earthChem.presentation.jsf.theme.Theme selectedTheme) {
		this.selectedTheme = selectedTheme;
	}
	
	public Organization getOrganization() {
	//	getSelectedTheme();
		if(organization==null) organization = new Organization();
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	

	public Organization getNewOrg() {
		if(newOrg==null) newOrg = new Organization();
		return newOrg;
	}

	public void setNewOrg(Organization newOrg) {
		this.newOrg = newOrg;
	}
	

	public List<Organization> getOrganizationList() {
	//	if(organizationList==null) organizationList = OrganizationDB.getOrganizationList();
		if(organizationList==null) organizationList = new ArrayList<Organization>();
		return organizationList;
	}

	public void setOrganizationList(List<Organization> organizationList) {
		this.organizationList = organizationList;
	}



	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}




	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}




	private org.earthChem.presentation.jsf.theme.Theme selectedTheme;  //organization
	private Organization organization;
	
//	@ManagedProperty("#{organizationService}")
//	private OrganizationService service;
	private String search; 
	private Organization newOrg;
	private List<Organization> organizationList;
	private boolean isNew;
	
 }
