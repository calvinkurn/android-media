package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class TopAdsDetailGroupDomainMapper implements Func1<Response<DataResponse<DataResponseCreateGroup>>, TopAdsDetailGroupDomainModel> {

    @Inject
    public TopAdsDetailGroupDomainMapper() {
    }


    @Override
    public TopAdsDetailGroupDomainModel call(Response<DataResponse<DataResponseCreateGroup>> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                && dataResponseResponse.body().getData() != null) {
            DataResponseCreateGroup dataResponseCreateGroup = dataResponseResponse.body().getData();
            return convertResponseToDomain(dataResponseCreateGroup);
        } else {
            return null;
        }
    }

    private TopAdsDetailGroupDomainModel convertResponseToDomain(DataResponseCreateGroup apiResponse){
        TopAdsDetailGroupDomainModel model = new TopAdsDetailGroupDomainModel();
        model.setAdId(apiResponse.getGroupId()); // groupID is assumed as adId
        model.setAdTitle(apiResponse.getGroupName());
        model.setGroupId(apiResponse.getGroupId());
        model.setShopId(apiResponse.getShopId());
        model.setStatus(apiResponse.getStatus());
        model.setAdSchedule(apiResponse.getGroupSchedule() == null ? "0" : "1");
        model.setAdStartDate(apiResponse.getGroupStartDate());
        model.setAdStartTime(apiResponse.getGroupStartTime());
        model.setAdEndDate(apiResponse.getGroupEndDate());
        model.setAdEndTime(apiResponse.getGroupEndTime());
        model.setPriceBid(apiResponse.getPriceBid());
        model.setAdBudget(apiResponse.getPriceDaily() != null && apiResponse.getPriceDaily() > 0 ? "1" : "0");
        model.setPriceDaily(apiResponse.getPriceDaily() == null ? 0 : apiResponse.getPriceDaily());
        model.setStickerId(apiResponse.getStickerId());
        model.setGroupTotal(apiResponse.getGroupTotal());
        model.setKeywordTotal(apiResponse.getKeywordTotal());
        return model;
    }
}
