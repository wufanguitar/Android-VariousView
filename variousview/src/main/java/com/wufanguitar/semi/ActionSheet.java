package com.wufanguitar.semi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Dimension;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wufanguitar.semi.base.BaseView;
import com.wufanguitar.variousview.R;

import java.util.List;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: 自定义ActionSheet
 */
public class ActionSheet extends BaseView implements View.OnClickListener {
    private static final int BOTTOM_BUTTON_ID = 100;
    private Context mContext;
    // ActionSheet的点击事件
    private OnClickListener mOnClickListener;
    // RecycleView中的数据
    private String[] mItemStrArray;
    private List<String> mItemStrList;

    // 各item
    // 颜色
    private int mItemStrColor;
    // 文字大小
    private int mItemStrSize;
    // 背景颜色
    private int mItemBgColor;
    // 高度
    private float mItemHeight;

    // 底部按钮
    private AppCompatTextView mBottomBtn;
    // 底部按钮高度
    private float mBottomBtnHeight;
    // 底部按钮topMargin
    private float mBottomBtnTopMargin;
    // 底部文字
    private String mBottomBtnStr;
    // 底部文字颜色
    private int mBottomBtnStrColor;
    // 底部文字大小
    private int mBottomBtnStrSize;
    // 底部背景颜色
    private int mBottomBtnBgColor;

    // 按返回键是否可以取消
    private boolean mBackKeyCancelable;
    // 点击外部是否可以取消
    private boolean mOutSideCancelable;

    // 设置ActionSheet的Padding
    private float[] mActionSheetPadding;
    // 设置ActionSheet的圆角
    private float mCornerRadius;

    public ActionSheet(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mOnClickListener = builder.mOnClickListener;
        this.mItemStrArray = builder.mItemStrArray;
        this.mItemStrList = builder.mItemStrList;
        this.mItemStrColor = builder.mItemStrColor;
        this.mItemStrSize = builder.mItemStrSize;
        this.mItemBgColor = builder.mItemBgColor;
        this.mItemHeight = builder.mItemHeight;
        this.mBottomBtnStr = builder.mBottomBtnStr;
        this.mBottomBtnHeight = builder.mBottomBtnHeight;
        this.mBottomBtnTopMargin = builder.mBottomBtnTopMargin;
        this.mBottomBtnStrColor = builder.mBottomBtnStrColor;
        this.mBottomBtnStrSize = builder.mBottomBtnStrSize;
        this.mBottomBtnBgColor = builder.mBottomBtnBgColor;
        this.mBackKeyCancelable = builder.mBackKeyCancelable;
        this.mOutSideCancelable = builder.mOutSideCancelable;
        this.mActionSheetPadding = builder.mActionSheetPadding;
        this.mCornerRadius = builder.mCornerRadius;
        initView();
    }

    public static class Builder {
        private Context mContext;
        private OnClickListener mOnClickListener;
        private static final int COLOR_BLUE = 0xFF1E82FF;

        // 各item
        private String[] mItemStrArray;
        private List<String> mItemStrList;
        private int mItemStrColor = COLOR_BLUE;
        private int mItemStrSize = 16;
        private int mItemBgColor = Color.WHITE;
        private float mItemHeight = 40;
        // 底部按钮
        private String mBottomBtnStr;
        private float mBottomBtnHeight = 40f;
        private float mBottomBtnTopMargin = 10f;
        private int mBottomBtnStrColor = COLOR_BLUE;
        private int mBottomBtnStrSize = 16;
        private int mBottomBtnBgColor = Color.WHITE;

        private boolean mBackKeyCancelable = true;
        // 是否能取消((默认提供关闭按钮，故此处默认为false))
        private boolean mOutSideCancelable = false;

        private float[] mActionSheetPadding = new float[]{10f, 0f, 10f, 10f};
        private float mCornerRadius = 5f;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder(Context context, OnClickListener onClickListener) {
            this.mContext = context;
            this.mOnClickListener = onClickListener;
        }

        public Builder setItemStrArray(String[] itemStrArray) {
            this.mItemStrArray = itemStrArray;
            return this;
        }

        public Builder setItemStrList(List<String> itemStrList) {
            this.mItemStrList = itemStrList;
            return this;
        }

        public Builder setItemStrColor(int itemStrColor) {
            this.mItemStrColor = itemStrColor;
            return this;
        }

        public Builder setItemStrSize(int itemStrSize) {
            this.mItemStrSize = itemStrSize;
            return this;
        }

        public Builder setItemBgColor(int itemBgColor) {
            this.mItemBgColor = itemBgColor;
            return this;
        }

        public Builder setItemHeight(float itemHeight) {
            this.mItemHeight = itemHeight;
            return this;
        }

        public Builder setBottomBtnHeight(float bottomBtnHeight) {
            this.mBottomBtnHeight = bottomBtnHeight;
            return this;
        }

        public Builder setBottomBtnTopMargin(float bottomBtnTopMargin) {
            this.mBottomBtnTopMargin = bottomBtnTopMargin;
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

        public Builder setBottomBtnBgColor(int bottomBtnBgColor) {
            this.mBottomBtnBgColor = bottomBtnBgColor;
            return this;
        }

        public Builder setBackKeyCancelable(boolean backKeyCancelable) {
            this.mBackKeyCancelable = backKeyCancelable;
            return this;
        }

        public Builder setOutSideCancelable(boolean outSideCancelable) {
            this.mOutSideCancelable = outSideCancelable;
            return this;
        }

        public Builder setActionSheetPadding(float[] actionSheetPadding) {
            this.mActionSheetPadding = actionSheetPadding;
            return this;
        }

        public Builder setCornerRadius(float cornerRadius) {
            this.mCornerRadius = cornerRadius;
            return this;
        }

        public ActionSheet build() {
            return new ActionSheet(this);
        }
    }

    public void initView() {
        setKeyBackCancelable(mBackKeyCancelable);
        setOutSideCancelable(mOutSideCancelable);
        initViews(Color.TRANSPARENT);
        init();
        createDefaultView();
    }

    private void createDefaultView() {
        LinearLayout panel = new LinearLayout(mContext);
        panel.setLayoutParams(mParams);
        panel.setOrientation(LinearLayout.VERTICAL);
        mContentContainer.addView(panel);
        if (mItemStrList != null && mItemStrArray != null) {
            return;
        }
        if (mItemStrList != null) {
            mItemStrArray = (String[]) mItemStrList.toArray();
        }
        if (mItemStrArray != null) {
            for (int i = 0; i < mItemStrArray.length; i++) {
                AppCompatTextView itemTv = new AppCompatTextView(mContext);
                itemTv.setId(BOTTOM_BUTTON_ID + i + 1);
                itemTv.setGravity(Gravity.CENTER);
                itemTv.setText(mItemStrArray[i]);
                itemTv.setTextColor(mItemStrColor);
                itemTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp2px(mItemStrSize));
                itemTv.setBackgroundDrawable(getItemDrawable(i, mItemStrArray, dp2px(mCornerRadius), mItemBgColor));
                itemTv.setOnClickListener(this);
                LinearLayout.LayoutParams itemParams = createLayoutParams(dp2px(mItemHeight));
                if (i > 0) {
                    itemParams.topMargin = dp2px(1);
                    panel.addView(itemTv, itemParams);
                } else {
                    panel.addView(itemTv, itemParams);
                }
            }
        }
        mBottomBtn = new AppCompatTextView(mContext);
        mBottomBtn.getPaint().setFakeBoldText(true);
        mBottomBtn.setId(BOTTOM_BUTTON_ID);
        mBottomBtn.setGravity(Gravity.CENTER);
        mBottomBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp2px(mBottomBtnStrSize));
        mBottomBtn.setTextColor(mBottomBtnStrColor);
        mBottomBtn.setText(TextUtils.isEmpty(mBottomBtnStr) ? mContext.getString(R.string.cancel) : mBottomBtnStr);
        mBottomBtn.setBackgroundDrawable(getItemDrawable(0, new String[]{""}, dp2px(mCornerRadius), mBottomBtnBgColor));
        mBottomBtn.setOnClickListener(this);
        LinearLayout.LayoutParams bottomParams = createLayoutParams(dp2px(mBottomBtnHeight));
        bottomParams.topMargin = dp2px(mBottomBtnTopMargin);
        panel.addView(mBottomBtn, bottomParams);
        panel.setPadding(dp2px(mActionSheetPadding[0]), dp2px(mActionSheetPadding[1]),
                dp2px(mActionSheetPadding[2]), dp2px(mActionSheetPadding[3]));
    }

    private Drawable getItemDrawable(int postion, String[] titles, @Dimension int radius, int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(color);
        int length = titles.length;
        switch (length) {
            case 0:
                return gd;
            case 1:
                gd.setCornerRadius(radius);
                break;
            case 2:
                gd.setCornerRadii(postion == 0 ?
                        new float[]{radius, radius, radius, radius, 0, 0, 0, 0} :
                        new float[]{0, 0, 0, 0, radius, radius, radius, radius});
                break;
            default:
                gd.setCornerRadii(postion == 0 ?
                        new float[]{radius, radius, radius, radius, 0, 0, 0, 0} :
                        postion == length - 1 ?
                                new float[]{0, 0, 0, 0, radius, radius, radius, radius} :
                                new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                break;
        }
        return gd;
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, mContext.getResources().getDisplayMetrics());
    }

    public LinearLayout.LayoutParams createLayoutParams(int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                height != 0 ? height : FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        return params;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener == null) {
            return;
        }
        dismiss();
        mOnClickListener.onDismiss(this, v.getId() == BOTTOM_BUTTON_ID);
        if (v.getId() != BOTTOM_BUTTON_ID) {
            mOnClickListener.onItemClick(this, v.getId() - BOTTOM_BUTTON_ID - 1);
        }
    }

    public interface OnClickListener {
        // item的点击事件
        void onItemClick(ActionSheet actionSheet, int index);

        // 是否点击底部按钮触发的事件
        void onDismiss(ActionSheet actionSheet, boolean isClickBottom);
    }
}
