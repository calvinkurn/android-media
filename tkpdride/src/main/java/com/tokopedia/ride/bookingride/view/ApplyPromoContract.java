package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;

/**
 * Created by alvarisi on 4/25/17.
 */

public interface ApplyPromoContract {
    interface View extends CustomerView{
        String getPromo();

        RequestParams getApplyPromoParams();

        void hideApplyPromoLoading();

        void onSuccessApplyPromo(ApplyPromo applyPromo);

        void onFailedApplyPromo(ApplyPromo applyPromo);

        void showApplyPromoLoading();

        void onFailedApplyPromo(String message);

        void showApplyPromoLayout();

        void hideApplyPromoLayout();
    }

    interface Presenter extends CustomerPresenter<View>{
        void actionApplyPromo();
    }
}
