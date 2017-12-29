package com.tokopedia.seller.shop.open.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;


public class ShopOpenCreateSuccessFragment extends BasePresenterFragment{


    public static ShopOpenCreateSuccessFragment newInstance() {
        return new ShopOpenCreateSuccessFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_create_success, container, false);

        return view;
    }

}