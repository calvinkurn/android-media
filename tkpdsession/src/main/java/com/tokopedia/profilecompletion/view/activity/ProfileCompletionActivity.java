package com.tokopedia.profilecompletion.view.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFinishedFragment;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.session.R;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfileCompletionActivity extends BasePresenterActivity implements HasComponent{

    private static final String ERROR_IMPLEMENT_LISTENER = "Error not implementing " +
            "ProfileCompletionContract.View";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MethodChecker.getColor(this, R.color.white)));
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.grey_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop(), 30, toolbar.getPaddingBottom());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, com.tokopedia.core.R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null)
            fragment = ProfileCompletionFragment.createInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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

    public void onFinishedForm() {
        ProfileCompletionFinishedFragment fragment = ProfileCompletionFinishedFragment.createInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public ProfileCompletionContract.View getProfileCompletionContractView() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                ProfileCompletionContract.View)
            return (ProfileCompletionFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        else
            throw new RuntimeException(ERROR_IMPLEMENT_LISTENER);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
