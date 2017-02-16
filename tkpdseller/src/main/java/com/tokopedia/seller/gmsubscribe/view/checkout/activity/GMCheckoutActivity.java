package com.tokopedia.seller.gmsubscribe.view.checkout.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.view.checkout.fragment.GMCheckoutFragment;
import com.tokopedia.seller.gmsubscribe.view.checkout.fragment.GMCheckoutFragmentCallback;
import com.tokopedia.seller.gmsubscribe.view.payment.GMDynamicPaymentActivity;
import com.tokopedia.seller.gmsubscribe.view.product.activity.GMProductActivity;

/**
 * Created by sebastianuskh on 1/31/17.
 */

public class GMCheckoutActivity extends BasePresenterActivity implements GMCheckoutFragmentCallback {

    public static final String CURRENT_SELECTED_PRODUCT = "CURRENT_SELECTED_PRODUCT";
    private static final int CHANGE_SELECTED_PRODUCT = 100;
    private static final int SELECT_AUTO_SUBSCRIBE_PRODUCT = 200;
    private static final int CHANGE_AUTO_SUBSCRIBE_PRODUCT = 300;
    private int currentSelected;
    private FragmentManager fragmentManager;

    public static Intent processSelectedProduct(Context context, int selected) {
        Intent intent = new Intent(context, GMCheckoutActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_SELECTED_PRODUCT, selected);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        currentSelected = bundle.getInt(CURRENT_SELECTED_PRODUCT);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gmsubscribe;
    }

    @Override
    protected void initView() {

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
        initCheckoutFragment();
    }


    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void changeCurrentSelected(Integer selectedProduct) {
        Intent intent = GMProductActivity.changeProductSelected(this, selectedProduct);;
        startActivityForResult(intent, CHANGE_SELECTED_PRODUCT);
    }

    @Override
    public void selectAutoSubscribePackageFirstTime() {
        Intent intent = GMProductActivity.selectAutoProductFirstTime(this);
        startActivityForResult(intent, SELECT_AUTO_SUBSCRIBE_PRODUCT);
    }

    @Override
    public void changeAutoSubscribePackage(Integer autoExtendSelectedProduct) {
        Intent intent = GMProductActivity.changeAutoProductSelected(this, autoExtendSelectedProduct);
        startActivityForResult(intent, CHANGE_AUTO_SUBSCRIBE_PRODUCT);
    }

    @Override
    public void goToDynamicPayment(String url, String parameter, String callbackUrl, Integer paymentId) {
        Intent intent = GMDynamicPaymentActivity.startPaymentWebview(this, url, parameter, paymentId, callbackUrl);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHANGE_SELECTED_PRODUCT:
                    updateSelectedProduct(data);
                    break;
                case SELECT_AUTO_SUBSCRIBE_PRODUCT:
                case CHANGE_AUTO_SUBSCRIBE_PRODUCT:
                    updateAutoSubscribeSelectedProduct(data);
            }
        }

    }

    private void updateAutoSubscribeSelectedProduct(Intent data) {
        Bundle bundle = data.getExtras();
        int selected = bundle.getInt(GMProductActivity.SELECTED_PRODUCT);
        updateAutoSubscribeSelectedProductOnFragment(selected);
    }

    private void updateSelectedProduct(Intent data) {
        Bundle bundle = data.getExtras();
        int selected = bundle.getInt(GMProductActivity.SELECTED_PRODUCT);
        updateSelectedProductOnFragment(selected);
    }

    private void updateSelectedProductOnFragment(int selected) {
        Fragment fragment = fragmentManager.findFragmentByTag(GMCheckoutFragment.TAG);
        if(fragment != null && fragment instanceof GMCheckoutFragment) {
            ((GMCheckoutFragment)fragment).updateSelectedProduct(selected);
        }
    }

    private void updateAutoSubscribeSelectedProductOnFragment(int selected) {
        Fragment fragment = fragmentManager.findFragmentByTag(GMCheckoutFragment.TAG);
        if(fragment != null && fragment instanceof GMCheckoutFragment) {
            ((GMCheckoutFragment)fragment).updateSelectedAutoProduct(selected);
        }
    }

    private void initCheckoutFragment() {
        if(fragmentManager.findFragmentByTag(GMCheckoutFragment.TAG) == null) {
            inflateFragment(
                    GMCheckoutFragment.createFragment(currentSelected),
                    false,
                    GMCheckoutFragment.TAG);
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
}
