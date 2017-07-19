package com.tokopedia.ride.bookingride.view;

import android.text.TextUtils;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.model.InterruptConfirmationHttpException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetProductAndEstimatedUseCase;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper.RideProductViewModelMapper;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 3/14/17.
 */

public class UberProductPresenter extends BaseDaggerPresenter<UberProductContract.View> implements UberProductContract.Presenter {
    private final String VALID_CURRENCY = "IDR";

    private RideProductViewModelMapper productViewModelMapper;
    private GetProductAndEstimatedUseCase getProductAndEstimatedUseCase;
    private GetFareEstimateUseCase getFareEstimateUseCase;

    private CompositeSubscription compositeSubscription;

    public UberProductPresenter(GetProductAndEstimatedUseCase getUberProductsUseCase,
                                GetFareEstimateUseCase getFareEstimateUseCase) {
        this.getProductAndEstimatedUseCase = getUberProductsUseCase;
        this.productViewModelMapper = new RideProductViewModelMapper();
        this.getFareEstimateUseCase = getFareEstimateUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize() {
        getView().showProgress();
    }

    @Override
    public void actionGetRideProducts(final PlacePassViewModel source, final PlacePassViewModel destination) {

        //format existing list to not show time and price when updating
        actionSetProductListWhenItsAvailable(destination);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
        getActionGetProducts(source, destination, requestParams, null, null);
    }

    private void actionSetProductListWhenItsAvailable(PlacePassViewModel destination) {
        List<Visitable> existingProductList = getView().getProductList();
        if (existingProductList != null && existingProductList.size() > 0) {
            List<Visitable> updatedList = new ArrayList<>();
            for (Visitable visitable : existingProductList) {
                if (visitable instanceof RideProductViewModel) {
                    RideProductViewModel rideProductViewModel = RideProductViewModel.copy((RideProductViewModel) visitable);
                    rideProductViewModel.setTimeEstimate(null);
                    if (destination != null) {
                        rideProductViewModel.setProductPriceFmt(null);
                        rideProductViewModel.setProductPrice(-1);
                        rideProductViewModel.setBaseFare("");
                        rideProductViewModel.setSurgePrice(false);
                    }

                    rideProductViewModel.setEnabled(false);
                    updatedList.add(rideProductViewModel);
                }
            }

            getView().renderProductList(updatedList);
        }
    }

    private void getActionGetProducts(final PlacePassViewModel source, final PlacePassViewModel destination, RequestParams requestParams, final String key, final String value) {
        getProductAndEstimatedUseCase.execute(requestParams, new Subscriber<List<ProductEstimate>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideProductList();
                    getView().hideProgress();

                    String message = e.getMessage();
                    if (e instanceof UnknownHostException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    }

                    getView().showErrorMessage(message, getView().getActivity().getString(R.string.btn_text_retry));
                }

            }

            @Override
            public void onNext(List<ProductEstimate> productEstimates) {
                if (isViewAttached() && !isUnsubscribed()) {
                    List<Visitable> productsList = productViewModelMapper.transform(productEstimates);
                    if (productsList.size() == 0) {
                        getView().hideProgress();
                        getView().hideProductList();
                        getView().showErrorMessage(getView().getActivity().getString(R.string.no_rides_found), getView().getActivity().getString(R.string.btn_text_retry));
                    } else {
                        if (destination != null) {
                            actionGetFareProduct(source, destination, productEstimates, key, value);
                        } else {
                            getView().hideProgress();
                            getView().hideErrorMessage();
                            getView().showProductList();
                            getView().renderProductList(productsList);
                        }

                        getMinimalProductEstimateAndRender(productEstimates);
                    }
                }
            }
        });
    }

    private void getMinimalProductEstimateAndRender(List<ProductEstimate> productEstimates) {
        int minTime = 0;
        for (ProductEstimate estimate : productEstimates) {
            if (estimate.getTimesEstimate() == null) {
                continue;
            }

            if (minTime == 0 && estimate.getTimesEstimate().getEstimate() > 0)
                minTime = estimate.getTimesEstimate().getEstimate();
            else if (minTime > estimate.getTimesEstimate().getEstimate()) {
                minTime = estimate.getTimesEstimate().getEstimate();
            }
        }


        getView().actionMinimumTimeEstResult(minTime / 60 + "\nmin");
    }

    private void actionGetFareProduct(PlacePassViewModel source, PlacePassViewModel destination, List<ProductEstimate> productEstimates, String key, String value) {
        List<Observable<RideProductViewModel>> observables = new ArrayList<>();
        for (int i = 0; i < productEstimates.size(); i++) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(source.getLatitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(source.getLongitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(destination.getLatitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(destination.getLongitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(1));
            requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(productEstimates.get(i).getProduct().getProductId()));
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                requestParams.putString(key, value);
            }
            observables.add(Observable.zip(Observable.just(productEstimates.get(i)),
                    getFareEstimateUseCase.createObservable(requestParams),
                    new Func2<ProductEstimate, FareEstimate, RideProductViewModel>() {
                        @Override
                        public RideProductViewModel call(ProductEstimate productEstimate, FareEstimate fareEstimate) {
                            return productViewModelMapper.transformRide(productEstimate, fareEstimate);
                        }
                    }));
        }

        compositeSubscription.add(Observable.merge(observables)
                .filter(new Func1<RideProductViewModel, Boolean>() {
                    @Override
                    public Boolean call(RideProductViewModel rideProductViewModel) {
                        return rideProductViewModel != null;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RideProductViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().hideProgress();
                            getView().hideProductList();

                            String message = e.getMessage();
                            if (e instanceof InterruptConfirmationHttpException) {
                                getView().openInterruptConfirmationWebView(((InterruptConfirmationHttpException) e).getTosUrl());
                                if (((InterruptConfirmationHttpException) e).getType().equalsIgnoreCase(InterruptConfirmationHttpException.TOS_CONFIRMATION_INTERRUPT)) {
                                    getView().showErrorTosConfirmation(((InterruptConfirmationHttpException) e).getTosUrl());
                                } else {
                                    getView().showErrorMessage(message, getView().getActivity().getString(R.string.btn_text_retry));
                                }
                            } else {
                                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                                    message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                                } else if (e instanceof SocketTimeoutException) {
                                    message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                                }

                                getView().showErrorMessage(message, getView().getActivity().getString(R.string.btn_text_retry));
                            }
                        }
                    }

                    @Override
                    public void onNext(List<RideProductViewModel> rideProductViewModels) {
                        if (isViewAttached()) {
                            getView().hideProgress();
                            getView().hideErrorMessage();
                            if (rideProductViewModels.size() > 0) {
                                getView().showProductList();
                                List<Visitable> visitables = new ArrayList<>();
                                visitables.addAll(rideProductViewModels);
                                getView().renderProductList(visitables);
                            } else {
                                getView().hideProductList();
                                getView().showErrorMessage(getView().getActivity().getString(R.string.no_rides_found),
                                        getView().getActivity().getString(R.string.btn_text_retry)
                                );
                            }
                        }

                    }
                })
        );
    }

    @Override
    public void actionGetRideProducts(String value, String key, PlacePassViewModel source, PlacePassViewModel destination) {
        actionSetProductListWhenItsAvailable(destination);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
        getActionGetProducts(source, destination, requestParams, key, value);
    }

    @Override
    public void detachView() {
        getFareEstimateUseCase.unsubscribe();
        getProductAndEstimatedUseCase.unsubscribe();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        super.detachView();
    }
}
