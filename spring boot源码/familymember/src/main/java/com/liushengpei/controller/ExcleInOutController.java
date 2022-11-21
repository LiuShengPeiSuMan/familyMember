package com.liushengpei.controller;

import com.liushengpei.service.IExcleInOutService;
import com.liushengpei.util.resultutil.Result;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 文件的上传与下载
 */
@RestController
@RequestMapping(value = "/excleinout")
public class ExcleInOutController {

    @Autowired
    private IExcleInOutService service;

    /**
     * 下载导入模板
     */
    @PostMapping(value = "/downLoadFile")
    public ResponseEntity<byte[]> downLoad(HttpServletRequest request, String fileName) {
        ResponseEntity<byte[]> responseEntity = service.downloadFile(request, "家庭成员统计表.xlsx");
        return responseEntity;
    }

    /**
     * 导出所有家族成员
     */
    @PostMapping(value = "/exportAllPeople")
    public void exportAllPeople(HttpServletResponse response) {
        Workbook workbook = service.exportFile();
        try {
            //创建输出流
            OutputStream outputStream = response.getOutputStream();
            //表名
            String fileName = "家族成员统计.xlsx";
            //设置字符集
            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
            //写入excle
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
