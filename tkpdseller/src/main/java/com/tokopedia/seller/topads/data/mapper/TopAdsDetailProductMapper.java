package com.tokopedia.seller.topads.data.mapper;

import com.tokopedia.seller.topads.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsDetailProductMapper implements Func1<Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>>, TopAdsDetailShopDomainModel> {
    @Override
    public TopAdsDetailShopDomainModel call(Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private TopAdsDetailShopDomainModel mappingResponse(Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>> response) {
        TopAdsProductDetailDataSourceModel dataModel = response.body().getData().get(0);
        TopAdsDetailShopDomainModel domainModel = new TopAdsDetailShopDomainModel();
        domainModel.setAdId(dataModel.getAdId());
        domainModel.setAdType(dataModel.getAdType());
        domainModel.setGroupId(dataModel.getGroupId());
        domainModel.setShopId(dataModel.getShopId());
        domainModel.setItemId(dataModel.getItemId());
        domainModel.setStatus(dataModel.getStatus());
        domainModel.setPriceBid(dataModel.getPriceBid());
        domainModel.setAdBudget(dataModel.getAdBudget());
        domainModel.setPriceDaily(dataModel.getPriceDaily());
        domainModel.setStickerId(dataModel.getStickerId());
        domainModel.setAdSchedule(dataModel.getAdSchedule());
        domainModel.setAdStartDate(dataModel.getAdStartDate());
        domainModel.setAdStartTime(dataModel.getAdStartTime());
        domainModel.setAdEndDate(dataModel.getAdEndDate());
        domainModel.setAdEndTime(dataModel.getAdEndTime());
        domainModel.setAdImage(dataModel.getAdImage());
        domainModel.setAdTitle(dataModel.getAdTitle());
        return domainModel;
    }
}
