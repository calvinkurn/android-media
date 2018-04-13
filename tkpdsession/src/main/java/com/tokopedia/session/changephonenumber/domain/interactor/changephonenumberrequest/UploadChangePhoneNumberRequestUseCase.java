package com.tokopedia.session.changephonenumber.domain.interactor.changephonenumberrequest;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.ChangePhoneNumberRequestModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.SubmitImageModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadHostModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadImageModel;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.ValidateImageModel;
import com.tokopedia.session.changephonenumber.data.repository.UploadImageRepository;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadChangePhoneNumberRequestUseCase extends UseCase<ChangePhoneNumberRequestModel> {

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_NEW_ADD = "new_add";
    public static final String DEFAULT_NEW_ADD = "2";

    public static final String PARAM_KTP_IMAGE_ID = "ktp";
    public static final String PARAM_BANKBOOK_IMAGE_ID = "tabungan";

    public static final String PARAM_KTP_IMAGE_PATH = "ktp_image_path";
    public static final String PARAM_BANK_BOOK_IMAGE_PATH = "bank_book_image_path";
    public static final String PARAM_DEVICE_ID = "device_id";

    public static final String PARAM_ID_WIDTH = "ktp_width";
    public static final String PARAM_ID_HEIGHT = "ktp_height";
    public static final String PARAM_BANKBOOK_WIDTH = "bankbook_width";
    public static final String PARAM_BANKBOOK_HEIGHT = "bankbook_height";
    public static final String PARAM_PHONE_NUMBER = "phone_number";

    private final UploadImageRepository uploadImageRepository;
    private final GetUploadHostUseCase getUploadHostUseCase;
    private final ValidateImageUseCase validateImageUseCase;
    private final UploadImageUseCase uploadImageUseCase;
    private final SubmitImageUseCase submitImageUseCase;

    public UploadChangePhoneNumberRequestUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 UploadImageRepository uploadImageRepository,
                                                 GetUploadHostUseCase getUploadHostUseCase,
                                                 UploadImageUseCase uploadImageUseCase,
                                                 ValidateImageUseCase validateImageUseCase,
                                                 SubmitImageUseCase submitImageUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
        this.getUploadHostUseCase = getUploadHostUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.validateImageUseCase = validateImageUseCase;
        this.submitImageUseCase = submitImageUseCase;
    }

    @Override
    public Observable<ChangePhoneNumberRequestModel> createObservable(final RequestParams requestParams) {
        final ChangePhoneNumberRequestModel changePhoneNumberRequestModel = new ChangePhoneNumberRequestModel();

        Observable<ChangePhoneNumberRequestModel> initialObservable = Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<ChangePhoneNumberRequestModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberRequestModel> call(RequestParams requestParams) {
                        return Observable.just(changePhoneNumberRequestModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberRequestModel, Observable<ValidateImageModel>>() {
                    @Override
                    public Observable<ValidateImageModel> call(ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
                        return validateImage(getValidateImageParam(requestParams));
                    }
                })
                .flatMap(new Func1<ValidateImageModel, Observable<ChangePhoneNumberRequestModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberRequestModel> call(ValidateImageModel validateImageModel) {
                        changePhoneNumberRequestModel.setValidateImageModel(validateImageModel);
                        changePhoneNumberRequestModel.setSuccess(validateImageModel.isSuccess());

                        if (!changePhoneNumberRequestModel.getValidateImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getValidateImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberRequestModel.getValidateImageModel().getErrorMessage());
                        else if (!changePhoneNumberRequestModel.getValidateImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getValidateImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getValidateImageModel().getResponseCode()));


                        return Observable.just(changePhoneNumberRequestModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberRequestModel, Observable<UploadHostModel>>() {
                    @Override
                    public Observable<UploadHostModel> call(ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
                        return getUploadHost(getUploadHostParam(requestParams));
                    }
                })
                .flatMap(new Func1<UploadHostModel, Observable<ChangePhoneNumberRequestModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberRequestModel> call(UploadHostModel uploadHostModel) {
                        changePhoneNumberRequestModel.setUploadHostModel(uploadHostModel);
                        changePhoneNumberRequestModel.setSuccess(uploadHostModel.isSuccess());

                        if (!changePhoneNumberRequestModel.getUploadHostModel().isSuccess()
                                && changePhoneNumberRequestModel.getUploadHostModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberRequestModel.getUploadHostModel().getErrorMessage());
                        else if (!changePhoneNumberRequestModel.getUploadHostModel().isSuccess()
                                && changePhoneNumberRequestModel.getUploadHostModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getUploadHostModel().getResponseCode()));
                        return Observable.just(changePhoneNumberRequestModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberRequestModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
                        return uploadImage(getUploadIdImageParam(requestParams,
                                changePhoneNumberRequestModel));
                    }
                })
                .flatMap(new Func1<UploadImageModel, Observable<ChangePhoneNumberRequestModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberRequestModel> call(UploadImageModel uploadImageModel) {
                        changePhoneNumberRequestModel.setUploadIdImageModel(uploadImageModel);
                        changePhoneNumberRequestModel.setSuccess(uploadImageModel.isSuccess());

                        if (!changePhoneNumberRequestModel.getUploadIdImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getUploadIdImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberRequestModel.getUploadIdImageModel().getErrorMessage());
                        else if (!changePhoneNumberRequestModel.getUploadIdImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getUploadIdImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getUploadIdImageModel().getResponseCode()));
                        return Observable.just(changePhoneNumberRequestModel);
                    }
                });

                if(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, "") != "") {
                    initialObservable = initialObservable.flatMap(new Func1<ChangePhoneNumberRequestModel, Observable<UploadImageModel>>() {
                        @Override
                        public Observable<UploadImageModel> call(ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
                            return uploadImage(getUploadBookBankImageParam(requestParams,
                                    changePhoneNumberRequestModel));
                        }
                    })
                            .flatMap(new Func1<UploadImageModel, Observable<ChangePhoneNumberRequestModel>>() {
                                @Override
                                public Observable<ChangePhoneNumberRequestModel> call(UploadImageModel uploadImageModel) {
                                    changePhoneNumberRequestModel.setUploadBankBookImageModel(uploadImageModel);
                                    changePhoneNumberRequestModel.setSuccess(uploadImageModel.isSuccess());

                                    if (!changePhoneNumberRequestModel.getUploadBankBookImageModel().isSuccess()
                                            && changePhoneNumberRequestModel.getUploadBankBookImageModel().getErrorMessage() != null)
                                        throw new ErrorMessageException(changePhoneNumberRequestModel.getUploadBankBookImageModel().getErrorMessage());
                                    else if (!changePhoneNumberRequestModel.getUploadBankBookImageModel().isSuccess()
                                            && changePhoneNumberRequestModel.getUploadBankBookImageModel().getResponseCode() != 200)
                                        throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getUploadBankBookImageModel().getResponseCode()));

                                    return Observable.just(changePhoneNumberRequestModel);
                                }
                            });
                }
                return initialObservable.flatMap(new Func1<ChangePhoneNumberRequestModel, Observable<SubmitImageModel>>() {
                    @Override
                    public Observable<SubmitImageModel> call(ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
                        return submitImage(getSubmitImageParam(requestParams,
                                changePhoneNumberRequestModel));
                    }
                })
                .flatMap(new Func1<SubmitImageModel, Observable<ChangePhoneNumberRequestModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberRequestModel> call(SubmitImageModel submitImageModel) {
                        changePhoneNumberRequestModel.setSubmitImageModel(submitImageModel);
                        changePhoneNumberRequestModel.setSuccess(submitImageModel.isSuccess());

                        if (!changePhoneNumberRequestModel.getSubmitImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getSubmitImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberRequestModel.getSubmitImageModel().getErrorMessage());
                        else if (!changePhoneNumberRequestModel.getSubmitImageModel().isSuccess()
                                && changePhoneNumberRequestModel.getSubmitImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberRequestModel.getSubmitImageModel().getResponseCode()));

                        return Observable.just(changePhoneNumberRequestModel);
                    }
                });

    }

    private Observable<SubmitImageModel> submitImage(RequestParams requestParams) {
        return submitImageUseCase.createObservable(requestParams);
    }

    private RequestParams getSubmitImageParam(RequestParams requestParams,
                                              ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        RequestParams params = RequestParams.create();
        params.putString(SubmitImageUseCase.PARAM_FILE_UPLOADED,
                generateFileUploaded(requestParams, changePhoneNumberRequestModel));
        params.putString(SubmitImageUseCase.PARAM_OS_TYPE, SubmitImageUseCase.DEFAULT_OS_TYPE);
        params.putString(PARAM_PHONE_NUMBER,requestParams.getString(PARAM_PHONE_NUMBER,""));
        return params;
    }

    private String generateFileUploaded(RequestParams requestParams, ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        JSONObject reviewPhotos = new JSONObject();

        try {
            reviewPhotos.put(PARAM_KTP_IMAGE_ID,
                    changePhoneNumberRequestModel.getUploadIdImageModel().getUploadImageData().getPicObj());
            if(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, "") != "") {
                reviewPhotos.put(PARAM_BANKBOOK_IMAGE_ID,
                        changePhoneNumberRequestModel.getUploadBankBookImageModel().getUploadImageData().getPicObj());
            }
        } catch (JSONException e) {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string.default_error_upload_image));
        }

        return reviewPhotos.toString();
    }

    private Observable<ValidateImageModel> validateImage(RequestParams requestParams) {
        return validateImageUseCase.createObservable(requestParams);
    }

    private RequestParams getValidateImageParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(ValidateImageUseCase.PARAM_ID_HEIGHT,
                requestParams.getString(PARAM_ID_HEIGHT,
                        ""));
        params.putString(ValidateImageUseCase.PARAM_ID_WIDTH,
                requestParams.getString(PARAM_ID_WIDTH,
                        ""));
        if(requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, "") != "") {
            params.putString(ValidateImageUseCase.PARAM_BANKBOOK_HEIGHT,
                    requestParams.getString(PARAM_BANKBOOK_HEIGHT,
                            ""));
            params.putString(ValidateImageUseCase.PARAM_BANKBOOK_WIDTH,
                    requestParams.getString(PARAM_BANKBOOK_WIDTH,
                            ""));
        }
        return params;
    }

    private RequestParams getUploadBookBankImageParam(RequestParams requestParams, ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        RequestParams params = RequestParams.create();
        String uploadUrl = "https://" + changePhoneNumberRequestModel.getUploadHostModel()
                .getUploadHostData().getGeneratedHost().getUploadHost();
        params.putString(UploadImageUseCase.PARAM_USER_ID,
                requestParams.getString(PARAM_USER_ID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_URL, uploadUrl);
        params.putString(UploadImageUseCase.PARAM_FILE_TO_UPLOAD,
                requestParams.getString(PARAM_BANK_BOOK_IMAGE_PATH, ""));
        params.putString(UploadImageUseCase.PARAM_SERVER_ID,
                String.valueOf(changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getServerId()));
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID,
                requestParams.getString(PARAM_USER_ID, ""));
        params.putString(UploadImageUseCase.PARAM_TOKEN, changePhoneNumberRequestModel
                .getValidateImageModel().getValidateImageData().getToken());
        params.putString(UploadImageUseCase.PARAM_WEB_SERVICE, "1");
        return params;
    }

    private Observable<UploadImageModel> uploadImage(RequestParams requestParams) {
        return uploadImageUseCase.createObservable(requestParams);
    }

    private RequestParams getUploadIdImageParam(RequestParams requestParams, ChangePhoneNumberRequestModel changePhoneNumberRequestModel) {
        RequestParams params = RequestParams.create();
        String uploadUrl = "https://" + changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getUploadHost();
        params.putString(UploadImageUseCase.PARAM_USER_ID,
                requestParams.getString(PARAM_USER_ID,
                        SessionHandler.getTempLoginSession(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_DEVICE_ID,
                requestParams.getString(PARAM_DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_URL, uploadUrl);
        params.putString(UploadImageUseCase.PARAM_FILE_TO_UPLOAD,
                requestParams.getString(PARAM_KTP_IMAGE_PATH, ""));
        params.putString(UploadImageUseCase.PARAM_SERVER_ID,
                String.valueOf(changePhoneNumberRequestModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getServerId()));
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID,
                requestParams.getString(PARAM_USER_ID, ""));
        params.putString(UploadImageUseCase.PARAM_TOKEN, changePhoneNumberRequestModel
                .getValidateImageModel().getValidateImageData().getToken());
        params.putString(UploadImageUseCase.PARAM_WEB_SERVICE, "1");
        return params;
    }

    private RequestParams getUploadHostParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(GetUploadHostUseCase.PARAM_NEW_ADD, requestParams.getString(PARAM_NEW_ADD,
                GetUploadHostUseCase.DEFAULT_NEW_ADD));
        return params;
    }

    private Observable<UploadHostModel> getUploadHost(RequestParams requestParams) {
        return getUploadHostUseCase.createObservable(requestParams);
    }


}
