package com.tokopedia.session.register.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.shop.notes.fragment.ManageShopNotesFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.register.fragment.RegisterStep1Fragment;
import com.tokopedia.session.register.fragment.RegisterStep2Fragment;
import com.tokopedia.session.register.model.RegisterStep1ViewModel;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterActivity extends BasePresenterActivity {

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

        Bundle bundle;
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
        } else {
            bundle = new Bundle();
        }

        RegisterStep1Fragment fragment = RegisterStep1Fragment.createInstance(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
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
    public String getScreenName() {
        return AppScreen.SCREEN_REGISTER;
    }

    public void goToStep2(RegisterStep1ViewModel model) {
        RegisterStep2Fragment fragment = RegisterStep2Fragment.createInstance(model);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }
}
