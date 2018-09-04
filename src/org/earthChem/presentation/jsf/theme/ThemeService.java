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
 
 
@ManagedBean(name="themeService", eager = true)
@ApplicationScoped
public class ThemeService {
     
    private static List<Theme> themes;
    private static Map<Integer,Theme> themeMap;
   
 /*   @PostConstruct
    public void init() {
    	update();
    }
 */
    public static void update() {
    	String q = "select p.person_num, concat(p.last_name, ', ',p.first_name, ', '||p.middle_name,', ',o.organization_name), p.last_name "+
    			" from person p left join affiliation a on  p.person_num = a.person_num "+  
    			" left join organization o on a.organization_num = o.organization_num "+
    			" where p.status <> 0 order by p.last_name, p.FIRST_NAME";
    	themes = DBUtil.getThemeList(q);
    	themeMap = new HashMap<Integer,Theme>();
    	for(Theme t: themes) {
    		themeMap.put(t.getId(), t);
    	}   	
    }

	public static List<Theme> getThemes() {
		String q = "select p.person_num, concat(p.last_name, ', ',p.first_name, ', '||p.middle_name,', ',o.organization_name), p.last_name "+
    			" from person p left join affiliation a on  p.person_num = a.person_num "+  
    			" left join organization o on a.organization_num = o.organization_num "+
    			" where p.status <> 0 order by p.last_name, p.FIRST_NAME";
    	themes = DBUtil.getThemeList(q);
    	themeMap = new HashMap<Integer,Theme>();
    	for(Theme t: themes) {
    		themeMap.put(t.getId(), t);
    	}   	
		return themes;
	}

	public static void setThemes(List<Theme> themes) {
		ThemeService.themes = themes;
	}

	public static Map<Integer, Theme> getThemeMap() {
		return themeMap;
	}

	public static void setThemeMap(Map<Integer, Theme> themeMap) {
		ThemeService.themeMap = themeMap;
	}
    
    
  /*  
    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    } 
    
    public List<Theme> getThemes() {
        return themes;
    } 
    
    public Map<Integer,Theme> getThemeMap() {
        return themeMap;
    } 
    */
}