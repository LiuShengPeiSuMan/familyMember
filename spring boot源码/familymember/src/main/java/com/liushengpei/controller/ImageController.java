package com.liushengpei.controller;

import com.liushengpei.pojo.FamilyMemberImage;
import com.liushengpei.service.IImageService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    private IImageService service;

    /**
     * 查询用户头像
     */
    @PostMapping(value = "findimage")
    public Result<FamilyMemberImage> findImage(@RequestParam(value = "id", defaultValue = "", required = false) String id) {
        if (id == null || id.equals("")) {
            return Result.fail("图片id不能为空");
        }
        FamilyMemberImage image = service.findImage(id);
        return Result.success(image);
    }

    /**
     * 二进制数据转为图片
     */
    @PostMapping(value = "/byteToImage")
    public Result<String> byteToImage(@RequestParam(value = "image", defaultValue = "", required = false) String image,
                                      @RequestParam(value = "imageName", defaultValue = "", required = false) String imageName) {
        if (image == null || image.equals("")) {
            return Result.fail("二进制数据不能为空");
        }
        if (imageName == null || imageName.equals("")) {
            return Result.fail("存储名称不能为空");
        }
        service.byteToImage(image, imageName);
        return Result.success("存储成功");
    }
}
