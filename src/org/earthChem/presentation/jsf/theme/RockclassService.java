package org.earthChem.presentation.jsf.theme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.earthChem.db.DBUtil;
import org.earthChem.presentation.jsf.theme.Theme;
 
 
@ManagedBean(name="rockclassService", eager = true)
@ApplicationScoped
public class RockclassService{

 private static String query= "select taxonomic_classifier_num, taxonomic_classifier_common_name, taxonomic_classifier_common_name from taxonomic_classifier "+
		 " where taxonomic_classifier_type_cv = 'Rock Type' order by taxonomic_classifier_name";

 private static Map<Integer,Theme> themeMap;
  
  public static List<Theme> getThemes() {
  	List<Theme> themes = DBUtil.getThemeList(query);
  	themeMap = new HashMap<Integer,Theme>();
  	for(Theme t: themes) {
  		themeMap.put(t.getId(), t);
  	}   	
      return themes;
  } 
  
  public static Map<Integer,Theme> getThemeMap() {
      return themeMap;
  } 
}