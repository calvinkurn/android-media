package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads;

import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ShopFeedViewModel;

/**
 * Created by milhamj on 18/01/18.
 */

public interface AdsTypeFactory {
    int type(ShopFeedViewModel viewModel);

    int type(ProductFeedViewModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);
}
