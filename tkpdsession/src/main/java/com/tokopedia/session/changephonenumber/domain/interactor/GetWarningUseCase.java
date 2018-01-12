package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.data.CheckStatusModel;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningUseCase extends UseCase<WarningViewModel> {
    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public GetWarningUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ChangePhoneNumberRepository changePhoneNumberRepository) {
        super(threadExecutor, postExecutionThread);
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<WarningViewModel> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.getWarning(requestParams.getParameters());
    }
}
