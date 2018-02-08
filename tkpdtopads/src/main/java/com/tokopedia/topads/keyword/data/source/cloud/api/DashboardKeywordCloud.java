package com.tokopedia.topads.keyword.data.source.cloud.api;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.EditTopAdsKeywordDetailDataModel;
import com.tokopedia.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.topads.keyword.data.mapper.KeywordAddDomainDataMapper;
import com.tokopedia.topads.keyword.data.model.cloud.KeywordAddResponseDatum;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.model.cloud.request.keywordadd.AddKeywordRequest;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

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

    public Observable<Response<DataResponse<List<EditTopAdsKeywordDetailDataModel>>>> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDataModel dataModel) {
        return keywordApi.editTopAdsKeywordDetail(getEditTopAdsKeywordDetailModel(dataModel));
    }

    private DataRequest<List<TopAdsKeywordEditDetailInputDataModel>> getEditTopAdsKeywordDetailModel(TopAdsKeywordEditDetailInputDataModel dataModel) {
        DataRequest<List<TopAdsKeywordEditDetailInputDataModel>> dataRequest = new DataRequest<>();
        List<TopAdsKeywordEditDetailInputDataModel> list = new ArrayList<>();
        list.add(dataModel);
        dataRequest.setData(list);
        return dataRequest;
    }

    public Observable<PageDataResponse<List<KeywordAddResponseDatum>>> addKeyword(AddKeywordDomainModel addKeywordDomainModel) {
        AddKeywordRequest addKeywordRequest = KeywordAddDomainDataMapper.convertDomainToRequestData(addKeywordDomainModel);
        return keywordApi.addKeyword(addKeywordRequest);
    }
}
