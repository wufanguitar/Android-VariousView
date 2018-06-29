package com.wufanguitar.semi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Dimension;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wufanguitar.semi.base.BaseView;
import com.wufanguitar.variousview.R;

import java.util.Arrays;
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

    // 选中的item
    private String mSelectedItemStr;
    // 选中的item的颜色
    private int mSelectedItemStrColor;
    // 选中的item是否加粗
    private boolean mSelectedItemBold;

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
        this.mSelectedItemStr = builder.mSelectedItemStr;
        this.mSelectedItemStrColor = builder.mSelectedItemStrColor;
        this.mSelectedItemBold = builder.mSelectedItemBold;
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

        // 选择的item
        private String mSelectedItemStr = "";
        private int mSelectedItemStrColor = COLOR_BLUE;
        private boolean mSelectedItemBold = true;

        // 底部按钮
        private String mBottomBtnStr;
        private float mBottomBtnHeight = 40f;
        private float mBottomBtnTopMargin = 5f;
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

        public Builder setSelectedItemStr(String selectedItemStr) {
            this.mSelectedItemStr = selectedItemStr;
            return this;
        }

        public Builder setSelectedItemStrColor(int selectedItemStrColor) {
            this.mSelectedItemStrColor = selectedItemStrColor;
            return this;
        }

        public Builder setSelectedItemBold(boolean selectedItemBold) {
            this.mSelectedItemBold = selectedItemBold;
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
        initViews();
        init();
        createDefaultView();
    }

    private void createDefaultView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mContentContainer.removeAllViews();
        mContentContainer.setLayoutParams(layoutParams);
        LinearLayout panel = new LinearLayout(mContext);
        layoutParams.gravity = Gravity.BOTTOM;
        panel.setLayoutParams(layoutParams);
        panel.setOrientation(LinearLayout.VERTICAL);
//        ListView listView = new ListView(mContext);
//        panel.addView(listView);
        mContentContainer.addView(panel);
        if (mItemStrList != null && mItemStrArray != null) {
            return;
        }
        if (mItemStrArray != null) {
            mItemStrList = Arrays.asList(mItemStrArray);
        }
        int selectedIndex = -1;
        if (!TextUtils.isEmpty(mSelectedItemStr)) {
            selectedIndex = mItemStrList.indexOf(mSelectedItemStr);
        }
//        setAdapter(listView, selectedIndex);
//        setListener(listView);
        int size = mItemStrList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                AppCompatTextView itemTv = new AppCompatTextView(mContext);
                itemTv.setId(BOTTOM_BUTTON_ID + i + 1);
                itemTv.setGravity(Gravity.CENTER);
                itemTv.setText(mItemStrList.get(i));
                itemTv.setTextSize(mItemStrSize);
                itemTv.setTextColor(selectedIndex == i ? mSelectedItemStrColor : mItemStrColor);
                setSelectedItemStrBold(itemTv, selectedIndex == i);
                itemTv.setBackgroundDrawable(getItemDrawable(i, mItemStrList, dp2px(mCornerRadius), mItemBgColor));
                itemTv.setOnClickListener(this);
                LinearLayout.LayoutParams itemParams = createLayoutParams(dp2px(mItemHeight));
                if (i > 0) {
                    itemParams.topMargin = dp2px(0.5f);
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
        mBottomBtn.setTextSize(mBottomBtnStrSize);
        mBottomBtn.setTextColor(mBottomBtnStrColor);
        mBottomBtn.setText(TextUtils.isEmpty(mBottomBtnStr) ? mContext.getString(R.string.cancel) : mBottomBtnStr);
        mBottomBtn.setBackgroundDrawable(getItemDrawable(0, mItemStrList, dp2px(mCornerRadius), mBottomBtnBgColor));
        mBottomBtn.setOnClickListener(this);
        LinearLayout.LayoutParams bottomParams = createLayoutParams(dp2px(mBottomBtnHeight));
        bottomParams.topMargin = dp2px(mBottomBtnTopMargin);
        panel.addView(mBottomBtn, bottomParams);
        panel.setPadding(dp2px(mActionSheetPadding[0]), dp2px(mActionSheetPadding[1]),
                dp2px(mActionSheetPadding[2]), dp2px(mActionSheetPadding[3]));
    }

//    private void setAdapter(ListView listView, final int selectedIndex) {
//        listView.setAdapter(new BaseAdapter() {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                AppCompatTextView itemTv;
//                if (convertView == null) {
//                    convertView = new AppCompatTextView(mContext);
//                }
//                itemTv = (AppCompatTextView) convertView;
//                itemTv.setId(BOTTOM_BUTTON_ID + position + 1);
//                itemTv.setGravity(Gravity.CENTER);
//                itemTv.setText(mItemStrList.get(position));
//                itemTv.setTextSize(mItemStrSize);
//                itemTv.setTextColor(selectedIndex == position ? mSelectedItemStrColor : mItemStrColor);
//                setSelectedItemStrBold(itemTv, selectedIndex == position);
//                itemTv.setBackgroundDrawable(getItemDrawable(position, mItemStrList, dp2px(mCornerRadius), mItemBgColor));
//                itemTv.setSingleLine(true);
//                return convertView;
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return mItemStrList.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mItemStrList.size();
//            }
//        });
//    }

//    private void setListener(ListView listView) {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                changeItemStyle(view);
//                dismiss();
//                if (mOnClickListener != null) {
//                    mOnClickListener.onItemClick(ActionSheet.this, view.getId() - BOTTOM_BUTTON_ID - 1, view.getId() == BOTTOM_BUTTON_ID);
//                }
//            }
//        });
//    }

    private Drawable getItemDrawable(int postion, List<String> titles, @Dimension int radius, int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(color);
        int size = titles.size();
        switch (size) {
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
                        postion == size - 1 ?
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
        changeItemStyle(v);
        dismiss();
        if (mOnClickListener != null) {
            mOnClickListener.onItemClick(this, v.getId() - BOTTOM_BUTTON_ID - 1, v.getId() == BOTTOM_BUTTON_ID);
        }
    }

    private void changeItemStyle(View view) {
        if (view.getId() == BOTTOM_BUTTON_ID) {
            return;
        }
        ViewGroup parent = (ViewGroup) mContentContainer.getChildAt(0);
        int count = parent.getChildCount();
        int clickIndex = parent.indexOfChild(view);
        for (int i = 0; i < count - 1; i++) {
            AppCompatTextView item = (AppCompatTextView) parent.getChildAt(i);
            item.setTextColor(clickIndex == i ? mSelectedItemStrColor : mItemStrColor);
            setSelectedItemStrBold(item, clickIndex == i);
        }
    }

    private void setSelectedItemStrBold(AppCompatTextView textView, boolean isBold) {
        if (mSelectedItemBold) {
            TextPaint textPaint = textView.getPaint();
            textPaint.setFakeBoldText(isBold);
        }
    }

    public interface OnClickListener {
        /**
         * @param actionSheet
         * @param index:         item的位置
         * @param isClickBottom: 是否点击底部按钮触发的事件
         */
        void onItemClick(ActionSheet actionSheet, int index, boolean isClickBottom);
    }
}
