package com.wufanguitar.variousview.semi.lib;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

final class WheelViewGestureListener extends SimpleOnGestureListener {

    final WheelView mWheelView;

    WheelViewGestureListener(WheelView wheelView) {
        mWheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mWheelView.scrollBy(velocityY);
        return true;
    }
}
