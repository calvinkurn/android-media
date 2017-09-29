package com.tokopedia.seller.product.manage.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.constant.ManageProductConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.di.DaggerManageProductComponent;
import com.tokopedia.seller.product.manage.di.ManageProductModule;
import com.tokopedia.seller.product.manage.view.adapter.SortManageProductAdapter;
import com.tokopedia.seller.product.manage.view.listener.SortManageProductView;
import com.tokopedia.seller.product.manage.view.model.SortManageProductModel;
import com.tokopedia.seller.product.manage.view.presenter.SortManageProductPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class SortManageProductFragment extends BaseListFragment<SortManageProductPresenter, SortManageProductModel> implements SortManageProductView{

    @Inject
    SortManageProductPresenter sortManageProductPresenter;

    String selectedSortProduct = SortProductOption.POSITION;

    public static SortManageProductFragment createInstance(String selectedSortProduct){
        SortManageProductFragment sortManageProductFragment = new SortManageProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ManageProductConstant.EXTRA_SORT_SELECTED, selectedSortProduct);
        sortManageProductFragment.setupArguments(bundle);
        return sortManageProductFragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerManageProductComponent.builder()
                .productComponent(getComponent(ProductComponent.class))
                .manageProductModule(new ManageProductModule())
                .build()
                .inject(this);
        sortManageProductPresenter.attachView(this);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        selectedSortProduct = arguments.getString(ManageProductConstant.EXTRA_SORT_SELECTED, SortProductOption.POSITION);
    }

    @Override
    protected BaseListAdapter<SortManageProductModel> getNewAdapter() {
        SortManageProductAdapter sortManageProductAdapter = new SortManageProductAdapter();
        sortManageProductAdapter.setSortProductOption(selectedSortProduct);
        return sortManageProductAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        sortManageProductPresenter.getListSortManageProduct(getResources().getStringArray(R.array.sort_option));
    }

    @Override
    public void onSuccessGetListSort(List<SortManageProductModel> sortManageProductModels) {
        onSearchLoaded(sortManageProductModels, sortManageProductModels.size());
    }

    @Override
    public void onItemClicked(SortManageProductModel sortManageProductModel) {
        ((SortManageProductAdapter)adapter).setSortProductOption(sortManageProductModel.getId());
        adapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra(ManageProductConstant.EXTRA_SORT_SELECTED, sortManageProductModel.getId());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected boolean hasNextPage() {
        return false;
    }
}
