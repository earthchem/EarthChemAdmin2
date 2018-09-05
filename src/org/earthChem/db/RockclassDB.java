package org.earthChem.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.earthChem.db.postgresql.hbm.Taxonomic;
import org.earthChem.presentation.jsf.theme.Theme;
import org.earthChem.db.postgresql.hbm.Organization;

public class RockclassDB {
	
	public static List<Taxonomic> getRockclassList(String type) {
		List<Taxonomic> list = new ArrayList<Taxonomic>();
		 String	q = "select c.taxonomic_classifier_num, c.taxonomic_classifier_type_cv, c.taxonomic_classifier_name, c.taxonomic_classifier_common_name, p.taxonomic_classifier_common_name parent, c.taxonomic_classifier_description "+
			" from taxonomic_classifier c left join taxonomic_classifier p "+ 
			" on c.parent_taxonomic_classifier_num = p.taxonomic_classifier_num "+
			" where upper(c.taxonomic_classifier_type_cv) like upper('%"+type+"%') "+
			" order by c.taxonomic_classifier_type_cv, c.taxonomic_classifier_name";
		List<Object[]> olist = DBUtil.getECList(q);
		for(Object[] arr: olist) {
			Taxonomic e = new Taxonomic();
			e.setTaxonomicClassifierNum((Integer) arr[0]);
			e.setTaxonomicClassifierTypeCv(""+arr[1]);
			e.setTaxonomicClassifierName(""+arr[2]);
			e.setTaxonomicClassifierCommonName(""+arr[3]);
			if(arr[4] != null) e.setParentTaxonomicClassifierName(""+arr[4]);
			if(arr[5] != null) e.setTaxonomicClassifierDescription(""+arr[5]);
			list.add(e);
		}
		return list;
	}
	
	
	public static Taxonomic getRockclass(Integer ccNum) {
		Taxonomic e = new Taxonomic();
		 String	q = "select c.taxonomic_classifier_num, c.taxonomic_classifier_type_cv, c.taxonomic_classifier_name, c.taxonomic_classifier_common_name,c.taxonomic_classifier_description, p.taxonomic_classifier_common_name p_name, p.taxonomic_classifier_num p_num "+
					" from taxonomic_classifier c left join taxonomic_classifier p "+ 
					" on c.parent_taxonomic_classifier_num = p.taxonomic_classifier_num where c.taxonomic_classifier_num ="+ccNum;
		Object[] arr = DBUtil.uniqueResult(q);
		e.setTaxonomicClassifierNum((Integer)arr[0]);
		e.setTaxonomicClassifierTypeCv(""+arr[1]);
		e.setTaxonomicClassifierName(""+arr[2]);
		e.setTaxonomicClassifierCommonName(""+arr[3]);		
		if(arr[4] != null) e.setTaxonomicClassifierDescription(""+arr[4]);	
		if(arr[5]!=null) {
			Theme t = new Theme();
			t.setId((Integer)arr[6]);
			t.setDisplayName(""+arr[5]);
			t.setName(""+arr[5]);
			e.setRockclassTheme(t);
		}
		return e;
	}
	
	
	public static String update(Taxonomic e, boolean isNew) {
		
		Integer num= e.getTaxonomicClassifierNum();
		String type = e.getTaxonomicClassifierTypeCv(); 
		Integer parentNum = null;
		if("Rock Class".equals(type)) {
			parentNum=e.getRockclassTheme().getId();
		}
		type = DBUtil.StringValue(type);
		String name = DBUtil.StringValue(e.getTaxonomicClassifierName());
		String commonName = DBUtil.StringValue(e.getTaxonomicClassifierCommonName());
		String description = DBUtil.StringValue(e.getTaxonomicClassifierDescription());
		String q = null;
		if(isNew) {			
			 q ="INSERT INTO taxonomic_classifier "+
					"(taxonomic_classifier_num, taxonomic_classifier_type_cv, taxonomic_classifier_name, taxonomic_classifier_common_name, taxonomic_classifier_description, parent_taxonomic_classifier_num) VALUES "+
					"("+num+","+type+","+name+","+commonName+","+description +","+parentNum +")";	
		} else {
			q = "update taxonomic_classifier  set "+
				" taxonomic_classifier_type_cv="+type+","+
				" taxonomic_classifier_name="+name+","+
				" taxonomic_classifier_common_name="+commonName+","+
				" taxonomic_classifier_description="+description+","+
				" parent_taxonomic_classifier_num="+parentNum+" where taxonomic_classifier_num ="+num;
		}
		return DBUtil.update(q);
	}

}
