package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyTopAdsModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.AddFeedModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.blog.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.brands.BrandsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedShopViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ActivityCardViewModel viewModel);

    int type(PromotedShopViewModel viewModel);

    int type(PromoCardViewModel promoCardViewModel);

    int type(BrandsViewModel officialStoreViewModel);

    int type(InspirationViewModel inspirationViewModel);

    int type(BlogViewModel viewModel);

    int type(PromotedProductViewModel promotedProductViewModel);

    int type(AddFeedModel addFeedModel);

    int type(RecentViewViewModel recentViewViewModel);

    int type(EmptyModel emptyModel);

    int type(EmptyTopAdsModel emptyModel);


    AbstractViewHolder createViewHolder(View view, int viewType);
}
