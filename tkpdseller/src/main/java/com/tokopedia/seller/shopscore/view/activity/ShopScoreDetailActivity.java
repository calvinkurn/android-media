package com.tokopedia.seller.shopscore.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.shopscore.view.fragment.ShopScoreDetailFragment;
import com.tokopedia.seller.shopscore.view.fragment.ShopScoreDetailFragmentCallback;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailActivity extends BasePresenterActivity implements ShopScoreDetailFragmentCallback {
    private static final String SELLER_CENTER_LINK = "https://seller.tokopedia.com/";
    private static final String SHOP_SCORE_INFORMATION = "https://help.tokopedia.com/hc/en-us/articles/115000854466-Performa-Toko";
    private FragmentManager fragmentManager;


    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SELLER_SHOP_SCORE;
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
        return R.layout.activity_shop_score_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setActionVar() {
        Fragment fragment =
                (fragmentManager.findFragmentByTag(ShopScoreDetailFragment.TAG));
        if (fragment == null) {
            fragment = ShopScoreDetailFragment.createFragment();
        }
        inflateFragment(
                fragment,
                false,
                ShopScoreDetailFragment.TAG);
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_container, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void goToGmSubscribe() {
        if (getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getApplication()).goToGMSubscribe(this);
        }
    }

    @Override
    public void goToSellerCenter() {
        openUrl(Uri.parse(SELLER_CENTER_LINK));
    }

    @Override
    public void goToCompleteInformation() {
        openUrl(Uri.parse(SHOP_SCORE_INFORMATION));
    }

    private void openUrl(Uri parse) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, parse);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            NetworkErrorHelper.showSnackbar(this, getString(R.string.error_no_browser));
        }
    }
}
