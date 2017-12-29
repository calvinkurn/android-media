package com.tokopedia.tokocash.qrpayment.presentation;

import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tokocash.R;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class SuccessPaymentQRActivity extends BasePresenterActivity {

    InfoSuccessPaymentQRView view;
    private Animation decelerateAnimation;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_success_payment_qr;
    }

    @Override
    protected void initView() {
        view = (InfoSuccessPaymentQRView) findViewById(R.id.info_success_payment);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        decelerateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.decelerate_animation);
    }

    @Override
    protected void setActionVar() {
        view.startAnimation(decelerateAnimation);
    }
}
