package com.tokopedia.discovery.newdiscovery.helper;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.R;

public class GqlSearchQueryHelper {

    private Context context;

    public GqlSearchQueryHelper(Context context) {
        this.context = context;
    }

    public String getInitiateSearchQuery() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_initiate_search);
    }
}
