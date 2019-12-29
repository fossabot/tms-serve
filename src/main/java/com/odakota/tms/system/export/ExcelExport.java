package com.odakota.tms.system.export;

import com.odakota.tms.constant.Constant;
import com.odakota.tms.system.base.BaseEntity;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Component
public class ExcelExport<T extends BaseEntity> {

    /**
     * Application-provided subclasses must implement this method to populate the Excel workbook document, given the
     * model.
     *
     * @param model the model Map
     */
    @SuppressWarnings("unchecked")
    public Workbook buildExcelDocument(Map<String, Object> model) throws Exception {

        List<T> list = (List<T>) model.get(Constant.MODEL_LIST_DATA);
        String sheetName = model.get(Constant.MODEL_SHEET_NAME).toString();
        Class<T> clazz = (Class<T>) model.get(Constant.MODEL_CLAZZ);
        Workbook workbook = new XSSFWorkbook();

        // create excel xls sheet
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(30);

        // create header row
        int totalColumn = buildHeader(sheet, buildStyle(workbook), clazz);

        fillData(sheet, list);

        // Resize all columns to fit the content size
        for (int i = 0; i < totalColumn; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }


    /**
     * Create style for header cells
     *
     * @param workbook the Excel workbook to populate
     * @return {@link CellStyle}
     */
    protected CellStyle buildStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        return style;
    }

    /**
     * Create header row
     *
     * @param sheet Excel worksheet
     * @param style style
     */
    protected int buildHeader(Sheet sheet, CellStyle style, Class<T> tClass) throws IntrospectionException {
        Row header = sheet.createRow(0);
        // Load all fields in the class (private included)
        int i = 0;
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(tClass, Object.class).getPropertyDescriptors()) {
            String methodName = descriptor.getReadMethod().getName();
            String name = methodName.startsWith("is") ? methodName.substring(2) : methodName.substring(3);
            header.createCell(i).setCellValue(name);
            header.getCell(i).setCellStyle(style);
            i++;
        }
        return i + 1;
    }

    protected void fillData(Sheet sheet, List<T> list)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        int rowCount = 1;
        for (T t : list) {
            Row userRow = sheet.createRow(rowCount++);
            Class<?> c = ((Object) t).getClass();
            int j = 0;
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(c, Object.class).getPropertyDescriptors()) {
                Method method = descriptor.getReadMethod();
                userRow.createCell(j).setCellValue(method.invoke(t).toString());
                j++;
            }
        }
    }
}
