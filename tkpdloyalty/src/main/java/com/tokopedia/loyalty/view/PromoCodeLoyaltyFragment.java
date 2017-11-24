package com.tokopedia.loyalty.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.loyalty.di.DaggerHachikoComponent;

/**
 * @author anggaprasetiyo on 24/11/17.
 */

public class PromoCodeLoyaltyFragment extends BasePresenterFragment {
    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerHachikoComponent.builder().appComponent((AppComponent) getComponent(AppComponent.class))
                .build().inject(this);
    }
}
