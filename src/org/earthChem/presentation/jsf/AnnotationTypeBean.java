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
import org.earthChem.db.AnnotationTypeDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.model.Affiliation;
import org.earthChem.model.AnnotationGroup;
import org.earthChem.model.AnnotationType;
import org.earthChem.model.AuthorList;
import org.earthChem.model.Citation;
import org.earthChem.model.EcStatusInfo;
import org.earthChem.model.Equipment;
import org.earthChem.model.Expedition;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

 
@ManagedBean(name="annotationTypeBean")
@SessionScoped
public class AnnotationTypeBean implements Serializable {
	
	 public void selectAnnotationType() {
		 List<AnnotationGroup> g = AnnotationTypeDB.getAnnotationGroup(annotationType.getAnnotationTypeNum());
		 annotationType.setGroups(g);
	 }
	
	 public void dialogClose() {
		 annotationTypeList = AnnotationTypeDB.getAnnotationTypeList();
	 }
	 
	 public void createNew(){
		 annotationType = new AnnotationType();
		 group = new AnnotationGroup();
		 
	 }
	 
	 public void deleteGroup(AnnotationGroup g) {
		Integer typeNum = annotationType.getAnnotationTypeNum();
		if(typeNum == null) {
			List<AnnotationGroup> list  = annotationType.getGroups();
			list.remove(g);
			annotationType.setGroups(list);
		} else {
			 String q = "delete from annotation_type_group where annotation_group_num ="+g.getGroupNum()+" and annotation_type_num ="+typeNum;
			 String status = DBUtil.update(q);
			 if(status == null) {
				 List<AnnotationGroup> gl = AnnotationTypeDB.getAnnotationGroup(typeNum);
				 annotationType.setGroups(gl);
	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New data was saved!", annotationType.getAnnotationTypeName()));	    		
			 }
			 else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
			 }	
		}
	 }
	 
	 public void addGroup() {
		Integer typeNum = annotationType.getAnnotationTypeNum();
		if(typeNum == null) {
			List<AnnotationGroup> list  = annotationType.getGroups();
			if(list == null) list = new ArrayList<AnnotationGroup>();
			list.add(group);
			annotationType.setGroups(list);		
			group = new AnnotationGroup();
		} else {
			String q = "INSERT INTO annotation_type_group VALUES ("+group.getGroupNum()+","+typeNum+")";
			String status = DBUtil.update(q);
			if(status == null) {
				 List<AnnotationGroup> gl = AnnotationTypeDB.getAnnotationGroup(typeNum);
				 annotationType.setGroups(gl);
	    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New data was saved!", annotationType.getAnnotationTypeName()));	    		
	    		group = new AnnotationGroup();
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
			}	
		}
	 }
	 
	 
	 public void save() {
		 Integer typeNum = annotationType.getAnnotationTypeNum();
		 if(typeNum != null) {
			 String q = "update annotation_type set annotation_type_name='"+annotationType.getAnnotationTypeName()+"' where annotation_type_num="+typeNum;
			 String status = DBUtil.update(q);
			 if(status == null) {
		    		PrimeFaces.current().executeScript("PF('annotationTypeDialog').hide()");
		    		createNew();
			 }
				else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
			}	
			 
		 } else {
			 String status=AnnotationTypeDB.create(annotationType);
			 if(status == null) {
		    		annotationTypeList = AnnotationTypeDB.getAnnotationTypeList();
		    		PrimeFaces.current().executeScript("PF('annotationTypeDialog').hide()");
		    		createNew();
			 }
				else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
			}		
		 }
	 }
	 
	 public void cancel() {
		 PrimeFaces.current().executeScript("PF('annotationTypeDialog').hide()");
		 createNew();
	 }

	
	public AnnotationType getAnnotationType() {
		if(annotationType == null) {
			annotationType = new AnnotationType();
		}
		return annotationType;
	}


	public List<AnnotationType> getAnnotationTypeList() {
		if(annotationTypeList == null) annotationTypeList = AnnotationTypeDB.getAnnotationTypeList();
		return annotationTypeList;
	}


	public void setAnnotationTypeList(List<AnnotationType> annotationTypeList) {
		this.annotationTypeList = annotationTypeList;
	}


	public void setAnnotationType(AnnotationType annotationType) {
		this.annotationType = annotationType;
	}
	
	

	public AnnotationGroup getGroup() {
		if(group==null) group = new AnnotationGroup();
		return group;
	}

	public void setGroup(AnnotationGroup group) {
		this.group = group;
	}



	private List<AnnotationType> annotationTypeList;
	private AnnotationType annotationType;
	private AnnotationGroup group; 

 }
