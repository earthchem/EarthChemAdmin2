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
 
 
@ManagedBean(name="organizationService", eager = true)
@ApplicationScoped
public class OrganizationService {
     
    private static List<Theme> themes;
    private static Map<Integer,Theme> themeMap;
    
    @PostConstruct
    public void init() {
    	update();
    }
  
    public static void update() {
    	String q = "select organization_num, concat(organization_name,', '||department), organization_name from organization where status <> 0 order by organization_name";
    	themes = DBUtil.getThemeList(q);
    	themeMap = new HashMap<Integer,Theme>();
    	for(Theme t: themes) {
    		themeMap.put(t.getId(), t);
    	}   	
    }
    
    public static void setThemes(List<Theme> themes) {
        themes = themes;
    } 
    
    public static List<Theme> getThemes() {
        return themes;
    } 
    
    public static Map<Integer,Theme> getThemeMap() {
        return themeMap;
    } 
}