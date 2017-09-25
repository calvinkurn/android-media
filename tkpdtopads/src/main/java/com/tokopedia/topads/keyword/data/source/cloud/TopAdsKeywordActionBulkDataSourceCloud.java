package com.tokopedia.topads.keyword.data.source.cloud;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.Keyword;
import com.tokopedia.topads.keyword.data.source.cloud.api.KeywordApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordActionBulkDataSourceCloud {

    private final KeywordApi keywordApi;

    @Inject
    public TopAdsKeywordActionBulkDataSourceCloud(KeywordApi keywordApi) {
        this.keywordApi = keywordApi;
    }

    public Observable<PageDataResponse<DataBulkKeyword>> actionBulk(RequestParams requestParams) {
        DataRequest<DataBulkKeyword> dataRequest = createDataRequest(requestParams);
        return keywordApi.actionBulkKeyword(dataRequest).map(new GetData<PageDataResponse<DataBulkKeyword>>());
    }

    private DataRequest<DataBulkKeyword> createDataRequest(RequestParams requestParams) {
        Keyword keyword = new Keyword();
        keyword.setGroupId(requestParams.getString(TopAdsNetworkConstant.PARAM_GROUP_ID, ""));
        keyword.setKeywordId(requestParams.getString(TopAdsNetworkConstant.PARAM_KEYWORD_AD_ID, ""));

        List<Keyword> keywords = new ArrayList<>();
        keywords.add(keyword);

        DataBulkKeyword dataBulkKeyword = new DataBulkKeyword();
        dataBulkKeyword.setShopId(requestParams.getString(TopAdsNetworkConstant.PARAM_SHOP_ID, ""));
        dataBulkKeyword.setAction(requestParams.getString(TopAdsNetworkConstant.PARAM_ACTION, ""));
        dataBulkKeyword.setKeyword(keywords);
        return new DataRequest<>(dataBulkKeyword);
    }
}