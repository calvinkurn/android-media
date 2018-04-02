package com.tokopedia.seller.product.variant.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.utils.ProductPriceRangeUtils;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailLevel1ListActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailLevelLeafActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantPickerActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDashboardNewAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity.EXTRA_HAS_ORIGINAL_VARIANT_LV1;
import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity.EXTRA_HAS_ORIGINAL_VARIANT_LV2;
import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity.EXTRA_PRODUCT_SIZECHART;
import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity.EXTRA_PRODUCT_VARIANT_SELECTION;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDashboardFragment extends BaseImageFragment
        implements ProductVariantMainView, ProductVariantDashboardNewAdapter.OnProductVariantDashboardNewAdapterListener,
        BaseListAdapter.Callback<ProductVariantDashboardViewModel> {

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private ProductVariantViewModel productVariantViewModel;
    private RecyclerView recyclerView;
    private List<ProductVariantDashboardViewModel> productVariantDashboardViewModelList;
    private ProductPictureViewModel productSizeChart;

    private HashMap<Pair<String, String>, Integer> mapCombination;
    private Parcelable recyclerViewState;

    private @CurrencyTypeDef
    int currencyType;
    private double defaultPrice;

    @StockTypeDef
    private int defaultStockType;
    private boolean isOfficialStore;
    private boolean needRetainImage;
    private String defaultSku;

    private int indexOptionParentSizeChart = -1;

    private ProductVariantDashboardNewAdapter productVariantDashboardNewAdapter;
    private View vgSizechart;
    private ImageView ivSizeChart;

    private OnProductVariantDashboardFragmentListener listener;

    public interface OnProductVariantDashboardFragmentListener {
        void onProductVariantSaved(ProductVariantViewModel productVariantViewModel,
                                   ProductPictureViewModel productPictureViewModel);
        void onVariantChangedFromResult();
    }

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

        productVariantByCatModelList = activityIntent.getParcelableArrayListExtra(ProductVariantDashboardActivity.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST);
        currencyType = activityIntent.getIntExtra(ProductVariantDashboardActivity.EXTRA_CURRENCY_TYPE, CurrencyTypeDef.TYPE_IDR);
        defaultPrice = activityIntent.getDoubleExtra(ProductVariantDashboardActivity.EXTRA_DEFAULT_PRICE, 0);
        defaultStockType = activityIntent.getIntExtra(ProductVariantDashboardActivity.EXTRA_STOCK_TYPE, 0);
        isOfficialStore = activityIntent.getBooleanExtra(ProductVariantDashboardActivity.EXTRA_IS_OFFICIAL_STORE, false);
        needRetainImage = activityIntent.getBooleanExtra(ProductVariantDashboardActivity.EXTRA_NEED_RETAIN_IMAGE, false);
        defaultSku = activityIntent.getStringExtra(ProductVariantDashboardActivity.EXTRA_DEFAULT_SKU);

        if (savedInstanceState == null) {
            productVariantViewModel = activityIntent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_SELECTION);
            productSizeChart = activityIntent.getParcelableExtra(EXTRA_PRODUCT_SIZECHART);
        } else {
            productVariantViewModel = savedInstanceState.getParcelable(ProductVariantDashboardActivity.EXTRA_PRODUCT_VARIANT_SELECTION);
            productSizeChart = savedInstanceState.getParcelable(EXTRA_PRODUCT_SIZECHART);
        }
        if (productVariantByCatModelList != null) {
            for (int i = 0, sizei = productVariantByCatModelList.size(); i < sizei; i++) {
                if (productVariantByCatModelList.get(i).isSizeIdentifier()) {
                    indexOptionParentSizeChart = i;
                    break;
                }
            }
        }

        productVariantDashboardNewAdapter = new ProductVariantDashboardNewAdapter(currencyType, this);
        productVariantDashboardNewAdapter.setCallback(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_main, container, false);
        variantLevelOneLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_one);
        variantLevelTwoLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_two);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productVariantDashboardNewAdapter);

        View buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateVariantPrice()) {
                    return;
                }
                listener.onProductVariantSaved(productVariantViewModel, getInputtedSizeChart());
            }
        });

        setupSizeChart(view);
        return view;
    }

    public ProductVariantViewModel getProductVariantViewModel() {
        return productVariantViewModel;
    }

    private boolean validateVariantPrice() {
        if (productVariantViewModel == null) {
            return true;
        }
        if (!productVariantViewModel.hasSelectedVariant()) {
            return true;
        }
        List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList =
                productVariantViewModel.getProductVariant();

        for (int i = 0, sizei = productVariantCombinationViewModelList.size(); i < sizei; i++) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel = productVariantCombinationViewModelList.get(i);
            double priceVar = productVariantCombinationViewModel.getPriceVar();
            if (priceVar == 0) {
                NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                        getString(R.string.product_variant_price_must_be_filled));
                return false;
            } else if (!ProductPriceRangeUtils.isPriceValid(priceVar, currencyType, isOfficialStore)) {
                NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                        getString(R.string.product_error_product_price_not_valid,
                                ProductPriceRangeUtils.getMinPriceString(currencyType, isOfficialStore),
                                ProductPriceRangeUtils.getMaxPriceString(currencyType, isOfficialStore)));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshAllItem();
    }

    private void setupSizeChart(View rootView) {
        vgSizechart = rootView.findViewById(R.id.vg_sizechart);
        ivSizeChart = vgSizechart.findViewById(R.id.image_view_sizechart);
        refreshImageView();
        ivSizeChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productSizeChart == null || TextUtils.isEmpty(productSizeChart.getUriOrPath())) {
                    showAddImageDialog();
                } else {
                    showEditImageDialog(productSizeChart.getUriOrPath());
                }
            }
        });
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

            //set the label text
            setLabelVariantLevel1();
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

            //set the label text
            setLabelVariantLevel2();
        } else {
            variantLevelTwoLabelView.setVisibility(View.GONE);
        }

    }

    private void initSizeChart() {
        if (isCatalogHasProductSizeChart() && productVariantViewModel != null) {
            ProductVariantOptionParent productVariantOptionParent =
                    productVariantViewModel.getVariantOptionParent(indexOptionParentSizeChart);
            if (productVariantOptionParent != null && productVariantOptionParent.hasProductVariantOptionChild()) {
                vgSizechart.setVisibility(View.VISIBLE);
            } else {
                vgSizechart.setVisibility(View.GONE);
            }
        } else {
            vgSizechart.setVisibility(View.GONE);
        }
    }


    public ProductPictureViewModel getInputtedSizeChart() {
        if (vgSizechart.getVisibility() == View.VISIBLE) {
            return productSizeChart;
        }
        return null;
    }

    private boolean isCatalogHasProductSizeChart() {
        return indexOptionParentSizeChart > -1;
    }

    private void setLabelVariantLevel1() {
        if (productVariantViewModel == null || productVariantViewModel.getVariantOptionParent(0) == null
                || !productVariantViewModel.getVariantOptionParent(0).hasProductVariantOptionChild()) {
            variantLevelOneLabelView.resetContentText();
        } else {
            ProductVariantOptionParent optionLv1 = productVariantViewModel.getVariantOptionParent(0);
            variantLevelOneLabelView.setContent(optionLv1.getProductVariantOptionChild().size()
                    + " " + optionLv1.getName());
        }
    }

    private void setLabelVariantLevel2() {
        if (productVariantViewModel == null || productVariantViewModel.getVariantOptionParent(1) == null
                || !productVariantViewModel.getVariantOptionParent(1).hasProductVariantOptionChild()) {
            variantLevelTwoLabelView.resetContentText();
            // if level 1 is chosen, set enabled to true
            if (productVariantViewModel != null) {
                boolean hasVariantLv1 = productVariantViewModel.getVariantOptionParent(0) != null &&
                        productVariantViewModel.getVariantOptionParent(0).hasProductVariantOptionChild();
                variantLevelTwoLabelView.setEnabled(hasVariantLv1);
            } else {
                variantLevelTwoLabelView.setEnabled(false);
            }
        } else {
            variantLevelTwoLabelView.setEnabled(true);
            ProductVariantOptionParent optionLv2 = productVariantViewModel.getVariantOptionParent(1);
            variantLevelTwoLabelView.setContent(optionLv2.getProductVariantOptionChild().size()
                    + " " + optionLv2.getName());
        }
    }

    @Override
    public void onItemClicked(ProductVariantDashboardViewModel productVariantDashboardViewModel) {
        if (productVariantDashboardViewModel.haslevel2()) {
            ProductVariantDetailLevel1ListActivity.start(getContext(), this, productVariantDashboardViewModel,
                    productVariantViewModel.getVariantOptionParent(0).getName(),
                    productVariantViewModel.getVariantOptionParent(1).getName(),
                    currencyType, defaultStockType, isOfficialStore,
                    needRetainImage);
        } else {
            ProductVariantDetailLevelLeafActivity.start(getContext(), this,
                    productVariantDashboardViewModel.getProductVariantCombinationViewModelList().get(0),
                    productVariantDashboardViewModel.getProductVariantOptionChildLv1(),
                    productVariantViewModel.getVariantOptionParent(0).getName(),
                    currencyType, defaultStockType, isOfficialStore,
                    needRetainImage);
        }
    }

    private void pickVariant(int level) {
        Intent intent = new Intent(getActivity(), ProductVariantPickerActivity.class);
        intent.putExtra(ProductVariantPickerActivity.EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL,
                productVariantByCatModelList.get(level - 1));
        intent.putExtra(ProductVariantPickerActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL,
                productVariantViewModel == null ? null : productVariantViewModel.getVariantOptionParent(level - 1));
        intent.putExtra(ProductVariantPickerActivity.EXTRA_HAS_ORIGINAL_VARIANT,
                getActivity().getIntent().getBooleanExtra(level == 1 ? EXTRA_HAS_ORIGINAL_VARIANT_LV1 : EXTRA_HAS_ORIGINAL_VARIANT_LV2,
                        false));
        startActivityForResult(intent, level);
    }

    @Override
    public boolean needRetainImage() {
        return needRetainImage;
    }

    @Override
    public void changeModelBasedImageUrlOrPath(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            productSizeChart = null;
            return;
        }
        if (productSizeChart == null) { // add
            productSizeChart = new ProductPictureViewModel();
            productSizeChart.setFilePath(imageUrl);
        } else { //change
            productSizeChart.setId(0);
            productSizeChart.setUrlOriginal("");
            productSizeChart.setUrlThumbnail("");
            productSizeChart.setFilePath(imageUrl);
        }
    }

    @Override
    public void refreshImageView() {
        if (productSizeChart == null || TextUtils.isEmpty(productSizeChart.getUriOrPath())) {
            ivSizeChart.setImageResource(R.drawable.ic_add_product);
        } else {
            ImageHandler.LoadImage(ivSizeChart, productSizeChart.getUriOrPath());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE:
            case ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE:
                onActivityResultFromItemPicker(requestCode, data);
                listener.onVariantChangedFromResult();
                break;
            case ProductVariantDetailLevel1ListActivity.VARIANT_EDIT_LEVEL1_LIST_REQUEST_CODE:
                onActivityResultFromDetail(data);
                listener.onVariantChangedFromResult();
                break;
            case ProductVariantDetailLevelLeafActivity.VARIANT_EDIT_LEAF_REQUEST_CODE:
                onActivityResultFromLeaf(data);
                listener.onVariantChangedFromResult();
                break;
        }
    }

    private void onActivityResultFromItemPicker(int requestCodeLevel, Intent data) {
        // it already been sorted. level 1 must be index 0. level 2 = index 1
        if (!data.hasExtra(ProductVariantPickerActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL)) {
            return;
        }
        recyclerViewState = null;

        ProductVariantOptionParent productVariantOptionParent =
                data.getParcelableExtra(ProductVariantPickerActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL);

        if (productVariantViewModel == null) {
            productVariantViewModel = new ProductVariantViewModel();
        }

        if (requestCodeLevel == 1 && (productVariantOptionParent == null || !productVariantOptionParent.hasProductVariantOptionChild())) {
            if (productVariantViewModel.getVariantOptionParent(0) != null) {
                productVariantViewModel.getVariantOptionParent(0).setProductVariantOptionChild(null);
                if (productVariantViewModel.getVariantOptionParent(1) != null) {
                    productVariantViewModel.getVariantOptionParent(1).setProductVariantOptionChild(null);
                }
            }
            productVariantViewModel.setProductVariant(null);
            refreshAllItem();
            return;
        }

        productVariantViewModel.replaceVariantOptionParentFor(requestCodeLevel, productVariantOptionParent);

        // get current selection for item level 1, level 2, and the matrix combination
        ProductVariantOptionParent productVariantOptionParentLevel1 = productVariantViewModel.getVariantOptionParent(0);
        ProductVariantOptionParent productVariantOptionParentLevel2 = productVariantViewModel.getVariantOptionParent(1);
        List<ProductVariantOptionChild> productVariantOptionChildLevel1List = productVariantOptionParentLevel1.getProductVariantOptionChild();
        List<ProductVariantOptionChild> productVariantOptionChildLevel2List = null;
        if (productVariantOptionParentLevel2 != null) {
            productVariantOptionChildLevel2List = productVariantOptionParentLevel2.getProductVariantOptionChild();
        }
        List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList = productVariantViewModel.getProductVariant();

        // create the map for the lookup (this is for performance, instead we do loop each time to get the combination model)
        createCombinationMap(productVariantCombinationViewModelList);

        // generate the matrix axb based on level 1 and level2.
        // example level1 has a variant, level 2 has b variants, the matrix will be (axb)
        // map is used to lookup if the value1x value2 already exist.
        List<ProductVariantCombinationViewModel> newProductVariantCombinationViewModelList = new ArrayList<>();
        for (int i = 0, sizei = productVariantOptionChildLevel1List.size(); i < sizei; i++) {
            if (productVariantOptionChildLevel2List != null) { // have 2 level of variant
                for (int j = 0, sizej = productVariantOptionChildLevel2List.size(); j < sizej; j++) {
                    Pair<String, String> pair = new Pair<>(
                            productVariantOptionChildLevel1List.get(i).getValue(),
                            productVariantOptionChildLevel2List.get(j).getValue());
                    if (mapCombination.containsKey(pair)) {
                        int combinationIndex = mapCombination.get(pair);
                        newProductVariantCombinationViewModelList.add(productVariantCombinationViewModelList.get(combinationIndex));
                    } else {
                        newProductVariantCombinationViewModelList.add(new ProductVariantCombinationViewModel(
                                isDefaultStockStatusActive(),
                                defaultPrice,
                                getDefaultStock(),
                                defaultSku,
                                productVariantOptionChildLevel1List.get(i).getValue(),
                                productVariantOptionChildLevel2List.get(j).getValue()
                        ));
                    }
                }
            } else { // only have 1 level of variant, exmple: red, but no XL
                Pair<String, String> pair = new Pair<>(productVariantOptionChildLevel1List.get(i).getValue(), "");
                if (mapCombination.containsKey(pair)) {
                    int combinationIndex = mapCombination.get(pair);
                    newProductVariantCombinationViewModelList.add(productVariantCombinationViewModelList.get(combinationIndex));
                } else {
                    newProductVariantCombinationViewModelList.add(new ProductVariantCombinationViewModel(
                            isDefaultStockStatusActive(),
                            defaultPrice,
                            getDefaultStock(),
                            defaultSku,
                            productVariantOptionChildLevel1List.get(i).getValue(),
                            ""
                    ));
                }
            }
        }
        productVariantViewModel.setProductVariant(newProductVariantCombinationViewModelList);

        refreshAllItem();
    }

    private void refreshAllItem() {
        initVariantLabel();
        initSizeChart();
        updateVariantItemListView();
    }

    private boolean isDefaultStockStatusActive() {
        return defaultStockType == StockTypeDef.TYPE_ACTIVE || defaultStockType == StockTypeDef.TYPE_ACTIVE_LIMITED;
    }

    private int getDefaultStock() {
        return (defaultStockType == StockTypeDef.TYPE_ACTIVE_LIMITED) ? 1 : 0;
    }

    private void createCombinationMap(List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList) {
        mapCombination = new HashMap<>();
        if (productVariantCombinationViewModelList != null) {
            for (int i = 0, sizei = productVariantCombinationViewModelList.size(); i < sizei; i++) {
                ProductVariantCombinationViewModel productVariantCombinationViewModel = productVariantCombinationViewModelList.get(i);
                mapCombination.put(new Pair<>(productVariantCombinationViewModel.getLevel1String(),
                        productVariantCombinationViewModel.getLevel2String()), i);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void onActivityResultFromDetail(Intent data) {
        if (ProductVariantDetailLevel1ListActivity.EXTRA_ACTION_SUBMIT.equals(data.getAction())) {
            onActivityResultFromDetailUpdateList((ProductVariantDashboardViewModel)
                    data.getParcelableExtra(ProductVariantDetailLevel1ListActivity.EXTRA_PRODUCT_VARIANT_DATA));
        }
    }

    @SuppressWarnings("unchecked")
    private void onActivityResultFromLeaf(Intent data) {
        if (ProductVariantDetailLevelLeafActivity.EXTRA_ACTION_SUBMIT.equals(data.getAction())) {
            ProductVariantOptionChild productVariantOptionChild = null;
            if (data.hasExtra(ProductVariantDetailLevelLeafActivity.EXTRA_PRODUCT_VARIANT_OPTION_CHILD)) {
                productVariantOptionChild = data.getParcelableExtra(ProductVariantDetailLevelLeafActivity.EXTRA_PRODUCT_VARIANT_OPTION_CHILD);
            }
            onActivityResultFromDetailLeafUpdateList((ProductVariantCombinationViewModel)
                            data.getParcelableExtra(ProductVariantDetailLevelLeafActivity.EXTRA_PRODUCT_VARIANT_LEAF_DATA),
                    productVariantOptionChild);
        }
    }

    private void onActivityResultFromDetailUpdateList(ProductVariantDashboardViewModel productVariantDashboardViewModel) {
        // update from dashboardviewmodel back to the variantview model
        String lv1Value = productVariantDashboardViewModel.getProductVariantOptionChildLv1().getValue();
        // for image
        productVariantViewModel.replaceVariantOptionChildFor(1,
                productVariantDashboardViewModel.getProductVariantOptionChildLv1());
        // for combination data list
        productVariantViewModel.replaceSelectedVariantFor(lv1Value,
                productVariantDashboardViewModel.getProductVariantCombinationViewModelList());
        refreshData();
    }

    private void onActivityResultFromDetailLeafUpdateList(ProductVariantCombinationViewModel productVariantCombinationViewModel,
                                                          ProductVariantOptionChild productVariantOptionChild) {
        // update from dashboardviewmodel back to the variantview model
        productVariantViewModel.replaceSelectedVariantFor(productVariantCombinationViewModel);
        // to replace image
        if (productVariantOptionChild != null) {
            productVariantViewModel.replaceVariantOptionChildFor(1, productVariantOptionChild);
        }
        refreshData();
    }

    private void refreshData() {
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        generateToDashboardViewModel();
        onSearchLoaded(this.productVariantDashboardViewModelList);
    }

    public void onSearchLoaded(@NonNull List<ProductVariantDashboardViewModel> list) {
        if (productVariantViewModel != null && productVariantViewModel.getVariantOptionParent(1) != null) {
            productVariantDashboardNewAdapter.setLevel2String(productVariantViewModel.getVariantOptionParent(1).getName());
        } else {
            productVariantDashboardNewAdapter.setLevel2String(null);
        }
        productVariantDashboardNewAdapter.clearData();
        productVariantDashboardNewAdapter.addData(list);
        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
        }
    }

    private void updateVariantItemListView() {
        if (productVariantViewModel == null || !productVariantViewModel.hasSelectedVariant()) {
            recyclerView.setVisibility(View.INVISIBLE);
            return;
        }
        generateToDashboardViewModel();
        recyclerView.setVisibility(View.VISIBLE);
        onSearchLoaded(productVariantDashboardViewModelList);
    }

    private void generateToDashboardViewModel() {
        // get level 1 and flattened the model to [dashboard view model]
        // CATEGORY + VARIANT SELECTION = DASHBOARD VIEW MODEL

        //RESULT:
        // 0: "Merah" -> List XL(comb model),M(comb model), S(comb model)
        // 1: "Biru" -> List XL(comb model),M(comb model), S(comb model)
        // 2: "Ungu" -> List XL(comb model),M(comb model), S(comb model)
        productVariantDashboardViewModelList = new ArrayList<>();
        List<ProductVariantOptionChild> productVariantOptionChildListLv1 =
                productVariantViewModel.getProductVariantOptionChild(0);
        if (productVariantOptionChildListLv1 == null) {
            return;
        }

        List<ProductVariantOptionChild> productVariantOptionChildListLv2LookUp =
                productVariantViewModel.getProductVariantOptionChild(1);
        SparseIntArray mapPvoToIndex = new SparseIntArray();
        createMap(productVariantOptionChildListLv2LookUp, mapPvoToIndex);

        List<ProductVariantCombinationViewModel> productVariant = productVariantViewModel.getProductVariant();
        if (productVariant == null) {
            productVariant = new ArrayList<>();
        }

        // loop for level 1: ex: red, blue, purple
        for (int i = 0, sizei = productVariantOptionChildListLv1.size(); i < sizei; i++) {
            ProductVariantDashboardViewModel productVariantDashboardViewModel =
                    new ProductVariantDashboardViewModel(productVariantOptionChildListLv1.get(i));
            for (int j = 0, sizej = productVariant.size(); j < sizej; j++) {
                productVariantDashboardViewModel.addCombinationModelIfAligned(productVariant.get(j),
                        productVariantOptionChildListLv2LookUp, mapPvoToIndex);
            }

            // server bug, the variant is less than the option; we generate the variant.
            // ex: option level 1 has red, blur, green, but there is only red and blue in the selected variant;
            // we generate the missing varian with all empty value and empty stock.
            if (productVariantDashboardViewModel.getProductVariantCombinationViewModelList().isEmpty() ) {
                productVariantDashboardViewModel.addCombinationModel(productVariantOptionChildListLv2LookUp);
                productVariantViewModel.getProductVariant().addAll(
                        productVariantDashboardViewModel.getProductVariantCombinationViewModelList());
            }
            productVariantDashboardViewModelList.add(productVariantDashboardViewModel);
        }
    }

    private void createMap(List<ProductVariantOptionChild> productVariantOptionChildList, SparseIntArray mapPvoToIndex) {
        if (productVariantOptionChildList != null && productVariantOptionChildList.size() > 0) {
            for (int i = 0, sizei = productVariantOptionChildList.size(); i < sizei; i++) {
                ProductVariantOptionChild productVariantOptionChildLv2 = productVariantOptionChildList.get(i);
                int tIdOrPvo = productVariantOptionChildLv2.gettId() > 0 ? productVariantOptionChildLv2.gettId() :
                        productVariantOptionChildLv2.getPvo();
                mapPvoToIndex.put(tIdOrPvo, i);
            }
        }
    }

    @Override
    public void onImageViewVariantClicked(ProductVariantDashboardViewModel model,
                                          VariantPictureViewModel pictureViewModel,
                                          int position) {
        onItemClicked(model);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
        outState.putParcelable(EXTRA_PRODUCT_SIZECHART, productSizeChart);
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachActivity(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity);
        }
    }

    protected void onAttachActivity(Context context) {
        listener = (OnProductVariantDashboardFragmentListener) context;
    }


}