package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.AddFeedModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.AddFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.RecentViewViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.RetryViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.InspirationViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.OfficialStoreViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.PromotedProductViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.ImageBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.VideoBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.PromoViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.PromotedShopViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.ActivityCardViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory implements FeedPlusTypeFactory {

    private final FeedPlus.View viewListener;

    public FeedPlusTypeFactoryImpl(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(ActivityCardViewModel activityCardViewModel) {
        return ActivityCardViewHolder.LAYOUT;
    }

    @Override
    public int type(PromotedShopViewModel viewModel) {
        return PromotedShopViewHolder.LAYOUT;
    }

    @Override
    public int type(PromoCardViewModel viewModel) {
        return PromoViewHolder.LAYOUT;
    }

    @Override
    public int type(OfficialStoreViewModel officialStoreViewModel) {
        return OfficialStoreViewHolder.LAYOUT;
    }

    @Override
    public int type(InspirationViewModel inspirationViewModel) {
        return InspirationViewHolder.LAYOUT;
    }

    @Override
    public int type(BlogViewModel viewModel) {
        if (viewModel.getVideoUrl().equals(""))
            return ImageBlogViewHolder.LAYOUT;
        else
            return VideoBlogViewHolder.LAYOUT;
    }

    @Override
    public int type(PromotedProductViewModel promotedProductViewModel) {
        return PromotedProductViewHolder.LAYOUT;
    }

    @Override
    public int type(AddFeedModel addFeedModel) {
        return AddFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(RecentViewViewModel recentViewViewModel) {
        return RecentViewViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel emptyModel) {
        return EmptyFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(RetryModel retryModel) {
        return RetryViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == EmptyFeedViewHolder.LAYOUT)
            viewHolder = new EmptyFeedViewHolder(view, viewListener);
        else if (type == RetryViewHolder.LAYOUT)
            viewHolder = new RetryViewHolder(view, viewListener);
        else if (type == AddFeedViewHolder.LAYOUT)
            viewHolder = new AddFeedViewHolder(view, viewListener);
        else if (type == ActivityCardViewHolder.LAYOUT)
            viewHolder = new ActivityCardViewHolder(view, viewListener);
        else if (type == PromotedShopViewHolder.LAYOUT)
            viewHolder = new PromotedShopViewHolder(view, viewListener);
        else if (type == PromoViewHolder.LAYOUT)
            viewHolder = new PromoViewHolder(view, viewListener);
        else if (type == OfficialStoreViewHolder.LAYOUT)
            viewHolder = new OfficialStoreViewHolder(view, viewListener);
        else if (type == InspirationViewHolder.LAYOUT)
            viewHolder = new InspirationViewHolder(view, viewListener);
        else if (type == ImageBlogViewHolder.LAYOUT)
            viewHolder = new ImageBlogViewHolder(view, viewListener);
        else if (type == VideoBlogViewHolder.LAYOUT)
            viewHolder = new VideoBlogViewHolder(view, viewListener);
        else if (type == PromotedProductViewHolder.LAYOUT)
            viewHolder = new PromotedProductViewHolder(view, viewListener);
        else if (type == RecentViewViewHolder.LAYOUT)
            viewHolder = new RecentViewViewHolder(view, viewListener);
        else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}
