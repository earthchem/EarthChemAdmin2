package org.earthChem.model;

import java.util.ArrayList;

// Generated Jul 30, 2014 4:07:06 PM by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.earthChem.presentation.jsf.theme.Theme;


public class EntityStatistics implements java.io.Serializable {

	private Integer statistics_num;
	private Integer citations;
	private Integer datasets;	
	private Integer stations;
	private Integer expeditions;
	private Integer specimens;
	private Integer total_chem_values;
	private Integer rock_values;
	private Integer mineral_values;
	private Integer volc_glass_values; 
	private Integer inclusion_values; 	
	private Date ts;
	public Integer getStatistics_num() {
		return statistics_num;
	}
	public void setStatistics_num(Integer statistics_num) {
		this.statistics_num = statistics_num;
	}
	public Integer getCitations() {
		return citations;
	}
	public void setCitations(Integer citations) {
		this.citations = citations;
	}
	public Integer getDatasets() {
		return datasets;
	}
	public void setDatasets(Integer datasets) {
		this.datasets = datasets;
	}
	public Integer getStations() {
		return stations;
	}
	public void setStations(Integer stations) {
		this.stations = stations;
	}
	public Integer getExpeditions() {
		return expeditions;
	}
	public void setExpeditions(Integer expeditions) {
		this.expeditions = expeditions;
	}
	public Integer getSpecimens() {
		return specimens;
	}
	public void setSpecimens(Integer specimens) {
		this.specimens = specimens;
	}
	public Integer getTotal_chem_values() {
		return total_chem_values;
	}
	public void setTotal_chem_values(Integer total_chem_values) {
		this.total_chem_values = total_chem_values;
	}
	public Integer getRock_values() {
		return rock_values;
	}
	public void setRock_values(Integer rock_values) {
		this.rock_values = rock_values;
	}
	public Integer getMineral_values() {
		return mineral_values;
	}
	public void setMineral_values(Integer mineral_values) {
		this.mineral_values = mineral_values;
	}
	public Integer getVolc_glass_values() {
		return volc_glass_values;
	}
	public void setVolc_glass_values(Integer volc_glass_values) {
		this.volc_glass_values = volc_glass_values;
	}
	public Integer getInclusion_values() {
		return inclusion_values;
	}
	public void setInclusion_values(Integer inclusion_values) {
		this.inclusion_values = inclusion_values;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
	
	


	
}
