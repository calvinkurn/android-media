package com.tokopedia.seller.opportunity.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.OpportunityCategoryAdapter;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.adapter.OpportunityShippingAdapter;
import com.tokopedia.seller.opportunity.fragment.OpportunityCategoryFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityFilterFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityShippingFragment;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.FilterItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterActivityViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterActivity extends BasePresenterActivity
        implements OpportunityFilterAdapter.FilterListener, OpportunityShippingAdapter.ShippingListener, OpportunityCategoryAdapter.CategoryListener {


    public interface FilterListener {
        void updateData(OpportunityFilterActivityViewModel viewModel);
    }

    private static final String ARGS_DATA = "OpportunityFilterActivity_ARGS_DATA";
    private static final String PARAM_FILTER_VIEW_MODEL = "PARAM_FILTER_VIEW_MODEL";
    public static final String PARAM_SELECTED_SHIPPING_TYPE = "PARAM_SELECTED_SHIPPING_TYPE";
    public static final String PARAM_SELECTED_CATEGORY = "PARAM_SELECTED_CATEGORY";

    View saveButton;
    View resetButton;
    private List<Fragment> listFragment;
    private OpportunityFilterActivityViewModel viewModel;
    private String selectedShipping;
    private String selectedCategory;

    public static Intent createIntent(Context context, OpportunityFilterActivityViewModel data) {
        Intent intent = new Intent(context, OpportunityFilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_FILTER_VIEW_MODEL, data);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getParcelable(ARGS_DATA) != null) {
            viewModel = savedInstanceState.getParcelable(ARGS_DATA);
            selectedShipping = savedInstanceState.getString(PARAM_SELECTED_SHIPPING_TYPE, "");
            selectedCategory = savedInstanceState.getString(PARAM_SELECTED_CATEGORY, "");
        } else if (getIntent().getExtras() != null &&
                getIntent().getExtras().getParcelable(PARAM_FILTER_VIEW_MODEL) != null) {
            viewModel = getIntent().getExtras().getParcelable(PARAM_FILTER_VIEW_MODEL);
        } else {
            viewModel = new OpportunityFilterActivityViewModel();
        }
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opportunity_filter;

    }

    @Override
    protected void initView() {
        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        OpportunityFilterFragment fragment = OpportunityFilterFragment.createInstance(viewModel.getListTitle());
        fragmentTransaction.replace(R.id.filter, fragment);

        listFragment = new ArrayList<>();
        listFragment.add(OpportunityCategoryFragment.createInstance(viewModel.getListCategory()));
        listFragment.add(OpportunityShippingFragment.createInstance(viewModel.getListShipping()));
        fragmentTransaction.replace(R.id.container, listFragment.get(0));

        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {
        saveButton.setOnClickListener(onSaveClicked());
        resetButton.setOnClickListener(onResetClicked());
    }

    private View.OnClickListener onResetClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCategory(viewModel.getListCategory());
                resetShipping(viewModel.getListShipping());
                resetTitle(viewModel.getListTitle());
                updateFilterTitleFragment();
            }
        };
    }

    private void resetTitle(ArrayList<FilterItemViewModel> listTitle) {
        for (int i = 0; i < listTitle.size(); i++) {
            listTitle.get(i).setActive(false);
            if (listTitle.get(i).isSelected())
                onFilterClicked(i);
        }
    }

    private void resetShipping(List<ShippingTypeViewModel> listShipping) {
        for (ShippingTypeViewModel shipping : listShipping) {
            shipping.setSelected(false);
        }
    }

    private void resetCategory(List<CategoryViewModel> listCategory) {
        for (CategoryViewModel category : listCategory) {
            if (category.getListChild().size() > 0)
                resetCategory(category.getListChild());
            else
                category.setSelected(false);
        }
    }

    private View.OnClickListener onSaveClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PARAM_SELECTED_SHIPPING_TYPE, selectedShipping);
                intent.putExtra(PARAM_SELECTED_CATEGORY, selectedCategory);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        };
    }

    @Override
    protected void initVar() {
        selectedShipping = "";
        selectedCategory = "";
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onFilterClicked(int pos) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (listFragment.get(pos) instanceof FilterListener)
            ((FilterListener) listFragment.get(pos)).updateData(viewModel);
        fragmentTransaction.replace(R.id.container, listFragment.get(pos));
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARGS_DATA, viewModel);
        outState.putString(PARAM_SELECTED_SHIPPING_TYPE, selectedShipping);
        outState.putString(PARAM_SELECTED_CATEGORY, selectedCategory);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onShippingSelected(ShippingTypeViewModel shippingTypeViewModel) {
        selectedShipping = "";
        viewModel.getListShipping().get(shippingTypeViewModel.getPosition())
                .setSelected(shippingTypeViewModel.isSelected());
        if (shippingTypeViewModel.isSelected())
            selectedShipping = String.valueOf(shippingTypeViewModel.getShippingTypeId());

        viewModel.getListTitle().get(1).setActive(!selectedShipping.equals(""));
        updateFilterTitleFragment();
    }

    private void updateFilterTitleFragment() {
        ((OpportunityFilterFragment) getFragmentManager().findFragmentById(R.id.filter))
                .updateData(viewModel);
    }


    @Override
    public void onCategorySelected(int position, ArrayList<CategoryViewModel> listCategory) {
        selectedCategory = "";
        viewModel.setListCategory(listCategory);
        for (CategoryViewModel model : viewModel.getListCategory()) {
            if (model.isSelected()) {
                selectedCategory = String.valueOf(listCategory.get(position).getCategoryId());
                break;
            }
        }

        viewModel.getListTitle().get(0).setActive(!selectedCategory.equals(""));
        updateFilterTitleFragment();

    }

    @Override
    public void onCategoryExpanded(int position, ArrayList<CategoryViewModel> listCategory) {
        viewModel.setListCategory(listCategory);
    }
}