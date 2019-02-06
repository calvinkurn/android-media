package com.tokopedia.inbox.rescenter.detail.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.inbox.rescenter.detail.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.ResCenterActionData;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.UploadResCenterImageData;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class DetailResCenterService extends IntentService implements DetailResCenterServiceConstant {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "EXTRA_PARAM_RESOLUTION_ID";
    private static final String EXTRA_PARAM_RECEIVER = "EXTRA_PARAM_RECEIVER";
    private static final String TAG = DetailResCenterService.class.getSimpleName();

    private int typeAction;
    private ResultReceiver receiver;
    private String resolutionID;

    public DetailResCenterService() {
        super("DetailResCenterService");
    }

    /**
     * Starts this resCenterService to perform typeAction Foo with the given parameters. If
     * the resCenterService is already performing a task this typeAction will be queued.
     *
     * @see IntentService
     */
    public static void startActionChangeSolution(@NonNull Context context,
                                                 @NonNull String resolutionID,
                                                 @NonNull DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_CHANGE_SOLUTION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }


    public static void startActionReply(@NonNull Context context,
                                        @NonNull String resolutionID,
                                        @NonNull DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_REPLY_CONVERSATION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    public static void startActionFinishReturSolution(@NonNull Context context,
                                                      @NonNull String resolutionID,
                                                      @NonNull DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_FINISH_RETUR_SOLUTION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    public static void startActionAcceptSolution(@NonNull Context context,
                                                 @NonNull String resolutionID,
                                                 @NonNull DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_ACCEPT_SOLUTION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    public static void startActionAcceptAdminSolution(@NonNull Context context,
                                                      @NonNull String resolutionID,
                                                      @NonNull DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_ACCEPT_ADMIN_SOLUTION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    public static void startActionCancelResolution(@NonNull Context context,
                                                   @NonNull String resolutionID,
                                                   @NonNull DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_CANCEL_RESOLUTION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    public static void startActionReportResolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        Intent intent = new Intent(context, DetailResCenterService.class);
        intent.putExtra(EXTRA_PARAM_ACTION_TYPE, ACTION_REPORT_RESOLUTION);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        intent.putExtra(EXTRA_PARAM_RECEIVER, mReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            resolutionID = intent.getStringExtra(EXTRA_PARAM_RESOLUTION_ID);
            typeAction = intent.getIntExtra(EXTRA_PARAM_ACTION_TYPE, DEFAULT_ACTION);
            receiver = intent.getParcelableExtra(EXTRA_PARAM_RECEIVER);

            ActionParameterPassData resCenterPass = new ActionParameterPassData();
            resCenterPass.setResolutionID(resolutionID);
            switch (typeAction) {
                case ACTION_CHANGE_SOLUTION:
                    processObservableReplySolution(resCenterPass);
                    break;
                case ACTION_REPLY_CONVERSATION:
                    processObservableReplySolution(resCenterPass);
                    break;
                case ACTION_ACCEPT_ADMIN_SOLUTION:
                    processObservableAcceptAdminSolution(resCenterPass);
                    break;
                case ACTION_ACCEPT_SOLUTION:
                    processObservableAcceptSolution(resCenterPass);
                    break;
                case ACTION_FINISH_RETUR_SOLUTION:
                    processObservableFinishReturSolution(resCenterPass);
                    break;
                case ACTION_CANCEL_RESOLUTION:
                    processObservableCancelResolution(resCenterPass);
                    break;
                case ACTION_REPORT_RESOLUTION:
                    processObservableReportResolution(resCenterPass);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }
        }
    }

    private void processObservableReportResolution(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        resCenterActService
                .getApi()
                .reportResolution(AuthUtil.generateParams(getApplicationContext(), NetworkParam.reportResolution(actionParameterPassData)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private Subscriber<Response<TkpdResponse>> getDefaultSubscriber() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                String message;
                int statusRequest;

                if (e instanceof IOException) {
                    statusRequest = STATUS_TIME_OUT;
                    message = MESSAGE_TIMEOUT_INFO;
                } else {
                    statusRequest = STATUS_ERROR;
                    message = e.getMessage();
                }

                Log.d(TAG + "-step6", message);
                Bundle resultData = new Bundle();
                resultData.putInt(EXTRA_PARAM_ACTION_TYPE, typeAction);
                resultData.putInt(EXTRA_PARAM_NETWORK_ERROR_TYPE, statusRequest);
                resultData.putString(EXTRA_PARAM_NETWORK_ERROR_MESSAGE, message);
                receiver.send(STATUS_ERROR, resultData);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        switch (typeAction) {
                            case ACTION_ACCEPT_ADMIN_SOLUTION:
                                // nothing spesific at the end develop, join in default section
                                break;
                            case ACTION_ACCEPT_SOLUTION:
                                // nothing spesific at the end develop, join in default section
                                break;
                            case ACTION_FINISH_RETUR_SOLUTION:
                                // nothing spesific at the end develop, join in default section
                                break;
                            case ACTION_CANCEL_RESOLUTION:
                                // nothing spesific at the end develop, join in default section
                                break;
                            case ACTION_REPORT_RESOLUTION:
                                // nothing spesific at the end develop, join in default section
                                break;
                            default:
                                break;
                        }
                        Bundle resultData = new Bundle();
                        resultData.putInt(EXTRA_PARAM_ACTION_TYPE, typeAction);
                        receiver.send(STATUS_FINISHED, resultData);
                    } else {
                        throw new RuntimeException(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            throw new RuntimeException(MESSAGE_UNKNOWN_INFO);
                        }

                        @Override
                        public void onTimeout() {
                            throw new RuntimeException(MESSAGE_TIMEOUT_INFO);
                        }

                        @Override
                        public void onServerError() {
                            throw new RuntimeException(MESSAGE_SERVER_INFO);
                        }

                        @Override
                        public void onBadRequest() {
                            throw new RuntimeException(MESSAGE_BAD_REQUEST_INFO);
                        }

                        @Override
                        public void onForbidden() {
                            throw new RuntimeException(MESSAGE_FORBIDDEN_INFO);
                        }
                    }, response.code());
                }
            }
        };
    }

    private void processObservableCancelResolution(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        resCenterActService
                .getApi()
                .cancelResolution(AuthUtil.generateParams(getApplicationContext(), NetworkParam.cancelResolution(actionParameterPassData)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private void processObservableAcceptSolution(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        resCenterActService
                .getApi()
                .acceptResolution(AuthUtil.generateParams(getApplicationContext(), NetworkParam.acceptSolution(actionParameterPassData)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private void processObservableFinishReturSolution(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        resCenterActService
                .getApi()
                .finishResolutionReturn(AuthUtil.generateParams(getApplicationContext(), NetworkParam.finishReturSolution(actionParameterPassData)))
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private void processObservableAcceptAdminSolution(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        resCenterActService
                .getApi()
                .acceptAdminResolution(AuthUtil.generateParams(getApplicationContext(), NetworkParam.acceptAdminSolution(actionParameterPassData)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultSubscriber());
    }

    private void processObservableReplySolution(ActionParameterPassData resCenterPass) {
        LocalCacheManager.ImageAttachment attachment = LocalCacheManager.ImageAttachment.Builder(resolutionID);
        LocalCacheManager.MessageConversation conversation = LocalCacheManager.MessageConversation.Builder(resolutionID).getCache();

        if (typeAction == ACTION_REPLY_CONVERSATION) {
            LocalCacheManager.StateDetailResCenter stateCache = LocalCacheManager.StateDetailResCenter.Builder(resolutionID).getCache();
            resCenterPass.setRefundAmount(String.valueOf(0));
            resCenterPass.setSolutionState(String.valueOf(0));
            resCenterPass.setTroubleState(String.valueOf(0));
            resCenterPass.setEdiSolFlag(String.valueOf(0));
            resCenterPass.setFlagReceived(String.valueOf(stateCache.getLastFlagReceived()));
        }

        resCenterPass.setReplyMsg(conversation.getMessage());
        resCenterPass.setListImages(attachment.getCache());

        Observable.just(resCenterPass)
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.getListImages().size() == 0) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
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
                        if (actionParameterPassData.getListImages().size() == 0) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(actionParameterPassData);
                        } else {
                            return getObservableUploadingFile(actionParameterPassData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        return getObservableReplyConversationValidation(actionParameterPassData);
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.getByPassFlag()) {
                            /**
                             * No need createResolutionPicture if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(actionParameterPassData);
                        } else {
                            return getObservableCreateResolutionPicture(actionParameterPassData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData actionParameterPassData) {
                        if (actionParameterPassData.getByPassFlag()) {
                            /**
                             * No need replyConversationSubmit if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(actionParameterPassData);
                        } else {
                            return getObservableReplyConversationSubmit(actionParameterPassData);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<ActionParameterPassData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String message;
                        Integer statusRequest;

                        if (e instanceof IOException) {
                            statusRequest = STATUS_TIME_OUT;
                            message = MESSAGE_TIMEOUT_INFO;
                        } else {
                            statusRequest = STATUS_ERROR;
                            message = e.getMessage();
                        }

                        Log.d(TAG + "-step6", message);
                        Bundle resultData = new Bundle();
                        resultData.putInt(EXTRA_PARAM_ACTION_TYPE, typeAction);
                        resultData.putInt(EXTRA_PARAM_NETWORK_ERROR_TYPE, statusRequest);
                        resultData.putString(EXTRA_PARAM_NETWORK_ERROR_MESSAGE, message);
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onNext(ActionParameterPassData data) {
                        Log.d(TAG + "-step6", String.valueOf(data));
                        LocalCacheManager.ImageAttachment.Builder(resolutionID).clearAll();
                        LocalCacheManager.MessageConversation.Builder(resolutionID).clear();

                        Bundle resultData = new Bundle();
                        resultData.putInt(EXTRA_PARAM_ACTION_TYPE, typeAction);
                        resultData.putParcelable(EXTRA_PARAM_RESPONSE_DATA, data.getResCenterActionData());
                        receiver.send(STATUS_FINISHED, resultData);
                    }
                });

    }

    private Observable<ActionParameterPassData> getObservableReplyConversationValidation(ActionParameterPassData actionParameterPassData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> replyConversationValidation =
                resCenterActService
                        .getApi()
                        .replyConversationValidation(
                                AuthUtil.generateParams(
                                        getApplicationContext(),
                                        NetworkParam.replyConversationValidation(actionParameterPassData)
                                )
                        );

        return Observable.zip(Observable.just(actionParameterPassData), replyConversationValidation, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionParameterPassData>() {
            @Override
            public ActionParameterPassData call(ActionParameterPassData actionParameterPassData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step3", response.body().getStrResponse());
                if (response.isSuccessful()) {
                    ResCenterActionData responseData = response.body().convertDataObj(ResCenterActionData.class);
                    if (responseData.isSuccess()) {
                        if (responseData.getPostKey() != null) {
                            actionParameterPassData.setPostKey(responseData.getPostKey());
                            actionParameterPassData.setByPassFlag(false);
                        } else {
                            actionParameterPassData.setResCenterActionData(responseData);
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

    private Observable<ActionParameterPassData> getObservableCreateResolutionPicture(final ActionParameterPassData actionParameterPassData) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                "http://" + actionParameterPassData.getUploadHost())
                .setIdentity()
                .addParam("attachment_string", actionParameterPassData.getAttachmentString())
                .addParam("file_path", actionParameterPassData.getFilePath())
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

    private Observable<ActionParameterPassData> getObservableReplyConversationSubmit(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = AuthUtil.generateParams(getApplicationContext(), NetworkParam.replyConversationSubmit(actionParameterPassData));
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> replyConversationSubmit = resCenterActService.getApi().replyConversationSubmit(params);

        return Observable.zip(Observable.just(actionParameterPassData), replyConversationSubmit, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionParameterPassData>() {
            @Override
            public ActionParameterPassData call(ActionParameterPassData previousData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step5", response.body().getStrResponse());

                if (response.isSuccessful()) {
                    ResCenterActionData temp = response.body().convertDataObj(ResCenterActionData.class);
                    if (temp.isSuccess()) {
                        previousData.setResCenterActionData(temp);
                        return previousData;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessages().toString());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }

    private Observable<ActionParameterPassData> getObservableUploadingFile(final ActionParameterPassData actionParameterPassData) {
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
                actionParameterPassData.setFilePath(attachmentCompiledString);
                actionParameterPassData.setPhotos(attachmentCompiledString);
                return actionParameterPassData;
            }
        });
    }

    private Observable<List<AttachmentResCenterVersion2DB>> uploading(final ActionParameterPassData actionParameterPassData) {
        return Observable
                .from(actionParameterPassData.getListImages())
                .flatMap(new Func1<AttachmentResCenterVersion2DB, Observable<AttachmentResCenterVersion2DB>>() {
                    @Override
                    public Observable<AttachmentResCenterVersion2DB> call(AttachmentResCenterVersion2DB attachmentResCenterDB) {
                        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                                "http://" + actionParameterPassData.getUploadHost())
                                .setIdentity()
                                .addParam("server_id", String.valueOf(actionParameterPassData.getServerID()))
                                .compileAllParam()
                                .finish();

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
