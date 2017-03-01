package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.mapper.TopAdsCreateGroupMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.model.request.CreateGroupRequest;
import com.tokopedia.seller.topads.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;
import com.tokopedia.seller.topads.domain.model.request.DataRequest;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGroupAdsDataSource {

    private final TopAdsSearchGroupMapper topAdsSearchGroupMapper;
    private final TopAdsCreateGroupMapper topAdsCreateGroupMapper;
    private final TopAdsManagementApi topAdsManagementApi;
    private final Context context;

    public TopAdsGroupAdsDataSource(Context context, TopAdsManagementApi topAdsManagementApi,
                                    TopAdsSearchGroupMapper topAdsSearchGroupMapper,
                                    TopAdsCreateGroupMapper topAdsCreateGroupMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsSearchGroupMapper;
        this.topAdsCreateGroupMapper = topAdsCreateGroupMapper;
    }

    public Observable<List<GroupAd>> searchGroupAds(String keyword) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_SHOP_ID, SessionHandler.getShopID(context));
        param.put(TopAdsNetworkConstant.PARAM_KEYWORD, keyword);
        return topAdsManagementApi.searchGroupAd(param).map(topAdsSearchGroupMapper);
    }

    public Observable<DataResponseCreateGroup> createGroup(CreateGroupRequest createGroupRequest) {
        DataRequest<CreateGroupRequest> dataRequest = new DataRequest<>();
        dataRequest.setData(createGroupRequest);
        return topAdsManagementApi.createGroupAd(dataRequest).map(topAdsCreateGroupMapper);
    }
}
