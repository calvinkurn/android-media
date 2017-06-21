package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.FeedDetailProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.FeedDetailShopDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.FeedDetailWholesaleDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailViewModel;

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
        DataFeedDetailDomain dataFeedDetailDomain = dataFeedDetailDomains.get(0);

        viewListener.onSuccessGetFeedDetail(
                createHeaderViewModel(
                        dataFeedDetailDomain.getCreate_time(),
                        dataFeedDetailDomain.getSource().getShop(),
                        dataFeedDetailDomain.getContent().getStatus_activity()),
                convertToViewModel(dataFeedDetailDomain),
                checkHasNextPage(dataFeedDetailDomains));

    }

    private boolean checkHasNextPage(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        return dataFeedDetailDomains.get(0).getMeta().getHas_next_page();
    }

    private ArrayList<Visitable> convertToViewModel(DataFeedDetailDomain dataFeedDetailDomain) {

        ArrayList<Visitable> listDetail = new ArrayList<>();

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
                productDomain.getRating()
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
