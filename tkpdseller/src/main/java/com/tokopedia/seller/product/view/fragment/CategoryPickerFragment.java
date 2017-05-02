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
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;
import com.tokopedia.seller.product.di.component.CategoryPickerViewComponent;
import com.tokopedia.seller.product.di.component.DaggerCategoryPickerViewComponent;
import com.tokopedia.seller.product.di.module.CategoryPickerViewModule;
import com.tokopedia.seller.product.view.adapter.category.CategoryPickerLevelAdapter;
import com.tokopedia.seller.product.view.adapter.category.CategoryPickerLevelAdapterListener;
import com.tokopedia.seller.product.view.listener.CategoryPickerFragmentListener;
import com.tokopedia.seller.product.view.listener.CategoryPickerView;
import com.tokopedia.seller.product.view.model.CategoryLevelViewModel;
import com.tokopedia.seller.product.view.model.CategoryViewModel;
import com.tokopedia.seller.product.view.presenter.CategoryPickerPresenter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsRetryDataBinder;

import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerFragment extends BaseDaggerFragment implements CategoryPickerView, CategoryPickerLevelAdapterListener {
    public static final String TAG = "CategoryPickerFragment";
    public static final int INIT_UNSELECTED = 0;
    public static final String INIT_SELECTED = "INIT_SELECTED";

    @Inject
    CategoryPickerPresenter presenter;

    private CategoryPickerLevelAdapter adapter;
    private CategoryPickerFragmentListener listener;
    private long selectedCategoryId;

    public static CategoryPickerFragment createInstance(long currentSelected) {
        CategoryPickerFragment categoryPickerFragment = new CategoryPickerFragment();
        Bundle args = new Bundle();
        args.putLong(INIT_SELECTED, currentSelected);
        categoryPickerFragment.setArguments(args);
        return categoryPickerFragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        selectedCategoryId = bundle.getLong(INIT_SELECTED, INIT_UNSELECTED);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    private void setupRecyclerView(View view) {
        RecyclerView categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CategoryPickerLevelAdapter(this);
        TopAdsRetryDataBinder topAdsRetryDataBinder = new TopAdsRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                initVar();
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
        categoryRecyclerView.setAdapter(adapter);
    }

    private void initVar() {
        if (selectedCategoryId == INIT_UNSELECTED){
            presenter.fetchCategoryLevelOne();
        } else {
            presenter.fetchCategoryFromSelected(selectedCategoryId);
        }
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
    public void renderCategory(List<CategoryViewModel> listCategory, long categoryId) {
        adapter.addLevelItem(listCategory, categoryId);
    }

    @Override
    public void renderCategoryFromSelected(List<CategoryLevelViewModel> categoryLevelDomainModels) {
        adapter.render(categoryLevelDomainModels);
    }

    @Override
    public void showRetryEmpty() {
        adapter.showRetryFull(true);
    }

    @Override
    public void hideRetryEmpty() {
        adapter.showRetryFull(false);
    }

    @Override
    public void selectParent(long categoryId) {
        presenter.fetchCategoryChild(categoryId);
    }

    @Override
    public void selectSetCategory(List<CategoryViewModel> listCategory) {
        listener.selectSetCategory(listCategory);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
