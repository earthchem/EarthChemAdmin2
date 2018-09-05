package org.earthChem.presentation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.earthChem.rest.CitationRest;
import org.earthChem.db.CitationDB;
import org.earthChem.db.CitationList;
import org.earthChem.db.CitationPurge;
import org.earthChem.db.DBUtil;
import org.earthChem.db.EquipmentDB;
import org.earthChem.db.ExpeditionDB;
import org.earthChem.db.MethodDB;
import org.earthChem.db.OrganizationDB;
import org.earthChem.db.postgresql.hbm.Affiliation;
import org.earthChem.db.postgresql.hbm.AuthorList;
import org.earthChem.db.postgresql.hbm.Citation;
import org.earthChem.db.postgresql.hbm.EcStatusInfo;
import org.earthChem.db.postgresql.hbm.Equipment;
import org.earthChem.db.postgresql.hbm.Expedition;
import org.earthChem.db.postgresql.hbm.Method;
import org.earthChem.db.postgresql.hbm.Organization;
import org.earthChem.db.postgresql.hbm.SamplingFeature;
import org.earthChem.rest.InvalidDoiException;
import org.earthChem.presentation.jsf.theme.CommentService;
import org.earthChem.presentation.jsf.theme.EquipmentService;
import org.earthChem.presentation.jsf.theme.OrganizationService;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.presentation.jsf.theme.ThemeService;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

 
@ManagedBean(name="checkViewBean")
@SessionScoped
public class CheckViewBean implements Serializable {

	private List<SamplingFeature> noParentAnalyzed; 
	private List<SamplingFeature> noChildStation; 
	private List<SamplingFeature> noParentSpecimen; 
	private List<SamplingFeature> noChildSpecimen; 
	
	/*
analyzed sample without specimen
select vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_analyzed_samples vas
where vas.sampling_feature_num not in (select distinct sampling_feature_num from related_feature rf where rf.relationship_type_num=9);

station without specimen
select  vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_stations vas
where vas.sampling_feature_num not in (select distinct related_sampling_feature_num from related_feature rf where rf.relationship_type_num=22);

specimen without station
select  vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_specimens vas
where vas.sampling_feature_num not in (select distinct sampling_feature_num from related_feature rf where rf.relationship_type_num=22);

specimen without analyzed sample
select vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_specimens vas
where vas.sampling_feature_num not in (select distinct related_sampling_feature_num from related_feature rf where rf.relationship_type_num=9);
;
*/
	public List<SamplingFeature> getNoParentAnalyzed() {
		String q = "select vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_analyzed_samples vas "+
				" where vas.sampling_feature_num not in (select distinct sampling_feature_num from related_feature rf where rf.relationship_type_num=9) ";
		return getList(q);
	}
	
	public List<SamplingFeature> getNoChildStation() {
		String q = "select  vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_stations vas "+
				" where vas.sampling_feature_num not in (select distinct related_sampling_feature_num from related_feature rf where rf.relationship_type_num=22)";
		return getList(q);
	}

	public List<SamplingFeature> getNoParentSpecimen() {
		String q = "select  vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_specimens vas "+
				" where vas.sampling_feature_num not in (select distinct sampling_feature_num from related_feature rf where rf.relationship_type_num=22)";
		return getList(q);
	}
	
	public List<SamplingFeature> getNoChildSpecimen() {
		String q = "select vas.sampling_feature_num, vas.sampling_feature_type_num, vas.sampling_feature_code from v_specimens vas "+
				" where vas.sampling_feature_num not in (select distinct related_sampling_feature_num from related_feature rf where rf.relationship_type_num=9)";
		return getList(q);
	}
	
	private List<SamplingFeature> getList(String q) {
		List<SamplingFeature> list =  new ArrayList<SamplingFeature>();
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] i: olist) {
			SamplingFeature s = new SamplingFeature();
			s.setSamplingFeatureNum((Integer)i[0]);
			s.setSamplingFeatureTypeNum((Integer)i[1]);
			s.setSamplingFeatureCode(""+i[2]);
			list.add(s);
		}
		return list;
	}
 }
