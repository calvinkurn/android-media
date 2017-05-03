package com.tokopedia.discovery.search.domain.interactor;

import android.content.Context;

import com.tokopedia.core.base.domain.UseCaseWithParams;
import com.tokopedia.discovery.search.domain.SearchParam;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;

import rx.Observable;

/**
 * @author erry on 23/02/17.
 */

public class SearchUseCase extends UseCaseWithParams<SearchParam, List<SearchData>> {

    private final SearchDataInteractor searchDataInteractor;

    public SearchUseCase(Context context) {
        this.searchDataInteractor = new SearchDataInteractor(context);
    }

    @Override
    protected Observable<List<SearchData>> createObservable(SearchParam requestParams) {
        return searchDataInteractor.getSearchData(requestParams.getParam());
    }
}
