package com.tokopedia.seller.product.edit.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.edit.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.seller.product.edit.domain.model.CategoryRecommDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.listener.ProductAddView;
import com.tokopedia.seller.product.edit.view.mapper.CategoryRecommDomainToViewMapper;
import com.tokopedia.seller.product.edit.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl<T extends ProductAddView> extends ProductAddPresenter<T> {
    public static final int TIME_DELAY = 500;

    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final ProductScoringUseCase productScoringUseCase;
    private final FetchCatalogDataUseCase fetchCatalogDataUseCase;
    private final GetCategoryRecommUseCase getCategoryRecommUseCase;
    private final AddProductShopInfoUseCase addProductShopInfoUseCase;
    private final FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase;
    private QueryListener getCategoryRecomListener;
    private Subscription subscriptionDebounceCategoryRecomm;
    private CatalogQueryListener getCatalogListener;
    private Subscription subscriptionDebounceCatalog;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                   FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                   GetCategoryRecommUseCase getCategoryRecommUseCase,
                                   ProductScoringUseCase productScoringUseCase,
                                   AddProductShopInfoUseCase addProductShopInfoUseCase,
                                   FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.fetchCatalogDataUseCase = fetchCatalogDataUseCase;
        this.getCategoryRecommUseCase = getCategoryRecommUseCase;
        this.productScoringUseCase = productScoringUseCase;
        this.addProductShopInfoUseCase = addProductShopInfoUseCase;
        this.fetchCategoryDisplayUseCase = fetchCategoryDisplayUseCase;
    }

    @Override
    public void fetchCategory(long categoryId) {
        RequestParams requestParam = FetchCategoryDisplayUseCase.generateParam(categoryId);
        fetchCategoryDisplayUseCase.execute(requestParam, new FetchCategoryDisplaySubscriber());
    }


    private class FetchCategoryDisplaySubscriber extends Subscriber<List<String>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            checkViewAttached();
        }

        @Override
        public void onNext(List<String> strings) {
            checkViewAttached();
            getView().populateCategory(strings);
        }
    }

    @Override
    public void saveDraftAndAdd(UploadProductInputViewModel viewModel, boolean isUploading) {
        RequestParams requestParam = generateRequestParamAddDraft(viewModel, isUploading);
        saveDraftProductUseCase.execute(requestParam, new SaveDraftAndAddSubscriber());
    }

    private RequestParams generateRequestParamAddDraft(UploadProductInputViewModel viewModel, boolean isUploading) {
        UploadProductInputDomainModel domainModel = UploadProductMapper.mapViewToDomain(viewModel);
        return SaveDraftProductUseCase.generateUploadProductParam(domainModel, getProductDraftId(), isUploading);
    }

    protected long getProductDraftId(){
        return getView().getProductDraftId();
    }

    @Override
    public void getShopInfo() {
        addProductShopInfoUseCase.execute(
                null,
                new Subscriber<AddProductShopInfoDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().onErrorLoadShopInfo(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(AddProductShopInfoDomainModel model) {
                        getView().onSuccessLoadShopInfo(model.isGoldMerchant(), model.isFreeReturn());
                    }
                });
    }

    @Override
    public void getCategoryRecommendation(String productTitle) {
        if (getCategoryRecomListener != null) {
            getCategoryRecomListener.getQueryString(productTitle);
        } else {
            createCategoryRecommSubscriber();
        }
    }

    private void createCategoryRecommSubscriber() {
        subscriptionDebounceCategoryRecomm = Observable.create(new Observable.OnSubscribe<String>() {
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
                if (!TextUtils.isEmpty(string)) {
                    getCategoryRecommendationFromServer(string, 3);
                }
            }
        };
    }

    @Override
    public void fetchCatalogData(String keyword, long departmentId, int start, int rows) {
        if (getCatalogListener != null) {
            getCatalogListener.getQuery(new CatalogQuery(keyword, departmentId, start, rows));
        } else {
            createCatalogSubscriber();
        }
    }

    private void createCatalogSubscriber() {
        subscriptionDebounceCatalog = Observable.create(new Observable.OnSubscribe<CatalogQuery>() {
            @Override
            public void call(final Subscriber<? super CatalogQuery> subscriber) {
                getCatalogListener = new CatalogQueryListener() {

                    @Override
                    public void getQuery(CatalogQuery catalogQuery) {
                        subscriber.onNext(catalogQuery);
                    }
                };
            }
        }).debounce(TIME_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberDebounceGetCatalog());
    }

    @NonNull
    private Subscriber<CatalogQuery> getSubscriberDebounceGetCatalog() {
        return new Subscriber<CatalogQuery>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(CatalogQuery catalogQuery) {
                getCatalogFromServer(catalogQuery.getKeyword(),
                        catalogQuery.getCategoryId(),
                        catalogQuery.getStart(),
                        catalogQuery.getRow());
            }
        };
    }

    private void getCatalogFromServer(String keyword, long departmentId, int start, int rows) {
        fetchCatalogDataUseCase.execute(
                FetchCatalogDataUseCase.createRequestParams(keyword, departmentId, start, rows),
                new Subscriber<CatalogDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().onErrorLoadCatalog(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(CatalogDataModel catalogDataModel) {
                        getView().onSuccessLoadCatalog(catalogDataModel.getResult().getCatalogs());
                    }
                });
    }

    private void getCategoryRecommendationFromServer(String productTitle, int expectRow) {
        //invalidate previous request
        getCategoryRecommUseCase.unsubscribe();
        getCategoryRecommUseCase.execute(
                GetCategoryRecommUseCase.createRequestParams(productTitle, expectRow),
                new Subscriber<CategoryRecommDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().onErrorLoadRecommendationCategory(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(CategoryRecommDomainModel categoryRecommDomainModel) {
                        getView().onSuccessLoadRecommendationCategory(
                                CategoryRecommDomainToViewMapper.mapDomainView(categoryRecommDomainModel)
                                        .getProductCategoryPrediction() );
                    }
                });
    }

    @Override
    public void saveDraft(UploadProductInputViewModel viewModel, boolean isUploading) {
        RequestParams requestParam = generateRequestParamAddDraft(viewModel, isUploading);
        saveDraftProductUseCase.execute(requestParam, new SaveDraftSubscriber(isUploading));
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
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorLoadScoringProduct(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(DataScoringProductView dataScoringProductView) {
                checkViewAttached();
                if (dataScoringProductView != null) {
                    getView().onSuccessLoadScoringProduct(dataScoringProductView);
                }
            }
        };
    }

    private interface QueryListener {
        void getQueryString(String string);
    }

    private interface CatalogQueryListener {
        void getQuery(CatalogQuery catalogQuery);
    }

    public static class CatalogQuery {
        String keyword;
        long categoryId;
        int start;
        int row;

        public CatalogQuery(String keyword, long categoryId, int start, int row) {
            this.keyword = keyword;
            this.categoryId = categoryId;
            this.start = start;
            this.row = row;
        }

        public long getCategoryId() {
            return categoryId;
        }

        public int getRow() {
            return row;
        }

        public int getStart() {
            return start;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    private class SaveDraftSubscriber extends Subscriber<Long> {

        boolean isUploading;
        SaveDraftSubscriber(boolean isUploading) {
            this.isUploading = isUploading;
        }
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            if (isUploading) {
                getView().onErrorStoreProductToDraftWhenUpload(ViewUtils.getErrorMessage(e));
            } else {
                getView().onErrorStoreProductToDraftWhenBackPressed(ViewUtils.getErrorMessage(e));
            }
        }

        @Override
        public void onNext(Long productId) {
            checkViewAttached();
            getView().onSuccessStoreProductToDraft(productId, isUploading);
        }
    }

    public void detachView() {
        super.detachView();
        saveDraftProductUseCase.unsubscribe();
        fetchCatalogDataUseCase.unsubscribe();
        getCategoryRecommUseCase.unsubscribe();
        addProductShopInfoUseCase.unsubscribe();
        fetchCategoryDisplayUseCase.unsubscribe();
        productScoringUseCase.unsubscribe();

        if (subscriptionDebounceCategoryRecomm != null) {
            subscriptionDebounceCategoryRecomm.unsubscribe();
        }
        if (subscriptionDebounceCatalog != null) {
            subscriptionDebounceCatalog.unsubscribe();
        }
    }


    private class SaveDraftAndAddSubscriber extends Subscriber<Long> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            getView().onErrorStoreProductAndAddToDraft(ViewUtils.getErrorMessage(e));
        }

        @Override
        public void onNext(Long productId) {
            checkViewAttached();
            getView().onSuccessStoreProductAndAddToDraft(productId);
        }
    }
}
