package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.FamilyMemberDao;
import com.liushengpei.pojo.FamilyMember;
import com.liushengpei.service.IExcleInOutService;
import com.liushengpei.util.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 文件的上传与下载
 */
@Service
public class ExcleInOutServiceImpl implements IExcleInOutService {
    @Autowired
    private FamilyMemberDao memberDao;

    /**
     * 下载模板
     */
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request, String fileName) {

        try {
            //指定要下载的文件路径
            File directory = new File("src/main/resources");
            String reportPath = directory.getCanonicalPath();
            String dirPath = reportPath + "/file/";
            //创建文件对象
            File file = new File(dirPath + File.separator + fileName);
            //设置响应头
            HttpHeaders headers = new HttpHeaders();
            //通知浏览器已下载方式打开
            fileName = getFileName(request, fileName);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<byte[]>(e.getMessage().getBytes(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    /**
     * 导出所有家族成员
     */
    @Override
    public Workbook exportFile() {
        String[] title = {"姓名", "年龄", "性别", "家庭住址", "出生日期", "死亡日期", "是否已婚", "最高学历", "工作职位", "工作地点", "电话号码", "电子邮箱"};
        List<FamilyMember> familyMemberList = memberDao.selectAll(null);
        Workbook workbook = writeToExcelByList(title, familyMemberList);
        return workbook;
    }

    /**
     * 导出家族成员信息
     **/
    public Workbook writeToExcelByList(String[] array, List<FamilyMember> familyMembers) {
        //创建工作薄
        Workbook wb = new XSSFWorkbook();
        //标题和页码
        CellStyle titleStyle = wb.createCellStyle();
        // 设置单元格对齐方式,水平居左
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        // 设置字体样式
        Font titleFont = wb.createFont();
        // 字体高度
        titleFont.setFontHeightInPoints((short) 12);
        // 字体样式
        titleFont.setFontName("黑体");
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置背景色
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        //必须设置 否则背景色不生效
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //设置边框
        titleStyle.setBorderBottom(BorderStyle.DASH_DOT); //下边框
        titleStyle.setBorderLeft(BorderStyle.DASH_DOT);//左边框
        titleStyle.setBorderTop(BorderStyle.DASH_DOT);//上边框
        titleStyle.setBorderRight(BorderStyle.DASH_DOT);//右边框

        //创建sheet
        Sheet sheet = wb.createSheet("家族成员");
        // 自动设置宽度
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(1, true);
        //列宽
        //sheet.setColumnWidth(2, );
        // 在sheet中添加标题行,行数从0开始
        Row row = sheet.createRow(0);
        for (int i = 0; i < array.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(array[i]);
            cell.setCellStyle(titleStyle);
        }
        // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
        CellStyle dataStyle = wb.createCellStyle();
        // 设置居中样式，水平居中
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        //数据从序号1开始
        try {
            int index = 1;
            for (FamilyMember f : familyMembers) {
                // 默认的行数从0开始，为了统一格式设置从1开始，就是从excel的第二行开始
                //姓名
                row = sheet.createRow(index);
                Cell cell01 = row.createCell(0);
                cell01.setCellValue(f.getName());
                cell01.setCellStyle(dataStyle);
                //年龄
                Cell cell02 = row.createCell(1);
                cell02.setCellValue(f.getAge());
                cell02.setCellStyle(dataStyle);
                //性别
                Integer sex = f.getSex();
                Cell cell03 = row.createCell(2);
                if (sex == 0) {
                    cell03.setCellValue("女");
                } else {
                    cell03.setCellValue("男");
                }
                cell03.setCellStyle(dataStyle);
                //家庭住址
                Cell cell04 = row.createCell(3);
                cell04.setCellValue(f.getHomeAddress());
                cell04.setCellStyle(dataStyle);
                //出生日期
                Cell cell05 = row.createCell(4);
                cell05.setCellValue(ToolUtil.dateFormat(f.getDateOfBirth()));
                cell05.setCellStyle(dataStyle);
                //死亡日期
                Date dateOfDeath = f.getDateOfDeath();
                Cell cell06 = row.createCell(5);
                if (dateOfDeath == null) {
                    cell06.setCellValue("无");
                } else {
                    String dateFormat = ToolUtil.dateFormat(dateOfDeath);
                    cell06.setCellValue(dateFormat);
                }
                cell06.setCellStyle(dataStyle);
                //是否已婚
                Cell cell07 = row.createCell(6);
                Integer marriedOfNot = f.getMarriedOfNot();
                if (marriedOfNot == 0) {
                    cell07.setCellValue("未婚");
                } else {
                    cell07.setCellValue("已婚");
                }
                cell07.setCellStyle(dataStyle);
                //最高学历
                Cell cell08 = row.createCell(7);
                Integer education = f.getEducation();
                String education1 = ToolUtil.education(education);
                cell08.setCellValue(education1);
                cell08.setCellStyle(dataStyle);
                //工作职位
                Cell cell09 = row.createCell(8);
                cell09.setCellValue(f.getWork());
                cell09.setCellStyle(dataStyle);
                //工作地点
                Cell cell10 = row.createCell(9);
                cell10.setCellValue(f.getWorkAddress());
                cell10.setCellStyle(dataStyle);
                //电话号码
                Cell cell11 = row.createCell(10);
                cell11.setCellValue(f.getPhone());
                cell11.setCellStyle(dataStyle);
                //电子邮箱
                Cell cell12 = row.createCell(11);
                cell12.setCellValue(f.getEmail());
                cell12.setCellStyle(dataStyle);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    //根据不同的浏览器进行编码设置
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String[] IEBrowserKeyWords = {"MSIE", "Trident", "Edge"};
        //获取请求头代理信息
        String userAgent = request.getHeader("User-Agent");
        for (String keyWord : IEBrowserKeyWords) {
            if (userAgent.contains(keyWord)) {
                return URLEncoder.encode(fileName, "UTF-8").replace("+", " ");
            }
        }
        return new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    }
}
