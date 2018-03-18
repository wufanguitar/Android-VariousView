package com.wufanguitar.toast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wufanguitar.semi.callback.ICustomLayout;
import com.wufanguitar.variousview.R;
import com.wufanguitar.semi.callback.ICustomLayout;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: 自定义Toast
 */

public class Toaster {
    private Context mContext;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private int mLayoutRes;
    private ICustomLayout mCustomLayout;
    private int mGravity;
    private int mDuration;
    private long mTimestamp;
    // 提示信息
    private String mTipStr;
    private int mTipRes;

    public Toaster(Builder builder) {
        this.mContext = builder.mContext;
        this.mParams = builder.mParams;
        this.mLayoutRes = builder.mLayoutRes;
        this.mCustomLayout = builder.mCustomLayout;
        this.mGravity = builder.mGravity;
        this.mTipStr = builder.mTipStr;
        this.mTipRes = builder.mTipRes;
        this.mDuration = builder.mDuration;
        initViews();
    }

    public static class Builder {
        private Context mContext;
        private ICustomLayout mCustomLayout;
        private int mGravity = Gravity.CENTER_VERTICAL;
        private int mDuration = 1000;
        private WindowManager.LayoutParams mParams;
        // 给Toast之外的窗口设置背景(如半透明)
        // 此时mParams的宽高一定不能为wrap_content, 否则没有效果
        // 该控件默认设置为match_parent
        private int mWindowBg;
        // 自定义的布局
        private int mLayoutRes = R.layout.toast_default_layout;
        // 提示信息
        private String mTipStr;
        private int mTipRes;

        public Builder(Context context) {
            initParams();
            this.mContext = context;
        }

        public Builder(Context context, @ColorRes int windowBg) {
            initParams();
            this.mContext = context;
            this.mWindowBg = windowBg;
            this.mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            this.mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        }

        public Builder setWindowLayoutParams(WindowManager.LayoutParams params) {
            this.mParams = params;
            return this;
        }

        public Builder setLayoutRes(int layoutRes) {
            this.mLayoutRes = layoutRes;
            return this;
        }

        public Builder setLayoutRes(int layoutRes, ICustomLayout customLayout) {
            this.mLayoutRes = layoutRes;
            this.mCustomLayout = customLayout;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public Builder setDuration(@IntRange(from = 1000, to = 5000) int duration) {
            this.mDuration = duration;
            return this;
        }

        public Builder setTipStr(String tipStr) {
            this.mTipStr = tipStr;
            return this;
        }

        public Builder setTipRes(@StringRes int tipRes) {
            this.mTipRes = tipRes;
            return this;
        }

        public Toaster build() {
            return new Toaster(this);
        }

        /**
         * 初始化窗体属性
         */
        private void initParams() {
            mParams = new WindowManager.LayoutParams();
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            // 类型
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            // 透明，不透明会出现重叠效果
            mParams.format = PixelFormat.TRANSLUCENT;
            // 位置属性
            mParams.gravity = mGravity;
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }
    }

    protected WindowManager.LayoutParams getWindowManagerParams() {
        return mParams;
    }

    private void initViews() {
        // 初始化吐司窗口布局
        mView = View.inflate(mContext, mLayoutRes, null);
        AppCompatTextView toastTv = (AppCompatTextView) mView.findViewById(R.id.toaster_message);
        if (toastTv == null) {
            mView = new AppCompatTextView(mContext);
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER);
            mView.setLayoutParams(layoutParams);
            ((AppCompatTextView) mView).setText(getTips());
        } else {
            toastTv.setText(getTips());
        }
        if (mCustomLayout != null) {
            mCustomLayout.customLayout(mView);
        }
    }

    private String getTips() {
        return TextUtils.isEmpty(mTipStr) ?
                mTipRes != 0 ? mContext.getString(mTipRes) : "" :
                mTipStr;
    }

    public void show() {
        mTimestamp = System.currentTimeMillis();
        ToasterHandler.getInstance().add(this);
        AccessibilityUtils.sendAccessibilityEvent(mView);
    }

    public boolean isShowing() {
        return mView != null && mView.isShown();
    }

    public View getView() {
        return mView;
    }

    public Context getContext() {
        return mContext;
    }

    public int getDuration() {
        return mDuration;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

}
