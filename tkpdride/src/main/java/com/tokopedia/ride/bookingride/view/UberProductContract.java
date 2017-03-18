package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.List;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface UberProductContract {

    interface View extends CustomerView {
        void showMessage(String message);

        void renderProductList(List<Visitable> datas);

        void showAdsBadges(String message);

        void hideAdsBadges();

        void showProgress();

        void hideProgress();

        void showErrorMessage(int messageResourceId);

        void hideErrorMessage(String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void actionGetRideProducts(PlacePassViewModel source, PlacePassViewModel destination);
    }
}
