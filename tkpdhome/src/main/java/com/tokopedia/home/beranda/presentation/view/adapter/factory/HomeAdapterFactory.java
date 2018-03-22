package com.tokopedia.home.beranda.presentation.view.adapter.factory;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.BannerViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.CategorySectionViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DigitalsViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DynamicChannelHeroViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DynamicChannelSprintViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HeaderViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.RetryViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.SellViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.SixGridChannelViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.TickerViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.inspiration.InspirationViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SixGridViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeAdapterFactory extends BaseAdapterTypeFactory implements HomeTypeFactory {

    private final HomeCategoryListener listener;
    private HomeFeedListener feedListener;
    private final FragmentManager fragmentManager;

    public HomeAdapterFactory(FragmentManager fragmentManager, HomeCategoryListener listener,
                              HomeFeedListener feedListener) {
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        this.feedListener = feedListener;
    }

    @Override
    public int type(BannerViewModel bannerViewModel) {
        return BannerViewHolder.LAYOUT;
    }

    @Override
    public int type(TickerViewModel tickerViewModel) {
        return TickerViewHolder.LAYOUT;
    }

    @Override
    public int type(DigitalsViewModel digitalsViewModel) {
        return DigitalsViewHolder.LAYOUT;
    }

    @Override
    public int type(CategorySectionViewModel categorySectionViewModel) {
        return CategorySectionViewHolder.LAYOUT;
    }

    @Override
    public int type(SellViewModel sellViewModel) {
        return SellViewHolder.LAYOUT;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(InspirationViewModel inspirationViewModel) {
        return InspirationViewHolder.LAYOUT;
    }

    @Override
    public int type(TopAdsViewModel topAdsViewModel) {
        return TopAdsViewHolder.LAYOUT;
    }

    @Override
    public int type(DynamicChannelViewModel dynamicChannelViewModel) {
        if (DynamicHomeChannel.Channels.LAYOUT_3_IMAGE.equals(dynamicChannelViewModel.getChannel().getLayout())
                || DynamicHomeChannel.Channels.LAYOUT_SPRINT.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return DynamicChannelSprintViewHolder.LAYOUT;
        } else if (DynamicHomeChannel.Channels.LAYOUT_HERO.equals(dynamicChannelViewModel.getChannel().getLayout())) {
            return DynamicChannelHeroViewHolder.LAYOUT;
        } else {
            return 0;
        }
    }

    @Override
    public int type(SixGridViewModel sixGridViewModel) {
        return SixGridChannelViewHolder.LAYOUT;
    }

    @Override
    public int type(RetryModel retryModel) {
        return RetryViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == BannerViewHolder.LAYOUT)
            viewHolder = new BannerViewHolder(view, listener);
        else if (type == TickerViewHolder.LAYOUT)
            viewHolder = new TickerViewHolder(view, listener);
        else if (type == DigitalsViewHolder.LAYOUT)
            viewHolder = new DigitalsViewHolder(fragmentManager, view, listener);
        else if (type == CategorySectionViewHolder.LAYOUT)
            viewHolder = new CategorySectionViewHolder(view, listener);
        else if (type == SellViewHolder.LAYOUT)
            viewHolder = new SellViewHolder(view, listener);
        else if(type == HeaderViewHolder.LAYOUT)
            viewHolder = new HeaderViewHolder(view, listener);
        else if (type == RetryViewHolder.LAYOUT)
            viewHolder = new RetryViewHolder(view, feedListener);
        else if (type == InspirationViewHolder.LAYOUT)
            viewHolder = new InspirationViewHolder(view, feedListener);
        else if (type == DynamicChannelHeroViewHolder.LAYOUT)
            viewHolder = new DynamicChannelHeroViewHolder(view, listener);
        else if (type == DynamicChannelSprintViewHolder.LAYOUT)
            viewHolder = new DynamicChannelSprintViewHolder(view, listener);
        else if (type == TopAdsViewHolder.LAYOUT)
            viewHolder = new TopAdsViewHolder(view);
        else if (type == SixGridChannelViewHolder.LAYOUT)
            viewHolder = new SixGridChannelViewHolder(view, listener);
        else viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
