package com.tokopedia.seller.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.seller.shopsettings.shipping.OpenShopEditShipping;
import com.tokopedia.seller.shopsettings.shipping.fragment.EditShippingViewListener;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.utils.UploadPhotoTask;
import com.tokopedia.seller.shop.fragment.ShopCreateFragment;
import com.tokopedia.seller.shop.fragment.ShopEditorFragment;
import com.tokopedia.seller.shop.presenter.ShopCreateView;
import com.tokopedia.seller.shop.presenter.ShopEditorView;
import com.tokopedia.seller.shop.presenter.ShopSettingView;

import java.io.File;
import java.util.ArrayList;


import static com.tokopedia.seller.shopsettings.shipping.OpenShopEditShipping.RESUME_OPEN_SHOP_KEY;

/**
 * Created by Zulfikar on 5/19/2016.
 */
public class ShopEditorActivity extends TkpdActivity implements
        ShopSettingView {

    FragmentManager supportFragmentManager;
    String FRAGMENT;
    Toolbar toolbar;

    FrameLayout container;
    private String onBack;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_EDITOR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflateView(R.layout.activity_simple_fragment);
        container = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.removeAllViews();
        setSupportActionBar(toolbar);

        fetchExtras(getIntent(), savedInstanceState);

        supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);
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
            case CREATE_SHOP_FRAGMENT_TAG:
                if (!isFragmentCreated(CREATE_SHOP_FRAGMENT_TAG)) {
                    fragment = ShopCreateFragment.newInstance();
                } else {
                    fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                }
                moveToFragment(fragment, true, CREATE_SHOP_FRAGMENT_TAG);
                createCustomToolbar(getString(R.string.title_open_shop));
                drawerHelper.setEnabled(false);
                break;
            case EDIT_SHOP_FRAGMENT_TAG:
                if (!isFragmentCreated(EDIT_SHOP_FRAGMENT_TAG)) {
                    fragment = new ShopEditorFragment();
                } else {
                    fragment = supportFragmentManager.findFragmentByTag(EDIT_SHOP_FRAGMENT_TAG);
                }
                moveToFragment(fragment, false, EDIT_SHOP_FRAGMENT_TAG);
                createCustomToolbar(getString(R.string.title_shop_information_menu));
                drawerHelper.setEnabled(true);
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
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public int getDrawerPosition() {
        return TkpdState.DrawerPosition.CREATE_SHOP;
    }

    public static void startOpenShopEditShippingActivity(AppCompatActivity context) {
        Intent intent = new Intent(context, OpenShopEditShipping.class);
        context.startActivityForResult(intent, ShopCreateView.REQUEST_EDIT_SHIPPING);
    }

    public static void continueOpenShopEditShippingActivity(AppCompatActivity context, OpenShopData openShopData) {
        Intent intent = new Intent(context, OpenShopEditShipping.class);
        intent.putExtra(RESUME_OPEN_SHOP_KEY, openShopData);
        context.startActivityForResult(intent, ShopCreateView.REQUEST_EDIT_SHIPPING);
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

    public static void finishActivity(Bundle bundle, Activity activity) {
        Router.goToHome(activity);
        Intent intent = new Intent(activity, ShopInfoActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).finish();
        }
        TrackingUtils.eventLoca("event : open store");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls) {
                File file = UploadPhotoTask.writeImageToTkpdPath(AddProductFragment.compressImage(imageUrls.get(0)));
                Fragment fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                if (fragment != null) {
                    ((ShopCreateView) fragment).setShopAvatar(file.getPath());
                }
                fragment = supportFragmentManager.findFragmentByTag(EDIT_SHOP_FRAGMENT_TAG);
                if (fragment != null) {
                    ((ShopEditorView) fragment).uploadImage(file.getPath());
                }
            }

            @Override
            public void onSuccess(String path, int position) {
                File file = UploadPhotoTask.writeImageToTkpdPath(AddProductFragment.compressImage(path));
                Fragment fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                if (fragment != null) {
                    ((ShopCreateView) fragment).setShopAvatar(file.getPath());
                }
                fragment = supportFragmentManager.findFragmentByTag(EDIT_SHOP_FRAGMENT_TAG);
                if (fragment != null) {
                    ((ShopEditorView) fragment).uploadImage(file.getPath());
                }
            }

            @Override
            public void onFailed(String message) {
                Fragment fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                if (fragment != null) {
                    ((ShopCreateView) fragment).onMessageError(0, message);
                }
                fragment = supportFragmentManager.findFragmentByTag(EDIT_SHOP_FRAGMENT_TAG);
                if (fragment != null) {
                    ((ShopEditorView) fragment).onMessageError(0, message);
                }

            }

            @Override
            public Context getContext() {
                return ShopEditorActivity.this;
            }
        }, requestCode, resultCode, data);

        if (data != null) {
            switch (requestCode) {
                case ShopCreateView.REQUEST_EDIT_SHIPPING:
                    Fragment fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                    OpenShopData shippingData = data.getParcelableExtra(EditShippingViewListener.EDIT_SHIPPING_DATA);

                    if (fragment != null) {
                        ((ShopCreateView) fragment).saveShippingData(shippingData);
                    }
                    break;
            }
        }

    }

    private void createCustomToolbar(String shopTitle) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(shopTitle);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_TO_SHOW, FRAGMENT);
        outState.putString(ON_BACK, onBack);
        super.onSaveInstanceState(outState);
    }
}
