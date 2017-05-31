package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.PromotionFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/29/17.
 */

public class GetFirstPageFeedsSubscriber extends Subscriber<FeedResult> {

    protected final FeedPlus.View viewListener;
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
        viewListener.onErrorGetFeedFirstPage(
                ErrorHandler.getErrorMessage(e,
                        viewListener.getActivity()));
    }

    @Override
    public void onNext(FeedResult feedResult) {
        viewListener.onSuccessGetFeedFirstPage(
                convertToViewModel(feedResult.getDataFeedDomainList()));

        if (feedResult.getDataFeedDomainList().size() > 0)
            viewListener.updateCursor(getCurrentCursor(feedResult));
    }

    protected ArrayList<Visitable> convertToViewModel(List<DataFeedDomain> dataFeedDomainList) {
        ArrayList<Visitable> listFeed = new ArrayList<>();
        for (DataFeedDomain domain : dataFeedDomainList) {
            switch (domain.getContent().getType()) {
                case TYPE_NEW_PRODUCT:
                    listFeed.add(convertToActivityViewModel(domain));
                    break;
                case TYPE_PROMOTION:
                    listFeed.add(convertToPromoViewModel(domain));
                    break;
                default:
                    break;
            }
        }
        return listFeed;
    }

    protected ActivityCardViewModel convertToActivityViewModel(DataFeedDomain domain) {
        return new ActivityCardViewModel(
                convertToProductCardHeaderViewModel(domain),
                convertToProductListViewModel(domain),
                domain.getSource().getShop().getShareLinkURL(),
                domain.getSource().getShop().getShareLinkDescription(),
                domain.getContent().getStatusActivity(),
                domain.getId(),
                domain.getContent().getTotalProduct());
    }

    protected ProductCardHeaderViewModel convertToProductCardHeaderViewModel(DataFeedDomain domain) {
        return new ProductCardHeaderViewModel(
                domain.getSource().getShop().getId(),
                (String) domain.getSource().getShop().getUrl(),
                domain.getSource().getShop().getName(),
                domain.getSource().getShop().getAvatar(),
                domain.getSource().getShop().getGold(),
                domain.getCreateTime(),
                domain.getSource().getShop().getOfficial()
        );
    }

    protected ArrayList<ProductFeedViewModel> convertToProductListViewModel(DataFeedDomain dataFeedDomain) {
        ArrayList<ProductFeedViewModel> listProduct = new ArrayList<>();
        for (ProductFeedDomain domain : dataFeedDomain.getContent().getProducts()) {
            listProduct.add(
                    new ProductFeedViewModel(
                            domain.getId(),
                            domain.getName(),
                            domain.getPrice(),
                            domain.getImage(),
                            domain.getImageSingle(),
                            (String) domain.getUrl(),
                            dataFeedDomain.getSource().getShop().getName(),
                            dataFeedDomain.getSource().getShop().getAvatar(),
                            domain.getWishlist()));
        }
        return listProduct;
    }

    private PromoCardViewModel convertToPromoViewModel(DataFeedDomain domain) {
        return new PromoCardViewModel(convertToPromoListViewModel(domain));
    }

    private ArrayList<PromoViewModel> convertToPromoListViewModel(DataFeedDomain dataFeedDomain) {
        ArrayList<PromoViewModel> listPromo = new ArrayList<>();
        for (PromotionFeedDomain domain : dataFeedDomain.getContent().getPromotions()) {
            listPromo.add(
                    new PromoViewModel(
                            domain.getDescription(),
                            domain.getPeriode(),
                            domain.getCode(),
                            domain.getThumbnail(),
                            domain.getUrl()));
        }
        if(dataFeedDomain.getContent().getPromotions().size()>1){
            listPromo.add(new PromoViewModel());
        }

        return listPromo;
    }


    protected String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getDataFeedDomainList().size() - 1;
        return feedResult.getDataFeedDomainList().get(lastIndex).getCursor();
    }
}
