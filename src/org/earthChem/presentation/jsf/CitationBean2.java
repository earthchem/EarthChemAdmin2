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
import org.earthChem.db.postgresql.hbm.AuthorList;
import org.earthChem.db.postgresql.hbm.Citation;
import org.earthChem.db.postgresql.hbm.EcStatusInfo;
import org.earthChem.rest.InvalidDoiException;
import org.earthChem.presentation.jsf.theme.CommentService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

/*********
 * JSF Backing Bean for Reference Page
 *
 */
 
@ManagedBean(name="citationBean2")
@SessionScoped
public class CitationBean2 implements Serializable {
	
	public void onRowReorder(ReorderEvent event) {
		int from = event.getFromIndex();
		int to = event.getToIndex();
		AuthorList fromA = authorList.get(event.getFromIndex());
		authorList.remove(from);
		authorList.add(to,fromA);
		CitationDB.updateAuthorOrder(authorList);
		citation = CitationDB.getCitation(citation.getCitationNum());
	}
	
	public void showDialog(ActionEvent e) {
		
	}
	
	public void updateDOI() {	
		String doi = citation.getDoi();
				try
				{
					if(new CitationRest().getCitationByDoi(doi) != null) 
					CitationDB.updateDOI(citation);
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
	
	
	public void searchDOI(String d) {	
		String doi = citation.getDoi();
		String q = "select citation_num from citation_external_identifier where citation_external_identifier ='"+doi+"'";
		
		if(doi != null && !"".equals(doi)) {
			if(DBUtil.uniqueObject(q) != null) {
				FacesContext.getCurrentInstance().addMessage("citationEditMsg", 
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"System Error:", "The DOI already exists in database!"));
			}
			else {
			//	Integer cnum = citation.getCitationNum();
				try
				{
					citation= new CitationRest().getCitationByDoi(doi);
				//	citation.setCitationNum(cnum);
					citation.setCitationNum((Integer) DBUtil.uniqueObject("select max(citation_num+1) from citation"));
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
	}
	
	public void createNew() {
				isNew = true;
				citation =  new Citation();
				citation.setCitationNum((Integer) DBUtil.uniqueObject("select max(citation_num+1) from citation"));
				newPerson = null;
	}
	
	//beging for list
	public void selectCitation() {	

		citation=CitationDB.getCitation(citation.getCitationNum());
		authorList = CitationDB.getAuthorList(citation.getCitationNum());
		isNew = false;
		newPerson = null;
	}

	public void selectStatus() {
		citations = CitationList.getCitationList(dataStatus);

	}
	//end for list
	
	public void saveNewCitationNum() {}
	
	//for authorlist
	public List<Theme> completeTheme(String query) {
		List<Theme> allThemes = service.getThemes();
		List<Theme> filteredThemes = new ArrayList<Theme>(); 
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().startsWith(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        } 
        return filteredThemes;
	}
	//for internal comment
	 public List<Theme> completeCommentTheme(String query) {
	        List<Theme> allThemes = CommentService.getThemes();
	        List<Theme> filteredThemes = new ArrayList<Theme>();
	        for (int i = 0; i < allThemes.size(); i++) {
	            Theme skin = allThemes.get(i);
	         
	            if(skin.getName().toLowerCase().startsWith(query.toLowerCase())) {
	                filteredThemes.add(skin);
	            }
	        }	
		    return filteredThemes;
	 }
	
	public void onItemSelect(SelectEvent event) { 
		newPerson =(Theme) event.getObject();
	}
	
	
	public void cancelNew() {
		newCitation = new Citation(newCitation.getCitationNum());
		commentTheme = null;		
	}
	
	public void cancelEdit() {
		citation = new Citation();
	//	citation = CitationDB.getCitation(citation.getCitationNum());
	//	authorList = CitationDB.getAuthorList(citation.getCitationNum());
	}
	
	//----database update -------------------
	public void addAuthorList() {
		Integer dbCitationNum = DBUtil.getNumber("select citation_num from citation where citation_num ="+citation.getCitationNum());
		if(dbCitationNum== null) {
			FacesContext.getCurrentInstance().addMessage("editAuthorListMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please save new citation!"));
		}
		else {
			Integer personNum = newPerson.getId();
			Integer order = authorList.size()+1;
			CitationDB.addAuthorList(citation.getCitationNum(), personNum, order);
			authorList = CitationDB.getAuthorList(citation.getCitationNum());
			citation = CitationDB.getCitation(citation.getCitationNum());
			newAuthorList = null;
			newPerson= null;
		}
	}

	
	public void updateCitation() {
		String status = null;
		if(isNew) {
			if(commentTheme != null) {
				citation.setInternalComment(commentTheme.getDisplayName());		
			}
			status = CitationDB.saveCitation(citation,true); 
			isNew = false; //add 12/18/17
		}
		else {
			status = CitationDB.saveCitation(citation,false); 
		}
		
		if(status == null) status = CitationDB.updateAuthorOrder(authorList);
		if(status == null) {
		//12/19/17	citation = CitationDB.getCitation(citation.getCitationNum());
			Iterator<String> itIds = FacesContext.getCurrentInstance().getClientIdsWithMessages();
			while (itIds.hasNext()) {
			    List<FacesMessage> messageList = FacesContext.getCurrentInstance().getMessageList(itIds.next());
			    if (!messageList.isEmpty()) { // if empty, it will be unmodifiable and throw UnsupportedOperationException...
			        messageList.clear();
			    }
			}
			FacesContext.getCurrentInstance().addMessage("citationEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "The data were saved!"));
			citations = CitationList.getCitationList(dataStatus);
		}
		else {
			FacesContext.getCurrentInstance().addMessage("citationEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void deleteAuthorList(Integer authorListNum) {
		CitationDB.deleteAuthorList(authorListNum);
		Iterator<AuthorList> iter = authorList.iterator();
		while(iter.hasNext()){
		    if(iter.next().getAuthorListNum()==authorListNum)
		        iter.remove();
		}
		CitationDB.updateAuthorOrder(authorList);
		authorList = CitationDB.getAuthorList(citation.getCitationNum());
	}
	
	public void purge() {
		FacesContext.getCurrentInstance().addMessage("citationViewMsg", new FacesMessage(FacesMessage.SEVERITY_WARN, "Message", "Please wait while the data related to this citation are being purged!"));
		String status = new CitationPurge().purge(citation.getCitationNum());
		if(status == null) {
			FacesContext.getCurrentInstance().addMessage("citationViewMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "The data related to this citation were purged!"));
			citation =  new Citation();
		}
		else {
			FacesContext.getCurrentInstance().addMessage("citationViewMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}
	}

	public void deprecate() {
		String status = CitationDB.inactiveCitation(citation.getCitationNum());
		if(status == null) {
			FacesContext.getCurrentInstance().addMessage("citationViewMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "This citation was deprecated!"));
			citation =  new Citation();
		}
		else {
			FacesContext.getCurrentInstance().addMessage("citationViewMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}

//---- get and set methods-------------------------------
	

	public Citation getCitation() {
		if(citation == null) createNew(); 
	//	Integer newNum = (Integer)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("selectedSearchNum");
	/*	Integer currentNum = null;
		if(citation != null) currentNum = citation.getCitationNum();	
		if(newNum != null) {
			if(newNum != null && newNum.intValue() ==9999999) {
				isNew = true;
				citation =  new Citation();
				citation.setCitationNum((Integer) DBUtil.uniqueObject("select max(citation_num+1) from citation"));
			} else if(currentNum==null || newNum.intValue() != currentNum.intValue() ) {	
				isNew=false;
				citation = CitationDB.getCitation(newNum);
				authorList = CitationDB.getAuthorList(citation.getCitationNum());
			}
		} 
		*/
		return citation;
	}
	
	public void setCitation(Citation citation) {
		this.citation = citation;
	}

	
	public List<AuthorList> getAuthorList() {		
		authorList = CitationDB.getAuthorList(citation.getCitationNum());  //12/08/17
		return authorList;
	}

	public void setAuthorList(List<AuthorList> authorList) {
		this.authorList = authorList;
	}

	public List<AuthorList> getNewAuthorList() {
		if(newAuthorList==null) {
			newAuthorList = new ArrayList<AuthorList>();
			newAuthorList.add(new AuthorList());
		}
		return newAuthorList;
	}

	public void setNewAuthorList(List<AuthorList> newAuthorList) {
		this.newAuthorList = newAuthorList;
	}

	public Theme getNewPerson() {
		return newPerson;
	}

	public void setNewPerson(Theme newPerson) {
		this.newPerson = newPerson;
	}
	

	public ThemeService getService() {
		return service;
	}

	public void setService(ThemeService service) {
		this.service = service;
	}

	
	public AuthorList getAddedAuthorList() {
		return addedAuthorList;
	}

	public void setAddedAuthorList(AuthorList addedAuthorList) {
		this.addedAuthorList = addedAuthorList;
	}

	public EcStatusInfo getCitationInfo() {
		return citationInfo;
	}

	public void setCitationInfo(EcStatusInfo citationInfo) {
		this.citationInfo = citationInfo;
	}

	public SelectItem[] getStatuses() {
		SelectItem[] options = {
				new SelectItem("IN_QUEUE","IN_QUEUE"),
				new SelectItem("IN_PROGRESS","IN_PROGRESS"),	
				new SelectItem("DATA_LOADED","DATA_LOADED"),			
				new SelectItem("COMPLETED","COMPLETED"),
				new SelectItem("ALERT","ALERT")
		};	
		return options;
	}
	
	
	
	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}


	public CommentService getCommentService() {
		return commentService;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	

	public Theme getCommentTheme() {
		return commentTheme;
	}

	public void setCommentTheme(Theme commentTheme) {
		this.commentTheme = commentTheme;
	}

	public List<Citation> getCitations() {
		if(citations==null) {
			if(dataStatus == null) citations = new ArrayList<Citation>();
			else	citations = CitationList.getCitationList(dataStatus);
		}
		
		return citations;
	}
	
	public void updateCitations() {
		citations = CitationList.getCitationList(dataStatus);
	}
	
	

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	@ManagedProperty("#{themeService}")
	private ThemeService service;  //person
	private CommentService commentService;  //citation comment
	private Citation citation = new Citation();
	private Citation newCitation = new Citation();
	private List<AuthorList> authorList;
	private List<AuthorList> newAuthorList;
	private Theme newPerson;
	private Theme commentTheme;
	private AuthorList addedAuthorList;
	private EcStatusInfo citationInfo = new EcStatusInfo();
//	private String dataStatus = "ALL";
	private String dataStatus;
	private boolean isNew;

	//for citation lists
	private List<Citation> citations;	
		
 }
