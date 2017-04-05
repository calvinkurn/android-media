package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.CatalogPickerComponent;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;
import com.tokopedia.seller.product.di.component.CategoryPickerViewComponent;
import com.tokopedia.seller.product.di.component.DaggerCategoryPickerViewComponent;
import com.tokopedia.seller.product.di.module.CategoryPickerViewModule;
import com.tokopedia.seller.product.view.presenter.CatalogPickerPresenter;
import com.tokopedia.seller.product.view.presenter.CategoryPickerPresenter;

import javax.inject.Inject;

/**
 * Created by sebastianuskh on 4/3/17.
 */

public class CatalogPickerFragment extends BaseDaggerFragment implements CatalogPickerView{
    public static final String TAG = "CatalogPicker";

    CatalogPickerPresenter presenter;

    private TkpdProgressDialog loadingDialog;

    public static CatalogPickerFragment createInstance() {
        return new CatalogPickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

    }

    @Override
    protected void initInjector() {
        CatalogPickerComponent catalogPickerComponent = getComponent(CatalogPickerComponent.class);
        presenter = catalogPickerComponent.catalogPickerPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_picker_fragment_layout, container, false);
        presenter.attachView(this);
        initVar();
        return view;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initVar() {

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.fetchCatalogData("samsung", 65, 1, 5);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoadingDialog() {
        loadingDialog.showDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }
}
