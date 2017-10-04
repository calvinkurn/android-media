package com.tokopedia.tkpdreactnative.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.di.DaggerReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * @author ricoharisin .
 */
public class ReactNetworkModule extends ReactContextBaseJavaModule {


    @Inject
    ReactNetworkRepository reactNetworkRepository;
    ReactNativeNetworkComponent daggerRnNetworkComponent;

    public ReactNetworkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        if (reactContext.getApplicationContext() instanceof MainApplication) {
            AppComponent appComponent = ((MainApplication) reactContext.getApplicationContext()).getApplicationComponent();
            daggerRnNetworkComponent = DaggerReactNativeNetworkComponent.builder()
                    .appComponent(appComponent).build();
            daggerRnNetworkComponent.inject(this);
        } else {
            throw new RuntimeException("Current context unsupported");
        }
    }

    @Override
    public String getName() {
        return "NetworkModule";
    }

    @ReactMethod
    public void getResponse(String url, String method, String request, Boolean isAuth, final Promise promise) {
        try {
            Subscription subscribe = reactNetworkRepository
                    .getResponse(url, method, convertStringRequestToHashMap(request), isAuth)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            promise.reject(e);
                        }

                        @Override
                        public void onNext(String s) {
                            if (getCurrentActivity() != null) {
                                promise.resolve(s);
                            } else {
                                promise.resolve("");
                            }
                        }
                    });
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getResponseJson(String url, String method, String request, Boolean isAuth, final Promise promise) {
        try {
            CommonUtils.dumper(url + " " + request);
            Subscription subscribe = reactNetworkRepository
                    .getResponse(url, method, request, isAuth)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            promise.reject(e);
                        }

                        @Override
                        public void onNext(String s) {
                            if (getCurrentActivity() != null) {
                                promise.resolve(s);
                            } else {
                                promise.resolve("");
                            }
                        }
                    });
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e){
            promise.reject(e);
        }
    }

    private static TKPDMapParam<String, String> convertStringRequestToHashMap(String request) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        try {
            JSONObject jsonObject = new JSONObject(request);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = jsonObject.get(key);
                    params.put(key, value.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

}
