package com.tokopedia.events.data;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.events.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import rx.functions.Func1;

/**
 * Created by pranaymohapatra on 15/02/18.
 */

public class SearchResponseMapper implements Func1<SearchResponse, SearchDomainModel> {
    @Override
    public SearchDomainModel call(SearchResponse searchResponseEntity) {
        CommonUtils.dumper("inside SearchResponseEntity = " + searchResponseEntity);
        return EventSearchMapper.getSearchMapper().convertToSearchDomain(searchResponseEntity);
    }
}
