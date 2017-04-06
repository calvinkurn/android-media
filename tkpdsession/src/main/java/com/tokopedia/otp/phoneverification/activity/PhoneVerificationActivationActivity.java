package com.tokopedia.otp.phoneverification.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationActivationFragment;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationActivationActivity extends BasePresenterActivity {


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
        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance(getPhoneVerificationListener());

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {
                if (isTaskRoot()
                        && GlobalConfig.isSellerApp()
                        && isHasShop()) {
                    Intent intent = SellerAppRouter.getSellerHomeActivity(PhoneVerificationActivationActivity.this);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (isTaskRoot()
                        && GlobalConfig.isSellerApp()) {
                    Intent intent = SellerRouter.getAcitivityShopCreateEdit(PhoneVerificationActivationActivity.this);
                    intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                            SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
                    intent.putExtra(SellerRouter.ShopSettingConstant.ON_BACK,
                            SellerRouter.ShopSettingConstant.LOG_OUT);
                    startActivity(intent);
                    finish();
                } else if (isTaskRoot()) {
                    Intent intent = HomeRouter.getHomeActivityInterfaceRouter(PhoneVerificationActivationActivity.this);
                    intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                            HomeRouter.INIT_STATE_FRAGMENT_FEED);
                    startActivity(intent);
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }

            @Override
            public void onSuccessVerification() {
                if (isTaskRoot()
                        && GlobalConfig.isSellerApp()
                        && isHasShop()) {
                    Intent intent = SellerAppRouter.getSellerHomeActivity(PhoneVerificationActivationActivity.this);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (isTaskRoot()
                        && GlobalConfig.isSellerApp()) {
                    Intent intent = SellerRouter.getAcitivityShopCreateEdit(PhoneVerificationActivationActivity.this);
                    intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                            SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
                    intent.putExtra(SellerRouter.ShopSettingConstant.ON_BACK,
                            SellerRouter.ShopSettingConstant.LOG_OUT);
                    startActivity(intent);
                    finish();
                } else if (isTaskRoot()) {
                    Intent intent = HomeRouter.getHomeActivityInterfaceRouter(PhoneVerificationActivationActivity.this);
                    intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                            HomeRouter.INIT_STATE_FRAGMENT_FEED);
                    startActivity(intent);
                    finish();
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        };
    }

    private boolean isHasShop() {
        return !SessionHandler.getShopID(PhoneVerificationActivationActivity.this).equals("")
                && !SessionHandler.getShopID(PhoneVerificationActivationActivity.this).equals("0");
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
}
