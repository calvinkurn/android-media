package com.tokopedia.seller.shopsettings.shipping.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.apiservices.shipment.EditShippingService;
import com.tokopedia.core.network.apiservices.shipment.EditShippingWebViewService;
import com.tokopedia.core.network.apiservices.shop.MyShopService;
import com.tokopedia.core.network.apiservices.shop.MyShopShipmentActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.EditShippingCouriers;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Kris on 2/22/2016.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 * TOKOPEDIA
 */
public class EditShippingInteractorImpl implements EditShippingRetrofitInteractor {
    private final EditShippingService editShippingService;
    private final MyShopShipmentActService myShopShipmentActService;
    private final EditShippingWebViewService editShippingWebViewService;
    private final MyShopService myShopService;
    CompositeSubscription compositeSubscription;

    public EditShippingInteractorImpl() {
        this.editShippingService = new EditShippingService();
        this.myShopShipmentActService = new MyShopShipmentActService();
        this.editShippingWebViewService = new EditShippingWebViewService();
        this.compositeSubscription = new CompositeSubscription();
        this.myShopService = new MyShopService();
    }


    @Override
    public void getCourierList(@NonNull Context context,
                               @NonNull Map<String, String> params,
                               @NonNull final CourierListListener listener) {

        Observable<Response<TkpdResponse>> observable = editShippingService
                .getApi()
                .getShippingList(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onNoConnection();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body().convertDataObj(EditShippingCouriers.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onNoConnection();
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
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
    public void initiateCourierList(@NonNull Context context,
                                    @NonNull final CourierListListener listener) {
        Observable<Response<TkpdResponse>> observable = editShippingService
                .getApi()
                .getShippingList(AuthUtil.generateParams(context, new HashMap<String, String>()));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onTimeout("Mohon ulangi beberapa saat lagi");
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body().convertDataObj(EditShippingCouriers.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onNoConnection();
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onTimeout("Mohon ulangi beberapa saat lagi");
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
    public void updateCourierChanges(@NonNull Context context,
                                     @NonNull Map<String, String> params,
                                     @NonNull final ShippingUpdateListener listener) {
        Observable<Response<TkpdResponse>> observable = myShopShipmentActService
                .getApi()
                .updateShipmentInfo(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onTimeout();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError())
                        listener.onSuccess(response.body().getStatusMessages().get(0));
                    else
                        listener.onFailed(response.body().getErrorMessages().get(0));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onNoConnection();
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onForbidden() {
                            listener.onTimeout();
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
    public void getShippingDetailWebView(@NonNull Context context,
                                         @NonNull String webViewURL,
                                         @NonNull Map<String, String> params,
                                         @NonNull final getShippingDetailListener listener) {

        URI webViewUri = URI.create(webViewURL);

        try {
            params.putAll(splitQuery(webViewUri));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Observable<String> observable = editShippingWebViewService
                .getApi()
                .getShippingWebViewDetail(MapNulRemover.removeNull(params));

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onTimeout();
            }

            @Override
            public void onNext(String webViewResources) {
                if (!webViewResources.isEmpty()) {
                    listener.onSuccess(webViewResources);
                } else {
                    listener.onTimeout();
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getOpenShopData(@NonNull Context context,
                                @NonNull Map<String, String> params,
                                @NonNull final getOpenShopDataListener listener) {
        Observable<Response<TkpdResponse>> observable = myShopService
                .getApi()
                .getOpenShopForm(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onTimeout();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body().convertDataObj(OpenShopData.class));
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onNoConnection();
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onForbidden() {
                            listener.onTimeout();
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

    public Observable<Response<TkpdResponse>> getOpenShopData(Map<String, String> params){
        return myShopService.getApi()
                .getOpenShopForm(params);
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }

    private Map<String, String> splitQuery(URI uri) throws UnsupportedEncodingException {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = uri.getQuery();
        String[] pairs = query != null ? query.split("&") : new String[0];
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return queryPairs;
    }

}
