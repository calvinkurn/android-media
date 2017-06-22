package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DataInspirationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.PromotionFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.InspirationRecommendationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationProductViewModel;

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
                ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(FeedResult feedResult) {
        viewListener.onSuccessGetFeedFirstPage(
                convertToViewModel(feedResult.getFeedDomain()));

        if (feedResult.getFeedDomain().getListFeed().size() > 0)
            viewListener.updateCursor(getCurrentCursor(feedResult));
    }

    protected ArrayList<Visitable> convertToViewModel(FeedDomain feedDomain) {
        ArrayList<Visitable> listFeedView = new ArrayList<>();
        addFeedData(listFeedView, feedDomain.getListFeed());
        if (listFeedView.size() > 1 && !(listFeedView.get(0) instanceof PromoCardViewModel))
            addInspirationData(listFeedView, feedDomain.getListInspiration());
        return listFeedView;
    }

    private void addInspirationData(ArrayList<Visitable> listFeedView, List<DataInspirationDomain> listInspiration) {
        if (listInspiration != null) {
            for (DataInspirationDomain domain : listInspiration) {
                InspirationViewModel model = convertToInspirationViewModel(domain);
                if (model.getListProduct() != null && model.getListProduct().size() > 0)
                    listFeedView.add(model);
            }
        }
    }

    private void addFeedData(ArrayList<Visitable> listFeedView, List<DataFeedDomain> listFeedDomain) {
        if (listFeedDomain != null)
            for (DataFeedDomain domain : listFeedDomain) {
                switch (domain.getContent().getType()) {
                    case TYPE_NEW_PRODUCT:
                        ActivityCardViewModel model = convertToActivityViewModel(domain);
                        if (model.getListProduct() != null && model.getListProduct().size() > 0)
                            listFeedView.add(model);
                        break;
                    case TYPE_PROMOTION:
                        PromoCardViewModel promo = convertToPromoViewModel(domain);
                        if (promo.getListPromo() != null && promo.getListPromo().size() > 0)
                            listFeedView.add(promo);
                        break;
                    default:
                        break;
                }
            }
    }

    private InspirationViewModel convertToInspirationViewModel(DataInspirationDomain domain) {
        return new InspirationViewModel(domain.getTitle(), convertToRecommendationListViewModel(domain));
    }

    private ArrayList<InspirationProductViewModel> convertToRecommendationListViewModel(DataInspirationDomain domains) {
        ArrayList<InspirationProductViewModel> listRecommendation = new ArrayList<>();
        if (domains.getRecommendation() != null && domains.getRecommendation().size() == 4)
            for (InspirationRecommendationDomain recommendationDomain : domains.getRecommendation()) {
                listRecommendation.add(convertToRecommendationViewModel(recommendationDomain));
            }
        return listRecommendation;
    }

    private InspirationProductViewModel convertToRecommendationViewModel(InspirationRecommendationDomain recommendationDomain) {
        return new InspirationProductViewModel(recommendationDomain.getId(),
                recommendationDomain.getName(),
                recommendationDomain.getPrice(),
                recommendationDomain.getImage_url(),
                recommendationDomain.getUrl());
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
        addSeeMorePromo(dataFeedDomain, listPromo);

        return listPromo;
    }

    private void addSeeMorePromo(DataFeedDomain dataFeedDomain, ArrayList<PromoViewModel> listPromo) {
        if (dataFeedDomain.getContent().getPromotions().size() > 1) {
            listPromo.add(new PromoViewModel());
        }
    }


    protected String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getFeedDomain().getListFeed().size() - 1;
        return feedResult.getFeedDomain().getListFeed().get(lastIndex).getCursor();
    }
}
