package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.apollographql.apollo.exception.ApolloNetworkException;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.FeedDetailProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.FeedDetailShopDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.FeedDetailWholesaleDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.SingleFeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailSubscriber extends Subscriber<List<DataFeedDetailDomain>> {
    private final FeedPlusDetail.View viewListener;
    private static final int MAX_RATING = 100;
    private static final int NUM_STARS = 5;

    public FeedDetailSubscriber(FeedPlusDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApolloNetworkException)
            viewListener.onErrorGetFeedDetail(viewListener.getString(R.string.msg_network_error));
        else viewListener.onErrorGetFeedDetail(e.toString());
    }

    @Override
    public void onNext(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        if (hasFeed(dataFeedDetailDomains)
                && dataFeedDetailDomains.get(0).getContent().getProducts().size() == 1) {
            DataFeedDetailDomain dataFeedDetailDomain = dataFeedDetailDomains.get(0);
            viewListener.onSuccessGetSingleFeedDetail(
                    createHeaderViewModel(
                            dataFeedDetailDomain.getCreate_time(),
                            dataFeedDetailDomain.getSource().getShop(),
                            dataFeedDetailDomain.getContent().getStatus_activity()),
                    convertToSingleViewModel(dataFeedDetailDomain));
        } else if (hasFeed(dataFeedDetailDomains)) {
            DataFeedDetailDomain dataFeedDetailDomain = dataFeedDetailDomains.get(0);
            viewListener.onSuccessGetFeedDetail(
                    createHeaderViewModel(
                            dataFeedDetailDomain.getCreate_time(),
                            dataFeedDetailDomain.getSource().getShop(),
                            dataFeedDetailDomain.getContent().getStatus_activity()),
                    convertToViewModel(dataFeedDetailDomain),
                    checkHasNextPage(dataFeedDetailDomains));
        } else {
            viewListener.onEmptyFeedDetail();
        }

    }

    private SingleFeedDetailViewModel convertToSingleViewModel(DataFeedDetailDomain dataFeedDetailDomain) {
        FeedDetailProductDomain productDomain = dataFeedDetailDomain.getContent().getProducts()
                .get(0);
        return createSingleProductViewModel(productDomain);
    }

    private SingleFeedDetailViewModel createSingleProductViewModel(FeedDetailProductDomain productDomain) {
        return new SingleFeedDetailViewModel(
                productDomain.getId(),
                productDomain.getName(),
                productDomain.getPrice(),
                productDomain.getImage(),
                productDomain.getProductLink(),
                productDomain.getCashback(),
                getIsWholesale(productDomain.getWholesale()),
                productDomain.getPreorder(),
                productDomain.getFreereturns(),
                productDomain.getWishlist(),
                getRating(productDomain.getRating())
        );
    }

    private Double getRating(Double rating) {
        return rating / MAX_RATING * NUM_STARS;
    }

    private boolean hasFeed(List<DataFeedDetailDomain> dataFeedDetailDomain) {
        return !dataFeedDetailDomain.isEmpty()
                && dataFeedDetailDomain.get(0) != null
                && dataFeedDetailDomain.get(0).getContent() != null
                && dataFeedDetailDomain.get(0).getContent().getProducts() != null
                && !dataFeedDetailDomain.get(0).getContent().getProducts().isEmpty();
    }

    private boolean checkHasNextPage(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        return dataFeedDetailDomains.get(0).getMeta().getHas_next_page();
    }

    private ArrayList<Visitable> convertToViewModel(DataFeedDetailDomain dataFeedDetailDomain) {

        ArrayList<Visitable> listDetail = new ArrayList<>();

        if (dataFeedDetailDomain.getContent() != null && dataFeedDetailDomain.getContent().getProducts() != null)
            for (FeedDetailProductDomain productDomain : dataFeedDetailDomain.getContent().getProducts()) {
                listDetail.add(createProductViewModel(productDomain));
            }
        return listDetail;
    }

    private FeedDetailViewModel createProductViewModel(FeedDetailProductDomain productDomain) {
        return new FeedDetailViewModel(
                productDomain.getId(),
                productDomain.getName(),
                productDomain.getPrice(),
                productDomain.getImage(),
                productDomain.getProductLink(),
                productDomain.getCashback(),
                getIsWholesale(productDomain.getWholesale()),
                productDomain.getPreorder(),
                productDomain.getFreereturns(),
                productDomain.getWishlist(),
                getRating(productDomain.getRating())
        );
    }

    private boolean getIsWholesale(List<FeedDetailWholesaleDomain> wholesale) {
        return wholesale.size() > 0;
    }


    private FeedDetailHeaderViewModel createHeaderViewModel(String create_time,
                                                            FeedDetailShopDomain shop,
                                                            String status_activity) {
        return new FeedDetailHeaderViewModel(shop.getId(),
                shop.getName(),
                shop.getAvatar(),
                shop.getGold(),
                create_time,
                shop.getOfficial(),
                shop.getShopLink(),
                shop.getShareLinkURL(),
                shop.getShareLinkDescription(),
                status_activity
        );
    }
}
