package com.tokopedia.otp.cotp.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.cotp.domain.GetVerificationMethodListUseCase;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 1/18/18.
 */

public class ChooseVerificationPresenter extends BaseDaggerPresenter<SelectVerification.View>
        implements SelectVerification.Presenter {

    private final GetVerificationMethodListUseCase getVerificationMethodListUseCase;
    private final SessionHandler sessionHandler;

    @Inject
    public ChooseVerificationPresenter(SessionHandler sessionHandler,
                                       GetVerificationMethodListUseCase
                                               getVerificationMethodListUseCase) {
        this.getVerificationMethodListUseCase = getVerificationMethodListUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void getMethodList(String phoneNumber, int otpType) {
        getView().showLoading();

        String userId = sessionHandler.isV4Login() ? sessionHandler.getLoginID() : sessionHandler
                .getTempLoginSession();
        getVerificationMethodListUseCase.execute(GetVerificationMethodListUseCase
                .getParam(phoneNumber,
                        otpType,
                        userId), new Subscriber<ListVerificationMethod>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoading();
                getView().onErrorGetList(ErrorHandler.getErrorMessage(e));
            }

            @Override
            public void onNext(ListVerificationMethod listVerificationMethod) {
                getView().dismissLoading();
                if (listVerificationMethod.getList().isEmpty()) {
                    getView().onErrorGetList("");
                } else {
                    getView().onSuccessGetList(listVerificationMethod);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getVerificationMethodListUseCase.unsubscribe();
    }
}
