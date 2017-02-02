package com.tokopedia.seller.gmsubscribe.view.home.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastianuskh on 1/20/17.
 * View for displaying the gm subscribe main features
 */

public class GMMainFeatureView extends FrameLayout {

    /** BUTTERKNIFE VIEWS FIELD */
    @BindView(R2.id.image_gm_subscribe_main_feature)
    ImageView imageViewFeature;

    @BindView(R2.id.title_gm_subscribe_main_feature)
    TextView textViewTitleFeature;

    @BindView(R2.id.desc_gm_subscribe_main_feature)
    TextView textViewDescFeature;

    private Drawable imgFeature;
    private String titleFeature;
    private String descFeature;


    /** CONSTRUCTOR */
    public GMMainFeatureView(Context context) {
        super(context);
        initView();
    }

    public GMMainFeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public GMMainFeatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        initView();

        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GMFeatureHomeView);
        try {
            imgFeature = styledAttributes.getDrawable(R.styleable.GMFeatureHomeView_feature_img);
            titleFeature = styledAttributes.getString(R.styleable.GMFeatureHomeView_feature_title);
            descFeature = styledAttributes.getString(R.styleable.GMFeatureHomeView_feature_desc);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_gm_subscribe_main_feature, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageViewFeature.setImageDrawable(imgFeature);
        textViewTitleFeature.setText(titleFeature);
        textViewDescFeature.setText(descFeature);
        invalidate();
        requestLayout();
    }
}
