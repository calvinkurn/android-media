package com.tokopedia.seller.topads.keyword.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.model.request.DataRequest;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.seller.topads.keyword.data.model.cloud.bulkkeyword.Keyword;
import com.tokopedia.seller.topads.keyword.data.source.cloud.api.KeywordApi;

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
        return keywordApi.actionBulkKeyword(dataRequest);
    }

    private DataRequest<DataBulkKeyword> createDataRequest(RequestParams requestParams) {
        DataBulkKeyword dataBulkKeyword = new DataBulkKeyword();
        dataBulkKeyword.setShopId(requestParams.getString(TopAdsNetworkConstant.PARAM_SHOP_ID, ""));
        dataBulkKeyword.setAction(requestParams.getString(TopAdsNetworkConstant.PARAM_ACTION, ""));

        List<Keyword> keywords = new ArrayList<>();
        Keyword keyword = new Keyword();
        keyword.setGroupId(requestParams.getString(TopAdsNetworkConstant.PARAM_GROUP_ID, ""));
        keyword.setKeywordId(requestParams.getString(TopAdsNetworkConstant.PARAM_KEYWORD_AD_ID, ""));
        keywords.add(keyword);
        dataBulkKeyword.setKeyword(keywords);
        return new DataRequest<>(dataBulkKeyword);
    }
}
