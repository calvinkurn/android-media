package com.tokopedia.session.changephonenumber.presenter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.ErrorMessageException;
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
import com.tokopedia.session.changephonenumber.data.mapper.SubmitImageMapper;
import com.tokopedia.session.changephonenumber.data.mapper.ValidateImageMapper;
import com.tokopedia.session.changephonenumber.data.mapper.GetUploadHostMapper;
import com.tokopedia.session.changephonenumber.data.mapper.UploadImageMapper;
import com.tokopedia.session.changephonenumber.data.repository.KtpRepositoryImpl;
import com.tokopedia.session.changephonenumber.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.session.changephonenumber.domain.interactor.CheckStatusUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.SubmitImageUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateImageUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.GetUploadHostUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.UploadChangePhoneNumberRequestUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.UploadImageUseCase;
import com.tokopedia.session.changephonenumber.listener.ChangePhoneNumberRequestView;

import java.io.File;
import java.net.UnknownHostException;

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
                        new ValidateImageMapper(),
                        new UploadImageMapper(),
                        new SubmitImageMapper()
                )
        );

        GetUploadHostUseCase getUploadHostUseCase = new GetUploadHostUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        UploadImageUseCase uploadImageUseCase = new UploadImageUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        ValidateImageUseCase validateImageUseCase = new ValidateImageUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        SubmitImageUseCase submitImageUseCase = new SubmitImageUseCase(
                new JobExecutor(), new UIThread(), uploadImageRepository
        );

        this.checkStatusUseCase = new CheckStatusUseCase(
                new JobExecutor(), new UIThread(), ktpRepository);

        this.uploadChangePhoneNumberRequestUseCase = new UploadChangePhoneNumberRequestUseCase(
                new JobExecutor(), new UIThread(),
                uploadImageRepository, getUploadHostUseCase,
                uploadImageUseCase, validateImageUseCase,
                submitImageUseCase
        );
    }

    @Override
    public void submitRequest() {
        if (isValidParam()) {
            viewListener.showLoading();
            uploadChangePhoneNumberRequestUseCase.execute(getUploadChangePhoneNumberRequestParam(),
                    new Subscriber<ChangePhoneNumberModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof UnknownHostException) {
                                viewListener.onErrorSubmitRequest(
                                        viewListener.getString(R.string.msg_no_connection));
                            } else if (e instanceof RuntimeException &&
                                    e.getLocalizedMessage() != null &&
                                    e.getLocalizedMessage().length() <= 3) {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        viewListener.onErrorSubmitRequest(
                                                viewListener.getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        viewListener.onErrorSubmitRequest(
                                                viewListener.getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        viewListener.onErrorSubmitRequest(
                                                viewListener.getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        viewListener.onErrorSubmitRequest(
                                                viewListener.getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        viewListener.onErrorSubmitRequest(
                                                viewListener.getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, Integer.parseInt(e.getLocalizedMessage()));
                            } else if (e instanceof ErrorMessageException &&
                                    e.getLocalizedMessage() != null) {
                                viewListener.onErrorSubmitRequest(e.getLocalizedMessage());
                            } else {
                                viewListener.onErrorSubmitRequest(
                                        viewListener.getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(ChangePhoneNumberModel changePhoneNumberModel) {
                            if (changePhoneNumberModel.isSuccess()) {
                                viewListener.onSuccessSubmitRequest();
                            } else {
                                viewListener.onErrorSubmitRequest(changePhoneNumberModel.getErrorMessage());
                            }
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
        setParamValidateImage(params);
        setParamGetUploadHost(params);
        setParamUploadIdImage(params);
        setParamUploadBankBookImage(params);
        return params;
    }

    private void setParamValidateImage(RequestParams params) {
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_ID_HEIGHT,
                pass.getIdHeight());
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_ID_WIDTH,
                pass.getIdWidth());
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_BANKBOOK_HEIGHT,
                pass.getBankBookHeight());
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_BANKBOOK_WIDTH,
                pass.getBankBookWidth());
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

    @Override
    public boolean isValidParam() {
        return pass.getUploadBankBookPath() != null && pass.getUploadIdPath() != null;
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
    public void setIdImage(String idPath) {
        pass.setUploadIdPath(idPath);
        setIdImageHeightAndWidth(pass, idPath);
    }

    private void setIdImageHeightAndWidth(ChangePhoneNumberRequestPass pass, String idPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(Uri.parse(idPath).getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        pass.setIdHeight(String.valueOf(imageHeight));
        pass.setIdWidth(String.valueOf(imageWidth));
    }

    @Override
    public void setBankBookImage(String bankBookPath) {
        pass.setUploadBankBookPath(bankBookPath);
        setBankBookImageHeightAndWidth(pass, bankBookPath);

    }

    private void setBankBookImageHeightAndWidth(ChangePhoneNumberRequestPass pass, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(Uri.parse(path).getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        pass.setBankBookHeight(String.valueOf(imageHeight));
        pass.setBankBookWidth(String.valueOf(imageWidth));
    }


    private RequestParams getCheckStatusParam() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
