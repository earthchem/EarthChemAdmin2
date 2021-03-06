package org.earthChem.db.postgresql.hbm;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0



/**
 * CitationExternalIdentifier generated by hbm2java
 */
public class CitationExternalIdentifier implements java.io.Serializable {

	private int bridgeNum;
	private Citation citation;
	private String citationExternalIdentifier;
	private String citationExternalIdentifierUri;

	public CitationExternalIdentifier() {
	}

	public int getBridgeNum() {
		return this.bridgeNum;
	}

	public void setBridgeNum(int bridgeNum) {
		this.bridgeNum = bridgeNum;
	}


	public Citation getCitation() {
		return this.citation;
	}

	public void setCitation(Citation citation) {
		this.citation = citation;
	}
	
	public String getCitationExternalIdentifier() {
		return this.citationExternalIdentifier;
	}

	public void setCitationExternalIdentifier(String citationExternalIdentifier) {
		this.citationExternalIdentifier = citationExternalIdentifier;
	}

	
	public String getCitationExternalIdentifierUri() {
		return this.citationExternalIdentifierUri;
	}

	public void setCitationExternalIdentifierUri(
			String citationExternalIdentifierUri) {
		this.citationExternalIdentifierUri = citationExternalIdentifierUri;
	}

}
