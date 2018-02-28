package com.tokopedia.session.forgotpassword.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.forgotpassword.fragment.ForgotPasswordFragment;
import com.tokopedia.session.forgotpassword.listener.ForgotPasswordFragmentView;
import com.tokopedia.session.forgotpassword.presenter.ForgotPasswordFragmentPresenterImpl;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordActivity extends BasePresenterActivity {

    private static final String TAG = "FORGOT_PASSWORD_FRAGMENT";
    private static final String INTENT_EXTRA_PARAM_EMAIL = "INTENT_EXTRA_PARAM_EMAIL";
    private static final String INTENT_EXTRA_AUTO_RESET = "INTENT_EXTRA_AUTO_RESET";
    private static final String INTENT_EXTRA_REMOVE_FOOTER = "INTENT_EXTRA_REMOVE_FOOTER";

    public static Intent createInstance(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            ForgotPasswordFragment fragment = ForgotPasswordFragment.createInstance(
                    getIntent().getExtras().getString(INTENT_EXTRA_PARAM_EMAIL, ""),
                    getIntent().getExtras().getBoolean(INTENT_EXTRA_AUTO_RESET, false),
                    getIntent().getExtras().getBoolean(INTENT_EXTRA_REMOVE_FOOTER, false));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, TAG);
            fragmentTransaction.commit();
        }
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
        return AppScreen.SCREEN_FORGOT_PASSWORD;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ForgotPasswordFragmentPresenterImpl.REQUEST_FORGOT_PASSWORD_CODE && resultCode == Activity.RESULT_OK) {
            ((ForgotPasswordFragmentView) getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        return intent;
    }

    public static Intent getAutomaticResetPasswordIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        intent.putExtra(INTENT_EXTRA_AUTO_RESET, true);
        intent.putExtra(INTENT_EXTRA_REMOVE_FOOTER, true);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
