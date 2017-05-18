package com.tokopedia.seller.topads.keyword.view.data.mapper;

import com.tokopedia.seller.topads.keyword.view.data.model.KeywordDashBoard;
import com.tokopedia.seller.topads.keyword.view.domain.model.KeywordDashboardDomain;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardMapper implements Func1<Response<KeywordDashBoard>, KeywordDashboardDomain> {

    @Inject
    public KeywordDashboardMapper() {
    }

    @Override
    public KeywordDashboardDomain call(Response<KeywordDashBoard> keywordDashBoardResponse) {


        ResponseBody responseBody = keywordDashBoardResponse.errorBody();
        if (responseBody != null)
            throw new RuntimeException(responseBody.toString());

        KeywordDashBoard body = keywordDashBoardResponse.body();
        if (body != null) {
            if (body.getErrors() != null && body.getErrors().size() > 0) {
                throw new RuntimeException(body.getErrors().get(0).getDetail());
            }

            KeywordDashboardDomain keywordDashboardDomain =
                    new KeywordDashboardDomain();

            ArrayList<KeywordDashboardDomain.Datum> data = new ArrayList<>();
            for (KeywordDashBoard.Datum datum : body.getData()) {
                data.add(convertToDomain(datum));
            }

            keywordDashboardDomain.setData(data);

            keywordDashboardDomain.setPage(convertToDomain(body.getPage()));


            return keywordDashboardDomain;
        }

        return null;
    }

    private KeywordDashboardDomain.Page_ convertToDomain(KeywordDashBoard.Page_ page) {
        KeywordDashboardDomain.Page_ res = new KeywordDashboardDomain.Page_();

        res.setCurrent(page.getCurrent());
        res.setMax(page.getMax());
        res.setMin(page.getMin());
        res.setPerPage(page.getPerPage());
        res.setTotal(page.getTotal());

        return res;
    }


    private KeywordDashboardDomain.Datum convertToDomain(KeywordDashBoard.Datum datum) {
        KeywordDashboardDomain.Datum res = new KeywordDashboardDomain.Datum();
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

        return res;
    }


}
