package com.tokopedia.seller.product.edit.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.dialog.AddEtalaseDialog;
import com.tokopedia.seller.product.edit.view.dialog.TextPickerDialogListener;
import com.tokopedia.seller.product.edit.view.fragment.EtalasePickerFragment;
import com.tokopedia.seller.product.edit.view.listener.EtalasePickerFragmentListener;
import com.tokopedia.seller.product.edit.view.listener.EtalasePickerView;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerActivity
        extends BaseActivity
        implements HasComponent<AppComponent>,
        EtalasePickerFragmentListener,
        TextPickerDialogListener {

    public static final String ETALASE_ID = "ETALASE_ID";
    public static final String ETALASE_NAME = "ETALASE_NAME";
    public static final int UNSELECTED_ETALASE_ID = -1;
    public static final String SELECTED_ETALASE_ID = "SELECTED_ETALASE_ID";
    private FragmentManager fragmentManager;

    public static Intent createInstance(Context context, long etalaseId) {
        Intent intent = new Intent(context, EtalasePickerActivity.class);
        intent.putExtra(SELECTED_ETALASE_ID, etalaseId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_simple_fragment);
        fragmentManager = getSupportFragmentManager();
        inflateEtalasePickerFragment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void inflateEtalasePickerFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(EtalasePickerFragment.TAG);
        if (fragment == null) {
            long etalaseId = getIntent().getLongExtra(SELECTED_ETALASE_ID, UNSELECTED_ETALASE_ID);
            fragment = EtalasePickerFragment.createInstance(etalaseId);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, EtalasePickerFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void openAddNewEtalaseDialog() {
        AddEtalaseDialog dialog = new AddEtalaseDialog();
        dialog.show(fragmentManager, AddEtalaseDialog.TAG);
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
    public void onTextPickerSubmitted(String newEtalaseName) {
        Fragment fragment = fragmentManager.findFragmentByTag(EtalasePickerFragment.TAG);
        if (fragment != null && fragment instanceof EtalasePickerView){
            ((EtalasePickerView)fragment).addNewEtalase(newEtalaseName);
        } else {
            throw new RuntimeException("fragment is not available or not instance of EtalasePicker");
        }
    }
}
