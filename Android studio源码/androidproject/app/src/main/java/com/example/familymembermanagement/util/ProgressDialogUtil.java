package com.example.familymembermanagement.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.familymembermanagement.R;

/**
 * 自定义dialog
 */
public class ProgressDialogUtil {

    private static ProgressDialog dialog;

    //显示
    public static Dialog showDialog(Context context, String mesage) {
        dialog = new ProgressDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog, null);
        TextView tv = view.findViewById(R.id.dialog_message);
        tv.setText(mesage);
        dialog.setView(view);
        dialog.show();
        return dialog;
    }

    //关闭
    public static void closeDialog() {
        dialog.hide();
    }
}
