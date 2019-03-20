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

public class RelatedFeature implements java.io.Serializable {
	private Integer relatedFeatureNum;
	private Integer samplingFeatureNum;
	private Integer relatedSamplingFeatureNum;
	private Integer relationshipTypeNum;
	private String samplingFeatureCode;
	private String relatedSamplingFeatureCode;
	private String relationshipTypeCode;
	
	
	
	
	
	public String getRelationshipTypeCode() {
		return relationshipTypeCode;
	}
	public void setRelationshipTypeCode(String relationshipTypeCode) {
		this.relationshipTypeCode = relationshipTypeCode;
	}
	public Integer getRelatedFeatureNum() {
		return relatedFeatureNum;
	}
	public void setRelatedFeatureNum(Integer relatedFeatureNum) {
		this.relatedFeatureNum = relatedFeatureNum;
	}
	public Integer getSamplingFeatureNum() {
		return samplingFeatureNum;
	}
	public void setSamplingFeatureNum(Integer samplingFeatureNum) {
		this.samplingFeatureNum = samplingFeatureNum;
	}
	public Integer getRelatedSamplingFeatureNum() {
		return relatedSamplingFeatureNum;
	}
	public void setRelatedSamplingFeatureNum(Integer relatedSamplingFeatureNum) {
		this.relatedSamplingFeatureNum = relatedSamplingFeatureNum;
	}
	public Integer getRelationshipTypeNum() {
		return relationshipTypeNum;
	}
	public void setRelationshipTypeNum(Integer relationshipTypeNum) {
		this.relationshipTypeNum = relationshipTypeNum;
	}
	public String getSamplingFeatureCode() {
		return samplingFeatureCode;
	}
	public void setSamplingFeatureCode(String samplingFeatureCode) {
		this.samplingFeatureCode = samplingFeatureCode;
	}
	public String getRelatedSamplingFeatureCode() {
		return relatedSamplingFeatureCode;
	}
	public void setRelatedSamplingFeatureCode(String relatedSamplingFeatureCode) {
		this.relatedSamplingFeatureCode = relatedSamplingFeatureCode;
	}

	
    


	
	
}
