package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.topads.dashboard.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsDetailProductMapper implements Func1<Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>>, TopAdsDetailProductDomainModel> {

    @Inject
    public TopAdsDetailProductMapper() {
    }

    @Override
    public TopAdsDetailProductDomainModel call(Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>> dataResponseResponse) {
        return mappingResponse(dataResponseResponse);
    }

    private TopAdsDetailProductDomainModel mappingResponse(Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>> response) {
        if (response.isSuccessful() && response.body() != null
                && response.body().getData() != null) {
            TopAdsProductDetailDataSourceModel dataModel = response.body().getData().get(0);
            TopAdsDetailProductDomainModel domainModel = new TopAdsDetailProductDomainModel();
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
            domainModel.setEnoughDeposit(dataModel.isEnoughDeposit());
            domainModel.setSuggestionBidValue(dataModel.getSuggestionBidValue());
            domainModel.setSuggestionBidButton(dataModel.getSuggestionBidButton());
            return domainModel;
        }else{
            return null;
        }
    }
}
