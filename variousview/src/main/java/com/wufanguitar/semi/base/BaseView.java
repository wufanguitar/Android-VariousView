package com.wufanguitar.semi.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.wufanguitar.semi.listener.OnCancelListerner;
import com.wufanguitar.semi.listener.OnDismissListener;
import com.wufanguitar.semi.utils.SemiAnimateUtil;
import com.wufanguitar.variousview.R;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

public class BaseView {
    protected final int DEFAULT_LEFT_RIGHT_BUTTON_NORMAL_COLOR = 0xFF057dff;
    protected final int DEFAULT_LEFT_RIGHT_BUTTON_PRESS_COLOR = 0xFFc2daf5;
    protected final int DEFAULT_TOPBAR_BACKGROUND_COLOR = 0xFFf5f5f5;
    protected final int DEFAULT_TOPBAR_TITLE_STRING_COLOR = 0xFF000000;
    protected final int DEFAULT_WHEEL_VIEW_BACKGROUND_COLOR = 0xFFFFFFFF;

    private Context mContext;

    // 回调监听
    private OnDismissListener mOnDismissListener;
    private OnCancelListerner mOnCancelListerner;

    // 自定义布局 view 的父 View
    protected ViewGroup mContentContainer;
    // 自定义布局 view 的根 View
    private ViewGroup mRootView;
    // 装载 mRootView 的 Dialog
    private AppCompatDialog mDialog;
    // 是通过哪个 View 启动
    protected View mClickView;

    // false : 底部显示
    // true : 居中显示（类似原生 Dialog 样式）
    protected boolean isDialogStyle;
    // 是否支持点击自定义布局 view 之外区域 dismiss
    protected boolean isOutsideDismiss;
    // 是否支持点击返回键 dismiss
    protected boolean isKeybackDismiss;
    private boolean isDismissing;

    // 动画相关
    private Animation mOutAnim;
    private Animation mInAnim;
    // 是否需要进入动画，默认为true
    private boolean mIsInAnim = true;
    // 是否需要退出动画，默认为true
    private boolean mIsOutAnim = true;

    public BaseView(Context context) {
        this.mContext = context;
    }

    protected void initViews() {
        mRootView = new FrameLayout(mContext);
        final FrameLayout.LayoutParams rootParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        rootParams.gravity = Gravity.BOTTOM;
        mContentContainer = new FrameLayout(mContext);
        mRootView.addView(mContentContainer, rootParams);
        createView();
        initListener();
//        setKeyBackListener();
    }

    protected void init() {
        mInAnim = getInAnimation();
        mOutAnim = getOutAnimation();
    }

    private void createView() {
        if (mRootView != null) {
            if (mDialog == null) {
                mDialog = new AppCompatDialog(mContext, R.style.semi_custom_dialog);
            }
            mDialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return false;
                }
            });

            prepareWindow(mDialog.getWindow());
            mDialog.setContentView(mRootView);
        }
    }

    private void prepareWindow(final Window window) {
        if (window == null) {
            throw new IllegalStateException("no visual activity");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#60000000"));
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#60000000")));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void initListener() {
        mDialog.setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (mOnDismissListener != null) {
                            // TODO: 2018/6/25 0025 替换或新增参数表示以什么方式
                            mOnDismissListener.onDismiss(BaseView.this);
                        }
                    }
                });
        mDialog.setOnKeyListener(
                new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (isKeybackDismiss && keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == MotionEvent.ACTION_DOWN) {
                            dismiss();
                            // TODO: 2018/6/25 0025 将cancel换成dismiss
                            cancel(KeyEvent.KEYCODE_BACK);
                        }
                        return true;
                    }
                });
        mRootView.setOnTouchListener(
                isOutsideDismiss ?
                        new View.OnTouchListener() {
                            @Override
                            @SuppressLint("ClickableViewAccessibility")
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    int[] location = new int[2];
                                    if (mContentContainer != null) {
                                        mContentContainer.getLocationOnScreen(location);
                                    }
                                    float pointY = event.getRawY();
                                    if (location[1] > 0 && pointY < location[1]) {
                                        dismiss();
                                    }
                                }
                                return false;
                            }
                        } :
                        null);
    }

    protected View inflateCustomView(int layoutRes) {
        if (mContext != null && mContentContainer != null) {
            mContentContainer.removeAllViews();
            return LayoutInflater.from(mContext).inflate(layoutRes, mContentContainer);
        }
        return null;
    }

    public void show(View view, boolean isInAnim) {
        this.mClickView = view;
        this.mIsInAnim = isInAnim;
        show();
    }

    public void show(boolean isInAnim) {
        this.mIsInAnim = isInAnim;
        show();
    }

    public void show(View view) {
        this.mClickView = view;
        show();
    }

    public void show() {
        if (isShowing()) {
            return;
        }
        showDialog();
        if (!isDialogStyle && mIsInAnim) {
            mContentContainer.startAnimation(mInAnim);
        }
    }

    private void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    private boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public void dismiss() {
        if (isDismissing) {
            return;
        }
        isDismissing = true;
        if (mIsOutAnim) {
            mOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dismissImmediately();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mContentContainer.startAnimation(mOutAnim);
        } else {
            dismissImmediately();
        }
    }

    public void dismissImmediately() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        isDismissing = false;
    }

    public void cancel(Object o) {
        if (mOnCancelListerner != null) {
            mOnCancelListerner.onCancel(o);
        }
    }

    private Animation getInAnimation() {
        int res = SemiAnimateUtil.getAnimationResource(Gravity.BOTTOM, true);
        return AnimationUtils.loadAnimation(mContext, res);
    }

    private Animation getOutAnimation() {
        int res = SemiAnimateUtil.getAnimationResource(Gravity.BOTTOM, false);
        return AnimationUtils.loadAnimation(mContext, res);
    }

    public BaseView setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    public BaseView setOnCancelListerner(OnCancelListerner cancelListerner) {
        this.mOnCancelListerner = cancelListerner;
        return this;
    }

    public View findViewById(@IdRes int id) {
        return mContentContainer.findViewById(id);
    }

}
