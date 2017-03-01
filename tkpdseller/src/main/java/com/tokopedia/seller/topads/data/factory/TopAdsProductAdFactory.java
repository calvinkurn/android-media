package com.tokopedia.seller.topads.data.factory;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.TopAdsBulkActionMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsProductAdsDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsProductAdFactory {

    private final Context context;
    private final TopAdsManagementApi topAdsManagementApi;
    private final TopAdsDetailProductMapper topAdsSearchGroupMapper;
    private TopAdsBulkActionMapper topAdsBulkActionMapper;

    public TopAdsProductAdFactory(Context context, TopAdsManagementApi topAdsManagementApi,
                                  TopAdsDetailProductMapper topAdsDetailProductMapper, TopAdsBulkActionMapper topAdsBulkActionMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsDetailProductMapper;
        this.topAdsBulkActionMapper = topAdsBulkActionMapper;
    }

    public TopAdsProductAdsDataSource createProductAdsDataSource() {
        return new TopAdsProductAdsDataSource(context, topAdsManagementApi, topAdsSearchGroupMapper, topAdsBulkActionMapper);
    }
}
