package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
select distinct t.annotation_type_num, t.annotation_type_name  from sampling_feature_annotation fa, annotation a, sampling_feature s, annotation_type  t
where  a.annotation_num = fa.annotation_num and s.sampling_feature_num = fa.sampling_feature_num
and s.sampling_feature_type_num = 1 and a.annotation_type_num = t.annotation_type_num
 */

public class Sample implements java.io.Serializable {
		
	public static Integer AlterationType=12;
	public static Integer Rocktexture=16;
	public static Integer SampleComment=22;
	public static Integer RockclassDetail=15;
	public static Integer Alias=11;
	public static Integer Alteration=13;
	public static Integer GeologicAge=26;
	private Integer stationNum;
	private Integer sampleNum;
	private Integer citationNum;
	private String stationName;
	private String samplingFeatureCode;
	private String samplingFeatureName;
	private String samplingFeatureDescription;
//	private String alias;
	private String AliasText;
	private String SampleCommentText;
	private String RockclassDetailText;
	private String RocktextureText;
	private String GeologicAgeText;
	private String AlterationText;
	private String AlterationTypeText;
	private TaxonomicClassifier tc;
	private String igsn;
	private String tcName;  //taxonomic_classifier
	private String tcParentName;  //taxonomic_classifier
	private Integer tcNum;  //taxonomic_classifier
	private Integer tcParentNum;  //taxonomic_classifier


	public Integer getTcNum() {
		return tcNum;
	}
	public void setTcNum(Integer tcNum) {
		this.tcNum = tcNum;
	}
	public Integer getTcParentNum() {
		return tcParentNum;
	}
	public void setTcParentNum(Integer tcParentNum) {
		this.tcParentNum = tcParentNum;
	}
	public String getTcName() {
		return tcName;
	}
	public void setTcName(String tcName) {
		this.tcName = tcName;
	}
	public String getTcParentName() {
		return tcParentName;
	}
	public void setTcParentName(String tcParentName) {
		this.tcParentName = tcParentName;
	}
	public String getIgsn() {
		return igsn;
	}
	public void setIgsn(String igsn) {
		this.igsn = igsn;
	}
	public TaxonomicClassifier getTc() {
		return tc;
	}
	public void setTc(TaxonomicClassifier tc) {
		this.tc = tc;
	}
	public String getAliasText() {
		return AliasText;
	}
	public void setAliasText(String aliasText) {
		AliasText = aliasText;
	}
	public String getSampleCommentText() {
		return SampleCommentText;
	}
	public void setSampleCommentText(String sampleCommentText) {
		SampleCommentText = sampleCommentText;
	}
	public String getRockclassDetailText() {
		return RockclassDetailText;
	}
	public void setRockclassDetailText(String rockclassDetailText) {
		RockclassDetailText = rockclassDetailText;
	}
	public String getRocktextureText() {
		return RocktextureText;
	}
	public void setRocktextureText(String rocktextureText) {
		RocktextureText = rocktextureText;
	}
	public String getGeologicAgeText() {
		return GeologicAgeText;
	}
	public void setGeologicAgeText(String geologicAgeText) {
		GeologicAgeText = geologicAgeText;
	}
	public String getAlterationText() {
		return AlterationText;
	}
	public void setAlterationText(String alterationText) {
		AlterationText = alterationText;
	}
	public String getAlterationTypeText() {
		return AlterationTypeText;
	}
	public void setAlterationTypeText(String alterationTypeText) {
		AlterationTypeText = alterationTypeText;
	}

	public Integer getCitationNum() {
		return citationNum;
	}
	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Integer getStationNum() {
		return stationNum;
	}
	public void setStationNum(Integer stationNum) {
		this.stationNum = stationNum;
	}
	public Integer getSampleNum() {
		return sampleNum;
	}
	public void setSampleNum(Integer sampleNum) {
		this.sampleNum = sampleNum;
	}
	public String getSamplingFeatureCode() {
		if(samplingFeatureCode != null) samplingFeatureCode = samplingFeatureCode.toUpperCase();
		return samplingFeatureCode;
	}
	public void setSamplingFeatureCode(String samplingFeatureCode) {
		if(samplingFeatureCode != null) samplingFeatureCode = samplingFeatureCode.toUpperCase();
		this.samplingFeatureCode = samplingFeatureCode;
	}
	public String getSamplingFeatureName() {
		return samplingFeatureName;
	}
	public void setSamplingFeatureName(String samplingFeatureName) {
		this.samplingFeatureName = samplingFeatureName;
	}
	public String getSamplingFeatureDescription() {
		return samplingFeatureDescription;
	}
	public void setSamplingFeatureDescription(String samplingFeatureDescription) {
		this.samplingFeatureDescription = samplingFeatureDescription;
	}
	
	
	
	
	
	
	
	
	
	

	
	
}
