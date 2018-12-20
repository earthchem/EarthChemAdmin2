package org.earthChem.model;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Method generated by hbm2java
 */

public class AnnotationType implements java.io.Serializable {

	private Integer annotationTypeNum;
	private String annotationTypeName;
	private String groupId;
	//private String groupArray;
	private String groupNames;
	private List<AnnotationGroup> groups;
	private List<Integer> groupNums;
	
	

	public List<Integer> getGroupNums() {
		return groupNums;
	}
	public void setGroupNums(List<Integer> groupNums) {
		this.groupNums = groupNums;
	}
	public String getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}
	public List<AnnotationGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<AnnotationGroup> groups) {
		this.groups = groups;
	}
	
	
	public Integer getAnnotationTypeNum() {
		return annotationTypeNum;
	}
	public void setAnnotationTypeNum(Integer annotationTypeNum) {
		this.annotationTypeNum = annotationTypeNum;
	}
	public String getAnnotationTypeName() {
		return annotationTypeName;
	}
	public void setAnnotationTypeName(String annotationTypeName) {
		this.annotationTypeName = annotationTypeName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
	
	
	
}