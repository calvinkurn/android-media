package com.tokopedia.ride.bookingride.view;

import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.List;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface UberProductContract {

    interface View extends CustomerView {
        void showMessage(String message);

        void renderProductList(List<Visitable> datas);

        List<Visitable> getProductList();

        void showAdsBadges(String message);

        void hideAdsBadges();

        void showProgress();

        void hideProgress();

        void hideProductList();

        void showProductList();

        void showErrorMessage(String message, String btnText);

        void hideErrorMessage();

        void hideErrorMessageLayout();

        void renderFareProduct(Visitable productEstimate, String productId, int position, FareEstimate fareEstimate);

        void actionMinimumTimeEstResult(String timeEst);

        Context getActivity();

        void showFareListHeader();

        void hideFareListHeader();

        void displayNearbyCabs(List<ProductEstimate> productEstimates);

        PlacePassViewModel getSource();

        void renderNearbyRides(NearbyRides nearbyRides);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void actionGetRideProducts(PlacePassViewModel source);

        void actionGetRideProducts(PlacePassViewModel source, PlacePassViewModel destination);
    }
}
