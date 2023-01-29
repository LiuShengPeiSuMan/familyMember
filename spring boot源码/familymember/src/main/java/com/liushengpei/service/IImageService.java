package com.liushengpei.service;

import com.liushengpei.pojo.FamilyMemberImage;

public interface IImageService {

    /**
     * 查询用户头像
     */
    FamilyMemberImage findImage(String id);

    /**
     * 将二进制转为图片
     * */
    void byteToImage(String image,String imageName);
}
