package com.liushengpei.service.serviceimpl;

import com.liushengpei.dao.FamilyMemberImageDao;
import com.liushengpei.pojo.FamilyMemberImage;
import com.liushengpei.service.IImageService;
import com.liushengpei.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

@Service
public class ImageServiceImpl implements IImageService {

    @Autowired
    private FamilyMemberImageDao imageDao;

    /**
     * 查询用户头像
     */
    @Override
    public FamilyMemberImage findImage(String id) {
        FamilyMemberImage image = imageDao.selectImage(id);
        return image;
    }

    /**
     * 将二进制转为图片
     */
    @Override
    public void byteToImage(String image,String imageName) {
        ImageUtil.byteToImage(image,imageName);
    }
}
