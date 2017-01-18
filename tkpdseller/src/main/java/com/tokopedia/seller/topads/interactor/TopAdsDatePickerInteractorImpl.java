package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.DataStatistic;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class TopAdsDatePickerInteractorImpl implements TopAdsDatePickerInteractor {

    private TopAdsCacheDataSourceImpl topAdsCacheDataSource;
    private Context context;

    public TopAdsDatePickerInteractorImpl(Context context) {
        this.context = context;
        topAdsCacheDataSource = new TopAdsCacheDataSourceImpl(context);
    }

    @Override
    public void resetDate() {
        topAdsCacheDataSource.resetDate();
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        topAdsCacheDataSource.saveDate(startDate, endDate);
    }

    @Override
    public Date getStartDate(Date defaultDate) {
        return topAdsCacheDataSource.getStartDate(defaultDate);
    }

    @Override
    public Date getEndDate(Date defaultDate) {
        return topAdsCacheDataSource.getEndDate(defaultDate);
    }

    @Override
    public void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex) {
        topAdsCacheDataSource.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
    }

    @Override
    public int getLastSelectionDatePickerType() {
        return topAdsCacheDataSource.getLastSelectionDatePickerType();
    }

    @Override
    public int getLastSelectionDatePickerIndex() {
        return topAdsCacheDataSource.getLastSelectionDatePickerIndex();
    }
}
