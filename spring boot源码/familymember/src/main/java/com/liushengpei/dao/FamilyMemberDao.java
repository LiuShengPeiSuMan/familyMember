package com.liushengpei.dao;

import com.liushengpei.pojo.FamilyMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FamilyMemberDao {

    /*
     * 查询所有家庭成员
     * */
    List<FamilyMember> selectAll(String name);

    /**
     * 添加家族成员
     */
    Integer addMember(FamilyMember member);

    /**
     * 查询家族成员生日
     */
    List<FamilyMember> peopleBirthday();

    /**
     * 查询当天生日家族成员
     */
    List<FamilyMember> selectNowDay();

    /**
     * 根据id查询家族成员详细信息
     */
    FamilyMember peopleDetailedById(String id);

    /**
     * 查询是否有重复成员
     */
    Integer queryRepeat(Map<String, Object> params);

    /**
     * 删除家族成员
     */
    Integer delFamilyMember(String id);

    /**
     * 删除家族成员
     */
    Integer delFamilyMember02(String id);

    /**
     * 修改家族成员详细信息
     */
    Integer updateFamilyMember(Map<String, Object> params);

    /**
     * 判断此成员是否存在
     */
    Integer queryName(String name);

    /**
     * 查询男女人数
     */
    List<Map<String, Object>> manAndWomanNum();

    /**
     * 查询所有人的年龄
     */
    List<Integer> queryAllAge();

    /**
     * 查询某为成员具体年龄
     */
    Integer queryAgeByName(String id);

    /**
     * 修改成员年龄
     */
    Integer updateAge(Map<String, Object> param);

}
