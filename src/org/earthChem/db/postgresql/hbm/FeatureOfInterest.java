package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;


public class FeatureOfInterest implements java.io.Serializable {

	private Integer samplingFeatureNum;
	private Integer featureOfInterestNum;
	private Integer featureOfInterestCvNum;
	private String featureOfInterestCvName;
	private String featureOfInterestDescription;
	private Integer featureOfInterestTypeNum;
	private String featureOfInterestTypeName;
	private String featureOfInterestTypeDescription;
	private String featureOfInterestCvDescription;
	
	
	public String getCvInfo() { 
	    return featureOfInterestCvName+ ","+ featureOfInterestCvDescription;
	} 
	
	public String getFeatureOfInterestCvDescription() {
		return featureOfInterestCvDescription;
	}
	public void setFeatureOfInterestCvDescription(String featureOfInterestCvDescription) {
		this.featureOfInterestCvDescription = featureOfInterestCvDescription;
	}
	public Integer getSamplingFeatureNum() {
		return samplingFeatureNum;
	}
	public void setSamplingFeatureNum(Integer samplingFeatureNum) {
		this.samplingFeatureNum = samplingFeatureNum;
	}
	public Integer getFeatureOfInterestNum() {
		return featureOfInterestNum;
	}
	public void setFeatureOfInterestNum(Integer featureOfInterestNum) {
		this.featureOfInterestNum = featureOfInterestNum;
	}
	public Integer getFeatureOfInterestTypeNum() {
		return featureOfInterestTypeNum;
	}
	public void setFeatureOfInterestTypeNum(Integer featureOfInterestTypeNum) {
		this.featureOfInterestTypeNum = featureOfInterestTypeNum;
	}
	public String getFeatureOfInterestTypeName() {
		return featureOfInterestTypeName;
	}
	public void setFeatureOfInterestTypeName(String featureOfInterestTypeName) {
		if(featureOfInterestTypeName != null) featureOfInterestTypeName = featureOfInterestTypeName.toUpperCase();
		this.featureOfInterestTypeName = featureOfInterestTypeName;
	}
	public String getFeatureOfInterestTypeDescription() {
		return featureOfInterestTypeDescription;
	}
	public void setFeatureOfInterestTypeDescription(String featureOfInterestTypeDescription) {
		this.featureOfInterestTypeDescription = featureOfInterestTypeDescription;
	}
	public Integer getFeatureOfInterestCvNum() {
		return featureOfInterestCvNum;
	}
	public void setFeatureOfInterestCvNum(Integer featureOfInterestCvNum) {
		this.featureOfInterestCvNum = featureOfInterestCvNum;
	}
	public String getFeatureOfInterestCvName() {
		return featureOfInterestCvName;
	}
	public void setFeatureOfInterestCvName(String featureOfInterestCvName) {
		if(featureOfInterestCvName !=null) featureOfInterestCvName = featureOfInterestCvName.toUpperCase();
		this.featureOfInterestCvName = featureOfInterestCvName;
	}
	public String getFeatureOfInterestDescription() {
		return featureOfInterestDescription;
	}
	public void setFeatureOfInterestDescription(String featureOfInterestDescription) {
		this.featureOfInterestDescription = featureOfInterestDescription;
	}
	
	
}
