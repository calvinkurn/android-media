package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.listener.OppurtunityView;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OppurtunityDetailButtonView extends BaseView<Object, OppurtunityView> {

    public OppurtunityDetailButtonView(Context context) {
        super(context);
    }

    public OppurtunityDetailButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OppurtunityView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_oppurtunity_button_view;
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
