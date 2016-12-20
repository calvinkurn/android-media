package com.tokopedia.inbox.rescenter.shipping.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.database.model.AttachmentResCenterDB;
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
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.UploadResCenterImageData;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsPostModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.utils.UploadImageResCenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
 * Created by hangnadi on 12/14/16.
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractor.class.getSimpleName();

    private final InboxResCenterService inboxResCenterService;
    private final ResCenterActService resCenterActService;
    private final CompositeSubscription compositeSubscription;

    public RetrofitInteractorImpl() {
        this.inboxResCenterService = new InboxResCenterService();
        this.resCenterActService = new ResCenterActService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void getShippingList(@NonNull final Context context,
                                @NonNull final TKPDMapParam params,
                                @NonNull final GetKurirListener listener) {

        listener.onStart();

        Observable<Response<TkpdResponse>> observable = inboxResCenterService.getApi().getCourierList(params);

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
                            getShippingList(context, params, listener);
                        }
                    });
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(ResCenterKurir.class));
                    } else {
                        if (response.body().isNullData()) listener.onError(null);
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(null);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getShippingList(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(null);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(null);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(null);
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        );
    }

    @Override
    public void storeShippingService(@NonNull final Context context,
                                     @NonNull final InputShippingParamsPostModel params,
                                     @NonNull final PostShippingListener listener) {
        compositeSubscription.add(Observable.just(params)
                .flatMap(new Func1<InputShippingParamsPostModel, Observable<InputShippingParamsPostModel>>() {
                    @Override
                    public Observable<InputShippingParamsPostModel> call(InputShippingParamsPostModel inputModel) {
                        if (inputModel.getAttachmentList().size() == 0) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(inputModel);
                        } else {
                            GenerateHostActService generateHostActService = new GenerateHostActService();
                            Observable<GeneratedHost> generateHost = generateHostActService.getApi().generateHost(AuthUtil.generateParams(context, NetworkParam.generateHost()));
                            return Observable.zip(Observable.just(inputModel), generateHost, new Func2<InputShippingParamsPostModel, GeneratedHost, InputShippingParamsPostModel>() {
                                @Override
                                public InputShippingParamsPostModel call(InputShippingParamsPostModel inputModel, GeneratedHost generatedHost) {
                                    if (generatedHost != null) {
                                        inputModel.setServerID(String.valueOf(generatedHost.getServerId()));
                                        inputModel.setUploadHost(generatedHost.getUploadHost());
                                        return inputModel;
                                    } else {
                                        throw new RuntimeException("ERROR GENERATE HOST");
                                    }
                                }
                            });
                        }
                    }
                })
                .flatMap(new Func1<InputShippingParamsPostModel, Observable<InputShippingParamsPostModel>>() {
                    @Override
                    public Observable<InputShippingParamsPostModel> call(InputShippingParamsPostModel inputModel) {
                        if (inputModel.getAttachmentList().size() == 0) {
                            /**
                             * No need generatehost if no uploading attachment
                             * just return the parameter
                             * bypass until step 3
                             */
                            return Observable.just(inputModel);
                        } else {
                            return getObservableUploadingFile(context, inputModel);
                        }
                    }
                })
                .flatMap(new Func1<InputShippingParamsPostModel, Observable<InputShippingParamsPostModel>>() {
                    @Override
                    public Observable<InputShippingParamsPostModel> call(InputShippingParamsPostModel inputModel) {
                        return getObservableInputShippingnValidation(inputModel);
                    }
                })
                .flatMap(new Func1<InputShippingParamsPostModel, Observable<InputShippingParamsPostModel>>() {
                    @Override
                    public Observable<InputShippingParamsPostModel> call(InputShippingParamsPostModel inputModel) {
//                        if (inputModel.getByPassFlag()) {
//                            /**
//                             * No need replyConversationSubmit if already returned response data
//                             * just return the parameter
//                             * bypass until step 6
//                             */
//                            return Observable.just(inputModel);
//                        } else {
//                            return getObservableReplyConversationSubmit(inputModel);
//                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<InputShippingParamsPostModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(InputShippingParamsPostModel data) {
                        Log.d(TAG + "-step6", String.valueOf(data));
                    }
                })
        );
    }

    private Observable<InputShippingParamsPostModel> getObservableInputShippingnValidation(InputShippingParamsPostModel inputModel) {
        return null;
    }

    private Observable<InputShippingParamsPostModel> getObservableUploadingFile(Context context, InputShippingParamsPostModel inputModel) {
        return Observable.zip(Observable.just(inputModel), doUploadFile(context, inputModel), new Func2<InputShippingParamsPostModel, List<AttachmentResCenterDB>, InputShippingParamsPostModel>() {
            @Override
            public InputShippingParamsPostModel call(InputShippingParamsPostModel inputModel, List<AttachmentResCenterDB> attachmentResCenterDBs) {
                return null;
            }
        });
    }

    private Observable<List<AttachmentResCenterDB>> doUploadFile(final Context context, final InputShippingParamsPostModel inputModel) {
        return Observable
                .from(inputModel.getAttachmentList())
                .flatMap(new Func1<AttachmentResCenterDB, Observable<AttachmentResCenterDB>>() {
                    @Override
                    public Observable<AttachmentResCenterDB> call(AttachmentResCenterDB attachmentResCenterDB) {
                        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context,
                                "http://" + inputModel.getUploadHost())
                                .setIdentity()
                                .addParam("server_id", String.valueOf(inputModel.getServerID()))
                                .compileAllParam()
                                .finish();

                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(attachmentResCenterDB.imagePath));
                        } catch (IOException e) {
                            throw new RuntimeException(context.getString(com.tokopedia.core.R.string.error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
                        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get("server_id"));

                        Log.d(TAG + "(step 2):host", inputModel.getUploadHost());
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

                        return Observable.zip(Observable.just(attachmentResCenterDB), upload, new Func2<AttachmentResCenterDB, UploadResCenterImageData, AttachmentResCenterDB>() {
                            @Override
                            public AttachmentResCenterDB call(AttachmentResCenterDB attachmentResCenterDB, UploadResCenterImageData uploadResCenterImageData) {
                                if (uploadResCenterImageData != null) {
                                    if (uploadResCenterImageData.getData() != null) {
                                        Log.d(TAG, "call: " + uploadResCenterImageData.getData().toString());
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
