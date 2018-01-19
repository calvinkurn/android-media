package com.tokopedia.otp.phoneverification.view.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationActivationFragment;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationActivationActivity extends BasePresenterActivity {

    public static final String EXTRA_IS_MANDATORY = "EXTRA_IS_MANDATORY";
    public static final String EXTRA_IS_LOGOUT_ON_BACK = "EXTRA_LOGOUT_ON_BACK";

    private boolean canSkip = false;
    private boolean isLogoutOnBack = false;

    public static Intent getIntent(Context context, boolean isMandatory, boolean isLogoutOnBack){
        Intent intent = new Intent(context, PhoneVerificationActivationActivity.class);
        intent.putExtra(EXTRA_IS_MANDATORY, isMandatory);
        intent.putExtra(EXTRA_IS_LOGOUT_ON_BACK, isLogoutOnBack);
        return intent;
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
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IS_MANDATORY)) {
            canSkip = intent.getBooleanExtra(EXTRA_IS_MANDATORY, false);
        }
        if (intent.hasExtra(EXTRA_IS_LOGOUT_ON_BACK)) {
            isLogoutOnBack = intent.getBooleanExtra(EXTRA_IS_LOGOUT_ON_BACK, false);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_verification_activation;
    }

    @Override
    protected void initView() {

        PhoneVerificationActivationFragment fragmentHeader = PhoneVerificationActivationFragment.createInstance();
        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance(getPhoneVerificationListener(), canSkip);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else if (((PhoneVerificationFragment) getFragmentManager().findFragmentById(R.id.container)).getListener() == null) {
            ((PhoneVerificationFragment) getFragmentManager().findFragmentById(R.id.container))
                    .setPhoneVerificationListener(getPhoneVerificationListener());
        }
        fragmentTransaction.commit();
    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {
                setIntentTarget(Activity.RESULT_CANCELED);
            }

            @Override
            public void onSuccessVerification() {
                setIntentTarget(Activity.RESULT_OK);
            }
        };
    }

    private void setIntentTarget(int result) {
        if (isTaskRoot()
                && GlobalConfig.isSellerApp()
                && isHasShop()) {
            goToSellerHome();
        } else if (isTaskRoot()
                && GlobalConfig.isSellerApp()) {
            goToSellerShopCreateEdit();
        } else if (isTaskRoot()) {
            goToConsumerHome();
        } else {
            setResult(result);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (isLogoutOnBack) {
            SessionHandler session = new SessionHandler(this);
            session.Logout(this);
        } else {
            super.onBackPressed();
        }
    }

    private void goToSellerHome() {
        Intent intent = SellerAppRouter.getSellerHomeActivity(PhoneVerificationActivationActivity.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void goToConsumerHome() {
        Intent intent = HomeRouter.getHomeActivityInterfaceRouter(PhoneVerificationActivationActivity.this);
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);
        startActivity(intent);
        finish();
    }

    private void goToSellerShopCreateEdit() {
        Intent intent = SellerRouter.getActivityShopCreateEdit(PhoneVerificationActivationActivity.this);
        startActivity(intent);
        finish();
    }

    private boolean isHasShop() {
        return SessionHandler.isUserHasShop(this);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PhoneVerificationActivationActivity.class);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
