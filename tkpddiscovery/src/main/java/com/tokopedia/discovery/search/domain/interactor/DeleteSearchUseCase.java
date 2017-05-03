package com.tokopedia.discovery.search.domain.interactor;

import android.content.Context;

import com.tokopedia.core.base.domain.UseCaseWithParams;
import com.tokopedia.discovery.search.domain.DeleteParam;

import retrofit2.Response;
import rx.Observable;

/**
 * @author erry on 01/03/17.
 */

public class DeleteSearchUseCase extends UseCaseWithParams<DeleteParam, Response<Void>> {

    private SearchDataInteractor dataFactory;

    public DeleteSearchUseCase(Context context) {
        this.dataFactory = new SearchDataInteractor(context);
    }

    @Override
    public Observable<Response<Void>> createObservable(DeleteParam requestParams) {
        return dataFactory.deleteRecentSearch(requestParams.getParam());
    }
}
