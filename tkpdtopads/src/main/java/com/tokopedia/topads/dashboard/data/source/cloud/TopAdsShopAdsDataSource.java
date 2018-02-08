package com.tokopedia.topads.dashboard.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailShopMapper;
import com.tokopedia.topads.dashboard.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsShopAdsDataSource {

    private final TopAdsDetailShopMapper topAdsDetailShopMapper;
    private final TopAdsManagementApi topAdsManagementApi;
    private final Context context;

    public TopAdsShopAdsDataSource(Context context, TopAdsManagementApi topAdsManagementApi, TopAdsDetailShopMapper topAdsDetailShopMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsDetailShopMapper = topAdsDetailShopMapper;
    }

    public Observable<TopAdsDetailShopDomainModel> getDetailProduct(String adId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return topAdsManagementApi.getDetailProduct(param).map(topAdsDetailShopMapper);
    }

    public Observable<TopAdsDetailShopDomainModel> saveDetailProduct(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
        return topAdsManagementApi.editProductAd(getSaveProductDetailRequest(topAdsDetailShopDomainModel)).map(topAdsDetailShopMapper);
    }

    public Observable<TopAdsDetailShopDomainModel> createDetailShop(TopAdsDetailShopDomainModel topAdsDetailShopDomainModels){
        return topAdsManagementApi.createProductAd(getSaveProductDetailRequest(topAdsDetailShopDomainModels)).map(topAdsDetailShopMapper);
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
        dataModel.setSource(TopAdsNetworkConstant.VALUE_SOURCE_ANDROID);
        switch (Integer.parseInt(domainModel.getStatus())) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
            case TopAdsConstant.STATUS_AD_NOT_SENT:
                dataModel.setToggle(TopAdsNetworkConstant.VALUE_TOGGLE_ON);
                break;
            default:
                dataModel.setToggle(TopAdsNetworkConstant.VALUE_TOGGLE_OFF);
                break;
        }
        return dataModel;
    }
}