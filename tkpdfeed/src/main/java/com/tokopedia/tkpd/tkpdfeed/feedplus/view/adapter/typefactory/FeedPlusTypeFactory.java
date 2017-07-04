package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.AddFeedModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ActivityCardViewModel viewModel);

    int type(PromotedShopViewModel viewModel);

    int type(PromoCardViewModel promoCardViewModel);

    int type(OfficialStoreViewModel officialStoreViewModel);

    int type(InspirationViewModel inspirationViewModel);

    int type(BlogViewModel viewModel);

    int type(PromotedProductViewModel promotedProductViewModel);

    int type(AddFeedModel addFeedModel);

    int type(RecentViewViewModel recentViewViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
