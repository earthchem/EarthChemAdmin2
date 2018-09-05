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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.earthChem.db.DBUtil;
import org.earthChem.db.OrganizationDB;
import org.earthChem.db.PersonDB;
import org.earthChem.db.postgresql.hbm.Affiliation;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.Person;
import org.earthChem.db.postgresql.hbm.PersonExternalIdentifier;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
 
@ManagedBean(name="personNewBean")
@SessionScoped
public class PersonNewBean implements Serializable {
	
	 @PostConstruct
	 public void init() {
		 String q ="SELECT external_identifier_system_num, external_identifier_system_name FROM external_identifier_system order by external_identifier_system_name";
		 externalIdentifierSystemOptions = DBUtil.getSelectItems(q);
		 reset();
	 }

	 private void reset() {
		 activeIndex = "null";
		 person = new Person();
		 newOrg= new Organization();
		 newPEI = new ArrayList<PersonExternalIdentifier>();
		 newPEI.add(new PersonExternalIdentifier());
		 newAffiliations = new ArrayList<Affiliation>();		 
		 newAffiliations.add(new Affiliation());	
		 autoAffiliations = new ArrayList<Affiliation>();
		 autoAffiliations.add(new Affiliation());
		 affiliations = new ArrayList<Affiliation>();
		 selectedOrganization = null;
	 }
	//actions
	 public void addNewPerson(){
		 person = new Person();
	 }
	 
	public List<org.earthChem.presentation.jsf.theme.Theme> completeTheme(String query) {
 //       List<org.earthChem.presentation.jsf.theme.Theme> allThemes = service.getThemes();
		 List<org.earthChem.presentation.jsf.theme.Theme> allThemes = service.getThemes();
        List<org.earthChem.presentation.jsf.theme.Theme> filteredThemes = new ArrayList<Theme>();
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        }        
        return filteredThemes;
    }
	
	public void addIdentifier() {
		int size = newPEI.size();
		PersonExternalIdentifier pei = newPEI.get(size-1);
		if(pei.getPersonExternalIdentifier() != null)
		newPEI.add(new PersonExternalIdentifier());
	}

	public void addEmail() {}
	public void selectCountry() {}

	public void cancleNewOrganization() {
		newOrg = null;
		activeIndex = null;
	}
	
	public void cancel() {
		reset();
	}
	
	public void onItemSelect(SelectEvent event) {
	}	
	
	public void addAffByAuto() {
		if(selectedOrganization !=null) {
			Affiliation a =  new Affiliation();
			Affiliation na = autoAffiliations.get(0);
			a.setOrganizationNum(selectedOrganization.getId());
			a.setOrganizationName( selectedOrganization.getDisplayName());
			String email = na.getPrimaryEmail();
			a.setPrimaryEmail(email);
			affiliations.add(a);					
			selectedOrganization = null;
			na.setPrimaryEmail(null);
		}
		
		
	}
	
	public void addAffiliation () {
		
	}

	// database
	public void addNewOrganization() {
		String status = OrganizationDB.save(newOrg, true);
		if(status == null) {
			Affiliation naff = new Affiliation();
			naff.setOrganizationNum(newOrg.getOrganizationNum());
			naff.setOrganizationName(newOrg.getFullName());
			naff.setPrimaryEmail(newOrg.getEmail());
			affiliations.add(naff);
			OrganizationService.update();
			activeIndex = null;
		} else {
			FacesContext.getCurrentInstance().addMessage("personNewMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}				
	}
	//addPersonAndAuthorList
	public void saveAll(Integer citationNum) {
		String status = PersonDB.addPersonAndAuthorList(person,affiliations,newPEI,citationNum);
		if(status == null) {			
			    PrimeFaces.current().executeScript("PF('personNew').hide()");
		//	    ThemeService.update();
			    reset();
		} else {
			FacesContext.getCurrentInstance().addMessage("personNewMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void save() {
		String status = PersonDB.addPerson(person,affiliations,newPEI);
		if(status == null) {
			ThemeService.update();
			FacesContext.getCurrentInstance().addMessage("personNewMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "New person, "+person.getFullName()+", was successfully created!"));
			reset();
		} else {
			FacesContext.getCurrentInstance().addMessage("personNewMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	
	// get and set methods----------------------------------------------------------------

	public OrganizationService getService() {
		return service;
	}

	public void setService(OrganizationService service) {
		this.service = service;
	}
	
	public List<Affiliation> getNewAffiliations() {
		if(newAffiliations == null) {
			 newAffiliations = new ArrayList<Affiliation>();		 
		}
		return newAffiliations;
	}

	public void setNewAffiliations(List<Affiliation> newAffiliations) {
		this.newAffiliations = newAffiliations;
	}


	public Theme getSelectedOrganization() {
		return selectedOrganization;
	}

	public void setSelectedOrganization(Theme selectedOrganization) {
		this.selectedOrganization = selectedOrganization;
	}

	public SelectItem[] getExternalIdentifierSystemOptions() {
		return externalIdentifierSystemOptions;
	}

	public void setExternalIdentifierSystemOptions(SelectItem[] externalIdentifierSystemOptions) {
		this.externalIdentifierSystemOptions = externalIdentifierSystemOptions;
	}


	public List<PersonExternalIdentifier> getNewPEI() {
		if(newPEI == null) {
			newPEI = new ArrayList<PersonExternalIdentifier>();
			newPEI.add(new PersonExternalIdentifier());
		}
		return newPEI;
	}

	public void setNewPEI(List<PersonExternalIdentifier> newPEI) {
		this.newPEI = newPEI;
	}


	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

	public Organization getNewOrg() {
		if(newOrg==null) newOrg = new Organization();
		return newOrg;
	}
	public void setNewOrg(Organization newOrg) {
		this.newOrg = newOrg;
	}


	public String getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}

	


	public List<Affiliation> getAutoAffiliations() {
		if(autoAffiliations == null) {
			autoAffiliations = new ArrayList<Affiliation>();		 
			 autoAffiliations.add(new Affiliation());
		}
		return autoAffiliations;
	}

	public void setAutoAffiliations(List<Affiliation> autoAffiliations) {
		this.autoAffiliations = autoAffiliations;
	}


	


	public List<Affiliation> getAffiliations() {
		if(affiliations == null) {
			affiliations = new ArrayList<Affiliation>();		 
		}
		return affiliations;
	}

	public void setAffiliations(List<Affiliation> affiliations) {
		this.affiliations = affiliations;
	}

	@ManagedProperty("#{organizationService}")
	private OrganizationService service;
	private Theme selectedOrganization;  //person
	private Person person;
	private List<Affiliation> newAffiliations;
	private List<Affiliation> autoAffiliations;
	private List<Affiliation> affiliations;
	private List<PersonExternalIdentifier> newPEI;
	private SelectItem[] externalIdentifierSystemOptions;
	private Organization newOrg;
	private String activeIndex;

	
 }
