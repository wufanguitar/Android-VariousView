package com.wufanguitar.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.wufanguitar.annotate.Visibility;
import com.wufanguitar.variousview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: 加载中 -> 加载失败/加载成功
 */

public class LoadingView extends View {
    // 加载默认的状态
    private static final int STATE_UNLOADED = 1;
    // 加载中的状态
    private static final int STATE_LOADING = 2;
    // 加载失败的状态(可重试状态)
    private static final int STATE_FAIL = 3;
    // 加载空的状态
    private static final int STATE_EMPTY = 4;
    // 加载成功的状态
    private static final int STATE_SUCCESS = 5;

    private static final int LOADING_RESOURCE_ID = 100;

    private LayoutInflater mInflater;
    private RelativeLayout.LayoutParams mLayoutParams;

    private int mEmptyResource;
    private int mFailResource;
    private int mLoadingResource;

    private View mLoadingView;
    private View mFailView;
    private View mEmptyView;

    private OnInflateListener mInflateListener;
    private OnRetryClickListener mRetryClickListener;

    @IntDef({STATE_UNLOADED, STATE_LOADING, STATE_FAIL, STATE_EMPTY, STATE_SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceType")
    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mEmptyResource = a.getResourceId(R.styleable.LoadingView_emptyResource, 0);
        mFailResource = a.getResourceId(R.styleable.LoadingView_failResource, 0);
        mLoadingResource = a.getResourceId(R.styleable.LoadingView_loadingResource, 0);
        a.recycle();
        if (mEmptyResource == 0) {
            AppCompatTextView emptyView = new AppCompatTextView(context);
            emptyView.setId(LOADING_RESOURCE_ID + 1);
            mEmptyResource = emptyView.getId();
        }
        if (mFailResource == 0) {
            AppCompatTextView failView = new AppCompatTextView(context);
            failView.setId(LOADING_RESOURCE_ID - 1);
            mFailResource = failView.getId();
        }
        if (mLoadingResource == 0) {
            AppCompatTextView loadingView = new AppCompatTextView(context);
            loadingView.setId(LOADING_RESOURCE_ID);
            mLoadingResource = loadingView.getId();
        }
        if (attrs == null) {
            mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mLayoutParams = new RelativeLayout.LayoutParams(context, attrs);
        }
        setVisibility(GONE);
        setWillNotDraw(true);
    }

    // 注入到Activity
    public static LoadingView inject(@NonNull Activity activity) {
        return inject(activity, false);
    }

    public static LoadingView inject(@NonNull Activity activity, boolean hasActionBar) {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        return inject(rootView, hasActionBar);
    }

    // 注入到View
    public static LoadingView inject(@NonNull View view) {
        return inject(view, false);
    }

    public static LoadingView inject(@NonNull View view, boolean hasActionBar) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            return inject(parent, hasActionBar);
        } else {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                return inject((ViewGroup) parent, hasActionBar);
            } else {
                throw new ClassCastException("view or view.getParent() must be ViewGroup");
            }
        }
    }

    public static LoadingView inject(@NonNull ViewGroup parent, boolean hasActionBar) {
        // 因为 LinearLayout/ScrollView/AdapterView/RecyclerView/NestedScrollView 的特性
        // 为了LoadingView能正常显示，自动再套一层（开发的时候就不用额外的工作量了）
        // 如果碰到实现更复杂的需求可在layout中使用LoadingView
        int screenHeight = 0;
        if (parent instanceof LinearLayout ||
                parent instanceof ScrollView ||
                parent instanceof AdapterView ||
                (parent instanceof ScrollingView && parent instanceof NestedScrollingChild) ||
                (parent instanceof NestedScrollingParent && parent instanceof NestedScrollingChild)) {
            ViewParent viewParent = parent.getParent();
            if (viewParent == null) {
                FrameLayout wrapper = new FrameLayout(parent.getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                wrapper.setLayoutParams(layoutParams);
                if (parent instanceof LinearLayout) {
                    LinearLayout wrapLayout = new LinearLayout(parent.getContext());
                    wrapLayout.setLayoutParams(parent.getLayoutParams());
                    wrapLayout.setOrientation(((LinearLayout) parent).getOrientation());
                    for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
                        View childView = parent.getChildAt(0);
                        parent.removeView(childView);
                        wrapLayout.addView(childView);
                    }
                    wrapper.addView(wrapLayout);
                } else if (parent instanceof ScrollView || parent instanceof ScrollingView) {
                    if (parent.getChildCount() != 1) {
                        throw new IllegalStateException("the ScrollView does not have one direct child");
                    }
                    View directView = parent.getChildAt(0);
                    parent.removeView(directView);
                    wrapper.addView(directView);
                    WindowManager wm = (WindowManager) parent.getContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    DisplayMetrics metrics = new DisplayMetrics();
                    if (wm != null) {
                        wm.getDefaultDisplay().getMetrics(metrics);
                        screenHeight = metrics.heightPixels;
                    }
                } else if (parent instanceof NestedScrollingParent &&
                        parent instanceof NestedScrollingChild) {
                    if (parent.getChildCount() == 2) {
                        View targetView = parent.getChildAt(1);
                        parent.removeView(targetView);
                        wrapper.addView(targetView);
                    } else if (parent.getChildCount() > 2) {
                        throw new IllegalStateException("the view is not refresh layout? view = "
                                + parent.toString());
                    }
                } else {
                    throw new IllegalStateException("the view does not have parent, view = "
                            + parent.toString());
                }
                parent.addView(wrapper);
                parent = wrapper;
            } else {
                FrameLayout root = new FrameLayout(parent.getContext());
                root.setLayoutParams(parent.getLayoutParams());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parent.setLayoutParams(layoutParams);
                if (viewParent instanceof ViewGroup) {
                    ViewGroup rootGroup = (ViewGroup) viewParent;
                    // 把parent从它自己的父容器中移除
                    rootGroup.removeView(parent);
                    // 然后替换成新的
                    rootGroup.addView(root);
                }
                root.addView(parent);
                parent = root;
            }
        }
        LoadingView loadingView = new LoadingView(parent.getContext());
        if (screenHeight > 0) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    hasActionBar ? screenHeight - loadingView.getActionBarHeight() : screenHeight);
            parent.addView(loadingView, params);
        } else {
            parent.addView(loadingView);
        }
        if (hasActionBar) {
            loadingView.setTopMargin();
        }
        loadingView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        loadingView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        return loadingView;
    }

    // 当有actionbar/toolbar的时候设置topMargin
    public void setTopMargin() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = getActionBarHeight();
    }

    public int getActionBarHeight() {
        int height = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return height;
    }

    @Override
    public void setVisibility(int visibility) {
        setVisibility(mEmptyView, visibility);
        setVisibility(mFailView, visibility);
        setVisibility(mLoadingView, visibility);
    }

    private void setVisibility(View view, @Visibility int visibility) {
        if (view != null && visibility != view.getVisibility()) {
            view.setVisibility(visibility);
        }
    }

    private View inflate(@LayoutRes int layoutResource, @State int viewType) {
        final ViewParent viewParent = getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            if (layoutResource != 0) {
                final ViewGroup parent = (ViewGroup) viewParent;
                final LayoutInflater factory;
                if (mInflater != null) {
                    factory = mInflater;
                } else {
                    factory = LayoutInflater.from(getContext());
                }
                final View view = factory.inflate(layoutResource, parent, false);
                final int index = parent.indexOfChild(this);
                // 防止还能触摸底下的 View
                view.setClickable(true);
                // 先不显示
                view.setVisibility(GONE);
                final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    if (parent instanceof RelativeLayout) {
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutParams;
                        mLayoutParams.setMargins(lp.leftMargin, lp.topMargin,
                                lp.rightMargin, lp.bottomMargin);
                        parent.addView(view, index, mLayoutParams);
                    } else {
                        parent.addView(view, index, layoutParams);
                    }
                } else {
                    parent.addView(view, index);
                }
                if (mLoadingView != null && mFailView != null && mEmptyView != null) {
                    parent.removeViewInLayout(this);
                }
                if (mInflateListener != null) {
                    mInflateListener.onInflate(viewType, view);
                }
                return view;
            } else {
                throw new IllegalArgumentException("LoadingView must have a valid layoutResource");
            }
        } else {
            throw new IllegalStateException("LoadingView must have a non-null ViewGroup viewParent");
        }
    }

    public View showEmpty() {
        if (mEmptyView == null) {
            mEmptyView = inflate(mEmptyResource, STATE_EMPTY);
        }
        showView(mEmptyView);
        return mEmptyView;
    }

    public View showFail() {
        if (mFailView == null) {
            mFailView = inflate(mFailResource, STATE_FAIL);
        }
        showView(mFailView);
        return mFailView;
    }

    public View showLoading() {
        if (mLoadingView == null) {
            mLoadingView = inflate(mLoadingResource, STATE_LOADING);
        }
        showView(mLoadingView);
        return mLoadingView;
    }

    // 加载成功时，所有状态全部不可见
    public void showSuccess() {
        setVisibility(GONE);
    }

    private void showView(View view) {
        setVisibility(view, VISIBLE);
        hideViews(view);
    }

    private void hideViews(View showView) {
        if (mEmptyView == showView) {
            setVisibility(mLoadingView, GONE);
            setVisibility(mFailView, GONE);
        } else if (mLoadingView == showView) {
            setVisibility(mEmptyView, GONE);
            setVisibility(mFailView, GONE);
        } else {
            setVisibility(mEmptyView, GONE);
            setVisibility(mLoadingView, GONE);
        }
    }

    public LoadingView setEmptyResource(@LayoutRes int emptyResource) {
        this.mEmptyResource = emptyResource;
        return this;
    }

    public LoadingView setRetryResource(@LayoutRes int retryResource) {
        this.mFailResource = retryResource;
        return this;
    }

    public LoadingView setLoadingResource(@LayoutRes int loadingResource) {
        this.mLoadingResource = loadingResource;
        return this;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    public void setOnInflateListener(OnInflateListener inflateListener) {
        mInflateListener = inflateListener;
    }

    public interface OnInflateListener {
        void onInflate(@State int viewType, View view);
    }

    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mRetryClickListener = listener;
    }

    public interface OnRetryClickListener {
        void onRetryClick();
    }
}
