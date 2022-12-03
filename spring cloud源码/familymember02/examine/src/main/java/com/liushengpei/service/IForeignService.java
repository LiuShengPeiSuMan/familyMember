package com.liushengpei.service;

import com.liushengpei.pojo.Examine;

public interface IForeignService {

    /**
     * 添加审核
     */
    Integer addExamine(Examine examine);

    /**
     * 判断删除家族成员有没有重复删除同一个人
     */
    Integer countPeopleExamine(String familyPeopleId);
}
