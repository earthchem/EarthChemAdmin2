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
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.earthChem.db.AnnotationDB;
import org.earthChem.db.DBUtil;
import org.earthChem.db.SampleDB;
import org.earthChem.db.StationDB;
import org.earthChem.db.TephraDB;
import org.earthChem.model.Annotation;
import org.earthChem.model.FeatureOfInterest;
import org.earthChem.model.Method;
import org.earthChem.model.Sample;
import org.earthChem.model.SamplingFeature;
import org.earthChem.model.Station;
import org.earthChem.model.TaxonomicClassifier;
import org.earthChem.presentation.jsf.theme.Theme;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

 
@ManagedBean(name="sampleBean2")
@SessionScoped
public class SampleBean2 implements Serializable {
	
	public void createNew() {
			sample = new Sample();
			annotation = new Annotation();
			annotationList = new ArrayList<String[]>();
	}
	
	public void deleteAnnotation(Integer sfAnnotationNum) {
		String status = AnnotationDB.deleteTephraAnnotation(sfAnnotationNum);
		if(status == null) {
			viewAnnList = AnnotationDB.getTephraAnnotationView(sample.getSampleNum());
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were deleted!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	

	public void selectSample() {
		viewAnnList = AnnotationDB.getTephraAnnotationView(sample.getSampleNum());
	}
	
	public void addAnnotation() {
		String status = AnnotationDB.addTephraAnnotation(sample.getSampleNum(),  citationNum, editedAnnList);
		if(status == null) {
			viewAnnList = AnnotationDB.getTephraAnnotationView(sample.getSampleNum());
	//		annotation = new Annotation();
	//		delAnnotation =  new Annotation();
	//		annotationList = SampleDB.getAnnotationList(annotationHeads,sample.getSampleNum());
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
/*	
	public void deleteAnnotation() {
		String status = AnnotationDB.deleteSamplingFeatureAnnotation(sample.getSampleNum(),
				delAnnotation.getAnnotationTypeNum(),delAnnotation.getDataSourceNum());
		if(status == null) {
			annotation = new Annotation();
			delAnnotation =  new Annotation();
			annotationList = SampleDB.getAnnotationList(annotationHeads,sample.getSampleNum());
			createNew();
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were deleted!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
*/	
	public void save() {		
		String status = TephraDB.update(sample);
		if(status == null) {
	//		sample = new Sample();
	//		PrimeFaces.current().executeScript("PF('sampleDialog').hide()");
	//		PrimeFaces.current().executeScript("PF('sampleTableWidgetVar').filter()");
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void cancel() {
		PrimeFaces.current().executeScript("PF('sampleDialog').hide()");
		sample = new Sample();
	}
	
	public Sample getSample() {
		if(sample==null) {
			sample = new Sample();
			sample.setStationName(search);
		}
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	public List<Sample> getSampleList() {
		String sfCode =(String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("sfCode");
		if(sfCode != null) sampleList = TephraDB.getSampleList(sfCode);
		return sampleList;
	}
	public void setSampleList(List<Sample> sampleList) {
		this.sampleList = sampleList;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}

	public List<String[]> getAnnotationList() {
		if(annotationList==null) annotationList = new ArrayList<String[]>();
		return annotationList;
	}

	public void setAnnotationList(List<String[]> annotationList) {
		this.annotationList = annotationList;
	}

	


	public Annotation getAnnotation() {
		if(annotation == null) annotation = new Annotation();
		return annotation;
	}


	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}


	public SelectItem[] getAnnotationTypes() {
		String q ="select distinct a.annotation_type_num, annotation_type_name from annotation_type a, annotation_type_group g "+
				" where a.annotation_type_num = g.annotation_type_num and g.annotation_group_num = 2 order by annotation_type_name";
		return DBUtil.getSelectItems(q);
	}
	
	public SelectItem[] getCitations() {
	String q ="select distinct a.data_source_num, a.data_source_num  from sampling_feature_annotation f, annotation a where a.annotation_num = f.annotation_num and f.sampling_feature_num ="+sample.getSampleNum();
		return DBUtil.getSelectItems(q);
	}
	
	/*
	public Annotation getDelAnnotation() {
		if(delAnnotation == null) delAnnotation = new Annotation();
		return delAnnotation;
	}


	public void setDelAnnotation(Annotation delAnnotation) {
		this.delAnnotation = delAnnotation;
	}
	*/

	

	public List<Annotation> getEditedAnnList() {		
	//	editedAnnList = new AnnotationDB.getTephraAnnotations();
		return editedAnnList = AnnotationDB.getTephraAnnotations();
	}

	public void setEditedAnnList(List<Annotation> editedAnnList) {
		this.editedAnnList = editedAnnList;
  
	}



	public Integer getCitationNum() {
		return citationNum;
	}

	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}

	


	public List<Annotation> getViewAnnList() {
		return viewAnnList;
	}

	public void setViewAnnList(List<Annotation> viewAnnList) {
		this.viewAnnList = viewAnnList;
	}




	private Sample sample;
	private List<Sample> sampleList;
	private String search;
//	private List<ColumnModel> columns;
	private List<String[]> annotationList;
	private List<Annotation> editedAnnList;
	private List<Annotation> viewAnnList;
	private Annotation annotation;
	private Integer citationNum;

	  
/*	
	static public class ColumnModel implements Serializable {
		 
        private String header;
        private String property;
        
        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }
 
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }
    }
*/
 }
