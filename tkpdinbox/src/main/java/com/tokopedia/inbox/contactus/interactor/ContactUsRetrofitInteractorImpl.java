package com.tokopedia.inbox.contactus.interactor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.inbox.contactus.model.GenerateHostPass;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.etc.ContactUsService;
import com.tokopedia.core.network.apiservices.etc.ContactUsWsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.contactus.UploadImageContactUsParam;
import com.tokopedia.inbox.contactus.model.ContactUsPass;
import com.tokopedia.inbox.contactus.model.CreateTicketResult;
import com.tokopedia.inbox.contactus.model.ImageUpload;
import com.tokopedia.inbox.contactus.model.ImageUploadResult;
import com.tokopedia.inbox.contactus.model.solution.SolutionResult;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
 * Created by nisie on 8/12/16.
 */
public class ContactUsRetrofitInteractorImpl implements ContactUsRetrofitInteractor {

    private static final String TAG = ContactUsRetrofitInteractorImpl.class.getSimpleName();
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";

    private final CompositeSubscription compositeSubscription;
    private final ContactUsService contactUsService;

    private static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private final ContactUsWsService contactUsWService;

    public ContactUsRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.contactUsService = new ContactUsService();
        this.contactUsWService = new ContactUsWsService();
    }

    @Override
    public void sendTicket(@NonNull final Context context, @NonNull final ContactUsPass params, @NonNull final SendTicketListener listener) {
        Observable<Response<TkpdResponse>> observable = Observable.just(params)
                .flatMap(new Func1<ContactUsPass, Observable<ContactUsPass>>() {
                    @Override
                    public Observable<ContactUsPass> call(final ContactUsPass contactUsPass) {

                        if (isHasPictures(contactUsPass)) {
                            return contactUsService.getApi()
                                    .createTicketValidation(AuthUtil.generateParams(context, contactUsPass.getCreateTicketValidationParam()))
                                    .map(new Func1<Response<TkpdResponse>, ContactUsPass>() {
                                        @Override
                                        public ContactUsPass call(Response<TkpdResponse> tkpdResponse) {
                                            if (tkpdResponse.isSuccessful() && tkpdResponse.body() != null && !tkpdResponse.body().isError()) {
                                                CreateTicketResult result = tkpdResponse.body().convertDataObj(CreateTicketResult.class);
                                                if (result.getIsSuccess() == 1) {
                                                    contactUsPass.setPostKey(result.getPostKey());
                                                    return contactUsPass;
                                                } else {
                                                    String errorMessage = "";
                                                    for (int i = 0; i < tkpdResponse.body().getErrorMessages().size(); i++) {
                                                        errorMessage += tkpdResponse.body().getErrorMessages().get(i);
                                                    }
                                                    throw new RuntimeException(errorMessage);
                                                }
                                            } else if (tkpdResponse.body() != null && tkpdResponse.body().getErrorMessages() != null) {
                                                throw new RuntimeException(tkpdResponse.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                                            } else {
                                                new ErrorHandler(new ErrorListener() {
                                                    @Override
                                                    public void onUnknown() {
                                                        throw new RuntimeException(context.getString(R.string.default_request_error_unknown));
                                                    }

                                                    @Override
                                                    public void onTimeout() {
                                                        throw new RuntimeException(context.getString(R.string.default_request_error_timeout));

                                                    }

                                                    @Override
                                                    public void onServerError() {
                                                        throw new RuntimeException(context.getString(R.string.default_request_error_internal_server));

                                                    }

                                                    @Override
                                                    public void onBadRequest() {
                                                        throw new RuntimeException(context.getString(R.string.default_request_error_bad_request));

                                                    }

                                                    @Override
                                                    public void onForbidden() {
                                                        throw new RuntimeException(context.getString(R.string.default_request_error_forbidden_auth));

                                                    }
                                                }, tkpdResponse.code());
                                                throw new RuntimeException(context.getString(R.string.failed_create_ticket));
                                            }
                                        }
                                    });
                        } else {
                            return Observable.just(contactUsPass);
                        }
                    }
                })
                .flatMap(new Func1<ContactUsPass, Observable<ContactUsPass>>() {
                    @Override
                    public Observable<ContactUsPass> call(ContactUsPass contactUsPass) {
                        if (isHasPictures(contactUsPass)) {
                            return getObservableGenerateHost(context, contactUsPass);
                        } else {
                            return Observable.just(contactUsPass);
                        }
                    }
                })
                .flatMap(new Func1<ContactUsPass, Observable<ContactUsPass>>() {
                    @Override
                    public Observable<ContactUsPass> call(ContactUsPass contactUsPass) {
                        if (isHasPictures(contactUsPass)) {
                            return getObservableUploadingFile(context, contactUsPass);
                        } else {
                            return Observable.just(contactUsPass);
                        }
                    }
                })
                .flatMap(new Func1<ContactUsPass, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(ContactUsPass contactUsPass) {
                        if (isHasPictures(contactUsPass)) {
                            return contactUsService.getApi()
                                    .createTicket(AuthUtil.generateParams(context, contactUsPass.getSubmitParam()));
                        } else {
                            return contactUsService.getApi()
                                    .createTicketValidation(AuthUtil.generateParams(context, contactUsPass.getCreateTicketValidationParam()));
                        }
                    }
                });


        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                } else {
                    listener.onError(e.getMessage());
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (!response.body().isError()
                                && response.body().getJsonData().getString("is_success").equals("1")) {
                            listener.onSuccess();
                        } else {
                            if (response.body().getStatus().equals(TOO_MANY_REQUEST))
                                listener.onError(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                            else if (response.body().isNullData())
                                listener.onNullData();
                            else
                                listener.onError(response.body().getErrorMessages().get(0));
                        }
                    } catch (JSONException e) {
                        listener.onError(context.getString(R.string.failed_create_ticket));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(context.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    private Observable<ContactUsPass> getObservableGenerateHost(Context context,
                                                                final ContactUsPass contactUsPass) {
        GenerateHostPass paramGenerateHost = new GenerateHostPass();
        paramGenerateHost.setNewAdd("2");

        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        AccountsService accountsService = new AccountsService(bundle);
        return accountsService.getApi()
                .generateHost(AuthUtil.generateParams(context, paramGenerateHost.getGenerateHostParam()))
                .map(new Func1<GeneratedHost, ContactUsPass>() {
                    @Override
                    public ContactUsPass call(GeneratedHost generatedHost) {
                        if (generatedHost.getMessageError() == null || generatedHost.getMessageError().isEmpty()) {
                            contactUsPass.setGeneratedHost(generatedHost);
                            return contactUsPass;
                        } else {
                            throw new RuntimeException(generatedHost.getMessageError().get(0));
                        }
                    }
                });
    }

    private Observable<ContactUsPass> getObservableUploadingFile(final Context context,
                                                                 final ContactUsPass contactUsPass) {

        return Observable.zip(Observable.just(contactUsPass), uploadFile(context, contactUsPass),
                new Func2<ContactUsPass, List<ImageUpload>, ContactUsPass>() {
                    @Override
                    public ContactUsPass call(ContactUsPass pass, List<ImageUpload> imageUploads) {
                        pass.setAttachment((ArrayList<ImageUpload>) imageUploads);
                        return pass;
                    }
                });
    }

    private Observable<List<ImageUpload>> uploadFile(final Context context, final ContactUsPass contactUsPass) {
        return Observable
                .from(contactUsPass.getAttachment())
                .flatMap(new Func1<ImageUpload, Observable<ImageUpload>>() {
                    @Override
                    public Observable<ImageUpload> call(ImageUpload imageUpload) {
                        String uploadUrl = "http://" + contactUsPass.getGeneratedHost().getUploadHost();
                        NetworkCalculator networkCalculator = new NetworkCalculator(
                                NetworkConfig.POST, context,
                                uploadUrl)
                                .setIdentity()
                                .addParam(PARAM_IMAGE_ID, imageUpload.getImageId())
                                .addParam(PARAM_WEB_SERVICE, "1")
                                .compileAllParam()
                                .finish();

                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(imageUpload.getFileLoc()));
                        } catch (IOException e) {
                            throw new RuntimeException(context.getString(R.string.error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                                file);
                        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(PARAM_IMAGE_ID));
                        RequestBody web_service = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(PARAM_WEB_SERVICE));

                        Log.d(TAG + "(step 2):host = ", contactUsPass.getGeneratedHost().getUploadHost());
                        Observable<ImageUploadResult> upload;
                        if (SessionHandler.isV4Login(context)) {
                            upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageContactUsParam.class)
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
                                            imageId, web_service

                                    );
                        } else {
                            upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageContactUsParam.class)
                                    .uploadImagePublic(
                                            networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                            networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                            networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                            networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                            userId,
                                            deviceId,
                                            hash,
                                            deviceTime,
                                            fileToUpload,
                                            imageId,
                                            web_service);
                        }

                        return Observable.zip(Observable.just(imageUpload), upload, new Func2<ImageUpload, ImageUploadResult, ImageUpload>() {
                            @Override
                            public ImageUpload call(ImageUpload imageUpload, ImageUploadResult imageUploadResult) {
                                if (imageUploadResult.getData() != null) {
                                    imageUpload.setPicSrc(imageUploadResult.getData().getPicSrc());
                                    imageUpload.setPicObj(imageUploadResult.getData().getPicObj());
                                } else if (imageUploadResult.getMessageError() != null) {
                                    throw new RuntimeException(imageUploadResult.getMessageError());
                                }
                                return imageUpload;
                            }
                        });
                    }
                })
                .toList();
    }

    private boolean isHasPictures(ContactUsPass pass) {
        return pass.getAttachment() != null && pass.getAttachment().size() > 0;
    }

    @Override
    public void unsubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void commentRating(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final CommentRatingListener listener) {
        Observable<Response<TkpdResponse>> observable = contactUsWService.getApi()
                .commentRating(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError() && response.body().getJsonData().optString("is_success").equals("1")) {
                        listener.onSuccess(null);

                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().toString());
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
                            listener.onTimeout();
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
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }



    @Override
    public void getSolution(@NonNull final Context context, @NonNull String id, @NonNull final GetSolutionListener listener) {
        Observable<Response<TkpdResponse>> observable = contactUsService.getApi()
                .getSolution(id);

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(SolutionResult.class));
                    } else {
                        if (response.body().getStatus().equals(TOO_MANY_REQUEST))
                            listener.onError(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                        else if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(context.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, response.code());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}