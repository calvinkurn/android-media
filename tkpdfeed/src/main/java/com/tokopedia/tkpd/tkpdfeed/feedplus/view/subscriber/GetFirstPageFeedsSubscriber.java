package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/29/17.
 */

public class GetFirstPageFeedsSubscriber extends Subscriber<FeedResult> {

    private final FeedPlus.View viewListener;
    private static final String TYPE_ACTIVITY = "activity";
    private static final String TYPE_NEW_PRODUCT = "new_product";
    private static final String TYPE_PROMOTION = "promotion";



    public GetFirstPageFeedsSubscriber(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetFeedFirstPage(e.toString());
    }

    @Override
    public void onNext(FeedResult feedResult) {
        if (feedResult.getDataSource() == FeedResult.SOURCE_LOCAL) {
            viewListener.onSuccessGetFeedFirstPage(
                    convertToViewModel(feedResult.getDataFeedDomainList()));
        } else {
            viewListener.onSuccessGetFeedFirstPage(
                    convertToViewModel(feedResult.getDataFeedDomainList()));
        }

        if (feedResult.getDataFeedDomainList().size() > 0)
            viewListener.updateCursor(getCurrentCursor(feedResult));
    }

    private ArrayList<Visitable> convertToViewModel(List<DataFeedDomain> dataFeedDomainList) {
        ArrayList<Visitable> listFeed = new ArrayList<>();
        for (DataFeedDomain domain : dataFeedDomainList) {
            switch (domain.getContent().getType()) {
                case TYPE_NEW_PRODUCT:
                    listFeed.add(convertToActivityViewModel(domain));
                    break;
                default:
                    break;
            }
        }
        return listFeed;
    }

    private ActivityCardViewModel convertToActivityViewModel(DataFeedDomain domain) {
        return new ActivityCardViewModel(
                convertToProductCardHeaderViewModel(domain),
                convertToProductListViewModel(domain),
                domain.getSource().getShop().getShareLinkURL(),
                domain.getContent().getStatusActivity());
    }

    private ProductCardHeaderViewModel convertToProductCardHeaderViewModel(DataFeedDomain domain) {
        return new ProductCardHeaderViewModel(
                domain.getSource().getShop().getName(),
                domain.getSource().getShop().getAvatar(),
                domain.getSource().getShop().getGold(),
                domain.getCreateTime(),
                domain.getSource().getShop().getOfficial()
        );
    }

    private ArrayList<ProductFeedViewModel> convertToProductListViewModel(DataFeedDomain dataFeedDomain) {
        ArrayList<ProductFeedViewModel> listProduct = new ArrayList<>();
        for (ProductFeedDomain domain : dataFeedDomain.getContent().getProducts()) {
            listProduct.add(
                    new ProductFeedViewModel(
                            domain.getName(),
                            domain.getPrice(),
                            domain.getImage(),
                            (String) domain.getUrl(),
                            dataFeedDomain.getSource().getShop().getName(),
                            dataFeedDomain.getSource().getShop().getAvatar(),
                            domain.getWishlist()));
        }
        return listProduct;
    }

    private String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getDataFeedDomainList().size() - 1;
        return feedResult.getDataFeedDomainList().get(lastIndex).getCursor();
    }
}
