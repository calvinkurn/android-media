package com.tokopedia.home.beranda.presentation.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SprintSaleCarouselViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SixGridViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeTypeFactory {

    int type(BannerViewModel bannerViewModel);

    int type(TickerViewModel tickerViewModel);

    int type(DigitalsViewModel digitalsViewModel);

    int type(CategorySectionViewModel categorySectionViewModel);

    int type(SellViewModel sellViewModel);

    int type(HeaderViewModel headerViewModel);

    int type(TopAdsViewModel topAdsViewModel);

    int type(SprintSaleCarouselViewModel sprintSaleCarouselViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(InspirationViewModel inspirationViewModel);

    int type(DynamicChannelViewModel dynamicChannelViewModel);

    int type(SixGridViewModel sixGridViewModel);
}
