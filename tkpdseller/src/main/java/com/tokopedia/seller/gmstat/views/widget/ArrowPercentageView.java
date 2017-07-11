package com.tokopedia.seller.gmstat.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;
import com.tokopedia.seller.gmstat.utils.KMNumbers;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.PERCENTAGE_FORMAT;

/**
 * Created by User on 7/10/2017.
 */

public class ArrowPercentageView extends FrameLayout {
    private double mPercentage;
    private ImageView ivArrowIcon;
    private LoaderTextView tvPercentage;
    private View view;
    private int downDrawableSrc = R.drawable.ic_rectangle_down;
    private int upDrawableSrc = R.drawable.ic_rectangle_up;
    private int stagnantDrawableSrc = 0;
    private int redColor = R.color.arrow_down;
    private int greenColor = R.color.arrow_up;
    private int greyColor = R.color.grey_400;

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
        mPercentage = a.getFloat(R.styleable.ArrowPercentageView_percentage, -101);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_gm_percentage, this);
        this.view = view;
        ivArrowIcon = (ImageView) view.findViewById(R.id.iv_arrow_icon);
        tvPercentage = (LoaderTextView) view.findViewById(R.id.tv_percentage);
        setUIPercentage();
        setAddStatesFromChildren(true);
    }

    private void setUIPercentage(){
        if (mPercentage < -100 || mPercentage > 100) {
            this.view.setVisibility(View.GONE);
        } else {
            if (mPercentage < 0) {
                ivArrowIcon.setImageResource(downDrawableSrc);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(),redColor));
            } else if (mPercentage > 0) {
                ivArrowIcon.setImageResource(upDrawableSrc);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(),greenColor));
            } else { // percentage is 0
                ivArrowIcon.setImageResource(stagnantDrawableSrc);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(),greyColor));
            }
            tvPercentage.setText(String.format(PERCENTAGE_FORMAT,
                    KMNumbers.formatString(mPercentage).replace("-", "")));
            this.view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        //TODO hendry setenabled
    }

    public void setPercentage(double percentage){
        mPercentage = percentage;
        setUIPercentage();
    }

}
