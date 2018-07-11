package com.tokopedia.session.register.domain.interactor;

import com.tokopedia.session.register.data.source.RegisterValidationSource;
import com.tokopedia.session.register.view.viewmodel.RegisterValidationViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationUseCase extends UseCase<RegisterValidationViewModel> {

    public static final String PARAM_ID = "id";

    private final RegisterValidationSource registerValidationSource;

    @Inject
    public RegisterValidationUseCase(RegisterValidationSource registerValidationSource) {
        this.registerValidationSource = registerValidationSource;
    }

    @Override
    public Observable<RegisterValidationViewModel> createObservable(RequestParams requestParams) {
        return registerValidationSource.validateRegister(requestParams.getParameters());
    }
}
