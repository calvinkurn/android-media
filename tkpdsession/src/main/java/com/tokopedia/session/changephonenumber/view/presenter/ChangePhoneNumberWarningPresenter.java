package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateOtpStatusUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.GetWarningSubscriber;
import com.tokopedia.session.changephonenumber.view.subscriber.ValidateOtpStatusSubscriber;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberWarningFragmentListener.View>
        implements ChangePhoneNumberWarningFragmentListener.Presenter {

    private final GetWarningUseCase getWarningUseCase;
    private final ValidateOtpStatusUseCase getValidateOtpStatusUseCase;
    private ChangePhoneNumberWarningFragmentListener.View view;

    public ChangePhoneNumberWarningPresenter(GetWarningUseCase getWarningUseCase,
                                             ValidateOtpStatusUseCase validateOtpStatusUseCase) {
        this.getWarningUseCase = getWarningUseCase;
        this.getValidateOtpStatusUseCase = validateOtpStatusUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getWarningUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void initView() {
//        validateOtpStatus(getView().getUserId());
        getWarning();
    }

    @Override
    public void getWarning() {
        view.showLoading();
        getWarningUseCase.execute(GetWarningUseCase.getGetWarningParam(),
                new GetWarningSubscriber(view));
    }

    @Override
    public void validateOtpStatus(int userId) {
        view.showLoading();
        getValidateOtpStatusUseCase.execute(ValidateOtpStatusUseCase.
                getValidateOtpParam(userId, RequestOtpUseCase.
                        OTP_TYPE_VERIFY_USER_CHANGE_PHONE_NUMBER),
                new ValidateOtpStatusSubscriber(view));
    }
}
