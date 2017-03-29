package com.tokopedia.inbox.rescenter.product;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailContract;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailImpl;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailActivity extends BasePresenterActivity<ProductDetailContract.Presenter>
    implements ProductDetailContract.ViewListener {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_TROUBLE_ID = "trouble_id";

    private static final String TAG_DETAIL_PRODUCT_FRAGMENT =
            ProductDetailFragment.class.getSimpleName();

    private Fragment fragment;
    private String resolutionID;
    private String troubleID;

    public static Intent newInstance(Context context, String resolutionID, String troubleID) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putString(EXTRA_PARAM_TROUBLE_ID, troubleID);
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
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        setResolutionID(extras.getString(EXTRA_PARAM_RESOLUTION_ID));
        setTroubleID(extras.getString(EXTRA_PARAM_TROUBLE_ID));
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
        presenter.generateFragment();
        inflateFragment();
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
}
