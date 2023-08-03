package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    private FloatingActionButton notebutton, timingbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        notebutton = findViewById(R.id.noteButton);
        timingbutton = findViewById(R.id.timingButton);
        //跳转至备忘录
        notebutton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
            startActivity(intent);
        });
        //跳转至计时
        timingbutton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TimingActivity.class);
            startActivity(intent);
        });
    }
}



