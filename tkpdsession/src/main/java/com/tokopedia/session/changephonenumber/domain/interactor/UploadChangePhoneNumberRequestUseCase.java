package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberModel;
import com.tokopedia.session.changephonenumber.data.SubmitImageModel;
import com.tokopedia.session.changephonenumber.data.UploadHostModel;
import com.tokopedia.session.changephonenumber.data.UploadImageModel;
import com.tokopedia.session.changephonenumber.data.ValidateImageModel;
import com.tokopedia.session.changephonenumber.domain.UploadImageRepository;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadChangePhoneNumberRequestUseCase extends UseCase<ChangePhoneNumberModel> {

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_NEW_ADD = "new_add";
    public static final String DEFAULT_NEW_ADD = "2";

    public static final String PARAM_KTP_IMAGE_ID = "ktp_image_id";
    public static final String PARAM_BANKBOOK_IMAGE_ID = "bankbook_image_id";

    public static final String PARAM_KTP_IMAGE_PATH = "ktp_image_path";
    public static final String PARAM_BANK_BOOK_IMAGE_PATH = "bank_book_image_path";
    public static final String PARAM_DEVICE_ID = "device_id";

    public static final String PARAM_ID_WIDTH = "ktp_width";
    public static final String PARAM_ID_HEIGHT = "ktp_height";
    public static final String PARAM_BANKBOOK_WIDTH = "bankbook_width";
    public static final String PARAM_BANKBOOK_HEIGHT = "bankbook_height";


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
    public Observable<ChangePhoneNumberModel> createObservable(final RequestParams requestParams) {
        final ChangePhoneNumberModel changePhoneNumberModel = new ChangePhoneNumberModel();

        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(RequestParams requestParams) {
                        return Observable.just(changePhoneNumberModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberModel, Observable<ValidateImageModel>>() {
                    @Override
                    public Observable<ValidateImageModel> call(ChangePhoneNumberModel changePhoneNumberModel) {
                        return validateImage(getValidateImageParam(requestParams));
                    }
                })
                .flatMap(new Func1<ValidateImageModel, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(ValidateImageModel validateImageModel) {
                        changePhoneNumberModel.setValidateImageModel(validateImageModel);
                        changePhoneNumberModel.setSuccess(validateImageModel.isSuccess());

                        if (!changePhoneNumberModel.getValidateImageModel().isSuccess()
                                && changePhoneNumberModel.getValidateImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberModel.getValidateImageModel().getErrorMessage());
                        else if (!changePhoneNumberModel.getValidateImageModel().isSuccess()
                                && changePhoneNumberModel.getValidateImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberModel.getValidateImageModel().getResponseCode()));


                        return Observable.just(changePhoneNumberModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberModel, Observable<UploadHostModel>>() {
                    @Override
                    public Observable<UploadHostModel> call(ChangePhoneNumberModel changePhoneNumberModel) {
                        return getUploadHost(getUploadHostParam(requestParams));
                    }
                })
                .flatMap(new Func1<UploadHostModel, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(UploadHostModel uploadHostModel) {
                        changePhoneNumberModel.setUploadHostModel(uploadHostModel);
                        changePhoneNumberModel.setSuccess(uploadHostModel.isSuccess());

                        if (!changePhoneNumberModel.getUploadHostModel().isSuccess()
                                && changePhoneNumberModel.getUploadHostModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberModel.getUploadHostModel().getErrorMessage());
                        else if (!changePhoneNumberModel.getUploadHostModel().isSuccess()
                                && changePhoneNumberModel.getUploadHostModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberModel.getUploadHostModel().getResponseCode()));
                        return Observable.just(changePhoneNumberModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(ChangePhoneNumberModel changePhoneNumberModel) {
                        return uploadImage(getUploadIdImageParam(requestParams,
                                changePhoneNumberModel));
                    }
                })
                .flatMap(new Func1<UploadImageModel, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(UploadImageModel uploadImageModel) {
                        changePhoneNumberModel.setUploadIdImageModel(uploadImageModel);
                        changePhoneNumberModel.setSuccess(uploadImageModel.isSuccess());

                        if (!changePhoneNumberModel.getUploadIdImageModel().isSuccess()
                                && changePhoneNumberModel.getUploadIdImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberModel.getUploadIdImageModel().getErrorMessage());
                        else if (!changePhoneNumberModel.getUploadIdImageModel().isSuccess()
                                && changePhoneNumberModel.getUploadIdImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberModel.getUploadIdImageModel().getResponseCode()));
                        return Observable.just(changePhoneNumberModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(ChangePhoneNumberModel changePhoneNumberModel) {
                        return uploadImage(getUploadBookBankImageParam(requestParams,
                                changePhoneNumberModel));
                    }
                })
                .flatMap(new Func1<UploadImageModel, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(UploadImageModel uploadImageModel) {
                        changePhoneNumberModel.setUploadBankBookImageModel(uploadImageModel);
                        changePhoneNumberModel.setSuccess(uploadImageModel.isSuccess());

                        if (!changePhoneNumberModel.getUploadBankBookImageModel().isSuccess()
                                && changePhoneNumberModel.getUploadBankBookImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberModel.getUploadBankBookImageModel().getErrorMessage());
                        else if (!changePhoneNumberModel.getUploadBankBookImageModel().isSuccess()
                                && changePhoneNumberModel.getUploadBankBookImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberModel.getUploadBankBookImageModel().getResponseCode()));

                        return Observable.just(changePhoneNumberModel);
                    }
                })

                .flatMap(new Func1<ChangePhoneNumberModel, Observable<SubmitImageModel>>() {
                    @Override
                    public Observable<SubmitImageModel> call(ChangePhoneNumberModel changePhoneNumberModel) {
                        return submitImage(getSubmitImageParam(requestParams,
                                changePhoneNumberModel));
                    }
                })
                .flatMap(new Func1<SubmitImageModel, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(SubmitImageModel submitImageModel) {
                        changePhoneNumberModel.setSubmitImageModel(submitImageModel);
                        changePhoneNumberModel.setSuccess(submitImageModel.isSuccess());

                        if (!changePhoneNumberModel.getSubmitImageModel().isSuccess()
                                && changePhoneNumberModel.getSubmitImageModel().getErrorMessage() != null)
                            throw new ErrorMessageException(changePhoneNumberModel.getSubmitImageModel().getErrorMessage());
                        else if (!changePhoneNumberModel.getSubmitImageModel().isSuccess()
                                && changePhoneNumberModel.getSubmitImageModel().getResponseCode() != 200)
                            throw new RuntimeException(String.valueOf(changePhoneNumberModel.getSubmitImageModel().getResponseCode()));

                        return Observable.just(changePhoneNumberModel);
                    }
                });

    }

    private Observable<SubmitImageModel> submitImage(RequestParams requestParams) {
        return submitImageUseCase.createObservable(requestParams);
    }

    private RequestParams getSubmitImageParam(RequestParams requestParams,
                                              ChangePhoneNumberModel changePhoneNumberModel) {
        RequestParams params = RequestParams.create();
        params.putString(SubmitImageUseCase.PARAM_USER_ID,
                requestParams.getString(PARAM_USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())));
        params.putString(SubmitImageUseCase.PARAM_FILE_UPLOADED,
                generateFileUploaded(requestParams, changePhoneNumberModel));

        return params;
    }

    private String generateFileUploaded(RequestParams requestParams, ChangePhoneNumberModel changePhoneNumberModel) {
        JSONObject reviewPhotos = new JSONObject();

        try {
            reviewPhotos.put(requestParams.getString(PARAM_KTP_IMAGE_ID, ""),
                    changePhoneNumberModel.getUploadIdImageModel().getUploadImageData().getPicObj());
            reviewPhotos.put(requestParams.getString(PARAM_BANKBOOK_IMAGE_ID, ""),
                    changePhoneNumberModel.getUploadBankBookImageModel().getUploadImageData().getPicObj());
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
        params.putString(ValidateImageUseCase.PARAM_USER_ID,
                requestParams.getString(PARAM_USER_ID,
                        ""));
        params.putString(ValidateImageUseCase.PARAM_ID_HEIGHT,
                requestParams.getString(PARAM_ID_HEIGHT,
                        ""));
        params.putString(ValidateImageUseCase.PARAM_ID_WIDTH,
                requestParams.getString(PARAM_ID_WIDTH,
                        ""));
        params.putString(ValidateImageUseCase.PARAM_BANKBOOK_HEIGHT,
                requestParams.getString(PARAM_BANKBOOK_HEIGHT,
                        ""));
        params.putString(ValidateImageUseCase.PARAM_BANKBOOK_WIDTH,
                requestParams.getString(PARAM_BANKBOOK_WIDTH,
                        ""));
        return params;
    }

    private RequestParams getUploadBookBankImageParam(RequestParams requestParams, ChangePhoneNumberModel changePhoneNumberModel) {
        RequestParams params = RequestParams.create();
        String uploadUrl = "https://" + changePhoneNumberModel.getUploadHostModel()
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
                String.valueOf(changePhoneNumberModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getServerId()));
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID,
                requestParams.getString(PARAM_BANKBOOK_IMAGE_ID, ""));
        params.putString(UploadImageUseCase.PARAM_TOKEN, changePhoneNumberModel
                .getValidateImageModel().getValidateImageData().getToken());
        return params;
    }

    private Observable<UploadImageModel> uploadImage(RequestParams requestParams) {
        return uploadImageUseCase.createObservable(requestParams);
    }

    private RequestParams getUploadIdImageParam(RequestParams requestParams, ChangePhoneNumberModel changePhoneNumberModel) {
        RequestParams params = RequestParams.create();
        String uploadUrl = "https://" + changePhoneNumberModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getUploadHost();
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
                String.valueOf(changePhoneNumberModel.getUploadHostModel().getUploadHostData().getGeneratedHost().getServerId()));
        params.putString(UploadImageUseCase.PARAM_IMAGE_ID,
                requestParams.getString(PARAM_KTP_IMAGE_ID, ""));
        params.putString(UploadImageUseCase.PARAM_TOKEN, changePhoneNumberModel
                .getValidateImageModel().getValidateImageData().getToken());
        return params;
    }

    private RequestParams getUploadHostParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(GetUploadHostUseCase.PARAM_USER_ID, requestParams.getString(PARAM_USER_ID,
                SessionHandler.getLoginID(MainApplication.getAppContext())));
        params.putString(GetUploadHostUseCase.PARAM_NEW_ADD, requestParams.getString(PARAM_NEW_ADD,
                GetUploadHostUseCase.DEFAULT_NEW_ADD));
        return params;
    }

    private Observable<UploadHostModel> getUploadHost(RequestParams requestParams) {
        return getUploadHostUseCase.createObservable(requestParams);
    }


}
