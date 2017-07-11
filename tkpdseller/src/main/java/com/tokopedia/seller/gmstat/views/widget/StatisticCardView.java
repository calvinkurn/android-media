package com.tokopedia.seller.gmstat.views.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.PercentageUtil;

/**
 * Created by User on 7/10/2017.
 */

public class StatisticCardView extends CardView {

    FrameLayout mFrameLayout;
    OnArrowDownClickListener onArrowDownClickListener;
    private TextView tvTitle;
    private ImageView ivArrowDown;
    private TextView tvAmount;
    private ArrowPercentageView arrowPercentageView;
    private TextView tvSubtitle;
    private TextView tvPercentageDesc;

    public StatisticCardView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public StatisticCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public StatisticCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {

    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_statistic_card, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ivArrowDown = (ImageView) view.findViewById(R.id.iv_arrow_down);
        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
        arrowPercentageView = (ArrowPercentageView) view.findViewById(R.id.view_arrow_percentage);
        tvSubtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        tvPercentageDesc = (TextView) view.findViewById(R.id.tv_percentage_description);

        mFrameLayout = (FrameLayout) view.findViewById(R.id.frame_content);

        ivArrowDown.setVisibility(View.GONE);
        tvSubtitle.setVisibility(View.GONE);
        tvPercentageDesc.setVisibility(View.GONE);
        arrowPercentageView.setVisibility(View.GONE);
        tvAmount.setVisibility(View.GONE);

        setAddStatesFromChildren(true);
        mFrameLayout.setAddStatesFromChildren(true);
    }

    public void setOnArrowDownClickListener(final OnArrowDownClickListener onArrowDownClickListener) {
        this.onArrowDownClickListener = onArrowDownClickListener;
        if (onArrowDownClickListener == null) {
            ivArrowDown.setVisibility(View.GONE);
            tvTitle.setClickable(false);
            tvTitle.setOnClickListener(null);
            ivArrowDown.setOnClickListener(null);
        } else {
            ivArrowDown.setVisibility(View.VISIBLE);
            tvTitle.setClickable(true);
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArrowDownClickListener.onArrowDownClicked();
                }
            };
            tvTitle.setOnClickListener(onClickListener);
            ivArrowDown.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        //TODO hendry setenabled
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (getContext().getString(R.string.chart_tag).equals(child.getTag())) {
            mFrameLayout.addView(child);
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    public void setTitle(CharSequence title){
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }

    }

    public void setAmount(CharSequence formattedAmount) {
        if (TextUtils.isEmpty(formattedAmount)) {
            tvAmount.setVisibility(View.GONE);
        } else {
            tvAmount.setText(formattedAmount);
            tvAmount.setVisibility(View.VISIBLE);
        }
    }

    public void setPercentage(Double percentage) {
        arrowPercentageView.setPercentage(percentage);
        arrowPercentageView.setVisibility(View.VISIBLE);
    }

    public void setSubtitle(CharSequence subtitle) {
        if (TextUtils.isEmpty(subtitle)) {
            tvSubtitle.setVisibility(View.GONE);
        } else {
            tvSubtitle.setText(subtitle);
            tvSubtitle.setVisibility(View.VISIBLE);
        }
    }

    public void setPercentageDesc(CharSequence percentageDesc) {
        if (TextUtils.isEmpty(percentageDesc)) {
            tvPercentageDesc.setVisibility(View.GONE);
        } else {
            tvPercentageDesc.setText(percentageDesc);
            tvPercentageDesc.setVisibility(View.VISIBLE);
        }
    }

    public void setPercentageUtil(PercentageUtil percentageUtil) {
        arrowPercentageView.setPercentageUtil(percentageUtil);
    }

    public interface OnArrowDownClickListener {
        void onArrowDownClicked();
    }
}
