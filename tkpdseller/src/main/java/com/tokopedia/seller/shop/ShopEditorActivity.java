package com.tokopedia.seller.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.seller.shop.di.component.DaggerDeleteCacheComponent;
import com.tokopedia.seller.shop.di.component.DeleteCacheComponent;
import com.tokopedia.seller.shop.fragment.ShopCreateFragment;
import com.tokopedia.seller.shop.fragment.ShopEditorFragment;
import com.tokopedia.seller.shop.presenter.ShopCreateView;
import com.tokopedia.seller.shop.presenter.ShopEditorView;
import com.tokopedia.seller.shop.presenter.ShopSettingView;
import com.tokopedia.seller.shopsettings.shipping.OpenShopEditShipping;
import com.tokopedia.seller.shopsettings.shipping.fragment.EditShippingViewListener;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.seller.shopsettings.shipping.OpenShopEditShipping.RESUME_OPEN_SHOP_KEY;

/**
 * Created by Zulfikar on 5/19/2016.
 */
public class ShopEditorActivity extends TActivity implements
        ShopSettingView, ShopEditorFragment.OnShopEditorFragmentListener {

    FragmentManager supportFragmentManager;
    String FRAGMENT;

    FrameLayout container;
    private String onBack;

    /*@DeepLink(Constants.Applinks.CREATE_SHOP)
    public static Intent getCallingApplinkCreateShopIntent(Context context, Bundle extras) {
        if (SessionHandler.isV4Login(context)
                && !SessionHandler.isUserHasShop(context)) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return new Intent(context, ShopEditorActivity.class)
                    .setData(uri.build())
                    .putExtra(ShopSettingView.FRAGMENT_TO_SHOW, ShopSettingView.CREATE_SHOP_FRAGMENT_TAG);
        } else {
            return HomeRouter.getHomeActivityInterfaceRouter(context);
        }
    }*/

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
            case CREATE_SHOP_FRAGMENT_TAG:
                if (!isFragmentCreated(CREATE_SHOP_FRAGMENT_TAG)) {
                    fragment = ShopCreateFragment.newInstance();
                } else {
                    fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                }
                moveToFragment(fragment, true, CREATE_SHOP_FRAGMENT_TAG);
                createCustomToolbar(getString(R.string.title_open_shop));
                break;
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
        if (activity.getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) activity.getApplication()).goToHome(activity);
        }
        Intent intent = new Intent(activity, ShopInfoActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        if (activity instanceof AppCompatActivity) {
            activity.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) ||
                requestCode == ImageGallery.TOKOPEDIA_GALLERY) {
            ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
                @Override
                public void onSuccess(ArrayList<String> imageUrls) {
                    File file = writeImageToTkpdPath(compressImage(imageUrls.get(0)));
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
                public void onSuccess(String path) {
                    File file = writeImageToTkpdPath(compressImage(path));
                    Fragment fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                    if (fragment != null && file != null) {
                        ((ShopCreateView) fragment).setShopAvatar(file.getPath());
                    }
                    fragment = supportFragmentManager.findFragmentByTag(EDIT_SHOP_FRAGMENT_TAG);
                    if (fragment != null && file != null) {
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

        } else if (requestCode == ShopCreateView.REQUEST_EDIT_SHIPPING) {
            if (data != null) {
                Fragment fragment = supportFragmentManager.findFragmentByTag(CREATE_SHOP_FRAGMENT_TAG);
                OpenShopData shippingData = data.getParcelableExtra(EditShippingViewListener.EDIT_SHIPPING_DATA);

                if (fragment != null) {
                    ((ShopCreateView) fragment).saveShippingData(shippingData);
                }
            }
        }


    }

    public File writeImageToTkpdPath(byte[] buffer) {
        if (buffer != null) {
            File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File photo = new File(directory.getAbsolutePath() + "/image.jpg");

            if (photo.exists()) {
                photo.delete();
            }

            try {
                FileOutputStream fos = new FileOutputStream(photo.getPath());

                fos.write(buffer);
                fos.close();
            } catch (java.io.IOException e) {
                return null;
            }
            return photo;
        }
        return null;
    }

    public byte[] compressImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Bitmap tempPicToUpload = null;
        if (tempPic != null) {
            try {
                tempPic = new ImageHandler().RotatedBitmap(tempPic, path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
                tempPicToUpload = new ImageHandler().ResizeBitmap(tempPic, 2048);
            } else {
                tempPicToUpload = tempPic;
            }
            tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, 70, bao);
            return bao.toByteArray();
        }
        return null;
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
            deleteShopInfoUseCase.executeSync(RequestParams.EMPTY);
        }
    }
}
