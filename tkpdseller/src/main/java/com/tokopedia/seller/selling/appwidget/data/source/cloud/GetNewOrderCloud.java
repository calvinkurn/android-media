package com.tokopedia.seller.selling.appwidget.data.source.cloud;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.selling.appwidget.data.NewOrderApi;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.NewOrderResult;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public class GetNewOrderCloud {
    private final NewOrderApi newOrderApi;
    private final Context context;

    @Inject
    public GetNewOrderCloud(NewOrderApi newOrderApi, @ActivityContext Context context) {
        this.newOrderApi = newOrderApi;
        this.context = context;
    }

    public Observable<List<DataOrder>> getNewOrderList() {
        return newOrderApi.getOrderNew(AuthUtil.generateParamsNetwork(context, getParamsNewOrder()))
                .map(new GetData<NewOrderResult>())
                .map(new GetListDataOrder());
    }

    public TKPDMapParam<String,String> getParamsNewOrder() {
        TKPDMapParam<String, String> paramsNewOrder = new TKPDMapParam<>();
        paramsNewOrder.put("page", "1");
        paramsNewOrder.put("per_page", "3");
        paramsNewOrder.put("bypass_hash", "1");
        return paramsNewOrder;
    }

    private class GetListDataOrder implements Func1<NewOrderResult, List<DataOrder>> {
        @Override
        public List<DataOrder> call(NewOrderResult newOrderResult) {
            return newOrderResult.getData().getDataOrder();
        }
    }
}
