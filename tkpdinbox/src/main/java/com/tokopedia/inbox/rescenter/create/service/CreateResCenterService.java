package com.tokopedia.inbox.rescenter.create.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.inbox.rescenter.create.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.ActionResponseData;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.UploadResCenterImageData;
import com.tokopedia.inbox.rescenter.utils.UploadImageResCenter;
import com.tokopedia.core.util.ImageUploadHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class CreateResCenterService extends IntentService {

    private static final String TAG = CreateResCenterService.class.getSimpleName();

    public static final String ACTION_CREATE_RESOLUTION = "ACTION_CREATE_RESOLUTION";

    public static final String EXTRA_CREATE_RESOLUTION = "EXTRA_CREATE_RESOLUTION";
    public static final String EXTRA_PARAM_RECEIVER = "EXTRA_PARAM_RECEIVER";
    public static final String EXTRA_PARAM_ACTION_TYPE = "EXTRA_PARAM_ACTION_TYPE";
    public static final String EXTRA_PARAM_STATUS = "EXTRA_PARAM_STATUS";
    public static final String EXTRA_PARAM_ERROR_MESSAGE = "EXTRA_PARAM_ERROR_MESSAGE";
    public static final String EXTRA_PARAM_DATA = "EXTRA_PARAM_DATA";
    public static final Integer RESULT_ERROR = 0;
    public static final Integer RESULT_SUCCESS = 1;

    public static final int STATUS_TIME_OUT = 2;
    public static final int STATUS_ERROR = 3;

    public static final String MESSAGE_NO_CONNECTION_EXCEPTION = "No connection";
    public static final String MESSAGE_TIME_OUT_EXCEPTION = "Timeout connection," +
            " Mohon ulangi beberapa saat lagi";

    private String action;
    private ResultReceiver receiver;

    public CreateResCenterService() {
        super("CreateResCenterService");
    }

    public static void startActionCreateResolution(Context context, ActionParameterPassData param, CreateResCenterReceiver receiver) {
        Intent intent = new Intent(context, CreateResCenterService.class);
        intent.setAction(ACTION_CREATE_RESOLUTION);
        intent.putExtra(EXTRA_CREATE_RESOLUTION, param);
        intent.putExtra(EXTRA_PARAM_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            action = intent.getAction();
            receiver = intent.getParcelableExtra(EXTRA_PARAM_RECEIVER);
            ActionParameterPassData data = intent.getParcelableExtra(EXTRA_CREATE_RESOLUTION);
            handleActionCreateResolution(data);
        }
    }

    private void handleActionCreateResolution(ActionParameterPassData data) {
        Observable.just(data)
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.getAttachmentData() == null || actionParameterPassData.getAttachmentData().isEmpty()) {
                            return Observable.just(actionParameterPassData);
                        } else {
                            GenerateHostActService generateHostActService = new GenerateHostActService();
                            Observable<GeneratedHost> generateHost = generateHostActService.getApi().generateHost(AuthUtil.generateParams(getApplicationContext(), NetworkParam.generateHost()));
                            return Observable.zip(Observable.just(actionParameterPassData), generateHost, new Func2<ActionParameterPassData, GeneratedHost, ActionParameterPassData>() {
                                @Override
                                public ActionParameterPassData call(ActionParameterPassData actionParameterPassData, GeneratedHost generatedHost) {
                                    if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                                        actionParameterPassData.setServerID(String.valueOf(generatedHost.getServerId()));
                                        actionParameterPassData.setUploadHost(generatedHost.getUploadHost());
                                        return actionParameterPassData;
                                    } else {
                                        throw new RuntimeException(generatedHost.getMessageError().get(0));
                                    }
                                }
                            });
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.getAttachmentData() == null || actionParameterPassData.getAttachmentData().isEmpty()) {
                            return Observable.just(actionParameterPassData);
                        } else {
                            return getObservableUploadingFile(actionParameterPassData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        return getObservableCreateResolutionValidation(actionParameterPassData);
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.isByPassFlag()) {
                            return Observable.just(actionParameterPassData);
                        } else {
                            return getObservableCreateResolutionPicture(actionParameterPassData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionResponseData>>() {
                    @Override
                    public Observable<ActionResponseData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.isByPassFlag()) {
                            return Observable.just(actionParameterPassData.getActionResponseData());
                        } else {
                            return getObservableCreateResolutionSubmit(actionParameterPassData);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<ActionResponseData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());

                        String message;
                        Integer statusRequest;

                        if (e instanceof IOException) {
                            statusRequest = STATUS_TIME_OUT;
                            message = MESSAGE_TIME_OUT_EXCEPTION;
                        } else {
                            statusRequest = STATUS_ERROR;
                            message = e.getMessage();
                        }

                        Log.d(TAG + "-step6", message);
                        Bundle resultData = new Bundle();
                        resultData.putString(EXTRA_PARAM_ACTION_TYPE, action);
                        resultData.putInt(EXTRA_PARAM_STATUS, statusRequest);
                        resultData.putString(EXTRA_PARAM_ERROR_MESSAGE, message);
                        receiver.send(RESULT_ERROR, resultData);
                    }

                    @Override
                    public void onNext(ActionResponseData data) {
                        Log.d(TAG + "-step6", String.valueOf(data));
                        Bundle resultData = new Bundle();
                        resultData.putString(EXTRA_PARAM_ACTION_TYPE, action);
                        resultData.putParcelable(EXTRA_PARAM_DATA, data);
                        receiver.send(RESULT_SUCCESS, resultData);
                    }
                });
    }

    private Observable<ActionResponseData> getObservableCreateResolutionSubmit(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = AuthUtil.generateParams(getApplicationContext(), NetworkParam.paramCreateResCenterSubmit(actionParameterPassData));
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> createResolutionSubmit = resCenterActService.getApi().createResolutionSubmit(params);

        return Observable.zip(Observable.just(actionParameterPassData), createResolutionSubmit, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionResponseData>() {
            @Override
            public ActionResponseData call(ActionParameterPassData previousData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step5", response.body().getStrResponse());

                if (response.isSuccessful()) {
                    ActionResponseData temp = response.body().convertDataObj(ActionResponseData.class);
                    if (temp.isSuccess()) {
                        return temp;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessages().toString());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }

    private Observable<ActionParameterPassData> getObservableCreateResolutionPicture(final ActionParameterPassData actionParameterPassData) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                "http://" + actionParameterPassData.getUploadHost())
                .setIdentity()
                .addParam("attachment_string", actionParameterPassData.getAttachmentString())
                .addParam("file_path", actionParameterPassData.getAttachmentString())
                .addParam("server_id", String.valueOf(actionParameterPassData.getServerID()))
                .addParam("web_service", String.valueOf(1))
                .compileAllParam()
                .finish();

        RequestBody attachmentString = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("attachment_string"));
        RequestBody filePath = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("file_path"));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("server_id"));
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("web_service"));

        Log.d(TAG + "(step 4):host", actionParameterPassData.getUploadHost());
        final Observable<UploadResCenterImageData> createResolutionPicture = RetrofitUtils.createRetrofit(networkCalculator.getUrl())
                .create(UploadImageResCenter.class)
                .createResolutionPicture(
                        networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                        networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                        networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                        networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                        userId,
                        attachmentString,
                        filePath,
                        serverId,
                        webservice
                );

        return Observable.zip(Observable.just(actionParameterPassData), createResolutionPicture,
                new Func2<ActionParameterPassData, UploadResCenterImageData, ActionParameterPassData>() {
                    @Override
                    public ActionParameterPassData call(ActionParameterPassData previousData, UploadResCenterImageData response) {
                        Log.d(TAG + "-step4", response.toString());
                        if (response.getData() != null) {
                            if (response.getData().isSuccess()) {
                                actionParameterPassData.setFileUploaded(response.getData().getFileUploaded());
                                return actionParameterPassData;
                            } else {
                                throw new RuntimeException(response.getMessageError().get(0));
                            }
                        } else {
                            throw new RuntimeException("create resolution picture error");
                        }
                    }
                });
    }

    private Observable<ActionParameterPassData> getObservableCreateResolutionValidation(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> createResolutionValidation =
                resCenterActService
                        .getApi()
                        .createResolutionValidation(
                                AuthUtil.generateParams(
                                        getApplicationContext(),
                                        NetworkParam.paramCreateResCenterValidation(actionParameterPassData)
                                )
                        );

        return Observable.zip(Observable.just(actionParameterPassData), createResolutionValidation, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionParameterPassData>() {
            @Override
            public ActionParameterPassData call(ActionParameterPassData actionParameterPassData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step3", response.body().getStrResponse());
                if (response.isSuccessful()) {
                    ActionResponseData responseData = response.body().convertDataObj(ActionResponseData.class);
                    if (responseData.isSuccess()) {
                        if (responseData.getPostKey() != null) {
                            actionParameterPassData.setPostKey(responseData.getPostKey());
                            actionParameterPassData.setByPassFlag(false);
                        } else {
                            actionParameterPassData.setActionResponseData(responseData);
                            actionParameterPassData.setByPassFlag(true);
                        }
                        return actionParameterPassData;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessages().toString());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }

    private Observable<ActionParameterPassData> getObservableUploadingFile(ActionParameterPassData actionParameterPassData) {
        return Observable.zip(Observable.just(actionParameterPassData), uploading(actionParameterPassData), new Func2<ActionParameterPassData, List<AttachmentResCenterVersion2DB>, ActionParameterPassData>() {
            @Override
            public ActionParameterPassData call(ActionParameterPassData actionParameterPassData, List<AttachmentResCenterVersion2DB> listAttachment) {
                int j = 0;
                String attachmentCompiledString = "";
                for (int i = 0; i < listAttachment.size(); i++) {
                    if (j != 0) {
                        if (!listAttachment.get(i).imagePath.isEmpty()) {
                            attachmentCompiledString = attachmentCompiledString + "~" + listAttachment.get(i).imageUrl;
                            j++;
                        }
                    } else {
                        if (!listAttachment.get(i).imagePath.isEmpty()) {
                            attachmentCompiledString = listAttachment.get(i).imageUrl;
                            j++;
                        }
                    }
                }
                Log.d(TAG + "(step2)string", attachmentCompiledString);
                actionParameterPassData.setAttachmentString(attachmentCompiledString);
                return actionParameterPassData;
            }
        });
    }

    private Observable<List<AttachmentResCenterVersion2DB>> uploading(final ActionParameterPassData actionParameterPassData) {
        return Observable
                .from(actionParameterPassData.getAttachmentData())
                .flatMap(new Func1<AttachmentResCenterVersion2DB, Observable<AttachmentResCenterVersion2DB>>() {
                    @Override
                    public Observable<AttachmentResCenterVersion2DB> call(AttachmentResCenterVersion2DB attachmentResCenterDB) {
                        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                                "https://" + actionParameterPassData.getUploadHost())
                                .setIdentity()
                                .addParam("server_id", String.valueOf(actionParameterPassData.getServerID()))
                                .compileAllParam()
                                .finish();

                        // https:// uploadhost /upload/attachment
                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(attachmentResCenterDB.imagePath));
                        } catch (IOException e) {
                            throw new RuntimeException(getApplicationContext().getString(R.string.error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
                        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("server_id"));

                        Log.d(TAG + "(step 2):host", actionParameterPassData.getUploadHost());
                        final Observable<UploadResCenterImageData> upload = RetrofitUtils.createRetrofit(networkCalculator.getUrl())
                                .create(UploadImageResCenter.class)
                                .uploadImage(
                                        networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                        networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                        networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                        networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                        userId,
                                        deviceId,
                                        hash,
                                        deviceTime,
                                        fileToUpload,
                                        serverId
                                );

                        return Observable.zip(Observable.just(attachmentResCenterDB), upload, new Func2<AttachmentResCenterVersion2DB, UploadResCenterImageData, AttachmentResCenterVersion2DB>() {
                            @Override
                            public AttachmentResCenterVersion2DB call(AttachmentResCenterVersion2DB attachmentResCenterDB, UploadResCenterImageData uploadResCenterImageData) {
                                if (uploadResCenterImageData != null) {
                                    if (uploadResCenterImageData.getData() != null) {
                                        attachmentResCenterDB.imageUrl = uploadResCenterImageData.getData().getFileUrl();
                                        Log.d(TAG + "(step2):url", uploadResCenterImageData.getData().getFileUrl());
                                        attachmentResCenterDB.save();
                                        return attachmentResCenterDB;
                                    } else {
                                        throw new RuntimeException(uploadResCenterImageData.getMessageError().get(0));
                                    }
                                } else {
                                    throw new RuntimeException("upload error");
                                }
                            }

                        });
                    }
                })
                .toList();
    }

}
