package com.wufanguitar.semi;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wufanguitar.variousview.R;
import com.wufanguitar.semi.base.BaseView;
import com.wufanguitar.semi.callback.ICustomLayout;
import com.wufanguitar.semi.lib.DividerType;
import com.wufanguitar.semi.view.WheelOptions;

import java.util.List;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: 条件选择器
 */

public class OptionsWheelView<T> extends BaseView implements View.OnClickListener {
    private static final String TAG_LEFT = "left";
    private static final String TAG_RIGHT = "right";
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private WheelOptions<T> mWheelOptions;
    // OptionsWheelView的点击事件
    private OnClickListener mOnClickListener;
    // 是否共用公共布局
    private boolean mShareCommonLayout;

    // 条件选择器布局
    private int mLayoutRes;
    // 显示时的外部背景色颜色，默认是灰色
    private int mBackgroundColor;

    // 自定义布局回调接口
    private ICustomLayout mICustomLayout;
    // 顶部标题栏布局
    private RelativeLayout mTopBarRL;
    private OnOptionsSelectListener mOptionsSelectListener;

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

    // 顶部标题
    private AppCompatTextView mTitleTv;
    // 顶部标题文字
    private String mTitleStr;
    // 顶部标题文字颜色
    private int mTitleStrColor;
    // 顶部标题文字大小
    private int mTitleStrSize;

    // 滚轮背景颜色
    private int mWheelViewBgColor;
    // 顶部标题栏背景颜色
    private int mTopBarBgColor;

    // 分割线以外的文字颜色
    private int mOutTextColor;
    // 分割线之间及选中项的文字颜色
    private int mCenterTextColor;
    // 滚轮中的文字大小
    private int mContentTextSize;

    // 分割线的颜色
    private int mDividerColor;
    // 分隔线类型
    private DividerType mDividerType;

    // 条目间距倍数，默认1.6F
    private float mLineSpacingMultiplier = Builder.DEFAULT_LINE_SPACING_MULTIPLIER;
    // 是否是对话框模式
    private boolean mIsDialog;
    // 设置是否能取消（对话框模式下包括外部点击和返回键，非对话框模式仅作用于外部点击）
    private boolean mCancelable;
    // 多个滚动项时是否联动
    private boolean mLinkage;

    // 是否只显示中间的单位标签
    private boolean mCenterLabel;
    // 选项一单位标签
    private String mLabelFirst;
    // 选项二单位标签
    private String mLabelSecond;
    // 选项三单位标签
    private String mLabelThird;
    // 选项一中选项是否循环（即收尾相连）
    private boolean mIsOptionFirstLoop;
    // 选项二中选项是否循环（即收尾相连）
    private boolean mIsOptionSecondLoop;
    // 选项三中选项是否循环（即收尾相连）
    private boolean mIsOptionThirdLoop;

    // 字体样式
    private Typeface mFontType;

    // 初始化时默认的选中项位置
    private int mSelectedPositionOptionFirst;
    private int mSelectedPositionOptionSecond;
    private int mSelectedPositionOptionThird;

    // WheelView在X轴的偏移量
    private int mOptionFirstXOffset;
    private int mOptionSecondXOffset;
    private int mOptionThirdXOffset;

    // 按返回键是否可以取消
    private boolean mBackKeyCancelable;

    // 构造方法
    public OptionsWheelView(Builder builder) {
        super(builder.mContext);
        this.mShareCommonLayout = builder.mShareCommonLayout;
        this.mOptionsSelectListener = builder.mOptionsSelectListener;
        this.mOnClickListener = builder.mOnClickListener;
        this.mLeftBtnStr = builder.mLeftBtnStr;
        this.mRightBtnStr = builder.mRightBtnStr;
        this.mTitleStr = builder.mTitleStr;

        this.mLeftBtnStrColor = builder.mLeftBtnStrColor;
        this.mRightBtnStrColor = builder.mRightBtnStrColor;
        this.mTitleStrColor = builder.mTitleStrColor;
        this.mWheelViewBgColor = builder.mWheelViewBgColor;
        this.mTopBarBgColor = builder.mTopBarBgColor;

        this.mLeftRightBtnStrSize = builder.mLeftRightBtnStrSize;
        this.mTitleStrSize = builder.mTitleStrSize;
        this.mContentTextSize = builder.mContentTextSize;

        this.mIsOptionFirstLoop = builder.mIsOptionFirstLoop;
        this.mIsOptionSecondLoop = builder.mIsOptionSecondLoop;
        this.mIsOptionThirdLoop = builder.mIsOptionThirdLoop;

        this.mCancelable = builder.mCancelable;
        this.mLinkage = builder.mLinkage;
        this.mCenterLabel = builder.mCenterLabel;

        this.mLabelFirst = builder.mLabelFirst;
        this.mLabelSecond = builder.mLabelSecond;
        this.mLabelThird = builder.mLabelThird;

        this.mFontType = builder.mFontType;

        this.mSelectedPositionOptionFirst = builder.mSelectedPositionOptionFirst;
        this.mSelectedPositionOptionSecond = builder.mSelectedPositionOptionSecond;
        this.mSelectedPositionOptionThird = builder.mSelectedPositionOptionThird;
        this.mOptionFirstXOffset = builder.mOptionFirstXOffset;
        this.mOptionSecondXOffset = builder.mOptionSecondXOffset;
        this.mOptionThirdXOffset = builder.mOptionThirdXOffset;

        this.mCenterTextColor = builder.mCenterTextColor;
        this.mOutTextColor = builder.mOutTextColor;
        this.mDividerColor = builder.mDividerColor;
        this.mLineSpacingMultiplier = builder.mLineSpacingMultiplier;
        this.mICustomLayout = builder.mICustomLayout;
        this.mLayoutRes = builder.mLayoutRes;
        this.mIsDialog = builder.mIsDialog;
        this.mDividerType = builder.mDividerType;
        this.mBackgroundColor = builder.mBackgroundColor;
        this.mDecorView = builder.mDecorView;

        this.mBackKeyCancelable = builder.mBackKeyCancelable;
        initView(builder.mContext);
    }

    public static class Builder {
        private static final int DEFAULT_TEXT_SIZE = 17;
        private static final float DEFAULT_LINE_SPACING_MULTIPLIER = 1.6F;
        private int mLayoutRes = R.layout.semi_option_default_layout;
        private ICustomLayout mICustomLayout;
        private Context mContext;
        private OnOptionsSelectListener mOptionsSelectListener;
        private OnClickListener mOnClickListener;
        // 是否共用公共布局（开发时若多处使用时资源布局一样，只是更换字符串可以使用）
        // 默认为false
        private boolean mShareCommonLayout = false;

        private String mLeftBtnStr;
        private String mRightBtnStr;
        private String mTitleStr;

        private int mLeftBtnStrColor;
        private int mRightBtnStrColor;
        private int mTitleStrColor;

        private int mWheelViewBgColor;
        private int mTopBarBgColor;

        private int mLeftRightBtnStrSize = DEFAULT_TEXT_SIZE;
        private int mTitleStrSize = DEFAULT_TEXT_SIZE + 1;
        private int mContentTextSize = DEFAULT_TEXT_SIZE + 1;

        private boolean mCancelable = true;
        private boolean mLinkage = true;
        private boolean mCenterLabel = true;

        private int mOutTextColor;
        private int mCenterTextColor;
        private int mDividerColor;
        private DividerType mDividerType;
        private int mBackgroundColor;
        public ViewGroup mDecorView;

        private float mLineSpacingMultiplier = DEFAULT_LINE_SPACING_MULTIPLIER;
        private boolean mIsDialog;

        private String mLabelFirst;
        private String mLabelSecond;
        private String mLabelThird;

        private boolean mIsOptionFirstLoop = false;
        private boolean mIsOptionSecondLoop = false;
        private boolean mIsOptionThirdLoop = false;

        private Typeface mFontType;

        private int mSelectedPositionOptionFirst;
        private int mSelectedPositionOptionSecond;
        private int mSelectedPositionOptionThird;

        private int mOptionFirstXOffset;
        private int mOptionSecondXOffset;
        private int mOptionThirdXOffset;

        // 按返回键是否可以取消(默认可以取消)
        private boolean mBackKeyCancelable = true;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder(Context context, OnOptionsSelectListener listener) {
            this.mContext = context;
            this.mOptionsSelectListener = listener;
        }

        public Builder(Context context, OnClickListener onClickListener) {
            this.mContext = context;
            this.mOnClickListener = onClickListener;
        }

        public Builder setLayoutRes(int layoutRes, ICustomLayout listener) {
            this.mLayoutRes = layoutRes;
            this.mICustomLayout = listener;
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setRightBtnStr(String rightBtnStr) {
            this.mRightBtnStr = rightBtnStr;
            return this;
        }

        public Builder setLeftBtnStr(String leftBtnStr) {
            this.mLeftBtnStr = leftBtnStr;
            return this;
        }

        public Builder setTitleStr(String titleStr) {
            this.mTitleStr = titleStr;
            return this;
        }

        public Builder setDialog(boolean isDialog) {
            this.mIsDialog = isDialog;
            return this;
        }

        // 设置是否共用公共布局效果
        public Builder setShareCommonLayout(boolean shareCommonLayout) {
            this.mShareCommonLayout = shareCommonLayout;
            return this;
        }

        public Builder setRightBtnStrColor(int rightBtnStrColor) {
            this.mRightBtnStrColor = rightBtnStrColor;
            return this;
        }

        public Builder setLeftBtnStrColor(int leftBtnStrColor) {
            this.mLeftBtnStrColor = leftBtnStrColor;
            return this;
        }

        /**
         * 显示时的外部背景色颜色，默认是灰色
         */
        public Builder setBackgroundColor(int backgroundColor) {
            this.mBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 必须是 viewgroup，设置要将 wheelview 显示到的容器
         */
        public Builder setDecorView(ViewGroup decorView) {
            this.mDecorView = decorView;
            return this;
        }

        public Builder setWheelViewBgColor(int wheelViewBgColor) {
            this.mWheelViewBgColor = wheelViewBgColor;
            return this;
        }

        public Builder setTopBarBgColor(int topBarBgColor) {
            this.mTopBarBgColor = topBarBgColor;
            return this;
        }

        public Builder setTitleStrColor(int titleStrColor) {
            this.mTitleStrColor = titleStrColor;
            return this;
        }

        public Builder setLeftRightBtnStrSize(int leftRightBtnStrSize) {
            this.mLeftRightBtnStrSize = leftRightBtnStrSize;
            return this;
        }

        public Builder setTitleStrSize(int titleStrSize) {
            this.mTitleStrSize = titleStrSize;
            return this;
        }

        public Builder setContentTextSize(int contentTextSize) {
            this.mContentTextSize = contentTextSize;
            return this;
        }

        public Builder setOutSideCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setLabels(String labelFirst, String labelSecond, String labelThird) {
            this.mLabelFirst = labelFirst;
            this.mLabelSecond = labelSecond;
            this.mLabelThird = labelThird;
            return this;
        }

        /**
         * 设置间距倍数，但是只能在 1.2 - 2.0f 之间
         */
        public Builder setLineSpacingMultiplier(float lineSpacingMultiplier) {
            this.mLineSpacingMultiplier = lineSpacingMultiplier;
            return this;
        }

        /**
         * 设置分割线的颜色
         */
        public Builder setDividerColor(int dividerColor) {
            this.mDividerColor = dividerColor;
            return this;
        }

        /**
         * 设置分割线的类型
         */
        public Builder setDividerType(DividerType dividerType) {
            this.mDividerType = dividerType;
            return this;
        }

        /**
         * 设置分割线之间的文字的颜色
         */
        public Builder setCenterTextColor(int centerTextColor) {
            this.mCenterTextColor = centerTextColor;
            return this;
        }

        /**
         * 设置分割线以外文字的颜色
         */
        public Builder setOutTextColor(int outTextColor) {
            this.mOutTextColor = outTextColor;
            return this;
        }

        public Builder setFontTypeface(Typeface fontType) {
            this.mFontType = fontType;
            return this;
        }

        public Builder setLoop(boolean isOptionFirstLoop, boolean isOptionSecondLoop, boolean isOptionThirdLoop) {
            this.mIsOptionFirstLoop = isOptionFirstLoop;
            this.mIsOptionSecondLoop = isOptionSecondLoop;
            this.mIsOptionThirdLoop = isOptionThirdLoop;
            return this;
        }

        public Builder setOptionsSelectedPosition(int selectedPositionOptionFirst) {
            this.mSelectedPositionOptionFirst = selectedPositionOptionFirst;
            return this;
        }

        public Builder setOptionsSelectedPosition(int selectedPositionOptionFirst,
                                                  int selectedPositionOptionSecond) {
            this.mSelectedPositionOptionFirst = selectedPositionOptionFirst;
            this.mSelectedPositionOptionSecond = selectedPositionOptionSecond;
            return this;
        }

        public Builder setOptionsSelectedPosition(int selectedPositionOptionFirst,
                                                  int selectedPositionOptionSecond,
                                                  int selectedPositionOptionThird) {
            this.mSelectedPositionOptionFirst = selectedPositionOptionFirst;
            this.mSelectedPositionOptionSecond = selectedPositionOptionSecond;
            this.mSelectedPositionOptionThird = selectedPositionOptionThird;
            return this;
        }

        public Builder setOptionsXOffset(int optionFirstXOffset, int optionSecondXOffset, int optionThirdXOffset) {
            this.mOptionFirstXOffset = optionFirstXOffset;
            this.mOptionSecondXOffset = optionSecondXOffset;
            this.mOptionThirdXOffset = optionThirdXOffset;
            return this;
        }

        public Builder setCenterLabel(boolean isCenterLabel) {
            this.mCenterLabel = isCenterLabel;
            return this;
        }

        public Builder setBackKeyCancelable(boolean backKeyCancelable) {
            this.mBackKeyCancelable = backKeyCancelable;
            return this;
        }

        public OptionsWheelView build() {
            return new OptionsWheelView(this);
        }
    }

    @SuppressWarnings("unchecked")
    private void initView(Context context) {
        setDialogOutSideCancelable(mCancelable);
        setKeyBackCancelable(mBackKeyCancelable);
        initViews(mBackgroundColor);
        init();
        initEvents();
        if (mICustomLayout == null) {
            inflateCustomView(mLayoutRes);
        } else if (inflateCustomView(mLayoutRes) != null) {
            mICustomLayout.customLayout(inflateCustomView(mLayoutRes));
        }
        // 公共（topbar）
        // 顶部标题
        mTitleTv = (AppCompatTextView) findViewById(R.id.title);
        if (mTitleTv != null) {
            mTitleTv.setText(TextUtils.isEmpty(mTitleStr) ? "" : mTitleStr);
            if (!mShareCommonLayout) {
                mTopBarRL = (RelativeLayout) findViewById(R.id.rl_topbar);
                mTopBarRL.setBackgroundColor(mTopBarBgColor == 0 ? DEFAULT_TOPBAR_BACKGROUND_COLOR : mTopBarBgColor);
                mTitleTv.setTextSize(mTitleStrSize);
                mTitleTv.setTextColor(mTitleStrColor == 0 ? DEFAULT_TOPBAR_TITLE_STRING_COLOR : mTitleStrColor);
            }
        }

        // 顶部左侧
        mLeftBtn = (AppCompatTextView) findViewById(R.id.left);
        if (mLeftBtn != null) {
            mLeftBtn.setText(TextUtils.isEmpty(mLeftBtnStr) ? "" : mLeftBtnStr);
            if (!mShareCommonLayout) {
                mLeftBtn.setTextSize(mLeftRightBtnStrSize);
                mLeftBtn.setTextColor(mLeftBtnStrColor == 0 ? DEFAULT_LEFT_RIGHT_BUTTON_NORMAL_COLOR : mLeftBtnStrColor);
            }
            mLeftBtn.setTag(mOnClickListener != null ? TAG_LEFT : TAG_CANCEL);
            if (!TextUtils.isEmpty(mLeftBtnStr)) {
                mLeftBtn.setOnClickListener(this);
            }
        }

        // 顶部右侧按钮
        mRightBtn = (AppCompatTextView) findViewById(R.id.right);
        if (mRightBtn != null) {
            mRightBtn.setText(TextUtils.isEmpty(mRightBtnStr) ? "" : mRightBtnStr);
            if (!mShareCommonLayout) {
                mRightBtn.setTextSize(mLeftRightBtnStrSize);
                mRightBtn.setTextColor(mRightBtnStrColor == 0 ? DEFAULT_LEFT_RIGHT_BUTTON_NORMAL_COLOR : mRightBtnStrColor);
            }
            mRightBtn.setTag(mOnClickListener != null ? TAG_RIGHT : TAG_SUBMIT);
            if (!TextUtils.isEmpty(mRightBtnStr)) {
                mRightBtn.setOnClickListener(this);
            }
        }

        // 滚轮布局
        final LinearLayout optionsWheelView = (LinearLayout) findViewById(R.id.wheel_option);
        if (optionsWheelView != null) {
            optionsWheelView.setBackgroundColor(mWheelViewBgColor == 0 ? DEFAULT_WHEEL_VIEW_BACKGROUND_COLOR : mWheelViewBgColor);
            mWheelOptions = new WheelOptions(optionsWheelView, mLinkage);
            configWheelOptions();
        }

        setOutSideCancelable(mCancelable);
    }

    private void configWheelOptions() {
        mWheelOptions.setTextContentSize(mContentTextSize);
        mWheelOptions.setLabels(mLabelFirst, mLabelSecond, mLabelThird);
        mWheelOptions.setTextXOffset(mOptionFirstXOffset, mOptionSecondXOffset, mOptionThirdXOffset);
        mWheelOptions.setLoop(mIsOptionFirstLoop, mIsOptionSecondLoop, mIsOptionThirdLoop);
        mWheelOptions.setTypeface(mFontType);
        mWheelOptions.setDividerColor(mDividerColor);
        mWheelOptions.setDividerType(mDividerType);
        mWheelOptions.setLineSpacingMultiplier(mLineSpacingMultiplier);
        mWheelOptions.setOutTextColor(mOutTextColor);
        mWheelOptions.setCenterTextColor(mCenterTextColor);
        mWheelOptions.setCenterLabel(mCenterLabel);
    }

    /**
     * 设置默认选中项的位置
     */
    public OptionsWheelView setOptionsSelectedPosition(int selectedPositionOptionFirst) {
        this.mSelectedPositionOptionFirst = selectedPositionOptionFirst;
        setCurrentItems();
        return this;
    }

    public OptionsWheelView setOptionsSelectedPosition(int selectedPositionOptionFirst,
                                                       int selectedPositionOptionSecond) {
        this.mSelectedPositionOptionFirst = selectedPositionOptionFirst;
        this.mSelectedPositionOptionSecond = selectedPositionOptionSecond;
        setCurrentItems();
        return this;
    }

    public OptionsWheelView setOptionsSelectedPosition(int selectedPositionOptionFirst,
                                                       int selectedPositionOptionSecond,
                                                       int selectedPositionOptionThird) {
        this.mSelectedPositionOptionFirst = selectedPositionOptionFirst;
        this.mSelectedPositionOptionSecond = selectedPositionOptionSecond;
        this.mSelectedPositionOptionThird = selectedPositionOptionThird;
        setCurrentItems();
        return this;
    }

    private void setCurrentItems() {
        if (mWheelOptions != null) {
            mWheelOptions.setCurrentItems(mSelectedPositionOptionFirst,
                    mSelectedPositionOptionSecond, mSelectedPositionOptionThird);
        }
    }

    public OptionsWheelView setRelatedOptions(List<T> optionsItems) {
        this.setRelatedOptions(optionsItems, null, null);
        return this;
    }

    public OptionsWheelView setRelatedOptions(List<T> optionsFirstItems, List<List<T>> optionsSecondItems) {
        this.setRelatedOptions(optionsFirstItems, optionsSecondItems, null);
        return this;
    }

    public OptionsWheelView setRelatedOptions(List<T> optionsFirstItems,
                                             List<List<T>> optionsSecondItems,
                                             List<List<List<T>>> optionsThirdItems) {
        mWheelOptions.setRelatedOptions(optionsFirstItems, optionsSecondItems, optionsThirdItems);
        setCurrentItems();
        return this;
    }

    // 不联动情况下调用
    public OptionsWheelView setNoRelatedOptions(List<T> optionsFirstItems,
                                               List<T> optionsSecondItems,
                                               List<T> optionsThirdItems) {
        mWheelOptions.setNoRelatedOptions(optionsFirstItems, optionsSecondItems, optionsThirdItems);
        setCurrentItems();
        return this;
    }

    public OptionsWheelView setTitleStr(String titleStr) {
        if (mTitleTv != null) {
            mTitleTv.setText(titleStr);
        }
        return this;
    }

    public OptionsWheelView setWheelOptions(@NonNull WheelOptions<T> wheelOptions) {
        this.mWheelOptions = wheelOptions;
        configWheelOptions();
        return this;
    }

    public OptionsWheelView setOptionsXOffset(int optionFirstXOffset, int optionSecondXOffset, int optionThirdXOffset) {
        this.mOptionFirstXOffset = optionFirstXOffset;
        this.mOptionSecondXOffset = optionSecondXOffset;
        this.mOptionThirdXOffset = optionThirdXOffset;
        return this;
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
            case TAG_CANCEL:
                cancel(TAG_CANCEL);
                dismiss();
                break;
            case TAG_RIGHT:
                if (mOnClickListener != null) {
                    mOnClickListener.onRightClick(mContentContainer);
                }
                break;
            case TAG_SUBMIT:
                returnData();
                dismiss();
            default:
                break;
        }
    }

    public OptionsWheelView returnData() {
        if (mOptionsSelectListener != null) {
            int[] optionsCurrentItems = mWheelOptions.getCurrentItems();
            mOptionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2], mClickView);
        }
        return this;
    }

    public interface OnOptionsSelectListener {
        void onOptionsSelect(int optionsFirst, int optionsSecond, int optionsThird, View view);
    }

    // 添加此方法是为了方便开发者封装
    public OptionsWheelView setOnOptionsSelectListener(OnOptionsSelectListener onOptionsSelectListener) {
        this.mOptionsSelectListener = onOptionsSelectListener;
        return this;
    }

    @Override
    public boolean isDialog() {
        return mIsDialog;
    }

    public interface OnClickListener {
        // 左按钮点击事件
        void onLeftClick(View view);

        // 右按钮点击事件
        void onRightClick(View view);
    }
}
