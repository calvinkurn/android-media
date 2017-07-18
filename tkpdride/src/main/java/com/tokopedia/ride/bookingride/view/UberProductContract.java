package com.tokopedia.ride.bookingride.view;

import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
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

        RequestParams getPromoParams();

        void openInterruptConfirmationWebView(String tosUrl);

        void showErrorTosConfirmation(String tosUrl);

        void showErrorTosConfirmationDialog(String message, String url, String code, String value);

        void openInterruptConfirmationDialog(String url, String code, String value);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void actionGetRideProducts(PlacePassViewModel source, PlacePassViewModel destination);

        void actionGetRideProducts(String value, String key, PlacePassViewModel source, PlacePassViewModel destination);
    }
}
