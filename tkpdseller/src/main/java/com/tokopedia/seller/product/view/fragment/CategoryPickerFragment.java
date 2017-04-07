package com.tokopedia.seller.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;
import com.tokopedia.seller.product.di.component.CategoryPickerViewComponent;
import com.tokopedia.seller.product.di.component.DaggerCategoryPickerViewComponent;
import com.tokopedia.seller.product.di.module.CategoryPickerViewModule;
import com.tokopedia.seller.product.view.adapter.category.CategoryPickerLevelAdapter;
import com.tokopedia.seller.product.view.adapter.category.CategoryPickerLevelAdapterListener;
import com.tokopedia.seller.product.view.model.CategoryViewModel;
import com.tokopedia.seller.product.view.presenter.CategoryPickerPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerFragment
        extends BaseDaggerFragment
        implements CategoryPickerView, CategoryPickerLevelAdapterListener {
    public static final String TAG = "CategoryPickerFragment";

    @Inject
    CategoryPickerPresenter presenter;

    private CategoryPickerLevelAdapter adapter;
    private CategoryPickerFragmentListener listener;

    public static CategoryPickerFragment createInstance() {
        return new CategoryPickerFragment();
    }

    @Override
    protected void initInjector() {
        CategoryPickerViewComponent component = DaggerCategoryPickerViewComponent
                .builder()
                .categoryPickerComponent(getComponent(CategoryPickerComponent.class))
                .categoryPickerViewModule(new CategoryPickerViewModule())
                .build();
        component.inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryPickerFragmentListener){
            this.listener = (CategoryPickerFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement CategoryPickerFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_picker_fragment_layout, container, false);
        setupRecyclerView(view);
        presenter.attachView(this);
        initVar();
        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CategoryPickerLevelAdapter(this);
        categoryRecyclerView.setAdapter(adapter);
    }

    private void initVar() {
        presenter.fetchAllCategoryData();
    }

    @Override
    public void showLoadingDialog() {
        adapter.showLoadingFull(true);
    }

    @Override
    public void dismissLoadingDialog() {
        adapter.showLoadingFull(false);
    }

    @Override
    public void renderCategory(List<CategoryViewModel> listCategory) {
        adapter.addLevelItem(listCategory);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void selectParent(int categoryId) {
        presenter.fetchCategoryWithParent(categoryId);
    }

    @Override
    public void selectSetCategory(List<CategoryViewModel> listCategory) {
        listener.selectSetCategory(listCategory);
    }
}
