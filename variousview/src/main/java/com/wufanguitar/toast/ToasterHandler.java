package com.wufanguitar.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

class ToasterHandler extends Handler {
    private static final int DEFAULT_HANDLER_CAPACITY = 10;
    private static final String ERROR_ST_WINDOWMANAGER_NULL = "The Toaster's WindowManager " +
            "was null when trying to remove the Toaster.";

    private static ToasterHandler mToaster;
    private final PriorityQueue<Toaster> superToastPriorityQueue;

    private static final class Messages {
        private static final int DISPLAY_SUPERTOAST = 0x0001;
        private static final int SHOW_NEXT = 0x0002;
        private static final int REMOVE_SUPERTOAST = 0x0003;
    }

    private class SuperToastComparator implements Comparator<Toaster> {

        @Override
        public int compare(Toaster x, Toaster y) {
            // 这里 -1 表示升序，1 表示降序
            return x.isShowing() ? -1 : x.getTimestamp() <= y.getTimestamp() ? -1 : 1;
        }
    }

    static synchronized ToasterHandler getInstance() {
        if (mToaster != null) return mToaster;
        else {
            mToaster = new ToasterHandler();
            return mToaster;
        }
    }

    private ToasterHandler() {
        superToastPriorityQueue = new PriorityQueue<>(DEFAULT_HANDLER_CAPACITY, new SuperToastComparator());
    }

    void add(Toaster superToast) {
        superToastPriorityQueue.add(superToast);
        showNextSuperToast();
    }

    private void showNextSuperToast() {
        if (superToastPriorityQueue.isEmpty()) return;
        final Toaster superToast = superToastPriorityQueue.peek();
        if (!superToast.isShowing()) {
            final Message message = obtainMessage(Messages.DISPLAY_SUPERTOAST);
            message.obj = superToast;
            sendMessage(message);
        }
    }

    private void sendDelayedMessage(Toaster superToast, int messageId, long delay) {
        Message message = obtainMessage(messageId);
        message.obj = superToast;
        sendMessageDelayed(message, delay);
    }

    @Override
    public void handleMessage(Message message) {
        final Toaster superToast = (Toaster) message.obj;
        switch (message.what) {
            case Messages.SHOW_NEXT:
                showNextSuperToast();
                break;
            case Messages.DISPLAY_SUPERTOAST:
                displaySuperToast(superToast);
                break;
            case Messages.REMOVE_SUPERTOAST:
                removeSuperToast(superToast);
                break;
            default:
                super.handleMessage(message);
                break;
        }
    }

    private void displaySuperToast(Toaster superToast) {
        if (superToast.isShowing()) return;

        final WindowManager windowManager = (WindowManager) superToast.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        if (windowManager == null)
            throw new IllegalStateException(ERROR_ST_WINDOWMANAGER_NULL);

        try {
            windowManager.addView(superToast.getView(), superToast.getWindowManagerParams());
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e(getClass().getName(), illegalArgumentException.toString());
        }
        sendDelayedMessage(superToast, Messages.REMOVE_SUPERTOAST,
                superToast.getDuration());
    }

    private void removeSuperToast(final Toaster superToast) {

        final WindowManager windowManager = (WindowManager) superToast.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        if (windowManager == null)
            throw new IllegalStateException(ERROR_ST_WINDOWMANAGER_NULL);

        try {
            windowManager.removeView(superToast.getView());
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e(getClass().getName(), illegalArgumentException.toString());
        }

        this.sendDelayedMessage(superToast, Messages.SHOW_NEXT, 0);

        superToastPriorityQueue.poll();
    }

    void cancelAllSuperToasts() {
        removeMessages(Messages.SHOW_NEXT);
        removeMessages(Messages.DISPLAY_SUPERTOAST);
        removeMessages(Messages.REMOVE_SUPERTOAST);

        for (Toaster superToast : superToastPriorityQueue) {
            final WindowManager windowManager = (WindowManager) superToast.getContext()
                    .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            if (superToast.isShowing()) {
                try {
                    windowManager.removeView(superToast.getView());
                } catch (NullPointerException | IllegalArgumentException exception) {
                    Log.e(getClass().getName(), exception.toString());
                }
            }
        }
        superToastPriorityQueue.clear();
    }
}
