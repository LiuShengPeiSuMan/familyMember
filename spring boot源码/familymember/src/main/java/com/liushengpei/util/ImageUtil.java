package com.liushengpei.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 处理图片
 */
public class ImageUtil {

    /**
     * 将图片转换为二进制
     */
    public static byte[] imageByteArray(MultipartFile imageFile) throws IOException {
        InputStream input = imageFile.getInputStream();
        //获取上传文件的文件名
        String fileName = imageFile.getOriginalFilename();
        System.out.println(fileName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = input.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
}
