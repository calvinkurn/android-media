package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberModel;
import com.tokopedia.session.changephonenumber.data.GeneratePostKeyModel;
import com.tokopedia.session.changephonenumber.data.UploadHostModel;
import com.tokopedia.session.changephonenumber.data.UploadImageModel;
import com.tokopedia.session.changephonenumber.domain.UploadImageRepository;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.session.changephonenumber.domain.interactor.UploadImageUseCase.PARAM_DEVICE_ID;
import static com.tokopedia.session.changephonenumber.domain.interactor.UploadImageUseCase.PARAM_IMAGE_PATH;
import static com.tokopedia.session.changephonenumber.domain.interactor.UploadImageUseCase.PARAM_USER_ID;

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

    private final UploadImageRepository uploadImageRepository;
    private final GetUploadHostUseCase getUploadHostUseCase;
    private final GeneratePostKeyUseCase generatePostKeyUseCase;
    private final UploadImageUseCase uploadImageUseCase;

    public UploadChangePhoneNumberRequestUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 UploadImageRepository uploadImageRepository,
                                                 GetUploadHostUseCase getUploadHostUseCase,
                                                 UploadImageUseCase uploadImageUseCase,
                                                 GeneratePostKeyUseCase generatePostKeyUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
        this.getUploadHostUseCase = getUploadHostUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.generatePostKeyUseCase = generatePostKeyUseCase;
    }

    @Override
    public Observable<ChangePhoneNumberModel> createObservable(final RequestParams requestParams) {
        return getUploadHost(getUploadHostParam(requestParams))
                .flatMap(new Func1<UploadHostModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(UploadHostModel uploadHostModel) {
                        return uploadImageId(getUploadImageIdParam(requestParams, uploadHostModel));
                    }
                })
                .flatMap(new Func1<UploadImageModel, Observable<ChangePhoneNumberModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberModel> call(UploadImageModel uploadImageModel) {
                        ChangePhoneNumberModel changePhoneNumberModel = new ChangePhoneNumberModel();
                        changePhoneNumberModel.setSuccess(uploadImageModel.isSuccess());
                        return Observable.just(changePhoneNumberModel);
                    }
                })
                .onErrorReturn(new Func1<Throwable, ChangePhoneNumberModel>() {
                    @Override
                    public ChangePhoneNumberModel call(Throwable throwable) {
                        CommonUtils.dumper("NISNIS " + throwable.toString());
                        ChangePhoneNumberModel model = new ChangePhoneNumberModel();
                        model.setSuccess(false);
                        return model;
                    }
                });

    }

    private Observable<UploadImageModel> uploadImageId(RequestParams requestParams) {
        return uploadImageUseCase.createObservable(requestParams);
    }

    private RequestParams getUploadImageIdParam(RequestParams requestParams, UploadHostModel uploadHostModel) {
        RequestParams params = RequestParams.create();
        String uploadUrl = "https://" + uploadHostModel.getUploadHostData().getGeneratedHost().getUploadHost();
        params.putString(UploadImageUseCase.PARAM_USER_ID, requestParams.getString(PARAM_USER_ID,
                SessionHandler.getTempLoginSession(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_DEVICE_ID, requestParams.getString(PARAM_DEVICE_ID,
                GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        params.putString(UploadImageUseCase.PARAM_URL, uploadUrl);
        params.putString(UploadImageUseCase.PARAM_IMAGE_PATH, requestParams.getString(PARAM_KTP_IMAGE_PATH, ""));
        params.putString(UploadImageUseCase.PARAM_SERVER_ID, String.valueOf(uploadHostModel.getUploadHostData().getGeneratedHost().getServerId()));
        return params;
    }


    private RequestParams getGeneratePostKeyParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(GeneratePostKeyUseCase.PARAM_USER_ID, requestParams.getString(PARAM_USER_ID,
                SessionHandler.getLoginID(MainApplication.getAppContext())));
        params.putString(PARAM_KTP_IMAGE_ID, requestParams.getString(PARAM_KTP_IMAGE_ID,
                ""));
        params.putString(GeneratePostKeyUseCase.PARAM_BANKBOOK_IMAGE_ID, requestParams.getString(PARAM_USER_ID,
                ""));
        return params;
    }

    private Observable<GeneratePostKeyModel> generatePostKey(RequestParams requestParams) {
        return generatePostKeyUseCase.createObservable(requestParams);
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
