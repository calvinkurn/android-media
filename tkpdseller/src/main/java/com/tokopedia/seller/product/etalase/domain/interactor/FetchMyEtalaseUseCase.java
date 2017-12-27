package com.tokopedia.seller.product.etalase.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.etalase.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.etalase.domain.model.MyEtalaseDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */
public class FetchMyEtalaseUseCase extends UseCase<MyEtalaseDomainModel>{
    public static final String ETALASE_PAGING = "ETALASE_PAGING";
    private final MyEtalaseRepository myEtalaseRepository;

    @Inject
    public FetchMyEtalaseUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, MyEtalaseRepository myEtalaseRepository) {
        super(threadExecutor, postExecutionThread);
        this.myEtalaseRepository = myEtalaseRepository;
    }

    @Override
    public Observable<MyEtalaseDomainModel> createObservable(RequestParams requestParams) {
        int page = requestParams.getInt(ETALASE_PAGING, 1);
        return myEtalaseRepository.fetchMyEtalase(page);
    }

    public static RequestParams generateRequestParam(int page) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putInt(ETALASE_PAGING, page);
        return requestParam;
    }
}
