package com.tokopedia.inbox.rescenter.inboxv2.domain.params;

import android.text.TextUtils;

import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortFilterModel;
import com.tokopedia.usecase.RequestParams;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxParams {

    public static final String PARAM_SORT_BY = "sortBy";
    public static final String PARAM_ASC = "asc";
    public static final String PARAM_FILTER = "filter";
    public static final String PARAM_START_DATE = "startTime";
    public static final String PARAM_END_DATE = "endTime";
    public static final String PARAM_START_ID = "startID";
    public static final String PARAM_LIMIT = "limit";
    public static final int PARAM_LIMIT_COUNT = 10;

//    public static RequestParams getParams(String startID, int sortBy, int asc, List<Integer> filters, String startDate, String endDate) {
//        RequestParams params = RequestParams.create();
//        params.putInt(PARAM_LIMIT, PARAM_LIMIT_COUNT);
//        params.putString(PARAM_START_ID, startID);
//        params.putInt(PARAM_SORT_BY, sortBy);
//        params.putInt(PARAM_ASC, asc);
//        String filterString = "";
//        if (filters.size() != 0) {
//            int pos = 0;
//            for (int filter : filters) {
//                filterString += String.valueOf(filter);
//                if (pos == filters.size() - 1) filterString += ",";
//                pos++;
//            }
//        }
//        params.putString(PARAM_FILTER, filterString);
//        params.putString(PARAM_START_DATE, startDate);
//        params.putString(PARAM_END_DATE, endDate);
//        return params;
//    }

    public static RequestParams getParams(ResoInboxSortFilterModel model) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_LIMIT, PARAM_LIMIT_COUNT);
        params.putString(PARAM_START_ID, model.getStartID());
        params.putInt(PARAM_SORT_BY, model.getSortBy());
        params.putInt(PARAM_ASC, model.getAsc());
        params.putString(PARAM_START_DATE, model.getStartDateString());
        params.putString(PARAM_END_DATE, model.getEndDateString());
        String filterString = "";
        if (model.getFilters().size() != 0) {
            int pos = 0;
            for (int filter : model.getFilters()) {
                filterString += String.valueOf(filter);
                if (pos != model.getFilters().size() - 1) filterString += ",";
                pos++;
            }
        }
        if (!TextUtils.isEmpty(model.getStartDateString())
                || !TextUtils.isEmpty(model.getEndDateString())) {
            if (!TextUtils.isEmpty(filterString)) {
                filterString += ",4";
            } else {
                filterString += "4";
            }
        }
        params.putString(PARAM_FILTER, filterString);
        return params;
    }

    public static RequestParams getEmptyParams() {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_LIMIT, PARAM_LIMIT_COUNT);
        return RequestParams.EMPTY;
    }
}
