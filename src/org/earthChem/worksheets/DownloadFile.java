 package org.earthChem.worksheets;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Workbook;
import org.earthChem.db.DBUtil;

//import org.earthChem.dal.ds.Query;
import org.primefaces.event.FileUploadEvent;
 
@ManagedBean(name="downloadFile")
@SessionScoped
public class DownloadFile implements Serializable {
	
	 public void download() {
			String fileName="bai.xlsx";
	/*		
		 	String q = "select d.specimen_code \"SAMPLE ID\", m.igsn \"IGSN\", 'EARTHCHEMDB' \"SOURCE\", c.doi \"DOI\", c.title \"TITLE\",  c.journal \"JOURNAL\", c.authors \"AUTHOR\", d.expedition_code \"EXPEDITION ID\", " + 
		 			"split_part( split_part(split_part(split_part(m.geometry_text,'(',2), ' ', 2), ')', 1), ',',1) \"LATITUDE\", split_part(   split_part(m.geometry_text,'(',2), ' ', 1) \"LONGITUDE\",  " + 
		 			"m.location_precisions \"LOC PREC\",  m.geological_ages, split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',3) \"MIN AGE\", split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',2) \"AGE\", " + 
		 			"split_part(split_part(array_to_string(geological_ages,','), '^',1),'|',4) \"MAX AGE\", d.method_code \"METHOD\", 'MINERAL' \"SAMPLE TYPE\",  " + 
		 			"split_part(split_part(array_to_string(m.taxon,','),'|',1)  ,':',1) \"MATERIAL\", split_part(split_part(array_to_string(m.taxon,','),'|',1)  ,':',2) \"TYPE\", split_part(split_part(array_to_string(m.taxon,','),'|',1)  ,':',3) \"COMPOSITION\", " + 
		 			"split_part(array_to_string(m.taxon,','),'|',2) \"ROCK NAME\", d.variable_code \"VARIABLE\", d.value_meas \"VALUE\"" + 
		 			" from mv_dataset_result_summary d, mv_specimen_summary m,  mv_citation_summary c" + 
		 			" where d.specimen_num=m.specimen_num and d.citation_num = c.citation_num and array_to_string(m.taxon,',') like '%igneous:volcanic:mafic|BASALT%' limit 100;";
	*/
			createFile("", fileName);
		 	
		 	
	 }

	

	 private void createFile(String query, String fileName)  {
		 	Workbook workbook = WorkbookUtil.databaseSheet(DBUtil.sampleDownload(query), "database");
			try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
		    ExternalContext externalContext = facesContext.getExternalContext();
		    externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");	
		    externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\""+fileName+"\""); 	 
		    workbook.write(externalContext.getResponseOutputStream());	
		    facesContext.responseComplete();
		    workbook.close();		    
			}
		    catch(Exception e) {
		        e.printStackTrace();
		    }
	} 
	 
	 
	

}