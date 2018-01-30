package com.tokopedia.tkpd.tkpdreputation.review.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpd.tkpdreputation.R;

/**
 * Created by zulfikarrahman on 1/23/18.
 */

public class ReviewProductItemFilterView extends BaseCustomView {

    private static final int DEF_VALUE_EMPTY = 0;
    private ImageView ratingBar;
    private TextView counterRating;
    private int rating;
    private boolean isActive;

    public ReviewProductItemFilterView(@NonNull Context context) {
        super(context);
        init();
    }

    public ReviewProductItemFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ReviewProductItemFilterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ReviewProductItemFilterView);
        try {
            rating = styledAttributes.getInt(R.styleable.ReviewProductItemFilterView_counter_rating, DEF_VALUE_EMPTY);
            isActive = styledAttributes.getBoolean(R.styleable.ReviewProductItemFilterView_isActive, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_item_filter_review, this);
        ratingBar = view.findViewById(R.id.image_rating);
        counterRating = view.findViewById(R.id.counter_star);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        counterRating.setText(String.valueOf(rating));
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        if (active) {
            ratingBar.setImageResource(R.drawable.ic_rating_gold);
        } else {
            ratingBar.setImageResource(R.drawable.ic_rating);
        }
    }
}
