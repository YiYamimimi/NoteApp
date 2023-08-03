package com.example.noteapp;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class TimingActivity extends AppCompatActivity {
    TomatoView clockView;
    private QMUIRoundButton btnStart;//半圆按钮
    private QMUIRoundButton btnStop;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        if(Build.VERSION.SDK_INT >= 24) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }


        initView();
//开始的监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockView.start();

            }
        });
//结束的监听
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockView.stop();
            }
        });
    }
    //获取
    private void initView() {
        clockView = findViewById(R.id.clockView);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
    }
}

