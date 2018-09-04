package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;


/**
 * Organization generated by hbm2java
 */

public class Organization implements java.io.Serializable {

	private int organizationNum;
	private Organization organization;
	private String organizationCode;
	private String organizationName;
	private String organizationDescription;
	private String organizationLink;
	private String department;
	private Integer organizationUniqueId;
	private String organizationUniqueIdType;
	private String addressPart1;
	private String addressPart2;
	private String city;
	private String zip;
	private Set actions = new HashSet(0);
	private Set organizations = new HashSet(0);
	private Set methods = new HashSet(0);
	private Set affiliations = new HashSet(0);
	private Integer organizationTypeNum=2;
	private int orgId;
	private Integer citationNum;
	private int typeNum;
	private int countryNum = 840;
	private String orgTypeName;	
	private String stateName;
	private String countryName;
	private Integer stateNum;
	private String fullName;
	private String email;

	
	
	public int getCountryNum() {
		return countryNum;
	}

	public void setCountryNum(int countryNum) {
		this.countryNum = countryNum;
	}

	public int getTypeNum() {
		return typeNum;
	}

	public void setTypeNum(int typeNum) {
		this.typeNum = typeNum;
	}

	public Integer getCitationNum() {
		return citationNum;
	}

	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public Organization() {
	}


	public int getOrganizationNum() {
		return this.organizationNum;
	}

	public void setOrganizationNum(int organizationNum) {
		this.organizationNum = organizationNum;
	}


	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationDescription() {
		return this.organizationDescription;
	}

	public void setOrganizationDescription(String organizationDescription) {
		this.organizationDescription = organizationDescription;
	}

	public String getOrganizationLink() {
		return this.organizationLink;
	}

	public void setOrganizationLink(String organizationLink) {
		this.organizationLink = organizationLink;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getOrganizationUniqueId() {
		return this.organizationUniqueId;
	}

	public void setOrganizationUniqueId(Integer organizationUniqueId) {
		this.organizationUniqueId = organizationUniqueId;
	}

	public String getOrganizationUniqueIdType() {
		return this.organizationUniqueIdType;
	}

	public void setOrganizationUniqueIdType(String organizationUniqueIdType) {
		this.organizationUniqueIdType = organizationUniqueIdType;
	}

	public String getAddressPart1() {
		return this.addressPart1;
	}

	public void setAddressPart1(String addressPart1) {
		this.addressPart1 = addressPart1;
	}

	public String getAddressPart2() {
		return this.addressPart2;
	}

	public void setAddressPart2(String addressPart2) {
		this.addressPart2 = addressPart2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Set getActions() {
		return this.actions;
	}

	public void setActions(Set actions) {
		this.actions = actions;
	}

	public Set getOrganizations() {
		return this.organizations;
	}

	public void setOrganizations(Set organizations) {
		this.organizations = organizations;
	}

	public Set getMethods() {
		return this.methods;
	}

	public void setMethods(Set methods) {
		this.methods = methods;
	}

	public Set getAffiliations() {
		return this.affiliations;
	}

	public void setAffiliations(Set affiliations) {
		this.affiliations = affiliations;
	}

	public Integer getOrganizationTypeNum() {
		return organizationTypeNum;
	}

	public void setOrganizationTypeNum(Integer organizationTypeNum) {
		this.organizationTypeNum = organizationTypeNum;
	}
	
	public String getOrgTypeName() {
		return orgTypeName;
	}

	public void setOrgTypeName(String orgTypeName) {
		this.orgTypeName = orgTypeName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getStateNum() {
		return stateNum;
	}

	public void setStateNum(Integer stateNum) {
		this.stateNum = stateNum;
	}

	public String getFullName() {
		if(organizationName != null) fullName=organizationName;
		if(department != null) fullName=organizationName+", "+department;
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
}
