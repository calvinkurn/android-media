package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.content.res.AssetManager;

import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.domain.OldGMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.List;

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
            if (isViewAttached())
                getView().onError(e);
        }

        @Override
        public void onFailure() {

        }
    };
    private GMStatNetworkController gmStatNetworkController;

    public GMDashboardPresenterImpl(GMStatNetworkController gmStatNetworkController) {
        this.gmStatNetworkController = gmStatNetworkController;
    }

    @Override
    public void fetchData() {
        DatePickerViewModel datePickerViewModel = getView().datePickerViewModel();
        getView().resetToLoading();
        gmStatNetworkController.fetchData(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate(), compositeSubscription, gmStatListener);
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
