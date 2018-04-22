package com.wufanguitar.shadow;

import java.io.Serializable;

/**
 * @Author: 吴凡
 * @Email: wu.fanguitar@163.com
 * @Time: 2018/4/22  10:21
 * @Description:
 */

public class ShadowProperty implements Serializable {
    public static final int ALL = 0x1111;
    public static final int LEFT = 0x0001;
    public static final int TOP = 0x0010;
    public static final int RIGHT = 0x0100;
    public static final int BOTTOM = 0x1000;

    // 阴影颜色
    private int mShadowColor;
    // 阴影半径
    private int mShadowRadius;
    // 阴影x偏移
    private int mShadowXOffset;
    // 阴影y偏移
    private int mShadowYOffset;
    // 阴影边
    private int mShadowSide = ALL;

    public int getShadowSide() {
        return mShadowSide;
    }

    public ShadowProperty setShadowSide(int shadowSide) {
        this.mShadowSide = shadowSide;
        return this;
    }

    public int getShadowOffset() {
        return getShadowOffsetHalf() * 2;
    }

    public int getShadowOffsetHalf() {
        return 0 >= mShadowRadius ? 0 : Math.max(mShadowXOffset, mShadowYOffset) + mShadowRadius;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public ShadowProperty setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
        return this;
    }

    public int getShadowRadius() {
        return mShadowRadius;
    }

    public ShadowProperty setShadowRadius(int shadowRadius) {
        this.mShadowRadius = shadowRadius;
        return this;
    }

    public int getShadowXOffset() {
        return mShadowXOffset;
    }

    public ShadowProperty setShadowXOffset(int shadowDx) {
        this.mShadowXOffset = shadowDx;
        return this;
    }

    public int getShadowYOffset() {
        return mShadowYOffset;
    }

    public ShadowProperty setShadowYOffset(int shadowDy) {
        this.mShadowYOffset = shadowDy;
        return this;
    }
}
