package com.wufanguitar.variousview.semi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

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
    private static final int SUBMIT_CANCEL_TEXT_DEFAULT_COLOR = 0xFF007AFF;
    private static final String DEFAULT_CLICK_TAG = "dismiss";
    private static final String TAG_LEFT = "left";
    private static final String TAG_RIGHT = "right";
    private static final String TAG_BOTTOM = "bottom";
    public int mLayoutRes;
    public ICustomLayout mCustomLayout;
    // 设置自定义Dialog
    private Dialog mDialog;
    // ContentView的点击事件
    private OnClickListener mOnClickListener;

    private RelativeLayout mTopBarRLayout;
    // 是否共用公共布局
    private boolean mShareCommonLayout;

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
    private AppCompatTextView mLeftBtn, mRightBtn;
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
    private AppCompatTextView mBottomBtn;
    // 底部文字
    private String mBottomBtnStr;
    // 底部文字颜色
    private int mBottomBtnStrColor;
    // 底部文字大小
    private int mBottomBtnStrSize;

    // 是否是对话框模式
    private boolean mIsDialog;
    // 是否能取消
    private boolean mCancelable = false;

    public ContentView(Builder builder) {
        super(builder.mContext);
        this.mShareCommonLayout = builder.mShareCommonLayout;
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
        this.mCancelable = builder.mCancelable;
        this.mCustomLayout = builder.mCustomLayout;
        this.mOnClickListener = builder.mOnClickListener;
        this.mLayoutRes = builder.mLayoutRes;
        this.mIsDialog = builder.mIsDialog;
        this.mDialog = builder.mDialog;
        initView(builder.mContext);
    }

    public static class Builder {
        private final int DEFAULT_TEXT_SIZE = 15;
        private final int DEFAULT_BOTTOM_TEXT_SIZE = 18;
        private final int DEFAULT_LEFT_RIGHT_BOTTON_TEXT_SIZE = 16;
        public int mLayoutRes = R.layout.semi_content_default_layout;
        public ICustomLayout mCustomLayout;
        private OnClickListener mOnClickListener;
        // 是否共用公共布局（开发时若多处使用时资源布局一样，只是更换字符串可以使用）
        // 默认为false
        private boolean mShareCommonLayout = false;
        private Context mContext;
        // 设置自定义Dialog
        private Dialog mDialog;
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

        // 是否能取消((默认提供关闭按钮，故此处默认为false))
        private boolean mCancelable = false;
        // 是否是对话框模式
        private boolean mIsDialog;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder(Context context, OnClickListener onClickListener) {
            this.mContext = context;
            this.mOnClickListener = onClickListener;
        }

        // 设置是否共用公共布局效果
        public Builder setShareCommonLayout(boolean shareCommonLayout) {
            this.mShareCommonLayout = shareCommonLayout;
            return this;
        }

        // 设置内容布局
        public Builder setLayoutRes(int res) {
            this.mLayoutRes = res;
            return this;
        }

        // 设置内容布局
        public Builder setLayoutRes(int res, ICustomLayout layout) {
            this.mLayoutRes = res;
            this.mCustomLayout = layout;
            return this;
        }

        public Builder setTitleStr(String titleStr) {
            this.mTitleStr = titleStr;
            return this;
        }

        public Builder setTitleStrColor(int titleStrColor) {
            this.mTitleStrColor = titleStrColor;
            return this;
        }

        public Builder setTitleStrSize(int titleStrSize) {
            this.mTitleStrSize = titleStrSize;
            return this;
        }

        public Builder setTitleBgColor(int titleBgColor) {
            this.mTitleBgColor = titleBgColor;
            return this;
        }

        public Builder setRightBtnStr(String rightBtnStr) {
            this.mRightBtnStr = rightBtnStr;
            return this;
        }

        public Builder setLeftBtnStrColor(int color) {
            this.mLeftBtnStrColor = color;
            return this;
        }

        public Builder setRightBtnStrColor(int color) {
            this.mRightBtnStrColor = color;
            return this;
        }

        public Builder setLeftBtnStr(String leftBtnStr) {
            this.mLeftBtnStr = leftBtnStr;
            return this;
        }

        public Builder setLeftRightBtnStrSize(int leftRightBtnStrSize) {
            this.mLeftRightBtnStrSize = leftRightBtnStrSize;
            return this;
        }

        public Builder setContentStr(String contentStr) {
            this.mContentStr = contentStr;
            return this;
        }

        public Builder setContentStrColor(int contentStrColor) {
            this.mContentStrColor = contentStrColor;
            return this;
        }

        public Builder setContentStrSize(int contentStrSize) {
            this.mContentStrSize = contentStrSize;
            return this;
        }

        public Builder setContentBgColor(int contentBgColor) {
            this.mContentBgColor = contentBgColor;
            return this;
        }

        public Builder setBottomBtnStr(String bottomBtnStr) {
            this.mBottomBtnStr = bottomBtnStr;
            return this;
        }

        public Builder setBottomBtnStrColor(int bottomBtnStrColor) {
            this.mBottomBtnStrColor = bottomBtnStrColor;
            return this;
        }

        public Builder setBottomBtnStrSize(int bottomBtnStrSize) {
            this.mBottomBtnStrSize = bottomBtnStrSize;
            return this;
        }

        public Builder isDialog(boolean isDialog) {
            this.mIsDialog = isDialog;
            return this;
        }

        public Builder setDialog(Dialog dialog) {
            this.mIsDialog = true;
            this.mDialog = dialog;
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
            return this;
        }

        public ContentView build() {
            return new ContentView(this);
        }
    }

    public void initView(Context context) {
        setDialog(mDialog);
        setDialogOutSideCancelable(mCancelable);
        initViews(Color.TRANSPARENT);
        init();
        // 自定义部分
        if (mCustomLayout == null && mLayoutRes == R.layout.semi_content_default_layout) {
            inflateCustomView(mLayoutRes);
            // 内容
            mContentTv = (AppCompatTextView) findViewById(R.id.tv_content);
            mContentTv.setText(TextUtils.isEmpty(mContentStr) ? "" : mContentStr);
            mContentTv.setTextColor(mContentStrColor == 0 ? CONTENT_DEFAULT_COLOR : mContentStrColor);
            mContentTv.setTextSize(mContentStrSize);
            mContentTv.setBackgroundColor(mContentBgColor == 0 ? DEFAULT_COLOR : mContentBgColor);
        } else if (mCustomLayout != null && inflateCustomView(mLayoutRes) != null) {
            mCustomLayout.customLayout(inflateCustomView(mLayoutRes));
        } else {
            inflateCustomView(mLayoutRes);
        }

        // 公共部分
        // 顶部标题
        mTitleTv = (AppCompatTextView) findViewById(R.id.title);
        if (mTitleTv != null) {
            mTitleTv.setText(TextUtils.isEmpty(mTitleStr) ? "" : mTitleStr);
            if (!mShareCommonLayout) {
                mTitleTv.setTextColor(mTitleStrColor == 0 ? TITLE_DEFAULT_COLOR : mTitleStrColor);
                mTitleTv.setTextSize(mTitleStrSize);
                mTopBarRLayout = (RelativeLayout) findViewById(R.id.rl_topbar);
                mTopBarRLayout.setBackgroundColor(mTitleBgColor == 0 ? DEFAULT_COLOR : mTitleBgColor);
            }
        }

        // 右侧(默认确定)按钮
        mRightBtn = (AppCompatTextView) findViewById(R.id.right);
        if (mRightBtn != null) {
            mRightBtn.setText(TextUtils.isEmpty(mRightBtnStr) ? "" : mRightBtnStr);
            if (!mShareCommonLayout) {
                mRightBtn.setTextColor(mRightBtnStrColor == 0 ? SUBMIT_CANCEL_TEXT_DEFAULT_COLOR : mRightBtnStrColor);
                mRightBtn.setTextSize(mLeftRightBtnStrSize);
            }
            mRightBtn.setTag(mOnClickListener != null ? TAG_RIGHT : DEFAULT_CLICK_TAG);
            if (!TextUtils.isEmpty(mRightBtnStr)) {
                mRightBtn.setOnClickListener(this);
            }
        }

        // 左侧(默认取消)按钮
        mLeftBtn = (AppCompatTextView) findViewById(R.id.left);
        if (mLeftBtn != null) {
            mLeftBtn.setText(TextUtils.isEmpty(mLeftBtnStr) ? "" : mLeftBtnStr);
            if (!mShareCommonLayout) {
                mLeftBtn.setTextColor(mLeftBtnStrColor == 0 ? SUBMIT_CANCEL_TEXT_DEFAULT_COLOR : mLeftBtnStrColor);
                mLeftBtn.setTextSize(mLeftRightBtnStrSize);
            }
            mLeftBtn.setTag(mOnClickListener != null ? TAG_LEFT : DEFAULT_CLICK_TAG);
            if (!TextUtils.isEmpty(mLeftBtnStr)) {
                mLeftBtn.setOnClickListener(this);
            }
        }

        // 底部
        mBottomBtn = (AppCompatTextView) findViewById(R.id.bottom);
        if (mBottomBtn != null) {
            mBottomBtn.setText(TextUtils.isEmpty(mBottomBtnStr) ? "" : mBottomBtnStr);
            mBottomBtn.setVisibility(TextUtils.isEmpty(mBottomBtnStr) ? View.GONE : View.VISIBLE);
            if (!mShareCommonLayout) {
                mBottomBtn.setTextColor(mBottomBtnStrColor == 0 ? DEFAULT_COLOR : mBottomBtnStrColor);
                mBottomBtn.setTextSize(mBottomBtnStrSize);
            }
            mBottomBtn.setTag(mOnClickListener != null ? TAG_BOTTOM : DEFAULT_CLICK_TAG);
            if (!TextUtils.isEmpty(mBottomBtnStr)) {
                mBottomBtn.setOnClickListener(this);
            }
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
                    mOnClickListener.onLeftClick(mContentContainer);
                }
                break;
            case TAG_RIGHT:
                if (mOnClickListener != null) {
                    mOnClickListener.onRightClick(mContentContainer);
                }
                break;
            case TAG_BOTTOM:
                if (mOnClickListener != null) {
                    mOnClickListener.onBottomClick(mContentContainer);
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
        void onLeftClick(View view);
        // 右按钮点击事件
        void onRightClick(View view);
        // 底部按钮点击事件
        void onBottomClick(View view);
    }
}
