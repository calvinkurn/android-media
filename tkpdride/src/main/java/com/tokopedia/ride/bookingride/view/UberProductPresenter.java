package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.mapper.RideProductViewModelMapper;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/14/17.
 */

public class UberProductPresenter extends BaseDaggerPresenter<UberProductContract.View> implements UberProductContract.Presenter {
    GetUberProductsUseCase mGetUberProductsUseCase;
    RideProductViewModelMapper mProductViewModelMapper;

    public UberProductPresenter(GetUberProductsUseCase getUberProductsUseCase) {
        mGetUberProductsUseCase = getUberProductsUseCase;
        mProductViewModelMapper = new RideProductViewModelMapper();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void actionGetRideProducts(PlacePassViewModel source, PlacePassViewModel destination) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetUberProductsUseCase.PARAM_LATITUDE, String.valueOf(source.getLatitude()));
        requestParams.putString(GetUberProductsUseCase.PARAM_LONGITUDE, String.valueOf(source.getLongitude()));
        mGetUberProductsUseCase.execute(requestParams, new Subscriber<List<Product>>() {
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

    }
}
