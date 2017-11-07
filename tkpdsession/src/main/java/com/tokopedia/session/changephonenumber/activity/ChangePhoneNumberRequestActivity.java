package com.tokopedia.session.changephonenumber.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.fragment.ChangePhoneNumberRequestFragment;
import com.tokopedia.session.changephonenumber.fragment.ChangePhoneNumberWaitFragment;

/**
 * Created by nisie on 3/2/17.
 */

public class ChangePhoneNumberRequestActivity extends BasePresenterActivity implements ChangePhoneNumberRequestFragment.ChangePhoneNumberRequestListener {

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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        ChangePhoneNumberRequestFragment fragment = ChangePhoneNumberRequestFragment.createInstance(this);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void goToThanksPage() {
        ChangePhoneNumberWaitFragment fragment = ChangePhoneNumberWaitFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
