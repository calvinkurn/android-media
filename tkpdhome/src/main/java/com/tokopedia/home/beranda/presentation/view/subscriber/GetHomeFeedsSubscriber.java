package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationProductViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.InspirationItemDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by henrypriyono on 1/3/18.
 */

public class GetHomeFeedsSubscriber extends Subscriber<FeedResult> {

    private static final String TYPE_INSPIRATION = "inspirasi";

    private final HomeFeedListener viewListener;
    private final int page;

    public static final String HOMEPAGE_ENHANCE_ANALYTIC = "HOMEPAGE_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private final LocalCacheHandler cache;

    public GetHomeFeedsSubscriber(HomeFeedListener viewListener, int page) {
        this.viewListener = viewListener;
        this.page = page;
        this.cache = new LocalCacheHandler(viewListener.getActivity(), HOMEPAGE_ENHANCE_ANALYTIC);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onShowRetryGetFeed();
    }

    private void clearCacheFeedAnalytic() {
        if (page == 1) {
            LocalCacheHandler.clearCache(viewListener.getActivity(), HOMEPAGE_ENHANCE_ANALYTIC);
        }
    }

    @Override
    public void onNext(FeedResult feedResult) {

        clearCacheFeedAnalytic();

        ArrayList<Visitable> list = convertToViewModel(feedResult.getFeedDomain());

        if (feedResult.isHasNext()) {
            viewListener.updateCursor(getCurrentCursor(feedResult));
        }

        viewListener.onSuccessGetFeed(list);

        if (!feedResult.isHasNext()) {
            viewListener.unsetEndlessScroll();
        }
    }

    private ArrayList<Visitable> convertToViewModel(FeedDomain feedDomain) {
        ArrayList<Visitable> listFeedView = new ArrayList<>();
        addFeedData(listFeedView, feedDomain.getListFeed());
        return listFeedView;
    }

    private void addFeedData(ArrayList<Visitable> listFeedView,
                             List<DataFeedDomain> listFeedDomain) {
        int positionFeedProductCard = cache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
        if (listFeedDomain != null) {
            for (DataFeedDomain domain : listFeedDomain) {
                switch (domain.getContent().getType() != null ? domain.getContent().getType() : "") {

                    case TYPE_INSPIRATION:
                        InspirationViewModel inspirationViewModel = convertToInspirationViewModel(domain);
                        if (inspirationViewModel != null
                                && inspirationViewModel.getListProduct() != null
                                && !inspirationViewModel.getListProduct().isEmpty()) {

                            positionFeedProductCard++;
                            String eventLabel = String.format("%s - %s", "rekomendasi untuk anda", inspirationViewModel.getSource());
                            inspirationViewModel.setEventLabel(eventLabel);
                            inspirationViewModel.setPositionFeedCard(positionFeedProductCard);

                            listFeedView.add(inspirationViewModel);

                            HomePageTracking.eventEnhancedImpressionProductHomePage(
                                    inspirationViewModel.getHomePageImpressionDataLayer()
                            );

                            cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, positionFeedProductCard);
                            cache.applyEditor();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private InspirationViewModel convertToInspirationViewModel(DataFeedDomain domain) {
        if (domain.getContent() != null
                && !domain.getContent().getInspirationDomains().isEmpty()) {
            return new InspirationViewModel(
                    domain.getContent().getInspirationDomains().get(0).getTitle(),
                    convertToRecommendationListViewModel(domain.getContent().getInspirationDomains().get(0).getListInspirationItem()),
                    domain.getContent().getInspirationDomains().get(0).getSource()
            );
        } else {
            return null;
        }
    }

    private ArrayList<InspirationProductViewModel> convertToRecommendationListViewModel(
            List<InspirationItemDomain> domains) {
        ArrayList<InspirationProductViewModel> listRecommendation = new ArrayList<>();
        if (domains != null && domains.size() == 4)
            for (InspirationItemDomain recommendationDomain : domains) {
                listRecommendation.add(convertToRecommendationViewModel(recommendationDomain));
            }
        return listRecommendation;
    }

    private InspirationProductViewModel convertToRecommendationViewModel(
            InspirationItemDomain recommendationDomain) {
        return new InspirationProductViewModel(recommendationDomain.getId(),
                recommendationDomain.getName(),
                recommendationDomain.getPrice(),
                recommendationDomain.getImageUrl(),
                recommendationDomain.getUrl(),
                page,
                recommendationDomain.getPriceInt());
    }

    private String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getFeedDomain().getListFeed().size() - 1;
        return feedResult.getFeedDomain().getListFeed().get(lastIndex).getCursor();
    }
}
