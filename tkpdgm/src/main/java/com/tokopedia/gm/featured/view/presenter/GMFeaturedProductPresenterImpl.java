package com.tokopedia.gm.featured.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductGetListUseCase;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductSubmitUseCase;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.gm.featured.domain.model.GMFeaturedProductSubmitDomainModel;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 9/7/17.
 */

public class GMFeaturedProductPresenterImpl extends GMFeaturedProductPresenter {

    private static final String TAG = "FeaturedProductPresente";
    private GMFeaturedProductSubmitUseCase gmFeaturedProductSubmitUseCase;
    private GMFeaturedProductGetListUseCase gmFeaturedProductGetListUseCase;

    @Inject
    public GMFeaturedProductPresenterImpl(
            GMFeaturedProductSubmitUseCase gmFeaturedProductSubmitUseCase,
            GMFeaturedProductGetListUseCase gmFeaturedProductGetListUseCase) {
        this.gmFeaturedProductSubmitUseCase = gmFeaturedProductSubmitUseCase;
        this.gmFeaturedProductGetListUseCase = gmFeaturedProductGetListUseCase;
    }

    @Override
    public void loadData() {
        gmFeaturedProductGetListUseCase.execute(RequestParams.EMPTY, new Subscriber<GMFeaturedProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onLoadSearchError(e);
                }
            }

            @Override
            public void onNext(GMFeaturedProductDomainModel gmFeaturedProductDomainModel) {
                List<GMFeaturedProductModel> GMFeaturedProductModels = convertToViewModel(gmFeaturedProductDomainModel);
                getView().onSearchLoaded(GMFeaturedProductModels, GMFeaturedProductModels.size());
            }

            private List<GMFeaturedProductModel> convertToViewModel(GMFeaturedProductDomainModel GMFeaturedProductDomainModel) {
                List<GMFeaturedProductModel> gmFeaturedProductModelList = new ArrayList<>();
                for (GMFeaturedProductDomainModel.Datum datum : GMFeaturedProductDomainModel.getData()) {
                    GMFeaturedProductModel gmFeaturedProductModel = new GMFeaturedProductModel();
                    gmFeaturedProductModel.setProductId(datum.getProductId());
                    gmFeaturedProductModel.setImageUrl(datum.getImageUri());
                    gmFeaturedProductModel.setProductName(datum.getName());
                    gmFeaturedProductModel.setProductPrice(datum.getPrice());
                    gmFeaturedProductModelList.add(gmFeaturedProductModel);
                }
                return gmFeaturedProductModelList;
            }
        });
    }

    @Override
    public void postData(RequestParams requestParams) {
        gmFeaturedProductSubmitUseCase.execute(requestParams, new Subscriber<GMFeaturedProductSubmitDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if (isViewAttached()) {
                    getView().onSubmitError(t);
                }
            }

            @Override
            public void onNext(GMFeaturedProductSubmitDomainModel gmFeaturedProductSubmitDomainModel) {
                if (gmFeaturedProductSubmitDomainModel.isData()) {
                    getView().onSubmitSuccess();
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        gmFeaturedProductSubmitUseCase.unsubscribe();
        gmFeaturedProductGetListUseCase.unsubscribe();
    }
}