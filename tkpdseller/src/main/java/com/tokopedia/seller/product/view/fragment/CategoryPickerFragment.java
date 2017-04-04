package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;
import com.tokopedia.seller.product.di.component.CategoryPickerViewComponent;
import com.tokopedia.seller.product.di.component.DaggerCategoryPickerViewComponent;
import com.tokopedia.seller.product.di.module.CategoryPickerViewModule;
import com.tokopedia.seller.product.view.presenter.CategoryPickerPresenter;

import javax.inject.Inject;

/**
 * Created by sebastianuskh on 4/3/17.
 */

public class CategoryPickerFragment extends BaseDaggerFragment implements CategoryPickerView{
    public static final String TAG = "CategoryPickerFragment";
    private CategoryPickerViewComponent component;

    @Inject
    CategoryPickerPresenter presenter;

    private TkpdProgressDialog loadingDialog;

    public static CategoryPickerFragment createInstance() {
        return new CategoryPickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

    }

    @Override
    protected void initInjector() {
        component = DaggerCategoryPickerViewComponent
                .builder()
                .categoryPickerComponent(getComponent(CategoryPickerComponent.class))
                .categoryPickerViewModule(new CategoryPickerViewModule())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_picker_fragment_layout, container, false);
        presenter.attachView(this);
        initVar();
        return view;
    }

    private void initVar() {
        presenter.fetchCategoryData();
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
