package com.tokopedia.inbox.rescenter.edit.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.UploadResCenterImageData;
import com.tokopedia.inbox.rescenter.edit.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.edit.model.passdata.ActionResponseData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;
import com.tokopedia.inbox.rescenter.utils.UploadImageResCenter;

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
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 8/25/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();
    private static final String ERROR_UNKNOWN = "$@1";
    private static final String ERROR_TIMEOUT = "$@2";
    private static final String ERROR_SERVER_ERROR = "$@3";
    private static final String ERROR_BAD_REQUEST = "$@4";
    private static final String ERROR_FORBIDDEN = "$@5";
    private static final String ERROR_NULL = "$@6";

    private final CompositeSubscription compositeSubscription;
    private final InboxResCenterService inboxResCenterService;

    public RetrofitInteractorImpl() {
        this.inboxResCenterService = new InboxResCenterService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getEditResolutionForm(@NonNull final Context context,
                                      @NonNull final Map<String, String> params,
                                      @NonNull final GetEditResolutionFormListener listener) {

        listener.onStart();

        compositeSubscription.add(
                Observable.just(params)
                        .flatMap(new Func1<Map<String, String>, Observable<EditResCenterFormData>>() {
                            @Override
                            public Observable<EditResCenterFormData> call(Map<String, String> stringStringMap) {
                                Observable<Response<TkpdResponse>> getFormRetrofit = inboxResCenterService.getApi()
                                        .getEditResolutionForm(AuthUtil.generateParams(context, params));

                                Observable<Response<TkpdResponse>> getProductListRetrofit = inboxResCenterService.getApi()
                                        .getResCenterProductList(AuthUtil.generateParams(context, params));

                                return Observable.zip(getFormRetrofit, getProductListRetrofit, new Func2<Response<TkpdResponse>, Response<TkpdResponse>, EditResCenterFormData>() {
                                    @Override
                                    public EditResCenterFormData call(Response<TkpdResponse> responseForm, Response<TkpdResponse> responseProduct) {
                                        if (responseForm.isSuccessful() && responseProduct.isSuccessful()) {
                                            if (!responseForm.body().isError() && !responseProduct.body().isError()) {
                                                EditResCenterFormData modelData = responseForm.body().convertDataObj(EditResCenterFormData.class);
                                                modelData.setListProd(responseProduct.body().convertDataObj(EditResCenterFormData.class).getListProd());
                                                return modelData;
                                            } else {
                                                if (responseForm.body().isNullData() || responseForm.body().isNullData()) {
                                                    throw new RuntimeException(ERROR_NULL);
                                                } else {
                                                    if (responseForm.body().isError()) {
                                                        throw new RuntimeException(responseForm.body().getErrorMessages().get(0));
                                                    } else {
                                                        throw new RuntimeException(responseProduct.body().getErrorMessages().get(0));
                                                    }
                                                }
                                            }
                                        } else {
                                            if (!responseForm.isSuccessful()) {
                                                createCustomErrorHandler(responseForm.code());
                                            } else {
                                                createCustomErrorHandler(responseProduct.code());
                                            }
                                        }
                                        return null;
                                    }
                                });
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<EditResCenterFormData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                                if (e instanceof IOException) {
                                    listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                        @Override
                                        public void onRetryClicked() {
                                            getEditResolutionForm(context, params, listener);
                                        }
                                    });
                                } else if (e instanceof RuntimeException) {
                                    switch (e.getMessage()) {
                                        case ERROR_UNKNOWN:
                                            listener.onTimeOut(null);
                                            break;
                                        case ERROR_TIMEOUT:
                                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getEditResolutionForm(context, params, listener);
                                                }
                                            });
                                            break;
                                        case ERROR_SERVER_ERROR:
                                            listener.onTimeOut(null);
                                            break;
                                        case ERROR_BAD_REQUEST:
                                            listener.onTimeOut(null);
                                            break;
                                        case ERROR_FORBIDDEN:
                                            listener.onTimeOut(null);
                                            listener.onFailAuth();
                                            break;
                                        case ERROR_NULL:
                                            listener.onTimeOut(null);
                                            break;
                                        default:
                                            listener.onError(e.getMessage());
                                            break;
                                    }
                                } else {
                                    listener.onTimeOut(null);
                                }
                            }

                            @Override
                            public void onNext(EditResCenterFormData data) {
                                listener.onSuccess(data);
                            }
                        })
        );
    }

    private void createCustomErrorHandler(int code) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                throw new RuntimeException(ERROR_UNKNOWN);
            }

            @Override
            public void onTimeout() {
                throw new RuntimeException(ERROR_TIMEOUT);
            }

            @Override
            public void onServerError() {
                throw new RuntimeException(ERROR_SERVER_ERROR);
            }

            @Override
            public void onBadRequest() {
                throw new RuntimeException(ERROR_BAD_REQUEST);
            }

            @Override
            public void onForbidden() {
                throw new RuntimeException(ERROR_FORBIDDEN);
            }
        }, code);
    }

    @Override
    public void getSolution(@NonNull final Context context,
                            @NonNull final Map<String, String> params,
                            @NonNull final FormSolutionListener listener) {
        Observable<Response<TkpdResponse>> observable = inboxResCenterService.getApi()
                .getSolutionList(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getSolution(context, params, listener);
                        }
                    });
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        Log.d(TAG, "onNext: " + response.body());
                        EditResCenterFormData data = response.body().convertDataObj(EditResCenterFormData.class);
                        listener.onSuccess(data.getListSolution());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessageJoined());
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getSolution(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void postEditResolution(@NonNull final Context context,
                                   @NonNull final ActionParameterPassData passData,
                                   @NonNull final ResultEditResolutionListener listener) {

        listener.onStart();

        compositeSubscription.add(Observable.just(passData)
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.getAttachmentData() == null
                                || passData.getAttachmentData().isEmpty()) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(passData);
                        } else {
                            GenerateHostActService generateHostActService = new GenerateHostActService();
                            Observable<GeneratedHost> generateHost = generateHostActService.getApi().generateHost(AuthUtil.generateParams(context, NetworkParam.generateHost()));
                            return Observable.zip(Observable.just(passData), generateHost, new Func2<ActionParameterPassData, GeneratedHost, ActionParameterPassData>() {
                                @Override
                                public ActionParameterPassData call(ActionParameterPassData passData, GeneratedHost generatedHost) {
                                    if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                                        passData.setServerID(String.valueOf(generatedHost.getServerId()));
                                        passData.setUploadHost(generatedHost.getUploadHost());
                                        return passData;
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
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.getAttachmentData() == null
                                || passData.getAttachmentData().isEmpty()) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(passData);
                        } else {
                            return getObservableUploadingFile(context, passData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        return getObservableEditResolutionValidation(context, NetworkParam.paramEditResCenterValidation(passData), passData);
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.isByPassFlag()) {
                            /**
                             * No need createResolutionPicture if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(passData);
                        } else {
                            return getObservableEditResolutionPicture(context, passData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionResponseData>>() {
                    @Override
                    public Observable<ActionResponseData> call(ActionParameterPassData passData) {
                        if (passData.isByPassFlag()) {
                            /**
                             * No need replyConversationSubmit if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(passData.getActionResponseData());
                        } else {
                            return getObservableEditResolutionSubmit(context, passData);
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
                        Log.d(TAG + "-step6", e.getMessage());
                        if (e instanceof IOException) {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    postEditResolution(context, passData, listener);
                                }
                            });
                        } else {
                            listener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ActionResponseData data) {
                        Log.d(TAG + "-step6", String.valueOf(data));
                        LocalCacheManager.AttachmentEditResCenter.Builder(passData.getResolutionID()).clearAll();
                        listener.onSuccess();
                    }
                }));
    }

    private Observable<ActionParameterPassData> getObservableUploadingFile(Context context, ActionParameterPassData passData) {
        return Observable.zip(Observable.just(passData), uploading(context, passData), new Func2<ActionParameterPassData, List<AttachmentResCenterVersion2DB>, ActionParameterPassData>() {
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

    private Observable<List<AttachmentResCenterVersion2DB>> uploading(final Context context,
                                                                      final ActionParameterPassData passData) {
        return Observable
                .from(passData.getAttachmentData())
                .flatMap(new Func1<AttachmentResCenterVersion2DB, Observable<AttachmentResCenterVersion2DB>>() {
                    @Override
                    public Observable<AttachmentResCenterVersion2DB> call(AttachmentResCenterVersion2DB attachmentResCenterDB) {
                        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context,
                                "https://" + passData.getUploadHost())
                                .setIdentity()
                                .addParam("server_id", String.valueOf(passData.getServerID()))
                                .compileAllParam()
                                .finish();

                        // https:// uploadhost /upload/attachment
                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(attachmentResCenterDB.imagePath));
                        } catch (IOException e) {
                            throw new RuntimeException(context.getString(R.string.error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
                        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("server_id"));

                        Log.d(TAG + "(step 2):host", passData.getUploadHost());
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

    private Observable<ActionParameterPassData> getObservableEditResolutionValidation(Context context, Map<String, String> params, ActionParameterPassData passData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> createResolutionValidation =
                resCenterActService
                        .getApi()
                        .replyConversationValidationNew(
                                AuthUtil.generateParams(context, params));

        return Observable.zip(Observable.just(passData), createResolutionValidation, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionParameterPassData>() {
            @Override
            public ActionParameterPassData call(ActionParameterPassData passData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step3", response.body().getStrResponse());
                if (response.isSuccessful()) {
                    ActionResponseData responseData = response.body().convertDataObj(ActionResponseData.class);
                    if (responseData != null && responseData.isSuccess()) {
                        if (responseData.getPostKey() != null) {
                            passData.setPostKey(responseData.getPostKey());
                            passData.setByPassFlag(false);
                        } else {
                            passData.setActionResponseData(responseData);
                            passData.setByPassFlag(true);
                        }
                        return passData;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessageJoined());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }

    private Observable<ActionParameterPassData> getObservableEditResolutionPicture(Context context, final ActionParameterPassData passData) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context,
                "http://" + passData.getUploadHost())
                .setIdentity()
                .addParam("attachment_string", passData.getAttachmentString())
                .addParam("file_path", passData.getAttachmentString())
                .addParam("server_id", String.valueOf(passData.getServerID()))
                .addParam("web_service", String.valueOf(1))
                .compileAllParam()
                .finish();

        RequestBody attachmentString = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("attachment_string"));
        RequestBody filePath = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("file_path"));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("server_id"));
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("web_service"));

        Log.d(TAG + "(step 4):host", passData.getUploadHost());
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

        return Observable.zip(Observable.just(passData), createResolutionPicture,
                new Func2<ActionParameterPassData, UploadResCenterImageData, ActionParameterPassData>() {
                    @Override
                    public ActionParameterPassData call(ActionParameterPassData passData, UploadResCenterImageData response) {
                        Log.d(TAG + "-step4", response.toString());
                        if (response.getData() != null) {
                            if (response.getData() != null && response.getData().isSuccess()) {
                                passData.setFileUploaded(response.getData().getFileUploaded());
                                return passData;
                            } else {
                                throw new RuntimeException(response.getMessageError().get(0));
                            }
                        } else {
                            throw new RuntimeException("create resolution picture error");
                        }
                    }
                });
    }

    private Observable<ActionResponseData> getObservableEditResolutionSubmit(Context context, ActionParameterPassData passData) {
        Map<String, String> params = AuthUtil.generateParams(context, NetworkParam.paramEditResCenterSubmit(passData));
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> createResolutionSubmit = resCenterActService.getApi().replyConversationSubmit(params);

        return Observable.zip(Observable.just(passData), createResolutionSubmit, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionResponseData>() {
            @Override
            public ActionResponseData call(ActionParameterPassData previousData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step5", response.body().getStrResponse());

                if (response.isSuccessful()) {
                    ActionResponseData temp = response.body().convertDataObj(ActionResponseData.class);
                    if (temp != null && temp.isSuccess()) {
                        return temp;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessageJoined());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }

    @Override
    public void postEditSellerResolution(@NonNull final Context context,
                                         @NonNull final ActionParameterPassData passData,
                                         @NonNull final ResultEditResolutionListener listener) {
        listener.onStart();

        compositeSubscription.add(Observable.just(passData)
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.getAttachmentData() == null
                                || passData.getAttachmentData().isEmpty()) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(passData);
                        } else {
                            GenerateHostActService generateHostActService = new GenerateHostActService();
                            Observable<GeneratedHost> generateHost = generateHostActService.getApi().generateHost(AuthUtil.generateParams(context, NetworkParam.generateHost()));
                            return Observable.zip(Observable.just(passData), generateHost, new Func2<ActionParameterPassData, GeneratedHost, ActionParameterPassData>() {
                                @Override
                                public ActionParameterPassData call(ActionParameterPassData passData, GeneratedHost generatedHost) {
                                    if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                                        passData.setServerID(String.valueOf(generatedHost.getServerId()));
                                        passData.setUploadHost(generatedHost.getUploadHost());
                                        return passData;
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
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.getAttachmentData() == null
                                || passData.getAttachmentData().isEmpty()) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(passData);
                        } else {
                            return getObservableUploadingFile(context, passData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        return getObservableEditResolutionValidation(context, NetworkParam.paramEditResCenterSellerValidation(passData), passData);
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.isByPassFlag()) {
                            /**
                             * No need createResolutionPicture if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(passData);
                        } else {
                            return getObservableEditResolutionPicture(context, passData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionResponseData>>() {
                    @Override
                    public Observable<ActionResponseData> call(ActionParameterPassData passData) {
                        if (passData.isByPassFlag()) {
                            /**
                             * No need replyConversationSubmit if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(passData.getActionResponseData());
                        } else {
                            return getObservableEditResolutionSubmit(context, passData);
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
                        Log.d(TAG + "-step6", e.getMessage());
                        if (e instanceof IOException) {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    postEditResolution(context, passData, listener);
                                }
                            });
                        } else {
                            listener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ActionResponseData data) {
                        Log.d(TAG + "-step6", String.valueOf(data));
                        LocalCacheManager.AttachmentEditResCenter.Builder(passData.getResolutionID()).clearAll();
                        listener.onSuccess();
                    }
                }));
    }

    @Override
    public void getAppealResolutionForm(@NonNull final Context context,
                                        @NonNull final Map<String, String> params,
                                        @NonNull final GetAppealResolutionFormListener listener) {

        listener.onStart();

        Observable<Response<TkpdResponse>> getFormRetrofit = inboxResCenterService.getApi()
                .getAppealResolutionForm(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getAppealResolutionForm(context, params, listener);
                        }
                    });
                } else {
                    listener.onTimeOut(null);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(AppealResCenterFormData.class));
                    } else {
                        if (response.body().isNullData()) listener.onTimeOut(null);
                        else listener.onError(response.body().getErrorMessageJoined());
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onTimeOut(null);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getAppealResolutionForm(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onTimeOut(null);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onTimeOut(null);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onTimeOut(null);
                            listener.onFailAuth();
                        }
                    }, response.code());
                }
            }

        };

        compositeSubscription.add(
                getFormRetrofit.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void postAppealResolution(@NonNull final Context context,
                                     @NonNull final ActionParameterPassData passData,
                                     @NonNull final ResultEditResolutionListener listener) {
        listener.onStart();

        compositeSubscription.add(Observable.just(passData)
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.getAttachmentData() == null
                                || passData.getAttachmentData().isEmpty()) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(passData);
                        } else {
                            GenerateHostActService generateHostActService = new GenerateHostActService();
                            Observable<GeneratedHost> generateHost = generateHostActService.getApi().generateHost(AuthUtil.generateParams(context, NetworkParam.generateHost()));
                            return Observable.zip(Observable.just(passData), generateHost, new Func2<ActionParameterPassData, GeneratedHost, ActionParameterPassData>() {
                                @Override
                                public ActionParameterPassData call(ActionParameterPassData passData, GeneratedHost generatedHost) {
                                    if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                                        passData.setServerID(String.valueOf(generatedHost.getServerId()));
                                        passData.setUploadHost(generatedHost.getUploadHost());
                                        return passData;
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
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.getAttachmentData() == null
                                || passData.getAttachmentData().isEmpty()) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(passData);
                        } else {
                            return getObservableUploadingFile(context, passData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        return getObservableAppealResolutionValidation(context, NetworkParam.paramAppealResCenterValidation(passData), passData);
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionParameterPassData>>() {
                    @Override
                    public Observable<ActionParameterPassData> call(ActionParameterPassData passData) {
                        if (passData.isByPassFlag()) {
                            /**
                             * No need createResolutionPicture if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(passData);
                        } else {
                            return getObservableEditResolutionPicture(context, passData);
                        }
                    }
                })
                .flatMap(new Func1<ActionParameterPassData, Observable<ActionResponseData>>() {
                    @Override
                    public Observable<ActionResponseData> call(ActionParameterPassData passData) {
                        if (passData.isByPassFlag()) {
                            /**
                             * No need replyConversationSubmit if already returned response data
                             * just return the parameter
                             * bypass until step 6
                             */
                            return Observable.just(passData.getActionResponseData());
                        } else {
                            return getObservableAppealResolutionSubmit(context, passData);
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
                        Log.d(TAG + "-step6", e.getMessage());
                        if (e instanceof IOException) {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    postAppealResolution(context, passData, listener);
                                }
                            });
                        } else {
                            listener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ActionResponseData data) {
                        Log.d(TAG + "-step6", String.valueOf(data));
                        LocalCacheManager.AttachmentEditResCenter.Builder(passData.getResolutionID()).clearAll();
                        listener.onSuccess();
                    }
                }));
    }

    private Observable<ActionParameterPassData> getObservableAppealResolutionValidation(Context context, Map<String, String> params, ActionParameterPassData passData) {
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> createResolutionValidation =
                resCenterActService
                        .getApi()
                        .rejectAdminResolutionValidation(
                                AuthUtil.generateParams(context, params));

        return Observable.zip(Observable.just(passData), createResolutionValidation, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionParameterPassData>() {
            @Override
            public ActionParameterPassData call(ActionParameterPassData passData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step3", response.body().getStrResponse());
                if (response.isSuccessful()) {
                    ActionResponseData responseData = response.body().convertDataObj(ActionResponseData.class);
                    if (responseData != null && responseData.isSuccess()) {
                        if (responseData.getPostKey() != null) {
                            passData.setPostKey(responseData.getPostKey());
                            passData.setByPassFlag(false);
                        } else {
                            passData.setActionResponseData(responseData);
                            passData.setByPassFlag(true);
                        }
                        return passData;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessageJoined());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }

    private Observable<ActionResponseData> getObservableAppealResolutionSubmit(Context context, ActionParameterPassData passData) {
        Map<String, String> params = AuthUtil.generateParams(context, NetworkParam.paramEditResCenterSubmit(passData));
        ResCenterActService resCenterActService = new ResCenterActService();
        Observable<Response<TkpdResponse>> rejectAdminSolutionSubmit = resCenterActService.getApi().rejectAdminResolutionSubmit(params);

        return Observable.zip(Observable.just(passData), rejectAdminSolutionSubmit, new Func2<ActionParameterPassData, Response<TkpdResponse>, ActionResponseData>() {
            @Override
            public ActionResponseData call(ActionParameterPassData previousData, Response<TkpdResponse> response) {
                Log.d(TAG + "-step5", response.body().getStrResponse());

                if (response.isSuccessful()) {
                    ActionResponseData temp = response.body().convertDataObj(ActionResponseData.class);
                    if (temp != null && temp.isSuccess()) {
                        return temp;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessageJoined());
                    }
                } else {
                    throw new RuntimeException(String.valueOf(response.code()));
                }
            }
        });
    }
}
