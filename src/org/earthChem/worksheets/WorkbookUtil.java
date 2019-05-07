 package org.earthChem.worksheets;

import java.io.IOException;
import java.io.Serializable;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.common.usermodel.fonts.FontInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.earthChem.db.DBUtil;
import org.earthChem.db.postgresql.hbm.StringTable;

import java.io.FileOutputStream;

//import org.earthChem.dal.ds.Query;
import org.primefaces.event.FileUploadEvent;
 
public class WorkbookUtil {
	
	public static Workbook databaseSheet(StringTable table, String sheetName)  {
		Workbook workbook = new XSSFWorkbook();  
		Sheet sheet = workbook.createSheet(sheetName);
		String[] columns = table.getHeads();
		Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
     
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

 
		Row headerRow = sheet.createRow(0);
	    for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
	     }
	     ArrayList<Object[]> list = table.getData();
	     int r = 1;
	     for(Object[] a: list) {
	    	  Row row = sheet.createRow(r++);
	    	  if(a != null)
	    	  for(int i = 0; i< a.length; i++) {
	    		  if(a[i] !=  null) row.createCell(i).setCellValue(""+a[i]);
	    	  }
	     }
	     
	     // Resize all columns to fit the content size
	      for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	      }
	      return workbook;
		}  
	
	
	public static void getData(StringTable table, Sheet sheet, CellStyle headerCellStyle)  {
	//	Workbook workbook = new XSSFWorkbook();  
//		Sheet sheet = workbook.createSheet(sheetName);
		String[] columns = table.getHeads();
		Row headerRow = sheet.createRow(0);
		 for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
	            cell.setCellStyle(headerCellStyle);
		 }
	     ArrayList<Object[]> list = table.getData();
	     int r = 1;
	     for(Object[] a: list) {
	    	  Row row = sheet.createRow(r++);
	    	  if(a != null)
	    	  for(int i = 0; i< a.length; i++) {
	    		  if(a[i] !=  null) row.createCell(i).setCellValue(""+a[i]);
	    	  }
	     }
	     
	     // Resize all columns to fit the content size
	      for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	      }
	   //   return workbook;
		}  
}