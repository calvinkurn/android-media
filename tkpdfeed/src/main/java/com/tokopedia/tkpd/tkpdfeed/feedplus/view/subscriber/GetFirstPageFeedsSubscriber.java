package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.TopPicksDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.PromotionFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.DataInspirationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.InspirationRecommendationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.BadgeDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.LabelDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewBadgeDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.LabelsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductCardHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.BadgeViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksItemViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/29/17.
 */

public class GetFirstPageFeedsSubscriber extends Subscriber<FeedResult> {

    private static final String FREE_RETURN = "Free Return";
    protected final FeedPlus.View viewListener;
    private static final String TYPE_OS_BRANDS = "official_store_brand";
    private static final String TYPE_OS_CAMPAIGN = "official_store_campaign";

    private static final String TYPE_NEW_PRODUCT = "new_product";
    private static final String TYPE_PROMOTION = "promotion";
    private static final String TYPE_TOPPICKS = "toppick";
    private final int page;


    public GetFirstPageFeedsSubscriber(FeedPlus.View viewListener, int page) {
        this.viewListener = viewListener;
        this.page = page;
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

        if (feedResult.getDataSource() == FeedResult.SOURCE_CLOUD)
            viewListener.clearData();

        FeedDomain feedDomain = feedResult.getFeedDomain();
        ArrayList<Visitable> listFeedView = new ArrayList<>();

        if (hasRecentView(feedDomain) && hasFeed(feedDomain)) {
            addRecentViewData(listFeedView, feedDomain.getRecentProduct());
            addMainData(listFeedView, feedDomain, feedResult);
        } else if (!hasRecentView(feedDomain) && hasFeed(feedDomain)) {
            addMainData(listFeedView, feedDomain, feedResult);
        } else if (hasRecentView(feedDomain) && !hasFeed(feedDomain)) {
            addRecentViewData(listFeedView, feedDomain.getRecentProduct());
            viewListener.onShowEmptyWithRecentView(listFeedView,
                    checkCanShowTopads(feedResult.getDataSource()));
        } else
            viewListener.onShowEmpty(checkCanShowTopads(feedResult.getDataSource()));


        if (hasFeed(feedDomain)) {
            viewListener.updateCursor(getCurrentCursor(feedResult));
            viewListener.setFirstCursor(feedDomain.getListFeed().get(0).getCursor());
        }

        if (feedResult.getDataSource() == FeedResult.SOURCE_CLOUD)
            viewListener.finishLoading();

    }

    private void addMainData(ArrayList<Visitable> listFeedView,
                             FeedDomain feedDomain, FeedResult feedResult) {
        addFeedData(listFeedView, feedDomain.getListFeed());
        addInspirationData(listFeedView, feedDomain.getListInspiration());
        checkCanLoadNext(feedResult, listFeedView);
    }

    private boolean checkCanShowTopads(int dataSource) {
        return dataSource == FeedResult.SOURCE_CLOUD;
    }

    private void checkCanLoadNext(FeedResult feedResult, ArrayList<Visitable> listFeedView) {

        if (hasFeed(feedResult.getFeedDomain())
                && !feedResult.isHasNext()
                && feedResult.getDataSource() == FeedResult.SOURCE_CLOUD) {
            viewListener.onSuccessGetFeedFirstPageWithAddFeed(listFeedView);
        } else {
            viewListener.showTopAds(checkCanShowTopads(feedResult.getDataSource()));
            viewListener.onSuccessGetFeedFirstPage(listFeedView);
        }
    }

    protected ArrayList<Visitable> convertToViewModel(FeedDomain feedDomain) {
        ArrayList<Visitable> listFeedView = new ArrayList<>();
        addFeedData(listFeedView, feedDomain.getListFeed());
        if (listFeedView.size() > 1 && !(listFeedView.get(0) instanceof PromoCardViewModel))
            addInspirationData(listFeedView, feedDomain.getListInspiration());
        return listFeedView;
    }

    private boolean hasFeed(FeedDomain feedDomain) {
        return feedDomain.getListFeed() != null
                && !feedDomain.getListFeed().isEmpty()
                && feedDomain.getListFeed().get(0) != null
                && feedDomain.getListFeed().get(0).getContent() != null
                && feedDomain.getListFeed().get(0).getContent().getType() != null;
    }

    private boolean hasRecentView(FeedDomain feedDomain) {
        return feedDomain.getRecentProduct() != null && !feedDomain.getRecentProduct().isEmpty();
    }

    private void addRecentViewData(ArrayList<Visitable> listFeedView,
                                   List<RecentViewProductDomain> recentProduct) {
        listFeedView.add(new RecentViewViewModel(convertToRecentViewModelItem(recentProduct)));
    }

    private ArrayList<RecentViewProductViewModel> convertToRecentViewModelItem(
            List<RecentViewProductDomain> domains) {
        ArrayList<RecentViewProductViewModel> listProduct = new ArrayList<>();
        for (RecentViewProductDomain domain : domains) {
            RecentViewProductViewModel model = convertToRecentProductViewModel(domain);
            listProduct.add(model);
        }
        return listProduct;
    }

    private RecentViewProductViewModel convertToRecentProductViewModel(
            RecentViewProductDomain domain) {
        return new RecentViewProductViewModel(domain.getId(),
                domain.getName(),
                domain.getPrice(),
                domain.getShop().getName(),
                domain.getImgUri(),
                Integer.parseInt(domain.getShop().getId()),
                domain.getPreorder(),
                domain.getWholesale(),
                convertToBadgeViewModel(domain.getBadges()),
                domain.getFreeReturn(),
                domain.getWishlist(),
                domain.getIsGold()
        );
    }

    private List<BadgeViewModel> convertToBadgeViewModel(List<RecentViewBadgeDomain> badges) {
        List<BadgeViewModel> badgeList = new ArrayList<>();
        for (RecentViewBadgeDomain badgeResponse : badges) {
            badgeList.add(new BadgeViewModel(badgeResponse.getTitle(), badgeResponse.getImageUrl()));
        }
        return badgeList;
    }

    private void addInspirationData(ArrayList<Visitable> listFeedView,
                                    List<DataInspirationDomain> listInspiration) {
        for (DataInspirationDomain domain : listInspiration) {
            InspirationViewModel model = convertToInspirationViewModel(domain);
            if (model.getListProduct() != null && model.getListProduct().size() > 0)
                listFeedView.add(model);
        }
    }

    private void addFeedData(ArrayList<Visitable> listFeedView,
                             List<DataFeedDomain> listFeedDomain) {
        if (listFeedDomain != null)
            for (DataFeedDomain domain : listFeedDomain) {
                switch (domain.getContent().getType() != null ? domain.getContent().getType() : "") {
                    case TYPE_OS_CAMPAIGN:
                        if (domain.getContent().getOfficialStores() != null
                                && !domain.getContent().getOfficialStores().isEmpty()) {
                            OfficialStoreCampaignViewModel campaign =
                                    convertToOfficialStoreCampaign(domain);
                            listFeedView.add(campaign);
                        }
                        break;
                    case TYPE_OS_BRANDS:
                        if (domain.getContent().getOfficialStores() != null
                                && !domain.getContent().getOfficialStores().isEmpty()) {
                            OfficialStoreBrandsViewModel officialStore =
                                    convertToBrandsViewModel(domain);
                            if (!officialStore.getListStore().isEmpty())
                                listFeedView.add(officialStore);
                        }
                        break;
                    case TYPE_NEW_PRODUCT:
                        ActivityCardViewModel model = convertToActivityViewModel(domain);
                        if (model.getListProduct() != null && !model.getListProduct().isEmpty())
                            listFeedView.add(model);
                        break;
                    case TYPE_PROMOTION:
                        PromoCardViewModel promo = convertToPromoViewModel(domain);
                        if (promo.getListPromo() != null && !promo.getListPromo().isEmpty())
                            listFeedView.add(promo);
                        break;
                    case TYPE_TOPPICKS:
                        ToppicksViewModel toppicks = convertToToppicksViewModel(domain);
                        if (toppicks.getList() != null && !toppicks.getList().isEmpty())
                            listFeedView.add(toppicks);
                    default:
                        break;
                }
            }
    }

    private ToppicksViewModel convertToToppicksViewModel(DataFeedDomain domain) {
        return new ToppicksViewModel(convertToListTopPicks(domain), page);
    }

    private ArrayList<ToppicksItemViewModel> convertToListTopPicks(DataFeedDomain domain) {
        ArrayList<ToppicksItemViewModel> list = new ArrayList<>();
        if (domain != null
                && domain.getContent() != null
                && domain.getContent().getTopPicksDomains() != null
                && !domain.getContent().getTopPicksDomains().isEmpty())
            for (TopPicksDomain topPicksDomain : domain.getContent().getTopPicksDomains()) {
                list.add(convertToToppicksProduct(topPicksDomain));
            }
        return list;
    }

    private ToppicksItemViewModel convertToToppicksProduct(TopPicksDomain topPicksDomain) {
        return new ToppicksItemViewModel(
                topPicksDomain.getName(),
                topPicksDomain.getImageUrl(),
                topPicksDomain.getUrl());
    }

    private OfficialStoreCampaignViewModel convertToOfficialStoreCampaign(DataFeedDomain domain) {
        return new OfficialStoreCampaignViewModel(
                domain.getContent().getOfficialStores().get(0).getMobile_img_url(),
                domain.getContent().getOfficialStores().get(0).getRedirect_url_app(),
                domain.getContent().getOfficialStores().get(0).getFeed_hexa_color(),
                domain.getContent().getOfficialStores().get(0).getTitle(),
                convertToOfficialStoreProducts(domain.getContent().getOfficialStores().get(0)),
                page
        );
    }

    private ArrayList<OfficialStoreCampaignProductViewModel>
    convertToOfficialStoreProducts(OfficialStoreDomain domain) {
        ArrayList<OfficialStoreCampaignProductViewModel> listStore = new ArrayList<>();
        if (domain.getProducts() != null)
            for (OfficialStoreProductDomain productDomain : domain.getProducts()) {
                listStore.add(convertToOfficialStoreProduct(productDomain));
            }
        return listStore;
    }

    private OfficialStoreCampaignProductViewModel
    convertToOfficialStoreProduct(OfficialStoreProductDomain productDomain) {
        return new OfficialStoreCampaignProductViewModel(
                productDomain.getData().getId(),
                productDomain.getData().getName(),
                productDomain.getData().getPrice(),
                productDomain.getData().getOriginal_price(),
                productDomain.getData().getDiscount_percentage(),
                productDomain.getData().getImage_url(),
                productDomain.getData().getImage_url_700(),
                productDomain.getData().getUrl_app(),
                productDomain.getData().getShop().getName(),
                productDomain.getBrand_logo(),
                productDomain.getData().getShop().getUrl_app(),
                convertLabels(productDomain.getData().getLabels()),
                isFreeReturn(productDomain.getData().getBadges()));
    }

    private boolean isFreeReturn(List<BadgeDomain> badges) {
        for (BadgeDomain domain : badges) {
            if (domain.getTitle().equals(FREE_RETURN))
                return true;
        }
        return false;
    }

    private List<LabelsViewModel> convertLabels(List<LabelDomain> labels) {
        List<LabelsViewModel> labelsViewModels = new ArrayList<>();
        for (LabelDomain labelDomain : labels) {
            labelsViewModels.add(new LabelsViewModel(labelDomain.getTitle(),
                    labelDomain.getColor()));
        }
        return labelsViewModels;
    }

    private OfficialStoreBrandsViewModel convertToBrandsViewModel(DataFeedDomain domain) {
        return new OfficialStoreBrandsViewModel(
                convertToListBrands(
                        domain.getContent().getOfficialStores()),
                page
        );
    }

    private ArrayList<OfficialStoreViewModel> convertToListBrands(
            List<OfficialStoreDomain> officialStores) {
        ArrayList<OfficialStoreViewModel> listStore = new ArrayList<>();
        if (officialStores != null)
            for (OfficialStoreDomain officialStoreDomain : officialStores) {
                listStore.add(convertToOfficialStore(officialStoreDomain));
            }
        return listStore;
    }

    private OfficialStoreViewModel convertToOfficialStore(OfficialStoreDomain officialStoreDomain) {
        return new OfficialStoreViewModel(
                officialStoreDomain.getShop_id(),
                officialStoreDomain.getShop_apps_url(),
                officialStoreDomain.getShop_name(),
                officialStoreDomain.getMicrosite_url(),
                officialStoreDomain.getIs_new()
        );
    }

    private InspirationViewModel convertToInspirationViewModel(
            DataInspirationDomain domain) {
        return new InspirationViewModel(domain.getTitle(),
                convertToRecommendationListViewModel(domain));
    }

    private ArrayList<InspirationProductViewModel> convertToRecommendationListViewModel(
            DataInspirationDomain domains) {
        ArrayList<InspirationProductViewModel> listRecommendation = new ArrayList<>();
        if (domains.getRecommendation() != null && domains.getRecommendation().size() == 4)
            for (InspirationRecommendationDomain recommendationDomain : domains.getRecommendation()) {
                listRecommendation.add(convertToRecommendationViewModel(recommendationDomain));
            }
        return listRecommendation;
    }

    private InspirationProductViewModel convertToRecommendationViewModel(
            InspirationRecommendationDomain recommendationDomain) {
        return new InspirationProductViewModel(recommendationDomain.getId(),
                recommendationDomain.getName(),
                recommendationDomain.getPrice(),
                recommendationDomain.getImage_url(),
                recommendationDomain.getUrl(),
                page);
    }

    protected ActivityCardViewModel convertToActivityViewModel(DataFeedDomain domain) {
        return new ActivityCardViewModel(
                convertToProductCardHeaderViewModel(domain),
                convertToProductListViewModel(domain),
                domain.getSource().getShop().getShareLinkURL(),
                domain.getSource().getShop().getShareLinkDescription(),
                domain.getContent().getStatusActivity(),
                domain.getId(),
                domain.getContent().getTotalProduct(),
                domain.getCursor(),
                page);
    }

    protected ProductCardHeaderViewModel convertToProductCardHeaderViewModel(DataFeedDomain domain) {
        return new ProductCardHeaderViewModel(
                domain.getSource().getShop().getId(),
                domain.getSource().getShop().getUrl(),
                domain.getSource().getShop().getName(),
                domain.getSource().getShop().getAvatar(),
                domain.getSource().getShop().getGold(),
                domain.getCreateTime(),
                domain.getSource().getShop().getOfficial()
        );
    }

    protected ArrayList<ProductFeedViewModel> convertToProductListViewModel(
            DataFeedDomain dataFeedDomain) {
        ArrayList<ProductFeedViewModel> listProduct = new ArrayList<>();
        for (ProductFeedDomain domain : dataFeedDomain.getContent().getProducts()) {
            listProduct.add(
                    new ProductFeedViewModel(
                            domain.getId(),
                            domain.getName(),
                            domain.getPrice(),
                            domain.getImage(),
                            domain.getImageSingle(),
                            domain.getUrl(),
                            dataFeedDomain.getSource().getShop().getName(),
                            dataFeedDomain.getSource().getShop().getAvatar(),
                            domain.getWishlist(),
                            page));
        }
        return listProduct;
    }

    private PromoCardViewModel convertToPromoViewModel(DataFeedDomain domain) {
        return new PromoCardViewModel(convertToPromoListViewModel(domain), page);
    }

    private ArrayList<PromoViewModel> convertToPromoListViewModel(DataFeedDomain dataFeedDomain) {
        ArrayList<PromoViewModel> listPromo = new ArrayList<>();
        for (PromotionFeedDomain domain : dataFeedDomain.getContent().getPromotions()) {
            listPromo.add(
                    new PromoViewModel(
                            domain.getId(),
                            domain.getDescription(),
                            domain.getPeriode(),
                            domain.getCode(),
                            domain.getThumbnail(),
                            domain.getUrl(),
                            domain.getName(),
                            page));
        }
        addSeeMorePromo(dataFeedDomain, listPromo);

        return listPromo;
    }

    private void addSeeMorePromo(DataFeedDomain dataFeedDomain, ArrayList<PromoViewModel> listPromo) {
        if (dataFeedDomain.getContent().getPromotions().size() > 1) {
            listPromo.add(new PromoViewModel(page));
        }
    }


    protected String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getFeedDomain().getListFeed().size() - 1;
        return feedResult.getFeedDomain().getListFeed().get(lastIndex).getCursor();
    }
}
