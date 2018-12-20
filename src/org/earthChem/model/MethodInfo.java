package org.earthChem.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

public class MethodInfo  implements Serializable {
	private Integer method_info_num;
	private Integer action_num;
	private Integer variable_num;
	private String content;
	private String NORM_STANDARD_NAME;
	private String NORM_STANDARD_VALUE;
	private String FCORR_RATIO;
	private String FCORR_STANDARD_NAME;
	private String FCORR_STANDARD_VALUE;
	private String METHOD_PRECISION_MIN;
	private String METHOD_PRECISION_MAX;
	private String METHOD_PRECISION_TYPE;
	private String STANDARD_DATA;
	private String methodCode;
	private String variableCode;
	
	
	
	public String getVariableCode() {
		return variableCode;
	}
	public void setVariableCode(String variableCode) {
		this.variableCode = variableCode;
	}
	public String getMethodCode() {
		return methodCode;
	}
	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}
	public Integer getMethod_info_num() {
		return method_info_num;
	}
	public void setMethod_info_num(Integer method_info_num) {
		this.method_info_num = method_info_num;
	}
	public Integer getAction_num() {
		return action_num;
	}
	public void setAction_num(Integer action_num) {
		this.action_num = action_num;
	}
	public Integer getVariable_num() {
		return variable_num;
	}
	public void setVariable_num(Integer variable_num) {
		this.variable_num = variable_num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNORM_STANDARD_NAME() {
		return NORM_STANDARD_NAME;
	}
	public void setNORM_STANDARD_NAME(String nORM_STANDARD_NAME) {
		NORM_STANDARD_NAME = nORM_STANDARD_NAME;
	}
	public String getNORM_STANDARD_VALUE() {
		return NORM_STANDARD_VALUE;
	}
	public void setNORM_STANDARD_VALUE(String nORM_STANDARD_VALUE) {
		NORM_STANDARD_VALUE = nORM_STANDARD_VALUE;
	}
	public String getFCORR_RATIO() {
		return FCORR_RATIO;
	}
	public void setFCORR_RATIO(String fCORR_RATIO) {
		FCORR_RATIO = fCORR_RATIO;
	}
	public String getFCORR_STANDARD_NAME() {
		return FCORR_STANDARD_NAME;
	}
	public void setFCORR_STANDARD_NAME(String fCORR_STANDARD_NAME) {
		FCORR_STANDARD_NAME = fCORR_STANDARD_NAME;
	}
	public String getFCORR_STANDARD_VALUE() {
		return FCORR_STANDARD_VALUE;
	}
	public void setFCORR_STANDARD_VALUE(String fCORR_STANDARD_VALUE) {
		FCORR_STANDARD_VALUE = fCORR_STANDARD_VALUE;
	}
	public String getMETHOD_PRECISION_MIN() {
		return METHOD_PRECISION_MIN;
	}
	public void setMETHOD_PRECISION_MIN(String mETHOD_PRECISION_MIN) {
		METHOD_PRECISION_MIN = mETHOD_PRECISION_MIN;
	}
	public String getMETHOD_PRECISION_MAX() {
		return METHOD_PRECISION_MAX;
	}
	public void setMETHOD_PRECISION_MAX(String mETHOD_PRECISION_MAX) {
		METHOD_PRECISION_MAX = mETHOD_PRECISION_MAX;
	}
	public String getMETHOD_PRECISION_TYPE() {
		return METHOD_PRECISION_TYPE;
	}
	public void setMETHOD_PRECISION_TYPE(String mETHOD_PRECISION_TYPE) {
		METHOD_PRECISION_TYPE = mETHOD_PRECISION_TYPE;
	}
	public String getSTANDARD_DATA() {
		return STANDARD_DATA;
	}
	public void setSTANDARD_DATA(String sTANDARD_DATA) {
		STANDARD_DATA = sTANDARD_DATA;
	}
	
	
}
