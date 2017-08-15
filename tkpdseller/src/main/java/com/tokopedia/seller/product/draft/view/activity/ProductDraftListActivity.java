package com.tokopedia.seller.product.draft.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.myproduct.ManageProductSeller;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListActivity extends DrawerPresenterActivity implements HasComponent<ProductComponent>{
    public static final String TAG = ProductDraftListActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductDraftListFragment.newInstance(), TAG)
                    .commit();
        }
    }

    @Override
    public void onErrorGetDeposit(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetNotificationDrawer(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetProfile(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetTokoCash(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetTopPoints(String errorMessage) {
        // no op
    }

    @Override
    public void onServerError() {
        // no op
    }

    @Override
    public void onTimezoneError() {
        // no op
    }

    @Override
    public void onHadesTimeout() {
        // no op
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.DRAFT_PRODUCT;
    }

    @Override
    protected void setupURIPass(Uri data) {
        // no op
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        // no op
    }

    @Override
    protected void initialPresenter() {
        // no op
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setViewListener() {
        // no op
    }

    @Override
    protected void initVar() {
        // no op
    }

    @Override
    protected void setActionVar() {
        // no op
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

}
