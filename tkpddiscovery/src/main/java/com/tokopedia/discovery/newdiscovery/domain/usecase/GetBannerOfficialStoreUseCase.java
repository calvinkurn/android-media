package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class GetBannerOfficialStoreUseCase extends UseCase<OfficialStoreBannerModel> {

    private final String KEYWORD = "keywords";
    private final BannerRepository bannerRepository;

    public GetBannerOfficialStoreUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         BannerRepository bannerRepository) {
        super(threadExecutor, postExecutionThread);
        this.bannerRepository = bannerRepository;
    }

    @Override
    public Observable<OfficialStoreBannerModel> createObservable(RequestParams requestParams) {
        return bannerRepository.getOfficialStoreBanner(requestParams.getString(KEYWORD, ""));
    }

    public RequestParams getRequestParam(String keywords) {
        RequestParams params = RequestParams.create();
        params.putString(KEYWORD, keywords);
        return params;
    }
}
