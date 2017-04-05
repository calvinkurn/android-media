package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.domain.model.MyEtalaseListDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */
public class FetchMyEtalaseUseCase extends UseCase<MyEtalaseListDomainModel>{
    private final MyEtalaseRepository myEtalaseRepository;

    @Inject
    public FetchMyEtalaseUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, MyEtalaseRepository myEtalaseRepository) {
        super(threadExecutor, postExecutionThread);
        this.myEtalaseRepository = myEtalaseRepository;
    }

    @Override
    public Observable<MyEtalaseListDomainModel> createObservable(RequestParams requestParams) {
        return myEtalaseRepository.fetchMyEtalase();
    }
}
