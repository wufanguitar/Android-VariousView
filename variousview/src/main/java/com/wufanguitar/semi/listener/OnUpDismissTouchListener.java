package com.wufanguitar.semi.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * @Author: 吴凡
 * @Email: wufan01@sunlands.com
 * @Time: 2018/4/9  10:27
 * @Description: 向上滑动消失
 */

@SuppressWarnings("PMD")
public class OnUpDismissTouchListener implements View.OnTouchListener {
    private final int mSlop;
    private final int mMinFlingVelocity;
    private final long mAnimationTime;

    private final View mView;
    private final DismissCallbacks mCallbacks;
    private int mViewHeight = 1;

    private float mDownX;
    private float mDownY;
    private boolean mSwiping;
    private int mSwipingSlop;
    private Object mToken;
    private VelocityTracker mVelocityTracker;
    private float mTranslationY;

    public OnUpDismissTouchListener(View view, Object token, DismissCallbacks callbacks) {
        ViewConfiguration vc = ViewConfiguration.get(view.getContext());
        mSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 16;
        mAnimationTime = view.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mView = view;
        mToken = token;
        mCallbacks = callbacks;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        motionEvent.offsetLocation(0, mTranslationY);

        if (mViewHeight < 2) {
            mViewHeight = mView.getHeight();
        }

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mDownX = motionEvent.getRawX();
                mDownY = motionEvent.getRawY();
                if (mCallbacks.canDismiss(mToken)) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(motionEvent);
                }
                mCallbacks.onTouch(view, true);
                return false;
            }

            case MotionEvent.ACTION_UP: {
                if (mVelocityTracker == null) {
                    break;
                }

                float deltaY = motionEvent.getRawY() - mDownY;
                mVelocityTracker.addMovement(motionEvent);
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                float velocityY = mVelocityTracker.getYVelocity();
                float absVelocityX = Math.abs(velocityX);
                float absVelocityY = Math.abs(velocityY);
                boolean dismiss = false;
                boolean dismissTop = false;
                if (Math.abs(deltaY) > mViewHeight / 2 && mSwiping) {
                    dismiss = true;
                    dismissTop = deltaY < 0;
                } else if (mMinFlingVelocity <= absVelocityY && absVelocityY > absVelocityX && mSwiping) {
                    dismiss = deltaY < 0;
                    dismissTop = (velocityY < 0) == (deltaY < 0);
                }
                if (dismiss) {
                    // dismiss
                    mView.animate()
                            .translationY(dismissTop ? -mViewHeight : 0)
                            .alpha(0)
                            .setDuration(mAnimationTime)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    performDismiss();
                                }
                            });
                } else if (mSwiping) {
                    // cancel
                    mView.animate()
                            .translationY(0)
                            .alpha(1)
                            .setDuration(mAnimationTime)
                            .setListener(null);
                    mCallbacks.onTouch(view, false);
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mTranslationY = 0;
                mDownX = 0;
                mDownY = 0;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (mVelocityTracker == null) {
                    break;
                }

                mView.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(mAnimationTime)
                        .setListener(null);
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mTranslationY = 0;
                mDownX = 0;
                mDownY = 0;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mVelocityTracker == null) {
                    break;
                }

                mVelocityTracker.addMovement(motionEvent);
                float deltaX = motionEvent.getRawX() - mDownX;
                float deltaY = motionEvent.getRawY() - mDownY;
                if (Math.abs(deltaY) > mSlop && Math.abs(deltaY) > Math.abs(deltaX) / 2) {
                    mSwiping = deltaY < 0;
                    mSwipingSlop = deltaY < 0 ? mSlop : 0;
                    mView.getParent().requestDisallowInterceptTouchEvent(true);

                    // 取消 Listview 的 touch 事件
                    MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL
                            | (motionEvent.getActionIndex()
                            << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    mView.onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                }

                if (mSwiping) {
                    mTranslationY = deltaY;
                    mView.setTranslationY(Math.min(0f, deltaY + mSwipingSlop));
                    mView.setAlpha(Math.max(0f, Math.min(1f,
                            1f + 2f * deltaY / mViewHeight)));
                    return true;
                }
                break;
            }

            default: {
                view.performClick();
                return false;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void performDismiss() {

        final ViewGroup.LayoutParams lp = mView.getLayoutParams();
        final int originalHeight = mView.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCallbacks.onDismiss(mView, mToken);
                // 重置到原始状态
                mView.setAlpha(1f);
                mView.setTranslationX(0);
                lp.height = originalHeight;
                mView.setLayoutParams(lp);
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                mView.setLayoutParams(lp);
            }
        });

        animator.start();
    }

    public interface DismissCallbacks {
        boolean canDismiss(Object token);

        void onDismiss(View view, Object token);

        void onTouch(View view, boolean touch);
    }
}
