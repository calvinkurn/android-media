package com.tokopedia.seller.goldmerchant.featured.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.featured.domain.interactor.FeaturedProductPOSTUseCase;
import com.tokopedia.seller.goldmerchant.featured.domain.interactor.FeaturedProductUseCase;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductDomainModel;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductPOSTDomainModel;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 9/7/17.
 */

public class FeaturedProductPresenterImpl extends FeaturedProductPresenter {

    private static final String TAG = "FeaturedProductPresente";
    private FeaturedProductPOSTUseCase featuredProductPOSTUseCase;
    private FeaturedProductUseCase featuredProductUseCase;

    @Inject
    public FeaturedProductPresenterImpl(
            FeaturedProductPOSTUseCase featuredProductPOSTUseCase,
            FeaturedProductUseCase featuredProductUseCase
    ) {
        this.featuredProductPOSTUseCase = featuredProductPOSTUseCase;
        this.featuredProductUseCase = featuredProductUseCase;
    }

    @Override
    public void loadData() {
        featuredProductUseCase.execute(RequestParams.EMPTY, new Subscriber<FeaturedProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }

            @Override
            public void onNext(FeaturedProductDomainModel featuredProductDomainModel) {
                revealGETData(featuredProductDomainModel);
            }
        });
    }

    private void revealGETData(FeaturedProductDomainModel featuredProductDomainModel) {
        if (isViewAttached()) {
            List<FeaturedProductModel> featuredProductModels = convertToViewModel(featuredProductDomainModel);
            getView().onSearchLoaded(
                    featuredProductModels,
                    featuredProductModels.size()
            );
        }
    }

    private List<FeaturedProductModel> convertToViewModel(FeaturedProductDomainModel featuredProductDomainModel) {

        List<FeaturedProductModel> featuredProductModels = new ArrayList<>();

        for (FeaturedProductDomainModel.Datum datum : featuredProductDomainModel.getData()) {
            FeaturedProductModel featuredProductModel = new FeaturedProductModel();
            featuredProductModel.setProductId(datum.getProductId());
            featuredProductModel.setImageUrl(datum.getImageUri());
            featuredProductModel.setProductName(datum.getName());
            featuredProductModel.setProductPrice(datum.getPrice());


            featuredProductModels.add(featuredProductModel);
        }

        return featuredProductModels;
    }

    @Override
    public void postData(RequestParams requestParams) {
        featuredProductPOSTUseCase.execute(requestParams, new Subscriber<FeaturedProductPOSTDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }

            @Override
            public void onNext(FeaturedProductPOSTDomainModel featuredProductPOSTDomainModel) {
                revealPOSTData(featuredProductPOSTDomainModel);
            }


        });
    }

    private void revealPOSTData(FeaturedProductPOSTDomainModel featuredProductPOSTDomainModel) {
        if (isViewAttached()) {
            if (featuredProductPOSTDomainModel.isData()) {
                getView().onPostSuccess();
            }
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        featuredProductPOSTUseCase.unsubscribe();
        featuredProductUseCase.unsubscribe();
    }
}
