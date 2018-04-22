package com.wufanguitar.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.wufanguitar.shadow.ShadowHelper;
import com.wufanguitar.shadow.ShadowProperty;
import com.wufanguitar.shadow.ShadowDrawable;
import com.wufanguitar.semi.bar.SnackBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private View shadowViewA;
    private View shadowViewB;
    private View shadowViewC;
    private View shadowImageC;
    private TextView shadowTvC;

    private View shadowViewD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton pickerViewBtn = (AppCompatButton) findViewById(R.id.enter_picker_view_btn);
        pickerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
            }
        });

        initShadow();
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
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

    private void initShadow() {
        shadowViewA = findViewById(R.id.activity_main_shadow_view_a);
        shadowViewB = findViewById(R.id.activity_main_shadow_view_b);
        shadowViewC = findViewById(R.id.activity_main_shadow_view_c);
        shadowViewC.setOnClickListener(this);

        shadowImageC = findViewById(R.id.activity_main_shadow_view_c_iv);
        shadowViewD = findViewById(R.id.activity_main_shadow_view_d);

        shadowTvC = (TextView) findViewById(R.id.activity_main_shadow_view_c_tv);

        ShadowHelper.with(shadowViewA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_shadow_view_c:
                shadowTvC.append("hello shadow!...");
                break;
            default:
                break;
        }
    }
}
