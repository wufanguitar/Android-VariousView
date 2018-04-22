package com.wufanguitar.shadow;

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.wufanguitar.semi.utils.ScreenUtil;

public class ShadowHelper {
    private static final int DEFAULT_SHADOW_COLOR = 0x33000000;
    private static final int DEFAULT_SHADOW_CORNER_RADIUS = 3;

    public static void with(View view) {
        ShadowProperty shadowProperty = new ShadowProperty()
                .setShadowColor(DEFAULT_SHADOW_COLOR)
                .setShadowRadius(ScreenUtil.dp2px(view.getContext(), DEFAULT_SHADOW_CORNER_RADIUS))
                .setShadowSide(ShadowProperty.ALL);
        ShadowDrawable shadowDrawable = new ShadowDrawable(shadowProperty);
        ViewCompat.setBackground(view, shadowDrawable);
        ViewCompat.setLayerType(view, ViewCompat.LAYER_TYPE_SOFTWARE, null);
    }

    public static void with(View view, ShadowProperty shadowProperty) {
        ShadowDrawable shadowDrawable = new ShadowDrawable(shadowProperty);
        ViewCompat.setBackground(view, shadowDrawable);
        ViewCompat.setLayerType(view, ViewCompat.LAYER_TYPE_SOFTWARE, null);
    }
}
