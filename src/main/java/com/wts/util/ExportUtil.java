package com.wts.util;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wts.entity.model.Course;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by 王天硕 on 2017/4/28.
 */
public class ExportUtil {
  public static void export(String[] title,String fileName,String SQL,HttpServletResponse response) throws IOException {
    List<Record> list = Db.find(SQL);
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet();
    XSSFRow row =sheet.createRow(0);
    for(int i=0;i<title.length;i++){
      XSSFCell cell=row.createCell(i);
      cell.setCellValue(title[i]);
    }
    for (int i = 0; i < list.size(); i++) {
      XSSFRow nextRow = sheet.createRow(i + 1);
      for (int k = 0; k < title.length; k++) {
        XSSFCell nextCell = nextRow.createCell(k);
        if (list.get(i).get(title[k]) == null) {
          nextCell.setCellValue("");
        } else {
          nextCell.setCellValue(Course.dao.find(SQL).get(i).get(title[k]).toString());
        }
      }
    }
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename="+fileName+"Export.xlsx");
    OutputStream out = response.getOutputStream();
    workbook.write(out);
    out.flush();
    out.close();
    workbook.close();
  }
}
