package com.example.familymembermanagement.standard;

import android.view.View;

import java.util.Map;

public interface UnifiedRuleFragment {

    //初始化视图
    void initView(View view);

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
