package com.wufanguitar.floating;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wufanguitar.variousview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wufanguitar.floating.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;

public final class Snackbar {

    public static abstract class Callback {

        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_TIMEOUT = 2;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;

        @IntDef({DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT,
                DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE})
        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {
        }

        public void onDismissed(Snackbar Snackbar, @DismissEvent int event) {
        }

        public void onShown(Snackbar Snackbar) {
        }
    }

    @IntDef({APPEAR_FROM_TOP_TO_DOWN, APPEAR_FROM_BOTTOM_TO_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OverSnackAppearDirection {
    }

    public static final int APPEAR_FROM_TOP_TO_DOWN = 0;

    public static final int APPEAR_FROM_BOTTOM_TO_TOP = 1;

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_LONG = 0;

    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;

    private static final Handler sHandler;
    private static final int MSG_SHOW = 0;
    private static final int MSG_DISMISS = 1;

    private
    @OverSnackAppearDirection
    int appearDirection = APPEAR_FROM_TOP_TO_DOWN;

    static {
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((Snackbar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((Snackbar) message.obj).hideView(message.arg1);
                        return true;
                }
                return false;
            }
        });
    }

    private final ViewGroup mParent;
    private final Context mContext;
    private final SnackbarLayout mView;
    private int mDuration;
    private Callback mCallback;

    private final AccessibilityManager mAccessibilityManager;

    private Snackbar(ViewGroup parent) {
        appearDirection = APPEAR_FROM_TOP_TO_DOWN;
        mParent = parent;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (appearDirection == APPEAR_FROM_BOTTOM_TO_TOP) {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_bsnackbar_layout, mParent, false);
        } else {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_tsnackbar_layout, mParent, false);
        }
        mAccessibilityManager = (AccessibilityManager)
                mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    private Snackbar(ViewGroup parent, @OverSnackAppearDirection int appearDirection) {
        this.appearDirection = appearDirection;
        mParent = parent;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (appearDirection == APPEAR_FROM_BOTTOM_TO_TOP) {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_bsnackbar_layout, mParent, false);
        } else {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_tsnackbar_layout, mParent, false);
        }
        mAccessibilityManager = (AccessibilityManager)
                mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            setMinHeight(0, 0);
        }
    }

    public Snackbar setMinHeight(int stateBarHeight, int actionBarHeight) {
        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (stateBarHeight > 0 || actionBarHeight > 0) {
                    mView.setPadding(0, stateBarHeight, 0, 0);
                    mView.setMinimumHeight(stateBarHeight + actionBarHeight);
                } else {
                    mView.setPadding(0, ScreenUtil.getStatusHeight(mContext), 0, 0);
                    mView.setMinimumHeight(ScreenUtil.getActionBarHeight(mContext) + ScreenUtil.getStatusHeight(mContext));
                }
            } else {
                if (stateBarHeight > 0 || actionBarHeight > 0) {
                    mView.setMinimumHeight(actionBarHeight);
                    ScreenUtil.setMargins(mView, 0, stateBarHeight, 0, 0);
                } else {
                    mView.setMinimumHeight(ScreenUtil.getActionBarHeight(mContext));
                    ScreenUtil.setMargins(mView, 0, ScreenUtil.getStatusHeight(mContext), 0, 0);
                }
            }
        }
        return this;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @NonNull CharSequence text, @Duration int duration) {
        Snackbar snackbar = new Snackbar(findSuitableParent(view), APPEAR_FROM_TOP_TO_DOWN);
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @NonNull CharSequence text, @Duration int duration, @OverSnackAppearDirection int appearDirection) {
        Snackbar snackbar = new Snackbar(findSuitableParent(view), appearDirection);
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                } else {
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }

    public Snackbar addIcon(int resource_id) {
        final TextView tv = mView.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(resource_id), null, null, null);
        return this;
    }

    public Snackbar addIcon(int resource_id, int width, int height) {
        final TextView tv = mView.getMessageView();
        if (width > 0 || height > 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(Bitmap.createScaledBitmap(((BitmapDrawable) (mContext.getResources().getDrawable(resource_id))).getBitmap(), width, height, true)), null, null, null);
        } else {
            addIcon(resource_id);
        }
        return this;
    }

    public Snackbar addIconProgressLoading(int resource_id, boolean left, boolean right) {
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.rotate);
        if (resource_id > 0) {
            drawable = mContext.getResources().getDrawable(resource_id);
        }
        addIconProgressLoading(drawable, left, right);
        return this;
    }

    public Snackbar addIconProgressLoading(Drawable drawable, boolean left, boolean right) {
        final ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "level", 0, 10000);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        mView.setBackgroundColor(mContext.getResources().getColor(Prompt.SUCCESS.getBackgroundColor()));
        if (left) {
            mView.getMessageView().setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        if (right) {
            mView.getMessageView().setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCallback != null) {
                    mCallback.onShown(Snackbar.this);
                }
                SnackbarManager.getInstance().onShown(mManagerCallback);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        return this;
    }

    public Snackbar setPromptThemBackground(Prompt prompt) {
        if (prompt == Prompt.SUCCESS) {
            setBackgroundColor(mContext.getResources().getColor(Prompt.SUCCESS.getBackgroundColor()));
            addIcon(Prompt.SUCCESS.getResIcon(), 0, 0);
        } else if (prompt == Prompt.ERROR) {
            setBackgroundColor(mContext.getResources().getColor(Prompt.ERROR.getBackgroundColor()));
            addIcon(Prompt.ERROR.getResIcon(), 0, 0);
        } else if (prompt == Prompt.WARNING) {
            setBackgroundColor(mContext.getResources().getColor(Prompt.WARNING.getBackgroundColor()));
            addIcon(Prompt.WARNING.getResIcon(), 0, 0);
        }
        return this;
    }

    public Snackbar setBackgroundColor(int colorId) {
        mView.setBackgroundColor(colorId);
        return this;
    }

    @NonNull
    public Snackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(mContext.getText(resId), listener);
    }

    @NonNull
    public Snackbar setAction(CharSequence text, final View.OnClickListener listener) {
        final TextView tv = mView.getActionView();
        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);

                    dispatchDismiss(Callback.DISMISS_EVENT_ACTION);
                }
            });
        }
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(ColorStateList colors) {
        final Button btn = mView.getActionView();
        btn.setTextColor(colors);
        return this;
    }

    @NonNull
    public Snackbar setActionTextSize(int size) {
        final Button btn = mView.getActionView();
        btn.setTextSize(size);
        return this;
    }

    @NonNull
    public Snackbar setMessageTextSize(int size) {
        final TextView tv = mView.getMessageView();
        tv.setTextSize(size);
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(@ColorInt int color) {
        final Button btn = mView.getActionView();
        btn.setTextColor(color);
        return this;
    }

    @NonNull
    public Snackbar setTextColor(@ColorInt int color) {
        setActionTextColor(color);
        setMessageTextColor(color);
        return this;
    }

    @NonNull
    public Snackbar setColor(ColorStateList colors) {
        setActionTextColor(colors);
        setMessageTextColor(colors);
        return this;
    }

    @NonNull
    public Snackbar setMessageTextColor(@ColorInt int color) {
        final TextView tv = mView.getMessageView();
        tv.setTextColor(color);
        return this;
    }

    @NonNull
    public Snackbar setMessageTextColor(ColorStateList colors) {
        final TextView tv = mView.getMessageView();
        tv.setTextColor(colors);
        return this;
    }

    @NonNull
    public Snackbar setText(@NonNull CharSequence message) {
        final TextView tv = mView.getMessageView();
        tv.setText(message);
        return this;
    }

    @NonNull
    public Snackbar setText(@StringRes int resId) {
        return setText(mContext.getText(resId));
    }

    @NonNull
    public Snackbar setDuration(@Duration int duration) {
        mDuration = duration;
        return this;
    }

    @Duration
    public int getDuration() {
        return mDuration;
    }

    @NonNull
    public View getView() {
        return mView;
    }

    public void show() {
        SnackbarManager.getInstance().show(mDuration, mManagerCallback);
    }

    public void dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL);
    }

    private void dispatchDismiss(@Callback.DismissEvent int event) {
        SnackbarManager.getInstance().dismiss(mManagerCallback, event);
    }

    @NonNull
    public Snackbar setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(mManagerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(mManagerCallback);
    }

    private final SnackbarManager.Callback mManagerCallback = new SnackbarManager.Callback() {
        @Override
        public void show() {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_SHOW, Snackbar.this));
        }

        @Override
        public void dismiss(int event) {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_DISMISS, event, 0, Snackbar.this));
        }
    };

    final void showView() {
        if (mView.getParent() == null) {
            final ViewGroup.LayoutParams lp = mView.getLayoutParams();
            if (lp instanceof CoordinatorLayout.LayoutParams) {
                // If our LayoutParams are from a CoordinatorLayout, we'll setup our Behavior

                final Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(View view) {
                        view.setVisibility(View.GONE);
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE);
                    }

                    @Override
                    public void onDragStateChanged(int state) {
                        switch (state) {
                            case SwipeDismissBehavior.STATE_DRAGGING:
                            case SwipeDismissBehavior.STATE_SETTLING:

                                SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                                break;
                            case SwipeDismissBehavior.STATE_IDLE:

                                SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                                break;
                        }
                    }
                });
                ((CoordinatorLayout.LayoutParams) lp).setBehavior(behavior);

                ((CoordinatorLayout.LayoutParams) lp).setMargins(0, -30, 0, 0);
            }
            mParent.addView(mView);
        }
        if (ViewCompat.isLaidOut(mView)) {
            animateViewIn();
        } else {

            mView.setOnLayoutChangeListener(new SnackbarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    animateViewIn();
                    mView.setOnLayoutChangeListener(null);
                }
            });
        }

        mView.setOnAttachStateChangeListener(new SnackbarLayout.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isShownOrQueued()) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onViewHidden(Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });

        if (ViewCompat.isLaidOut(mView)) {
            if (shouldAnimate()) {
                animateViewIn();
            } else {
                onViewShown();
            }
        } else {
            mView.setOnLayoutChangeListener(new SnackbarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    mView.setOnLayoutChangeListener(null);

                    if (shouldAnimate()) {
                        animateViewIn();
                    } else {
                        onViewShown();
                    }
                }
            });
        }
    }

    private void animateViewIn() {
        Animation anim;
        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            anim = getAnimationInFromTopToDown();
        } else {
            anim = getAnimationInFromBottomToTop();
        }
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onViewShown();
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mView.startAnimation(anim);
    }

    private void animateViewOut(final int event) {
        Animation anim;

        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            anim = getAnimationOutFromTopToDown();
        } else {
            anim = getAnimationOutFromBottomToTop();
        }
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onViewHidden(event);
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mView.startAnimation(anim);
    }


    private Animation getAnimationInFromTopToDown() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.top_in);
    }

    private Animation getAnimationInFromBottomToTop() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_snackbar_in);
    }

    private Animation getAnimationOutFromTopToDown() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.top_out);
    }

    private Animation getAnimationOutFromBottomToTop() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_snackbar_out);
    }

    final void hideView(@Callback.DismissEvent final int event) {
        if (shouldAnimate() && mView.getVisibility() == View.VISIBLE) {
            animateViewOut(event);
        } else {
            onViewHidden(event);
        }
    }

    private void onViewShown() {
        SnackbarManager.getInstance().onShown(mManagerCallback);
        if (mCallback != null) {
            mCallback.onShown(this);
        }
    }

    private void onViewHidden(int event) {
        SnackbarManager.getInstance().onDismissed(mManagerCallback);
        if (mCallback != null) {
            mCallback.onDismissed(this, event);
        }
        final ViewParent parent = mView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mView);
        }
    }

    private boolean shouldAnimate() {
        return !mAccessibilityManager.isEnabled();
    }

    private boolean isBeingDragged() {
        final ViewGroup.LayoutParams lp = mView.getLayoutParams();
        if (lp instanceof CoordinatorLayout.LayoutParams) {
            final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) lp;
            final CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof SwipeDismissBehavior) {
                return ((SwipeDismissBehavior) behavior).getDragState() != SwipeDismissBehavior.STATE_IDLE;
            }
        }
        return false;
    }

    public static class SnackbarLayout extends LinearLayout {
        private TextView mMessageView;
        private Button mActionView;

        private int mMaxWidth;
        private int mMaxInlineActionWidth;

        interface OnLayoutChangeListener {
            void onLayoutChange(View view, int left, int top, int right, int bottom);
        }

        interface OnAttachStateChangeListener {
            void onViewAttachedToWindow(View v);

            void onViewDetachedFromWindow(View v);
        }

        private OnLayoutChangeListener mOnLayoutChangeListener;
        private OnAttachStateChangeListener mOnAttachStateChangeListener;

        public SnackbarLayout(Context context) {
            this(context, null);
        }

        public SnackbarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
            mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            mMaxInlineActionWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(this, a.getDimensionPixelSize(
                        R.styleable.SnackbarLayout_elevation, 0));
            }
            a.recycle();
            setClickable(true);

            LayoutInflater.from(context).inflate(R.layout.view_tsnackbar_layout_include, this);
            ViewCompat.setAccessibilityLiveRegion(this,
                    ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
            ViewCompat.setImportantForAccessibility(this,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            mMessageView = (TextView) findViewById(R.id.snackbar_text);
            mActionView = (Button) findViewById(R.id.snackbar_action);
        }

        TextView getMessageView() {
            return mMessageView;
        }

        Button getActionView() {
            return mActionView;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (mMaxWidth > 0 && getMeasuredWidth() > mMaxWidth) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            final int multiLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical_2lines);
            final int singleLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical);
            final boolean isMultiLine = mMessageView.getLayout().getLineCount() > 1;
            boolean remeasure = false;
            if (isMultiLine && mMaxInlineActionWidth > 0
                    && mActionView.getMeasuredWidth() > mMaxInlineActionWidth) {
                if (updateViewsWithinLayout(VERTICAL, multiLineVPadding,
                        multiLineVPadding - singleLineVPadding)) {
                    remeasure = true;
                }
            } else {
                final int messagePadding = isMultiLine ? multiLineVPadding : singleLineVPadding;
                if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                    remeasure = true;
                }
            }
            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        void animateChildrenIn(int delay, int duration) {
            ViewCompat.setAlpha(mMessageView, 0f);
            ViewCompat.animate(mMessageView).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
            if (mActionView.getVisibility() == VISIBLE) {
                ViewCompat.setAlpha(mActionView, 0f);
                ViewCompat.animate(mActionView).alpha(1f).setDuration(duration)
                        .setStartDelay(delay).start();
            }
        }

        void animateChildrenOut(int delay, int duration) {
            ViewCompat.setAlpha(mMessageView, 1f);
            ViewCompat.animate(mMessageView).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
            if (mActionView.getVisibility() == VISIBLE) {
                ViewCompat.setAlpha(mActionView, 1f);
                ViewCompat.animate(mActionView).alpha(0f).setDuration(duration)
                        .setStartDelay(delay).start();
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (changed && mOnLayoutChangeListener != null) {
                mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewAttachedToWindow(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            mOnLayoutChangeListener = onLayoutChangeListener;
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
            mOnAttachStateChangeListener = listener;
        }

        private boolean updateViewsWithinLayout(final int orientation,
                                                final int messagePadTop, final int messagePadBottom) {
            boolean changed = false;
            if (orientation != getOrientation()) {
                setOrientation(orientation);
                changed = true;
            }
            if (mMessageView.getPaddingTop() != messagePadTop
                    || mMessageView.getPaddingBottom() != messagePadBottom) {
                updateTopBottomPadding(mMessageView, messagePadTop, messagePadBottom);
                changed = true;
            }
            return changed;
        }

        private static void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
            if (ViewCompat.isPaddingRelative(view)) {
                ViewCompat.setPaddingRelative(view,
                        ViewCompat.getPaddingStart(view), topPadding,
                        ViewCompat.getPaddingEnd(view), bottomPadding);
            } else {
                view.setPadding(view.getPaddingLeft(), topPadding,
                        view.getPaddingRight(), bottomPadding);
            }
        }
    }

    final class Behavior extends SwipeDismissBehavior<SnackbarLayout> {
        @Override
        public boolean canSwipeDismissView(View child) {
            return child instanceof SnackbarLayout;
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, SnackbarLayout child,
                                             MotionEvent event) {
            // We want to make sure that we disable any Snackbar timeouts if the user is
            // currently touching the Snackbar. We restore the timeout when complete
            if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                        break;
                }
            }

            return super.onInterceptTouchEvent(parent, child, event);
        }
    }
}