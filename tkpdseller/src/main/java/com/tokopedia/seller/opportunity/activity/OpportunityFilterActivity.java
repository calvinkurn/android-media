package com.tokopedia.seller.opportunity.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterTitleAdapter;
import com.tokopedia.seller.opportunity.fragment.OpportunityFilterFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityFilterTitleFragment;
import com.tokopedia.seller.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OptionViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterActivity extends BasePresenterActivity
        implements OpportunityFilterTitleAdapter.FilterListener,
        OpportunityFilterAdapter.CategoryListener {

    public interface FilterTitleListener {
        void updateData(ArrayList<FilterViewModel> viewModel);
    }

    public interface FilterListener {
        void updateData(FilterViewModel viewModel);
    }

    private static final String ARGS_DATA = "OpportunityFilterActivity_ARGS_DATA";
    private static final String PARAM_FILTER_VIEW_MODEL = "PARAM_FILTER_VIEW_MODEL";
    public static final String PARAM_SELECTED_SHIPPING_TYPE = "PARAM_SELECTED_SHIPPING_TYPE";
    public static final String PARAM_SELECTED_CATEGORY = "PARAM_SELECTED_CATEGORY";
    public static final String PARAM_SHIPPING_LIST = "PARAM_SHIPPING_LIST";
    public static final String PARAM_CATEGORY_LIST = "PARAM_CATEGORY_LIST";


    View saveButton;
    View resetButton;
    private List<Fragment> listFragment;
    private OpportunityFilterViewModel viewModel;
    private String selectedShipping;
    private String selectedCategory;

    public static Intent createIntent(Context context, OpportunityFilterViewModel data) {
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
            viewModel = new OpportunityFilterViewModel();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
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

        OpportunityFilterTitleFragment fragment =
                OpportunityFilterTitleFragment.createInstance(viewModel.getListFilter());
        fragmentTransaction.replace(R.id.filter, fragment);

        listFragment = new ArrayList<>();
        for (FilterViewModel filterModel : viewModel.getListFilter()) {
            listFragment.add(OpportunityFilterFragment.createInstance(filterModel));
        }
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
//                resetFilter(viewModel.getListCategory());
//                resetShipping(viewModel.getListShipping());
//                resetTitle(viewModel.getListTitle());
                updateFilterTitleFragment();
            }
        };
    }

//    private void resetTitle(ArrayList<FilterItemViewModel> listTitle) {
//        for (int i = 0; i < listTitle.size(); i++) {
//            listTitle.get(i).setActive(false);
//            if (listTitle.get(i).isSelected())
//                onTitleClicked(i);
//        }
//    }
//
//    private void resetShipping(List<ShippingTypeViewModel> listShipping) {
//        for (ShippingTypeViewModel shipping : listShipping) {
//            shipping.setSelected(false);
//        }
//    }
//
//    private void resetFilter(List<FilterViewModel> listCategory) {
//        for (FilterViewModel category : listCategory) {
//            if (category.getListChild().size() > 0)
//                resetFilter(category.getListChild());
//            else
//                category.setSelected(false);
//        }
//    }

    private View.OnClickListener onSaveClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();


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
    public void onTitleClicked(int pos) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (listFragment.get(pos) instanceof FilterListener)
            ((FilterListener) listFragment.get(pos)).updateData(viewModel.getListFilter().get(pos));
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

    private void updateFilterTitleFragment() {
        ((OpportunityFilterTitleFragment) getFragmentManager().findFragmentById(R.id.filter))
                .updateData(viewModel.getListFilter());
    }

    @Override
    public void onFilterSelected(int position, FilterViewModel filterViewModel) {
        CommonUtils.dumper("NISNIS isSelected " + filterViewModel.getListChild().size()
                + " " + filterViewModel.isSelected());

//        selectedCategory = "";
//        viewModel.getListFilter(listCategory);
//        for (FilterViewModel model : viewModel.getListFilter()) {
//            if (model.isSelected()) {
//                selectedCategory = String.valueOf(listCategory.get(position).getCategoryId());
//                break;
//            }
//        }
//
//        viewModel.getListTitle().get(0).setActive(!selectedCategory.equals(""));
        updateFilterTitleFragment();

    }

    @Override
    public void onFilterExpanded(int position, FilterViewModel filterViewModel) {
//        viewModel.getListFilter().set(position, filterViewModel);
        CommonUtils.dumper("NISNIS isExpanded" + filterViewModel.getListChild().size());

    }


}