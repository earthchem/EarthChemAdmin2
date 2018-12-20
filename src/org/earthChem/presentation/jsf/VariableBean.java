package org.earthChem.presentation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.earthChem.db.DBUtil;
import org.earthChem.db.VariableDB;
import org.earthChem.model.Variable;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

 
@ManagedBean(name="variableBean")
@SessionScoped
public class VariableBean implements Serializable {

	
	
	
	public void selectListType() {
		if(typeNum==null) return;
		variableList= VariableDB.getVariableList(typeNum);
		
	}
	
	public void selectVariable() {
		variable = VariableDB.getVariable(variable.getVariableNum()); 
		isNew =false;
	}
	
	public void createNew() {
		variable = new Variable();
		variable.setVariableNum(((Integer) DBUtil.getNumber("SELECT nextval('variable_variable_num_seq')")).intValue());
		isNew = true;
	}
	
	public void selectType() {}

	@SuppressWarnings("deprecation")
	public void update(){
		String status = VariableDB.update(variable, isNew);
		if(status == null) {
			variable = new Variable();	
			PrimeFaces.current().executeScript("PF('variableDialog').hide()");
			PrimeFaces.current().executeScript("PF('variableTableWidgetVar').filter()");
			variableList = VariableDB.getVariableList(typeNum);
		}
		else {
			FacesContext.getCurrentInstance().addMessage("variableEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}

	public void cancelUpdate() {	
		variable = new Variable();
		PrimeFaces.current().executeScript("PF('variableDialog').hide()");		
	}

	
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public List<Variable> getVariableList() {
		if(variableList==null) variableList= new ArrayList<Variable>();
		return variableList;
	}

	public void setVariableList(List<Variable> variableList) {
		this.variableList = variableList;
	}

	public Variable getVariable() {
		if(variable == null) createNew();
		return variable;
	}
	public void setVariable(Variable variable) {
		this.variable = variable;
	}
	
	
	
	public Integer getTypeNum() {
		return typeNum;
	}

	public void setTypeNum(Integer typeNum) {
		this.typeNum = typeNum;
	}



	private boolean isNew;	
	private Variable variable; 
	private List<Variable> variableList;
	private Integer typeNum;

 }
