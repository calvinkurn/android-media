package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetProductAndEstimatedUseCase;
import com.tokopedia.ride.bookingride.domain.GetPromoUseCase;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper.RideProductViewModelMapper;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.exception.InterruptConfirmationHttpException;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 3/14/17.
 */

public class UberProductPresenter extends BaseDaggerPresenter<UberProductContract.View> implements UberProductContract.Presenter {
    private final String VALID_CURRENCY = "IDR";

    private RideProductViewModelMapper mProductViewModelMapper;
    private GetProductAndEstimatedUseCase getProductAndEstimatedUseCase;
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private GetPromoUseCase getPromoUseCase;

    public UberProductPresenter(GetProductAndEstimatedUseCase getUberProductsUseCase,
                                GetFareEstimateUseCase getFareEstimateUseCase,
                                GetPromoUseCase getPromoUseCase) {
        getProductAndEstimatedUseCase = getUberProductsUseCase;
        mProductViewModelMapper = new RideProductViewModelMapper();
        this.getFareEstimateUseCase = getFareEstimateUseCase;
        this.getPromoUseCase = getPromoUseCase;
    }

    @Override
    public void initialize() {
        actionGetPromo();
        getView().showProgress();
    }

    private void actionGetPromo() {
        getPromoUseCase.execute(getView().getPromoParams(), new Subscriber<List<Promo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached())
                    getView().hideAdsBadges();
            }

            @Override
            public void onNext(List<Promo> promo) {
                if (isViewAttached()) {
                    if (promo.size() > 0) {
                        getView().showAdsBadges(promo.get(0).getCode().toUpperCase() + ": " + promo.get(0).getOffer());
                    } else {
                        getView().hideAdsBadges();
                    }
                }
            }
        });
    }

    @Override
    public void actionGetRideProducts(final PlacePassViewModel source, final PlacePassViewModel destination) {
        getView().showProgress();
        getView().hideProductList();
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
                    getView().showErrorMessage(e.getMessage(), getView().getActivity().getString(R.string.btn_text_retry));
                }

            }

            @Override
            public void onNext(List<ProductEstimate> productEstimates) {
                if (isViewAttached() && !isUnsubscribed()) {
                    List<Visitable> productsList = mProductViewModelMapper.transform(productEstimates);
                    if (productsList.size() == 0) {
                        getView().hideProgress();
                        getView().hideProductList();
                        getView().showErrorMessage(getView().getActivity().getString(R.string.no_rides_found), getView().getActivity().getString(R.string.btn_text_retry));
                    } else {
                        //check if currency code of any product is not IDR, show error message
//                        for (ProductEstimate pe : productEstimates) {
//                            if (pe.getProduct().getPriceDetail() != null && !pe.getProduct().getPriceDetail().getCurrencyCode().equalsIgnoreCase(VALID_CURRENCY)) {
//                                getView().showErrorMessage(getView().getActivity().getString(R.string.no_uber_valid_location), getView().getActivity().getString(R.string.btn_text_retry));
//                                return;
//                            }
//                        }
                        if (destination != null) {
                            actionGetFareProduct2(source, destination, productEstimates);
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

    private void actionGetFareProduct(PlacePassViewModel source, PlacePassViewModel destination, List<ProductEstimate> productEstimates) {
        for (int i = 0; i < productEstimates.size(); i++) {
            actionFareProductEstimate(
                    productEstimates.get(i),
                    source,
                    destination,
                    productEstimates.get(i).getProduct().getProductId(),
                    i
            );
        }


    }

    private void actionGetFareProduct2(PlacePassViewModel source, PlacePassViewModel destination, List<ProductEstimate> productEstimates) {
        List<Observable<RideProductViewModel>> observables = new ArrayList<>();
        for (int i = 0; i < productEstimates.size(); i++) {

            RequestParams requestParams = RequestParams.create();
            requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(source.getLatitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(source.getLongitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(destination.getLatitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(destination.getLongitude()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(productEstimates.get(i).getProduct().getProductId()));
            requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(1));


            observables.add(Observable.zip(Observable.just(productEstimates.get(i)),
                    getFareEstimateUseCase.createObservable(requestParams),
                    new Func2<ProductEstimate, FareEstimate, RideProductViewModel>() {
                        @Override
                        public RideProductViewModel call(ProductEstimate productEstimate, FareEstimate fareEstimate) {
                            return mProductViewModelMapper.transformRide(productEstimate, fareEstimate);
                        }
                    })
                    .onErrorReturn(new Func1<Throwable, RideProductViewModel>() {
                        @Override
                        public RideProductViewModel call(Throwable throwable) {
                            return null;
                        }
                    }));
        }

        Observable.merge(observables)
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
                            getView().showErrorMessage(e.getMessage(), getView().getActivity().getString(R.string.btn_text_retry));
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
                });

    }


    @Override
    public void actionFareProductEstimate(final ProductEstimate productEstimate, PlacePassViewModel source, PlacePassViewModel destination, final String productId, final int position) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(source.getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(destination.getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(destination.getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(productId));
        requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(1));
        getFareEstimateUseCase.execute(requestParams, new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof InterruptConfirmationHttpException) {
                    //show Interrupt Confirmation Dialog
                } else {
                    //show error message
                    getView().hideProductList();
                    getView().showErrorMessage(e.getMessage(), getView().getActivity().getString(R.string.btn_text_retry));
                }
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (isViewAttached() && !isUnsubscribed()) {
                    getView().hideErrorMessage();
                    getView().showProductList();
                    getView().renderFareProduct(mProductViewModelMapper.transform(productEstimate, fareEstimate), productId, position, fareEstimate);
                }
            }
        });
    }

    @Override
    public void detachView() {
        getFareEstimateUseCase.unsubscribe();
        getProductAndEstimatedUseCase.unsubscribe();
        getPromoUseCase.unsubscribe();
        super.detachView();
    }
}
