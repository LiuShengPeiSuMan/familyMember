package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText editTextTextPersonName,editTextTextPassword;
    Button button;
    TextView textView14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        quote();
        Intent intent=getIntent();
        String account=intent.getStringExtra("account");
        String password=intent.getStringExtra("password");
        editTextTextPersonName.setText(account);
        editTextTextPassword.setText(password);
        SharedPreferences sp=getSharedPreferences("mr",MODE_PRIVATE);
        String name=sp.getString("account",null);
        String password1=sp.getString("password",null);
        editTextTextPassword.setText(name);
        editTextTextPersonName.setText(password1);
        textView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent1);
            }
        });
        //登录按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(editTextTextPersonName.getText().toString().equals(name)&&editTextTextPassword.getText().toString().equals(password1))){
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });
    }
    public void quote(){
        editTextTextPersonName=findViewById(R.id.editTextTextPersonName);
        editTextTextPassword=findViewById(R.id.editTextTextPassword);
        button=findViewById(R.id.button);
        textView14=findViewById(R.id.textView14);
    }
}