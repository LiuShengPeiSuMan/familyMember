package com.liushengpei.util;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 处理图片
 */
public class ImageUtil {

    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    /**
     * 将图片转换为二进制
     *
     * @param imageFile 图片文件
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

    /**
     * 将二进制转为图片
     *
     * @param twoFile   图片二进制数组
     * @param imageName 图片存储时叫什么名称
     */
    public static void byteToImage(String twoFile, String imageName) {
        try {
            //字符串专为二进制数组
            byte[] bytes = decoder.decodeBuffer(twoFile);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bi1 = ImageIO.read(bais);
            //存储的路径，可以是jpg,png,gif格式
            File w2 = new File("D://file//" + imageName + ".jpg");
            //不管输出什么格式图片，此处不需改动
            ImageIO.write(bi1, "jpg", w2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
