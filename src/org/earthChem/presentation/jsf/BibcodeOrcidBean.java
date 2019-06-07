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

import org.earthChem.rest.BibcodeOrcidRest;
import org.earthChem.rest.CitationRest;
import org.earthChem.db.CitationDB;
import org.earthChem.db.CitationList;
import org.earthChem.db.CitationPurge;
import org.earthChem.db.DBUtil;
import org.earthChem.db.postgresql.hbm.AuthorList;
import org.earthChem.db.postgresql.hbm.Citation;
import org.earthChem.db.postgresql.hbm.EcStatusInfo;
import org.earthChem.rest.InvalidDoiException;
import org.earthChem.presentation.jsf.theme.CommentService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

/*********
 * JSF Backing Bean for Reference Page
 *
 */
 
@ManagedBean(name="bibcodeOrcidBean")
@SessionScoped
public class BibcodeOrcidBean implements Serializable {
	

	
	public void searchBibcodebyDOI() {	
				try
				{
					
					new BibcodeOrcidRest().searchBibcodeByDoi();				
				}
				catch (InvalidDoiException ie)
				{
					FacesContext.getCurrentInstance().addMessage("citationEdiMsg", 
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Validation Error:", "Entered Invalid DOI"));
				} 
				catch (Exception ex)
				{
					FacesContext.getCurrentInstance().addMessage("citationEdiMsg", 
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"System Error:", ex.getMessage()));
				}

	}
	
	
		
	public void orcidUpdate() {	
		try
		{
			
			new BibcodeOrcidRest().orcidUpdate();				
		}
		catch (InvalidDoiException ie)
		{
			FacesContext.getCurrentInstance().addMessage("citationEdiMsg", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Validation Error:", "Entered Invalid DOI"));
		} 
		catch (Exception ex)
		{
			FacesContext.getCurrentInstance().addMessage("citationEdiMsg", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"System Error:", ex.getMessage()));
		}

}
	
	
 }
