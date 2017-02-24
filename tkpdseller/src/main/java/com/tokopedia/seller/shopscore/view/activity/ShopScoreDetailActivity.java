package com.tokopedia.seller.shopscore.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.view.fragment.ShopScoreDetailFragment;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailActivity extends BasePresenterActivity {
    private FragmentManager fragmentManager;

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
        inflateFragment(
                ShopScoreDetailFragment.createFragment(),
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

}
