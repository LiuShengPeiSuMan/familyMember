package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMember;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件的上传与下载
 */
public interface IExcleInOutService {

    /**
     * 下载导入模板
     */
    ResponseEntity<byte[]> downloadFile(HttpServletRequest request, String fileName);

    /**
     * 导出家族成员
     */
    Workbook exportFile();
}
