package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    TextView textView12,textView13;
    EditText editTextTextPersonName3;
    ListView listView2;
    Helper helper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        textView12=findViewById(R.id.textView12);
        textView13=findViewById(R.id.textView13);
        editTextTextPersonName3=findViewById(R.id.editTextTextPersonName3);
        listView2=findViewById(R.id.listview2);
        helper=new Helper(SearchActivity.this);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextTextPersonName3.getText().toString();//获取输入框的值
                if (name.isEmpty()){
                    Toast.makeText(SearchActivity.this,"请输入要查询的数据",Toast.LENGTH_SHORT).show();
                    return;
                }
                db=helper.getReadableDatabase();
                //按条件查询
                cursor=db.rawQuery("SELECT * FROM student WHERE ssh=? OR ssz=?",new String[]{name,name});
                List<Map<String,String>> list=new ArrayList<>();
                if (cursor.getCount()!=0) {
                    while (cursor.moveToNext()) {
                        Map<String, String> map = new HashMap<>();
                        map.put("ssh", cursor.getString(1));
                        map.put("ssz", cursor.getString(2));
                        map.put("ssrs", cursor.getString(3));
                        list.add(map);
                    }
                    editTextTextPersonName3.setText("");
                }
                cursor.close();
                db.close();
                SimpleAdapter adapter=new SimpleAdapter(SearchActivity.this,list,R.layout.list,
                        new String[]{"ssh","ssz","ssrs"},new int[]{R.id.textView9,R.id.textView10,R.id.textView11});
                listView2.setAdapter(adapter);
            }
        });
    }
}