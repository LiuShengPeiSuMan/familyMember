package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    TextView textView4;
    EditText editTextTextPersonName4,editTextTextPersonName5,editTextTextPersonName6;
    Button button3;
    Helper helper;
    SQLiteDatabase db;
    ContentValues values;
    String drom,dromname,nop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        quote();
        helper=new Helper(AddActivity.this);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTextPersonName4.getText().toString().isEmpty()){
                    Toast.makeText(AddActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextTextPersonName5.getText().toString().isEmpty()){
                    Toast.makeText(AddActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextTextPersonName6.getText().toString().isEmpty()){
                    Toast.makeText(AddActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //把对应的输入框内容添加到数据库中
                    drom=editTextTextPersonName4.getText().toString();
                    dromname=editTextTextPersonName5.getText().toString();
                    nop=editTextTextPersonName6.getText().toString();
                    db = helper.getWritableDatabase();  //获取数据库可写
                    values = new ContentValues();
                    values.put("ssh", drom);
                    values.put("ssz", dromname);
                    values.put("ssrs", nop);
                    db.insert("student", null, values);
                    db.close();
                    Toast.makeText(AddActivity.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                }
                editTextTextPersonName4.setText("");
                editTextTextPersonName5.setText("");
                editTextTextPersonName6.setText("");

            }
        });
    }
    public void quote(){
        textView4=findViewById(R.id.textView4);
        editTextTextPersonName4=findViewById(R.id.editTextTextPersonName4);
        editTextTextPersonName5=findViewById(R.id.editTextTextPersonName5);
        editTextTextPersonName6=findViewById(R.id.editTextTextPersonName6);
        button3=findViewById(R.id.button3);
    }
}