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
import org.earthChem.model.Affiliation;
import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.model.EcStatusInfo;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
import org.earthChem.model.Method;
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

 
@ManagedBean(name="methodBean")
@SessionScoped
public class MethodBean implements Serializable {
	
	//---auto complete---
		public void onItemSelectOrg(SelectEvent event) {
	//		if(expedition == null) getExpedition();
	//		expedition.setOrganizationNum(selectedOrganization.getId());
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
		
	
		public void selectMethod() {
				method = MethodDB.getMethod(method.getMethodNum()); 
				isNew =false;
		}
		
		public void createNew() {
			method = new Method();
			method.setMethodNum(((Long) DBUtil.uniqueObject("SELECT nextval('method_method_num_seq')")).intValue());
			isNew = true;
			selectedOrganization = null;
		}
	
		public void createOrganization() {
			organization= new Organization();
		}
		
		public void addNewOrganization() {
			String status = OrganizationDB.save(organization, true);
			if(status == null) {
				OrganizationService.update();
				method.setOrganizationName(organization.getOrganizationName());
				method.setOrganizationNum(organization.getOrganizationNum());
		//		isNew = false;
				organization = new Organization();
				PrimeFaces.current().executeScript("PF('labDialog').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage("newOrganizationMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
			}				
		}
		
		public void cancleNewOrganization() {
			PrimeFaces.current().executeScript("PF('labDialog').hide()");
			organization = new Organization();
		}


	public void updateMethod(){
		if(selectedOrganization != null) method.setOrganizationNum(selectedOrganization.getId());
		String status = MethodDB.update(method, isNew);
		if(status == null) {
			method = new Method();		
		PrimeFaces.current().executeScript("PF('methodDialogVar').hide()");
		PrimeFaces.current().executeScript("PF('methodTableWidgetVar').filter()");
		
		// oncomplete="PF('methodTableWidgetVar').filter()"
		methodList = MethodDB.getMethodList();
		}
		else {//"methodEditMsg"
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}

	public void cancelEditMethod() {
		
		method = new Method();
		PrimeFaces.current().executeScript("PF('methodDialogVar').hide()");
		
	}

	//get set methods
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
	
	public boolean isNew() {
		return isNew;
	}


	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

/*
	public List<Method> getMethodList() {
		
		 return MethodDB.getMethodList();
	}

*/
	
	
	public Method getMethod() {
		if(method == null) {
			method = new Method();
			method.setMethodNum(((Long) DBUtil.uniqueObject("SELECT nextval('method_method_num_seq')")).intValue());
		}
		return method;
	}


	public List<Method> getMethodList() {
		if(methodList == null) methodList = MethodDB.getMethodList();
		return methodList;
	}


	public void setMethodList(List<Method> methodList) {
		this.methodList = methodList;
	}


	public void setMethod(Method method) {
		this.method = method;
	}

	private List<Method> methodList;
	private Method method;
	private boolean isNew;	
	private Theme selectedOrganization;
	private Organization organization;	
//	private boolean isNewOrg;
 }
