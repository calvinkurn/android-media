package com.tokopedia.seller.topads.data.mapper;

import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailGroupDomainModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class TopAdsDetailGroupDomainMapper implements Func1<Response<DataResponse<DataResponseCreateGroup>>, TopAdsDetailGroupDomainModel> {
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
        TopAdsDetailGroupDomainModel model = new TopAdsDetailGroupDomainModel(
                apiResponse.getGroupId(), // adId is assumed to groupId
                apiResponse.getGroupName(), // adTitle
                apiResponse.getGroupId(),
                apiResponse.getShopId(),
                apiResponse.getStatus(),
                apiResponse.getGroupSchedule() == null ? "0": "1",
                apiResponse.getGroupStartDate(),
                apiResponse.getGroupStartTime(),
                apiResponse.getGroupEndDate(),
                apiResponse.getGroupEndTime(),
                apiResponse.getPriceBid(),
                apiResponse.getPriceDaily() != null && apiResponse.getPriceDaily() > 0 ? "1" : "0",
                apiResponse.getPriceDaily() == null ? 0 : apiResponse.getPriceDaily() ,
                apiResponse.getStickerId(),
                apiResponse.getGroupTotal()
        );

        return model;
    }
}
