package com.tokopedia.seller.transaction.neworder.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.transaction.neworder.data.NewOrderApi;
import com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder.NewOrderResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public class GetNewOrderCloud {
    public static final String BYPASS_HASH = "bypass_hash";
    public static final String VALUE_BYPASS = "1";
    private final NewOrderApi newOrderApi;
    private final Context context;

    @Inject
    public GetNewOrderCloud(NewOrderApi newOrderApi, @ApplicationContext Context context) {
        this.newOrderApi = newOrderApi;
        this.context = context;
    }

    public Observable<List<DataOrder>> getNewOrderList(TKPDMapParam<String, String> parameters) {
        return newOrderApi.getOrderNew(AuthUtil.generateParamsNetwork(context, getParamsNewOrder(parameters)))
                .map(new GetData<NewOrderResult>())
                .map(new GetListDataOrder());
    }

    public TKPDMapParam<String,String> getParamsNewOrder(TKPDMapParam<String, String> parameters) {
        parameters.put(BYPASS_HASH, VALUE_BYPASS);
        return parameters;
    }

    private class GetListDataOrder implements Func1<NewOrderResult, List<DataOrder>> {
        @Override
        public List<DataOrder> call(NewOrderResult newOrderResult) {
            return newOrderResult.getDataNewOrder().getDataOrder();
        }
    }
}
