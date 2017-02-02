package com.tokopedia.seller.gmsubscribe.common.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.seller.gmsubscribe.common.adapter.model.EmptyModel;


/**
 * @author kulomady on 1/24/17.
 */

public class EmptyViewHolder extends AbstractViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = com.tokopedia.core.R.layout.view_no_result;

    public EmptyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(EmptyModel element) {

    }

}
