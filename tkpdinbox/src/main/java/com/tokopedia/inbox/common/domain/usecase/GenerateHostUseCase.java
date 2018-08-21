package com.tokopedia.inbox.common.domain.usecase;

import com.tokopedia.inbox.common.data.source.ResolutionCommonSource;
import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 30/07/18.
 */

public class GenerateHostUseCase extends UseCase<GenerateHostDomain> {
    public static final int STATIC_GOLANG_VALUE = 2;
    public static final String PARAM_SERVER_LANGUAGE = "new_add";

    private ResolutionCommonSource resolutionCommonSource;

    @Inject
    public GenerateHostUseCase(ResolutionCommonSource resolutionCommonSource) {
        this.resolutionCommonSource = resolutionCommonSource;
    }

    @Override
    public Observable<GenerateHostDomain> createObservable(RequestParams requestParams) {
        return resolutionCommonSource.generateHost(requestParams);
    }
}
