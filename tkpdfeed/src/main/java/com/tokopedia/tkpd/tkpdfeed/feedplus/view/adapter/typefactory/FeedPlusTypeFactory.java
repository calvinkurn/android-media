package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ProductCardViewModel viewModel);

    int type(PromotedShopViewModel viewModel);

    int type(PromoViewModel viewModel);

    int type(OfficialStoreViewModel officialStoreViewModel);

    int type(BlogViewModel viewModel);


    AbstractViewHolder createViewHolder(View view, int viewType);


}
