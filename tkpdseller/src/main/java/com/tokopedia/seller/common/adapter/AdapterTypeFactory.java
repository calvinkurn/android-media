package com.tokopedia.seller.common.adapter;

import android.view.View;

import com.tokopedia.seller.common.adapter.model.EmptyModel;
import com.tokopedia.seller.common.adapter.model.LoadingModel;
import com.tokopedia.seller.common.adapter.viewholders.AbstractViewHolder;

/**
 * @author kulomady on 1/24/17.
 */

public interface AdapterTypeFactory {

    int type(EmptyModel viewModel);

    int type(LoadingModel viewModel);

    AbstractViewHolder createViewHolder(View parent, int type);
}
