package com.tokopedia.seller.topads.keyword.data.source.cloud.api;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.data.model.request.DataRequest;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.seller.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class DashboardKeywordCloud {

    private KeywordApi keywordApi;

    @Inject
    public DashboardKeywordCloud(KeywordApi keywordApi) {
        this.keywordApi = keywordApi;
    }

    public Observable<PageDataResponse<List<Datum>>> getDashboardKeyword(RequestParams requestParams) {
        return keywordApi.getDashboardKeyword(requestParams.getParamsAllValueInString());
    }

    public Observable<Response<DataResponse<List<EditTopAdsKeywordDetailDomainModel>>>> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDataModel dataModel) {
        return keywordApi.editTopAdsKeywordDetail(getEditTopAdsKeywordDetailModel(dataModel));
    }

    private DataRequest<List<TopAdsKeywordEditDetailInputDataModel>> getEditTopAdsKeywordDetailModel(TopAdsKeywordEditDetailInputDataModel dataModel) {
        DataRequest<List<TopAdsKeywordEditDetailInputDataModel>> dataRequest = new DataRequest<>();
        List<TopAdsKeywordEditDetailInputDataModel> list = new ArrayList<>();
        list.add(dataModel);
        dataRequest.setData(list);
        return dataRequest;
    }
}
