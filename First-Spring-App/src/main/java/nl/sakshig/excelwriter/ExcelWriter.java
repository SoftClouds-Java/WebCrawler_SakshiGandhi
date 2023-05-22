package nl.sakshig.excelwriter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelWriter {

    private static final String filePath = "C:\\Users\\saksh\\Downloads\\Sakshi_Excel\\output.xlsx";
    private static Sheet sheet;

    public static void writer(String titleText, String abstractText, String attrRole, String attributeIndus, String speakerName,
                              String attrProduct, String attributeTopic, String attrSessionType, String Company, String designation, String linkedin) {
        try (Workbook workbook = openWorkbook(filePath)) {
            sheet = workbook.getSheet("Sheet1");

            // If sheet doesn't exist, create a new one
            if (sheet == null) {
                sheet = workbook.createSheet("Sheet1");
            }

            // Cell styles
            CellStyle headerCellStyle = createHeaderCellStyle(workbook);
            CellStyle textCellStyle = createTextCellStyle(workbook);

            String[] columnNames = {"Title", "Description", "Speaker Name", "Designation", "Company", "Role", "Industry", "Product", "Topic", "Session Type", "Linkedin"};
            Row headerRow = sheet.createRow(0);
            int headerIndex = 0;

            for (String header : columnNames) {
                Cell cell = headerRow.createCell(headerIndex++);
                cell.setCellValue(header);
                cell.setCellStyle(headerCellStyle);
            }

            int lastRowIndex = sheet.getLastRowNum();

            String[] columnValues = {titleText, abstractText, speakerName, designation, Company, attrRole, attributeIndus, attrProduct,
                    attributeTopic, attrSessionType, linkedin};

            int cellIndex = 0;
            Row dataRow = sheet.createRow(++lastRowIndex);

            for (String cellData : columnValues) {
                Cell dataCell = dataRow.createCell(cellIndex++);
                dataCell.setCellValue(cellData.replaceFirst(" ", "  "));
                dataCell.setCellStyle(textCellStyle);
            }

            for (int colNum = 0; colNum < columnValues[0].length(); colNum++) {
                sheet.setDefaultColumnWidth(65);
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Excel file created or appended successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Workbook openWorkbook(String filePath) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(filePath)) {
            return WorkbookFactory.create(fileIn);
        } catch (Exception e) {
            return new XSSFWorkbook();
        }
    }

    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setWrapText(true);
        return headerCellStyle;
    }

    private static CellStyle createTextCellStyle(Workbook workbook) {
        CellStyle textCellStyle = workbook.createCellStyle();
        textCellStyle.setWrapText(true);
        textCellStyle.setBorderBottom(BorderStyle.THIN);
        textCellStyle.setBorderTop(BorderStyle.THIN);
        textCellStyle.setBorderLeft(BorderStyle.THIN);
        textCellStyle.setBorderRight(BorderStyle.THIN);
        return textCellStyle;
    }
}