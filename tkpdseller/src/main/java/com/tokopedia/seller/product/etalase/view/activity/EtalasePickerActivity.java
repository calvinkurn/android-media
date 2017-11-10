package com.tokopedia.seller.product.etalase.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;
import com.tokopedia.seller.product.etalase.view.dialog.AddEtalaseDialog;
import com.tokopedia.seller.product.etalase.view.fragment.EtalasePickerFragment;
import com.tokopedia.seller.product.etalase.view.listener.EtalasePickerFragmentListener;
import com.tokopedia.seller.product.etalase.view.listener.EtalasePickerView;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerActivity extends BaseSimpleActivity implements HasComponent<ProductComponent>,
        EtalasePickerFragmentListener, BaseTextPickerDialogFragment.Listener {

    public static final String ETALASE_ID = "ETALASE_ID";
    public static final String ETALASE_NAME = "ETALASE_NAME";
    public static final int UNSELECTED_ETALASE_ID = -1;
    public static final String SELECTED_ETALASE_ID = "SELECTED_ETALASE_ID";

    public static Intent createInstance(Context context, long etalaseId) {
        Intent intent = new Intent(context, EtalasePickerActivity.class);
        intent.putExtra(SELECTED_ETALASE_ID, etalaseId);
        return intent;
    }

    @Override
    public void openAddNewEtalaseDialog() {
        AddEtalaseDialog dialog = new AddEtalaseDialog();
        dialog.show(getSupportFragmentManager(), AddEtalaseDialog.TAG);
    }

    @Override
    public void selectEtalase(Integer etalaseId, String etalaseName) {
        Intent intent = new Intent();
        intent.putExtra(ETALASE_ID, etalaseId);
        intent.putExtra(ETALASE_NAME, etalaseName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTextPickerSubmitted(String text) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if (fragment != null && fragment instanceof EtalasePickerView){
            ((EtalasePickerView)fragment).addNewEtalase(text);
        } else {
            throw new RuntimeException("fragment is not available or not instance of EtalasePicker");
        }
    }

    @Override
    protected Fragment getNewFragment() {
        long etalaseId = getIntent().getLongExtra(SELECTED_ETALASE_ID, UNSELECTED_ETALASE_ID);
        return EtalasePickerFragment.createInstance(etalaseId);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }
}