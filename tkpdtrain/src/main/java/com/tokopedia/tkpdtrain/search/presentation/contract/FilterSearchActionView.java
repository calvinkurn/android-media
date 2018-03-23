package com.tokopedia.tkpdtrain.search.presentation.contract;

import com.tokopedia.tkpdtrain.search.domain.FilterSearchData;

/**
 * Created by nabillasabbaha on 3/22/18.
 */

public interface FilterSearchActionView {

    FilterSearchData getFilterSearchData();

    void onChangeFilterSearchData(FilterSearchData filterSearchData);
}
