package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.activity.CatalogPickerActivity;
import com.tokopedia.seller.product.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsDashboardFragment;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductInfoViewHolder {

    public static final int REQUEST_CODE_CATEGORY = 101;
    public static final int REQUEST_CODE_CATALOG = 102;

    private EditText nameEditText;
    private LabelView categoryLabelView;
    private LabelView catalogLabelView;

    private Fragment fragment;
    private String name;
    private int categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameEditText.setText(name);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ProductInfoViewHolder(final Fragment fragment, View view) {
        this.fragment = fragment;
        nameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        categoryLabelView = (LabelView) view.findViewById(R.id.label_view_category);
        catalogLabelView = (LabelView) view.findViewById(R.id.label_view_catalog);
        categoryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment.getActivity(), CategoryPickerActivity.class);
                fragment.startActivityForResult(intent, REQUEST_CODE_CATEGORY);
            }
        });
        catalogLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogPickerActivity.start(fragment, fragment.getActivity(), REQUEST_CODE_CATALOG, nameEditText.getText().toString(), categoryId, 0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductInfoViewHolder.REQUEST_CODE_CATEGORY:
                break;
            case ProductInfoViewHolder.REQUEST_CODE_CATALOG:
                break;
        }
    }
}