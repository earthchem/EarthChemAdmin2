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
import org.earthChem.presentation.jsf.SampleBean.ColumnModel;
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

	public void selectSample() {
		viewAnnList = AnnotationDB.getTephraAnnotationView(sample.getSampleNum());
		tcList= TephraDB.getTaxonomicClassifier(sample.getSampleNum(), sample.getMaterialNum());
		getTcOptions();
	}
	
	public SelectItem[] getTcOptions()
	{
		Integer merialNum = sample.getMaterialNum();
		if(merialNum != null && merialNum == 6) {
			tcOptions = (new HtmlOptions()).getMinTaxonomicClassifiers();
		}
		else tcOptions = (new HtmlOptions()).getRockTaxonomicClassifiers();
		return tcOptions;
	}
	
	public void save() {		
		String status = TephraDB.update(sample);
		if(status == null) {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void cancel() {
		PrimeFaces.current().executeScript("PF('sampleDialog').hide()");
		sample = new Sample();
	}
	
//-------------  annotation ------------------	
	public void addAnnotation() {
		String status = AnnotationDB.addTephraAnnotation(sample.getSampleNum(),  citationNum, editedAnnList);
		if(status == null) {
			viewAnnList = AnnotationDB.getTephraAnnotationView(sample.getSampleNum());
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
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
	
//------------------ tc --------------------	
	public void deleteTc(Integer bridgeNum) {
		String status = SampleDB.deleteSamplingTaxonomicClassifier(bridgeNum);
		if(status == null) {
			tcList= SampleDB.getTaxonomicClassifier(sample.getSampleNum());
			taxonomicClassifier = new TaxonomicClassifier();
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void addTc() {
		String status = TephraDB.addTaxonomicClassifier(taxonomicClassifier, sample.getSampleNum());
		if(status == null) {
			tcList= TephraDB.getTaxonomicClassifier(sample.getSampleNum(), sample.getMaterialNum());
			taxonomicClassifier = new TaxonomicClassifier();
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
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
	
	public List<TaxonomicClassifier> getTcList() {
	//   if(sample != null)	tcList= SampleDB.getTaxonomicClassifier(sample.getSampleNum());
		return tcList;
	}

	public void setTcList(List<TaxonomicClassifier> tcList) {
		this.tcList = tcList;
	}	
	

	public List<Annotation> getEditedAnnList() {		
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

	public TaxonomicClassifier getTaxonomicClassifier() {
		if(taxonomicClassifier==null) taxonomicClassifier = new TaxonomicClassifier();
		return taxonomicClassifier;
	}

	public void setTaxonomicClassifier(TaxonomicClassifier taxonomicClassifier) {
		this.taxonomicClassifier = taxonomicClassifier;
	}



	private Sample sample;
	private List<Sample> sampleList;
	private String search;
	private List<String[]> annotationList;
	private List<Annotation> editedAnnList;
	private List<Annotation> viewAnnList;
	private Annotation annotation;
	private Integer citationNum;
	private List<TaxonomicClassifier> tcList;
	private TaxonomicClassifier taxonomicClassifier;
	private SelectItem[] tcOptions;
	  
}
