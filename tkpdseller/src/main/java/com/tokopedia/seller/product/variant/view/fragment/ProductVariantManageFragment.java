package com.tokopedia.seller.product.variant.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantData;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantStatus;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDataManageActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantPickerActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantManageListAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;
import com.tokopedia.seller.product.variant.view.model.ProductVariantManageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantManageFragment extends BaseListFragment<BlankPresenter, ProductVariantManageViewModel> implements ProductVariantMainView {

    private static final int MULTIPLY_START_TEMP_ID = 10000;

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private VariantData variantData;

    public static ProductVariantManageFragment newInstance() {
        Bundle args = new Bundle();
        ProductVariantManageFragment fragment = new ProductVariantManageFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_main, container, false);
        variantLevelOneLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_one);
        variantLevelTwoLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_two);
        initVariantLabel();
        updateVariantUnitView();
        return view;
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
        return new ProductVariantManageListAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ProductVariantManageViewModel productVariantManageViewModel) {
        // TODO start activity with the correct parameter
        //ProductVariantDataManageActivity.start(getContext(),ProductVariantManageFragment.this,
        //        10, //
        //        "Warna", // test,
        //        new ArrayList<ProductVariantValue>(), // test
        //        new ArrayList<Long>() //test
        //        );
        Intent intent = new Intent(getContext(), ProductVariantDataManageActivity.class);
        getContext().startActivity(intent);
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
            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE: {
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
                    default:
                        super.onActivityResult(requestCode, resultCode, data);
                }
                updateVariantStatusToDefault();
                updateVariantItemList();
                updateVariantUnitView();
            }
            break;
            case ProductVariantDataManageActivity.REQUEST_CODE: {
                if (data.getAction().equals(ProductVariantDataManageActivity.EXTRA_ACTION_DELETE)) {
                    long variantIdToDelete = data.getLongExtra(ProductVariantDataManageActivity.EXTRA_VARIANT_ID, 0);
                    if (variantIdToDelete != 0) {
                        // TODO delete variant status for variantIdToDelete
                        // remove from selected variantIdToDelete
                    }
                } else if (data.getAction().equals(ProductVariantDataManageActivity.EXTRA_ACTION_DELETE)) {
                    long variantIdToUpdate = data.getLongExtra(ProductVariantDataManageActivity.EXTRA_VARIANT_ID, 0);
                    if (variantIdToUpdate != 0) {
                        ArrayList<Long> selectedVariantValueIdList = (ArrayList<Long>)
                                data.getSerializableExtra(ProductVariantDataManageActivity.EXTRA_VARIANT_VALUE_LIST);
                        // TODO select the variantID with selected value: selectedVariantValueIdList
                    }
                }
            }
            break;
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

    private void updateVariantItemList() {
        List<ProductVariantManageViewModel> productVariantManageViewModelList = new ArrayList<>();
        if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE) {
            productVariantManageViewModelList = ProductVariantUtils.getProductVariantManageViewModelListTwoLevel(
                    variantData.getVariantUnitSubmitList(), variantData.getVariantStatusList(), productVariantByCatModelList);
        } else if (productVariantByCatModelList.size() >= ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {

        }
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
                        variantLevelOneLabelView.getContent().equalsIgnoreCase(getString(R.string.product_label_choose))) {
                    variantLevelTwoLabelView.setEnabled(false);
                    variantLevelTwoLabelView.setContent(getString(R.string.product_label_choose));
                } else {
                    variantLevelTwoLabelView.setEnabled(true);
                    variantLevelTwoLabelView.setContent(getVariantTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE));
                }
                break;
        }
    }

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