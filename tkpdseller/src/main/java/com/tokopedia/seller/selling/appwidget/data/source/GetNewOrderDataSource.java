package com.tokopedia.seller.selling.appwidget.data.source;

import com.tokopedia.seller.selling.appwidget.data.source.cloud.GetNewOrderCloud;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;

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

    public Observable<List<DataOrder>> getNewOrderList() {
        return getNewOrderCloud.getNewOrderList();
    }
}
