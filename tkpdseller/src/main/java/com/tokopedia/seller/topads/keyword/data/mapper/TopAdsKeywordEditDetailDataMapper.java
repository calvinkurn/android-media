package com.tokopedia.seller.topads.keyword.data.mapper;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;

import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailDataMapper implements Func1<Response<DataResponse<List<EditTopAdsKeywordDetailDomainModel>>>, EditTopAdsKeywordDetailDomainModel> {

    public static TopAdsKeywordEditDetailInputDataModel mapDomainToData(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        TopAdsKeywordEditDetailInputDataModel dataModel = new TopAdsKeywordEditDetailInputDataModel();
        dataModel.setKeywordId(String.valueOf(modelInput.getKeywordId()));
        dataModel.setGroupId(String.valueOf(modelInput.getGroupId()));
        dataModel.setKeywordTag(modelInput.getKeywordTag());
        dataModel.setKeywordTypeId(String.valueOf(modelInput.getKeywordTypeId()));
        dataModel.setPriceBid(String.valueOf(modelInput.getPriceBid()));
        dataModel.setShopId(modelInput.getShopId());
        dataModel.setSource(TopAdsNetworkConstant.VALUE_SOURCE_ANDROID);
        dataModel.setToggle(String.valueOf(modelInput.getToggle()));
        return dataModel;
    }

    @Override
    public EditTopAdsKeywordDetailDomainModel call(Response<DataResponse<List<EditTopAdsKeywordDetailDomainModel>>> dataResponse) {
        EditTopAdsKeywordDetailDomainModel domainModel = new EditTopAdsKeywordDetailDomainModel();
        return domainModel;
    }
}
