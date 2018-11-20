package org.earthChem.presentation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.earthChem.db.DBUtil;
import org.earthChem.db.EntityStatisticsDB;
import org.earthChem.db.VariableDB;
import org.earthChem.db.postgresql.hbm.EntityStatistics;
import org.earthChem.db.postgresql.hbm.Variable;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

 
@ManagedBean(name="refreshViewBean")
@SessionScoped
public class RefreshViewBean implements Serializable {

	public void refreshView() {
		String status = null;
		status = refreshOne("mv_citation_summary");
		if(status == null) status = refreshOne("mv_dataset_summary");
		if(status == null) status = refreshOne("mv_dataset_result_summary");
		if(status == null) status = refreshOne("mv_station_summary");
		if(status == null) status = refreshOne("mv_specimen_summary");
		if(status == null) status = refreshOne("mv_expedition_summary");	
		String q ="insert into entity_statistics(citations,datasets,stations,expeditions,specimens,total_chem_values,rock_values,mineral_values,volc_glass_values,inclusion_values) "+
				" select "+
		 " (select count(distinct citation_num) from mv_dataset_result_summary) as citations, "+
		 " (select count(distinct dataset_num) from mv_dataset_result_summary) as datasets, "+
		 " (select count(distinct station_num) from mv_dataset_result_summary) as stations, "+
		 " (select count(distinct expedition_num) from mv_dataset_result_summary) as expeditions, "+
		 " (select count(distinct specimen_num) from mv_dataset_result_summary) as specimens, "+
		 " (select count(*) from mv_dataset_result_summary) as total_chem_values, "+
		 " (select count(*) from mv_dataset_result_summary where material_code in ('ROCK','WR')) as rock_values, "+
		 " (select count(*) from mv_dataset_result_summary where material_code ='MIN') as mineral_values, "+
		 " (select count(*) from mv_dataset_result_summary where material_code ='GL') as volc_glass_values, "+
		 " (select count(*) from mv_dataset_result_summary where material_code ='INC') as inclusion_values";		
		if(status == null) status = DBUtil.update(q);
		if(status == null) {
			FacesContext.getCurrentInstance().addMessage("mainMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "All views were refreshed!"));
		}
		else {
			FacesContext.getCurrentInstance().addMessage("mainMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error! " + status));
		}
		PrimeFaces.current().executeScript("PF('statusDialog').hide()");
	}
	
	private String refreshOne(String table) {
		String status = DBUtil.update("refresh materialized view "+table);
		return status;
	}
	
	public List<EntityStatistics> getStatisticsList() { return EntityStatisticsDB.getStatisticsList();}
 }
