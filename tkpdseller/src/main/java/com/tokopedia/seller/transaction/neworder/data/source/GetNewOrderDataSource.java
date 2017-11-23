package com.tokopedia.seller.transaction.neworder.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.transaction.neworder.data.source.cloud.GetNewOrderCloud;
import com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder.DataOrder;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public class GetNewOrderDataSource {
    private final GetNewOrderCloud getNewOrderCloud;

    @Inject
    public GetNewOrderDataSource(GetNewOrderCloud getNewOrderCloud) {
        this.getNewOrderCloud = getNewOrderCloud;
    }

    public Observable<List<DataOrder>> getNewOrderList(RequestParams requestParams) {
        return getNewOrderCloud.getNewOrderList(requestParams.getParamsAllValueInString());
    }
}
