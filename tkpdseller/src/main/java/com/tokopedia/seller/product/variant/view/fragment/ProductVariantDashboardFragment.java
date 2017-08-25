package com.tokopedia.seller.product.variant.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantData;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantStatus;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantPickerActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDashboardAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;
import com.tokopedia.seller.product.variant.view.model.ProductVariantManageViewModel;
import com.tokopedia.seller.topads.dashboard.view.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDashboardFragment extends BaseListFragment<BlankPresenter, ProductVariantManageViewModel> implements ProductVariantMainView {

    private static final int MULTIPLY_START_TEMP_ID = 10000;

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private VariantData variantData;
    private View variantListView;

    public static ProductVariantDashboardFragment newInstance() {
        Bundle args = new Bundle();
        ProductVariantDashboardFragment fragment = new ProductVariantDashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityIntent = getActivity().getIntent();
        productVariantByCatModelList = activityIntent
                .getParcelableArrayListExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST);
        if (savedInstanceState == null) {
            if (activityIntent.hasExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION)) {
                variantData = activityIntent.getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION);
            }
        } else {
            variantData = savedInstanceState.getParcelable(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_variant_main;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        variantLevelOneLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_one);
        variantLevelTwoLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_two);
        variantListView = view.findViewById(R.id.linear_layout_variant_list);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariantLabel();
        updateVariantUnitView();
        updateVariantItemListView();
    }

    private void initVariantLabel() {
        if (productVariantByCatModelList == null) {
            return;
        }
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {
            ProductVariantByCatModel productVariantByCatModel = ProductVariantUtils.getProductVariantByCatModel(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantByCatModelList);
            if (productVariantByCatModel != null) {
                variantLevelOneLabelView.setVisibility(View.VISIBLE);
                variantLevelOneLabelView.setTitle(productVariantByCatModel.getName());
                variantLevelOneLabelView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickVariant(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE);
                    }
                });
            }
        }
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE) {
            ProductVariantByCatModel productVariantByCatModel = ProductVariantUtils.getProductVariantByCatModel(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, productVariantByCatModelList);
            if (productVariantByCatModel != null) {
                variantLevelTwoLabelView.setVisibility(View.VISIBLE);
                variantLevelTwoLabelView.setTitle(productVariantByCatModel.getName());
                variantLevelTwoLabelView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickVariant(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE);
                    }
                });
            }
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<ProductVariantManageViewModel> getNewAdapter() {
        return new ProductVariantDashboardAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ProductVariantManageViewModel productVariantManageViewModel) {
        // TODO start activity with the correct parameter
        ProductVariantByCatModel productVariantByCatModel = ProductVariantUtils.getProductVariantByCatModel(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, productVariantByCatModelList);
        ArrayList<ProductVariantValue> productVariantValueList = new ArrayList<>();
        productVariantValueList.addAll(ProductVariantUtils.getProductVariantValueListForVariantDetail(
                productVariantManageViewModel.getTemporaryId(), variantData.getVariantUnitSubmitList(),
                variantData.getVariantStatusList(), productVariantByCatModel.getUnitList()));
        ProductVariantDetailActivity.start(getContext(), ProductVariantDashboardFragment.this,
                productVariantManageViewModel.getTemporaryId(),
                productVariantManageViewModel.getTitle(),
                true,
                productVariantValueList,
                new ArrayList<Long>());
    }

    private void pickVariant(int level) {
        Intent intent = new Intent(getActivity(), ProductVariantPickerActivity.class);
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY, ProductVariantUtils.getProductVariantByCatModel(level, productVariantByCatModelList));
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT, getVariantUnitSubmit(level));
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_START_TEMP_ID, level * MULTIPLY_START_TEMP_ID);
        startActivityForResult(intent, level);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE:
            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE:
                onActivityResultFromItemPicker(requestCode, data);
                break;
            case ProductVariantDetailActivity.REQUEST_CODE:
                onActivityResultFromDataManage(data);
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onActivityResultFromItemPicker(int requestCode, Intent data) {
        VariantUnitSubmit variantUnitSubmit = data.getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT);
        switch (requestCode) {
            case ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE:
                setVariantLevel(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantUnitSubmit);
                checkVariantValidation(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantUnitSubmit);
                break;
            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE:
                setVariantLevel(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, variantUnitSubmit);
                checkVariantValidation(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, variantUnitSubmit);
                break;
        }
        updateVariantStatusToDefault();
        updateVariantUnitView();
        updateVariantItemListView();
    }

    @SuppressWarnings("unchecked")
    private void onActivityResultFromDataManage(Intent data) {
        if (data.getAction().equals(ProductVariantDetailActivity.EXTRA_ACTION_DELETE)) {
            long variantIdToDelete = data.getLongExtra(ProductVariantDetailActivity.EXTRA_VARIANT_ID, 0);
            if (variantIdToDelete != 0) {
                // TODO delete variant status for variantIdToDelete
                // remove from selected variantIdToDelete
            }
        } else if (data.getAction().equals(ProductVariantDetailActivity.EXTRA_ACTION_SUBMIT)) {
            long variantIdToUpdate = data.getLongExtra(ProductVariantDetailActivity.EXTRA_VARIANT_ID, 0);
            if (variantIdToUpdate != 0) {
                ArrayList<Long> selectedVariantValueIdList = (ArrayList<Long>)
                        data.getSerializableExtra(ProductVariantDetailActivity.EXTRA_VARIANT_VALUE_LIST);
                if (selectedVariantValueIdList == null || selectedVariantValueIdList.size() == 0) {
                    if (data.hasExtra(ProductVariantDetailActivity.EXTRA_VARIANT_HAS_STOCK)) {
                        boolean variantHasStock = data.getBooleanExtra(ProductVariantDetailActivity.EXTRA_VARIANT_HAS_STOCK, false);
                        //TODO set the variant stock to available/empty (based on variantHasStock)
                    }
                } else {
                    // TODO update the selectedVariantValueIdList of the variant to available
                }
            }
        }
    }

    private void setVariantLevel(int level, VariantUnitSubmit variantUnitSubmit) {
        if (variantData == null) {
            variantData = new VariantData();
        }
        if (variantData.getVariantUnitSubmitList() == null) {
            variantData.setVariantUnitSubmitList(new ArrayList<VariantUnitSubmit>());
        }
        int variantUnitSubmitSize = variantData.getVariantUnitSubmitList().size();
        for (int i = 0; i < variantUnitSubmitSize; i++) {
            VariantUnitSubmit variantUnitSubmitTemp = variantData.getVariantUnitSubmitList().get(i);
            if (variantUnitSubmitTemp.getPosition() == level) {
                variantData.getVariantUnitSubmitList().set(i, variantUnitSubmit);
                return;
            }
        }
        // Variant unit not found, add it
        variantData.getVariantUnitSubmitList().add(variantUnitSubmit);
    }

    /**
     * If option list empty, need to remove other variant level
     * for example level 1 empty, level 2, 3, 4 need to be removed
     *
     * @param level
     * @param variantUnitSubmit
     */
    private void checkVariantValidation(int level, VariantUnitSubmit variantUnitSubmit) {
        if (variantUnitSubmit.getVariantSubmitOptionList() != null && variantUnitSubmit.getVariantSubmitOptionList().size() > 0) {
            return;
        }
        int variantUnitSubmitSize = variantData.getVariantUnitSubmitList().size();
        for (int i = 0; i < variantUnitSubmitSize; i++) {
            VariantUnitSubmit variantUnitSubmitTemp = variantData.getVariantUnitSubmitList().get(i);
            if (variantUnitSubmitTemp.getPosition() > level) {
                variantData.getVariantUnitSubmitList().remove(i);
            }
        }
    }

    private void updateVariantStatusToDefault() {
        if (variantData == null) {
            variantData = new VariantData();
        }
        if (variantData.getVariantStatusList() == null) {
            variantData.setVariantStatusList(new ArrayList<VariantStatus>());
        }
        List<VariantStatus> variantStatusList = new ArrayList<>();
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE) {
            variantStatusList = ProductVariantUtils.getVariantStatusList(
                    getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE), getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE));
        } else if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {
            variantStatusList = ProductVariantUtils.getVariantStatusList(
                    getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE));
        }
        variantData.setVariantStatusList(variantStatusList);
    }

    /**
     * Update variant item list view
     */
    private void updateVariantItemListView() {
        if (variantData == null || variantData.getVariantUnitSubmitList() == null
                || variantData.getVariantUnitSubmitList().size() == 0) {
            variantListView.setVisibility(View.GONE);
            return;
        }
        List<ProductVariantManageViewModel> productVariantManageViewModelList = new ArrayList<>();
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE) {
            productVariantManageViewModelList = ProductVariantUtils.getProductVariantManageViewModelListTwoLevel(
                    variantData.getVariantUnitSubmitList(), variantData.getVariantStatusList(), productVariantByCatModelList);
        } else if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {

        }
        variantListView.setVisibility(View.VISIBLE);
        onSearchLoaded(productVariantManageViewModelList, productVariantManageViewModelList.size());
    }

    private void updateVariantUnitView() {
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {
            updateVariantUnitView(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE);
        }
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE) {
            updateVariantUnitView(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE);
        }
    }

    private void updateVariantUnitView(int level) {
        switch (level) {
            case ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE:
                variantLevelOneLabelView.setContent(getVariantTitle(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE));
                break;
            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE:
                if (TextUtils.isEmpty(variantLevelOneLabelView.getContent()) ||
                        variantLevelOneLabelView.isContentDefault()) {
                    variantLevelTwoLabelView.setEnabled(false);
                    variantLevelTwoLabelView.resetContentText();
                } else {
                    variantLevelTwoLabelView.setEnabled(true);
                    variantLevelTwoLabelView.setContent(getVariantTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE));
                }
                break;
        }
    }

    /**
     * @param level ex 1
     * @return selected name String for that variant, ex: "hijau, merah, biru"
     */
    private String getVariantTitle(int level) {
        VariantUnitSubmit variantUnitSubmit = getVariantUnitSubmit(level);
        String title = ProductVariantUtils.getMultipleVariantOptionTitle(level, variantUnitSubmit, productVariantByCatModelList);
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.product_label_choose);
        }
        return title;
    }

    private VariantUnitSubmit getVariantUnitSubmit(int level) {
        if (variantData == null) {
            return null;
        }
        return ProductVariantUtils.getVariantUnitSubmit(level, variantData.getVariantUnitSubmitList());
    }

    /**
     * function to return the result to the caller (activity)
     *
     * @return
     */
    public VariantData getVariantData() {
        return variantData;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION, variantData);
    }
}