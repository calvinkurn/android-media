package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatEmptyUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.OldGMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatClearCacheUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetBuyerGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetPopularProductUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetProductGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatMarketInsightUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.empty.GMEmptyModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * Created on 1/2/17.
 *
 * @author normansyahputa
 */

public class GMDashboardPresenterImpl extends GMDashboardPresenter {

    private GMStatMarketInsightUseCase marketInsightUseCase;
    private GMStatGetBuyerGraphUseCase buyerGraphUseCase;
    private GMStatGetPopularProductUseCase popularProductUseCase;
    private GMStatGetTransactionGraphUseCase transactionGraphUseCase;
    private GMStatGetProductGraphUseCase productGraphUseCase;
    private GMStatEmptyUseCase gmStatEmptyUseCase;

    public GMDashboardPresenterImpl(
            GMStatMarketInsightUseCase marketInsightUseCase,
            GMStatGetBuyerGraphUseCase buyerGraphUseCase,
            GMStatGetPopularProductUseCase popularProductUseCase,
            GMStatGetTransactionGraphUseCase transactionGraphUseCase,
            GMStatGetProductGraphUseCase productGraphUseCase,
            GMStatEmptyUseCase gmStatEmptyUseCase) {
        this.marketInsightUseCase = marketInsightUseCase;
        this.buyerGraphUseCase = buyerGraphUseCase;
        this.popularProductUseCase = popularProductUseCase;
        this.transactionGraphUseCase = transactionGraphUseCase;
        this.productGraphUseCase = productGraphUseCase;
        this.gmStatEmptyUseCase = gmStatEmptyUseCase;
    }

    public void getPopularProduct() {
        Calendar dayOne = Calendar.getInstance();
        dayOne.add(Calendar.DATE, -30);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        RequestParams popularParam = GMStatGetPopularProductUseCase.createRequestParam(dayOne.getTimeInMillis(), yesterday.getTimeInMillis());
        popularProductUseCase.execute(popularParam, new Subscriber<GetPopularProduct>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showMessageError(e);
            }

            @Override
            public void onNext(GetPopularProduct getPopularProduct) {
                getView().onSuccessPopularProduct(getPopularProduct);
            }
        });
    }

    public void getProductGraph(long startDate, long endDate) {
        RequestParams productParam = GMStatGetProductGraphUseCase.createRequestParam(startDate, endDate);
        productGraphUseCase.execute(productParam, new Subscriber<GetProductGraph>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showMessageError(e);
            }

            @Override
            public void onNext(GetProductGraph getProductGraph) {
                getView().onSuccessProductnGraph(getProductGraph);
            }
        });
    }

    @Override
    public void fetchData(long starDate, long endDate) {
        getProductGraph(starDate, endDate);
        RequestParams transactionParam = GMStatGetTransactionGraphUseCase.createRequestParam(starDate, endDate);
        transactionGraphUseCase.execute(transactionParam, new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showMessageError(e);
            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                getView().onSuccessTransactionGraph(mergeModel);
            }
        });

        getPopularProduct();

        RequestParams buyerParam = GMStatGetBuyerGraphUseCase.createRequestParam(starDate, endDate);
        buyerGraphUseCase.execute(buyerParam, new Subscriber<GetBuyerGraph>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showMessageError(e);
            }

            @Override
            public void onNext(GetBuyerGraph getBuyerGraph) {
                getView().onSuccessBuyerGraph(getBuyerGraph);
            }
        });

        marketInsightUseCase.execute(RequestParams.EMPTY, new Subscriber<KeywordModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showMessageError(e);
            }

            @Override
            public void onNext(KeywordModel keywordModel) {
                onSuccessGetShopCategory(keywordModel.getShopCategory());
                onSuccessGetKeyword(keywordModel.getKeywords());
                onSuccessGetCategory(keywordModel.getCategoryName());
            }
        });
    }

    private void onSuccessGetShopCategory(GetShopCategory getShopCategory) {
        if (getShopCategory == null
                || getShopCategory.getShopCategory() == null
                || getShopCategory.getShopCategory().isEmpty()) {
            getView().onGetShopCategoryEmpty();
        }
    }

    private void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
        getView().onSuccessGetKeyword(getKeywords);
    }

    private void onSuccessGetCategory(List<String> categoryNameList) {
        if (categoryNameList == null || categoryNameList.size() <= 0) {
            return;
        }
        String categoryName = categoryNameList.get(0);
        getView().onSuccessGetCategory(categoryName);
    }

    private void showMessageError(Throwable e) {
        gmStatEmptyUseCase.execute(RequestParams.EMPTY, new Subscriber<GMEmptyModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GMEmptyModel gmEmptyModel) {
                if (isViewAttached()) {
                    getView().onSuccessProductnGraph(gmEmptyModel.productGraph);
                    getView().onSuccessTransactionGraph(gmEmptyModel.transactionGraph);
                    getView().onSuccessBuyerGraph(gmEmptyModel.buyerGraph);
                    getView().onSuccessPopularProduct(gmEmptyModel.popularProduct);

                    if (gmEmptyModel.keywordModel.getShopCategory() == null
                            || gmEmptyModel.keywordModel.getShopCategory().getShopCategory() == null
                            || gmEmptyModel.keywordModel.getShopCategory().getShopCategory().isEmpty()) {
                        onSuccessGetShopCategory(gmEmptyModel.keywordModel.getShopCategory());
                        onSuccessGetKeyword(gmEmptyModel.keywordModel.getKeywords());
                    }

                    getView().showSnackbarRetry();
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        buyerGraphUseCase.unsubscribe();
        transactionGraphUseCase.unsubscribe();
        productGraphUseCase.unsubscribe();
        popularProductUseCase.unsubscribe();
        marketInsightUseCase.unsubscribe();
    }
}