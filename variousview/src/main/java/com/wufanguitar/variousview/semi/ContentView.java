package com.wufanguitar.variousview.semi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wufanguitar.variousview.R;
import com.wufanguitar.variousview.semi.base.BaseView;
import com.wufanguitar.variousview.semi.callback.ICustomLayout;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: 内容展示
 */

public class ContentView extends BaseView implements View.OnClickListener{
    private static final int DEFAULT_COLOR = 0xFFFFFFFF;
    private static final int TITLE_DEFAULT_COLOR = 0xdd888888;
    private static final int CONTENT_DEFAULT_COLOR = 0xdd666666;
    private static final int BOTTOM_BACKGROUND_DEFAULT_COLOR = 0xFFCE0000;
    private static final int SUBMIT_CANCEL_TEXT_DEFAULT_COLOR = 0xFF007AFF;
    private static final String DEFAULT_CLICK_TAG = "dismiss";
    private static final String TAG_LEFT = "left";
    private static final String TAG_RIGHT = "right";
    private static final String TAG_BOTTOM = "bottom";
    private int mLayoutRes;
    private ICustomLayout mCustomLayout;
    private OnClickListener mOnClickListener;

    private RelativeLayout mTopBarRLayout;

    // 顶部标题
    private AppCompatTextView mTitleTv;
    // 顶部标题文字
    private String mTitleStr;
    // 顶部标题文字颜色
    private int mTitleStrColor;
    // 顶部标题文字大小
    private int mTitleStrSize;
    // 顶部标题背景颜色
    private int mTitleBgColor;

    // 顶部左侧/右侧按钮
    private AppCompatButton mLeftBtn, mRightBtn;
    // 顶部左侧按钮文字
    private String mLeftBtnStr;
    // 顶部左侧按钮文字颜色
    private int mLeftBtnStrColor;
    // 顶部右侧按钮文字
    private String mRightBtnStr;
    // 顶部右侧按钮文字颜色
    private int mRightBtnStrColor;
    // 顶部左侧/右侧按钮文字大小
    private int mLeftRightBtnStrSize;

    private AppCompatTextView mContentTv;
    // 内容文字
    private String mContentStr;
    // 内容颜色
    private int mContentStrColor;
    // 内容文字大小
    private int mContentStrSize;
    // 内容背景颜色
    private int mContentBgColor;

    // 底部按钮
    private AppCompatButton mBottomBtn;
    // 底部文字
    private String mBottomBtnStr;
    // 底部文字颜色
    private int mBottomBtnStrColor;
    // 底部文字大小
    private int mBottomBtnStrSize;
    // 底部背景颜色
    private int mBottomBgColor;

    // 是否是对话框模式
    private boolean mIsDialog;
    // 是否能取消
    private boolean mCancelable = false;

    public ContentView(ContentView.Builder builder) {
        super(builder.mContext);
        this.mTitleStr = builder.mTitleStr;
        this.mTitleStrColor = builder.mTitleStrColor;
        this.mTitleStrSize = builder.mTitleStrSize;
        this.mTitleBgColor = builder.mTitleBgColor;
        this.mLeftBtnStr = builder.mLeftBtnStr;
        this.mLeftBtnStrColor = builder.mLeftBtnStrColor;
        this.mRightBtnStr = builder.mRightBtnStr;
        this.mRightBtnStrColor = builder.mRightBtnStrColor;
        this.mLeftRightBtnStrSize = builder.mLeftRightBtnStrSize;
        this.mContentStr = builder.mContentStr;
        this.mContentStrColor = builder.mContentStrColor;
        this.mContentStrSize = builder.mContentStrSize;
        this.mContentBgColor = builder.mContentBgColor;
        this.mBottomBtnStr = builder.mBottomBtnStr;
        this.mBottomBtnStrColor = builder.mBottomBtnStrColor;
        this.mBottomBtnStrSize = builder.mBottomBtnStrSize;
        this.mBottomBgColor = builder.mBottomBgColor;
        this.mCancelable = builder.mCancelable;
        this.mCustomLayout = builder.mCustomLayout;
        this.mOnClickListener = builder.mOnClickListener;
        this.mLayoutRes = builder.mLayoutRes;
        this.mIsDialog = builder.mIsDialog;
        initView(builder.mContext);
    }

    public static class Builder {
        private final int DEFAULT_TEXT_SIZE = 15;
        private final int DEFAULT_BOTTOM_TEXT_SIZE = 18;
        private final int DEFAULT_LEFT_RIGHT_BOTTON_TEXT_SIZE = 16;
        private int mLayoutRes = R.layout.view_content_default;
        private ICustomLayout mCustomLayout;
        private OnClickListener mOnClickListener;
        private Context mContext;
        // 标题文字
        private String mTitleStr;
        // 标题颜色
        private int mTitleStrColor;
        // 标题文字大小
        private int mTitleStrSize = DEFAULT_TEXT_SIZE;
        // 标题背景颜色
        private int mTitleBgColor;

        // 右侧按钮文字
        private String mRightBtnStr;
        // 左侧按钮文字
        private String mLeftBtnStr;
        // 左侧文字颜色
        private int mLeftBtnStrColor;
        // 右侧文字颜色
        private int mRightBtnStrColor;
        // 右侧/左侧文字大小
        private int mLeftRightBtnStrSize = DEFAULT_LEFT_RIGHT_BOTTON_TEXT_SIZE;

        // 内容文字
        private String mContentStr;
        // 内容颜色
        private int mContentStrColor;
        // 内容文字大小
        private int mContentStrSize = DEFAULT_TEXT_SIZE;
        // 内容背景颜色
        private int mContentBgColor;

        // 底部文字
        private String mBottomBtnStr;
        // 底部文字颜色
        private int mBottomBtnStrColor;
        // 底部文字大小
        private int mBottomBtnStrSize = DEFAULT_BOTTOM_TEXT_SIZE;
        // 底部背景颜色
        private int mBottomBgColor;

        // 是否能取消((默认提供关闭按钮，故此处默认为false))
        private boolean mCancelable = false;
        // 是否是对话框模式
        private boolean mIsDialog;

        public Builder(Context context) {
            this.mContext = context;
        }

        // 设置内容布局
        public ContentView.Builder setLayoutRes(int res) {
            this.mLayoutRes = res;
            return this;
        }

        // 设置内容布局
        public ContentView.Builder setLayoutRes(int res, ICustomLayout layout) {
            this.mLayoutRes = res;
            this.mCustomLayout = layout;
            return this;
        }

        public ContentView.Builder setTitleStr(String titleStr) {
            this.mTitleStr = titleStr;
            return this;
        }

        public ContentView.Builder setTitleStrColor(int titleStrColor) {
            this.mTitleStrColor = titleStrColor;
            return this;
        }

        public ContentView.Builder setTitleStrSize(int titleStrSize) {
            this.mTitleStrSize = titleStrSize;
            return this;
        }

        public ContentView.Builder setTitleBgColor(int titleBgColor) {
            this.mTitleBgColor = titleBgColor;
            return this;
        }

        public ContentView.Builder setRightBtnStr(String rightBtnStr) {
            this.mRightBtnStr = rightBtnStr;
            return this;
        }

        public ContentView.Builder setLeftBtnStrColor(int color) {
            this.mLeftBtnStrColor = color;
            return this;
        }

        public ContentView.Builder setRightBtnStrColor(int color) {
            this.mRightBtnStrColor = color;
            return this;
        }

        public ContentView.Builder setLeftBtnStr(String leftBtnStr) {
            this.mLeftBtnStr = leftBtnStr;
            return this;
        }

        public ContentView.Builder setLeftRightBtnStrSize(int leftRightBtnStrSize) {
            this.mLeftRightBtnStrSize = leftRightBtnStrSize;
            return this;
        }

        public ContentView.Builder setContentStr(String contentStr) {
            this.mContentStr = contentStr;
            return this;
        }

        public ContentView.Builder setContentStrColor(int contentStrColor) {
            this.mContentStrColor = contentStrColor;
            return this;
        }

        public ContentView.Builder setContentStrSize(int contentStrSize) {
            this.mContentStrSize = contentStrSize;
            return this;
        }

        public ContentView.Builder setContentBgColor(int contentBgColor) {
            this.mContentBgColor = contentBgColor;
            return this;
        }

        public ContentView.Builder setBottomBtnStr(String bottomBtnStr) {
            this.mBottomBtnStr = bottomBtnStr;
            return this;
        }

        public ContentView.Builder setBottomBtnStrColor(int bottomBtnStrColor) {
            this.mBottomBtnStrColor = bottomBtnStrColor;
            return this;
        }

        public ContentView.Builder setBottomBtnStrSize(int bottomBtnStrSize) {
            this.mBottomBtnStrSize = bottomBtnStrSize;
            return this;
        }

        public ContentView.Builder setBottomBgColor(int bottomBgColor) {
            this.mBottomBgColor = bottomBgColor;
            return this;
        }

        public ContentView.Builder isDialog(boolean isDialog) {
            this.mIsDialog = isDialog;
            return this;
        }

        public ContentView.Builder setOnClickListener(OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
            return this;
        }

        public ContentView build() {
            return new ContentView(this);
        }
    }


    private void initView(Context context) {
        setDialogOutSideCancelable(mCancelable);
        initViews(Color.TRANSPARENT);
        init();
        // 自定义部分
        if (mCustomLayout == null && mLayoutRes == R.layout.view_content_default) {
            LayoutInflater.from(context).inflate(mLayoutRes, mContentContainer);
            // 内容
            mContentTv = (AppCompatTextView) findViewById(R.id.content_tv);
            mContentTv.setText(TextUtils.isEmpty(mContentStr) ? "" : mContentStr);
            mContentTv.setTextColor(mContentStrColor == 0 ? CONTENT_DEFAULT_COLOR : mContentStrColor);
            mContentTv.setTextSize(mContentStrSize);
            mContentTv.setBackgroundColor(mContentBgColor == 0 ? DEFAULT_COLOR : mContentBgColor);

            // 底部
            mBottomBtn = (AppCompatButton) findViewById(R.id.content_bottom_btn);
            mBottomBtn.setText(TextUtils.isEmpty(mBottomBtnStr) ? "" : mBottomBtnStr);
            mBottomBtn.setVisibility(TextUtils.isEmpty(mBottomBtnStr) ? View.GONE : View.VISIBLE);
            mBottomBtn.setTextColor(mBottomBtnStrColor == 0 ? DEFAULT_COLOR : mBottomBtnStrColor);
            mBottomBtn.setTextSize(mBottomBtnStrSize);
            mBottomBtn.setBackgroundColor(mBottomBgColor == 0 ? BOTTOM_BACKGROUND_DEFAULT_COLOR : mBottomBgColor);
            mBottomBtn.setTag(mOnClickListener != null ? TAG_BOTTOM : DEFAULT_CLICK_TAG);
            mBottomBtn.setOnClickListener(this);
        } else if (mCustomLayout != null) {
            mCustomLayout.customLayout(LayoutInflater.from(context).inflate(mLayoutRes, mContentContainer));
        } else {
            LayoutInflater.from(context).inflate(mLayoutRes, mContentContainer);
        }

        // 公共部分
        mTopBarRLayout = (RelativeLayout) findViewById(R.id.rl_topbar);
        // 顶部标题
        mTitleTv = (AppCompatTextView) findViewById(R.id.tv_title);
        mTitleTv.setText(TextUtils.isEmpty(mTitleStr) ? "" : mTitleStr);
        mTitleTv.setTextColor(mTitleStrColor == 0 ? TITLE_DEFAULT_COLOR : mTitleStrColor);
        mTitleTv.setTextSize(mTitleStrSize);
        mTopBarRLayout.setBackgroundColor(mTitleBgColor == 0 ? DEFAULT_COLOR : mTitleBgColor);

        // 右侧(默认确定)按钮
        mRightBtn = (AppCompatButton) findViewById(R.id.btn_right);
        mRightBtn.setText(TextUtils.isEmpty(mRightBtnStr) ? "" : mRightBtnStr);
        mRightBtn.setTextColor(mRightBtnStrColor == 0 ? SUBMIT_CANCEL_TEXT_DEFAULT_COLOR : mRightBtnStrColor);
        mRightBtn.setTextSize(mLeftRightBtnStrSize);
        mRightBtn.setTag(mOnClickListener != null ? TAG_RIGHT : DEFAULT_CLICK_TAG);
        if (!TextUtils.isEmpty(mRightBtnStr)) {
            mRightBtn.setOnClickListener(this);
        }

        // 左侧(默认取消)按钮
        mLeftBtn = (AppCompatButton) findViewById(R.id.btn_left);
        mLeftBtn.setText(TextUtils.isEmpty(mLeftBtnStr) ? "" : mLeftBtnStr);
        mLeftBtn.setTextColor(mLeftBtnStrColor == 0 ? SUBMIT_CANCEL_TEXT_DEFAULT_COLOR : mLeftBtnStrColor);
        mLeftBtn.setTextSize(mLeftRightBtnStrSize);
        mLeftBtn.setTag(mOnClickListener != null ? TAG_LEFT : DEFAULT_CLICK_TAG);
        if (!TextUtils.isEmpty(mLeftBtnStr)) {
            mLeftBtn.setOnClickListener(this);
        }

        setOutSideCancelable(mCancelable);
    }

    @Override
    public boolean isDialog() {
        return mIsDialog;
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        switch (tag) {
            case TAG_LEFT:
                if (mOnClickListener != null) {
                    mOnClickListener.onLeftClick(this, v);
                }
                break;
            case TAG_RIGHT:
                if (mOnClickListener != null) {
                    mOnClickListener.onRightClick(this, v);
                }
                break;
            case TAG_BOTTOM:
                if (mOnClickListener != null) {
                    mOnClickListener.onBottomClick(this, v);
                }
                break;
            case DEFAULT_CLICK_TAG:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnClickListener {
        // 左按钮点击事件
        void onLeftClick(ContentView pickerView, View view);
        // 右按钮点击事件
        void onRightClick(ContentView pickerView, View view);
        // 底部按钮点击事件
        void onBottomClick(ContentView pickerView, View view);
    }
}
