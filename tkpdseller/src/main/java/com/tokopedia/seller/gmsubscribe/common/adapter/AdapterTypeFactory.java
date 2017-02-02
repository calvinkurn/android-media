package com.tokopedia.seller.gmsubscribe.common.adapter;

import android.view.View;

import com.tokopedia.seller.gmsubscribe.common.adapter.model.EmptyModel;
import com.tokopedia.seller.gmsubscribe.common.adapter.model.LoadingModel;
import com.tokopedia.seller.gmsubscribe.common.adapter.viewholders.AbstractViewHolder;

/**
 * @author kulomady on 1/24/17.
 */

public interface AdapterTypeFactory {

    int type(EmptyModel viewModel);

    int type(LoadingModel viewModel);

    AbstractViewHolder createViewHolder(View parent, int type);
}
