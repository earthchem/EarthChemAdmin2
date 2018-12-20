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
import org.earthChem.db.postgresql.hbm.Annotation;
import org.earthChem.db.postgresql.hbm.FeatureOfInterest;
import org.earthChem.db.postgresql.hbm.Method;
import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.db.postgresql.hbm.Station;
import org.earthChem.db.postgresql.hbm.TaxonomicClassifier;
import org.earthChem.db.postgresql.hbm.Sample;
import org.earthChem.presentation.jsf.theme.Theme;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

 
@ManagedBean(name="sampleBean")
@SessionScoped
public class SampleBean implements Serializable {
	
	public void createNew() {
			sample = new Sample();
			annotation = new Annotation();
			delAnnotation =  new Annotation();
			annotationList = new ArrayList<String[]>();
	}
	
	public void lookup() {	
		if(search != null && !"".equals(search)) {
			search = search.trim();
			sampleList = SampleDB.getSampleList(search);
			if(sampleList.size()==0) {
				FacesContext.getCurrentInstance().addMessage("sampleListMsg", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error!", "The sample was not found!"));
			} else {
				PrimeFaces.current().executeScript("PF('sampleTableWidgetVar').filter()");
			}
		}
	}
	

	public void findStation(String code) {		
		if(code != null && !"".equals(code)) {
			Station tmp = StationDB.getStation(code);
			if(tmp != null) {
				sample.setStationNum(tmp.getSamplingFeatureNum());
				sample.setStationName(tmp.getSamplingFeatureCode());
			}
		}
	}


	public void selectSample() {
		annotationHeads = SampleDB.getAnnotationHeads(sample.getSampleNum()); 
		if(annotationHeads != null) {
			annotationList = SampleDB.getAnnotationList(annotationHeads,sample.getSampleNum());
			columns = new ArrayList<ColumnModel>();
			for(String s: annotationHeads) columns.add(new ColumnModel(s,s));
		}
	}
	
	public void addAnnotation() {
		String status = AnnotationDB.addSamplingFeatureAnnotation(sample.getSampleNum(),
				annotation.getAnnotationTypeNum(),annotation.getDataSourceNum(),annotation.getAnnotationText());
		if(status == null) {
			annotation = new Annotation();
			delAnnotation =  new Annotation();
			annotationList = SampleDB.getAnnotationList(annotationHeads,sample.getSampleNum());
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	
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
		String status = SampleDB.addTaxonomicClassifier(taxonomicClassifier, sample.getSampleNum());
		if(status == null) {
			tcList= SampleDB.getTaxonomicClassifier(sample.getSampleNum());
			taxonomicClassifier = new TaxonomicClassifier();
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The data were saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
		}	
	}
	
	public void update() {
		String code = sample.getStationName();
		if(code != null && !"".equals(code)) {
				Station tmp = StationDB.getStation(code);
				if(tmp != null) {
					sample.setStationNum(tmp.getSamplingFeatureNum());
					sample.setStationName(tmp.getSamplingFeatureCode());
				} else {
					FacesContext.getCurrentInstance().addMessage("sampleEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "The station, "+code+" is not found in database!"));
					return;
				}
		}	
		
		String status = SampleDB.update(sample);
		if(status == null) {
			sample = new Sample();
			PrimeFaces.current().executeScript("PF('sampleDialog').hide()");
			PrimeFaces.current().executeScript("PF('sampleTableWidgetVar').filter()");
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
		if(sfCode != null) sampleList = SampleDB.getSampleList(sfCode);
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

	
	
	
	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
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


	public List<String> getAnnotationHeads() {
		return annotationHeads;
	}

	public void setAnnotationHeads(List<String> annotationHeads) {
		this.annotationHeads = annotationHeads;
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
	
	
	public Annotation getDelAnnotation() {
		if(delAnnotation == null) delAnnotation = new Annotation();
		return delAnnotation;
	}


	public void setDelAnnotation(Annotation delAnnotation) {
		this.delAnnotation = delAnnotation;
	}

	
	public List<TaxonomicClassifier> getTcList() {
		tcList= SampleDB.getTaxonomicClassifier(sample.getSampleNum());
		return tcList;
	}

	public void setTcList(List<TaxonomicClassifier> tcList) {
		this.tcList = tcList;
	}	

	
	
	public TaxonomicClassifier getTaxonomicClassifier() {
		if(taxonomicClassifier==null) taxonomicClassifier = new TaxonomicClassifier();
		return taxonomicClassifier;
	}

	public void setTaxonomicClassifier(TaxonomicClassifier taxonomicClassifier) {
		this.taxonomicClassifier = taxonomicClassifier;
	}

	

	public boolean isPetdb() {
		return isPetdb;
	}

	public void setPetdb(boolean isPetdb) {
		this.isPetdb = isPetdb;
	}



	private Sample sample;
	private List<Sample> sampleList;
	private String search;
	private List<ColumnModel> columns;
	private List<String[]> annotationList;
	private List<String> annotationHeads;
	private Annotation annotation;
	private Annotation delAnnotation;
	private List<TaxonomicClassifier> tcList;
	private TaxonomicClassifier taxonomicClassifier;
	private boolean isPetdb = false;
	  
	
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

 }
