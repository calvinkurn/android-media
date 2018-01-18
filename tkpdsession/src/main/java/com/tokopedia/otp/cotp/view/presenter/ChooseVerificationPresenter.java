package com.tokopedia.otp.cotp.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.cotp.domain.GetVerificationMethodListUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.session.R;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 1/18/18.
 */

public class ChooseVerificationPresenter extends BaseDaggerPresenter<SelectVerification.View>
        implements SelectVerification.Presenter {

    private final GetVerificationMethodListUseCase getVerificationMethodListUseCase;

    @Inject
    public ChooseVerificationPresenter(GetVerificationMethodListUseCase getVerificationMethodListUseCase) {
        this.getVerificationMethodListUseCase = getVerificationMethodListUseCase;
    }

    @Override
    public void getMethodList(String phoneNumber) {
        getView().showLoading();

        ArrayList<MethodItem> list = new ArrayList<>();
        list.add(new MethodItem(
                RequestOtpUseCase.MODE_SMS,
                "https://78.media.tumblr.com/avatar_c97800bfc163_128.png",
                MethodItem.getSmsMethodText(MethodItem.getMaskedPhoneNumber(phoneNumber)),
                "NGENG"
        ));
        list.add(new MethodItem(
                RequestOtpUseCase.MODE_CALL,
                "https://78.media.tumblr.com/avatar_c97800bfc163_128.png",
                MethodItem.getCallMethodText(MethodItem.getMaskedPhoneNumber(phoneNumber)),
                "NGENG2"
        ));

        getView().dismissLoading();
        ListVerificationMethod listVerificationMethod = new ListVerificationMethod(list);
        getView().onSuccessGetList(listVerificationMethod);

//        getVerificationMethodListUseCase.execute(GetVerificationMethodListUseCase
//                .getParam(phoneNumber), new Subscriber<ListVerificationMethod>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                getView().dismissLoading();
//                getView().onErrorGetList(ErrorHandler.getErrorMessage(e));
//            }
//
//            @Override
//            public void onNext(ListVerificationMethod listVerificationMethod) {
//                getView().dismissLoading();
//                if (listVerificationMethod.getList().isEmpty()) {
//                    getView().onErrorGetList("");
//                } else {
//                    getView().onSuccessGetList(listVerificationMethod);
//                }
//            }
//        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getVerificationMethodListUseCase.unsubscribe();
    }
}
