package org.earthChem.presentation.jsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/*********
 * JSF Backing Bean for Logout Page
 *
 *
 */
@ManagedBean(name="logoutBean")
@SessionScoped
public class LogoutBean implements Serializable {
	private static final long serialVersionUID = -5818671124700032389L;

	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/faces/ECAdmin.xhtml/logout?faces-redirect=true";
	}
}
