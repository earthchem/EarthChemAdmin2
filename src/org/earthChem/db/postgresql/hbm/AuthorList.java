package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0


/**
 * AuthorList generated by hbm2java
 */

public class AuthorList implements java.io.Serializable {

	private int authorListNum;
	private Citation citation;
	private Person person;
	private int authorOrder;
	private Integer citationNum;
	private String fullName;
	private Integer personNum;

	public AuthorList() {
	}

	public AuthorList(int authorListNum, Citation citation, Person person,
			int authorOrder) {
		this.authorListNum = authorListNum;
		this.citation = citation;
		this.person = person;
		this.authorOrder = authorOrder;
	}

	public int getAuthorListNum() {
		return this.authorListNum;
	}

	public void setAuthorListNum(int authorListNum) {
		this.authorListNum = authorListNum;
	}

	public Citation getCitation() {
		return this.citation;
	}

	public void setCitation(Citation citation) {
		this.citation = citation;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getAuthorOrder() {
		return this.authorOrder;
	}

	public void setAuthorOrder(int authorOrder) {
		this.authorOrder = authorOrder;
	}

	public Integer getCitationNum() {
		return citationNum;
	}

	public void setCitationNum(Integer citationNum) {
		this.citationNum = citationNum;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	
}
