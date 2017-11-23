package com.tokopedia.topads.keyword.data.mapper;

import com.tokopedia.topads.dashboard.data.model.data.Page;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardMapper implements Func1<PageDataResponse<List<Datum>>, KeywordDashboardDomain> {

    @Inject
    public KeywordDashboardMapper() {
    }

    private com.tokopedia.topads.keyword.domain.model.Page convertToDomain(Page page) {
        com.tokopedia.topads.keyword.domain.model.Page res =
                new com.tokopedia.topads.keyword.domain.model.Page();
        res.setCurrent(page.getCurrent());
        res.setMax(page.getMax());
        res.setMin(page.getMin());
        res.setPerPage(page.getPerPage());
        res.setTotal(page.getTotal());
        return res;
    }

    private com.tokopedia.topads.keyword.domain.model.Datum convertToDomain(Datum datum) {
        com.tokopedia.topads.keyword.domain.model.Datum res =
                new com.tokopedia.topads.keyword.domain.model.Datum();
        res.setKeywordId(datum.getKeywordId());//1
        res.setKeywordTag(datum.getKeywordTag());//2
        res.setGroupId(datum.getGroupId());//3
        res.setGroupName(datum.getGroupName());//4
        res.setKeywordStatus(datum.getKeywordStatus());// 5
        res.setKeywordStatusDesc(datum.getKeywordStatusDesc());//6
        res.setKeywordTypeId(datum.getKeywordTypeId());//7
        res.setKeywordTypeDesc(datum.getKeywordTypeDesc());//8
        res.setKeywordStatusToogle(datum.getKeywordStatusToogle());//9
        res.setKeywordPriceBidFmt(datum.getKeywordPriceBidFmt());//10
        res.setStatAvgClick(datum.getStatAvgClick());//11
        res.setStatTotalSpent(datum.getStatTotalSpent());//12
        res.setStatTotalImpression(datum.getStatTotalImpression());
        res.setStatTotalClick(datum.getStatTotalClick());
        res.setStatTotalCtr(datum.getStatTotalCtr());
        res.setStatTotalConversion(datum.getStatTotalConversion());
        res.setLabelEdit(datum.getLabelEdit());//17
        res.setLabelPerClick(datum.getLabelPerClick());//18
        res.setLabelOf(datum.getLabelOf());//19
        res.setGroupBid(datum.getGroupBid());
        return res;
    }

    @Override
    public KeywordDashboardDomain call(PageDataResponse<List<Datum>> listPageDataResponse) {
        KeywordDashboardDomain keywordDashboardDomain = new KeywordDashboardDomain();
        ArrayList<com.tokopedia.topads.keyword.domain.model.Datum> data = new ArrayList<>();
        for (Datum datum : listPageDataResponse.getData()) {
            data.add(convertToDomain(datum));
        }
        keywordDashboardDomain.setData(data);
        keywordDashboardDomain.setPage(convertToDomain(listPageDataResponse.getPage()));
        return keywordDashboardDomain;
    }
}