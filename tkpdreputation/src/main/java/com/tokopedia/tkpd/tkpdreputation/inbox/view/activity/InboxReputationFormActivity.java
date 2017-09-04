package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFormFragment;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationFormActivity extends BasePresenterActivity
        implements HasComponent {

    public static final String ARGS_REVIEW_ID = "ARGS_REVIEW_ID";
    public static final String ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID";
    public static final String ARGS_PRODUCT_ID = "ARGS_PRODUCT_ID";
    public static final String ARGS_SHOP_ID = "ARGS_SHOP_ID";

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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        String reputationId = getIntent().getExtras().getString(ARGS_REPUTATION_ID, "");
        String reviewId = getIntent().getExtras().getString(ARGS_REVIEW_ID, "");
        String productId = getIntent().getExtras().getString(ARGS_PRODUCT_ID, "");
        String shopId = getIntent().getExtras().getString(ARGS_SHOP_ID, "");


        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxReputationFormFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationFormFragment.createInstance(reviewId, reputationId,
                    productId, shopId);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
    public Object getComponent() {
        return getApplicationComponent();
    }

    public static Intent getGiveReviewIntent(Context context, String reviewId,
                                             String reputationId, String productId,
                                             String shopId) {
        Intent intent = new Intent(context, InboxReputationFormActivity.class);
        intent.putExtra(ARGS_PRODUCT_ID, productId);
        intent.putExtra(ARGS_REPUTATION_ID, reputationId);
        intent.putExtra(ARGS_REVIEW_ID, reviewId);
        intent.putExtra(ARGS_SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUploadHandler.CODE_UPLOAD_IMAGE)
            getFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode,
                    resultCode, data);
        else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
