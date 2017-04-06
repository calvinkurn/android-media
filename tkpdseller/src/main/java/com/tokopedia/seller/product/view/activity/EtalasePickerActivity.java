package com.tokopedia.seller.product.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerEtalasePickerComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerComponent;
import com.tokopedia.seller.product.di.module.EtalasePickerModule;
import com.tokopedia.seller.product.view.dialog.AddEtalaseDialog;
import com.tokopedia.seller.product.view.dialog.AddEtalaseDialogListener;
import com.tokopedia.seller.product.view.fragment.EtalasePickerFragment;
import com.tokopedia.seller.product.view.fragment.EtalasePickerFragmentListener;
import com.tokopedia.seller.product.view.fragment.EtalasePickerView;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerActivity
        extends TActivity
        implements HasComponent<EtalasePickerComponent>,
        EtalasePickerFragmentListener,
        AddEtalaseDialogListener {

    public static final String ETALASE_ID = "ETALASE_ID";
    public static final String ETALASE_NAME = "ETALASE_NAME";
    private FragmentManager fragmentManager;
    private EtalasePickerComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        fragmentManager = getSupportFragmentManager();

        initInjection();
        inflateEtalasePickerFragment();

    }

    private void inflateEtalasePickerFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(EtalasePickerFragment.TAG);
        if (fragment == null) {
            fragment = EtalasePickerFragment.createInstance();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, EtalasePickerFragment.TAG);
        fragmentTransaction.commit();
    }

    private void initInjection() {
        component = DaggerEtalasePickerComponent
                .builder()
                .appComponent(getApplicationComponent())
                .etalasePickerModule(new EtalasePickerModule())
                .build();
    }

    @Override
    public EtalasePickerComponent getComponent() {
        return component;
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
    public void addEtalase(String newEtalaseName) {
        Fragment fragment = fragmentManager.findFragmentByTag(EtalasePickerFragment.TAG);
        if (fragment != null && fragment instanceof EtalasePickerView){
            ((EtalasePickerView)fragment).addNewEtalase(newEtalaseName);
        } else {
            throw new RuntimeException("fragment is not available or not instance of EtalasePicker");
        }
    }
}
