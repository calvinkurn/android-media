package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.model.request.CreateGroupRequest;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.data.model.request.DataRequest;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailGroupDomainModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGroupAdsDataSource {

    private final TopAdsSearchGroupMapper topAdsSearchGroupMapper;
    private final TopAdsDetailGroupMapper topAdsDetailGroupMapper;
    private final TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper;
    private final TopAdsManagementApi topAdsManagementApi;
    private final Context context;

    public TopAdsGroupAdsDataSource(Context context, TopAdsManagementApi topAdsManagementApi,
                                    TopAdsSearchGroupMapper topAdsSearchGroupMapper,
                                    TopAdsDetailGroupMapper topAdsDetailGroupMapper,
                                    TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsSearchGroupMapper;
        this.topAdsDetailGroupMapper = topAdsDetailGroupMapper;
        this.topAdsDetailGroupDomainMapper = topAdsDetailGroupDomainMapper;
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
        return topAdsManagementApi.createGroupAd(dataRequest).map(topAdsDetailGroupMapper);
    }

    public Observable<TopAdsDetailGroupDomainModel> getDetailGroup(String groupId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_GROUP_ID, groupId);
        return topAdsManagementApi.getDetailGroup(params).map(topAdsDetailGroupDomainMapper);
    }
}
