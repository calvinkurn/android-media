package com.tokopedia.seller.product.variant.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantPickerActivity;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantMainFragment extends BaseDaggerFragment implements ProductVariantMainView {

    private static final int REQUEST_CODE_VARIANT_LEVEL_ONE = 1;
    private static final int REQUEST_CODE_VARIANT_LEVEL_TWO = 2;

    private LabelView variantLevelOneLabelView;
    private LabelView variantLevelTwoLabelView;

    public static ProductVariantMainFragment newInstance() {
        Bundle args = new Bundle();
        ProductVariantMainFragment fragment = new ProductVariantMainFragment();
        fragment.setArguments(args);
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
        variantLevelOneLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_one);
        variantLevelTwoLabelView = (LabelView) view.findViewById(R.id.label_view_variant_level_two);
        variantLevelOneLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVariantLevelOne();
            }
        });
        variantLevelTwoLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    protected void initInjector() {

    }

    private void pickVariantLevelOne() {
        Intent intent = new Intent(getActivity(), ProductVariantPickerActivity.class);
        ArrayList<ProductVariantViewModel> modelList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
            productVariantViewModel.setId(i);
            productVariantViewModel.setTitle(UUID.randomUUID().toString() + " - " + String.valueOf(i));
            if (i % 2 == 0) {
                productVariantViewModel.setHexCode("#b74747");
            } else {
                productVariantViewModel.setImageUrl("https://image.flaticon.com/teams/slug/freepik.jpg");
            }
            modelList.add(productVariantViewModel);
        }
        intent.putExtra(ProductVariantPickerActivity.EXTRA_INTENT_PICKER_ITEM_LIST, modelList);
        getActivity().startActivityForResult(intent, REQUEST_CODE_VARIANT_LEVEL_ONE);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}