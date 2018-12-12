package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpdpdp.R;

public class RatingBarWithTextView extends BaseCustomView {
    TextView tv_rating;
    TextView tv_max_rating;

    public RatingBarWithTextView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public RatingBarWithTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RatingBarWithTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setRating(int rating) {
        String ratingText = String.valueOf(rating);
        tv_rating.setText(ratingText);
    }
    public void setRating(String rating) {
        tv_rating.setText(rating);
    }


    public void setMaxRating(int maxRating) {
        String maxRatingText = String.valueOf(maxRating);
        tv_rating.setText(maxRatingText);
    }

    public int getLayoutView(){
        return R.layout.view_rating_bar_with_text;
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        tv_rating = findViewById(R.id.tv_rating);
        tv_max_rating = findViewById(R.id.tv_max_rating);
    }
}
