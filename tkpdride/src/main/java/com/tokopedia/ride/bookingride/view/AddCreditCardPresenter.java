package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * Created by alvarisi on 4/25/17.
 */

public class AddCreditCardPresenter extends BaseDaggerPresenter<AddCreditCardContract.View>
        implements AddCreditCardContract.Presenter {

    @Inject
    public AddCreditCardPresenter() {
    }
}
