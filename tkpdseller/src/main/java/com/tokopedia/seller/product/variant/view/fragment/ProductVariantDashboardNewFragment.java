package com.tokopedia.seller.product.variant.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardNewActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailLevel1ListActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailLevelLeafActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantPickerNewActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDashboardNewAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST;
import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_SELECTION;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDashboardNewFragment extends BaseListFragment<BlankPresenter, ProductVariantDashboardNewViewModel>
        implements ProductVariantMainView, ProductVariantDashboardNewAdapter.OnProductVariantDashboardNewAdapterListener {

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private ProductVariantViewModel productVariantViewModel;
    private RecyclerView recyclerView;
    private List<ProductVariantDashboardNewViewModel> productVariantDashboardNewViewModelList;

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

    private ProductVariantDashboardNewAdapter productVariantDashboardNewAdapter;

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
        currencyType = activityIntent.getIntExtra(ProductVariantDashboardNewActivity.EXTRA_CURRENCY_TYPE, CurrencyTypeDef.TYPE_IDR);
        defaultPrice = activityIntent.getDoubleExtra(ProductVariantDashboardNewActivity.EXTRA_DEFAULT_PRICE, 0);
        defaultStockType = activityIntent.getIntExtra(ProductVariantDashboardNewActivity.EXTRA_STOCK_TYPE, 0);
        isOfficialStore = activityIntent.getBooleanExtra(ProductVariantDashboardNewActivity.EXTRA_IS_OFFICIAL_STORE, false);
        needRetainImage = activityIntent.getBooleanExtra(ProductVariantDashboardNewActivity.EXTRA_NEED_RETAIN_IMAGE, false);
        defaultSku = activityIntent.getStringExtra(ProductVariantDashboardNewActivity.EXTRA_DEFAULT_SKU);

        if (savedInstanceState == null) {
            productVariantViewModel = activityIntent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_SELECTION);
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
        updateVariantItemListView();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
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

    private void setLabelVariantLevel1() {
        if (productVariantViewModel == null || productVariantViewModel.getVariantOptionParent(1) == null
                || !productVariantViewModel.getVariantOptionParent(1).hasProductVariantOptionChild()) {
            variantLevelOneLabelView.resetContentText();
        } else {
            ProductVariantOptionParent optionLv1 = productVariantViewModel.getVariantOptionParent(1);
            variantLevelOneLabelView.setContent(optionLv1.getProductVariantOptionChild().size()
                    + " " + optionLv1.getName());
        }
    }

    private void setLabelVariantLevel2() {
        if (productVariantViewModel == null || productVariantViewModel.getVariantOptionParent(2) == null
                || !productVariantViewModel.getVariantOptionParent(2).hasProductVariantOptionChild()) {
            variantLevelTwoLabelView.resetContentText();
            // if level 1 is chosen, set enabled to true
            if (productVariantViewModel != null) {
                variantLevelTwoLabelView.setEnabled(productVariantViewModel.getVariantOptionParent(1) != null);
            }
        } else {
            variantLevelTwoLabelView.setEnabled(true);
            ProductVariantOptionParent optionLv2 = productVariantViewModel.getVariantOptionParent(2);
            variantLevelTwoLabelView.setContent(optionLv2.getProductVariantOptionChild().size()
                    + " " + optionLv2.getName());
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<ProductVariantDashboardNewViewModel> getNewAdapter() {
        productVariantDashboardNewAdapter = new ProductVariantDashboardNewAdapter(currencyType, this);
        return productVariantDashboardNewAdapter;
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel) {
        if (productVariantDashboardNewViewModel.haslevel2()) {
            ProductVariantDetailLevel1ListActivity.start(getContext(), this, productVariantDashboardNewViewModel,
                    productVariantViewModel.getVariantOptionParent(1).getName(),
                    productVariantViewModel.getVariantOptionParent(2).getName(),
                    currencyType, defaultStockType, isOfficialStore,
                    needRetainImage);
        } else {
            ProductVariantDetailLevelLeafActivity.start(getContext(), this,
                    productVariantDashboardNewViewModel.getProductVariantCombinationViewModelList().get(0),
                    productVariantDashboardNewViewModel.getProductVariantOptionChildLv1(),
                    productVariantViewModel.getVariantOptionParent(1).getName(),
                    currencyType, defaultStockType, isOfficialStore,
                    needRetainImage);
        }
    }

    private void pickVariant(int level) {
        Intent intent = new Intent(getActivity(), ProductVariantPickerNewActivity.class);
        intent.putExtra(ProductVariantPickerNewActivity.EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL,
                productVariantByCatModelList.get(level - 1));
        intent.putExtra(ProductVariantPickerNewActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL,
                productVariantViewModel == null ? null : productVariantViewModel.getVariantOptionParent(level));
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
            case ProductVariantDetailLevel1ListActivity.VARIANT_EDIT_LEVEL1_LIST_REQUEST_CODE:
                onActivityResultFromDetail(data);
                break;
            case ProductVariantDetailLevelLeafActivity.VARIANT_EDIT_LEAF_REQUEST_CODE:
                onActivityResultFromLeaf(data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onActivityResultFromItemPicker(int requestCodeLevel, Intent data) {
        // it already been sorted. level 1 must be index 0. level 2 = index 1
        if (!data.hasExtra(ProductVariantPickerNewActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL)) {
            return;
        }
        recyclerViewState = null;

        ProductVariantOptionParent productVariantOptionParent =
                data.getParcelableExtra(ProductVariantPickerNewActivity.EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL);
        if (requestCodeLevel == 1 &&  (productVariantOptionParent == null || !productVariantOptionParent.hasProductVariantOptionChild())) {
            productVariantViewModel.getVariantOptionParent(1).setProductVariantOptionChild(null);
            if (productVariantViewModel.getVariantOptionParent(2)!= null) {
                productVariantViewModel.getVariantOptionParent(2).setProductVariantOptionChild(null);
            }
            productVariantViewModel.setProductVariant(null);
            initVariantLabel();
            updateVariantItemListView();
            return;
        }

        if (productVariantViewModel == null) {
            productVariantViewModel = new ProductVariantViewModel();
        }
        productVariantViewModel.replaceVariantOptionParentFor(requestCodeLevel, productVariantOptionParent);

        // get current selection for item level 1, level 2, and the matrix combination
        ProductVariantOptionParent productVariantOptionParentLevel1 = productVariantViewModel.getVariantOptionParent(1);
        ProductVariantOptionParent productVariantOptionParentLevel2 = productVariantViewModel.getVariantOptionParent(2);
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

        initVariantLabel();
        updateVariantItemListView();
    }

    private boolean isDefaultStockStatusActive(){
        return defaultStockType == StockTypeDef.TYPE_ACTIVE || defaultStockType == StockTypeDef.TYPE_ACTIVE_LIMITED;
    }

    private int getDefaultStock(){
        return (defaultStockType == StockTypeDef.TYPE_ACTIVE_LIMITED)? 1 : 0;
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
            onActivityResultFromDetailUpdateList((ProductVariantDashboardNewViewModel)
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

    private void onActivityResultFromDetailUpdateList(ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel) {
        // update from dashboardviewmodel back to the variantview model
        String lv1Value = productVariantDashboardNewViewModel.getProductVariantOptionChildLv1().getValue();
        // for image
        productVariantViewModel.replaceVariantOptionChildFor(1,
                productVariantDashboardNewViewModel.getProductVariantOptionChildLv1());
        // for combination data list
        productVariantViewModel.replaceSelectedVariantFor(lv1Value,
                productVariantDashboardNewViewModel.getProductVariantCombinationViewModelList());
        refreshData();
    }

    private void onActivityResultFromDetailLeafUpdateList(ProductVariantCombinationViewModel productVariantCombinationViewModel,
                                                          ProductVariantOptionChild productVariantOptionChild) {
        // update from dashboardviewmodel back to the variantview model
        productVariantViewModel.replaceSelectedVariantFor(productVariantCombinationViewModel);
        // to replace image
        if (productVariantOptionChild!= null) {
            productVariantViewModel.replaceVariantOptionChildFor(1, productVariantOptionChild);
        }
        refreshData();
    }

    private void refreshData(){
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        generateToDashboardViewModel();
        if (productVariantViewModel!= null && productVariantViewModel.getVariantOptionParent(2)!= null) {
            productVariantDashboardNewAdapter.setLevel2String(productVariantViewModel.getVariantOptionParent(2).getName());
        } else {
            productVariantDashboardNewAdapter.setLevel2String(null);
        }
        adapter.clearData();
        onSearchLoaded(this.productVariantDashboardNewViewModelList, this.productVariantDashboardNewViewModelList.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductVariantDashboardNewViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        if (recyclerViewState!= null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
        }
    }

    private void updateVariantItemListView() {
        if (productVariantViewModel == null || !productVariantViewModel.hasSelectedVariant()) {
            recyclerView.setVisibility(View.GONE);
            return;
        }
        generateToDashboardViewModel();
        adapter.clearData();
        recyclerView.setVisibility(View.VISIBLE);
        onSearchLoaded(productVariantDashboardNewViewModelList, productVariantDashboardNewViewModelList.size());
    }

    private void generateToDashboardViewModel() {
        // get level 1 and flattened the model to [dashboard view model]
        // CATEGORY + VARIANT SELECTION = DASHBOARD VIEW MODEL

        //RESULT:
        // 0: "Merah" -> List XL(comb model),M(comb model), S(comb model)
        // 1: "Biru" -> List XL(comb model),M(comb model), S(comb model)
        // 2: "Ungu" -> List XL(comb model),M(comb model), S(comb model)
        productVariantDashboardNewViewModelList = new ArrayList<>();
        List<ProductVariantOptionChild> productVariantOptionChildListLv1 =
                productVariantViewModel.getProductVariantOptionChild(0);
        if (productVariantOptionChildListLv1 == null) {
            return;
        }

        List<ProductVariantOptionChild> productVariantOptionChildListLv2LookUp =
                productVariantViewModel.getProductVariantOptionChild(1);
        SparseIntArray mapPvoToIndex = new SparseIntArray();
        createMap(productVariantOptionChildListLv2LookUp, mapPvoToIndex);
        // loop for level 1: ex: red, blue, purple
        for (int i = 0, sizei = productVariantOptionChildListLv1.size(); i < sizei; i++) {
            ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel =
                    new ProductVariantDashboardNewViewModel(productVariantOptionChildListLv1.get(i));
            List<ProductVariantCombinationViewModel> productVariant = productVariantViewModel.getProductVariant();
            for (int j = 0, sizej = productVariant.size(); j < sizej; j++) {
                productVariantDashboardNewViewModel.addCombinationModelIfAligned(productVariant.get(j),
                        productVariantOptionChildListLv2LookUp, mapPvoToIndex);
            }
            productVariantDashboardNewViewModelList.add(productVariantDashboardNewViewModel);
        }
    }

    private void createMap(List<ProductVariantOptionChild> productVariantOptionChildList, SparseIntArray mapPvoToIndex){
        if (productVariantOptionChildList!= null && productVariantOptionChildList.size() > 0) {
            for (int i = 0, sizei = productVariantOptionChildList.size(); i < sizei; i++) {
                ProductVariantOptionChild productVariantOptionChildLv2 = productVariantOptionChildList.get(i);
                int tIdOrPvo = productVariantOptionChildLv2.gettId() > 0 ? productVariantOptionChildLv2.gettId():
                        productVariantOptionChildLv2.getPvo();
                mapPvoToIndex.put(tIdOrPvo, i);
            }
        }
    }

    @Override
    public void onImageViewVariantClicked(ProductVariantDashboardNewViewModel model,
                                          VariantPictureViewModel pictureViewModel,
                                          int position) {
        onItemClicked(model);
    }


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
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
    }


}