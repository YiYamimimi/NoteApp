package com.example.noteapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Button Registerbutton;
    private EditText Registername, Registerpassword, againpassword;
    /*数据库成员变量*/
    private UserDbOpenHelper userDbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Registerbutton = findViewById(R.id.Registerbutton);
        Registername = findViewById(R.id.Registername);
        Registerpassword = findViewById(R.id.Registerpassword);
        againpassword = findViewById(R.id.againpassword);

        //实例化数据库
        userDbOpenHelper = new UserDbOpenHelper(RegisterActivity.this, "user.db", 1);

        Registerbutton.setOnClickListener(view -> {
            //获取内容
            String Rename = Registername.getText().toString();
            String Repassword = Registerpassword.getText().toString();
            String Reagainpassword = againpassword.getText().toString();

            //判断可能发生错误的情况
            if (TextUtils.isEmpty(Rename)) {
                Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(Repassword)) {
                Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            } else if (!TextUtils.equals(Repassword, Reagainpassword)) {
                Toast.makeText(RegisterActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
            } else {
                insertData(userDbOpenHelper.getReadableDatabase(), Rename, Repassword);
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
            }
            RegisterActivity.this.finish();
        });
    }

    //插入数据
    private void insertData(SQLiteDatabase readableDatabase, String username1, String password1) {
        ContentValues values = new ContentValues();
        values.put("username", username1);
        values.put("password", password1);
        readableDatabase.insert("user", null, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDbOpenHelper != null) {
            userDbOpenHelper.close();
        }
    }
}

