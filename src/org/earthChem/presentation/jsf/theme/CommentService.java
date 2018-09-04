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
 
 
@ManagedBean(name="commentService", eager = true)
@ApplicationScoped
public class CommentService {
     
    private static List<Theme> themes;
    
    @PostConstruct
    public void init() {
    	String q = "select distinct internal_comment from ec_status_info where internal_comment is not null order by internal_comment";
    	themes = DBUtil.getSimpleThemeList(q);
    }
  
    
    public static List<Theme> getThemes() {
        return themes;
    } 
  
}