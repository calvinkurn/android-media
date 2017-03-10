package com.tokopedia.session.changephonenumber.presenter;

import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberModel;
import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberRequestPass;
import com.tokopedia.session.changephonenumber.data.CheckStatusModel;
import com.tokopedia.session.changephonenumber.data.factory.KtpSourceFactory;
import com.tokopedia.session.changephonenumber.data.factory.UploadImageSourceFactory;
import com.tokopedia.session.changephonenumber.data.mapper.CheckStatusMapper;
import com.tokopedia.session.changephonenumber.data.mapper.GeneratePostKeyMapper;
import com.tokopedia.session.changephonenumber.data.mapper.GetUploadHostMapper;
import com.tokopedia.session.changephonenumber.data.mapper.UploadImageMapper;
import com.tokopedia.session.changephonenumber.data.repository.KtpRepositoryImpl;
import com.tokopedia.session.changephonenumber.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.session.changephonenumber.domain.interactor.CheckStatusUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.GeneratePostKeyUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.GetUploadHostUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.UploadChangePhoneNumberRequestUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.UploadImageUseCase;
import com.tokopedia.session.changephonenumber.listener.ChangePhoneNumberRequestView;

import rx.Subscriber;

/**
 * Created by nisie on 3/2/17.
 */

public class ChangePhoneNumberRequestPresenterImpl implements ChangePhoneNumberRequestPresenter {

    private final ChangePhoneNumberRequestView viewListener;
    private final CheckStatusUseCase checkStatusUseCase;
    private final UploadChangePhoneNumberRequestUseCase uploadChangePhoneNumberRequestUseCase;
    private ChangePhoneNumberRequestPass pass;

    public ChangePhoneNumberRequestPresenterImpl(ChangePhoneNumberRequestView viewListener) {
        this.viewListener = viewListener;
        this.pass = new ChangePhoneNumberRequestPass();

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
        String authKey = sessionHandler.getAccessToken(viewListener.getActivity());
        authKey = sessionHandler.getTokenType(viewListener.getActivity()) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);
        KtpRepositoryImpl ktpRepository = new KtpRepositoryImpl(
                new KtpSourceFactory(
                        viewListener.getActivity(),
                        accountsService,
                        new CheckStatusMapper()
                ));

        UploadImageService uploadImageService = new UploadImageService();

        UploadImageRepositoryImpl uploadImageRepository = new UploadImageRepositoryImpl(
                new UploadImageSourceFactory(
                        viewListener.getActivity(),
                        accountsService,
                        uploadImageService,
                        new GetUploadHostMapper(),
                        new GeneratePostKeyMapper(),
                        new UploadImageMapper()
                )
        );

        GetUploadHostUseCase getUploadHostUseCase = new GetUploadHostUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        UploadImageUseCase uploadImageUseCase = new UploadImageUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        GeneratePostKeyUseCase generatePostKeyUseCase = new GeneratePostKeyUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        this.checkStatusUseCase = new CheckStatusUseCase(
                new JobExecutor(), new UIThread(), ktpRepository);

        this.uploadChangePhoneNumberRequestUseCase = new UploadChangePhoneNumberRequestUseCase(
                new JobExecutor(), new UIThread(),
                uploadImageRepository, getUploadHostUseCase,
                uploadImageUseCase, generatePostKeyUseCase
        );
    }

    @Override
    public void submitRequest() {
        if (isValid()) {
            viewListener.showLoading();
            uploadChangePhoneNumberRequestUseCase.execute(getUploadChangePhoneNumberRequestParam(),
                    new Subscriber<ChangePhoneNumberModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            viewListener.onErrorSubmitRequest("WALALALA");
                        }

                        @Override
                        public void onNext(ChangePhoneNumberModel changePhoneNumberModel) {
                            CommonUtils.dumper("NISNIS " + changePhoneNumberModel);
                            viewListener.onErrorSubmitRequest("WALALA");
                        }
                    });
        }
    }

    private RequestParams getUploadChangePhoneNumberRequestParam() {
        RequestParams params = RequestParams.create();
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_USER_ID,
                SessionHandler.getTempLoginSession(viewListener.getActivity()));
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_DEVICE_ID,
                GCMHandler.getRegistrationId(viewListener.getActivity()));
        setParamGetUploadHost(params);
        setParamUploadIdImage(params);
        setParamUploadBankBookImage(params);
        return params;
    }

    private void setParamUploadBankBookImage(RequestParams params) {
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_BANK_BOOK_IMAGE_PATH,
                pass.getUploadBankBookPath());
    }

    private void setParamUploadIdImage(RequestParams params) {

        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_KTP_IMAGE_PATH,
                pass.getUploadIdPath());
    }

    private void setParamGetUploadHost(RequestParams params) {
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_NEW_ADD,
                UploadChangePhoneNumberRequestUseCase.DEFAULT_NEW_ADD);
    }

    private boolean isValid() {
        return pass != null;
    }

    @Override
    public void checkStatus() {
        viewListener.showLoading();
        checkStatusUseCase.execute(getCheckStatusParam(), new Subscriber<CheckStatusModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.onErrorcheckStatus(e.toString());

            }

            @Override
            public void onNext(CheckStatusModel checkStatusModel) {
                if (checkStatusModel.isSuccess() &&
                        checkStatusModel.getCheckStatusData().isSuccess()
                        && checkStatusModel.getErrorMessage() == null
                        && checkStatusModel.getStatusMessage() != null) {
                    viewListener.onSuccessCheckStatus(checkStatusModel.getCheckStatusData());
                } else if (checkStatusModel.getErrorMessage() != null) {
                    viewListener.onErrorcheckStatus(checkStatusModel.getErrorMessage());
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            viewListener.onErrorcheckStatus(viewListener.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            viewListener.onErrorcheckStatus(viewListener.getString(R.string.default_request_error_timeout));
                        }

                        @Override
                        public void onServerError() {
                            viewListener.onErrorcheckStatus(viewListener.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            viewListener.onErrorcheckStatus(viewListener.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            viewListener.onErrorcheckStatus(viewListener.getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, checkStatusModel.getResponseCode());
                }
            }
        });
    }

    @Override
    public void setIdPath(String idPath) {
        pass.setUploadIdPath(idPath);
    }

    @Override
    public void setBankBookPath(String bankBookPath) {
        pass.setUploadBankBookPath(bankBookPath);
    }

    private RequestParams getCheckStatusParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CheckStatusUseCase.PARAM_USER_ID,
                SessionHandler.getTempLoginSession(viewListener.getActivity()));
        return requestParams;
    }
}
