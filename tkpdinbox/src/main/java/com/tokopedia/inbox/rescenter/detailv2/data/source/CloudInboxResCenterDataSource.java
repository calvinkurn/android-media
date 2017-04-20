package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.network.apiservices.user.apis.InboxResCenterApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.TrackAwbReturProductMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;

import rx.Observable;

/**
 * Created by hangnadi on 3/16/17.
 */

public class CloudInboxResCenterDataSource {
    private final Context context;
    private final InboxResCenterApi inboxResCenterApi;
    private final TrackAwbReturProductMapper trackAwbReturProductMapper;

    public CloudInboxResCenterDataSource(Context context, InboxResCenterApi inboxResCenterApi) {
        super();
        this.context = context;
        this.inboxResCenterApi = inboxResCenterApi;
        this.trackAwbReturProductMapper = new TrackAwbReturProductMapper();
    }

    public Observable<TrackingAwbReturProduct> trackAwbReturProduct(TKPDMapParam<String, Object> parameters) {
        return inboxResCenterApi.trackShippingRefv2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(trackAwbReturProductMapper);
    }
}