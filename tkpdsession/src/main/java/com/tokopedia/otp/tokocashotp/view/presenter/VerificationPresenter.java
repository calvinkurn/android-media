package com.tokopedia.otp.tokocashotp.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.otp.tokocashotp.domain.interactor.RequestOtpTokoCashUseCase;
import com.tokopedia.otp.tokocashotp.domain.interactor.VerifyOtpTokoCashUseCase;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.subscriber.RequestOtpTokoCashSubscriber;
import com.tokopedia.otp.tokocashotp.view.subscriber.VerifyOtpTokoCashSubscriber;
import com.tokopedia.otp.tokocashotp.view.viewlistener.Verification;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpTokoCashUseCase requestTokoCashOTPUseCase;
    private final VerifyOtpTokoCashUseCase verifyTokoCashOTPUseCase;

    @Inject
    public VerificationPresenter(RequestOtpTokoCashUseCase requestTokoCashOTPUseCase,
                                 VerifyOtpTokoCashUseCase verifyTokoCashOTPUseCase) {
        this.requestTokoCashOTPUseCase = requestTokoCashOTPUseCase;
        this.verifyTokoCashOTPUseCase = verifyTokoCashOTPUseCase;
    }

    public void attachView(Verification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestTokoCashOTPUseCase.unsubscribe();
        verifyTokoCashOTPUseCase.unsubscribe();
    }

    @Override
    public void requestOTP(VerificationViewModel viewModel) {
        if (!TextUtils.isEmpty(viewModel.getPhoneNumber())) {
            getView().showLoadingProgress();
            int type = viewModel.getType();
            switch (type) {
                case VerificationActivity.TYPE_SMS:
                    requestTokoCashOTPUseCase.execute(RequestOtpTokoCashUseCase.getParam(viewModel
                            .getPhoneNumber(), RequestOtpTokoCashUseCase.TYPE_SMS), new
                            RequestOtpTokoCashSubscriber(getView()));
                    break;
                case VerificationActivity.TYPE_PHONE_CALL:
                    requestTokoCashOTPUseCase.execute(RequestOtpTokoCashUseCase.getParam(viewModel
                            .getPhoneNumber(), RequestOtpTokoCashUseCase.TYPE_PHONE), new
                            RequestOtpTokoCashSubscriber(getView()));
                    break;
            }
        }
    }

    @Override
    public void verifyOtp(String phoneNumber, String otpCode) {
        getView().showLoadingProgress();

//        getView().onSuccessVerifyOTP(new VerifyOtpTokoCashViewModel(
//                "asd",
//                true,
//                true,
//                getDummyList()
//        ));

        verifyTokoCashOTPUseCase.execute(VerifyOtpTokoCashUseCase.getParam(phoneNumber, otpCode), new
                VerifyOtpTokoCashSubscriber(getView()));
    }

    private ArrayList<AccountTokocash> getDummyList() {
        ArrayList<AccountTokocash> listAccount = new ArrayList<>();
        listAccount.add(new AccountTokocash(
                101,
                "Nisie 1",
                "nisie@nisie.com",
                "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/3/30/2590134/pic_2590134_16109006-d6a4-11e4-a5e2-d9ac4908a8c2.jpg"
        ));
        listAccount.add(new AccountTokocash(
                102,
                "Nisie 2",
                "nisie@nisie.com",
                "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/3/30/2590134/pic_2590134_16109006-d6a4-11e4-a5e2-d9ac4908a8c2.jpg"
        ));
        listAccount.add(new AccountTokocash(
                103,
                "Nisie 3",
                "nisie@nisie.com",
                "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/3/30/2590134/pic_2590134_16109006-d6a4-11e4-a5e2-d9ac4908a8c2.jpg"
        ));

        return listAccount;
    }
}
