package com.tokopedia.seller.reputation.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.reputation.view.listener.SellerReputationInterface;

/**
 * @author normansyahputa on 3/30/16.
 */
public class SellerReputationActivity extends BaseActivity implements SellerReputationInterface {

    ImageHandler imageHandler;
    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatusBar(getWindow());
        inject();
        setContentView(R.layout.activity_seller_repuation);

        isFirstTime = savedInstanceState == null;
        if (isFirstTime) {
            fetchIntent();
        } else {
            fetchSaveInstanceState(savedInstanceState);
        }
    }

    public void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.green_600));
    }

    private void fetchSaveInstanceState(Bundle savedInstanceState) {

    }

    private void fetchIntent() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // if fragment not created transact fragment.
        if (isFragmentCreated(SellerReputationFragment.TAG)) {
            inflateNewFragment(
                    getContainerId(),
                    SellerReputationFragment.createInstance(),
                    SellerReputationFragment.TAG
            );
        }
    }

    private int getContainerId() {
        return R.id.container;
    }

    private void inflateNewFragment(@IdRes int containerId, Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment, TAG);
        fragmentTransaction.commit();
    }

    /**
     * @param tag tag defined by fragment
     * @return true means fragment is null
     */
    public boolean isFragmentCreated(String tag) {
        return getFragmentManager().findFragmentByTag(tag) == null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public ImageHandler imageHandler() {
        if (imageHandler == null) {
            imageHandler = new ImageHandler(this);
        }
        return imageHandler;
    }

    protected void inject() {
        // do nothing
    }
}