package com.tokopedia.seller.product.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.ProductCategoryPrediction;
import com.tokopedia.seller.product.view.activity.CatalogPickerActivity;
import com.tokopedia.seller.product.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductInfoViewHolder {

    public static final int REQUEST_CODE_CATEGORY = 101;
    public static final int REQUEST_CODE_CATALOG = 102;
    private final RadioGroup radioGroupCategoryRecomm;
    private EditText nameEditText;
    private LabelView categoryLabelView;
    private LabelView catalogLabelView;
    private View categoryRecommView;
    private ProductInfoViewHolder.Listener listener;
    private int categoryId;
    private int catalogId = -1;

    public ProductInfoViewHolder(View view) {
        categoryRecommView = view.findViewById(R.id.view_group_category_recomm);
        radioGroupCategoryRecomm = (RadioGroup) categoryRecommView.findViewById(R.id.radio_group_category_recomm);
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
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() >= 2) {
                    if (listener != null) {
                        listener.onProductNameChanged(s.toString());
                    }
                }
            }
        });
        radioGroupCategoryRecomm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
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
                ProductCategoryPrediction productCategoryPrediction = (ProductCategoryPrediction) radioButton.getTag();
                if (productCategoryPrediction == null) {
                    categoryLabelView.resetContentText();
                    return;
                }
                processCategory(productCategoryPrediction.getCategoryName());
            }
        });
    }

    public void setListener(ProductInfoViewHolder.Listener listener) {
        this.listener = listener;
    }

    public String getName() {
        return nameEditText.getText().toString();
    }

    public void setName(String name) {
        nameEditText.setText(name);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductInfoViewHolder.REQUEST_CODE_CATEGORY:
                if (resultCode == Activity.RESULT_OK) {
                    processCategory(data);
                }
                break;
            case ProductInfoViewHolder.REQUEST_CODE_CATALOG:
                if (resultCode == Activity.RESULT_OK) {
                    processCatalog(data);
                }
                break;
        }
    }

    private void processCatalog(Intent intent) {
        catalogId = intent.getIntExtra(CatalogPickerActivity.CATALOG_ID, 0);
        String catalogName = intent.getStringExtra(CatalogPickerActivity.CATALOG_NAME);
        catalogLabelView.setContent(catalogName);
    }

    private void processCategory(Intent data) {
        List<CategoryViewModel> listCategory = Parcels.unwrap(data.getParcelableExtra(CategoryPickerActivity.CATEGORY_RESULT_LEVEL));
        String category = "";
        int previousCategoryId = categoryId;
        for (int i = 0; i < listCategory.size(); i++) {
            CategoryViewModel viewModel = listCategory.get(i);
            categoryId = viewModel.getId();
            category += viewModel.getName();
            if (i < listCategory.size() - 1) {
                category += "\n";
            }
        }
        if (previousCategoryId != categoryId) {
            if (categoryId <= 0) {
                hideAndClearCatalog();
            }
            if (listener != null) {
                listener.onCategoryChanged(categoryId);
            }
        }
        categoryLabelView.setContent(category);
    }

    private void processCategory(String[] categoryNameArr) {
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

    public void successFetchCatalogData(List<Catalog> catalogViewModelList, int maxRows) {
        if (catalogViewModelList == null || catalogViewModelList.size() == 0) {
            hideAndClearCatalog();
        } else {
            catalogId = -1;
            catalogLabelView.setVisibility(View.VISIBLE);
        }
    }

    private void hideAndClearCategoryRecomm() {
        categoryRecommView.setVisibility(View.GONE);
    }

    public void hideAndClearCatalog() {
        catalogLabelView.setVisibility(View.GONE);
        catalogId = -1;
    }

    public void showCatRecommError() {
        hideAndClearCategoryRecomm();
    }

    public void successGetCategoryRecommData(List<ProductCategoryPrediction> categoryPredictionList) {
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
            radioButton.setTag(categoryPredictionList.get(i));
        }
        categoryRecommView.setVisibility(View.VISIBLE);
    }

    public interface Listener {
        void onCategoryPickerClicked(int categoryId);

        void onCatalogPickerClicked(String keyword, int depId, int selectedCatalogId);

        void onProductNameChanged(String productName);

        void onCategoryChanged(int categoryId);
    }

}