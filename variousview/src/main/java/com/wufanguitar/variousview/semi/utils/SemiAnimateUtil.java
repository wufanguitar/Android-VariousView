package com.wufanguitar.variousview.semi.utils;

import android.view.Gravity;

import com.wufanguitar.variousview.R;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

public class SemiAnimateUtil {
    private static final int INVALID = -1;

    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.semi_slide_in_bottom : R.anim.semi_slide_out_bottom;
        }
        return INVALID;
    }
}
