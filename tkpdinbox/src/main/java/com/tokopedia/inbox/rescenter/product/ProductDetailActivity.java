package com.tokopedia.inbox.rescenter.product;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailContract;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailImpl;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;
import com.tokopedia.track.TrackApp;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailActivity extends BasePresenterActivity<ProductDetailContract.Presenter>
    implements ProductDetailContract.ViewListener, HasComponent<ResolutionDetailComponent> {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_TROUBLE_ID = "trouble_id";
    private static final String EXTRA_PARAM_PRODUCT_NAME = "product_name";
    private static final String EXTRA_PARAM_IS_RESO_DETAIL = "is_reso_detail";

    private static final String TAG_DETAIL_PRODUCT_FRAGMENT =
            ProductDetailFragment.class.getSimpleName();

    private Fragment fragment;
    private String resolutionID;
    private String troubleID;
    private String productName;

    public static Intent newInstance(Context context, String resolutionID, String troubleID, String productName) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putString(EXTRA_PARAM_TROUBLE_ID, troubleID);
        bundle.putString(EXTRA_PARAM_PRODUCT_NAME, productName);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceResolutionDetail(Context context, String resolutionID, String troubleID, String productName) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putString(EXTRA_PARAM_TROUBLE_ID, troubleID);
        bundle.putString(EXTRA_PARAM_PRODUCT_NAME, productName);
        bundle.putBoolean(EXTRA_PARAM_IS_RESO_DETAIL, true);
        intent.putExtras(bundle);
        return intent;
    }
    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public String getTroubleID() {
        return troubleID;
    }

    @Override
    public void setTroubleID(String troubleID) {
        this.troubleID = troubleID;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        setResolutionID(extras.getString(EXTRA_PARAM_RESOLUTION_ID));
        setTroubleID(extras.getString(EXTRA_PARAM_TROUBLE_ID));
        setProductName(extras.getString(EXTRA_PARAM_PRODUCT_NAME));
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductDetailImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_center_detail;
    }

    @Override
    protected void initView() {
        initAppBarTitle();
        presenter.generateFragment();
        inflateFragment();
    }

    private void initAppBarTitle() {
        final ActionBar actionBar = getSupportActionBar();
        String title = getString(R.string.title_activity_product_detail_rescenter);
        title += " " + productName;
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void inflateFragment() {
        if (getFragmentManager().findFragmentByTag(TAG_DETAIL_PRODUCT_FRAGMENT) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, getFragment(), TAG_DETAIL_PRODUCT_FRAGMENT)
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
    public ResolutionDetailComponent getComponent() {
        return DaggerResolutionDetailComponent.builder()
                .appComponent(getApplicationComponent())
                .resolutionDetailModule(new ResolutionDetailModule())
                .build();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.get(EXTRA_PARAM_IS_RESO_DETAIL) != null && extras.getBoolean(EXTRA_PARAM_IS_RESO_DETAIL)) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoDetailClickChatBox(resolutionID).getEvent());
            }
        }
        super.onBackPressed();
    }
}
