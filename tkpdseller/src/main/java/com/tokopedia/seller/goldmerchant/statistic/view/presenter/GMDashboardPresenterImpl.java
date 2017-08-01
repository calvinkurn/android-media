package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMStatConstant;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetBuyerGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetPopularProductUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetProductGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphShopInfoUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatMarketInsightShopInfoUseCase;
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

    private GMStatMarketInsightShopInfoUseCase marketInsightShopInfoUseCase;
    private GMStatGetBuyerGraphUseCase buyerGraphUseCase;
    private GMStatGetPopularProductUseCase popularProductUseCase;
    private GMStatGetTransactionGraphShopInfoUseCase gmStatGetTransactionGraphShopInfoUseCase;
    private GMStatGetProductGraphUseCase productGraphUseCase;

    public GMDashboardPresenterImpl(
            GMStatMarketInsightShopInfoUseCase marketInsightShopInfoUseCase ,
            GMStatGetBuyerGraphUseCase buyerGraphUseCase,
            GMStatGetPopularProductUseCase popularProductUseCase,
            GMStatGetTransactionGraphShopInfoUseCase gmStatGetTransactionGraphShopInfoUseCase,
            GMStatGetProductGraphUseCase productGraphUseCase) {
        this.marketInsightShopInfoUseCase = marketInsightShopInfoUseCase;
        this.buyerGraphUseCase = buyerGraphUseCase;
        this.popularProductUseCase = popularProductUseCase;
        this.gmStatGetTransactionGraphShopInfoUseCase = gmStatGetTransactionGraphShopInfoUseCase;
        this.productGraphUseCase = productGraphUseCase;
    }

    @Override
    public void fetchData(long starDate, long endDate) {
        getProductGraph(starDate, endDate);
        RequestParams transactionParam = GMStatGetTransactionGraphUseCase.createRequestParam(starDate, endDate);
        gmStatGetTransactionGraphShopInfoUseCase.execute(transactionParam, new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadTransactionGraph(e);
                }
            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                getView().onSuccessLoadTransactionGraph(mergeModel, mergeModel.isGoldMerchant());
            }
        });

        RequestParams buyerParam = GMStatGetBuyerGraphUseCase.createRequestParam(starDate, endDate);
        buyerGraphUseCase.execute(buyerParam, new Subscriber<GetBuyerGraph>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadBuyerGraph(e);
                }
            }

            @Override
            public void onNext(GetBuyerGraph getBuyerGraph) {
                getView().onSuccessLoadBuyerGraph(getBuyerGraph);
            }
        });

        marketInsightShopInfoUseCase.execute(RequestParams.EMPTY, new Subscriber<KeywordModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadMarketInsight(e);
                }
            }

            @Override
            public void onNext(KeywordModel keywordModel) {
                onSuccessGetShopCategory(keywordModel.getShopCategory(), keywordModel.isGoldMerchant());
                onSuccessGetKeyword(keywordModel.getKeywords(), keywordModel.isGoldMerchant());
                onSuccessGetCategory(keywordModel.getCategoryName());
            }
        });

        getPopularProduct();
    }

    private void getProductGraph(long startDate, long endDate) {
        RequestParams productParam = GMStatGetProductGraphUseCase.createRequestParam(startDate, endDate);
        productGraphUseCase.execute(productParam, new Subscriber<GetProductGraph>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadProductGraph(e);
                }
            }

            @Override
            public void onNext(GetProductGraph getProductGraph) {
                getView().onSuccessLoadProductGraph(getProductGraph);
            }
        });
    }

    private void getPopularProduct() {
        Calendar dayOne = Calendar.getInstance();
        dayOne.add(Calendar.DATE, -GMStatConstant.POPULAR_PRODUCT_DATE_RANGE);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, GMStatConstant.MAX_DAY_FROM_CURRENT_DATE);
        RequestParams popularParam = GMStatGetPopularProductUseCase.createRequestParam(dayOne.getTimeInMillis(), yesterday.getTimeInMillis());
        popularProductUseCase.execute(popularParam, new Subscriber<GetPopularProduct>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadPopularProduct(e);
                }
            }

            @Override
            public void onNext(GetPopularProduct getPopularProduct) {
                getView().onSuccessLoadPopularProduct(getPopularProduct);
            }
        });
    }

    private void onSuccessGetShopCategory(GetShopCategory getShopCategory, boolean isGoldMerchant) {
        if (getShopCategory == null
                || getShopCategory.getShopCategory() == null
                || getShopCategory.getShopCategory().isEmpty()) {
            getView().onGetShopCategoryEmpty(isGoldMerchant);
        }
    }

    private void onSuccessGetKeyword(List<GetKeyword> getKeywords, boolean isGoldMerchant) {
        getView().onSuccessGetKeyword(getKeywords, isGoldMerchant);
    }

    private void onSuccessGetCategory(List<String> categoryNameList) {
        if (categoryNameList == null || categoryNameList.size() <= 0) {
            return;
        }
        String categoryName = categoryNameList.get(0);
        getView().onSuccessGetCategory(categoryName);
    }

    @Override
    public void detachView() {
        super.detachView();
        buyerGraphUseCase.unsubscribe();
        gmStatGetTransactionGraphShopInfoUseCase.unsubscribe();
        productGraphUseCase.unsubscribe();
        popularProductUseCase.unsubscribe();
        marketInsightShopInfoUseCase.unsubscribe();
    }
}