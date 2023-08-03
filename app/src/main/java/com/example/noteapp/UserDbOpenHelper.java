package com.example.noteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbOpenHelper extends SQLiteOpenHelper {
    //创建用户数据表
    final String CREATE_USER_SQL=
            "create table user(_id integer primary " + "key autoincrement , username, password)";
    public UserDbOpenHelper(Context context, String name, int version){
        super(context,name,null,1);
    }
    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
