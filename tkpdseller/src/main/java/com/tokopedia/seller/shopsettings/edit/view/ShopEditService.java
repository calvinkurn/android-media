package com.tokopedia.seller.shopsettings.edit.view;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tokopedia.core.myproduct.model.GenerateHostModel;
import com.tokopedia.core.network.apiservices.shop.MyShopActAfterService;
import com.tokopedia.core.network.apiservices.shop.MyShopActService;
import com.tokopedia.core.network.apiservices.shop.MyShopInfoActService;
import com.tokopedia.core.network.apiservices.shop.MyShopInfoService;
import com.tokopedia.core.network.apiservices.shop.apis.UploadShopLogo;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.seller.shopsettings.edit.constant.ShopEditServiceConstant;
import com.tokopedia.core.shop.model.OpenShopPictureModel;
import com.tokopedia.core.shop.model.ShopEditorModel;
import com.tokopedia.core.shop.model.ShopScheduleModel;
import com.tokopedia.core.shop.model.UpdateShopImageModel;
import com.tokopedia.core.shop.model.openShopSubmitData.OpenShopSubmitData;
import com.tokopedia.core.shop.model.openShopValidationData.OpenShopValidationData;
import com.tokopedia.core.shop.model.responseEdit.ResponseEdit;
import com.tokopedia.core.shop.model.shopData.Data;
import com.tokopedia.core.shop.model.uploadShopLogoData.UploadShopLogoData;
import com.tokopedia.seller.shopsettings.edit.utils.UploadPhotoShopTask;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
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

//import com.tokopedia.tkpd.shop.model.ShopCreateParams;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ShopEditService extends IntentService implements ShopEditServiceConstant {

    public static final String GENERATE_HOST = "GENERATE HOST ";
    public static final String UNKNOWN_ERROR = "Kesalahan tidak diketahui";
    public static final String UPLOAD_SHOP_LOGO = "UPLOAD SHOP LOGO ";
    public static final String OPEN_SHOP_VALIDATION = "OPEN SHOP VALIDATION ";
    public static final String OPEN_SHOP_PICTURE = "OPEN SHOP PICTURE ";
    public static final String OPEN_SHOP_SUBMIT = "OPEN SHOP SUBMIT ";
    public static final String SERVICE_TYPE = "SERVICE_TYPE";
    public static final String RETRY_DATA = "RETRY_DATA";
    String messageTAG = ShopEditService.class.getSimpleName() + " ";

    private AuthService service;
    private ResultReceiver receiver;
    private Gson gson;
    SessionHandler sessionHandler;

    public ShopEditService() {
        super("ShopEditorService");
    }

    public static void startShopService(Context context, DownloadResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, ShopEditService.class);
        if (receiver != null)
            intent.putExtra(ShopEditService.RECEIVER, receiver);

        // set mandatory param
        intent.putExtra(TYPE, type);

        /* Send optional extras to Download IntentService */
        switch (type) {
            case CREATE_SHOP:
                HashMap<String, String> openShopValidationParam = Parcels.unwrap(bundle.getParcelable(OPEN_SHOP_VALIDATION_PARAM));
                intent.putExtra(INPUT_IMAGE, bundle.getString(INPUT_IMAGE));
                intent.putExtra(OPEN_SHOP_VALIDATION_PARAM, Parcels.wrap(openShopValidationParam));
                break;
            case GET_SHOP_DATA:
                break;
            case POST_EDIT_DATA:
                ShopEditorModel shopEditorModel = Parcels.unwrap(bundle.getParcelable(MODEL_EDIT_SHOP_DATA));
                intent.putExtra(MODEL_EDIT_SHOP_DATA, Parcels.wrap(shopEditorModel));
                break;
            case UPDATE_SHOP_IMAGE:
                intent.putExtra(INPUT_IMAGE, bundle.getString(INPUT_IMAGE));
                break;
            case UPDATE_SHOP_SCHEDULE:
                ShopScheduleModel shopScheduleModel = Parcels.unwrap(bundle.getParcelable(MODEL_SCHEDULE_SHOP));
                intent.putExtra(MODEL_SCHEDULE_SHOP, Parcels.wrap(shopScheduleModel));
                break;
            case CREATE_SHOP_WITHOUT_IMAGE:
                HashMap<String, String> openShopValidationParamWithoutImg = Parcels.unwrap(bundle.getParcelable(OPEN_SHOP_VALIDATION_PARAM));
                intent.putExtra(OPEN_SHOP_VALIDATION_PARAM, Parcels.wrap(openShopValidationParamWithoutImg));
                break;
            default:
                throw new RuntimeException("unknown type for starting download !!!");
        }

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle running;

        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        gson = new GsonBuilder().create();
        sessionHandler = new SessionHandler(getApplicationContext());

        TKPDMapParam<String, String> params;

        switch (type) {
            case CREATE_SHOP:
                sendRunningStatus(type);
                running = new Bundle();
                running.putInt(TYPE, type);

                HashMap<String, String> openShopValidationParam = Parcels.unwrap(intent.getParcelableExtra(OPEN_SHOP_VALIDATION_PARAM));

                Map<String, Object> datasPassingAround = new HashMap<>();
                datasPassingAround.put(INPUT_IMAGE, intent.getStringExtra(INPUT_IMAGE));
                datasPassingAround.put(GENERATED_RETROFIR_INSTANCE_API, RetrofitUtils.createRetrofit().create(GeneratedHostActApi.class));
                datasPassingAround.put(OPEN_SHOP_VALIDATION_PARAM, openShopValidationParam);
                datasPassingAround.put(SERVICE_TYPE, type);
                createShop(datasPassingAround, type);
                break;
            case GET_SHOP_DATA:
                sendRunningStatus(type);
                service = new MyShopInfoService();
                params = new TKPDMapParam<>();
                ((MyShopInfoService) service).getApi().getInfo(AuthUtil.generateParamsNetwork(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case POST_EDIT_DATA:
                sendRunningStatus(type);
                ShopEditorModel shopEditorModel = Parcels.unwrap(intent.getParcelableExtra(MODEL_EDIT_SHOP_DATA));
                params = new TKPDMapParam<String, String>();
                params.put("closed_note", "");
                params.put("closed_until", "");
                params.put("reason", "");
                params.put("default_sort", "");
                params.put("short_desc", shopEditorModel.getmShopDesc());
                params.put("status", "");
                params.put("tag_line", shopEditorModel.getmShopSlogan());
                service = new MyShopInfoActService();
                ((MyShopInfoActService) service).getApi().updateInfo(AuthUtil.generateParamsNetwork(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case UPDATE_SHOP_IMAGE:
                sendRunningStatus(type);
                running = new Bundle();
                running.putInt(TYPE, type);

                datasPassingAround = new HashMap<>();
                datasPassingAround.put(INPUT_IMAGE, intent.getStringExtra(INPUT_IMAGE));
                datasPassingAround.put(GENERATED_RETROFIR_INSTANCE_API, RetrofitUtils.createRetrofit().create(GeneratedHostActApi.class));
                postUpdateShopImage(datasPassingAround, type);
                break;
            case UPDATE_SHOP_SCHEDULE:
                sendRunningStatus(type);
                ShopScheduleModel shopScheduleModel = Parcels.unwrap(intent.getParcelableExtra(MODEL_SCHEDULE_SHOP));
                params = new TKPDMapParam<String, String>();
                params.put("closed_note", shopScheduleModel.getClose_note());
                params.put("close_start", shopScheduleModel.getClose_start());
                params.put("close_end", shopScheduleModel.getClose_end());
                params.put("close_action", shopScheduleModel.getClose_action().toString());
                service = new MyShopInfoActService();
                ((MyShopInfoActService) service).getApi().updateShopClose(AuthUtil.generateParamsNetwork(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case CREATE_SHOP_WITHOUT_IMAGE:
                sendRunningStatus(type);
                running = new Bundle();
                running.putInt(TYPE, type);

                HashMap<String, String> openShopValidationParamWithoutImg = Parcels.unwrap(intent.getParcelableExtra(OPEN_SHOP_VALIDATION_PARAM));

                datasPassingAround = new HashMap<>();
                datasPassingAround.put(GENERATED_RETROFIR_INSTANCE_API, RetrofitUtils.createRetrofit().create(GeneratedHostActApi.class));
                datasPassingAround.put(OPEN_SHOP_VALIDATION_PARAM, openShopValidationParamWithoutImg);
                datasPassingAround.put(SERVICE_TYPE, type);
                createShopWithoutImage(datasPassingAround, type);
                break;
        }
    }

    public void postUpdateShopImage(Map<String, Object> updateShopImageParams, final int type) {
        Observable.just(updateShopImageParams)
                .flatMap(generateHost())
                .flatMap(uploadShopLogo())
                .flatMap(updateShopImage())
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe(new rx.Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, Object> stringObjectMap) {
                        UpdateShopImageModel updateShopImageModel = (UpdateShopImageModel) stringObjectMap.get(UPDATE_SHOP_IMAGE_MODEL);
                        Bundle result = new Bundle();
                        result.putInt(TYPE, type);
                        result.putParcelable(UPLOAD_SHOP_LOGO_DATA, Parcels.wrap(updateShopImageModel));
                        result.putString(PIC_SRC, (String) stringObjectMap.get(PIC_SRC));
                        receiver.send(STATUS_FINISHED, result);
                    }
                });
    }

    private void createShop(final Map<String, Object> shopCreateParams, final int type) {
        Observable.just(shopCreateParams)
                .flatMap(generateHost())
                .flatMap(uploadShopLogo())
                .flatMap(createShopValidation())
                .flatMap(openShopPicture())
                .flatMap(openShopSubmit())
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe(new rx.Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Bundle resultData = new Bundle();
                            resultData.putInt(TYPE, type);
                            resultData.putString(MESSAGE_ERROR_FLAG, e.getLocalizedMessage());
                            resultData.putParcelable(RETRY_DATA, Parcels.wrap(shopCreateParams.get(OPEN_SHOP_VALIDATION_PARAM)));
                            resultData.putString(PIC_SRC, (String) shopCreateParams.get(INPUT_IMAGE));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    }

                    @Override
                    public void onNext(Map<String, Object> shopCreateParams) {
                        OpenShopSubmitData openShopSubmitData = (OpenShopSubmitData) shopCreateParams.get(OPEN_SHOP_SUBMIT_DATA);
                        Bundle result = new Bundle();
                        result.putInt(TYPE, type);
                        result.putParcelable(OPEN_SHOP_SUBMIT_DATA, Parcels.wrap(openShopSubmitData));
                        receiver.send(STATUS_FINISHED, result);

                    }
                });
    }

    private void createShopWithoutImage(final Map<String, Object> shopCreateParams, final int type) {
        Observable.just(shopCreateParams)
                .flatMap(createShopValidation())
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.immediate())
                .subscribe(new rx.Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Bundle resultData = new Bundle();
                            resultData.putInt(TYPE, type);
                            resultData.putString(MESSAGE_ERROR_FLAG, e.getLocalizedMessage());
                            resultData.putParcelable(RETRY_DATA, Parcels.wrap(shopCreateParams.get(OPEN_SHOP_VALIDATION_PARAM)));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    }

                    @Override
                    public void onNext(Map<String, Object> shopCreateParams) {
                        OpenShopValidationData openShopValidationData = (OpenShopValidationData) shopCreateParams.get(OPEN_SHOP_VALIDATION_DATA);
                        Bundle result = new Bundle();
                        result.putInt(TYPE, type);
                        result.putParcelable(OPEN_SHOP_SUBMIT_DATA, Parcels.wrap(openShopValidationData));
                        receiver.send(STATUS_FINISHED, result);

                    }
                });
    }

    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        int type;
        int position;
        ErrorListener listener;

        public Subscriber(int type, int position) {
            this.position = position;
            this.type = type;
            listener = new ErrorListener(type);
        }

        public Subscriber(int type) {
            this.type = type;
            listener = new ErrorListener(type);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            listener.noConnection();
        }

        @Override
        public void onNext(Response<TkpdResponse> responseData) {
            if (responseData.isSuccessful()) {
                TkpdResponse response = responseData.body();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getStringData());
                } catch (JSONException je) {
                    Log.e(TAG, messageTAG + je.getLocalizedMessage());
                }
                if (!response.isError()) {
                    switch (type) {
                        case GET_SHOP_DATA:
                            Data data = gson.fromJson(jsonObject.toString(), Data.class);
                            Bundle result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putParcelable(MODEL_GET_SHOP_DATA, Parcels.wrap(data));
                            result.putString(JSON_SHOP_DATA_CACHE, jsonObject.toString());
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case POST_EDIT_DATA:
                            removeCache();
                            ResponseEdit.DataBean dataBean = gson.fromJson(jsonObject.toString(), ResponseEdit.DataBean.class);
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putParcelable(MODEL_RESPONSE_EDIT_SHOP, Parcels.wrap(dataBean));
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case UPDATE_SHOP_SCHEDULE:
                            removeCache();
                            ResponseEdit.DataBean dataBean1 = gson.fromJson(jsonObject.toString(), ResponseEdit.DataBean.class);
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putParcelable(MODEL_RESPONSE_UPDATE_SHOP_CLOSE, Parcels.wrap(dataBean1));
                            receiver.send(STATUS_FINISHED, result);
                            break;

                    }// end of switch-case
                } else {
                    onMessageError(response.getErrorMessages());
                }
            } else {
                new ErrorHandler(listener, responseData.code());
            }
        }

        /**
         * No connection still not known
         */
        public class ErrorListener implements com.tokopedia.core.network.retrofit.response.ErrorListener {
            int errorCode;
            String error;

            public ErrorListener(int errorCode) {
                this.errorCode = errorCode;
                switch (errorCode) {
                    case ResponseStatus.SC_REQUEST_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_GATEWAY_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                        error = "SERVER ERROR";
                        break;
                    case ResponseStatus.SC_FORBIDDEN:
                        error = "FORBIDDEN ACCESS";
                        break;
                    case ResponseStatus.SC_BAD_GATEWAY:
                        error = "INVALID INPUT";
                        break;
                    case ResponseStatus.SC_BAD_REQUEST:
                        error = "INVALID INPUT";
                        break;
                }
            }

            public void onResponse() {
                Bundle resultData = new Bundle();
                switch (type) {
                    case UPDATE_SHOP_SCHEDULE:
                    case POST_EDIT_DATA:
                    case GET_SHOP_DATA:
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
                        break;
                }
            }

            public void noConnection() {
                error = ShopEditService.noNetworkConnection;
                onResponse();
            }

            @Override
            public void onUnknown() {
                onResponse();
            }

            @Override
            public void onTimeout() {
                onResponse();
            }

            @Override
            public void onServerError() {
                onResponse();
            }

            @Override
            public void onBadRequest() {
                onResponse();
            }

            @Override
            public void onForbidden() {
                onResponse();
            }
        }

        public void onMessageError(List<String> MessageError) {
            if (MessageError == null || !(MessageError.size() > 0))
                return;

            Bundle resultData = new Bundle();
            switch (type) {
                case UPDATE_SHOP_SCHEDULE:
                case POST_EDIT_DATA:
                case GET_SHOP_DATA:
                    resultData.putInt(TYPE, type);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
                    break;
            }
        }
    }

    private void removeCache() {
        ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, getApplicationContext());
    }

    private void sendRunningStatus(int type) {
        /* Update UI: Product CourierService is running */
        Bundle running = new Bundle();
        running.putInt(TYPE, type);
        receiver.send(STATUS_RUNNING, running);
    }


    Func1<Map<String, Object>, Observable<Map<String, Object>>> generateHost() {
        return new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
            @Override
            public Observable<Map<String, Object>> call(Map<String, Object> shopParams) {

                TKPDMapParam<String, String> params = new TKPDMapParam<>();
                params.put(SERVER_LANGUAGE, GOLANG_VALUE);
                Observable<GenerateHostModel> result = new GenerateHostActService().getApi().generateHost2(AuthUtil.generateParamsNetwork(getApplicationContext(), params));

                return Observable.zip(Observable.just(shopParams)
                        , result,
                        new Func2<Map<String, Object>, GenerateHostModel, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> shopCreateParams, GenerateHostModel generateHostModel) {
                                if (generateHostModel.getData().getGenerateHost() == null) {
                                    if (generateHostModel.getMessageError().get(0) != null) {
                                        throw new UnsupportedOperationException(generateHostModel.getMessageError().get(0));
                                    } else {
                                        throw new UnsupportedOperationException(UNKNOWN_ERROR);
                                    }
                                }
                                shopCreateParams.put(GENERATE_HOST_MODEL, generateHostModel);
                                return shopCreateParams;
                            }
                        });
            }
        };
    }

    Func1<Map<String, Object>, Observable<Map<String, Object>>> uploadShopLogo() {
        return new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
            @Override
            public Observable<Map<String, Object>> call(Map<String, Object> shopParams) {

                GenerateHostModel generateHostModel = (GenerateHostModel) shopParams.get(GENERATE_HOST_MODEL);
                GenerateHostModel.GenerateHost generateHost = generateHostModel.getData().getGenerateHost();

                NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, getApplicationContext(),
                        HTTPS + generateHost.getUploadHost() + UPLOAD_SHOP_IMAGE)
                        .setIdentity()
                        .addParam(UploadPhotoShopTask.NEW_ADD, UploadPhotoShopTask.GOLANG)
                        .addParam(UploadPhotoShopTask.RESOLUTION, UploadPhotoShopTask.RESOLUTION_DEFAULT_VALUE)
                        .addParam(UploadPhotoShopTask.SERVER_ID, generateHost.getServerId())
                        .compileAllParam()
                        .finish();

                File file = new File((String) shopParams.get(INPUT_IMAGE));

                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
                RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
                RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoShopTask.NEW_ADD));
                RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoShopTask.RESOLUTION));
                RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(UploadPhotoShopTask.SERVER_ID));

                Observable<UploadShopLogoData> result = RetrofitUtils.createRetrofit(networkCalculator.getUrl())
                        .create(UploadShopLogo.class)
                        .uploadShopLogoV4(
                                networkCalculator.getUrl(),
                                networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                userId,
                                deviceId,
                                hash,
                                deviceTime,
                                fileToUpload,
                                newAdd,
                                resolution,
                                serverId
                        );
                return Observable.zip(Observable.just(shopParams),
                        result,
                        new Func2<Map<String, Object>, UploadShopLogoData, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> shopParams, UploadShopLogoData uploadShopLogoData) {
                                if (uploadShopLogoData.getMessageError() != null && uploadShopLogoData.getMessageError().size() != 0) {
                                    throw new UnsupportedOperationException(uploadShopLogoData.getMessageError().get(0));
                                }
                                shopParams.put(UPLOAD_SHOP_LOGO_DATA, uploadShopLogoData);
                                return shopParams;
                            }
                        });
            }
        };
    }

    Func1<Map<String, Object>, Observable<Map<String, Object>>> updateShopImage() {
        return new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
            @Override
            public Observable<Map<String, Object>> call(Map<String, Object> shopCreateParams) {
                final UploadShopLogoData uploadShopLogoData = (UploadShopLogoData) shopCreateParams.get(UPLOAD_SHOP_LOGO_DATA);
                TKPDMapParam<String, String> params = new TKPDMapParam<>();
                params.put("pic_code", uploadShopLogoData.getData().getImage().getPicCode());
                params.put("pic_src", uploadShopLogoData.getData().getImage().getPicSrc());
                Observable<UpdateShopImageModel> uploadProductImageDataObservable =
                        new MyShopInfoActService().getApi().updatePictureNew(AuthUtil.generateParamsNetwork(getApplicationContext(), params));

                return Observable.zip(Observable.just(shopCreateParams), uploadProductImageDataObservable,
                        new Func2<Map<String, Object>, UpdateShopImageModel, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> shopCreateParams, UpdateShopImageModel updateShopImageModel) {
                                shopCreateParams.put(PIC_SRC, uploadShopLogoData.getData().getImage().getPicSrc());
                                shopCreateParams.put(UPDATE_SHOP_IMAGE_MODEL, updateShopImageModel);
                                return shopCreateParams;
                            }
                        });
            }
        };
    }

    Func1<Map<String, Object>, Observable<Map<String, Object>>> createShopValidation() {
        return new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
            @Override
            public Observable<Map<String, Object>> call(Map<String, Object> shopParams) {

                UploadShopLogoData uploadShopLogoData = (UploadShopLogoData) (shopParams.get(UPLOAD_SHOP_LOGO_DATA));
                HashMap<String, String> openShopValidationParam = (HashMap<String, String>) (shopParams.get(OPEN_SHOP_VALIDATION_PARAM));
                
                //[START] change generate param to new param
                Iterator<String> iterator = openShopValidationParam.keySet().iterator();
                TKPDMapParam<String, String> openShopValidationParam2 = new TKPDMapParam<>();
                for(;iterator.hasNext();) {
                    String next = iterator.next();
                    String s = openShopValidationParam.get(next);
                    openShopValidationParam2.put(next,s);
                }

                if (uploadShopLogoData != null) {
                    // Add Shop Logo to the Parameter
                    openShopValidationParam.put(SHOP_LOGO, uploadShopLogoData.getData().getUpload().getSrc());
                    openShopValidationParam2.put(SHOP_LOGO, uploadShopLogoData.getData().getUpload().getSrc());
                }
                //[END] change generate param to new param

                service = new MyShopActService();
                Observable<OpenShopValidationData> result = ((MyShopActService) service).getApi().openShopValidationNew(
                        AuthUtil.generateParamsNetwork(getApplicationContext(), openShopValidationParam2));

                return Observable.zip(Observable.just(shopParams), result,
                        new Func2<Map<String, Object>, OpenShopValidationData, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> shopParams, OpenShopValidationData openShopValidationData) {
                                int type = (Integer) shopParams.get(SERVICE_TYPE);
                                switch (type) {
                                    case CREATE_SHOP:
                                        if (openShopValidationData.getData().getPostKey() == null) {
                                            if (openShopValidationData.getMessageError() != null && openShopValidationData.getMessageError().size() != 0) {
                                                throw new UnsupportedOperationException(openShopValidationData.getMessageError().get(0));
                                            } else {
                                                throw new UnsupportedOperationException(UNKNOWN_ERROR);
                                            }
                                        }
                                        break;
                                    case CREATE_SHOP_WITHOUT_IMAGE:
                                        if (openShopValidationData.getData().getIsSuccess() == null) {
                                            if (openShopValidationData.getMessageError() != null && openShopValidationData.getMessageError().size() != 0) {
                                                throw new UnsupportedOperationException(openShopValidationData.getMessageError().get(0));
                                            } else {
                                                throw new UnsupportedOperationException(UNKNOWN_ERROR);
                                            }
                                        }
                                        break;
                                }

                                shopParams.put(OPEN_SHOP_VALIDATION_DATA, openShopValidationData);
                                return shopParams;
                            }
                        });
            }
        };
    }

    Func1<Map<String, Object>, Observable<Map<String, Object>>> openShopPicture() {
        return new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
            @Override
            public Observable<Map<String, Object>> call(Map<String, Object> shopParams) {

                UploadShopLogoData uploadShopLogoData = (UploadShopLogoData) shopParams.get(UPLOAD_SHOP_LOGO_DATA);
                String shopLogo = uploadShopLogoData.getData().getUpload().getSrc();
                GenerateHostModel generateHostModel = (GenerateHostModel) shopParams.get(GENERATE_HOST_MODEL);
                GenerateHostModel.GenerateHost generateHost = generateHostModel.getData().getGenerateHost();

                TKPDMapParam<String, String> params = new TKPDMapParam<>();
                params.put("shop_logo", shopLogo);
                params.put("server_id", generateHost.getServerId());
                Observable<OpenShopPictureModel> result = new MyShopActAfterService(HTTPS + generateHost.getUploadHost() + "/web-service/v4/action/upload-image-helper/").getApi().openShopPicture(AuthUtil.generateParamsNetwork(getApplicationContext(), params));

                return Observable.zip(Observable.just(shopParams), result,
                        new Func2<Map<String, Object>, OpenShopPictureModel, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> shopParams, OpenShopPictureModel openShopPictureModel) {
                                if (openShopPictureModel.getData().getFile_uploaded() == null) {
                                    if (openShopPictureModel.getMessage_error() != null) {
                                        throw new UnsupportedOperationException(openShopPictureModel.getMessage_error()[0]);
                                    } else {
                                        throw new UnsupportedOperationException(UNKNOWN_ERROR);
                                    }
                                }
                                shopParams.put(OPEN_SHOP_PICTURE_DATA, openShopPictureModel);
                                return shopParams;
                            }
                        });
            }
        };
    }

    static TKPDMapParam<String, String> openShopSubmitCreateParam(String fileUploaded, String postKey) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("file_uploaded", fileUploaded);
        param.put("post_key", postKey);
        return param;
    }


    Func1<Map<String, Object>, Observable<Map<String, Object>>> openShopSubmit() {
        return new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
            @Override
            public Observable<Map<String, Object>> call(Map<String, Object> shopParams) {

                OpenShopPictureModel openShopPictureModel = (OpenShopPictureModel) shopParams.get(OPEN_SHOP_PICTURE_DATA);
                OpenShopValidationData openShopValidationData = (OpenShopValidationData) shopParams.get(OPEN_SHOP_VALIDATION_DATA);

                String fileUploaded = "";
                if (openShopPictureModel != null) {
                    fileUploaded = openShopPictureModel.getData().getFile_uploaded();
                }

                String postKey = openShopValidationData.getData().getPostKey();

//                throw new UnsupportedOperationException("done, dont continue for testing");

                service = new MyShopActService();
                Observable<OpenShopSubmitData> result = ((MyShopActService) service).getApi().openShopSubmitNew(
                        AuthUtil.generateParamsNetwork(getApplicationContext(), openShopSubmitCreateParam(fileUploaded, postKey)));

                return Observable.zip(Observable.just(shopParams), result,
                        new Func2<Map<String, Object>, OpenShopSubmitData, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> shopParams, OpenShopSubmitData openShopSubmitData) {
                                if (openShopSubmitData.getData().getIsSuccess() == null) {
                                    if (openShopSubmitData.getMessageError() != null && openShopSubmitData.getMessageError().size() != 0) {
                                        throw new UnsupportedOperationException(OPEN_SHOP_SUBMIT + openShopSubmitData.getMessageError().get(0));
                                    } else {
                                        throw new UnsupportedOperationException(OPEN_SHOP_SUBMIT + UNKNOWN_ERROR);
                                    }
                                }
                                shopParams.put(OPEN_SHOP_SUBMIT_DATA, openShopSubmitData);
                                return shopParams;
                            }
                        });
            }
        };
    }

}