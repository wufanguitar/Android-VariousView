package com.wufanguitar.semi.bar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wufanguitar.semi.callback.ICustomLayout;
import com.wufanguitar.semi.listener.OnSwipeDismissTouchListener;
import com.wufanguitar.semi.listener.OnUpDismissTouchListener;
import com.wufanguitar.variousview.R;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * @Author: 吴凡
 * @Email: wufan01@sunlands.com
 * @Time: 2018/4/13  11:21
 * @Description: 基于Android-support-design-SnackBar源码上进行修改
 */

public final class SnackBar extends BaseTransientBar<SnackBar> {

    public static final int LENGTH_SHORT = BaseTransientBar.LENGTH_SHORT;
    public static final int LENGTH_LONG = BaseTransientBar.LENGTH_LONG;
    public static final int LENGTH_INDEFINITE = BaseTransientBar.LENGTH_INDEFINITE;

    public static class Callback extends BaseCallback<SnackBar> {
        /**
         * Indicates that the Snackbar was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = BaseCallback.DISMISS_EVENT_SWIPE;
        /**
         * Indicates that the Snackbar was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = BaseCallback.DISMISS_EVENT_ACTION;
        /**
         * Indicates that the Snackbar was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = BaseCallback.DISMISS_EVENT_TIMEOUT;
        /**
         * Indicates that the Snackbar was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = BaseCallback.DISMISS_EVENT_MANUAL;
        /**
         * Indicates that the Snackbar was dismissed from a new Snackbar being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = BaseCallback.DISMISS_EVENT_CONSECUTIVE;

        @Override
        public void onShown(SnackBar transientBottomBar) {
        }

        @Override
        public void onDismissed(SnackBar transientBottomBar, @DismissEvent int event) {
        }
    }

    private SnackBar(ViewGroup parent, View content, ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    @NonNull
    public static SnackBar make(@NonNull View view, @NonNull CharSequence text,
                                @Duration int duration) {
        final ViewGroup parent = findSuitableParent(view);
        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. "
                    + "Please provide a valid view.");
        }
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SnackBarContentLayout content =
                (SnackBarContentLayout) inflater.inflate(
                        R.layout.snackbar_layout_include, parent, false);
        final SnackBar snackBar = new SnackBar(parent, content, content);
        snackBar.setText(text);
        snackBar.setDuration(duration);
        return snackBar;
    }

    @NonNull
    public static SnackBar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }

    @NonNull
    public SnackBar setText(@NonNull CharSequence message) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setText(message);
        return this;
    }

    @NonNull
    public SnackBar setText(@StringRes int resId) {
        return setText(getContext().getText(resId));
    }

    @NonNull
    public SnackBar setBackgroundColor(int colorId) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        contentLayout.setBackgroundColor(colorId);
        return this;
    }

    @NonNull
    public SnackBar setBackgroundResource(@DrawableRes int resid) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        contentLayout.setBackgroundResource(resid);
        return this;
    }

    @NonNull
    public SnackBar addIcon(@DrawableRes int resource) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(mView.getResources().getDrawable(resource), null, null, null);
        return this;
    }

    @NonNull
    public SnackBar addIcon(@DrawableRes int resource, int width, int height) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        if (width > 0 || height > 0) {
            Bitmap bitmap = getBitmapFromDrawable(mView.getContext(), resource);
            if (bitmap == null) {
                return this;
            }
            tv.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(mView.getResources(),
                    Bitmap.createScaledBitmap(bitmap, width, height, true)), null, null, null);
            bitmap.recycle();
        } else {
            addIcon(resource);
        }
        return this;
    }

    @NonNull
    public SnackBar addIconWithAnimation(@DrawableRes int resource, @AnimRes int animation) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        ImageView imageView = new ImageView(mView.getContext());
        imageView.setBackgroundResource(resource);
        contentLayout.addView(imageView, 0);
        contentLayout.setPadding(tv.getCompoundDrawablePadding(), contentLayout.getPaddingTop(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        imageView.startAnimation(AnimationUtils.loadAnimation(mView.getContext(), animation));
        return this;
    }

    public Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) throws IllegalArgumentException {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            } else {
                throw new IllegalArgumentException("unsupported drawable type");
            }
        }
        return null;
    }

    public SnackBar setOnClickAction(final View.OnClickListener clickAction) {
        return setOnClickAction(clickAction, true);
    }

    public SnackBar setOnClickAction(final View.OnClickListener clickAction, final boolean dismiss) {
        final SnackBarContentLayout contentLayout = (SnackBarContentLayout) mView.getChildAt(0);
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction.onClick(v);
                if (dismiss) {
                    dispatchDismiss(Callback.DISMISS_EVENT_ACTION);
                }
            }
        });
        return this;
    }

    @NonNull
    public SnackBar customLayout(@LayoutRes int customLayoutRes, @Nullable ICustomLayout customLayout) {
        mView.removeAllViews();
        View customView = LayoutInflater.from(mView.getContext()).inflate(customLayoutRes, null);
        mView.addView(customView);
        if (customLayout != null) {
            customLayout.customLayout(customView);
        }
        return this;
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    public SnackBar enableSwipeToDismiss() {
        mView.setOnTouchListener(new OnSwipeDismissTouchListener(mView, null,
                new OnSwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        mTargetParent.removeView(mView);
                    }

                    @Override
                    public void onTouch(View view, boolean touch) {

                    }
                }));
        return this;
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    public SnackBar enableUpToDismiss() {
        mView.setOnTouchListener(new OnUpDismissTouchListener(mView, null,
                new OnUpDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        mTargetParent.removeView(mView);
                    }

                    @Override
                    public void onTouch(View view, boolean touch) {

                    }
                }));
        return this;
    }

    @RestrictTo(LIBRARY_GROUP)
    public static final class SnackBarLayout extends BaseTransientBar.SnackbarBaseLayout {
        public SnackBarLayout(Context context) {
            super(context);
        }

        public SnackBarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Work around our backwards-compatible refactoring of Snackbar and inner content
            // being inflated against snackbar's parent (instead of against the snackbar itself).
            // Every child that is width=MATCH_PARENT is remeasured again and given the full width
            // minus the paddings.
            int childCount = getChildCount();
            int availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(),
                                    MeasureSpec.EXACTLY));
                }
            }
        }
    }
}

