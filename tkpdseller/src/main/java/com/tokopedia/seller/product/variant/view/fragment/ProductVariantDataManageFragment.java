package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDataManageFragment extends Fragment {

    public static class SelectedVariantData{
        private long variantDataId;
        private String variantName;
        boolean isSelected;
    }
    public static ProductVariantDataManageFragment newInstance() {
        ProductVariantDataManageFragment fragment = new ProductVariantDataManageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_main, container, false);
        return view;
    }

}