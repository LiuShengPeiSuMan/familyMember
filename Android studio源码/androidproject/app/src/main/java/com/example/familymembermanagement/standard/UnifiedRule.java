package com.example.familymembermanagement.standard;

import java.util.Map;

/**
 * 统一模板
 */
public interface UnifiedRule {

    //初始化视图
    void initView();

    //初始化数据
    void initData();

    //事件监听
    void initEvent();

    //请求成功
    void requestSuccess(String data);

    //请求失败
    void requestFail(String data);

    //api调用
    void apiCall(Map<String, Object> params);
}
