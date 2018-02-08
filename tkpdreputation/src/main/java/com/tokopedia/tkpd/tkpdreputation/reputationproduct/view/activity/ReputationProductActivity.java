package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.fragment.ReputationProductFragment;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;

/**
 * Created by hangnadi on 7/7/15.
 */
public class ReputationProductActivity extends BasePresenterActivity {

    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String EXTRA_SHOP_ID = "shop_id";
    public static final String EXTRA_DATA_MODEL = "data_model";


    private String productId;
    private String shopId;
    private ReviewProductModel dataModel;


    public static Intent getCallingIntent(Context context, String productId, String shopId, ReviewProductModel dataModel) {
        Intent callingIntent = new Intent(context, ReputationProductActivity.class);
        callingIntent.putExtra(EXTRA_PRODUCT_ID, productId);
        callingIntent.putExtra(EXTRA_SHOP_ID, shopId);
        callingIntent.putExtra(EXTRA_DATA_MODEL, (Parcelable) dataModel);
        return callingIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
    protected int getLayoutId() {
        return R.layout.activity_reputation_product_view;
    }

    @Override
    protected void initView() {
        Fragment fragment;
        if (getFragmentManager().findFragmentByTag(ReputationProductFragment.class.getSimpleName()) == null) {
            fragment = getFragment();
        } else {
            fragment = getFragmentManager().findFragmentByTag(ReputationProductFragment.class.getSimpleName());
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment, ReputationProductFragment.class.getSimpleName())
                .commit();
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
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_REPUTATION_VIEW_DETAIL;
    }

    private Fragment getFragment() {
        return ReputationProductFragment.createInstance(getIntentProductID(), getIntentShopID(), getIntentModelData());
    }

    private String getIntentProductID() {
        return getIntent().getStringExtra(EXTRA_PRODUCT_ID);
    }

    private String getIntentShopID() {
        return getIntent().getStringExtra(EXTRA_SHOP_ID);
    }

    private ReviewProductModel getIntentModelData() {
        return (ReviewProductModel) getIntent().getParcelableExtra(EXTRA_DATA_MODEL);
    }

}
