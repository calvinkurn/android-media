package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.domain.model.data.GroupAdAction;
import com.tokopedia.seller.topads.domain.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.domain.model.request.DataRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsShopAdsDataSource {

    private final TopAdsDetailProductMapper topAdsDetailProductMapper;
    private final TopAdsManagementApi topAdsManagementApi;
    private final Context context;

    public TopAdsShopAdsDataSource(Context context, TopAdsManagementApi topAdsManagementApi, TopAdsDetailProductMapper topAdsDetailProductMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsDetailProductMapper = topAdsDetailProductMapper;
    }

    public Observable<TopAdsDetailShopDomainModel> getDetailProduct(String adId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return topAdsManagementApi.getDetailProduct(param).map(topAdsDetailProductMapper);
    }

    public Observable<TopAdsDetailShopDomainModel> saveDetailProduct(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
        return topAdsManagementApi.saveProductAd(getSaveProductDetailRequest(topAdsDetailShopDomainModel)).map(topAdsDetailProductMapper);
    }

    private DataRequest<List<TopAdsProductDetailDataSourceModel>> getSaveProductDetailRequest(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
        DataRequest<List<TopAdsProductDetailDataSourceModel>> dataRequest = new DataRequest<>();
        List<TopAdsProductDetailDataSourceModel> dataRequestList = new ArrayList<>();
        dataRequestList.add(convert(topAdsDetailShopDomainModel));
        dataRequest.setData(dataRequestList);
        return dataRequest;
    }

    private TopAdsProductDetailDataSourceModel convert(TopAdsDetailShopDomainModel domainModel) {
        TopAdsProductDetailDataSourceModel dataModel = new TopAdsProductDetailDataSourceModel();
        dataModel.setAdId(domainModel.getAdId());
        dataModel.setAdType(domainModel.getAdType());
        dataModel.setGroupId(domainModel.getGroupId());
        dataModel.setShopId(domainModel.getShopId());
        dataModel.setItemId(domainModel.getItemId());
        dataModel.setStatus(domainModel.getStatus());
        dataModel.setPriceBid(domainModel.getPriceBid());
        dataModel.setAdBudget(domainModel.getAdBudget());
        dataModel.setPriceDaily(domainModel.getPriceDaily());
        dataModel.setStickerId(domainModel.getStickerId());
        dataModel.setAdSchedule(domainModel.getAdSchedule());
        dataModel.setAdStartDate(domainModel.getAdStartDate());
        dataModel.setAdStartTime(domainModel.getAdStartTime());
        dataModel.setAdEndDate(domainModel.getAdEndDate());
        dataModel.setAdEndTime(domainModel.getAdEndTime());
        dataModel.setAdImage(domainModel.getAdImage());
        dataModel.setAdTitle(domainModel.getAdTitle());
        return dataModel;
    }
}
