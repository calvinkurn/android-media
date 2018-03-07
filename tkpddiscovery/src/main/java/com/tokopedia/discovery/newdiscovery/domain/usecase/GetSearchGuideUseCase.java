package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.discovery.newdiscovery.data.repository.GuidedSearchRepository;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GetSearchGuideUseCase extends UseCase<GuidedSearchViewModel> {

    public static final String PARAM_QUERY = "PARAM_QUERY";

    private GuidedSearchRepository guidedSearchRepository;

    public GetSearchGuideUseCase(GuidedSearchRepository guidedSearchRepository) {
        this.guidedSearchRepository = guidedSearchRepository;
    }

    @Override
    public Observable<GuidedSearchViewModel> createObservable(RequestParams requestParams) {
        return guidedSearchRepository.getGuidedSearch(requestParams.getString(PARAM_QUERY, ""));
    }
}
