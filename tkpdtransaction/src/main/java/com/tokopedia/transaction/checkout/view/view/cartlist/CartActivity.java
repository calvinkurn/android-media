package com.tokopedia.transaction.checkout.view.view.cartlist;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BasePresenterActivity
        implements CartFragment.OnPassingCartDataListener,
        CartRemoveProductFragment.OnPassingCartDataListener {

    private List<CartItemData> mCartItemData;
    private List<CartItemData> mCartItemDataAfterRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCartItemData = new ArrayList<>();
        mCartItemDataAfterRemove = new ArrayList<>();
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
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout_cart_remove, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cart_remove) {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
            if (fragment == null || !(fragment instanceof CartRemoveProductFragment)) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, CartRemoveProductFragment.newInstance(mCartItemData))
                        .addToBackStack(null)
                        .commit();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_tx_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartFragment)) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CartFragment.newInstance())
                    .commit();
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    /**
     * Pass data from cart fragment into its container activity
     * @param cartItemData List of cart items
     */
    @Override
    public void onPassingCartData(List<CartItemData> cartItemData) {
        mCartItemData = new ArrayList<>(cartItemData);
    }

    /**
     * Pass data from cart fragment into its container activity
     *
     * @param cartItemData List of cart items
     */
    @Override
    public void onAfterRemovePassingCartData(List<CartItemData> cartItemData) {
        mCartItemDataAfterRemove = new ArrayList<>(cartItemData);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
