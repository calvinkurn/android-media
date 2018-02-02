package com.tokopedia.session.changephonenumber.view.presenter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.securityquestion.data.factory.KtpSourceFactory;
import com.tokopedia.otp.securityquestion.data.factory.UploadImageSourceFactory;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.ChangePhoneNumberRequestModel;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.ChangePhoneNumberRequestPass;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.CheckStatusModel;
import com.tokopedia.otp.securityquestion.data.repository.KtpRepositoryImpl;
import com.tokopedia.otp.securityquestion.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest.CheckStatusUseCase;
import com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest.GetUploadHostUseCase;
import com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest.SubmitImageUseCase;
import com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest.UploadChangePhoneNumberRequestUseCase;
import com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest.UploadImageUseCase;
import com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest.ValidateImageUseCase;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.CheckStatusMapper;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.GetUploadHostMapper;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.SubmitImageMapper;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.UploadImageMapper;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.ValidateImageMapper;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberRequestView;

import java.io.File;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 3/2/17.
 */

public class ChangePhoneNumberRequestPresenterImpl implements ChangePhoneNumberRequestPresenter {

    private static final int MINIMUM_NUMBER_LENGTH = 7;
    private static final int MAXIMUM_NUMBER_LENGTH = 15;
    private final ChangePhoneNumberRequestView viewListener;
    private final CheckStatusUseCase checkStatusUseCase;
    private final UploadChangePhoneNumberRequestUseCase uploadChangePhoneNumberRequestUseCase;
    private ChangePhoneNumberRequestPass pass;
    private String phoneNumber;

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
    public void continueToNext() {
        if (isValidParam()) {
            viewListener.showLoading();
            viewListener.onSuccessValidRequest();
        }
    }

    @Override
    public void submitRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        if (isValidParam()) {
            viewListener.showLoading();
            //viewListener.onSuccessValidRequest();
            uploadChangePhoneNumberRequestUseCase.execute(getUploadChangePhoneNumberRequestParam(),
                    new Subscriber<ChangePhoneNumberRequestModel>() {
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
                        public void onNext(ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
                            if (changePhoneNumberRequestModel.isSuccess()) {
                                viewListener.onSuccessSubmitRequest();
                            } else {
                                viewListener.onErrorSubmitRequest(changePhoneNumberRequestModel.getErrorMessage());
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
        if(pass.getUploadBankBookPath() != null){
            setParamUploadBankBookImage(params);
        }
        setParamPhoneNumber(params);
        return params;
    }

    private void setParamPhoneNumber(RequestParams params) {
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_PHONE_NUMBER,
                phoneNumber);
    }

    private void setParamValidateImage(RequestParams params) {
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_ID_HEIGHT,
                pass.getIdHeight());
        params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_ID_WIDTH,
                pass.getIdWidth());
        Log.e("sandeep","path="+pass.getUploadBankBookPath());
        Log.e("sandeep","height="+pass.getBankBookHeight());
        Log.e("sandeep","width="+pass.getBankBookWidth());
        if(pass.getUploadBankBookPath() != null) {
            params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_BANKBOOK_HEIGHT,
                    pass.getBankBookHeight());
            params.putString(UploadChangePhoneNumberRequestUseCase.PARAM_BANKBOOK_WIDTH,
                    pass.getBankBookWidth());
        }
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
        return /*pass.getUploadBankBookPath() != null && */pass.getUploadIdPath() != null;
    }

    @Override
    public void onDestroyView() {
        checkStatusUseCase.unsubscribe();
        uploadChangePhoneNumberRequestUseCase.unsubscribe();
    }

    @Override
    public void onNewNumberTextChanged(Editable editable, int selection) {
        String newNumber = editable.toString().replaceAll("\\s+", "");
        newNumber = CustomPhoneNumberUtil.transform(newNumber);

        if (isNumberLengthValid(newNumber)) {
            viewListener.enableNextButton();
        } else {
            viewListener.disableNextButton();
        }

        if (editable.toString().length() != newNumber.length()) {
            int lengthDifference = newNumber.length() - editable.toString().length();
            if (selection + lengthDifference < 0)
                viewListener.correctPhoneNumber(newNumber, 0);
            else if (selection > newNumber.length())
                viewListener.correctPhoneNumber(newNumber, newNumber.length());
            else
                viewListener.correctPhoneNumber(newNumber, selection + lengthDifference);

        }
    }

    private boolean isNumberLengthValid(String newNumber) {
        newNumber = newNumber.replace("-", "");
        return (newNumber.length() >= MINIMUM_NUMBER_LENGTH && newNumber.length() <=
                MAXIMUM_NUMBER_LENGTH);
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
                if (e instanceof UnknownHostException) {
                    viewListener.onErrorcheckStatus(
                            viewListener.getString(R.string.msg_no_connection));
                } else if (e instanceof RuntimeException &&
                        e.getLocalizedMessage() != null &&
                        e.getLocalizedMessage().length() <= 3) {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            viewListener.onErrorcheckStatus(
                                    viewListener.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            viewListener.onErrorcheckStatus(
                                    viewListener.getString(R.string.default_request_error_timeout));
                        }

                        @Override
                        public void onServerError() {
                            viewListener.onErrorcheckStatus(
                                    viewListener.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            viewListener.onErrorcheckStatus(
                                    viewListener.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            viewListener.onErrorcheckStatus(
                                    viewListener.getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, Integer.parseInt(e.getLocalizedMessage()));
                } else if (e instanceof ErrorMessageException &&
                        e.getLocalizedMessage() != null) {
                    viewListener.onErrorcheckStatus(e.getLocalizedMessage());
                } else {
                    viewListener.onErrorcheckStatus(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

            }

            @Override
            public void onNext(CheckStatusModel checkStatusModel) {
                if (checkStatusModel.isSuccess() &&
                        checkStatusModel.getCheckStatusData().isSuccess()
                        && checkStatusModel.getErrorMessage() == null
                        && checkStatusModel.getStatusMessage() != null) {
                    viewListener.onSuccessCheckStatus(checkStatusModel.getCheckStatusData());
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
