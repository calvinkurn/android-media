package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailView extends BaseView<Object, DetailResCenterFragmentView> {

    public DetailView(Context context) {
        super(context);
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_detail_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {

    }
}

