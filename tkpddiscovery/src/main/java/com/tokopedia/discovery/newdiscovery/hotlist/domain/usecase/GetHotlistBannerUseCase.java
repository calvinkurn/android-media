package com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBannerModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/6/17.
 */

public class GetHotlistBannerUseCase extends UseCase<HotlistBannerModel> {

    private final BannerRepository bannerRepository;

    public GetHotlistBannerUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            BannerRepository bannerRepository) {
        super(threadExecutor, postExecutionThread);
        this.bannerRepository = bannerRepository;
    }

    @Override
    public Observable<HotlistBannerModel> createObservable(RequestParams requestParams) {
        return bannerRepository.getHotlistBanner(requestParams.getParameters());
    }
}
