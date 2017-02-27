package com.tokopedia.seller.opportunity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.core.product.customview.BaseView;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OppurtunityDetailStatusView extends BaseView<Object, OppurtunityView> {

    public OppurtunityDetailStatusView(Context context) {
        super(context);
    }

    public OppurtunityDetailStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OppurtunityView listener) {

    }

    @Override
    protected int getLayoutView() {
        return 0;
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
