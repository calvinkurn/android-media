package com.tokopedia.seller.product.etalase.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.etalase.domain.MyEtalaseRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/6/17.
 */

public class AddNewEtalaseUseCase extends UseCase<Boolean>{

    public static final String NEW_ETALASE_NAME = "NEW_ETALASE_NAME";
    private final MyEtalaseRepository myEtalaseRepository;

    @Inject
    public AddNewEtalaseUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                MyEtalaseRepository myEtalaseRepository) {
        super(threadExecutor, postExecutionThread);
        this.myEtalaseRepository = myEtalaseRepository;
    }

    public static RequestParams generateRequestParam(String newEtalaseName) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putString(NEW_ETALASE_NAME, newEtalaseName);
        return requestParam;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String etalaseName = requestParams.getString(NEW_ETALASE_NAME, "");
        if (etalaseName.isEmpty()){
            throw new RuntimeException("Adding etalase name with empty name");
        }
        return Observable.just(etalaseName)
                .flatMap(new AddNewEtalase());
    }

    private class AddNewEtalase implements Func1<String, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(String newEtalaseName) {
            return myEtalaseRepository.addNewEtalase(newEtalaseName);
        }
    }

}
