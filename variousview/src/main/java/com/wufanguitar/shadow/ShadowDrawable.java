package com.wufanguitar.shadow;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * @Author: 吴凡
 * @Email: wu.fanguitar@163.com
 * @Time: 2018/4/22  10:21
 * @Description:
 */

public class ShadowDrawable extends Drawable {
    private PorterDuffXfermode mSrcOut = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    private Paint mPaint;
    private RectF mBounds = new RectF();
    private int mWidth;
    private int mHeight;
    private ShadowProperty mShadowProperty;
    private int mShadowOffset;
    private RectF mDrawRect;
    private float mCornerRadiusX;
    private float mCornerRadiusY;

    public ShadowDrawable(ShadowProperty shadowProperty) {
        this(shadowProperty, Color.TRANSPARENT, shadowProperty.getShadowRadius(), shadowProperty.getShadowRadius());
    }

    public ShadowDrawable(ShadowProperty shadowProperty, float cornerRadiusX, float cornerRadiusY) {
        this(shadowProperty, Color.TRANSPARENT, cornerRadiusX, cornerRadiusY);
    }

    public ShadowDrawable(ShadowProperty shadowProperty, int color, float cornerRadiusX, float cornerRadiusY) {
        this.mShadowProperty = shadowProperty;
        this.mShadowOffset = mShadowProperty.getShadowOffset();
        this.mCornerRadiusX = cornerRadiusX;
        this.mCornerRadiusY = cornerRadiusY;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // 解决旋转时的锯齿问题
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);

        // 设置阴影
        mPaint.setShadowLayer(shadowProperty.getShadowRadius(),
                shadowProperty.getShadowXOffset(),
                shadowProperty.getShadowYOffset(),
                shadowProperty.getShadowColor());

        mDrawRect = new RectF();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (bounds.right - bounds.left > 0 && bounds.bottom - bounds.top > 0) {
            mBounds.left = bounds.left;
            mBounds.right = bounds.right;
            mBounds.top = bounds.top;
            mBounds.bottom = bounds.bottom;
            mWidth = (int) (mBounds.right - mBounds.left);
            mHeight = (int) (mBounds.bottom - mBounds.top);
            int shadowSide = mShadowProperty.getShadowSide();
            int left = (shadowSide & ShadowProperty.LEFT) == ShadowProperty.LEFT ? mShadowOffset : 0;
            int top = (shadowSide & ShadowProperty.TOP) == ShadowProperty.TOP ? mShadowOffset : 0;
            int right = mWidth - ((shadowSide & ShadowProperty.RIGHT) == ShadowProperty.RIGHT ? mShadowOffset : 0);
            int bottom = mHeight - ((shadowSide & ShadowProperty.BOTTOM) == ShadowProperty.BOTTOM ? mShadowOffset : 0);
            mDrawRect = new RectF(left, top, right, bottom);
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setXfermode(null);
        canvas.drawRoundRect(
                mDrawRect,
                mCornerRadiusX, mCornerRadiusY,
                mPaint
        );
        mPaint.setXfermode(mSrcOut);
        canvas.drawRoundRect(mDrawRect, mCornerRadiusX, mCornerRadiusY, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    public ShadowDrawable setColor(int color) {
        mPaint.setColor(color);
        return this;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
