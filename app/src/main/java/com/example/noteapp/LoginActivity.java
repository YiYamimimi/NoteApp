package com.example.noteapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText et_password,et_name;
    private UserDbOpenHelper userDbOpenHelper;
    //数据库里存储的password
    String dbpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Thread(() -> runOnUiThread(() -> {
            btn_login= findViewById(R.id.loginbutton);
            btn_login.setOnClickListener(view -> {
                String etpassword=et_password.getText().toString();
                String key=et_name.getText().toString();
                Cursor cursor=userDbOpenHelper.getReadableDatabase().query("user",null,"username = ?",new String[]{key},null,null,null);
                //创建ArrayList对象，用于保存用户数据结果
                ArrayList<Map<String,String>> resultList = new ArrayList<>();
                while(cursor.moveToNext()){
                    //将结果集中的数据存入HashMap
                    Map<String,String> map=new HashMap<>();
                    map.put("username",cursor.getString(1));
                    map.put("password",cursor.getString(2));
                    resultList.add(map);
                    //获取数据库中符合用户名的对应的密码
                    dbpassword=map.get("password");
                }
                //数据库中没有查询到用户账号
                if(resultList.size() == 0) {
                    //提示
                    Toast.makeText(LoginActivity.this,
                            "该账号名未注册", Toast.LENGTH_LONG).show();
                } else {
                    //查到后 对比输入的密码与数据库的密码
                    if(etpassword.equals(dbpassword)){
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"密码错误,请重试",Toast.LENGTH_SHORT).show();
                    }
                };
            });
        })).start();

        //初始化
        initView();


        //定义数据库对象
        userDbOpenHelper=new UserDbOpenHelper(LoginActivity.this,"user.db", 1);

        //跳转至注册页面
        Button Registerbutton= findViewById(R.id.registerbutton);
        Registerbutton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    //定义初始化
    private void initView(){
        btn_login=findViewById(R.id.loginbutton);
        et_name=findViewById(R.id.editname);
        et_password=findViewById(R.id.password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDbOpenHelper != null) {
            userDbOpenHelper.close();
        }
    }
}
