package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    ListView listView1;
    ImageView imageView,imageView2;
    Helper helper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        quote();
        insert();
        cursor.close();
        db.close();
        //搜索
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        //添加
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //长按删除
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //获取ListView某一项的值
                Map<String,Object> map=(Map<String, Object>) parent.getItemAtPosition(position);
                //弹出对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("确认删除该宿舍？");
                builder.setCancelable(false);
                //点击对话框确认按钮执行删除数据语句按条件删除
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=helper.getWritableDatabase();
                        db.delete("student","ssh=?",new String[]{(String) map.get("id")});
                        db.close();
                        insert();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }
    public void quote(){
        listView1=findViewById(R.id.listView1);
        imageView=findViewById(R.id.imageView);
        imageView2=findViewById(R.id.imageView2);
    }
    public void insert(){
        helper=new Helper(HomeActivity.this);
        db=helper.getReadableDatabase();
        cursor=db.query("student",null,null,null,null,null,null);
        List<Map<String,String>> list=new ArrayList<>();
        if (cursor.getCount()!=0){
            while (cursor.moveToNext()){
                Map<String,String> map=new HashMap<>();
                map.put("id",cursor.getString(1));
                map.put("name",cursor.getString(2));
                map.put("num",cursor.getString(3));
                list.add(map);
            }
        }
        SimpleAdapter adapter=new SimpleAdapter(HomeActivity.this,list,R.layout.list,
                new String[]{"id","name","num"},new int[]{R.id.textView9,R.id.textView10,R.id.textView11});
        listView1.setAdapter(adapter);
    }
}