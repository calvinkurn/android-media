package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.content.res.AssetManager;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.OldGMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetBuyerGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetPopularProductUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetProductGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatMarketInsightUseCase;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 1/2/17.
 * @author normansyahputa
 */

public class GMDashboardPresenterImpl extends GMDashboardPresenter {
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private OldGMStatRepository gmStatListener = new OldGMStatRepository() {
        @Override
        public void onSuccessGetShopCategory(GetShopCategory getShopCategory) {
            if (isViewAttached()) {
                if (getShopCategory == null
                        || getShopCategory.getShopCategory() == null
                        || getShopCategory.getShopCategory().isEmpty()) {
                    getView().onGetShopCategoryEmpty();
                }
            }
        }

        @Override
        public void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph) {
            if (isViewAttached())
                getView().onSuccessTransactionGraph(getTransactionGraph);
        }

        @Override
        public void onSuccessProductGraph(GetProductGraph getProductGraph) {
            if (isViewAttached())
                getView().onSuccessProductnGraph(getProductGraph);
        }

        @Override
        public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
            if (isViewAttached())
                getView().onSuccessPopularProduct(getPopularProduct);
        }

        @Override
        public void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph) {
            if (isViewAttached())
                getView().onSuccessBuyerGraph(getBuyerGraph);
        }

        @Override
        public void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
            if (isViewAttached())
                getView().onSuccessGetKeyword(getKeywords);
        }

        @Override
        public void onSuccessGetCategory(List<String> categoryNameList) {
            if (!isViewAttached())
                return;

            if (categoryNameList == null || categoryNameList.size() <= 0)
                return;

            String categoryName = categoryNameList.get(0);

            getView().onSuccessGetCategory(categoryName);
        }

        @Override
        public void onComplete() {
            if (isViewAttached())
                getView().onComplete();
        }

        @Override
        public void onError(Throwable e) {
            showMessageError(e);
        }

        @Override
        public void onFailure() {

        }
    };

    private void showMessageError(Throwable e) {
        if (isViewAttached())
            getView().onError(e);
    }

    private GMStatNetworkController gmStatNetworkController;
    private GMStatMarketInsightUseCase marketInsightUseCase;
    private GMStatGetBuyerGraphUseCase buyerGraphUseCase;
    private GMStatGetPopularProductUseCase popularProductUseCase;
    private GMStatGetTransactionGraphUseCase transactionGraphUseCase;
    private GMStatGetProductGraphUseCase productGraphUseCase;

    public GMDashboardPresenterImpl(
            GMStatNetworkController gmStatNetworkController,
            GMStatMarketInsightUseCase marketInsightUseCase,
            GMStatGetBuyerGraphUseCase buyerGraphUseCase,
            GMStatGetPopularProductUseCase popularProductUseCase,
            GMStatGetTransactionGraphUseCase transactionGraphUseCase,
            GMStatGetProductGraphUseCase productGraphUseCase
    ) {
        this.gmStatNetworkController = gmStatNetworkController;
        this.marketInsightUseCase = marketInsightUseCase;
        this.buyerGraphUseCase = buyerGraphUseCase;
        this.popularProductUseCase = popularProductUseCase;
        this.transactionGraphUseCase = transactionGraphUseCase;
        this.productGraphUseCase = productGraphUseCase;
    }

    private void processKeywordModel(KeywordModel keywordModel) {
        if (keywordModel.getShopCategory() == null
                || keywordModel.getShopCategory().getShopCategory() == null
                || keywordModel.getShopCategory().getShopCategory().isEmpty())
            return;

        onSuccessGetShopCategory(keywordModel.getShopCategory());

        onSuccessGetKeyword(keywordModel.getKeywords());

        onSuccessGetCategory(keywordModel.getCategoryName());
    }

    public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
        if (isViewAttached())
            getView().onSuccessPopularProduct(getPopularProduct);
    }

    public void onSuccessGetShopCategory(GetShopCategory getShopCategory) {
        if (isViewAttached()) {
            if (getShopCategory == null
                    || getShopCategory.getShopCategory() == null
                    || getShopCategory.getShopCategory().isEmpty()) {
                getView().onGetShopCategoryEmpty();
            }
        }
    }

    private void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
        if (isViewAttached())
            getView().onSuccessGetKeyword(getKeywords);
    }

    private void onSuccessGetCategory(List<String> categoryNameList) {
        if (!isViewAttached())
            return;

        if (categoryNameList == null || categoryNameList.size() <= 0)
            return;

        String categoryName = categoryNameList.get(0);

        getView().onSuccessGetCategory(categoryName);
    }

    public void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph) {
        if (isViewAttached())
            getView().onSuccessBuyerGraph(getBuyerGraph);
    }

    public void onSuccessProductGraph(GetProductGraph getProductGraph) {
        if (isViewAttached())
            getView().onSuccessProductnGraph(getProductGraph);
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
                onSuccessPopularProduct(getPopularProduct);
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
                onSuccessProductGraph(getProductGraph);
            }
        });
    }

    public void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph) {
        if (isViewAttached())
            getView().onSuccessTransactionGraph(getTransactionGraph);
    }

    @Override
    public void fetchData() {
        DatePickerViewModel datePickerViewModel = getView().datePickerViewModel();
        getView().resetToLoading();

        getProductGraph(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());

        RequestParams transactionParam = GMStatGetTransactionGraphUseCase.createRequestParam(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
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
                onSuccessTransactionGraph(mergeModel);
            }
        });

        getPopularProduct();

        RequestParams buyerParam = GMStatGetBuyerGraphUseCase.createRequestParam(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
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
                onSuccessBuyerGraph(getBuyerGraph);
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
                processKeywordModel(keywordModel);
            }
        });
    }

    @Override
    public void onResume() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        fetchData();
    }

    @Override
    public void onPause() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    public void displayDefaultValue(AssetManager assets) {
        if (assets == null)
            return;

        gmStatNetworkController.fetchDataEmptyState(gmStatListener, assets);
    }
}
