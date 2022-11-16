package com.example.familymembermanagement.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymembermanagement.R;

/**
 *自定义toast
 * */
public class ToastUtil {

    //居中
    public static void toast(Context context,String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    //自定义
    public static void customToast(Context context,int resource,String msg){
        Toast toast=new Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.toast,null);
        //图片
        ImageView imageView=view.findViewById(R.id.icon);
        imageView.setImageResource(resource);
        //文本
        TextView textView=view.findViewById(R.id.message);
        textView.setText(msg);
        toast.setView(view);
        //时长
        toast.setDuration(Toast.LENGTH_SHORT);
        //位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
