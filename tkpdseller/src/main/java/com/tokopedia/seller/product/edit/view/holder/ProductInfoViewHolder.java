package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.view.activity.CatalogPickerActivity;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductInfoViewHolder extends ProductViewHolder implements RadioGroup.OnCheckedChangeListener {

    private final TextWatcher nameTextWatcher;

    public interface Listener {
        void onCategoryPickerClicked(long categoryId);

        void onCatalogPickerClicked(String keyword, long depId, long selectedCatalogId);

        void onProductNameChanged(String productName);

        void onCategoryChanged(long categoryId);

        void fetchCategory(long categoryId);
    }

    private static final String BUNDLE_CATEGORY_ID = "BUNDLE_CATEGORY_ID";
    private static final String BUNDLE_CATEGORY_NAME = "BUNDLE_CATEGORY_NAME";
    private static final String BUNDLE_CATALOG_SHOWN = "BUNDLE_CATALOG_SHOWN";
    private static final String BUNDLE_CATALOG_ID = "BUNDLE_CATALOG_ID";
    private static final String BUNDLE_CATALOG_NAME = "BUNDLE_CATALOG_NAME";
    private static final String BUNDLE_CAT_RECOMM = "BUNDLE_CAT_RECOM";

    private static final int DEFAULT_CATEGORY_ID = -1;
    private static final int DEFAULT_CATALOG_ID = -1;
    public static final int REQUEST_CODE_CATEGORY = 101;
    public static final int REQUEST_CODE_CATALOG = 102;

    private TextInputLayout nameTextInputLayout;
    private EditText nameEditText;
    private LabelView categoryLabelView;
    private LabelView catalogLabelView;
    private View categoryRecommView;
    private RadioGroup radioGroupCategoryRecomm;

    private Listener listener;
    private long categoryId;
    private long catalogId;

    ArrayList <ProductCategoryPredictionViewModel> categoryPredictionList;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ProductInfoViewHolder(View view) {
        categoryId = DEFAULT_CATEGORY_ID;
        catalogId = DEFAULT_CATALOG_ID;
        categoryRecommView = view.findViewById(R.id.view_group_category_recomm);
        radioGroupCategoryRecomm = (RadioGroup) categoryRecommView.findViewById(R.id.radio_group_category_recomm);
        nameTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_name);
        nameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        categoryLabelView = (LabelView) view.findViewById(R.id.label_view_category);
        catalogLabelView = (LabelView) view.findViewById(R.id.label_view_catalog);
        categoryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onCategoryPickerClicked(categoryId);
                }
            }
        });
        catalogLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onCatalogPickerClicked(nameEditText.getText().toString(), categoryId, catalogId);
                }
            }
        });
        nameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nameTextInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() >= 2) {
                    if (listener != null) {
                        listener.onProductNameChanged(s.toString());
                    }
                }
            }
        };
        nameEditText.addTextChangedListener(nameTextWatcher);
        radioGroupCategoryRecomm.setOnCheckedChangeListener(this);
    }

    public boolean isNameEditable(){
        return nameEditText.isEnabled();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if(checkedId < 0){
            return;
        }

        if (checkedId != categoryId) {
            categoryId = checkedId;
            if (categoryId <= 0) {
                hideAndClearCatalog();
            }
            if (listener != null) {
                listener.onCategoryChanged(categoryId);
            }
        }
        View radioButton = group.findViewById(checkedId);
        if (radioButton == null) {
            categoryLabelView.resetContentText();
            return;
        }
        int childIndex = group.indexOfChild(radioButton);
        ProductCategoryPredictionViewModel productCategoryPrediction = categoryPredictionList.get(childIndex);
        if (productCategoryPrediction == null) {
            categoryLabelView.resetContentText();
            return;
        }
        processCategory(productCategoryPrediction.getCategoryName());
    }

    public String getName() {
        return nameEditText.getText().toString().trim();
    }

    public void setName(String name) {
        nameEditText.setText(name==null?null:MethodChecker.fromHtml(name));
        nameEditText.setSelection( nameEditText.getText() == null? 0 : nameEditText.getText().length());
    }

    public void setNameNoWatcher(String name) {
        nameEditText.removeTextChangedListener(nameTextWatcher);
        setName(name);
        nameEditText.addTextChangedListener(nameTextWatcher);
    }

    public String getCatalogName() {
        return catalogLabelView.getContent();
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCatalogId() {
        return catalogId;
    }

    public void setNameEnabled(boolean enabled) {
        nameEditText.setEnabled(enabled);
    }

    public void successGetCategoryRecommData(List<ProductCategoryPredictionViewModel> categoryPredictionList) {
        if (categoryPredictionList == null || categoryPredictionList.size() == 0) {
            hideAndClearCategoryRecomm();
            return;
        }
        int predictionSize = categoryPredictionList.size();
        // create the radio buttons
        if (radioGroupCategoryRecomm.getChildCount() == 0 ||
                predictionSize != radioGroupCategoryRecomm.getChildCount()) {
            radioGroupCategoryRecomm.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(radioGroupCategoryRecomm.getContext());
            for (int i = 0; i < predictionSize; i++) {
                RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.item_basic_radio_button, radioGroupCategoryRecomm, false);
                radioGroupCategoryRecomm.addView(radioButton);
            }
        }

        radioGroupCategoryRecomm.clearCheck();

        // update values
        for (int i = 0; i < predictionSize; i++) {
            RadioButton radioButton = (RadioButton) radioGroupCategoryRecomm.getChildAt(i);
            radioButton.setText(categoryPredictionList.get(i).getPrintedString());
            radioButton.setId(categoryPredictionList.get(i).getLastCategoryId());
        }
        this.categoryPredictionList = (ArrayList) categoryPredictionList;
        categoryRecommView.setVisibility(View.VISIBLE);
    }

    private void processCategoryFromActivityResult(Intent data) {
        long previousCategoryId = categoryId;
        categoryId = data.getLongExtra(CategoryPickerActivity.CATEGORY_RESULT_ID, -1);
        if (previousCategoryId != categoryId) {
            if (categoryId <= 0) {
                hideAndClearCatalog();
            }
            if (listener != null) {
                listener.onCategoryChanged(categoryId);
            }
            // reselect the id if exist on the radio button
            // unselect if the id not exist
            selectRadioByCategoryId((int)categoryId);
        }
    }

    private void selectRadioByCategoryId(int categoryId){
        radioGroupCategoryRecomm.setOnCheckedChangeListener(null);
        RadioButton radioButton = (RadioButton) radioGroupCategoryRecomm.findViewById(categoryId);
        if (radioButton == null) {
            radioGroupCategoryRecomm.clearCheck();
        } else if (!radioButton.isChecked()) {
            radioButton.setChecked(true);
        }
        radioGroupCategoryRecomm.setOnCheckedChangeListener(this);
    }

    public void processCategory(String[] categoryNameArr) {
        String category = "";
        for (int i = 0, sizei = categoryNameArr.length; i < sizei; i++) {
            String categoryName = categoryNameArr[i];
            if (TextUtils.isEmpty(categoryName)) {
                continue;
            }
            category += categoryName;
            if (i < sizei - 1) {
                category += "\n";
            }
        }
        categoryLabelView.setContent(category);
    }

    private void hideAndClearCategoryRecomm() {
        categoryPredictionList = null;
        categoryRecommView.setVisibility(View.GONE);
    }

    public void successFetchCatalogData(List<Catalog> catalogViewModelList) {
        if (catalogViewModelList == null || catalogViewModelList.size() < 1) {
            hideAndClearCatalog();
        } else {
            catalogId = -1;
            catalogLabelView.setVisibility(View.VISIBLE);
        }
    }

    private void processCatalogFromActivityResult(Intent intent) {
        catalogId = intent.getLongExtra(CatalogPickerActivity.CATALOG_ID, 0);
        String catalogName = intent.getStringExtra(CatalogPickerActivity.CATALOG_NAME);
        setCatalog(catalogId, catalogName);
    }

    public void setCatalog(long catalogId, String name) {
        this.catalogId = catalogId;
        if (catalogId <= 0) {
            catalogLabelView.setContent(catalogLabelView.getContext().getString(R.string.product_label_choose));
        } else {
            catalogLabelView.setContent(name);
            catalogLabelView.setVisibility(View.VISIBLE);
        }
    }

    public void hideAndClearCatalog() {
        setCatalog(-1, null);
        catalogLabelView.setVisibility(View.GONE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductInfoViewHolder.REQUEST_CODE_CATEGORY:
                if (resultCode == Activity.RESULT_OK) {
                    processCategoryFromActivityResult(data);
                }
                break;
            case ProductInfoViewHolder.REQUEST_CODE_CATALOG:
                if (resultCode == Activity.RESULT_OK) {
                    processCatalogFromActivityResult(data);
                }
                break;
        }
    }

    public Pair<Boolean, String> checkWithPreviousNameBeforeCopy(String productNameBeforeCopy) {
        if (nameEditText.getText().toString().equals(productNameBeforeCopy)) {
            return setNameError(nameTextInputLayout.getContext().getString(R.string.product_error_product_name_copy_duplicate));
        }
        return new Pair<>(true, "");
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        if (TextUtils.isEmpty(getName())) {
            return setNameError(nameTextInputLayout.getContext().getString(R.string.product_error_product_name_empty));
        }
        if (categoryId < 0) {
            Snackbar.make(categoryLabelView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_category_empty, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(categoryLabelView.getContext(), R.color.green_400))
                    .show();
            categoryLabelView.getParent().requestChildFocus(categoryLabelView,categoryLabelView);
            return new Pair<>(false,AppEventTracking.AddProduct.FIELDS_MANDATORY_CATEGORY);
        }
        return new Pair<>(true, "");
    }

    @NonNull
    private Pair<Boolean, String> setNameError(String errorMessage) {
        nameTextInputLayout.setError(errorMessage);
        nameTextInputLayout.clearFocus();
        nameTextInputLayout.requestFocus();
        return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_PRODUCT_NAME);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(BUNDLE_CATEGORY_ID, categoryId);
        savedInstanceState.putString(BUNDLE_CATEGORY_NAME, categoryLabelView.getContent());
        savedInstanceState.putBoolean(BUNDLE_CATALOG_SHOWN, catalogLabelView.getVisibility() == View.VISIBLE);
        savedInstanceState.putLong(BUNDLE_CATALOG_ID, catalogId);
        savedInstanceState.putString(BUNDLE_CATALOG_NAME, catalogLabelView.getContent());
        savedInstanceState.putParcelableArrayList(BUNDLE_CAT_RECOMM, categoryPredictionList);
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        categoryId = savedInstanceState.getLong(BUNDLE_CATEGORY_ID, DEFAULT_CATEGORY_ID);
        if (!TextUtils.isEmpty(savedInstanceState.getString(BUNDLE_CATEGORY_NAME))) {
            categoryLabelView.setContent(savedInstanceState.getString(BUNDLE_CATEGORY_NAME));
        }
        setCatalog(savedInstanceState.getLong(BUNDLE_CATALOG_ID, DEFAULT_CATALOG_ID), savedInstanceState.getString(BUNDLE_CATALOG_NAME));
        catalogLabelView.setVisibility(savedInstanceState.getBoolean(BUNDLE_CATALOG_SHOWN, false) ? View.VISIBLE : View.GONE);

        List<ProductCategoryPredictionViewModel> productCategoryPredictionViewModelList = savedInstanceState.getParcelableArrayList(BUNDLE_CAT_RECOMM);
        successGetCategoryRecommData(productCategoryPredictionViewModelList);

        selectRadioByCategoryId((int)categoryId);
    }
}