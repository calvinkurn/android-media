package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.InspirationViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.OfficialStoreViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.ImageBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.VideoBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.PromoViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.PromotedShopViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.ProductCardViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;

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
        return ProductCardViewHolder.LAYOUT;
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
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == EmptyFeedViewHolder.LAYOUT)
            viewHolder = new EmptyFeedViewHolder(view);
        else if (type == ProductCardViewHolder.LAYOUT)
            viewHolder = new ProductCardViewHolder(view, viewListener);
        else if (type == PromotedShopViewHolder.LAYOUT)
            viewHolder = new PromotedShopViewHolder(view);
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
        else
            viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
