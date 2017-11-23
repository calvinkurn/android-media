package com.tokopedia.topads.keyword.view.model;

import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef;

/**
 * Created by normansyahputa on 5/23/17.
 */

public class BaseKeywordParam {
    public long startDate;
    public long endDate;
    public String startDateDesc;
    public String endDateDesc;

    public String shopId;
    public String keywordTag;
    public int page;

    // filtering
    public long groupId;
    @KeywordStatusTypeDef
    public int keywordStatus;
    public int keywordTypeId;
    public int sortingParam;
    public boolean isPositive;
    public long keywordId;

    public int isPositive() {
        return isPositive ? 1 : 0;
    }
}
