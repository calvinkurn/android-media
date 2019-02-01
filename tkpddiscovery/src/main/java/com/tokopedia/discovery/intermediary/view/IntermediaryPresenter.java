package com.tokopedia.discovery.intermediary.view;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.domain.interactor.GetCategoryHeaderUseCase;
import com.tokopedia.discovery.intermediary.domain.interactor.GetIntermediaryCategoryUseCase;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryPresenter extends BaseDaggerPresenter<IntermediaryContract.View>
        implements IntermediaryContract.Presenter {

    private final GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase;
    private final GetCategoryHeaderUseCase getCategoryHeaderUseCase;
    private final AddWishListUseCase addWishlistActionUseCase;
    private final RemoveWishListUseCase removeWishlistActionUseCase;
    private WishListActionListener wishlistActionListener;

    public IntermediaryPresenter(GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase,
                                 GetCategoryHeaderUseCase getCategoryHeaderUseCase,
                                 AddWishListUseCase addWishlistActionUseCase,
                                 RemoveWishListUseCase removeWishlistActionUseCase) {
        this.getIntermediaryCategoryUseCase = getIntermediaryCategoryUseCase;
        this.getCategoryHeaderUseCase = getCategoryHeaderUseCase;
        this.addWishlistActionUseCase = addWishlistActionUseCase;
        this.removeWishlistActionUseCase = removeWishlistActionUseCase;
    }

    @Override
    public void getIntermediaryCategory(String categoryId) {
        getIntermediaryCategoryUseCase.setCategoryId(categoryId);
        getCategoryHeaderUseCase.setCategoryId(categoryId);
        getView().showLoading();
       /* getIntermediaryCategoryUseCase.execute( RequestParams.EMPTY,
                new IntermediarySubscirber());*/
        getCategoryHeaderUseCase.execute(RequestParams.EMPTY, new CategoryHeaderSubscirber());
    }

    @Override
    public void addFavoriteShop(String categoryId) {

    }

    @Override
    public void addWishLish(int position, Data data) {
        if (getView().isUserHasLogin()) {
            if (data.getProduct().isWishlist()) {
                removeWishlist(data.getProduct().getId(), getView().getUserId());
            } else {
                addWishlist(data.getProduct().getId(), getView().getUserId());
            }
        } else {
            launchLoginActivity(data.getProduct().getId());
        }
    }

    @Override
    public void setWishlishListener(WishListActionListener wishListActionListener) {
        this.wishlistActionListener = wishListActionListener;
    }

    private void addWishlist(String productId, String userId) {
        Log.d(this.toString(), "Add Wishlist " + productId);
        addWishlistActionUseCase.createObservable(productId, userId, wishlistActionListener);
    }

    private void removeWishlist(String productId, String userId) {
        Log.d(this.toString(), "Remove Wishlist " + productId);
        removeWishlistActionUseCase.createObservable(productId, userId, wishlistActionListener);
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);
        getView().launchLoginActivity(extras);
    }

    private class CategoryHeaderSubscirber extends DefaultSubscriber<Response<CategoryHadesModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().skipIntermediaryPage();
            getView().hideLoading();
            e.printStackTrace();
        }

        @Override
        public void onNext(Response<CategoryHadesModel> categoryHadesModelResponse) {
            if (categoryHadesModelResponse.body().getData() != null &&
                    categoryHadesModelResponse.body().getData().getIntermediary() &&
                    categoryHadesModelResponse.body().getData().getTemplate().equals(IntermediaryCategoryDomainModel.LIFESTYLE_TEMPLATE)) {
                getIntermediaryCategoryUseCase.setCategoryId(categoryHadesModelResponse.body().getData().getId());
                getIntermediaryCategoryUseCase.setCategoryHadesModel(categoryHadesModelResponse.body());
                getIntermediaryCategoryUseCase.execute(RequestParams.EMPTY, new IntermediarySubscirber(categoryHadesModelResponse.body()));
            } else {
                getView().skipIntermediaryPage(categoryHadesModelResponse.body());
            }
        }

    }

    private class IntermediarySubscirber extends DefaultSubscriber<IntermediaryCategoryDomainModel> {

        private final CategoryHadesModel categoryHadesModel;

        private IntermediarySubscirber(CategoryHadesModel categoryHadesModel) {
            this.categoryHadesModel = categoryHadesModel;
        }

        @Override
        public void onCompleted() {
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            getView().skipIntermediaryPage();
            e.printStackTrace();
        }

        @Override
        public void onNext(IntermediaryCategoryDomainModel domainModel) {
            if (isViewAttached()) {
                getView().renderTopAds();
                getView().renderCuratedProducts(mappingDataLayer(domainModel));
                if (domainModel.getHotListModelList().size() > 0) {
                    getView().renderHotList(domainModel.getHotListModelList());
                }

                getView().updateDepartementId(domainModel.getDepartementId());
                if (domainModel.getVideoModel() != null && domainModel.getVideoModel().getVideoUrl() != null) {
                    getView().renderVideo(domainModel.getVideoModel());
                }
                if (domainModel.getBrandModelList() != null && domainModel.getBrandModelList().size() > 0) {
                    getView().renderBrands(domainModel.getBrandModelList());
                }
                getView().renderCategoryChildren(domainModel.getChildCategoryModelList());
                getView().renderHeader(domainModel.getHeaderModel());
                if (domainModel.getBannerModelList().size() > 0) {
                    getView().renderBanner(domainModel.getBannerModelList());
                }
                getView().backToTop();
                getView().stopFirebaseTrace();
            }
        }

        private List<CuratedSectionModel> mappingDataLayer(IntermediaryCategoryDomainModel domain) {
            int page;
            for (int i = 0; i < domain.getCuratedSectionModelList().size(); i++) {
                page = +1;
                CuratedSectionModel model = domain.getCuratedSectionModelList().get(i);
                model.setProducts(mappingProduct(
                        model.getProducts(),
                        String.format(Locale.getDefault(), "/intermediary/%s - product %d - %s", domain.getHeaderModel().getCategoryName().toLowerCase(), page, model.getTitle()),
                        getView().getTrackerAttribution())
                );
                domain.getCuratedSectionModelList().set(i, model);
                List<Object> productDataLayers = new ArrayList<>();

                for (ProductModel productModel : model.getProducts()) {
                    productDataLayers.add(productModel.generateImpressionDataLayer());
                }
                getView().trackEventEnhance(
                        DataLayer.mapOf(
                                "event", "productView",
                                "eventCategory", "intermediary page",
                                "eventAction", "product curation impression",
                                "eventLabel", "",
                                "ecommerce", DataLayer.mapOf(
                                        "currencyCode", "IDR",
                                        "impressions", DataLayer.listOf(
                                                productDataLayers.toArray(new Object[productDataLayers.size()])

                                        ))
                        )
                );
            }
            return domain.getCuratedSectionModelList();
        }

        private List<ProductModel> mappingProduct(List<ProductModel> products,
                                                  String trackerListName,
                                                  String trackerAttribution) {
            int position = 1;
            for (int i = 0; i < products.size(); i++) {
                position++;
                ProductModel model = products.get(i);
                model.setTrackerListName(trackerListName);
                model.setTrackerAttribution(trackerAttribution);
                model.setTrackerPosition(position);
                products.set(i, model);
            }
            return products;
        }

    }
}
