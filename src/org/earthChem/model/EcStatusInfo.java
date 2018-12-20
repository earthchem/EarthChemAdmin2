package org.earthChem.model;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.Date;

public class EcStatusInfo implements java.io.Serializable {

	private int ecStatusInfoNum;
	private Citation citation;
	private String ecStatusInfoName;
	private Date dataEnteredDate;
	private String dataStatus;
	private String publicComment;
	private String internalComment;

	public EcStatusInfo() {
	}

	public EcStatusInfo(int ecStatusInfoNum, Citation citation,
			String ecStatusInfoName, Date dataEnteredDate, String dataStatus,
			String publicComment, String internalComment) {
		this.ecStatusInfoNum = ecStatusInfoNum;
		this.citation = citation;
		this.ecStatusInfoName = ecStatusInfoName;
		this.dataEnteredDate = dataEnteredDate;
		this.dataStatus = dataStatus;
		this.publicComment = publicComment;
		this.internalComment = internalComment;
	}


	public int getEcStatusInfoNum() {
		return this.ecStatusInfoNum;
	}

	public void setEcStatusInfoNum(int ecStatusInfoNum) {
		this.ecStatusInfoNum = ecStatusInfoNum;
	}

	public Citation getCitation() {
		return this.citation;
	}

	public void setCitation(Citation citation) {
		this.citation = citation;
	}

	public String getEcStatusInfoName() {
		return this.ecStatusInfoName;
	}

	public void setEcStatusInfoName(String ecStatusInfoName) {
		this.ecStatusInfoName = ecStatusInfoName;
	}

	public Date getDataEnteredDate() {
		return this.dataEnteredDate;
	}

	public void setDataEnteredDate(Date dataEnteredDate) {
		this.dataEnteredDate = dataEnteredDate;
	}

	public String getDataStatus() {
		return this.dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getPublicComment() {
		return this.publicComment;
	}

	public void setPublicComment(String publicComment) {
		this.publicComment = publicComment;
	}

	public String getInternalComment() {
		return this.internalComment;
	}

	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}

}
