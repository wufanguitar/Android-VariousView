package com.wufanguitar.semi.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;

import com.wufanguitar.annotate.Dismiss;
import com.wufanguitar.semi.listener.OnDismissListener;
import com.wufanguitar.semi.utils.ScreenUtil;
import com.wufanguitar.semi.utils.SemiAnimateUtil;
import com.wufanguitar.variousview.R;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

public class BaseView<V extends BaseView> {
    protected final int DEFAULT_LEFT_RIGHT_BUTTON_NORMAL_COLOR = 0xFF057dff;
    protected final int DEFAULT_LEFT_RIGHT_BUTTON_PRESS_COLOR = 0xFFc2daf5;
    protected final int DEFAULT_TOPBAR_BACKGROUND_COLOR = 0xFFf5f5f5;
    protected final int DEFAULT_TOPBAR_TITLE_STRING_COLOR = 0xFF000000;
    protected final int DEFAULT_WHEEL_VIEW_BACKGROUND_COLOR = 0xFFFFFFFF;

    private final int DEFAULT_TRANSLUCENCE_COLOR = 0x60000000;
    private Context mContext;

    // 回调监听
    private OnDismissListener mOnDismissListener;

    // 默认是 activity 的根view
    protected ViewGroup mDecorView;
    // 自定义布局 view 的父 View
    protected ViewGroup mContentContainer;
    // 自定义布局 view 的根 View
    private ViewGroup mRootView;
    // 装载 mRootView 的 Dialog
    private AppCompatDialog mDialog;
    // 是通过哪个 View 启动
    protected View mClickView;
    private View mStatusBarView;

    // false : 底部显示
    // true : 居中显示（类似原生 Dialog 样式）
    protected boolean isDialogStyle;
    // 是否支持点击自定义布局 view 之外区域 dismiss
    protected boolean isOutsideDismiss;
    // 是否支持点击返回键 dismiss
    protected boolean isKeybackDismiss;
    private boolean isDismissing;
    // 回调 Dismiss.EVENT_MANUAL_CANCEL 类型
    private boolean dismissByCancel = true;

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
        rootParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mContentContainer = new FrameLayout(mContext);
        mRootView.addView(mContentContainer, rootParams);
        createView();
        initListener();
    }

    protected void init() {
        mInAnim = getInAnimation();
        mOutAnim = getOutAnimation();
    }

    private void createView() {
        if (mDialog == null) {
            mDialog = new AppCompatDialog(mContext, R.style.semi_custom_dialog);
        }
        mStatusBarView = createStatusBarView();
        mDialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        prepareWindow(mDialog.getWindow());
        mDialog.setContentView(mRootView);
    }

    private View createStatusBarView() {
        View statusBarView = new View(mContext);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getStatusHeight(mContext));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(DEFAULT_TRANSLUCENCE_COLOR);
        return statusBarView;
    }

    private void prepareWindow(final Window window) {
        if (window == null) {
            throw new IllegalStateException("no visual activity");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(DEFAULT_TRANSLUCENCE_COLOR);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            if (mStatusBarView != null) {
                decorView.addView(mStatusBarView);
                for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
                    View childView = decorView.getChildAt(i);
                    if (childView instanceof ViewGroup) {
                        childView.setFitsSystemWindows(true);
                        ((ViewGroup) childView).setClipToPadding(true);
                    }
                }
            }
        }
        window.setBackgroundDrawable(new ColorDrawable(DEFAULT_TRANSLUCENCE_COLOR));
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void initListener() {
        mDialog.setOnKeyListener(
                new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (isKeybackDismiss && keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == MotionEvent.ACTION_DOWN) {
                            dismiss(Dismiss.EVENT_KEY_BACK);
                            dismissByCancel = false;
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
                                        dismiss(Dismiss.EVENT_OUT_SIDE);
                                        dismissByCancel = false;
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

    private void dismiss(@Dismiss int event) {
        if (isDismissing) {
            return;
        }
        isDismissing = true;
        callDismissListener(event);
        dismiss();
    }

    public void dismiss() {
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
            if (dismissByCancel) {
                callDismissListener(Dismiss.EVENT_CANCEL);
            }
        }
        isDismissing = false;
    }

    private void callDismissListener(@Dismiss int event) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(event);
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

    protected View findViewById(@IdRes int id) {
        return mContentContainer.findViewById(id);
    }

    public V setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return (V) this;
    }
}
