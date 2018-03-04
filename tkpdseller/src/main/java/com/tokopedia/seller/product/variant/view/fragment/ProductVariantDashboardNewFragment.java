package com.tokopedia.seller.product.variant.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
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
        implements ProductVariantMainView {

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private ProductVariantViewModel productVariantViewModel;
    private RecyclerView recyclerView;
    private List<ProductVariantDashboardNewViewModel> productVariantDashboardNewViewModelList;
    private @CurrencyTypeDef
    int currencyType;
    private HashMap<String, Integer> mapLevel1;
    private HashMap<String, Integer> mapLevel2;
    private HashMap<Pair<String, String>, Integer> mapCombination;
    private Parcelable recyclerViewState;

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

        if (activityIntent.hasExtra(EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST)) {
            productVariantByCatModelList = activityIntent.getParcelableArrayListExtra(ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST);
        } else { // TODO remove this, only for test
            productVariantByCatModelList = getProductVariantByCatModelListFromJson();
        }
        currencyType = activityIntent.getIntExtra(ProductVariantDashboardNewActivity.EXTRA_CURRENCY_TYPE, CurrencyTypeDef.TYPE_IDR);

        if (savedInstanceState == null) {
            if (activityIntent.hasExtra(EXTRA_PRODUCT_VARIANT_SELECTION)) {
                productVariantViewModel = activityIntent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_SELECTION);
            } else { //TODO this is just test, remove this after finish testing
                productVariantViewModel = getProductVariantByPrdModelFromJson();
            }
        } else {
            productVariantViewModel = savedInstanceState.getParcelable(ProductVariantDashboardNewActivity.EXTRA_PRODUCT_VARIANT_SELECTION);
        }
    }

    //TODO, remove this. just for test
    public ArrayList<ProductVariantByCatModel> getProductVariantByCatModelListFromJson() {
        String jsonString = loadJSONFromAsset();
        Type type = new TypeToken<ArrayList<ProductVariantByCatModel>>() {
        }.getType();
        return new Gson().fromJson(jsonString, type);
    }

    //TODO, remove this. just for test
    public ProductVariantViewModel getProductVariantByPrdModelFromJson() {
        String jsonString = loadJSONFromAsset2();
        Type type = new TypeToken<ProductVariantViewModel>() {
        }.getType();
        return new Gson().fromJson(jsonString, type);
    }

    //TODO, remove this. just for test
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("test_variant_by_cat.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //TODO, remove this. just for test
    public String loadJSONFromAsset2() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("test_variant_by_prd.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
//        updateVariantUnitView();
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
        if (productVariantViewModel == null || productVariantViewModel.getVariantOptionParent(1) == null) {
            variantLevelOneLabelView.resetContentText();
        } else {
            ProductVariantOptionParent optionLv1 = productVariantViewModel.getVariantOptionParent(1);
            variantLevelOneLabelView.setContent(optionLv1.getProductVariantOptionChild().size()
                    + " " + optionLv1.getName());
        }
    }

    private void setLabelVariantLevel2() {
        if (productVariantViewModel == null || productVariantViewModel.getVariantOptionParent(2) == null) {
            variantLevelTwoLabelView.resetContentText();
            // if level 1 is chosen, set enabled to true
            variantLevelTwoLabelView.setEnabled(productVariantViewModel.getVariantOptionParent(1) != null);
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
        return new ProductVariantDashboardNewAdapter(currencyType);
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
                    currencyType);
        } else {
            ProductVariantDetailLevelLeafActivity.start(getContext(), this,
                    productVariantDashboardNewViewModel.getProductVariantCombinationViewModelList().get(0),
                    productVariantViewModel.getVariantOptionParent(1).getName(),
                    currencyType);
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
        createOptionMap(productVariantOptionChildLevel1List,productVariantOptionChildLevel2List);
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
                                0, // TODO setup default price for generated variant
                                0, // TODO setup default stock for generated variant
                                "",
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
                            0, // TODO setup default price for generated variant
                            0, // TODO setup default stock for generated variant
                            "",
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

    private void createOptionMap(List<ProductVariantOptionChild> productVariantOptionChildLevel1List,
                                 List<ProductVariantOptionChild> productVariantOptionChildLevel2List) {
        mapLevel1 = new HashMap<>();
        mapLevel2 = new HashMap<>();
        int counter = 1;
        for (int i = 0, sizei = productVariantOptionChildLevel1List.size(); i < sizei; i++) {
            ProductVariantOptionChild productVariantOptionChild = productVariantOptionChildLevel1List.get(i);
            productVariantOptionChild.settId(counter++);
            mapLevel1.put(productVariantOptionChild.getValue(), i);
        }

        if (productVariantOptionChildLevel2List != null) {
            for (int i = 0, sizei = productVariantOptionChildLevel2List.size(); i < sizei; i++) {
                ProductVariantOptionChild productVariantOptionChild = productVariantOptionChildLevel2List.get(i);
                productVariantOptionChild.settId(counter++);
                mapLevel2.put(productVariantOptionChild.getValue(), i);
            }
        }
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
            onActivityResultFromDetailLeafUpdateList((ProductVariantCombinationViewModel)
                    data.getParcelableExtra(ProductVariantDetailLevelLeafActivity.EXTRA_PRODUCT_VARIANT_LEAF_DATA));
        }
    }

    private void onActivityResultFromDetailUpdateList(ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel) {
        // update from dashboardviewmodel back to the variantview model
        String lv1Value = productVariantDashboardNewViewModel.getProductVariantOptionChildLv1().getValue();
        productVariantViewModel.replaceSelectedVariantFor(lv1Value,
                productVariantDashboardNewViewModel.getProductVariantCombinationViewModelList());
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        generateToDashboardViewModel();
        adapter.clearData();
        onSearchLoaded(this.productVariantDashboardNewViewModelList, this.productVariantDashboardNewViewModelList.size());
    }

    private void onActivityResultFromDetailLeafUpdateList(ProductVariantCombinationViewModel productVariantCombinationViewModel) {
        // update from dashboardviewmodel back to the variantview model
        productVariantViewModel.replaceSelectedVariantFor(productVariantCombinationViewModel);
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        generateToDashboardViewModel();
        adapter.clearData();
        onSearchLoaded(this.productVariantDashboardNewViewModelList, this.productVariantDashboardNewViewModelList.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductVariantDashboardNewViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        if (recyclerViewState!= null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    /**
     * Update variant item list view
     */
    private void updateVariantItemListView() {
        if (productVariantViewModel == null || !productVariantViewModel.hasSelectedVariant()) {
            recyclerView.setVisibility(View.GONE);
            return;
        }
        generateToDashboardViewModel();
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
        //TODO use map to change this double loop to single loop.
        List<ProductVariantOptionChild> productVariantOptionChildListLv2LookUp =
                productVariantViewModel.getProductVariantOptionChild(1);
        // loop for level 1: ex: red, blue, purple
        for (int i = 0, sizei = productVariantOptionChildListLv1.size(); i < sizei; i++) {
            ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel =
                    new ProductVariantDashboardNewViewModel(productVariantOptionChildListLv1.get(i));
            List<ProductVariantCombinationViewModel> productVariant = productVariantViewModel.getProductVariant();
            for (int j = 0, sizej = productVariant.size(); j < sizej; j++) {
                productVariantDashboardNewViewModel.addCombinationModelIfAligned(productVariant.get(j),
                        productVariantOptionChildListLv2LookUp);
            }
            productVariantDashboardNewViewModelList.add(productVariantDashboardNewViewModel);
        }
    }

    public ProductVariantViewModel getProductVariantViewModelGenerateTid() {
        if (!productVariantViewModel.hasSelectedVariant()) {
            return productVariantViewModel;
        }
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
        createOptionMap(productVariantOptionChildLevel1List,productVariantOptionChildLevel2List);

        // generate the matrix axb based on level 1 and level2.
        // example level1 has a variant, level 2 has b variants, the matrix will be (axb)
        // map is used to lookup if the value1x value2 already exist.
        for (int i = 0, sizei = productVariantCombinationViewModelList.size(); i < sizei; i++) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel = productVariantCombinationViewModelList.get(i);
            String level1String = productVariantCombinationViewModel.getLevel1String();

            List<Integer> integerList = new ArrayList<>();
            int indexLevel1 = mapLevel1.get(level1String);
            int tIdLevel1 = productVariantOptionParentLevel1.getProductVariantOptionChild().get(indexLevel1).gettId();
            integerList.add(tIdLevel1);
            if (productVariantOptionParentLevel2!= null && productVariantOptionParentLevel2.hasProductVariantOptionChild()) {
                String level2String = productVariantCombinationViewModel.getLevel2String();
                int indexLevel2 = mapLevel2.get(level2String);
                int tIdLevel2 = productVariantOptionParentLevel2.getProductVariantOptionChild().get(indexLevel2).gettId();
                integerList.add(tIdLevel2);
            }
            productVariantCombinationViewModel.setOpt(integerList);
        }
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