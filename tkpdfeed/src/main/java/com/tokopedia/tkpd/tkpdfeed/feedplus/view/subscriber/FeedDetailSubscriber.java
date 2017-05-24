package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.FeedDetailProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.FeedDetailShopDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.FeedDetailWholesaleDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailSubscriber extends Subscriber<List<DataFeedDetailDomain>> {
    private final FeedPlusDetail.View viewListener;

    public FeedDetailSubscriber(FeedPlusDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetFeedDetail(e.toString());
    }

    @Override
    public void onNext(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        viewListener.onSuccessGetFeedDetail(
                convertToViewModel(dataFeedDetailDomains),
                checkHasNextPage(dataFeedDetailDomains));

    }

    private boolean checkHasNextPage(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        return dataFeedDetailDomains.get(0).getMeta().getHas_next_page();
    }

    private ArrayList<Visitable> convertToViewModel(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        DataFeedDetailDomain dataFeedDetailDomain = dataFeedDetailDomains.get(0);

        ArrayList<Visitable> listDetail = new ArrayList<>();
        listDetail.add(createHeaderViewModel(dataFeedDetailDomain.getSource().getShop()));

        for (FeedDetailProductDomain productDomain : dataFeedDetailDomain.getContent().getProducts()) {
            listDetail.add(createProductViewModel(productDomain));
        }
        return listDetail;
    }

    private FeedDetailViewModel createProductViewModel(FeedDetailProductDomain productDomain) {
        return new FeedDetailViewModel(
                productDomain.getName(),
                productDomain.getPrice(),
                productDomain.getImage(),
                productDomain.getProductLink(),
                productDomain.getCashback(),
                getIsWholesale(productDomain.getWholesale()),
                productDomain.getPreorder(),
                productDomain.getFreereturns(),
                productDomain.getWishlist(),
                productDomain.getRating()
        );
    }

    private boolean getIsWholesale(List<FeedDetailWholesaleDomain> wholesale) {
        return wholesale.size() > 0;
    }


    private FeedDetailHeaderViewModel createHeaderViewModel(FeedDetailShopDomain shop) {
        return new FeedDetailHeaderViewModel(shop.getName(),
                shop.getAvatar(),
                shop.getGold(),
                shop.getShareLinkDescription(),
                shop.getOfficial());
    }
}
