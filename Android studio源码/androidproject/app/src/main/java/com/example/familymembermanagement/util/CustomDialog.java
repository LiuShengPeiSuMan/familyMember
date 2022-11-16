package com.example.familymembermanagement.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.familymembermanagement.R;

/**
 * 自定义加载弹框
 */
public class CustomDialog extends ProgressDialog {

    String xx = "";
    public TextView tv;

    public CustomDialog(Context context, String msg) {
        super(context);
        xx = msg;
    }

    public CustomDialog(Context context, int theme, String msg) {
        super(context, theme);
        xx = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //设置不可取消，点击其他区域不能取消，
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.custom_progress_dialog);
        tv = this.findViewById(R.id.tvforprogress);
        tv.setText(xx);
    }

    @Override
    public void show() {
        super.show();
    }
}
