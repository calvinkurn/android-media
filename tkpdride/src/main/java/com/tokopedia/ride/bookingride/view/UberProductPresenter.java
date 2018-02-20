package com.tokopedia.ride.bookingride.view;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetNearbyCarsUseCase;
import com.tokopedia.ride.bookingride.domain.GetPriceEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetProductAndEstimatedUseCase;
import com.tokopedia.ride.bookingride.domain.GetTimePriceEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper.RideProductViewModelMapper;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.PriceEstimate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 3/14/17.
 */

public class UberProductPresenter extends BaseDaggerPresenter<UberProductContract.View> implements UberProductContract.Presenter {

    private RideProductViewModelMapper productViewModelMapper;
    private GetProductAndEstimatedUseCase getProductAndEstimatedUseCase;
    private GetPriceEstimateUseCase getPriceEstimateUseCase;
    private GetNearbyCarsUseCase getNearbyCarsUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public UberProductPresenter(GetProductAndEstimatedUseCase getProductAndEstimatedUseCase,
                                GetPriceEstimateUseCase getPriceEstimateUseCase,
                                GetNearbyCarsUseCase getNearbyCarsUseCase) {
        this.getProductAndEstimatedUseCase = getProductAndEstimatedUseCase;
        this.getPriceEstimateUseCase = getPriceEstimateUseCase;
        this.getNearbyCarsUseCase = getNearbyCarsUseCase;
        this.productViewModelMapper = new RideProductViewModelMapper();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize() {
        getView().showProgress();
    }

    @Override
    public void actionGetRideProducts(PlacePassViewModel source) {
        actionSetProductListWhenItsAvailable(null);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
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
                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    } else if (e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getActivity().getResources().getString(R.string.error_internet_not_connected) :
                                e.getMessage();
                    } else {
                        message = getView().getActivity().getResources().getString(R.string.error_please_try_again_later);
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
                        getView().hideProgress();
                        getView().hideErrorMessage();
                        getView().showProductList();
                        getView().renderProductList(productsList);
                        getView().displayNearbyCabs(productEstimates);
                        getMinimalProductEstimateAndRender(productEstimates);
                        //renderNearbyCarsAroundSource(productEstimates);
                    }
                }
            }
        });
    }

    @Override
    public void actionGetRideProducts(final PlacePassViewModel source, final PlacePassViewModel destination) {
        actionSetProductListWhenItsAvailable(destination);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
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
                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    } else if (e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getActivity().getResources().getString(R.string.error_internet_not_connected) :
                                e.getMessage();
                    } else {
                        message = getView().getActivity().getResources().getString(R.string.error_please_try_again_later);
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
                            actionGetPricesEstimate(source, destination, productEstimates);
                        } else {
                            getView().hideProgress();
                            getView().hideErrorMessage();
                            getView().showProductList();
                            getView().renderProductList(productsList);
                            getView().showFareListHeader();
                            //renderNearbyCarsAroundSource(productEstimates);
                        }
                        getView().displayNearbyCabs(productEstimates);
                        getMinimalProductEstimateAndRender(productEstimates);
                    }
                }
            }
        });
    }

    private void actionSetProductListWhenItsAvailable(PlacePassViewModel destination) {
        if (!isViewAttached()) {
            return;
        }


        List<Visitable> existingProductList = getView().getProductList();
        if (existingProductList != null && existingProductList.size() > 0) {
            List<Visitable> updatedList = new ArrayList<>();
            for (Visitable visitable : existingProductList) {
                if (visitable instanceof RideProductViewModel) {
                    RideProductViewModel rideProductViewModel = RideProductViewModel.copy((RideProductViewModel) visitable);
                    rideProductViewModel.setTimeEstimate(null);
                    rideProductViewModel.setProductPriceFmt(null);
                    if (destination != null) {
                        rideProductViewModel.setBaseFare("");
                        rideProductViewModel.setSurgePrice(false);
                        rideProductViewModel.setDestinationSelected(true);
                        getView().showFareListHeader();
                    } else {
                        rideProductViewModel.setDestinationSelected(false);
                        getView().hideFareListHeader();
                    }

                    rideProductViewModel.setEnabled(false);
                    updatedList.add(rideProductViewModel);
                }
            }

            getView().renderProductList(updatedList);
        }
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

    private void actionGetPricesEstimate(PlacePassViewModel source, PlacePassViewModel destination, List<ProductEstimate> productEstimates) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetTimePriceEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetTimePriceEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(source.getLongitude()));
        requestParams.putString(GetTimePriceEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(destination.getLatitude()));
        requestParams.putString(GetTimePriceEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(destination.getLongitude()));
        compositeSubscription.add(
                Observable.zip(
                        Observable.just(productEstimates),
                        getPriceEstimateUseCase.createObservable(requestParams),
                        new Func2<List<ProductEstimate>, List<PriceEstimate>, List<RideProductViewModel>>() {
                            @Override
                            public List<RideProductViewModel> call(List<ProductEstimate> productEstimateList, List<PriceEstimate> priceEstimates) {
                                return productViewModelMapper.transformRide(productEstimateList, priceEstimates);
                            }
                        })
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
                                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                                    } else if (e instanceof SocketTimeoutException) {
                                        message = getView().getActivity().getResources().getString(R.string.error_internet_not_connected);
                                    } else if (e instanceof UnProcessableHttpException) {
                                        message = TextUtils.isEmpty(e.getMessage()) ?
                                                getView().getActivity().getResources().getString(R.string.error_internet_not_connected) :
                                                e.getMessage();
                                    } else {
                                        message = getView().getActivity().getResources().getString(R.string.error_please_try_again_later);
                                    }
                                    getView().showErrorMessage(message, getView().getActivity().getString(R.string.btn_text_retry));
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
                                        getView().showFareListHeader();
                                    } else {
                                        getView().hideProductList();
                                        getView().showErrorMessage(getView().getActivity().getString(R.string.no_rides_found),
                                                getView().getActivity().getString(R.string.btn_text_retry)
                                        );
                                    }
                                }
                            }
                        }));
    }


    private void renderNearbyCarsAroundSource(List<ProductEstimate> productEstimates) {
        CommonUtils.dumper("nearby rides renderNearbyCarsAroundSource");

        PlacePassViewModel source = getView().getSource();
        if (source == null || productEstimates == null || !isViewAttached()) {
            return;
        }


        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetNearbyCarsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetNearbyCarsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
        requestParams.putString(GetNearbyCarsUseCase.PARAM_CAR_ETA, "4");
        requestParams.putString(GetNearbyCarsUseCase.PARAM_BIKE_ETA, "4");
        getNearbyCarsUseCase.execute(requestParams, new Subscriber<NearbyRides>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                CommonUtils.dumper("nearby rides error");
            }

            @Override
            public void onNext(NearbyRides nearbyRides) {
                if (nearbyRides == null) {
                    CommonUtils.dumper("nearby rides are null");
                } else {
                    CommonUtils.dumper("nearby rides");
                    getView().renderNearbyRides(nearbyRides);
                }
            }
        });

    }

    @Override
    public void detachView() {
        getPriceEstimateUseCase.unsubscribe();
        getProductAndEstimatedUseCase.unsubscribe();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        super.detachView();
    }
}
