package com.tokopedia.transaction.purchase.interactor;

import android.content.Context;

import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.UploadImageService;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * TxUploadInteractorImpl
 * Created by anggaprasetiyo on 8/11/16.
 */
public class TxUploadInteractorImpl implements TxUploadInteractor {
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    private final GenerateHostActService generateHostActService;
    private final CompositeSubscription compositeSubscription;
    private final UploadImageService uploadImageService;
    private final TXOrderActService txActService;

    public TxUploadInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.generateHostActService = new GenerateHostActService();
        this.uploadImageService = new UploadImageService();
        this.txActService = new TXOrderActService();
    }

    @Override
    public void uploadImageProof(final Context context, final String imagePath, final TxVerData txVerData,
                                 final OnImageProofUpload listener) {
        Map<String, String> params = new HashMap<>();
        params.put("new_add", "2");
        Observable<GeneratedHost> observableGeneratedHost = generateHostActService.getApi()
                .generateHost(AuthUtil.generateParams(context, params));

        Func1<GeneratedHost, Observable<Response<TkpdResponse>>> funcUploadImage =
                new Func1<GeneratedHost, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(GeneratedHost generatedHost) {
                        String uploadUrl = "http://" + generatedHost.getUploadHost()
                                + "/web-service/v4/action/upload-image/upload_proof_image.pl/";

                        Map<String, String> paramsMap = AuthUtil.generateParams(context,
                                new HashMap<String, String>());
                        File file = new File(imagePath);
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                                paramsMap.get(PARAM_USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                                paramsMap.get(PARAM_DEVICE_ID));
                        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"),
                                paramsMap.get(PARAM_OS_TYPE));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                                paramsMap.get(PARAM_HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                                paramsMap.get(PARAM_TIMESTAMP));
                        RequestBody paymentImage = RequestBody.create(MediaType.parse("image/*"),
                                file);
                        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"),
                                String.valueOf(generatedHost.getServerId()));
                        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), "");
                        RequestBody paymentId = RequestBody.create(MediaType.parse("text/plain"),
                                txVerData.getPaymentId());
                        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), "1");

                        Map<String, RequestBody> requestBodyMap = new HashMap<>();
                        requestBodyMap.put(PARAM_USER_ID, userId);
                        requestBodyMap.put(PARAM_DEVICE_ID, deviceId);
                        requestBodyMap.put(PARAM_OS_TYPE, osType);
                        requestBodyMap.put(PARAM_HASH, hash);
                        requestBodyMap.put(PARAM_TIMESTAMP, deviceTime);
                        requestBodyMap.put("new_add", newAdd);
                        requestBodyMap.put("payment_id", paymentId);
                        requestBodyMap.put("server_id", serverId);
                        requestBodyMap.put("token", token);
                        return uploadImageService.getApi()
                                .uploadImageProof(uploadUrl, requestBodyMap, paymentImage);
                    }
                };

        Func1<Response<TkpdResponse>, Observable<Response<TkpdResponse>>> funcValidateImage =
                new Func1<Response<TkpdResponse>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Response<TkpdResponse> tkpdResponseResponse) {
                        JSONObject jsonData = tkpdResponseResponse.body().getJsonData();
                        String picObj;
                        String picSrc;
                        try {
                            picObj = jsonData.getString("pic_obj");
                            picSrc = jsonData.getString("pic_src");
                        } catch (JSONException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                        Map<String, String> params = new HashMap<>();
                        params.put("pic_src", picSrc);
                        params.put("pic_obj", picObj);
                        return txActService.getApi()
                                .uploadValidProofByPayment(AuthUtil.generateParams(context, params));
                    }
                };
        compositeSubscription.add(observableGeneratedHost
                .flatMap(funcUploadImage)
                .flatMap(funcValidateImage)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onFailed("Gagal Mengupload gambar, silahkan coba lagi");
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                        if (tkpdResponseResponse.isSuccessful() && !tkpdResponseResponse.body().isError())
                            listener.onSuccess(tkpdResponseResponse.body().getStatusMessages().get(0));
                    }
                }));
    }

    @Override
    public void unSubscribeObservable() {
        if (compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

}
