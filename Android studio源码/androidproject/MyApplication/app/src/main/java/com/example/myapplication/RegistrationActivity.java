package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    TextView textView22;
    EditText editTextTextPersonName2,editTextTextPassword2,editTextTextPassword3;
    Button button2;
    String account,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        quote();
        textView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.button2) {
                    if (editTextTextPersonName2.getText().toString().isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (editTextTextPassword2.getText().toString().isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (editTextTextPassword3.getText().toString().isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!(editTextTextPassword2.getText().toString().equals(editTextTextPassword3.getText().toString()))){
                        Toast.makeText(RegistrationActivity.this,"确保两次密码一致",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    account=editTextTextPersonName2.getText().toString();
                    password=editTextTextPassword3.getText().toString();
                        SharedPreferences.Editor editor = getSharedPreferences("mr", MODE_PRIVATE).edit();
                        editor.putString("account", account);
                        editor.putString("password", password);
                        editor.commit();

                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        intent.putExtra("account", account);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                }
            }
        });
    }
    public void quote(){
        textView22=findViewById(R.id.textView22);
        editTextTextPersonName2=findViewById(R.id.editTextTextPersonName2);
        editTextTextPassword2=findViewById(R.id.editTextTextPassword2);
        editTextTextPassword3=findViewById(R.id.editTextTextPassword3);
        button2=findViewById(R.id.button2);
    }
}