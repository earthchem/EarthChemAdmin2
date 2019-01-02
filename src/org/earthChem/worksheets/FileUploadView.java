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
import org.earthChem.model.StringTable;

import java.io.FileOutputStream;

//import org.earthChem.dal.ds.Query;
import org.primefaces.event.FileUploadEvent;
 
@ManagedBean(name="fileUploadView")
@SessionScoped
public class FileUploadView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -457854015607534161L;
	private List<List<String>> ds;
	private List<ColumnModel> columns;
	private List<IncorrectData> incorrectVariables;
	private List<IncorrectData> incorrectValues;
	private String incorrectSamples;
	private Workbook workbook;
	private String fileName="bai.xlsx";
	private SelectItem[] variableOptions;
	
//	@PostConstruct
    public void init() {
	/*	Query query=null;
		List<String[]> variableList = new ArrayList<String[]>(); 
		try {
			query = new Query();
	
		  String q = "select variable_code, variable_definition from variable where variable_definition is not null order by variable_code";
		  ResultSet rs = query.getResultSet(q);
		    while(rs.next()) 
		    {
		     String [] arr = new String[2];
		     arr[0] = rs.getString(1);
		     arr[1] = rs.getString(2);
		     variableList.add(arr);	
		    }
		           
	    } catch (Exception e) {
			e.printStackTrace();
		}	finally {
			 query.close();  
		}     
		int i = 0;
		variableOptions = new SelectItem[variableList.size()];  
		for(String[] v: variableList) {
			 String label = null;
			 if(v[1] != null) { 
				 if(v[1].length() > 150) label =v[1].substring(0,150)+ "...";
				 label = v[0]+" ("+v[1]+")";
				 variableOptions[i++] = new SelectItem(v[0], label, v[1]);  
			 }
		}
		*/
    } 


	public void handleFileUpload(FileUploadEvent event) {
    	 if (event.getFile().equals(null)) {
    		 	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File is null", null));	 
     	 }
    	 incorrectSamples = null;
    	 InputStream file;
    	 try {
	    	 file = event.getFile().getInputstream();
	    	 fileName = event.getFile().getFileName();		
	    	 
	    	 if(fileName.endsWith("xls"))
	    		 workbook = new HSSFWorkbook(file);
	    	 else
	    		 workbook = new XSSFWorkbook(file);   	
    	 } catch (IOException e) {
    		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error reading file" + e, null));
    	 }
    	createDisplayData();
    	findIncorrectData();
    	if(incorrectSamples != null)
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", incorrectSamples+", exist in database."));
    	else {
    		Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages();
    		while(it.hasNext()) {
    			it.next();
    			it.remove();
    		} 
    	}
	}
	 

	 
	 

	public void downloadFile()  {
		try {
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    ExternalContext externalContext = facesContext.getExternalContext();
	    if(fileName.endsWith("xls")) externalContext.setResponseContentType("application/vnd.ms-excel");
	    else externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");	 	    
	    externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\""+fileName+"\""); 	 
	    workbook.write(externalContext.getResponseOutputStream());
	    facesContext.responseComplete();
		}
	    catch(IOException e) {
	        e.printStackTrace();
	    }
	}

	 //12/28/18
	 public void downloadFile2()  {
	/*   String[] columns = {"Name", "Email"};
		   workbook = new XSSFWorkbook();  
		   Sheet sheet = workbook.createSheet("Bai");
	    
		    Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setFontHeightInPoints((short) 14);
	        headerFont.setColor(IndexedColors.RED.getIndex());
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);
        
	        Row headerRow = sheet.createRow(0);
	        for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
	            cell.setCellStyle(headerCellStyle);
	        }
	        Row row = sheet.createRow(1);
	        row.createCell(0).setCellValue("bai2");
	        row.createCell(1).setCellValue("bai@gmail.com");
	        
	     // Resize all columns to fit the content size
	        for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	*/
		 	String q = "select * from method";
		 	workbook = WorkbookUtil.databaseSheet(DBUtil.download(q), "database");
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
	 
	 
	
	 
	 
	 
	 // example
/*
 *   // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

      // CreationHelper helps us create instances of various things like DataFormat, 
     //      Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way 
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Employee");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(Employee employee: employees) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(employee.getName());

            row.createCell(1)
                    .setCellValue(employee.getEmail());

            Cell dateOfBirthCell = row.createCell(2);
            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(3)
                    .setCellValue(employee.getSalary());
        }

		// Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
 */
	 
	 
	 
	 
	 
	 
	 
	private void findIncorrectData() {
		incorrectVariables = new ArrayList<IncorrectData>();
		int i = 0;
		for(ColumnModel model: columns) {
			String header = model.getHeader();
			if("Name".equals(header)) continue;
			++i;
			boolean find = false;
			for(SelectItem s: variableOptions)
			{		
				if(s.getValue().equals(header)) { find=true; break;}
			}	
		    if(!find) incorrectVariables.add(new IncorrectData(i,header,""));	
		}		
	}
	
	public void onValueChange() {
		for(IncorrectData v: incorrectVariables)
		{
			if("".equals(v.getCorrect())) continue;
			Sheet sheet  =  workbook.getSheetAt(0);
			Cell c =  SheetUtil.getCellWithMerges(sheet, 0, v.getColId());
			c.setCellValue(v.getCorrect());
			columns.set(v.getColId(),new ColumnModel(v.getCorrect(),v.getCorrect()));	
		}
		
		for(IncorrectData v: incorrectValues)
		{
			if("".equals(v.getCorrect())) continue;
			Sheet sheet  =  workbook.getSheetAt(0);
			Cell c =  SheetUtil.getCellWithMerges(sheet, v.getRowId(), v.getColId());
			c.setCellValue(v.getCorrect());
		}
		createDisplayData();
	}
	
	
	private void createDisplayData() {
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator =  sheet.iterator();
		Map<Integer,String> columnMap = new HashMap<Integer,String>();
    	 columns = new ArrayList<ColumnModel>();  
    	 ds= new ArrayList<List<String>>();
    	 incorrectValues = new ArrayList<IncorrectData>();
    	 String desc = null;
    	 while (rowIterator.hasNext()) {
		    	Row row = rowIterator.next();
		    	Iterator<Cell> cellIterator = row.cellIterator();
		    	List<String> rlist = new ArrayList<String>();
		    	 while (cellIterator.hasNext()) {
		    		 Cell cell = cellIterator.next();
		    		 if(cell.getRowIndex()==0)  {
		    			 columns.add(new ColumnModel(cell.getStringCellValue(),""+cell.getColumnIndex()));
		    			 columnMap.put(cell.getColumnIndex(),cell.getStringCellValue());
		    		  }
		    		 else {		
			    		 switch (cell.getCellType()) {
					    	 case Cell.CELL_TYPE_NUMERIC:
					    		 rlist.add(""+cell.getNumericCellValue());
					    	 break;
					    	 case Cell.CELL_TYPE_STRING:
					    		 String text = cell.getStringCellValue();
					    		 desc = text;
					    		 if(cell.getColumnIndex()==0) {
					    			 if(isSampleNameExist(text)) {
					    				 if(incorrectSamples == null) {
					    					 incorrectSamples = "The sample names, "+text;					    				 
					    				 } else {
					    					 incorrectSamples += ", "+text;
					    				 }
					    				 text+=" (exist in db)";
					    			 }
					    		 }
					    		 else {
						    		 String arr[] = text.split(", ");
						    		 if(arr.length != 2 || !isNumeric(arr[0])|| !isNumeric(arr[1])) {
						    			 incorrectValues.add(new IncorrectData(cell.getRowIndex(),cell.getColumnIndex(),text,desc+":"+columnMap.get(cell.getColumnIndex())));
						    		 }
					    		 }
					    		 rlist.add(text);		 
					    	 break;		
					    	 default: rlist.add("");
		                     break;
				    	 }	
		    		 }			        	 
		    	} //second while
		    	if(row.getRowNum() != 0) ds.add(rlist);
		    	
    	 }  //first while
		
	}
	
	private boolean isSampleNameExist(String sampleName) {
		boolean find = false;
	/*	Query query=null;
		List<String[]> variableList = new ArrayList<String[]>(); 
		try {
		  query = new Query();	
		  String q = "select sampling_feature_num from sampling_feature where sampling_feature_code ='"+sampleName+"'";
		  ResultSet rs = query.getResultSet(q);
		  if(rs.next()) find = true;
	    } catch (Exception e) {
			e.printStackTrace();
		}	finally {
			 query.close();  
		} 
		*/  
		return find;
	}
	
	public List<IncorrectData> getIncorrectValues() {
		return incorrectValues;
	}

	public void setIncorrectValues(List<IncorrectData> incorrectValues) {
		this.incorrectValues = incorrectValues;
	}


	public List<IncorrectData> getIncorrectVariables() {
		return incorrectVariables;
	}

	public void setIncorrectVariables(List<IncorrectData> incorrectVariables) {
		this.incorrectVariables = incorrectVariables;
	}
	
	public List<List<String>> getDs() {    	
		return ds;
	}

	public void setDs(List<List<String>> ds) {
		this.ds = ds;
	}

	public List<ColumnModel> getColumns() {
        return columns;
    }
	
	
	public SelectItem[] getVariableOptions()  { 
        return variableOptions;  
    }  
	

	//----------------------------------------------------

	static public class ColumnModel implements Serializable {
		 
        /**
		 * 
		 */
		private static final long serialVersionUID = -7544931531126034658L;
		private String header;
        private String property;
 
        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }
 
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }
    }
	
	
		static public class IncorrectData implements Serializable {
		 
		private static final long serialVersionUID = 2814447658816948453L;
		/**
		 * 
		 */
		private int rowId;
		private int colId;
		private String incorrect;
		private String correct;
		private String desc;
		
		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public IncorrectData(int colId, String incorrect, String desc) {
			this.colId = colId;
            this.incorrect = incorrect;
            this.desc = desc;
        }
		
		public IncorrectData(int rowId, int colId, String incorrect, String desc) {
			this.rowId = rowId;
			this.colId = colId;
            this.incorrect = incorrect;
            this.desc = desc;
        }
		
        public String getIncorrect() {
			return incorrect;
		}

		public void setIncorrect(String incorrect) {
			this.incorrect = incorrect;
		}

		public String getCorrect() {
			return correct;
		}

		public void setCorrect(String correct) {
			this.correct = correct;
		}

		public int getColId() {
			return colId;
		}

		public void setColId(int colId) {
			this.colId = colId;
		}
		
		public int getRowId() {
			return rowId;
		}

		public void setRowId(int rowId) {
			this.rowId = rowId;
		}
    }
		
	static public boolean isNumeric(String s) {  
		    return s.matches("[-+]?\\d*\\.?\\d+");  
	}  	
}