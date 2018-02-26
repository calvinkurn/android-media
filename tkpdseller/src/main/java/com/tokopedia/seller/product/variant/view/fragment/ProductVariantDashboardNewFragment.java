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
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantViewConverter;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardNewActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailLevel1Activity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantPickerNewActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDashboardAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardViewModel;

import java.util.ArrayList;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDashboardNewFragment extends BaseListFragment<BlankPresenter, ProductVariantDashboardViewModel> implements ProductVariantMainView {

    //TODO, do not use this
    private static final int MULTIPLY_START_TEMP_ID = 10000;

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private ProductVariantViewModel productVariantViewModel;
    private RecyclerView recyclerView;

    public static ProductVariantDashboardNewFragment newInstance() {
        Bundle args = new Bundle();
        ProductVariantDashboardNewFragment fragment = new ProductVariantDashboardNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityIntent = getActivity().getIntent();
        productVariantByCatModelList = activityIntent.getParcelableArrayListExtra(ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST);
        if (savedInstanceState == null) {
            if (activityIntent.hasExtra(ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_SELECTION)) {
                productVariantViewModel = activityIntent.getParcelableExtra(ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_SELECTION);
            }
        } else {
            productVariantViewModel = savedInstanceState.getParcelable(ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_SELECTION);
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
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariantLabel();
        updateVariantUnitView();
        updateVariantItemListView();
    }

    private void initVariantLabel() {
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            return;
        }
        // it is already sorted.
        // index 0 is always level 1
        // index 1 is always level 2

        // if variant level 1 exists
        if (productVariantByCatModelList.size() >= 1 && productVariantByCatModelList.get(0) != null) {
            variantLevelOneLabelView.setVisibility(View.VISIBLE);
            variantLevelOneLabelView.setTitle(productVariantByCatModelList.get(0).getName());
            variantLevelOneLabelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickVariant(1);
                }
            });
        } else {
            variantLevelOneLabelView.setVisibility(View.GONE);
        }

        // if variant level 2 exists
        if (productVariantByCatModelList.size() >= 2 && productVariantByCatModelList.get(1) != null) {
            variantLevelTwoLabelView.setVisibility(View.VISIBLE);
            variantLevelTwoLabelView.setTitle(productVariantByCatModelList.get(1).getName());
            variantLevelTwoLabelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickVariant(2);
                }
            });
        } else {
            variantLevelTwoLabelView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<ProductVariantDashboardViewModel> getNewAdapter() {
        return new ProductVariantDashboardAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ProductVariantDashboardViewModel productVariantDashboardViewModel) {
//        ProductVariantByCatModel productVariantByCatModel = ProductVariantViewConverter.getProductVariantByCatModel(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, productVariantByCatModelList);
//        ArrayList<ProductVariantDetailViewModel> productVariantDetailViewModelList = new ArrayList<>();
//        if (productVariantByCatModel != null) {
//            productVariantDetailViewModelList = new ArrayList<>(ProductVariantViewConverter.getProductVariantValueListForVariantDetail(
//                    ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE,
//                    productVariantDataSubmit.getProductVariantUnitSubmitList(),
//                    productVariantByCatModel.getUnitList()));
//        }
//        ProductVariantDetailActivity.start(getContext(), ProductVariantDashboardNewFragment.this,
//                productVariantDashboardViewModel.getTemporaryId(),
//                productVariantDashboardViewModel.getTitle(),
//                ProductVariantViewConverter.isContainVariantStatusByOptionId(productVariantDashboardViewModel.getTemporaryId(), productVariantDataSubmit.getProductVariantCombinationSubmitList()),
//                productVariantDetailViewModelList,
//                new ArrayList<>(ProductVariantUtils.getSelectedOptionIdList(productVariantDashboardViewModel.getTemporaryId(), productVariantDataSubmit.getProductVariantCombinationSubmitList())));
    }

    private void pickVariant(int level) {
        Intent intent = new Intent(getActivity(), ProductVariantPickerNewActivity.class);
        int index = level - 1;
        intent.putExtra(ProductVariantPickerNewActivity.EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL,
                productVariantByCatModelList.get(index));
        intent.putExtra(ProductVariantPickerNewActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL,
                productVariantViewModel == null? null : productVariantViewModel.getVariantOptionParent(index));
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
            case ProductVariantDetailLevel1Activity.VARIANT_EDIT_LEVEL1_REQUEST_CODE:
                onActivityResultFromDetail(data);
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onActivityResultFromItemPicker(int requestCode, Intent data) {
        //TODO get variant from item picker

//        ProductVariantUnitSubmit productVariantUnitSubmit = data.getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT);
//        int level = ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE;
//        switch (requestCode) {
//            case ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE:
//                level = ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE;
//                break;
//            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE:
//                level = ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE;
//                break;
//        }
//
//        addVariantCombination(level, productVariantUnitSubmit);
//        updateVariantUnitList(productVariantUnitSubmit);
//        checkVariantValidation(level, productVariantUnitSubmit);

        //TODO remap image need?
        remapImageToVariant();
//TODO
//        updateVariantUnitView();
//        updateVariantItemListView();
    }

    /**
     * this is to give the variant the imageURL, currenly imageURL can be uploaded from desktop (retrieve from server)
     * Change this logic when we already have the logic to upload variant image
     */
    private void remapImageToVariant() {
        //TODO image variant, need or not?
//        if (this.productVariantDataSubmit == null || this.productVariantDataSubmit.getProductVariantUnitSubmitList() == null ||
//                this.productVariantDataSubmit.getProductVariantUnitSubmitList().size() == 0 ||
//                oldProductVariantOptionSubmitListLv1 == null || oldProductVariantOptionSubmitListLv1.size() == 0) {
//            return;
//        }
//        List<ProductVariantUnitSubmit> productVariantUnitSubmitList = this.productVariantDataSubmit.getProductVariantUnitSubmitList();
//        for (int i = 0, sizei = productVariantUnitSubmitList.size(); i < sizei; i++) {
//            ProductVariantUnitSubmit productVariantUnitSubmit = productVariantUnitSubmitList.get(i);
//            if (productVariantUnitSubmit.getPosition() == ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {
//                List<ProductVariantOptionSubmit> productVariantOptionSubmitList = productVariantUnitSubmit.getProductVariantOptionSubmitList();
//                for (int j = 0, sizej = productVariantOptionSubmitList.size(); j < sizej; j++) {
//                    ProductVariantOptionSubmit productVariantOptionSubmit =
//                            productVariantOptionSubmitList.get(j);
//                    productVariantOptionSubmit.setPictureItemList(getOldPictureItem(productVariantOptionSubmit));
//                }
//                break;
//            }
//        }
    }

//    private List<PictureItem> getOldPictureItem(ProductVariantOptionSubmit productVariantOptionSubmit) {
//        long vuvId = productVariantOptionSubmit.getVariantUnitValueId();
//        String customText = productVariantOptionSubmit.getCustomText();
//        if (vuvId == 0 && TextUtils.isEmpty(customText)) {
//            return null;
//        }
//        if (vuvId != 0) {
//            for (int i = 0, sizei = oldProductVariantOptionSubmitListLv1.size(); i < sizei; i++) {
//                ProductVariantOptionSubmit oldProductVariantOptionSubmit = oldProductVariantOptionSubmitListLv1.get(i);
//                if (vuvId == oldProductVariantOptionSubmit.getVariantUnitValueId()) {
//                    return oldProductVariantOptionSubmit.getPictureItemList();
//                }
//            }
//        } else {
//            for (int i = 0, sizei = oldProductVariantOptionSubmitListLv1.size(); i < sizei; i++) {
//                ProductVariantOptionSubmit oldProductVariantOptionSubmit = oldProductVariantOptionSubmitListLv1.get(i);
//                if (customText.equals(oldProductVariantOptionSubmit.getCustomText())) {
//                    return oldProductVariantOptionSubmit.getPictureItemList();
//                }
//            }
//        }
//        return null;
//    }

    @SuppressWarnings("unchecked")
    private void onActivityResultFromDetail(Intent data) {
        //TODO level 1 can delete?
//        if (data.getAction().equals(ProductVariantDetailActivity.EXTRA_ACTION_DELETE)) {
//            onActivityResultFromDetailDeleteOption(data);
//        } else
        if (ProductVariantDetailLevel1Activity.EXTRA_ACTION_SUBMIT.equals(data.getAction())) {
            onActivityResultFromDetailUpdateList((ProductVariantViewModel)
                    data.getParcelableExtra(ProductVariantDetailLevel1Activity.EXTRA_PRODUCT_VARIANT_DATA));
        }
    }

    private void onActivityResultFromDetailDeleteOption(Intent data) {
//        long optionIdToDelete = data.getLongExtra(ProductVariantDetailActivity.EXTRA_VARIANT_OPTION_ID, ProductVariantConstant.NOT_AVAILABLE_OPTION_ID);
//        if (optionIdToDelete == ProductVariantConstant.NOT_AVAILABLE_OPTION_ID) {
//            return;
//        }
//        productVariantDataSubmit = ProductVariantUtils.getRemovedVariantDataByOptionId(optionIdToDelete, productVariantDataSubmit);
//        updateVariantUnitView();
//        updateVariantItemListView();
    }

    private void onActivityResultFromDetailUpdateList(ProductVariantViewModel productVariantViewModel) {
        this.productVariantViewModel = productVariantViewModel;
        //TODO reset UI
//        long optionIdToUpdated = intent.getLongExtra(ProductVariantDetailActivity.EXTRA_VARIANT_OPTION_ID, ProductVariantConstant.NOT_AVAILABLE_OPTION_ID);
//        if (optionIdToUpdated == ProductVariantConstant.NOT_AVAILABLE_OPTION_ID) {
//            return;
//        }
//        boolean variantHasStock = intent.getBooleanExtra(ProductVariantDetailActivity.EXTRA_VARIANT_HAS_STOCK, false);
//        ArrayList<Long> selectedVariantValueIdList =
//                (ArrayList<Long>) intent.getSerializableExtra(ProductVariantDetailActivity.EXTRA_VARIANT_VALUE_LIST);
//        if (selectedVariantValueIdList == null) {
//            selectedVariantValueIdList = new ArrayList<>();
//        }
//        List<ProductVariantCombinationSubmit> variantCombinationList;
//        if (selectedVariantValueIdList.size() > 0) {
//            variantCombinationList = ProductVariantUtils.getUpdatedVariantCombinationList(
//                    optionIdToUpdated, selectedVariantValueIdList, productVariantDataSubmit.getProductVariantCombinationSubmitList());
//        } else {
//            variantCombinationList = ProductVariantUtils.getUpdatedVariantCombinationList(
//                    optionIdToUpdated, variantHasStock, productVariantDataSubmit.getProductVariantCombinationSubmitList());
//        }
//        productVariantDataSubmit.setProductVariantCombinationSubmitList(variantCombinationList);
//        updateVariantUnitView();
//        updateVariantItemListView();
    }

    private void updateVariantUnitList(ProductVariantUnitSubmit productVariantUnitSubmit) {
//        List<ProductVariantUnitSubmit> variantUnitSubmitList = productVariantDataSubmit.getProductVariantUnitSubmitList();
//        // Update variant unit list position
//        variantUnitSubmitList = ProductVariantUtils.getUpdatedVariantUnitListPosition(variantUnitSubmitList, productVariantUnitSubmit);
//        // Validate variant unit list
//        variantUnitSubmitList = ProductVariantUtils.getValidatedVariantUnitList(variantUnitSubmitList);
//
//        productVariantDataSubmit.setProductVariantUnitSubmitList(variantUnitSubmitList);
    }

    /**
     * If option list empty, need to remove other variant level
     * for example level 1 empty, level 2, 3, 4 need to be removed
     *
     * @param level
     * @param productVariantUnitSubmit
     */
    private void checkVariantValidation(int level, ProductVariantUnitSubmit productVariantUnitSubmit) {
//        if (productVariantUnitSubmit.getProductVariantOptionSubmitList() != null &&
//                productVariantUnitSubmit.getProductVariantOptionSubmitList().size() > 0) {
//            return;
//        }
//        int variantUnitSubmitSize = productVariantDataSubmit.getProductVariantUnitSubmitList().size();
//        for (int i = variantUnitSubmitSize - 1; i >= 0; i--) {
//            ProductVariantUnitSubmit productVariantUnitSubmitTemp = productVariantDataSubmit.getProductVariantUnitSubmitList().get(i);
//            if (productVariantUnitSubmitTemp.getPosition() > level) {
//                productVariantDataSubmit.getProductVariantUnitSubmitList().remove(i);
//            }
//        }
    }

    private void addVariantCombination(int level, ProductVariantUnitSubmit productVariantUnitSubmit) {
//        ProductVariantUnitSubmit otherVariantUnitSubmit = null;
//        List<ProductVariantOptionSubmit> otherVariantUnitSubmitList = new ArrayList<>();
//        ProductVariantUnitSubmit oldVariantUnitSubmit = null;
//        List<ProductVariantOptionSubmit> oldProductVariantOptionSubmitList = new ArrayList<>();
//        List<ProductVariantCombinationSubmit> variantCombinationSubmitList = new ArrayList<>();
//        switch (level) {
//            case ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE:
//                otherVariantUnitSubmit = getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE);
//                break;
//            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE:
//                otherVariantUnitSubmit = getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE);
//                break;
//        }
//        if (otherVariantUnitSubmit != null) {
//            otherVariantUnitSubmitList = otherVariantUnitSubmit.getProductVariantOptionSubmitList();
//        }
//        oldVariantUnitSubmit = getVariantUnitSubmit(level);
//        if (oldVariantUnitSubmit != null) {
//            oldProductVariantOptionSubmitList = oldVariantUnitSubmit.getProductVariantOptionSubmitList();
//        }
//        if (productVariantDataSubmit != null) {
//            variantCombinationSubmitList = productVariantDataSubmit.getProductVariantCombinationSubmitList();
//        }
//        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = ProductVariantUtils.getAddedVariantCombinationList(
//                oldProductVariantOptionSubmitList, productVariantUnitSubmit.getProductVariantOptionSubmitList(),
//                variantCombinationSubmitList, otherVariantUnitSubmitList);
//        productVariantDataSubmit.setProductVariantCombinationSubmitList(productVariantCombinationSubmitList);
    }

    /**
     * Update variant item list view
     */
    private void updateVariantItemListView() {
        if (productVariantViewModel == null || !productVariantViewModel.hasSelectedVariant()) {
            recyclerView.setVisibility(View.GONE);
            return;
        }
//        List<ProductVariantDashboardViewModel> variantManageViewModelList = ProductVariantViewConverter.getGeneratedVariantDashboardViewModelList(
//                productVariantDataSubmit.getProductVariantUnitSubmitList(),
//                productVariantDataSubmit.getProductVariantCombinationSubmitList(),
//                productVariantByCatModelList);
        recyclerView.setVisibility(View.VISIBLE);
//        onSearchLoaded(variantManageViewModelList, variantManageViewModelList.size());
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
        String title = "";
        ProductVariantUnitSubmit productVariantUnitSubmit = getVariantUnitSubmit(level);
        if (productVariantUnitSubmit != null) {
            title = ProductVariantViewConverter.getMultipleVariantOptionTitle(level, productVariantUnitSubmit.getProductVariantOptionSubmitList(), productVariantByCatModelList);
        }
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.product_label_choose);
        }
        return title;
    }

    private ProductVariantUnitSubmit getVariantUnitSubmit(int level) {
        return null;
//        if (productVariantDataSubmit == null) {
//            return null;
//        }
//        return ProductVariantViewConverter.getVariantUnitSubmitByLevel(level, productVariantDataSubmit.getProductVariantUnitSubmitList());
    }

    /**
     * function to return the result to the caller (activity)
     *
     * @return
     */
    public ProductVariantViewModel getProductVariantViewModel() {
        return productVariantViewModel;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
    }
}