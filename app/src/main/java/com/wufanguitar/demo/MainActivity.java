package com.wufanguitar.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;

import com.wufanguitar.floating.Prompt;
import com.wufanguitar.floating.Snackbar;

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
//                new Toaster.Builder(MainActivity.this).setTipStr("你好").build().show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
        Snackbar snackBar = Snackbar.make(viewGroup, "成功...", Snackbar.LENGTH_SHORT, Snackbar.APPEAR_FROM_TOP_TO_DOWN);
        snackBar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackBar.setPromptThemBackground(Prompt.SUCCESS);
        snackBar.show();
    }
}
