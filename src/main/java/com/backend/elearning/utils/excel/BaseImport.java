package com.backend.elearning.utils.excel;

import com.backend.elearning.exception.BadRequestException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Component
public class BaseImport <T>{

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<T> listDatas;
//
//    public UserExcelExporter(List<User> listUsers) {
//        this.listUsers = listUsers;
//        workbook = new XSSFWorkbook();
//    }


    private void writeHeaderLine(String[] headers) {
        sheet = workbook.createSheet("Users");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        for (String header: headers) {
            createCell(row, 0, header, style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(String[] fields, Class<T> clazz) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (T data : this.listDatas) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (String fieldName: fields) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    createCell(row, columnCount++, value, style);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new BadRequestException(e.getMessage());
                }
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
