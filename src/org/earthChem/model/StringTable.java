package org.earthChem.model;


import java.util.ArrayList;

public class StringTable implements java.io.Serializable {

	private String[] heads;
	private ArrayList<Object[]> data;
	
	
	public String[] getHeads() {
		return heads;
	}
	public void setHeads(String[] heads) {
		this.heads = heads;
	}
	public ArrayList<Object[]> getData() {
		return data;
	}
	public void setData(ArrayList<Object[]> data) {
		this.data = data;
	}
	
	
	
	
	
}
