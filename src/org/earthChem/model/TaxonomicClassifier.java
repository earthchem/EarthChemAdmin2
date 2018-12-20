package org.earthChem.model;

public class TaxonomicClassifier implements java.io.Serializable {

	private Integer classifierNum;
	private Integer parentNum;
	private Integer classifierTypeCv;
	private String classifierName;
	private String classifierCommonName;
	private String classifierDescription;
	private String parentName;
	private Integer citationNum;
	private Integer bridgeNum;
	
	
	
	
	public String getClassifierName() {
		return classifierName;
	}
	public void setClassifierName(String classifierName) {
		this.classifierName = classifierName;
	}
	public Integer getCitationNum() {
		return citationNum;
	}
	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}
	public Integer getBridgeNum() {
		return bridgeNum;
	}
	public void setBridgeNum(Integer bridgeNum) {
		this.bridgeNum = bridgeNum;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Integer getClassifierNum() {
		return classifierNum;
	}
	public void setClassifierNum(Integer classifierNum) {
		this.classifierNum = classifierNum;
	}
	public Integer getParentNum() {
		return parentNum;
	}
	public void setParentNum(Integer parentNum) {
		this.parentNum = parentNum;
	}
	public Integer getClassifierTypeCv() {
		return classifierTypeCv;
	}
	public void setClassifierTypeCv(Integer classifierTypeCv) {
		this.classifierTypeCv = classifierTypeCv;
	}

	public String getClassifierCommonName() {
		return classifierCommonName;
	}
	public void setClassifierCommonName(String classifierCommonName) {
		this.classifierCommonName = classifierCommonName;
	}
	public String getClassifierDescription() {
		return classifierDescription;
	}
	public void setClassifierDescription(String classifierDescription) {
		this.classifierDescription = classifierDescription;
	}
	
	
}
