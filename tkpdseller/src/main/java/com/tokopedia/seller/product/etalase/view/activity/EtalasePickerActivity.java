package com.tokopedia.seller.product.etalase.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant;
import com.tokopedia.seller.ProductEditItemComponentInstance;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.seller.product.etalase.view.fragment.EtalasePickerFragment;
import com.tokopedia.seller.product.etalase.view.listener.EtalasePickerFragmentListener;

import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerActivity extends BaseSimpleActivity implements HasComponent<ProductComponent>,
        EtalasePickerFragmentListener {

    public static final int UNSELECTED_ETALASE_ID = -1;
    public static final String SELECTED_ETALASE_ID = "SELECTED_ETALASE_ID";

    //region Add Etalase
    private static final int REQUEST_ADD_ETALASE = 1;
    private static final String PARAM_ADD_ETALASE_SUCCESS = "IS_SUCCESS";
    //endregion

    public static Intent createInstance(Context context, long etalaseId) {
        Intent intent = new Intent(context, EtalasePickerActivity.class);
        intent.putExtra(SELECTED_ETALASE_ID, etalaseId);
        return intent;
    }

    @Override
    public void openAddNewEtalaseDialog() {
        Intent etalaseIntent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE_ADD);
        if (etalaseIntent != null) {
            startActivityForResult(etalaseIntent, REQUEST_ADD_ETALASE);
        }
    }

    @Override
    public void selectEtalase(Integer etalaseId, String etalaseName) {
        Intent intent = new Intent();
        intent.putExtra(ProductExtraConstant.EXTRA_ETALASE_ID, etalaseId);
        intent.putExtra(ProductExtraConstant.EXTRA_ETALASE_NAME, etalaseName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected Fragment getNewFragment() {
        long etalaseId;
        Uri uri = getIntent().getData();
        if (uri != null){
            List<String> segments = uri.getPathSegments();
            etalaseId = Long.parseLong(segments.get(segments.size() - 1));
        } else {
            etalaseId = getIntent().getLongExtra(SELECTED_ETALASE_ID, UNSELECTED_ETALASE_ID);
        }
        return EtalasePickerFragment.createInstance(etalaseId);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public ProductComponent getComponent() {
        return ProductEditItemComponentInstance.getComponent(getApplication());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ADD_ETALASE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getBooleanExtra(PARAM_ADD_ETALASE_SUCCESS, false)) {
                reloadEtalaseData();
            }
        }
    }

    private void reloadEtalaseData() {
        Fragment fragment = getFragment();
        if (fragment instanceof EtalasePickerFragment) {
            ((EtalasePickerFragment) fragment).refreshEtalaseData();
        }
    }
}