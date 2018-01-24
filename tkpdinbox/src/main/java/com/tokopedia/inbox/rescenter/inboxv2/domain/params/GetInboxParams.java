package com.tokopedia.inbox.rescenter.inboxv2.domain.params;

import com.tokopedia.usecase.RequestParams;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxParams {

    public static final String PARAM_SORT_BY = "sortBy";
    public static final String PARAM_ASC = "asc";
    public static final String PARAM_FILTER = "filter";
    public static final String PARAM_START_DATE = "startDate";
    public static final String PARAM_END_DATE = "endDate";
    public static final String PARAM_START_ID = "startID";
    public static final String PARAM_LIMIT = "limit";

    public static RequestParams getParams(String startID, int sortBy, int asc, List<Integer> filters, String startDate, String endDate) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_LIMIT, 10);
        params.putString(PARAM_START_ID, startID);
        params.putInt(PARAM_SORT_BY, sortBy);
        params.putInt(PARAM_ASC, asc);
        String filterString = "";
        if (filters.size() != 0) {
            int pos = 0;
            for (int filter : filters) {
                filterString += String.valueOf(filter);
                if (pos == filters.size() - 1) filterString += ",";
                pos++;
            }
        }
        params.putString(PARAM_FILTER, filterString);
        params.putString(PARAM_START_DATE, startDate);
        params.putString(PARAM_END_DATE, endDate);
        return params;
    }

    public static RequestParams getEmptyParams() {
        return RequestParams.EMPTY;
    }
}
