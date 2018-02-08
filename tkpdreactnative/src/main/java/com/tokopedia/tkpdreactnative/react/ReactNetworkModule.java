package com.tokopedia.tkpdreactnative.react;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.di.DaggerReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeNetworkComponent;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ricoharisin .
 */
public class ReactNetworkModule extends ReactContextBaseJavaModule {


    @Inject
    ReactNetworkRepository reactNetworkRepository;
    @Inject
    UnifyReactNetworkRepository unifyReactNetworkRepository;

    ReactNativeNetworkComponent daggerRnNetworkComponent;

    private CompositeSubscription compositeSubscription;

    public ReactNetworkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        if (reactContext.getApplicationContext() instanceof MainApplication) {
            AppComponent appComponent = ((MainApplication) reactContext.getApplicationContext()).getApplicationComponent();
            daggerRnNetworkComponent = DaggerReactNativeNetworkComponent.builder()
                    .appComponent(appComponent).build();
            daggerRnNetworkComponent.inject(this);
            compositeSubscription = new CompositeSubscription();
        } else {
            throw new RuntimeException("Current context unsupported");
        }
    }

    @Override
    public String getName() {
        return "NetworkModule";
    }

    @Deprecated
    @ReactMethod
    public void getResponse(String url, String method, String request, Boolean isAuth, final Promise promise) {
        try {
            compositeSubscription.add(reactNetworkRepository.getResponse(url, method, convertStringRequestToHashMap(request), isAuth)
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
                    }));
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
            compositeSubscription.add(reactNetworkRepository.getResponseJson(url, method, request, isAuth)
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
                    }));
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e) {
            promise.reject(e);
        }
    }


    /**
     * call api with <b>encoded</b> parameter query
     * @param url
     * @param method POST or GET
     * @param encodedRequest the request data must be encoded
     * @param isAuth
     * @param promise
     */
    @ReactMethod
    public void getResponseParam(String url, String method, String encodedRequest, Boolean isAuth, final Promise promise) {
        try {
            CommonUtils.dumper(url + " " + encodedRequest);
            compositeSubscription.add(reactNetworkRepository
                    .getResponseParam(url, method, encodedRequest, isAuth)
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
                    })
            );
        } catch (UnknownMethodException e) {
            promise.reject(e);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void request(ReadableMap readableMap, final Promise promise) {
        HashMap<String, Object> maps = readableMap.toHashMap();
        ReactNetworkingConfiguration.Builder builder = new ReactNetworkingConfiguration.Builder();
        for (Map.Entry<String, Object> map : maps.entrySet()) {
            switch (map.getKey()) {
                case ReactConst.Networking.URL:
                    builder.setUrl(String.valueOf(map.getValue()));
                    break;
                case ReactConst.Networking.METHOD:
                    builder.setMethod(String.valueOf(map.getValue()));
                    break;
                case ReactConst.Networking.ENCODING:
                    builder.setEncoding(String.valueOf(map.getValue()));
                    break;
                case ReactConst.Networking.AUTHORIZATIONMODE:
                    builder.setAuthorizationMode(String.valueOf(map.getValue()));
                    break;
                case ReactConst.Networking.HEADERS:
                    if (map.getValue() instanceof HashMap) {
                        builder.setHeaders((HashMap<String, Object>) map.getValue());
                    }
                    break;
                case ReactConst.Networking.PARAMS:
                    if (map.getValue() instanceof HashMap) {
                        builder.setParams((HashMap<String, Object>) map.getValue());
                    }
                    break;

            }
        }
        ReactNetworkingConfiguration configuration = builder.build();

        try {
            compositeSubscription.add(unifyReactNetworkRepository.request(configuration)
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
                    })
            );
        } catch (Exception e) {
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
