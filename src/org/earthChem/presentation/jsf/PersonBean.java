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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.earthChem.db.DBUtil;
import org.earthChem.db.OrganizationDB;
import org.earthChem.db.PersonDB;
import org.earthChem.db.postgresql.hbm.Affiliation;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.Person;
import org.earthChem.db.postgresql.hbm.PersonExternalIdentifier;
//import org.earthChem.domain.Affiliation;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
 
@ManagedBean(name="personBean")
@SessionScoped
public class PersonBean implements Serializable {
	 
	private void reset() {
		 affiliations = PersonDB.getAffiliactions(person.getPersonNum());
		 peis = PersonDB.getPersonExternalIdentifier(person.getPersonNum());
		 
		 newPEI = new ArrayList<PersonExternalIdentifier>();
		 newPEI.add(new PersonExternalIdentifier());
		 
		 newAffiliations = new ArrayList<Affiliation>();		 
		 newAffiliations.add(new Affiliation());	
	 }
	 
	 // begin for person tab
	 public void createNew(){
		 person = new Person();	
		 newAffiliations = new ArrayList<Affiliation>();		 
		 newAffiliations.add(new Affiliation());
		 affiliations = new ArrayList<Affiliation>();	
		 selectedOrganization = null;
		 newOrg = new Organization();
		 isNew = true;
	 }
	 public void selectPerson() {
		 reset();
	//	 affiliations = PersonDB.getAffiliactions(person.getPersonNum());
	//	 peis = PersonDB.getPersonExternalIdentifier(person.getPersonNum());
		 isNew = false;
	 }
	 
	 // end for person tab
	public List<org.earthChem.presentation.jsf.theme.Theme> completeTheme(String query) {
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

	public void onItemSelect(SelectEvent event) {
	}
	
	public void addAffByAuto(AjaxBehaviorEvent ev) {
		if(selectedOrganization !=null) {
			Affiliation a =  new Affiliation();
			Affiliation na = newAffiliations.get(0);
			a.setOrganizationNum(selectedOrganization.getId());
			a.setOrganizationName( selectedOrganization.getDisplayName());
			String email = na.getPrimaryEmail();
			a.setPrimaryEmail(email);
			Integer affNum = DBUtil.getNumber("select nextval('affiliation_affiliation_num_seq')");
			String q = "insert into affiliation (affiliation_num,person_num, organization_num, primary_email) "+
			" values ("+affNum+","+person.getPersonNum()+","+selectedOrganization.getId()+","+DBUtil.StringValue(email)+")";			
			a.setAffiliationNum(affNum);
			affiliations.add(a);		
			DBUtil.update(q);
			selectedOrganization = null;
			na.setPrimaryEmail(null);
		}
	}
	
	public void deleteAff(Integer affNum) {
			String q = "DELETE FROM affiliation WHERE affiliation_num ="+affNum;			
			DBUtil.update(q);	
			affiliations = PersonDB.getAffiliactions(person.getPersonNum());
	}
	
	public void cancel() {
		person = new Person();
		reset();
		PrimeFaces.current().executeScript("PF('personDialog').hide()");
	}
	
//database -----------------------------	
	public void addNewOrganization() {
		String status = OrganizationDB.save(newOrg, true);
		Integer personNum = person.getPersonNum();
		if(personNum != null && status==null) status = DBUtil.update("INSERT INTO affiliation (person_num, organization_num) values ("+personNum+","+newOrg.getOrganizationNum()+")");
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
	
	public void save() {
		String status = null;
		if(isNew) status = PersonDB.addPerson(person,affiliations,newPEI);
		else status = PersonDB.updatePerson(person,newAffiliations,newPEI);
		if(status == null) {
			ThemeService.update();
			FacesContext.getCurrentInstance().addMessage("personMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "Person, "+person.getFullName()+", was successfully created/updated!"));
			PrimeFaces.current().executeScript("PF('personDialog').hide()");
			PrimeFaces.current().executeScript("PF('personTableWidgetVar').filter()");
			reset();
			personList = PersonDB.getPersonList();
			
		} else {
			FacesContext.getCurrentInstance().addMessage("personMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void deleteAffiliation(Integer affNum) {
		DBUtil.update("DELETE FROM affiliation WHERE affiliation_num="+affNum);
		affiliations = PersonDB.getAffiliactions(selectedTheme.getId());		
	}
	
	public void deletePEI(Integer bridgeNum) {
		DBUtil.update("DELETE FROM person_external_identifier WHERE bridge_num="+bridgeNum);
		peis = PersonDB.getPersonExternalIdentifier(selectedTheme.getId());
	}

	// get and set methods----------------------------------------------------------------
	public Theme getSelectedTheme() {
		Theme newTheme = (Theme)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("selectedSearch");
		if(newTheme != null) {
			if(selectedTheme==null || newTheme.getId() != selectedTheme.getId()) {
				selectedTheme = newTheme;
				person = PersonDB.getPerson(selectedTheme.getId());;
				reset();
			}
		}
		return selectedTheme; 
	}

	public void setSelectedTheme(Theme selectedTheme) {
		this.selectedTheme = selectedTheme;
	}

	
	public Person getPerson() { 
	//	getSelectedTheme();
		if(person== null) person=new Person();
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Affiliation> getAffiliations() {
			return affiliations;
	}

	public void setAffiliations(List<Affiliation> affiliations) {
		this.affiliations = affiliations;
	}

	public OrganizationService getService() {
		return service;
	}

	public void setService(OrganizationService service) {
		this.service = service;
	}
	
	public List<Affiliation> getNewAffiliations() {
		if(newAffiliations == null) {
			 newAffiliations = new ArrayList<Affiliation>();		 
			 newAffiliations.add(new Affiliation());
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

	public List<PersonExternalIdentifier> getPeis() {
		return peis;
	}
	public void setPeis(List<PersonExternalIdentifier> peis) {
		this.peis = peis;
	}

	
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}


	public List<Person> getPersonList() {
		personList = PersonDB.getPersonList();
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
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

	public String getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}


	public Organization getNewOrg() {
		if(newOrg==null) newOrg = new Organization();
		return newOrg;
	}
	public void setNewOrg(Organization newOrg) {
		this.newOrg = newOrg;
	}
	
	@ManagedProperty("#{organizationService}")
	private OrganizationService service;
	private Theme selectedTheme;  //person
	private Theme selectedOrganization;  //person
	private Person person;
	private List<Affiliation> affiliations= new ArrayList<Affiliation>();
	private List<Affiliation> newAffiliations;
	private List<PersonExternalIdentifier> newPEI;
	private List<PersonExternalIdentifier> peis;
	//below is fields used in person tab
	private List<Person> personList;
	private boolean isNew;
	private List<Affiliation> autoAffiliations;
	private String activeIndex;
	private Organization newOrg;
	
 }
