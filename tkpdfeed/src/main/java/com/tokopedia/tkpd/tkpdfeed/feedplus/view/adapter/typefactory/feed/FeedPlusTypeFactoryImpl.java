package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.EmptyFeedBeforeLoginViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.ImageBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.VideoBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.favoritecta.FavoriteCtaViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.inspiration.InspirationViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.ContentProductViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.KolRecommendationViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.KolViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore.OfficialStoreBrandsViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore.OfficialStoreCampaignViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.ActivityCardViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.AddFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.RetryViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromoViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromotedProductViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromotedShopViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview.RecentViewViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads.FeedTopadsViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.toppicks.ToppicksViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FavoriteCtaViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.blog.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.ContentProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.AddFeedModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedShopViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksViewModel;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory implements FeedPlusTypeFactory {

    private final FeedPlus.View viewListener;
    private final TopAdsItemClickListener topAdsItemClickListener;
    private final FeedPlus.View.Toppicks toppicksListener;
    private final FeedPlus.View.Kol kolViewListener;


    public FeedPlusTypeFactoryImpl(FeedPlusFragment context) {
        this.viewListener = context;
        this.topAdsItemClickListener = context;
        this.toppicksListener = context;
        this.kolViewListener = context;
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
    public int type(OfficialStoreBrandsViewModel brandsViewModel) {
        return OfficialStoreBrandsViewHolder.LAYOUT;
    }

    @Override
    public int type(OfficialStoreCampaignViewModel officialStoreViewModel) {
        return OfficialStoreCampaignViewHolder.LAYOUT;
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
    public int type(ToppicksViewModel toppicksViewModel) {
        return ToppicksViewHolder.LAYOUT;
    }

    @Override
    public int type(KolViewModel kolViewModel) {
        return KolViewHolder.LAYOUT;
    }

    @Override
    public int type(KolRecommendationViewModel kolRecommendationViewModel) {
        return KolRecommendationViewHolder.LAYOUT;
    }

    @Override
    public int type(FeedTopAdsViewModel feedTopAdsViewModel) {
        return FeedTopadsViewHolder.LAYOUT;
    }

    @Override
    public int type(FavoriteCtaViewModel favoriteCtaViewModel) {
        return FavoriteCtaViewHolder.LAYOUT;
    }

    @Override
    public int type(ContentProductViewModel contentProductViewModel) {
        return ContentProductViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyFeedBeforeLoginModel emptyFeedBeforeLoginModel) {
        return EmptyFeedBeforeLoginViewHolder.LAYOUT;
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
        else if (type == OfficialStoreCampaignViewHolder.LAYOUT)
            viewHolder = new OfficialStoreCampaignViewHolder(view, viewListener);
        else if (type == OfficialStoreBrandsViewHolder.LAYOUT)
            viewHolder = new OfficialStoreBrandsViewHolder(view, viewListener);
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
        else if (type == ToppicksViewHolder.LAYOUT)
            viewHolder = new ToppicksViewHolder(view, toppicksListener);
        else if (type == KolViewHolder.LAYOUT)
            viewHolder = new KolViewHolder(view, kolViewListener);
        else if (type == KolRecommendationViewHolder.LAYOUT)
            viewHolder = new KolRecommendationViewHolder(view, kolViewListener);
        else if (type == FeedTopadsViewHolder.LAYOUT)
            viewHolder = new FeedTopadsViewHolder(view, topAdsItemClickListener);
        else if (type == FavoriteCtaViewHolder.LAYOUT)
            viewHolder = new FavoriteCtaViewHolder(view, viewListener);
        else if (type == ContentProductViewHolder.LAYOUT)
            viewHolder = new ContentProductViewHolder(view, viewListener);
        else if (type == EmptyFeedBeforeLoginViewHolder.LAYOUT)
            viewHolder = new EmptyFeedBeforeLoginViewHolder(view, viewListener);
        else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}
