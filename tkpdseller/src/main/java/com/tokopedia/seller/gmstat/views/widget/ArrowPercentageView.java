package com.tokopedia.seller.gmstat.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.GMStatConstant;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.PercentageUtil;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.PERCENTAGE_FORMAT;

/**
 * Created by User on 7/10/2017.
 */

public class ArrowPercentageView extends FrameLayout {
    private double percentage = GMStatConstant.NO_DATA_AVAILABLE;
    private float textSize;

    private ImageView ivArrowIcon;
    private TextView tvPercentage;
    private View view;
    private int downDrawableSrc = R.drawable.ic_rectangle_down;
    private int upDrawableSrc = R.drawable.ic_rectangle_up;
    private int stagnantDrawableSrc = 0;
    private int redColor = R.color.arrow_down;
    private int greenColor = R.color.arrow_up;
    private int greyColor = R.color.grey_400;
    private PercentageUtil percentageUtil;
    private int noDataRes = R.string.no_data;

    public ArrowPercentageView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public ArrowPercentageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public ArrowPercentageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArrowPercentageView);
        if (a.hasValue(R.styleable.ArrowPercentageView_arrow_percentage_value)) {
            percentage = a.getFloat(R.styleable.ArrowPercentageView_arrow_percentage_value, 0);
        }
        textSize = a.getDimension(R.styleable.ArrowPercentageView_arrow_percentage_text_size, 0);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_gm_percentage, this);
        this.view = view;
        ivArrowIcon = (ImageView) view.findViewById(R.id.iv_arrow_icon);
        tvPercentage = (TextView) view.findViewById(R.id.tv_percentage);
        if (textSize > 0) {
            tvPercentage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        setUIPercentage();
        setAddStatesFromChildren(true);
    }

    private void setUIPercentage(){
        if (percentageUtil != null) {
            percentageUtil.calculatePercentage(percentage, ivArrowIcon, tvPercentage);
        } else {
            if (percentage == GMStatConstant.NO_DATA_AVAILABLE) {
                ivArrowIcon.setVisibility(View.GONE);
                tvPercentage.setText(noDataRes);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(), greyColor));
            } else {
                ivArrowIcon.setVisibility(View.VISIBLE);
                if (percentage < 0) {
                    ivArrowIcon.setImageResource(downDrawableSrc);
                    tvPercentage.setTextColor(ContextCompat.getColor(getContext(), redColor));
                } else if (percentage > 0) {
                    ivArrowIcon.setImageResource(upDrawableSrc);
                    tvPercentage.setTextColor(ContextCompat.getColor(getContext(), greenColor));
                } else if (percentage == 0) { // percentage is 0
                    ivArrowIcon.setImageResource(stagnantDrawableSrc);
                    tvPercentage.setTextColor(ContextCompat.getColor(getContext(), greyColor));
                }
                tvPercentage.setText(String.format(PERCENTAGE_FORMAT,
                        KMNumbers.formatString(percentage * 100).replace("-", "")));
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    // input percentage 1 for 100%
    public void setPercentage(double percentage){
        this.percentage = percentage;
        setUIPercentage();
    }

    public void setNoDataPercentage(){
        setPercentage(GMStatConstant.NO_DATA_AVAILABLE);
    }

    public void setPercentageUtil(PercentageUtil percentageUtil) {
        this.percentageUtil = percentageUtil;
    }
}
