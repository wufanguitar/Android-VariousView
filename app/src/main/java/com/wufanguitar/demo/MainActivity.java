package com.wufanguitar.demo;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.wufanguitar.demo.pickerview.PickerViewActivity;
import com.wufanguitar.toast.Toaster;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton pickerViewBtn = (AppCompatButton) findViewById(R.id.enter_picker_view_btn);
        pickerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PickerViewActivity.class);
//                startActivity(intent);
                new Toaster.Builder(MainActivity.this).setTipStr("你好").build().show();
            }
        });
    }
}
