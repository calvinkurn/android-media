package com.tokopedia.session.register.registerphonenumber.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.changename.view.fragment.ChangeNameFragment;
import com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameFragment;
import com.tokopedia.session.register.registerphonenumber.view.fragment.RegisterPhoneNumberFragment;

/**
 * @author by yfsx on 22/03/18.
 */

public class AddNameActivity extends BasePresenterActivity implements HasComponent {

    public static final String PARAM_PHONE = "param_phone";

    public static Intent newInstance(Context context, String phoneNumber) {
        Intent intent = new Intent(context, AddNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE, phoneNumber);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }

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
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (AddNameFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = AddNameFragment.newInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
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
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
