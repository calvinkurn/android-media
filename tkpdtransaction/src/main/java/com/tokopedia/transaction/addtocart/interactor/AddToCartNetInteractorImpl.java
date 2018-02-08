package com.tokopedia.transaction.addtocart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartService;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.transaction.addtocart.model.responseaddress.AddressData;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Form;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Angga.Prasetiyo on 11/03/2016.
 */
public class AddToCartNetInteractorImpl implements AddToCartNetInteractor {
    private static final String TAG = AddToCartNetInteractorImpl.class.getSimpleName();
    private final TXCartService txCartService;
    private final TXCartActService txCartActService;
    private final CompositeSubscription compositeSubscription;
    private final PeopleActService peopleActService;

    public AddToCartNetInteractorImpl() {
        this.txCartService = new TXCartService();
        this.txCartActService = new TXCartActService();
        this.compositeSubscription = new CompositeSubscription();
        this.peopleActService = new PeopleActService();
    }

    @Override
    public void getAddToCartForm(@NonNull Context context, @NonNull Map<String, String> param,
                                 @NonNull final OnGetCartFormListener listener) {
        Observable<Response<TkpdResponse>> observable = txCartService.getApi()
                .getAddToCartForm(AuthUtil.generateParams(context, param));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError() && !response.body().isNullData()) {
                        listener.onSuccess(response.body().convertDataObj(AtcFormData.class));
                        return;
                    }
                }
                listener.onFailure();
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getAddressShipping(@NonNull Context context, @NonNull Map<String, String> param,
                                   @NonNull final OnGetCartAddressListener listener) {
        Observable<Response<TkpdResponse>> observable = txCartService.getApi()
                .cartSearchAddress(AuthUtil.generateParams(context, param));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(AddressData.class));
                } else {
                    listener.onFailure();
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateCartPrice(@NonNull Context context, @NonNull Map<String, String> param,
                                   @NonNull final OnCalculateProduct listener) {

        Observable<Response<TkpdResponse>> observable = txCartService.getApi()
                .calculateCart(AuthUtil.generateParams(context, param));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                try {
                    if (response.isSuccessful() && !response.body().isError()
                            && !response.body().isNullData()) {
                        listener.onSuccess(response.body()
                                .getJsonData().getJSONObject("product").getString("price"));
                    } else if (response.body().isError()) {
                        listener.onShowErrorMessage(response.body().getErrorMessages().get(0));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateCartShipping(@NonNull final Context context,
                                      @NonNull final Map<String, String> param,
                                      @NonNull final OnCalculateShipping listener) {

        Observable<Response<TkpdResponse>> observable = txCartService.getApi()
                .calculateCart(AuthUtil.generateParams(context, param));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                calculateCartShipping(context, param, listener);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(Form.class).getShipment());
                } else {
                    calculateCartShipping(context, param, listener);
                }

            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateCartAddressShipping(@NonNull final Context context,
                                             @NonNull final Map<String, String> param,
                                             @NonNull final OnCalculateAddressShipping listener) {

        Observable<Response<TkpdResponse>> observable = txCartService.getApi()
                .calculateCart(AuthUtil.generateParams(context, param));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                calculateCartAddressShipping(context, param, listener);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(Form.class).getShipment());
                } else {
                    calculateCartAddressShipping(context, param, listener);
                }

            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void updateAddress(@NonNull final Context context,
                              @NonNull Map<String, String> params,
                              @NonNull final OnUpdateAddressListener listener) {

        final Observable<Response<TkpdResponse>> observable = peopleActService.getApi()
                .editAddress(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) {
                        if (!tkpdResponse.getErrorMessages().isEmpty()) {
                            listener.onFailure(tkpdResponse.getErrorMessages().get(0));
                        } else {
                            listener.onFailure(context.getString(R.string.default_request_error_unknown));
                        }
                        return;
                    }
                    if (tkpdResponse.isNullData()) {
                        listener.onFailure(context.getString(R.string.default_request_error_null_data));
                        return;
                    }
                    if (!response.body().getJsonData().isNull("is_success")) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt("is_success");
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    listener.onSuccess();
                                    break;
                                default:
                                    listener.onFailure(message);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onFailure(context
                                    .getString(R.string.default_request_error_unknown));
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onFailure(context.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onFailure("Timeout connection," +
                                    " Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onFailure("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onFailure("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onFailure("Terjadi Kesalahan, " +
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
    public void addToCart(@NonNull final Context context, final Scheduler schedulerSubscribe,
                          @NonNull final Map<String, String> params,
                          @NonNull final OnAddToCart listener) {
        Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .addToCart(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onTimeout();
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (!response.body().isError()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isNullData()) {
                        try {
                            if (tkpdResponse.getJsonData().getInt("is_success") == 1) {
                                StringBuilder sb = new StringBuilder();
                                for (String s : tkpdResponse.getStatusMessages()) {
                                    if (tkpdResponse.getStatusMessages().size() > 1) {
                                        sb.append(s).append("\n");
                                    } else {
                                        sb.append(s);
                                    }

                                }
                                listener.onSuccess(sb.toString());
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (String s : tkpdResponse.getErrorMessages()) {
                                    if (tkpdResponse.getErrorMessages().size() > 1) {
                                        sb.append(s).append("\n");
                                    } else {
                                        sb.append(s);
                                    }
                                }
                                listener.onFailure(sb.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            StringBuilder sb = new StringBuilder();
                            for (String s : tkpdResponse.getErrorMessages()) {
                                if (tkpdResponse.getErrorMessages().size() > 1) {
                                    sb.append(s).append("\n");
                                } else {
                                    sb.append(s);
                                }
                            }
                            listener.onFailure(sb.toString());
                        }
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String s : response.body().getErrorMessages()) {
                        if (response.body().getErrorMessages().size() > 1) {
                            sb.append(s).append("\n");
                        } else {
                            sb.append(s);
                        }
                    }
                    listener.onFailure(sb.toString());
                }
            }
        };

        compositeSubscription.add(observable
                .subscribeOn((schedulerSubscribe != null)
                        ? schedulerSubscribe : Schedulers.newThread())
                .unsubscribeOn((schedulerSubscribe != null)
                        ? schedulerSubscribe : Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribeObservable() {
        compositeSubscription.unsubscribe();
    }


}
