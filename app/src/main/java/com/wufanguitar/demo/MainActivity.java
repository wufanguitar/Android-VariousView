package com.wufanguitar.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.wufanguitar.demo.pickerview.PickerViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton pickerViewBtn = (AppCompatButton) findViewById(R.id.enter_picker_view_btn);
        pickerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PickerViewActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
    }
}
