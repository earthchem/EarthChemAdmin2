package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;


/**
 * Person generated by hbm2java
 */

public class Person implements java.io.Serializable {

	private int personNum;
	private Integer affiliationNum;
	private String firstName;
	private String middleName;
	private String lastName;
	private int pnum=1;
	private Set affiliations = new HashSet(0);
	private Set authorLists = new HashSet(0);
	private Set personExternalIdentifiers = new HashSet(0);
	private String fullName;
	private Organization organization;
	private String citations;


	public Person() {
	}
	
	public Person(int personNum, String firstName, String lastName, int pnum) {
		this.personNum = personNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.pnum = pnum;
	}

	public Person(int personNum, String firstName, String middleName,
			String lastName, int pnum, Set affiliations, Set authorLists,
			Set personExternalIdentifiers) {
		this.personNum = personNum;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.pnum = pnum;
		this.affiliations = affiliations;
		this.authorLists = authorLists;
		this.personExternalIdentifiers = personExternalIdentifiers;
	}

	

	public String getCitations() {
		return citations;
	}

	public void setCitations(String citations) {
		this.citations = citations;
	}

	public Integer getAffiliationNum() {
		return affiliationNum;
	}

	public void setAffiliationNum(Integer affiliationNum) {
		this.affiliationNum = affiliationNum;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public int getPersonNum() {
		return this.personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}


	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		if(firstName != null) firstName = firstName.toUpperCase();
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		if(middleName!= null) middleName = middleName.toUpperCase();
		this.middleName = middleName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		if(lastName !=null) lastName = lastName.toUpperCase();
		this.lastName = lastName;
	}

	public int getPnum() {
		return this.pnum;
	}

	public void setPnum(int pnum) {
		this.pnum = pnum;
	}

	public Set getAffiliations() {
		return this.affiliations;
	}

	public void setAffiliations(Set affiliations) {
		this.affiliations = affiliations;
	}

	public Set getAuthorLists() {
		return this.authorLists;
	}

	public void setAuthorLists(Set authorLists) {
		this.authorLists = authorLists;
	}

	public Set getPersonExternalIdentifiers() {
		return this.personExternalIdentifiers;
	}

	public void setPersonExternalIdentifiers(Set personExternalIdentifiers) {
		this.personExternalIdentifiers = personExternalIdentifiers;
	}
	
	public String getFullName() {
		if(fullName == null && (firstName != null || lastName != null)) {
			if(middleName==null || !middleName.trim().equals("")) {
				fullName = firstName+" "+lastName;
			} else {
				fullName = firstName+" "+middleName+" "+lastName;
			}
		} 
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}