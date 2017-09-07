package com.tokopedia.seller.common.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.common.repository.SellerLoginRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class SaveLoginTimeStampUseCase extends UseCase<Boolean> {
    private final SellerLoginRepository sellerLoginRepository;

    @Inject
    public SaveLoginTimeStampUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                     SellerLoginRepository sellerLoginRepository) {
        super(threadExecutor, postExecutionThread);
        this.sellerLoginRepository = sellerLoginRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return sellerLoginRepository.saveLoginTime();
    }

}
