/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.engines.qbe.exporter;

import it.eng.spagobi.engines.qbe.query.Field;
import it.eng.spagobi.engines.worksheet.bo.MeasureScaleFactorOption;
import it.eng.spagobi.engines.worksheet.serializer.json.WorkSheetSerializationUtils;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.metadata.IMetaData;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class QbeXLSExporter {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(QbeXLSExporter.class);
    
    public static final String PROPERTY_HEADER_FONT_SIZE = "HEADER_FONT_SIZE";
    public static final String PROPERTY_HEADER_COLOR = "HEADER_COLOR";
    public static final String PROPERTY_CONTENT_FONT_SIZE = "CONTENT_FONT_SIZE";
    public static final String PROPERTY_CONTENT_COLOR = "CONTENT_COLOR";
    public static final String PROPERTY_FONT_NAME = "FONT_NAME";
    
	public static final short DEFAULT_HEADER_FONT_SIZE = 8;
	public static final short DEFAULT_CONTENT_FONT_SIZE = 8;
	public static final String DEFAULT_FONT_NAME = "Verdana";
	public static final String DEFAULT_HEADER_COLOR = "BLACK";
	public static final String DEFAULT_CONTENT_COLOR = "BLACK";
	
	public static final int DEFAULT_DECIMAL_PRECISION = 8;
    
	public static final int DEFAULT_START_COLUMN = 0;
	
	private Locale locale;
	private Map<String, Object> properties;
    
	IDataStore dataStore = null;
	Vector extractedFields = null;
	Map<Integer, CellStyle> decimalFormats = new HashMap<Integer, CellStyle>();

	public QbeXLSExporter(IDataStore dataStore, Locale locale ) {
		super();
		this.dataStore = dataStore;
		this.locale = locale;
		this.properties = new HashMap<String, Object>();
	}

	public IDataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(IDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public QbeXLSExporter() {
		super();
		this.properties = new HashMap<String, Object>();
	}
	
	public void setProperty(String propertyName, Object propertyValue) {
		this.properties.put(propertyName, propertyValue);
	}
	
	public Object getProperty(String propertyName) {
		return this.properties.get(propertyName);
	}
	
	public Workbook export(){
		Workbook wb = new HSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet("new sheet");
	    for(int j = 0; j < 50; j++){
			sheet.createRow(j);
		}
	    fillSheet(sheet, wb, createHelper, 0);

	    return wb;
	}
	
	public void fillSheet(Sheet sheet,Workbook wb, CreationHelper createHelper, int startRow) {		
	    // we enrich the JSON object putting every node the descendants_no property: it is useful when merging cell into rows/columns headers
	    // and when initializing the sheet
		 if (dataStore!=null  && !dataStore.isEmpty()) {
			    CellStyle[] cellTypes = fillSheetHeader(sheet, wb, createHelper, startRow, DEFAULT_START_COLUMN);
			    fillSheetData(sheet, wb, createHelper, cellTypes, startRow+1, DEFAULT_START_COLUMN);    	
		    }
	}
	
	public CellStyle[] fillSheetHeader(Sheet sheet,Workbook wb, CreationHelper createHelper, int beginRowHeaderData, int beginColumnHeaderData) {	
		CellStyle hCellStyle = this.buildHeaderCellStyle(sheet);
		IMetaData d = dataStore.getMetaData();	
    	int colnum = d.getFieldCount();
    	Row row = sheet.getRow(beginRowHeaderData);
    	CellStyle[] cellTypes = new CellStyle[colnum]; // array for numbers patterns storage
    	for(int j = 0; j < colnum; j++){
    		Cell cell = row.createCell(j + beginColumnHeaderData);
    	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    	    String fieldName = d.getFieldName(j);
    	    IFieldMetaData fieldMetaData = d.getFieldMeta(j);
    	    String format = (String) fieldMetaData.getProperty("format");
    	    String alias = (String) fieldMetaData.getAlias();
    	    String scaleFactorHeader = (String) fieldMetaData.getProperty(WorkSheetSerializationUtils.WORKSHEETS_ADDITIONAL_DATA_FIELDS_OPTIONS_SCALE_FACTOR);
    	    String header ;
    	    
            if (extractedFields != null && extractedFields.get(j) != null) {
    	    	Field field = (Field) extractedFields.get(j);
    	    	fieldName = field.getAlias();
    	    	if (field.getPattern() != null) {
    	    		format = field.getPattern();
    	    	}
    	    }
            CellStyle aCellStyle = this.buildContentCellStyle(sheet);
            if (format != null) {
	    		short formatInt = HSSFDataFormat.getBuiltinFormat(format);  		  
	    		aCellStyle.setDataFormat(formatInt);
		    	cellTypes[j] = aCellStyle;
            }

           	if (alias!=null && !alias.equals("")) {
           		header = alias;
           	} else {
           		header = fieldName;
           	}	 

           	header = MeasureScaleFactorOption.getScaledName(header, scaleFactorHeader, locale);
       		cell.setCellValue(createHelper.createRichTextString(header));
       		
           	cell.setCellStyle(hCellStyle);

    	}
    	return cellTypes;
	}
	
	public CellStyle buildHeaderCellStyle(Sheet sheet) {
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
        Font font = sheet.getWorkbook().createFont();
        Short fontSize = (Short) this.getProperty(PROPERTY_HEADER_FONT_SIZE);
        font.setFontHeightInPoints( fontSize != null ? fontSize : DEFAULT_HEADER_FONT_SIZE );
        String fontName = (String) this.getProperty(PROPERTY_FONT_NAME);
        font.setFontName( fontName != null ? fontName : DEFAULT_FONT_NAME );
        String color = (String) this.getProperty(PROPERTY_HEADER_COLOR);
        font.setColor( color != null ? IndexedColors.valueOf(color).getIndex() : IndexedColors.valueOf(DEFAULT_HEADER_COLOR).getIndex() );
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
        return cellStyle;
	}
	
	public CellStyle buildContentCellStyle(Sheet sheet) {
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
        Font font = sheet.getWorkbook().createFont();
        Short fontSize = (Short) this.getProperty(PROPERTY_CONTENT_FONT_SIZE);
        font.setFontHeightInPoints( fontSize != null ? fontSize : DEFAULT_CONTENT_FONT_SIZE );
        String fontName = (String) this.getProperty(PROPERTY_FONT_NAME);
        font.setFontName( fontName != null ? fontName : DEFAULT_FONT_NAME );
        String color = (String) this.getProperty(PROPERTY_CONTENT_COLOR);
        font.setColor( color != null ? IndexedColors.valueOf(color).getIndex() : IndexedColors.valueOf(DEFAULT_CONTENT_COLOR).getIndex() );
        cellStyle.setFont(font);
        return cellStyle;
	}

	public void fillSheetData(Sheet sheet,Workbook wb, CreationHelper createHelper,CellStyle[] cellTypes, int beginRowData, int beginColumnData) {	
		CellStyle dCellStyle = this.buildContentCellStyle(sheet);
		Iterator it = dataStore.iterator();
    	int rownum = beginRowData;
    	short formatIndexInt = HSSFDataFormat.getBuiltinFormat("#,##0");
	    CellStyle cellStyleInt = this.buildContentCellStyle(sheet); // cellStyleInt is the default cell style for integers
	    cellStyleInt.cloneStyleFrom(dCellStyle);
	    cellStyleInt.setDataFormat(formatIndexInt);
	    
		CellStyle cellStyleDate = this.buildContentCellStyle(sheet); // cellStyleDate is the default cell style for dates
		cellStyleDate.cloneStyleFrom(dCellStyle);
		cellStyleDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
		
		IMetaData d = dataStore.getMetaData();	
		
		while(it.hasNext()){
			Row rowVal = sheet.getRow(rownum);
			IRecord record =(IRecord)it.next();
			List fields = record.getFields();
			int length = fields.size();
			for ( int fieldIndex = 0 ; fieldIndex < length ; fieldIndex++ ){
				IField f = (IField)fields.get(fieldIndex);
				if (f != null && f.getValue()!= null) {

					Class c = d.getFieldType(fieldIndex);
					logger.debug("Column [" + (fieldIndex) + "] class is equal to [" + c.getName() + "]");
					if (rowVal == null) {
						rowVal = sheet.createRow(rownum);
					}
					Cell cell = rowVal.createCell(fieldIndex + beginColumnData);
					cell.setCellStyle(dCellStyle);
					if( Integer.class.isAssignableFrom(c) || Short.class.isAssignableFrom(c)) {
						logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "INTEGER" + "]");					
					    Number val = (Number)f.getValue();
					    cell.setCellValue(val.intValue());
					    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					    cell.setCellStyle((cellTypes[fieldIndex] != null) ? cellTypes[fieldIndex] : cellStyleInt);
					}else if( Number.class.isAssignableFrom(c) ) {
			    	    IFieldMetaData fieldMetaData = d.getFieldMeta(fieldIndex);	    
						String decimalPrecision = (String)fieldMetaData.getProperty(IFieldMetaData.DECIMALPRECISION);
						CellStyle cs ;
					    if (decimalPrecision != null) {
					    	cs = getDecimalNumberFormat(new Integer(decimalPrecision), sheet, createHelper, dCellStyle);
					    } else {
					    	cs = getDecimalNumberFormat(DEFAULT_DECIMAL_PRECISION, sheet, createHelper, dCellStyle);
					    }

						logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "NUMBER" + "]");
					    Number val = (Number)f.getValue();

					    Double value = val.doubleValue();
						String scaleFactor = (String) fieldMetaData.getProperty(WorkSheetSerializationUtils.WORKSHEETS_ADDITIONAL_DATA_FIELDS_OPTIONS_SCALE_FACTOR);
												
					    cell.setCellValue(MeasureScaleFactorOption.applyScaleFactor(value, scaleFactor));			    
					    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					    cell.setCellStyle((cellTypes[fieldIndex] != null) ? cellTypes[fieldIndex] : cs);
					}else if( String.class.isAssignableFrom(c)){
						logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "STRING" + "]");	    
					    String val = (String)f.getValue();
					    cell.setCellValue(createHelper.createRichTextString(val));
					    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}else if( Boolean.class.isAssignableFrom(c) ) {
						logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "BOOLEAN" + "]");
					    Boolean val = (Boolean)f.getValue();
					    cell.setCellValue(val.booleanValue());
					    cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
					}else if(Date.class.isAssignableFrom(c)){
						logger.debug("Column [" + (fieldIndex+1) + "] type is equal to [" + "DATE" + "]");	    
					    Date val = (Date)f.getValue();
					    cell.setCellValue(val);	
					    cell.setCellStyle(cellStyleDate);
					}else{
						logger.warn("Column [" + (fieldIndex+1) + "] type is equal to [" + "???" + "]");
					    String val = f.getValue().toString();
					    cell.setCellValue(createHelper.createRichTextString(val));
					    cell.setCellType(HSSFCell.CELL_TYPE_STRING);	    
					}
				}
			}
		   rownum ++;
		}
	}
	

	public void setExtractedFields(Vector extractedFields) {
		this.extractedFields = extractedFields;
	}
	
	
	private CellStyle getDecimalNumberFormat(int j, Sheet sheet, CreationHelper createHelper, CellStyle dCellStyle) {

		if (decimalFormats.get(j) != null)
			return decimalFormats.get(j);
		String decimals = "";
		for (int i = 0; i < j; i++) {
			decimals += "0";
		}
		
	    CellStyle cellStyleDoub = this.buildContentCellStyle(sheet); // cellStyleDoub is the default cell style for doubles
	    cellStyleDoub.cloneStyleFrom(dCellStyle);
	    DataFormat df = createHelper.createDataFormat();
	    cellStyleDoub.setDataFormat(df.getFormat("#,##0."+decimals));
		
		decimalFormats.put(j, cellStyleDoub);
		return cellStyleDoub;
	}

	
}
