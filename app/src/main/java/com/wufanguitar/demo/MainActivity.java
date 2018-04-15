package com.wufanguitar.demo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;

import com.wufanguitar.floating.Prompt;
import com.wufanguitar.floating.Snackbar;
import com.wufanguitar.semi.bar.SnackBar;
import com.wufanguitar.semi.callback.ICustomLayout;

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
                String m = "sdfasdfasdf\nasdfasdf\nasdfasdfasdf";
                String s = "sdfasdfasdf";
//                Intent intent = new Intent(MainActivity.this, PickerViewActivity.class);
//                startActivity(intent);
//                new Toaster.Builder(MainActivity.this).setTipStr("你好").build().show();
                SnackBar.make(getWindow().getDecorView(), m, SnackBar.LENGTH_LONG)
                        .setBackgroundColor(Color.WHITE)
                        .setDisplayDirection(SnackBar.DISPLAY_ON_TOP)
                        .addIconWithAnimation(R.drawable.iconfont_automate_clockin_tips_success, 0, 0, R.anim.snackbar_icon_pulse)
                        .enableSwipeToDismiss()
//                        .customLayout(R.layout.clock_in_click_area_layout, new ICustomLayout() {
//                            @Override
//                            public void customLayout(View v) {
//
//                            }
//                        })
                        .show();
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
//        final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
//        Snackbar snackBar = Snackbar.make(getWindow().getDecorView(), "成功...", Snackbar.LENGTH_SHORT, Snackbar.APPEAR_FROM_TOP_TO_DOWN);
//        snackBar.setAction("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        snackBar.setPromptThemBackground(Prompt.SUCCESS);
//        snackBar.show();
        String m = "sdfasdfasdf\nasdfasdf\nasdfasdfasdf";
        String s = "sdfasdfasdf";
        SnackBar.make(getWindow().getDecorView(), m, SnackBar.LENGTH_LONG)
                .setBackgroundColor(Color.WHITE)
                .setDisplayDirection(SnackBar.DISPLAY_ON_TOP)
                .addIconWithAnimation(R.drawable.iconfont_automate_clockin_tips_success, 0, 0, R.anim.snackbar_icon_pulse)
                .enableSwipeToDismiss()
//                        .customLayout(R.layout.clock_in_click_area_layout, new ICustomLayout() {
//                            @Override
//                            public void customLayout(View v) {
//
//                            }
//                        })
                .show();
    }
}
