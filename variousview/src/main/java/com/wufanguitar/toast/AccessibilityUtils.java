package com.wufanguitar.toast;

import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

public class AccessibilityUtils {

    @SuppressWarnings("UnusedReturnValue")
    public static boolean sendAccessibilityEvent(View view) {
        final AccessibilityManager accessibilityManager = (AccessibilityManager)
                view.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (!accessibilityManager.isEnabled()) return false;

        final AccessibilityEvent accessibilityEvent = AccessibilityEvent
                .obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        accessibilityEvent.setClassName(view.getClass().getName());
        accessibilityEvent.setPackageName(view.getContext().getPackageName());

        view.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityManager.sendAccessibilityEvent(accessibilityEvent);
        return true;
    }
}
