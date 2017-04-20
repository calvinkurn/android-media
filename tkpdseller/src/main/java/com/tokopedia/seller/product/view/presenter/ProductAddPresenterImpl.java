package com.tokopedia.seller.product.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl extends ProductAddPresenter {
    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final ProductScoringUseCase productScoringUseCase;
    private final AddProductUseCase addProductUseCase;
    private final FetchCatalogDataUseCase fetchCatalogDataUseCase;
    private final GetCategoryRecommUseCase getCategoryRecommUseCase;

    private QueryListener getCategoryRecomListener;
    private Subscription subscriptionCategoryRecomm;

    public static final int TIME_DELAY = 700;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                   AddProductUseCase addProductUseCase,
                                   FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                   GetCategoryRecommUseCase getCategoryRecommUseCase,
                                   ProductScoringUseCase productScoringUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.addProductUseCase = addProductUseCase;
        this.fetchCatalogDataUseCase = fetchCatalogDataUseCase;
        this.getCategoryRecommUseCase = getCategoryRecommUseCase;
        this.productScoringUseCase = productScoringUseCase;
        createCategoryRecommSubscriber();
    }

    private void createCategoryRecommSubscriber(){
        subscriptionCategoryRecomm = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                getCategoryRecomListener = new QueryListener() {
                    @Override
                    public void getQueryString(String string) {
                        subscriber.onNext(string);
                    }
                };
            }
        }).debounce(TIME_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceGetCategoryRecomm());
    }

    @NonNull
    private Subscriber<String> getSubscriberDebounceGetCategoryRecomm() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String string) {
                if (!string.equals("")) {
                    getCategoryRecommendationFromServer(string, 3);
                }
            }
        };
    }

    @Override
    public void fetchCatalogData(String keyword, int departmentId, int start, int rows) {
        fetchCatalogDataUseCase.execute(
                FetchCatalogDataUseCase.createRequestParams(keyword, departmentId, start, rows),
                new Subscriber<CatalogDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showCatalogError(e);
                    }

                    @Override
                    public void onNext(CatalogDataModel catalogDataModel) {
                        getView().successFetchCatalogData(
                                catalogDataModel.getResult().getCatalogs(),
                                catalogDataModel.getResult().getTotalRecord());
                    }
                });
    }

    @Override
    public void getCategoryRecommendation(String productTitle) {
        if (getCategoryRecomListener != null) {
            getCategoryRecomListener.getQueryString(productTitle);
        }

    }

    private void getCategoryRecommendationFromServer(String productTitle, int expectRow) {
        getCategoryRecommUseCase.execute(
                GetCategoryRecommUseCase.createRequestParams(productTitle, expectRow),
                new Subscriber<CategoryRecommDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showCatRecommError(e);
                    }

                    @Override
                    public void onNext(CategoryRecommDataModel categoryRecommDataModel) {
                        getView().successGetCategoryRecommData(
                                categoryRecommDataModel.getData().get(0).getProductCategoryPrediction()
                        );
                    }
                });
    }

    @Override
    public void saveDraft(UploadProductInputViewModel viewModel) {
        UploadProductInputDomainModel domainModel = UploadProductMapper.map(viewModel);
        RequestParams requestParam = SaveDraftProductUseCase.generateUploadProductParam(domainModel);
        saveDraftProductUseCase.execute(requestParam, new SaveDraftSubscriber());
    }

    @Override
    public void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        RequestParams requestParams = ProductScoringUseCase.createRequestParams(valueIndicatorScoreModel);
        productScoringUseCase.execute(requestParams, getSubscriberProductScoring());
    }

    public Subscriber<DataScoringProductView> getSubscriberProductScoring() {
        return new Subscriber<DataScoringProductView>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                checkViewAttached();
            }

            @Override
            public void onNext(DataScoringProductView dataScoringProductView) {
                checkViewAttached();
                if(dataScoringProductView != null) {
                    getView().onSuccessGetScoringProduct(dataScoringProductView);
                }
            }
        };
    }

    private class SaveDraftSubscriber extends Subscriber<Long> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(Long productId) {
            RequestParams requestParam = AddProductUseCase.generateUploadProductParam(productId);
            addProductUseCase.execute(requestParam, new AddProductSubscriber());
        }
    }

    private class AddProductSubscriber extends Subscriber<AddProductDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(AddProductDomainModel addProductDomainModel) {
            checkViewAttached();
        }
    }

    private interface QueryListener {
        void getQueryString(String string);
    }

    public void detachView(){
        super.detachView();
        addProductUseCase.unsubscribe();
        saveDraftProductUseCase.unsubscribe();
        subscriptionCategoryRecomm.unsubscribe();
    }
}
