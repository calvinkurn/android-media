package com.tokopedia.session.register.registerphonenumber.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by yfsx on 13/03/18.
 */

public class LoginRegisterPhoneNumberUseCase extends UseCase<LoginRegisterPhoneNumberModel> {
    private RegisterPhoneNumberUseCase registerPhoneNumberUseCase;
    private GetUserInfoUseCase getUserInfoUseCase;
    private MakeLoginUseCase makeLoginUseCase;

    @Inject
    public LoginRegisterPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           RegisterPhoneNumberUseCase registerPhoneNumberUseCase,
                                           GetUserInfoUseCase getUserInfoUseCase,
                                           MakeLoginUseCase makeLoginUseCase) {
        super(threadExecutor, postExecutionThread);
        this.registerPhoneNumberUseCase = registerPhoneNumberUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<LoginRegisterPhoneNumberModel> createObservable(RequestParams requestParams) {
        LoginRegisterPhoneNumberModel loginRegisterPhoneNumberModel = new LoginRegisterPhoneNumberModel();
        return getObservableRegisterPhoneNumber(requestParams, loginRegisterPhoneNumberModel)
                .flatMap(getObservableGetInfo(loginRegisterPhoneNumberModel))
                .flatMap(addGetInfoResult(loginRegisterPhoneNumberModel))
                .flatMap(getObservableMakeLogin(loginRegisterPhoneNumberModel))
                .flatMap(addMakeLoginResult(loginRegisterPhoneNumberModel));
    }

    private Observable<LoginRegisterPhoneNumberModel> getObservableRegisterPhoneNumber(
            RequestParams requestParams, final LoginRegisterPhoneNumberModel viewModel) {
        return registerPhoneNumberUseCase.createObservable(requestParams)
                .flatMap(addRegisterResultToModel(viewModel));
    }

    private Func1<RegisterPhoneNumberModel, Observable<LoginRegisterPhoneNumberModel>>
    addRegisterResultToModel(final LoginRegisterPhoneNumberModel viewModel) {
        return new Func1<RegisterPhoneNumberModel, Observable<LoginRegisterPhoneNumberModel>>() {
            @Override
            public Observable<LoginRegisterPhoneNumberModel> call(RegisterPhoneNumberModel registerPhoneNumberModel) {
                viewModel.setRegisterPhoneNumberModel(registerPhoneNumberModel);
                return Observable.just(viewModel);
            }
        };
    }

    private Func1<LoginRegisterPhoneNumberModel, Observable<GetUserInfoDomainModel>>
    getObservableGetInfo(LoginRegisterPhoneNumberModel viewModel) {
        return new Func1<LoginRegisterPhoneNumberModel, Observable<GetUserInfoDomainModel>>() {
            @Override
            public Observable<GetUserInfoDomainModel> call(LoginRegisterPhoneNumberModel viewModel1) {
                return getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam());
            }
        };
    }

    private Func1<GetUserInfoDomainModel, Observable<LoginRegisterPhoneNumberModel>>
    addGetInfoResult(final LoginRegisterPhoneNumberModel viewModel) {
        return new Func1<GetUserInfoDomainModel, Observable<LoginRegisterPhoneNumberModel>> (){
            @Override
            public Observable<LoginRegisterPhoneNumberModel> call(GetUserInfoDomainModel getUserInfoDomainModel) {
                viewModel.setGetUserInfoDomainModel(getUserInfoDomainModel);
                return Observable.just(viewModel);
            }
        };
    }

    private Func1<LoginRegisterPhoneNumberModel, Observable<MakeLoginDomain>>
    getObservableMakeLogin(LoginRegisterPhoneNumberModel viewModel) {
        return new Func1<LoginRegisterPhoneNumberModel, Observable<MakeLoginDomain>>() {
            @Override
            public Observable<MakeLoginDomain> call(LoginRegisterPhoneNumberModel viewModel1) {
                return makeLoginUseCase.createObservable(MakeLoginUseCase.getParam(
                        String.valueOf(
                                viewModel1.getRegisterPhoneNumberModel().getRegisterPhoneNumberData().getuId()))
                );
            }
        };
    }

    private Func1<MakeLoginDomain, Observable<LoginRegisterPhoneNumberModel>>
    addMakeLoginResult(final LoginRegisterPhoneNumberModel viewModel) {
        return new Func1<MakeLoginDomain, Observable<LoginRegisterPhoneNumberModel>> (){
            @Override
            public Observable<LoginRegisterPhoneNumberModel> call(MakeLoginDomain makeLoginDomain) {
                viewModel.setMakeLoginDomain(makeLoginDomain);
                return Observable.just(viewModel);
            }
        };
    }
}
