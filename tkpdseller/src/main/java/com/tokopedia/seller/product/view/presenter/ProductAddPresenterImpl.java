package com.tokopedia.seller.product.view.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.ShopInfoUseCase;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import java.util.List;
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
    private final ShopInfoUseCase shopInfoUseCase;

    private QueryListener getCategoryRecomListener;
    private Subscription subscriptionDebounceCategoryRecomm;
    private CatalogQueryListener getCatalogListener;
    private Subscription subscriptionDebounceCatalog;

    public static final int TIME_DELAY = 500;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                   AddProductUseCase addProductUseCase,
                                   FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                   GetCategoryRecommUseCase getCategoryRecommUseCase,
                                   ProductScoringUseCase productScoringUseCase,
                                   ShopInfoUseCase shopInfoUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.addProductUseCase = addProductUseCase;
        this.fetchCatalogDataUseCase = fetchCatalogDataUseCase;
        this.getCategoryRecommUseCase = getCategoryRecommUseCase;
        this.productScoringUseCase = productScoringUseCase;
        this.shopInfoUseCase = shopInfoUseCase;
        createCategoryRecommSubscriber();
        createCatalogSubscriber();
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

    public void getCatalogFromServer(String keyword, int departmentId, int start, int rows) {
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

    @Override
    public void getShopInfo(String userId, String deviceId, String shopId) {
        shopInfoUseCase.execute(
                ShopInfoUseCase.createRequestParamByShopId(userId, deviceId, shopId),
                new Subscriber<ShopModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showErrorGetShopInfo(e);
                    }

                    @Override
                    public void onNext(ShopModel shopModel) {
                        boolean isGoldMerchant = shopModel.info.shopIsGold == 1;
                        boolean isFreeReturn = shopModel.info.isFreeReturns();
                        getView().showGoldMerchant(isGoldMerchant);
                        getView().showFreeReturn(isFreeReturn);
                    }
                });
    }

    @Override
    public void fetchCatalogData(String keyword, int departmentId, int start, int rows) {
        if (getCatalogListener != null) {
            getCatalogListener.getQuery(new CatalogQuery(keyword, departmentId, start, rows));
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
                if (dataScoringProductView != null) {
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

    private interface CatalogQueryListener {
        void getQuery(CatalogQuery catalogQuery);
    }

    public static class CatalogQuery {
        String keyword;
        int categoryId;
        int start;
        int row;

        public CatalogQuery(String keyword, int categoryId, int start, int row) {
            this.keyword = keyword;
            this.categoryId = categoryId;
            this.start = start;
            this.row = row;
        }

        public int getCategoryId() {
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

    public void detachView() {
        super.detachView();
        addProductUseCase.unsubscribe();
        saveDraftProductUseCase.unsubscribe();
        fetchCatalogDataUseCase.unsubscribe();
        getCategoryRecommUseCase.unsubscribe();
        shopInfoUseCase.unsubscribe();

        subscriptionDebounceCategoryRecomm.unsubscribe();
        subscriptionDebounceCatalog.unsubscribe();
    }
}
