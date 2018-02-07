package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads;

import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ProductFeedTopAdsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ShopFeedTopAdsViewModel;

/**
 * Created by milhamj on 18/01/18.
 */

public interface AdsTypeFactory {
    int type(ShopFeedTopAdsViewModel viewModel);

    int type(ProductFeedTopAdsViewModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);
}
