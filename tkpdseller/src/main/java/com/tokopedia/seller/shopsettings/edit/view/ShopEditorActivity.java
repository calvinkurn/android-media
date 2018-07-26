package com.tokopedia.seller.shopsettings.edit.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shop.common.di.component.DaggerDeleteCacheComponent;
import com.tokopedia.seller.shop.common.di.component.DeleteCacheComponent;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.seller.shopsettings.edit.presenter.ShopSettingView;

import javax.inject.Inject;

/**
 * Created by Zulfikar on 5/19/2016.
 */
public class ShopEditorActivity extends TActivity implements
        ShopSettingView, ShopEditorFragment.OnShopEditorFragmentListener {

    FragmentManager supportFragmentManager;
    String FRAGMENT;

    FrameLayout container;
    private String onBack;

    @Inject
    DeleteShopInfoUseCase deleteShopInfoUseCase;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_EDITOR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflateView(R.layout.activity_simple_fragment);
        container = (FrameLayout) findViewById(R.id.container);

        fetchExtras(getIntent(), savedInstanceState);

        supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null) {
            initFragment(FRAGMENT);
        }
        DeleteCacheComponent deleteCacheComponent = DaggerDeleteCacheComponent.builder().appComponent(getApplicationComponent()).build();
        deleteCacheComponent.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initFragment(String FRAGMENT_TAG) {
        Fragment fragment = null;

        switch (FRAGMENT_TAG) {
            case EDIT_SHOP_FRAGMENT_TAG:
                if (!isFragmentCreated(EDIT_SHOP_FRAGMENT_TAG)) {
                    fragment = new ShopEditorFragment();
                } else {
                    fragment = supportFragmentManager.findFragmentByTag(EDIT_SHOP_FRAGMENT_TAG);
                }
                moveToFragment(fragment, false, EDIT_SHOP_FRAGMENT_TAG);
                createCustomToolbar(getString(R.string.title_shop_information_menu));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (onBack == null || onBack.equals(FINISH)) {
            finish();
        } else if (onBack.equals(LOG_OUT)) {
            SessionHandler session = new SessionHandler(this);
            session.Logout(this);
            UnifyTracking.eventDrawerClick((AppEventTracking.EventLabel.SIGN_OUT));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, TAG);
        if (isAddtoBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void fetchExtras(Intent intent, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            FRAGMENT = savedInstanceState.getString(FRAGMENT_TO_SHOW, "");
            onBack = savedInstanceState.getString(ON_BACK, "");
        } else if (getIntent().getExtras() != null) {
            FRAGMENT = getIntent().getExtras().getString(FRAGMENT_TO_SHOW, "");
            onBack = intent.getExtras().getString(ON_BACK);
        }
    }

    @Override
    public boolean isFragmentCreated(String tag) {
        return supportFragmentManager.findFragmentByTag(tag) != null;
    }

    private void createCustomToolbar(String shopTitle) {
        toolbar.setTitle(shopTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_TO_SHOW, FRAGMENT);
        outState.putString(ON_BACK, onBack);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void deleteCacheShopInfov2() {
        if (deleteShopInfoUseCase != null) {
            deleteShopInfoUseCase.executeSync();
        }
    }
}
