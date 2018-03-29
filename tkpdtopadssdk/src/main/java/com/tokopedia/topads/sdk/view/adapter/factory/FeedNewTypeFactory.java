package com.tokopedia.topads.sdk.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ProductFeedNewViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ShopFeedNewViewModel;

/**
 * @author by milhamj on 29/03/18.
 */

public interface FeedNewTypeFactory {

    int type(ShopFeedNewViewModel viewModel);

    int type(ProductFeedNewViewModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);
}
