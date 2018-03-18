package com.wufanguitar.semi.lib;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

final class OnItemSelectedRunnable implements Runnable {
    final WheelView mWheelView;

    public OnItemSelectedRunnable(WheelView wheelView) {
        mWheelView = wheelView;
    }

    @Override
    public final void run() {
        if (mWheelView == null || mWheelView.getOnItemSelectedListener() == null) {
            return;
        }
        mWheelView.getOnItemSelectedListener().onItemSelected(mWheelView.getCurrentItem());
    }
}
