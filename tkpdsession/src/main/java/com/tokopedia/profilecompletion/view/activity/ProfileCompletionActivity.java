package com.tokopedia.profilecompletion.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFinishedFragment;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;
import com.tokopedia.profilecompletion.view.fragment.ProfilePhoneVerifCompletionFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.fragment.RegisterEmailFragment;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfileCompletionActivity extends BasePresenterActivity {

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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MethodChecker.getColor(this, R.color.white)));
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.grey_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        ProfileCompletionFragment fragment = ProfileCompletionFragment.createInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
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

    public void onFinishedForm() {
        ProfileCompletionFinishedFragment fragment = ProfileCompletionFinishedFragment.createInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }
}
