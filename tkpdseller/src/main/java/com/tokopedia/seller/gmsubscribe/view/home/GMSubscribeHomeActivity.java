package com.tokopedia.seller.gmsubscribe.view.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.view.checkout.activity.GMCheckoutActivity;
import com.tokopedia.seller.gmsubscribe.view.product.activity.GMProductActivity;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GMSubscribeHomeActivity
        extends DrawerPresenterActivity
        implements GMHomeFragmentCallback{


    public static final int REQUEST_PRODUCT = 1;
    private FragmentManager fragmentManager;

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gmsubscribe;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        fragmentManager = getFragmentManager();
    }

    @Override
    protected void setActionVar() {
        goToGMHome();
    }

    public void goToGMHome() {
        if(fragmentManager.findFragmentByTag(GMHomeFragment.TAG) == null) {
            inflateFragment(
                    GMHomeFragment.createFragment(),
                    false,
                    GMHomeFragment.TAG);
        }
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.parent_view, fragment, tag);
        if(isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void goToGMProductFristTime() {
        Intent intent = GMProductActivity.selectProductFirstTime(this);
        startActivityForResult(intent, REQUEST_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                processIntent(data);
            }
        }
    }

    private void processIntent(Intent data) {
        Bundle bundle = data.getExtras();
        int selected = bundle.getInt(GMProductActivity.SELECTED_PRODUCT);
        Intent intent = GMCheckoutActivity.processSelectedProduct(this, selected);
        startActivity(intent);
    }

    @Override
    public void changeActionBarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setDrawer(boolean isShown) {
        drawer.setEnabled(isShown);
    }



    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GM_SUBSCRIBE;
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND;
    }
}
