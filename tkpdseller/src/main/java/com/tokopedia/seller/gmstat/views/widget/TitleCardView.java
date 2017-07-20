package com.tokopedia.seller.gmstat.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by hendry on 7/10/2017.
 */

public class TitleCardView extends CardView {

    @LayoutRes
    public static final int DEFAULT_LOADING_LAYOUT_RES = R.layout.widget_line_chart_container_loading;
    @LayoutRes
    public static final int DEFAULT_LOADING_TITLE_LAYOUT_RES = R.layout.widget_title_card_loading;

    FrameLayout mFrameLayout;
    OnArrowDownClickListener onArrowDownClickListener;
    private TextView tvTitle;
    private float mTitleTextSize;
    private ImageView ivArrowDown;
    private ViewGroup vgTitle;

    private View emptyView;
    private View loadingView;
    private View loadingTitleView;
    private View contentView;

    private CharSequence mTitleString;
    private int mLoadingLayoutRes;
    private int mEmptyLayoutRes;
    private Drawable mIconDrawable;
    private boolean mUseGradientTitleLoading;
    private ViewGroup vgTitleContent;

    public TitleCardView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public TitleCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public TitleCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitleCardView);
        mTitleString = a.getString(R.styleable.TitleCardView_title);
        mTitleTextSize = a.getDimensionPixelSize(R.styleable.TitleCardView_title_size, 0);
        mLoadingLayoutRes = a.getResourceId(R.styleable.TitleCardView_loading_layout, DEFAULT_LOADING_LAYOUT_RES);
        mEmptyLayoutRes = a.getResourceId(R.styleable.TitleCardView_empty_layout, 0);
        mIconDrawable = a.getDrawable(R.styleable.TitleCardView_icon);
        mUseGradientTitleLoading = a.getBoolean(R.styleable.TitleCardView_use_gradient_title_loading, true);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_title_card, this);
        vgTitle = (ViewGroup) view.findViewById(R.id.vg_title);
        vgTitleContent = (ViewGroup) vgTitle.findViewById(R.id.vg_title_content);
        tvTitle = (TextView) vgTitleContent.findViewById(R.id.tv_title);
        if (mTitleTextSize!= 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        }
        ImageView ivIcon = (ImageView) vgTitleContent.findViewById(R.id.iv_icon);
        if (mIconDrawable == null) {
            ivIcon.setVisibility(View.GONE);
        } else {
            ivIcon.setImageDrawable(mIconDrawable);
            ivIcon.setVisibility(View.VISIBLE);
        }
        ivArrowDown = (ImageView) vgTitleContent.findViewById(R.id.iv_arrow_down);

        mFrameLayout = (FrameLayout) view.findViewById(R.id.frame_content);

        setTitle(mTitleString);
        ivArrowDown.setVisibility(View.GONE);

        this.setPreventCornerOverlap(false);

        setAddStatesFromChildren(true);
        mFrameLayout.setAddStatesFromChildren(true);
    }

    public void setOnArrowDownClickListener(final OnArrowDownClickListener onArrowDownClickListener) {
        this.onArrowDownClickListener = onArrowDownClickListener;
        if (onArrowDownClickListener == null) {
            vgTitleContent.setOnClickListener(null);
            ivArrowDown.setOnClickListener(null);
        } else {
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArrowDownClickListener.onArrowDownClicked();
                }
            };
            vgTitleContent.setOnClickListener(onClickListener);
            ivArrowDown.setOnClickListener(onClickListener);
        }
        checkArrowDownVisibility();
    }

    // there are 2 condition,
    // when the title is empty, or the listener is empty,
    // arrowDown will be hidden and title cannot be clicked
    private void checkArrowDownVisibility(){
        if (onArrowDownClickListener== null || TextUtils.isEmpty(tvTitle.getText())){
            vgTitleContent.setClickable(false);
            ivArrowDown.setVisibility(View.GONE);
        } else {
            vgTitleContent.setClickable(true);
            ivArrowDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.vg_title_card_view) {
            // Carry on adding the View...
            super.addView(child, index, params);
        } else {
            // remove the link between child and previous parent before add (if any)
            if (child.getParent()!= null) {
                ViewGroup viewParent = (ViewGroup) child.getParent();
                viewParent.removeView(child);
            }
            mFrameLayout.addView(child, params);
            // assume first view added is the content;
            if (contentView == null) {
                contentView = child;
            }
        }
    }

    public void setLoadingViewRes(int loadingViewRes) {
        View emptyView = LayoutInflater.from(getContext()).inflate(loadingViewRes, mFrameLayout, false);
        setLoadingView(emptyView);
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(View loadingView) {
        if (this.loadingView!= null) {
            mFrameLayout.removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        loadingView.setVisibility(View.GONE);
        addView(loadingView);
    }

    public void setEmptyViewRes(int emptyViewRes) {
        View emptyView = LayoutInflater.from(getContext()).inflate(emptyViewRes, mFrameLayout, false);
        setEmptyView(emptyView);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        if (this.emptyView!= null) {
            mFrameLayout.removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        emptyView.setVisibility(View.GONE);
        addView(emptyView);
    }

    // showing that the content to loading state
    public void setLoadingState (boolean isLoading){
        if (loadingView == null) {
            setDefaultLoadingView();
        }
        if (isLoading && this.loadingView != null) {
            if (contentView!= null) {
                contentView.setVisibility(View.GONE);
            }
            if (emptyView!= null) {
                emptyView.setVisibility(View.GONE);
            }
            loadingView.setVisibility(View.VISIBLE);
            if (mUseGradientTitleLoading) {
                setLoadingTitleState(true);
            }
        } else {
            setContentVisible();
            setLoadingTitleState(false);
        }
    }

    // will animate the title to gradient, showing that the title is loading
    private void setLoadingTitleState(boolean isLoading){
        if (loadingTitleView == null) {
            setDefaultLoadingTitleView();
        }
        if (isLoading && this.loadingTitleView != null) {
            if (vgTitleContent!= null) {
                vgTitleContent.setVisibility(View.GONE);
            }
            loadingTitleView.setVisibility(View.VISIBLE);
        } else {
            if (loadingTitleView!= null) {
                loadingTitleView.setVisibility(View.GONE);
            }
            if (vgTitleContent!= null) {
                vgTitleContent.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setEmptyState (boolean isEmpty){
        if (emptyView == null) {
            setDefaultEmptyView();
        }
        if (isEmpty && emptyView!= null) {
            if (contentView!= null){
                contentView.setVisibility(View.GONE);
            }
            if (this.loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
            emptyView.setVisibility(View.VISIBLE);
        } else {
            setContentVisible();
        }
    }

    public void setContentVisible() {
        if (this.loadingView!= null) {
            loadingView.setVisibility(View.GONE);
        }
        if (emptyView!=null) {
            emptyView.setVisibility(View.GONE);
        }
        contentView.setVisibility(View.VISIBLE);
    }

    public void setDefaultLoadingView(){
        if (mLoadingLayoutRes == 0) return;
        this.loadingView = LayoutInflater.from(getContext()).inflate(mLoadingLayoutRes, mFrameLayout, false);
        loadingView.setVisibility(View.GONE);
        addView(loadingView);
    }

    public void setDefaultLoadingTitleView(){
        this.loadingTitleView = LayoutInflater.from(getContext()).inflate(DEFAULT_LOADING_TITLE_LAYOUT_RES, vgTitle, false);
        loadingTitleView.setVisibility(View.GONE);
        vgTitle.addView(loadingTitleView);
    }

    public void setDefaultEmptyView(){
        if (mEmptyLayoutRes == 0) return;
        this.emptyView = LayoutInflater.from(getContext()).inflate(mEmptyLayoutRes, mFrameLayout, false);
        emptyView.setVisibility(View.GONE);
        addView(emptyView);
    }

    public void setTitle(CharSequence title) {
        mTitleString = title;
        tvTitle.setText(title);
        checkArrowDownVisibility();
    }

    public interface OnArrowDownClickListener {
        void onArrowDownClicked();
    }
}
