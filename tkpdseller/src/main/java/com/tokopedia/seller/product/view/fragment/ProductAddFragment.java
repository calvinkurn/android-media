package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.holder.ProductAdditionalInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.view.holder.ProductInfoViewHolder;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddFragment extends BaseDaggerFragment {

    public static final String TAG = ProductAddFragment.class.getSimpleName();

    private ProductInfoViewHolder productInfoViewHolder;
    private ProductImageViewHolder productImageViewHolder;
    private ProductDetailViewHolder productDetailViewHolder;
    private ProductAdditionalInfoViewHolder productAdditionalInfoViewHolder;

    public static ProductAddFragment createInstance() {
        ProductAddFragment fragment = new ProductAddFragment();
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_add, container, false);
        productInfoViewHolder = new ProductInfoViewHolder(view);
        productImageViewHolder = new ProductImageViewHolder((AppCompatActivity)getActivity(), view);
        productDetailViewHolder = new ProductDetailViewHolder(view);
        productAdditionalInfoViewHolder = new ProductAdditionalInfoViewHolder(view);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}