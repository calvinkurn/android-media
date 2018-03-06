package com.tokopedia.seller.product.variant.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by nakama on 06/03/18.
 */

public class VariantImageView extends FrameLayout {
    public VariantImageView(@NonNull Context context) {
        super(context);
    }

    public VariantImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VariantImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VariantImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
//        TypedArray a = getContext().obtainStyledAttributes(attrs, com.tokopedia.design.R.styleable.LoadingStateView);
//        loadingLayoutRes = a.getResourceId(com.tokopedia.design.R.styleable.LoadingStateView_lsv_loading_layout, DEFAULT_LOADING_LAYOUT_RES);
//        errorLayoutRes = a.getResourceId(com.tokopedia.design.R.styleable.LoadingStateView_lsv_error_layout, VIEW_NOT_AVAILABLE);
//        emptyLayoutRes = a.getResourceId(com.tokopedia.design.R.styleable.LoadingStateView_lsv_empty_layout, VIEW_NOT_AVAILABLE);
//        a.recycle();
    }

    private void init() {
//        View view = inflate(getContext(), com.tokopedia.design.R.layout.widget_loading_state_view, this);
//        frameLayout = (FrameLayout) view.findViewById(com.tokopedia.design.R.id.frame_content);
//        setFocusableInTouchMode(true);
    }

}
