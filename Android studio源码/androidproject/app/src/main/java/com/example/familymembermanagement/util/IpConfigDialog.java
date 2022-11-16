package com.example.familymembermanagement.util;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.familymembermanagement.R;

/**
 * 自定义对话框
 */
public class IpConfigDialog extends AlertDialog {

    private EditText iped, ported;
    private Button config;

    private String ip,port;

    public IpConfigDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_config_dialog);
        initView();
        initEvent();
    }

    /**
     * 初始化控件
     */
    public void initView() {
        iped = findViewById(R.id.ip);
        ported = findViewById(R.id.host);
        config = findViewById(R.id.ok_config);
    }

    //初始化界面控件的显示数据
    private void refreshView() {
        if (!TextUtils.isEmpty(ip)) {
            ip=iped.getText().toString();
        }
        if (!TextUtils.isEmpty(port)) {
            port=ported.getText().toString();
        }
    }

    public void initEvent() {
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IpConfigDialog.this.getContext(), "配置成功"+ip+port, Toast.LENGTH_SHORT).show();
                if (onClickConfigListener != null) {
                    onClickConfigListener.ipConfig();
                }
            }
        });
    }

    public interface onClickConfigListener {
        void ipConfig();
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    public IpConfigDialog.onClickConfigListener onClickConfigListener;

    public IpConfigDialog setOnClickConfigListener(IpConfigDialog.onClickConfigListener onClickConfigListener) {
        this.onClickConfigListener = onClickConfigListener;
        return this;
    }
}
