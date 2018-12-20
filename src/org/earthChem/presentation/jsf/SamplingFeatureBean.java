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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.earthChem.db.AnnotationDB;
import org.earthChem.db.DBUtil;
import org.earthChem.db.StationDB;
import org.earthChem.db.postgresql.hbm.Annotation;
import org.earthChem.db.postgresql.hbm.FeatureOfInterest;
import org.earthChem.db.postgresql.hbm.Method;
import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.db.postgresql.hbm.Station;
import org.earthChem.presentation.jsf.theme.Theme;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

 
@ManagedBean(name="sfBean")
@SessionScoped
public class SamplingFeatureBean implements Serializable {
	
	public void lookup() {
//	System.out.println("bc-lk "+	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("database"));
		String code = search.getSamplingFeatureCode();
		if(code==null || "".equals(code.trim())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("WARN!",  "The code is required!") );
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("sfCode",code);
		}
		
	}	
	
	public void createNew() {
		if(search.getSamplingFeatureTypeNum() == 1) {
			PrimeFaces.current().executeScript("PF('sampleDialog').show()");
		} else {
			PrimeFaces.current().executeScript("PF('stationDialog').show()");
		}		
	}	
	
	public SamplingFeature getSearch() {
		if(search == null) search = new SamplingFeature();
		return search;
	}


	public void setSearch(SamplingFeature search) {
		this.search = search;
	}

	

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	


	public Integer getSfNum() {
		return sfNum;
	}

	public void setSfNum(Integer sfNum) {
		this.sfNum = sfNum;
	}




	private SamplingFeature search;
	private String page="empty";
	private Integer sfNum;
/*	
	public void createNew() {
		station = new Station();
		search = null;
		geograph = new FeatureOfInterest();
		tectonicSetting=new Annotation();
	
	}
	
	
	
	public void selectStation() {
		station.setFoiList(StationDB.getGeograph(station.getSamplingFeatureNum()));
		geograph = new FeatureOfInterest();
		station.setTsList(AnnotationDB.getAnnotationList(station.getSamplingFeatureNum(), 62));
	}
	
	public void deleteGeograph(FeatureOfInterest foi){
		Integer foiNum = foi.getFeatureOfInterestNum();
		if(foiNum != null) {
			DBUtil.update("DELETE FROM feature_of_interest WHERE feature_of_interest_num ="+foiNum);
		} 
		List<FeatureOfInterest> list = station.getFoiList();
		list.remove(foi);
		station.setFoiList(list);
	}
	
	public void deleteTectonicSetting(Annotation ts){
		Integer sfNum = station.getSamplingFeatureNum();
		if(sfNum !=null) {
			AnnotationDB.deleteSamplingFeatureAnnotation(sfNum, 62, ts.getDataSourceNum());
			station.setTsList(AnnotationDB.getAnnotationList(station.getSamplingFeatureNum(), 62));
		}
		else station.getTsList().remove(ts);
	}
	
	public void addTectonicSetting(){
		Integer sfNum = station.getSamplingFeatureNum();
		if(sfNum !=null) {
			AnnotationDB.addSamplingFeatureAnnotation(sfNum, 62, tectonicSetting.getDataSourceNum(),tectonicSetting.getAnnotationText());
			station.setTsList(AnnotationDB.getAnnotationList(station.getSamplingFeatureNum(), 62));
		}
		else {
			List<Annotation> list = station.getTsList();
			if(list==null) list = new ArrayList<Annotation>();
			list.add(tectonicSetting);
			station.setTsList(list);
			tectonicSetting = new Annotation();
		}

	}
	
	
	public void addGeograph(){
		if(station.getSamplingFeatureNum() != null) {
			StationDB.addGeograph(station.getSamplingFeatureNum(),geograph);
		}
		
		if(typeMap==null) {
			typeMap = DBUtil.getIntegerStringMap("select feature_of_interest_type_num, feature_of_interest_type_name from feature_of_interest_type where feature_of_interest_type_description <> 'sesar' order by feature_of_interest_type_name");
		}
		String typeLabel = typeMap.get(geograph.getFeatureOfInterestTypeNum());
		geograph.setFeatureOfInterestTypeName(typeLabel);
		List<FeatureOfInterest> list = station.getFoiList();
		if(list == null) list = new ArrayList<FeatureOfInterest>();
		list.add(geograph);
		station.setFoiList(list);	
		geograph = new FeatureOfInterest();
	}
	
	public void update() {
		if("LINE".equals(station.getSamplingFeatureGeotype()) &&
		(station.getLat2()==null || station.getLong2() == null)) {
			FacesContext.getCurrentInstance().addMessage("stationEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Please fill up Geometry 2!"));
		} else {
			String status = StationDB.update(station);
			if(status == null) {
				station = new Station();
				PrimeFaces.current().executeScript("PF('stationDialog').hide()");
				PrimeFaces.current().executeScript("PF('stationTableWidgetVar').filter()");
			} else {
				FacesContext.getCurrentInstance().addMessage("stationEditMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", status));
			}	
		}
	}
	
	public void cancel() {
		PrimeFaces.current().executeScript("PF('stationDialog').hide()");
		station = new Station();
	}
	
	public Station getStation() {
		if(station==null) station = new Station();
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	public List<Station> getStationList() {
		return stationList;
	}
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}

	
	
	public FeatureOfInterest getGeograph() {
		if(geograph==null) geograph = new FeatureOfInterest();
		return geograph;
	}

	public void setGeograph(FeatureOfInterest geograph) {
		this.geograph = geograph;
	}



	public Map<Integer, String> getTypeMap() {
		if(typeMap==null) {
			typeMap = DBUtil.getIntegerStringMap("select feature_of_interest_type_num, feature_of_interest_type_name from feature_of_interest_type where feature_of_interest_type_description <> 'sesar' order by feature_of_interest_type_name");
		}
		return typeMap;
	}
	
	public Annotation getTectonicSetting() {
		if(tectonicSetting==null) tectonicSetting=new Annotation();
		return tectonicSetting;
	}

	public void setTectonicSetting(Annotation tectonicSetting) {
		this.tectonicSetting = tectonicSetting;
	}




	private Station station;
	private List<Station> stationList;
	private String search;
	private FeatureOfInterest geograph;
	private Map<Integer, String> typeMap; //geograph
	private Annotation tectonicSetting;
	*/
	
 }
