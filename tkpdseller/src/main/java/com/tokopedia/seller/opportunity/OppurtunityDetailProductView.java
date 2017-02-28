package com.tokopedia.seller.opportunity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OppurtunityDetailProductView extends BaseView<Object, OppurtunityView> {

    public OppurtunityDetailProductView(Context context) {
        super(context);
    }

    public OppurtunityDetailProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OppurtunityView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_oppurtunity_product_list_view;
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
