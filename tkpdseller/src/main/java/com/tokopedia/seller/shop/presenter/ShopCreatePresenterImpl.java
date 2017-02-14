package com.tokopedia.seller.shop.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.apiservices.shop.MyShopActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shipping.model.openshopshipping.OpenShopData;
import com.tokopedia.seller.shop.ShopEditService;
import com.tokopedia.seller.shop.ShopEditorActivity;
import com.tokopedia.seller.shop.constant.ShopEditServiceConstant;
import com.tokopedia.core.shop.model.ShopCreateParams;
import com.tokopedia.core.shop.model.checkDomainShopName.CheckDomainShopName;
import com.tokopedia.core.shop.model.openShopSubmitData.OpenShopSubmitData;
import com.tokopedia.core.shop.model.openShopValidationData.OpenShopValidationData;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.util.SessionHandler;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.seller.shop.ShopEditService.RETRY_DATA;
import static com.tokopedia.seller.shop.constant.ShopEditServiceConstant.OPEN_SHOP_VALIDATION_PARAM;
import static com.tokopedia.seller.shop.constant.ShopEditServiceConstant.PIC_SRC;

/**
 * Created by Sebast on 5/19/2016.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ShopCreatePresenterImpl extends ShopCreatePresenter implements DownloadResultReceiver.Receiver {

    /**
     * ENVIROMENT SETUP VARIABLE
     */
    private MyShopActService myShopActService;
    private LocalCacheHandler cache;
    DownloadResultReceiver mReceiver;
    private Subscription domainSubscription;
    private Subscription nameSubscription;
    private QueryListener domainListener;
    private QueryListener nameListener;

    /**
     * DATA USED INSIDE PRESENTER
     */
    private ShopCreateParams shopCreateParams;


    public ShopCreatePresenterImpl(ShopCreateView view) {
        super(view);
        myShopActService = new MyShopActService();
    }

    @Override
    public String getMessageTAG() {
        return getMessageTAG(ShopCreatePresenter.class);
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return className.getSimpleName();
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        Log.d(STUART, SHOP_CREATE_FRAGMENT + " getRotationData(Bundle argument) ");
        shopCreateParams = Parcels.unwrap(argument.getParcelable(DATA_VIEW));
    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate) {
            shopCreateParams = new ShopCreateParams();
            ScreenTracking.screen(AppScreen.SCREEN_CREATE_SHOP);
        }
    }

    @Override
    public void initData(@NonNull Context context) {

        refreshView();

        if (!SessionHandler.isMsisdnVerified()) {
            view.showPhoneVerification(true);
        }

        initSubscription(context);

    }

    private interface QueryListener {
        void getQueryString(String string);
    }


    private void refreshView() {
        if (!shopCreateParams.getShopImgAvatarSrc().equals("")) {
            view.setShopAvatar(shopCreateParams.getShopImgAvatarSrc());
        }

        if (!shopCreateParams.getShopDomain().equals("")) {
            view.setShopDomain(shopCreateParams.getShopDomain());
        }

        if (!shopCreateParams.getShopName().equals("")) {
            view.setShopName(shopCreateParams.getShopName());
        }

        if (!shopCreateParams.getShopShortDesc().equals("")) {
            view.setShopDesc(shopCreateParams.getShopShortDesc());
        }

        if (!shopCreateParams.getShopTagLine().equals("")) {
            view.setShopTag(shopCreateParams.getShopTagLine());
        }
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        saveDescTag();
        Log.d(STUART, SHOP_CREATE_FRAGMENT + " saveDataBeforeRotation(Bundle argument) ");
        argument.putParcelable(DATA_VIEW, Parcels.wrap(shopCreateParams));
    }

    private void initSubscription(Context context) {
        compositeSubscription.add(domainSubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                domainListener = new QueryListener() {
                    @Override
                    public void getQueryString(String string) {
                        subscriber.onNext(string);
                    }
                };
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Subscribe Error");

                    }

                    @Override
                    public void onNext(String string) {
                        if (!string.equals("")) {
                            checkShopDomainWS(string);
                        }
                    }
                }));

        compositeSubscription.add(nameSubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                nameListener = new QueryListener() {
                    @Override
                    public void getQueryString(String string) {
                        subscriber.onNext(string);
                    }
                };
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String string) {
                        if (!string.equals("")) {
                            checkShopNameWS(string);
                        }
                    }
                }));

    }

    @Override
    public void
    checkShopDomain(final String domain) {
        if (domainListener != null) {
            domainListener.getQueryString(domain);
        }
    }

    private void checkShopDomainWS(final String domain) {
        shopCreateParams.setShopDomain(domain);
        compositeSubscription.add(myShopActService.getApi().checkDomain(
                AuthUtil.generateParams(view.getMainContext(), convertDomainToMap(domain)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setShopDomainResult(CommonUtils.generateMessageError(view.getMainContext(), e.getMessage()), false);
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponseResponse) {

                        Pair<Boolean, String> resultDomain = checkResponseDomain(tkpdResponseResponse);
                        view.showProgress(false);
                        if(resultDomain.first)
                            shopCreateParams.setShopDomain(domain);

                        view.setShopDomainResult(resultDomain.second, resultDomain.first);

//                        Gson gson = new GsonBuilder().create();
//                        if(tkpdResponseResponse.body() == null || tkpdResponseResponse.body().getStrResponse() == null){
//                            view.setShopDomainResult(CommonUtils.generateMessageError(view.getMainContext(), null), false);
//                            return;
//                        }
//
//                        CheckDomainShopName checkDomainShopName = gson.fromJson(tkpdResponseResponse.body().getStrResponse(), CheckDomainShopName.class);
//                        if(checkDomainShopName.getData().getStatusDomain() == 1){
//                            view.setShopDomainResult("", true);
//                            shopCreateParams.setShopDomain(domain);
//                        } else {
//                            if(tkpdResponseResponse.body().getErrorMessages() != null && tkpdResponseResponse.body().getErrorMessages().size() > 0) {
//                                view.setShopDomainResult(tkpdResponseResponse.body().getErrorMessages().get(0), false);
//                                Log.e("ShopCreatePresenterImpl", tkpdResponseResponse.body().getErrorMessages().get(0));
//                            }else{
//                                view.setShopDomainResult(CommonUtils.generateMessageError(view.getMainContext(), null), false);
//                            }
//                        }
                    }
                }));
    }

    @NonNull
    private Map<String, String> convertDomainToMap(String domain) {
        Map<String, String> params = new HashMap<>();
        params.put("shop_domain", domain);
        return params;
    }

    @Override
    public void checkShopName(final String s) {
        if (nameListener != null) {
            nameListener.getQueryString(s);
        }
    }

    private void checkShopNameWS(String s) {
        shopCreateParams.setShopName(s);
        compositeSubscription.add(myShopActService.getApi().checkShopName(
                AuthUtil.generateParams(view.getMainContext(), convertNameToMap(s)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setShopNameResult(CommonUtils.generateMessageError(view.getMainContext(), e.getMessage()), false);
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                        Pair<Boolean, String> resultName = checkResponseName(tkpdResponseResponse);
                        view.showProgress(false);
                        view.setShopNameResult(resultName.second, resultName.first);

//                        if(tkpdResponseResponse.body() == null || tkpdResponseResponse.body().getStrResponse() == null){
//                            view.setShopNameResult(CommonUtils.generateMessageError(view.getMainContext(), null), false);
//                            return;
//                        }
//                        Gson gson = new GsonBuilder().create();
//                        CheckDomainShopName checkDomainShopName = gson.fromJson(tkpdResponseResponse.body().getStrResponse(), CheckDomainShopName.class);
//                        if(checkDomainShopName.getData().getStatusShopName() == 1){
//                            view.setShopNameResult("", true);
//                        } else {
//                            if(tkpdResponseResponse.body().getErrorMessages() != null && tkpdResponseResponse.body().getErrorMessages().size() > 0) {
//                                view.setShopNameResult(tkpdResponseResponse.body().getErrorMessages().get(0), false);
//                            }else{
//                                view.setShopNameResult(CommonUtils.generateMessageError(view.getMainContext(), null), false);
//                            }
//                        }
                    }
                }));
    }

    @NonNull
    private Map<String, String> convertNameToMap(String s) {
        Map<String, String> params = new HashMap<>();
        params.put("shop_name", s);
        return params;
    }

    @Override
    public void sendShopRequest(Context context) {
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPEN_SHOP_VALIDATION_PARAM, Parcels.wrap(shopCreateParams.toHashMap()));
        if (shopCreateParams.getShopImgAvatarSrc().equals("")) {
            ShopEditService.startShopService(context, mReceiver, bundle, ShopEditService.CREATE_SHOP_WITHOUT_IMAGE);
        } else {
            bundle.putString(ShopEditService.INPUT_IMAGE, shopCreateParams.getShopImgAvatarSrc());
            ShopEditService.startShopService(context, mReceiver, bundle, ShopEditService.CREATE_SHOP);
        }
    }

    @Override
    public void saveShippingData(OpenShopData shippingData) {
        shopCreateParams.setOpenShopData(shippingData);
    }

    @Override
    public void saveShopAvatarUrl(String imagePath) {
        shopCreateParams.setShopImgAvatarSrc(imagePath);
    }

    @Override
    public boolean verifyData() {
        // check data
        if (view.checkDomainError()) {
            view.onMessageError(DOMAIN_ERROR, "");
            return false;
        }

        if (view.checkNameError()) {
            view.onMessageError(NAME_ERROR, "");
            return false;
        }

        if (view.checkTagError()) {
            view.onMessageError(TAG_ERROR, "");
            return false;
        }

        if (view.checkDescError()) {
            view.onMessageError(DESC_ERROR, "");
            return false;
        }

        return true;

    }

    @Override
    public void saveDescTag() {
        shopCreateParams.setShopTagLine(view.getShopTag());
        shopCreateParams.setShopShortDesc(view.getShopDesc());

    }

    @Override
    public void finalCheckDomainName(final String domain, String name) {
        compositeSubscription.add(Observable.concat(
                myShopActService.getApi().checkDomain(
                        AuthUtil.generateParams(view.getMainContext(), convertDomainToMap(domain))),
                myShopActService.getApi().checkShopName(
                        AuthUtil.generateParams(view.getMainContext(), convertNameToMap(name)))
        )
                .toList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Response<TkpdResponse>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Response<TkpdResponse>> responses) {
                        view.showProgress(false);
                        Log.i(TAG, "check domain if ok");
                        Pair<Boolean, String> resultDomain = checkResponseDomain(responses.get(0));
                        if(!resultDomain.first) {
                            view.setShopDomainResult(resultDomain.second, resultDomain.first);
                            return;
                        }

                        Pair<Boolean, String> resultName = checkResponseName(responses.get(1));
                        if(!resultName.first) {
                            view.setShopNameResult(resultName.second, resultName.first);
                            return;
                        }
                        if (verifyData()) {
                            view.startOpenShopEditShippingActivity();
                        }
                    }
                }));
    }

    private Pair<Boolean, String> checkResponseName(Response<TkpdResponse> tkpdResponseResponse) {
        if(tkpdResponseResponse.body() == null || tkpdResponseResponse.body().getStrResponse() == null){
            return new Pair<>(false, CommonUtils.generateMessageError(view.getMainContext(), null));
        }
        Gson gson = new GsonBuilder().create();
        CheckDomainShopName checkDomainShopName = gson.fromJson(tkpdResponseResponse.body().getStrResponse(), CheckDomainShopName.class);
        if(checkDomainShopName.getData().getStatusShopName() == 1){
            return new Pair<>(true, "");
        } else {
            if(tkpdResponseResponse.body().getErrorMessages() != null && tkpdResponseResponse.body().getErrorMessages().size() > 0) {
                return new Pair<>(false, tkpdResponseResponse.body().getErrorMessages().get(0));
            }else{
                return new Pair<>(false, CommonUtils.generateMessageError(view.getMainContext(), null));
            }
        }
    }

    @Nullable
    private Pair<Boolean, String> checkResponseDomain(Response<TkpdResponse> responses) {
        if(responses.body() == null || responses.body().getStrResponse() == null){
            return new Pair<>(false, CommonUtils.generateMessageError(view.getMainContext(), null));
        }

        Gson gson = new GsonBuilder().create();
        CheckDomainShopName checkDomainShopName = gson.fromJson(responses.body().getStrResponse(), CheckDomainShopName.class);
        if(checkDomainShopName.getData().getStatusDomain() == 1) {
            return new Pair<>(true, "");
        } else {
            if(responses.body().getErrorMessages() != null && responses.body().getErrorMessages().size() > 0) {
                return new Pair<>(false, responses.body().getErrorMessages().get(0));
            }else{
                return new Pair<>(false, CommonUtils.generateMessageError(view.getMainContext(), null));
            }
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(ShopEditService.TYPE, ShopEditService.INVALID_TYPE);
        switch (resultCode) {
            case ShopEditServiceConstant.STATUS_RUNNING:
                switch (type) {
                    case ShopEditServiceConstant.CREATE_SHOP:
                        view.showProgress(true);
                        break;
                    case ShopEditServiceConstant.CREATE_SHOP_WITHOUT_IMAGE:
                        view.showProgress(true);
                }
                break;
            case ShopEditService.STATUS_FINISHED:
                UnifyTracking.eventCreateShopSuccess();
                switch (type) {
                    case ShopEditService.CREATE_SHOP:
                        view.showProgress(false);
                        OpenShopSubmitData openShopSubmitData = Parcels.unwrap(resultData.getParcelable(ShopEditServiceConstant.OPEN_SHOP_SUBMIT_DATA));
                        finishOpenShopService(openShopSubmitData);
                        break;
                    case ShopEditServiceConstant.CREATE_SHOP_WITHOUT_IMAGE:
                        view.showProgress(false);
                        OpenShopValidationData openShopValidationData = Parcels.unwrap(resultData.getParcelable(ShopEditServiceConstant.OPEN_SHOP_SUBMIT_DATA));
                        finishOpenShopServiceWithoutImage(openShopValidationData);
                }
                break;
            case ShopEditService.STATUS_ERROR:
                String errorMessage = resultData.getString(ShopEditServiceConstant.MESSAGE_ERROR_FLAG);
                switch (type) {
                    case ShopEditService.CREATE_SHOP:
                        shopCreateParams.setShopImgAvatarSrc(resultData.getString(PIC_SRC));
                        shopCreateParams.saveFromMap((Map<String, String>) Parcels.unwrap(resultData.getParcelable(RETRY_DATA)));
                        refreshView();
                        view.showProgress(false);
                        view.onMessageError(0, errorMessage);
                        view.goToEditShipping(shopCreateParams.getOpenShopData());
                        break;
                    case ShopEditService.CREATE_SHOP_WITHOUT_IMAGE:
                        shopCreateParams.saveFromMap((Map<String, String>) Parcels.unwrap(resultData.getParcelable(RETRY_DATA)));
                        refreshView();
                        view.showProgress(false);
                        view.onMessageError(0, errorMessage);
                        view.goToEditShipping(shopCreateParams.getOpenShopData());
                        break;
                }
        }// end of status download service
    }

    private void finishOpenShopServiceWithoutImage(OpenShopValidationData openShopValidationData) {
        if (openShopValidationData.getData().getIsSuccess() != null) {
            if (openShopValidationData.getData().getIsSuccess() == 1) {
                Bundle bundle = ShopInfoActivity.createBundle(
                        // shop id
                        openShopValidationData.getData().getShopId().toString(),
                        // shop url
                        openShopValidationData.getData().getShopUrl());
                saveCache(openShopValidationData.getData().getShopId().toString());
                ShopEditorActivity.finishActivity(bundle, (Activity) view.getMainContext());

            } else {
                String unknownError = "Kesalahan Tidak Diketahui";
                view.onMessageError(0, unknownError);
            }

        } else {
            String unknownError = "Kesalahan Tidak Diketahui";
            view.onMessageError(0, unknownError);
        }
    }

    private void finishOpenShopService(OpenShopSubmitData openShopSubmitData) {
        if (openShopSubmitData.getData().getIsSuccess() != null) {
            if (openShopSubmitData.getData().getIsSuccess() == 1) {
                Bundle bundle = ShopInfoActivity.createBundle(
                        // shop id
                        openShopSubmitData.getData().getShopId().toString(),
                        // shop url
                        openShopSubmitData.getData().getShopUrl());
                saveCache(openShopSubmitData.getData().getShopId().toString());
                ShopEditorActivity.finishActivity(bundle, (Activity) view.getMainContext());

            } else {
                String unknownError = "Kesalahan Tidak Diketahui";
                view.onMessageError(0, unknownError);
            }

        } else {
            String unknownError = "Kesalahan Tidak Diketahui";
            view.onMessageError(0, unknownError);
        }
    }

    private void saveCache(String shopID) {
        SessionHandler session = new SessionHandler(view.getMainContext());
        session.setLoginSession(session.getLoginID(), session.getLoginName(), shopID, SessionHandler.isMsisdnVerified());
        LocalCacheHandler.clearCache(view.getMainContext(), "USER_INFO");
    }

}
