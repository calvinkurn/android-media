package com.tokopedia.seller.shop.open.view.activity;

import android.os.Bundle;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.tokopedia.seller.R;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.shop.open.view.adapter.ShopOpenStepperViewAdapterOpenShop;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenMandatoryActivity extends TActivity implements StepperLayout.StepperListener {

    StepperLayout stepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_open_mandatory);

        stepperLayout = (StepperLayout) findViewById(R.id.stepper_view);
        stepperLayout.setAdapter(new ShopOpenStepperViewAdapterOpenShop(getFragmentManager(), this));
        stepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}
