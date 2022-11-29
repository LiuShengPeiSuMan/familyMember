package com.liushengpei.service;

import java.util.Map;

public interface IHomePageService {

    /**
     * 添加通知
     */
    String addNotice(Integer type, String text);

    /**
     * 查询首页数据
     * */
    Map<String,Object> queryData();
}
