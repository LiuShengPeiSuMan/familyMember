package com.liushengpei.service;

import java.util.Map;

/**
 * 对外提供接口逻辑
 */
public interface IForeignService {

    /**
     * 修改户主家庭人数（添加）
     */
    Integer addFamilyPeopleNum(Map<String, Object> params);
}
