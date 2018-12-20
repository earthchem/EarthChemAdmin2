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
import org.primefaces.event.RowEditEvent;
import org.earthChem.db.CitationDB;
import org.earthChem.db.CitationList;
import org.earthChem.db.CitationPurge;
import org.earthChem.db.DBUtil;
import org.earthChem.db.EquipmentDB;
import org.earthChem.db.ExpeditionDB;
import org.earthChem.model.Action;
import org.earthChem.db.ActionDB;


 
@ManagedBean(name="analysisActionBean")
@SessionScoped
public class AnalysisActionBean implements Serializable {
	private List<Action> actionList;
	private Action action;
	private Integer citationNum;
	
	
	public void createNew(RowEditEvent event) {
		
	}
	
	
	
	public void onRowEdit(RowEditEvent event) {
		Action editedAction =(Action) event.getObject();
	
		System.out.println("bc-add "+editedAction.getMethodNum());
	
	}
	
	
	
	

	public void lookup() {
	//	actionList = ActionDB.getActionList(citationNum);
		
		
		actionList = new ArrayList<Action>();
		
			Action ac = new Action();
			ac.setMethodNum(3);
			actionList.add(ac);
			ac = new Action();
			ac.setMethodNum(5);
			actionList.add(ac);
		
		
		
		
		
		
		if(actionList.size()==0) {
			FacesContext.getCurrentInstance().addMessage("analysisActionMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "No action data were found!", ""));
		}
		action = new Action();
	}	
	
	public void createNew() {
		
	}
	
	public void add() {
		String status = ActionDB.Add(action);
		if(status == null) {
			actionList = ActionDB.getActionList(citationNum);
			action = new Action();
		} else {
			FacesContext.getCurrentInstance().addMessage("analysisActionMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void delete(Integer actionNum) {
		String status = ActionDB.delete(actionNum);
		if(status == null) {
			actionList = ActionDB.getActionList(citationNum);
		} else {
			FacesContext.getCurrentInstance().addMessage("analysisActionMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}			
	}
	
	public List<Action> getActionList() {
		if(actionList==null) actionList = new ArrayList<Action>();
		return actionList;
	}

	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}

	public SelectItem[] getDatasets() {
		if(citationNum != null) {
			String q ="select d.dataset_num, d.dataset_code from citation_dataset c, dataset d where c.dataset_num = d.dataset_num "+
					" and c.citation_num = "+citationNum+" order by d.dataset_code";
			return DBUtil.getSelectItems(q); 
		}
		return new SelectItem[0];
	}

	public Action getAction() {
		if(action==null) action = new Action();
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Integer getCitationNum() {
		return citationNum;
	}

	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}

	
	 
 }
