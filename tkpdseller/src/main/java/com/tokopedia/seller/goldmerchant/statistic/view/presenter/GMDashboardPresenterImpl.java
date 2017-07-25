package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.util.Log;

import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.domain.OldGMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.subscriptions.CompositeSubscription;

/**
 * Created on 1/2/17.
 * @author normansyahputa
 */

public class GMDashboardPresenterImpl extends GMDashboardPresenter {
    public static final String IS_FETCH_DATA = "IS_FETCH_DATA";
    public static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String TAG = "GMFragmentPresenterImpl";
    private static final Locale locale = new Locale("in", "ID");
    private boolean isFetchData = false, isFirstTime = false;
    @IntRange(from = 0, to = 2)
    private int lastSelectionPeriod = 1;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private long sDate = -1, eDate = -1;
    private long shopId;
    private float[] mValues = new float[10];
    private String[] mLabels = new String[10];
    private int selectionType;
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
                getView().onSuccessTransactionGraph(getTransactionGraph, sDate, eDate, lastSelectionPeriod, selectionType);
        }

        @Override
        public void onSuccessProductnGraph(GetProductGraph getProductGraph) {
            if (isViewAttached())
                getView().onSuccessProductnGraph(getProductGraph, isFirstTime);
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

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public void setFetchData(boolean fetchData) {
        isFetchData = fetchData;
    }

    public void initInstance() {
        for (int i = 0; i < mLabels.length; i++) {
            mLabels[i] = "";
        }
    }

    public float[] getmValues() {
        return mValues;
    }

    public String[] getmLabels() {
        return mLabels;
    }

    @Override
    public void fetchData() {
        sDate = getView().datePickerViewModel().getStartDate();
        eDate = getView().datePickerViewModel().getEndDate();
        if (isFirstTime && isFetchData) {
            getView().resetToLoading();
            gmStatNetworkController.fetchData(shopId, sDate, eDate, compositeSubscription, gmStatListener);
        } else if (!isFirstTime) {
            gmStatNetworkController.fetchData(shopId, sDate, eDate, compositeSubscription, gmStatListener);
        }

        if (isFetchData) {
            isFetchData = false;
        }
    }

    @Override
    public void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        this.lastSelectionPeriod = lastSelectionPeriod;
        this.selectionType = selectionType;
        isFetchData = true;
        isFirstTime = true;
        this.sDate = sDate;
        this.eDate = eDate;

        //[START] dummy data, dont remove this line of code - necessary for demo.
//        gmstat.getGmStatNetworkController().fetchData(gmStatListener, getActivity().getAssets());
        //[END] dummy data
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


        resetDateSelection();
        gmStatNetworkController.fetchDataEmptyState(gmStatListener, assets);
    }

    @Override
    public void saveState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(IS_FETCH_DATA, isFetchData);
        savedInstanceState.putBoolean(IS_FIRST_TIME, isFirstTime);
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        isFetchData = savedInstanceState.getBoolean(IS_FETCH_DATA);
        isFirstTime = savedInstanceState.getBoolean(IS_FIRST_TIME);
    }

    /**
     * reset sDate-eDate to 7 days
     */
    private void resetDateSelection(){
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, -1);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);


        sDate = startCalendar.getTimeInMillis();
        eDate = endCalendar.getTimeInMillis();
        Log.d(TAG, String.format("resetDateSelection : %s - %s ", getFormattedDate(sDate), getFormattedDate(eDate)));
        isFirstTime = false;
    }

    public String getFormattedDate(long dateLong) {
        Date date = new Date(dateLong);
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale);// "HH:mm:ss:SSS"
        return formatter.format(date);
    }

    public void setsDate(long sDate) {
        this.sDate = sDate;
    }

    public void seteDate(long eDate) {
        this.eDate = eDate;
    }
}
