package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.List;

/**
 * Created by alvarisi on 4/25/17.
 */

public interface ApplyPromoContract {
    interface View extends CustomerView {
        String getPromo();

        RequestParams getApplyPromoParams();

        void hideApplyPromoLoading();

        void onSuccessApplyPromo(FareEstimate applyPromo);

        void onFailedApplyPromo(ApplyPromo applyPromo);

        void showApplyPromoLoading();

        void onFailedApplyPromo(String message);

        void showApplyPromoLayout();

        void hideApplyPromoLayout();

        RequestParams getParams();

        void setEmptyPromoError();

        void clearEmptyPromoError();

        void showPromoLoading();

        RequestParams getPromoParams();

        void hidePromoLoading();

        void renderPromoList(List<Promo> promos);

        void renderEmptyOnGoingPromo();

        void enableApplyButton();

        void disableApplyButton();
    }

    interface Presenter extends CustomerPresenter<View> {
        void actionApplyPromo();

        void getOnGoingPromo();
    }
}
