package com.tokopedia.seller.gmstat.views.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderTextView;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.PercentageUtil;

/**
 * Created by hendry on 7/10/2017.
 */

public class TitleCardView extends CardView {

    FrameLayout mFrameLayout;
    OnArrowDownClickListener onArrowDownClickListener;
    private LoaderTextView tvTitle;
    private ImageView ivArrowDown;
    private ViewGroup vgTitle;

    private View emptyView;
    private View loadingView;
    private View contentView;

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

    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_title_card, this);
        vgTitle = (ViewGroup) view.findViewById(R.id.vg_title);
        tvTitle = (LoaderTextView) vgTitle.findViewById(R.id.tv_title);
        ivArrowDown = (ImageView) vgTitle.findViewById(R.id.iv_arrow_down);

        mFrameLayout = (FrameLayout) view.findViewById(R.id.frame_content);

        ivArrowDown.setVisibility(View.GONE);

        this.setPreventCornerOverlap(false);

        setAddStatesFromChildren(true);
        mFrameLayout.setAddStatesFromChildren(true);
    }

    public void setOnArrowDownClickListener(final OnArrowDownClickListener onArrowDownClickListener) {
        this.onArrowDownClickListener = onArrowDownClickListener;
        if (onArrowDownClickListener == null) {
            vgTitle.setOnClickListener(null);
            ivArrowDown.setOnClickListener(null);
        } else {
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArrowDownClickListener.onArrowDownClicked();
                }
            };
            vgTitle.setOnClickListener(onClickListener);
            ivArrowDown.setOnClickListener(onClickListener);
        }
        checkArrowDownVisibility();
    }

    // there are 2 condition,
    // when the title is empty, or the listener is empty,
    // arrowDown will be hidden and title cannot be clicked
    private void checkArrowDownVisibility(){
        if (onArrowDownClickListener== null || TextUtils.isEmpty(tvTitle.getText())){
            vgTitle.setClickable(false);
            ivArrowDown.setVisibility(View.GONE);
        } else {
            vgTitle.setClickable(true);
            ivArrowDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        //TODO hendry setenabled
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

    public void setLoadingView(View loadingView) {
        if (this.loadingView!= null) {
            mFrameLayout.removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        loadingView.setVisibility(View.GONE);
        addView(loadingView);
    }

    public void setEmptyView(View emptyView) {
        if (this.emptyView!= null) {
            mFrameLayout.removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        emptyView.setVisibility(View.GONE);
        addView(emptyView);
    }

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
        } else {
            setContentVisible();
        }
    }

    public void setEmptyState (boolean isEmpty){
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
        this.loadingView = LayoutInflater.from(getContext()).inflate(R.layout.widget_line_chart_container_loading, mFrameLayout, false);
        loadingView.setVisibility(View.GONE);
        addView(loadingView);
    }

    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        checkArrowDownVisibility();
    }

    public interface OnArrowDownClickListener {
        void onArrowDownClicked();
    }
}
