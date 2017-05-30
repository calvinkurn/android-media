package com.tokopedia.seller.topads.keyword.data.mapper;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.keyword.data.model.EditTopAdsKeywordDetailDataModel;
import com.tokopedia.seller.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailDataMapper implements Func1<Response<DataResponse<List<EditTopAdsKeywordDetailDataModel>>>, EditTopAdsKeywordDetailDomainModel> {

    public static final String ON = "on";
    public static final String OFF = "off";

    @Inject
    public TopAdsKeywordEditDetailDataMapper() {
    }

    public static TopAdsKeywordEditDetailInputDataModel mapDomainToData(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        TopAdsKeywordEditDetailInputDataModel dataModel = new TopAdsKeywordEditDetailInputDataModel();
        dataModel.setKeywordId(String.valueOf(modelInput.getKeywordId()));
        dataModel.setGroupId(String.valueOf(modelInput.getGroupId()));
        dataModel.setKeywordTag(modelInput.getKeywordTag());
        dataModel.setKeywordTypeId(String.valueOf(modelInput.getKeywordTypeId()));
        dataModel.setPriceBid(modelInput.getPriceBid());
        dataModel.setShopId(modelInput.getShopId());
        dataModel.setSource("dashboard_user_main");
        if (modelInput.getToggle() == 1) {
            dataModel.setToggle(ON);
        } else {
            dataModel.setToggle(OFF);
        }
        return dataModel;
    }

    @Override
    public EditTopAdsKeywordDetailDomainModel call(Response<DataResponse<List<EditTopAdsKeywordDetailDataModel>>> dataResponse) {
        EditTopAdsKeywordDetailDomainModel domainModel = new EditTopAdsKeywordDetailDomainModel();
        EditTopAdsKeywordDetailDataModel dataModel = dataResponse.body().getData().get(0);
        domainModel.setGroupId(Long.parseLong(dataModel.getGroupId()));
        domainModel.setKeywordId(Integer.parseInt(dataModel.getKeywordId()));
        domainModel.setKeywordTag(dataModel.getKeywordTag());
        domainModel.setKeywordTypeId(Integer.parseInt(dataModel.getKeywordTypeId()));
        domainModel.setPriceBid(Double.parseDouble(dataModel.getPriceBid()));
        domainModel.setShopId(dataModel.getShopId());
        domainModel.setSource(dataModel.getSource());
        int toggle = (dataModel.getToggle().equals(ON)) ? 1 : 0;
        domainModel.setToggle(toggle);
        return domainModel;
    }
}
