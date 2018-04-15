package com.wufanguitar.semi.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wufanguitar.variousview.R;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * @Author: 吴凡
 * @Email: wufan01@sunlands.com
 * @Time: 2018/4/13  11:22
 * @Description: 基于Android-support-design-SnackBar源码上进行修改
 */

@RestrictTo(LIBRARY_GROUP)
public class SnackbarContentLayout extends LinearLayout implements
        BaseTransientBar.ContentViewCallback {
    private TextView mMessageView;
    private Button mActionView;

    private int mMaxWidth;
    private int mMaxInlineActionWidth;

    public SnackbarContentLayout(Context context) {
        this(context, null);
    }

    public SnackbarContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMessageView = (TextView) findViewById(R.id.snackbar_text);
        mActionView = (Button) findViewById(R.id.snackbar_action);
    }

    public TextView getMessageView() {
        return mMessageView;
    }

    public Button getActionView() {
        return mActionView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int lineVPadding = getResources().getDimensionPixelSize(
                R.dimen.dimen_14_dp);
        boolean remeasure = false;
        if (updateViewsWithinLayout(HORIZONTAL, lineVPadding, lineVPadding)) {
            remeasure = true;
        }
        if (remeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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

    @Override
    public void animateContentIn(int delay, int duration) {
        mMessageView.setAlpha(0f);
        mMessageView.animate().alpha(1f).setDuration(duration)
                .setStartDelay(delay).start();

        if (mActionView.getVisibility() == VISIBLE) {
            mActionView.setAlpha(0f);
            mActionView.animate().alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }
    }

    @Override
    public void animateContentOut(int delay, int duration) {
        mMessageView.setAlpha(1f);
        mMessageView.animate().alpha(0f).setDuration(duration)
                .setStartDelay(delay).start();

        if (mActionView.getVisibility() == VISIBLE) {
            mActionView.setAlpha(1f);
            mActionView.animate().alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }
    }
}
