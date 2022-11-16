package com.liushengpei.dao;

import com.liushengpei.pojo.FamilyMemberImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 家族成员头像
 */
@Mapper
public interface FamilyMemberImageDao {

    /**
     * 添加家族成员图片
     */
    Integer addImage(FamilyMemberImage familyMemberImage);
}
