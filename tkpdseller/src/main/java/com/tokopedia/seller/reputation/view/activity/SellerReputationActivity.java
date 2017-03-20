package com.tokopedia.seller.reputation.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Toast;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.reputation.view.listener.SellerReputationInterface;
import com.tokopedia.seller.topads.utils.ViewUtils;

/**
 * @author normansyahputa on 3/30/16.
 */
public class SellerReputationActivity extends BaseActivity implements SellerReputationInterface {

    ImageHandler imageHandler;
    private AppComponent component;
    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setTranslucentStatusBar(getWindow());
        inject();
        setContentView(R.layout.activity_seller_repuation);

        isFirstTime = savedInstanceState == null;
        if (isFirstTime) {
            fetchIntent();
        } else {
            fetchSaveInstanceState(savedInstanceState);
        }
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
        } else {
            // do nothing
        }
    }

    private int getContainerId() {
        return R.id.container;
    }

    private void inflateNewFragment(@IdRes int containerId, Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
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
//        networkDemo.setSellerReputationActivity(null);
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