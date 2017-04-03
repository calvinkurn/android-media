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
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper.RideProductViewModelMapper;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.exception.TosConfirmationHttpException;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.List;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/14/17.
 */

public class UberProductPresenter extends BaseDaggerPresenter<UberProductContract.View> implements UberProductContract.Presenter {
    private RideProductViewModelMapper mProductViewModelMapper;
    private GetProductAndEstimatedUseCase getProductAndEstimatedUseCase;
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private GetPromoUseCase getPromoUseCase;
    private List<ProductEstimate> mProductEstimates;

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
//        actionGetPromo();
    }

    private void actionGetPromo() {
        getPromoUseCase.execute(RequestParams.EMPTY, new Subscriber<Promo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideAdsBadges();
            }

            @Override
            public void onNext(Promo promo) {
                getView().showAdsBadges("");
            }
        });
    }

    @Override
    public void actionGetRideProducts(final PlacePassViewModel source, final PlacePassViewModel destination) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
        getProductAndEstimatedUseCase.execute(requestParams, new Subscriber<List<ProductEstimate>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorMessage(e.getMessage(), getView().getActivity().getString(R.string.btn_text_retry));
            }

            @Override
            public void onNext(List<ProductEstimate> productEstimates) {
                List<Visitable> productsList = mProductViewModelMapper.transform(productEstimates);

                getView().hideProgress();

                if (productsList.size() == 0) {
                    getView().showErrorMessage(getView().getActivity().getString(R.string.no_rides_found), getView().getActivity().getString(R.string.btn_text_retry));
                } else {
                    getView().hideErrorMessage();
                    getView().showProductList();
                    getView().renderProductList(productsList);
                    if (source != null && destination != null)
                        actionGetFareProduct(source, destination, productEstimates);
                    mProductEstimates = productEstimates;

                    getMinimalProductEstimateAndRender(productEstimates);
                }
            }
        });/*
        getProductAndEstimatedUseCase.execute(requestParams, new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Product> products) {
                List<Visitable> productsList = mProductViewModelMapper.transform(products);

                getView().hideProgress();

                if(productsList.size() == 0){
                    getView().showErrorMessage(R.string.no_rides_found);
                }

                getView().renderProductList(productsList);
            }
        });
*/
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

                if (e instanceof TosConfirmationHttpException) {
                    //show TOS Confirmation Dialog
                } else {
                    //show error message
                    getView().hideProductList();
                    getView().showErrorMessage(e.getMessage(), getView().getActivity().getString(R.string.btn_text_retry));
                }
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                getView().hideErrorMessage();
                getView().showProductList();
                getView().renderFareProduct(mProductViewModelMapper.transform(productEstimate, fareEstimate), productId, position, fareEstimate);
            }
        });
    }
}
