package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * Created by alvarisi on 4/25/17.
 */

public class ManagePaymentOptionsPresenter extends BaseDaggerPresenter<ManagePaymentOptionsContract.View>
        implements ManagePaymentOptionsContract.Presenter {

    @Inject
    public ManagePaymentOptionsPresenter() {
    }
}
