package com.tokopedia.seller.shop.open.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.activity.ShopOpenMandatoryActivity;


public class ShopOpenReserveDomainSuccessFragment extends Fragment{
    public static final String TAG = ShopOpenReserveDomainSuccessFragment.class.getSimpleName();

    public static final String ARG_SHOP_NAME = "shop_nm";
    private String shopName;

    public static ShopOpenReserveDomainSuccessFragment newInstance(String shopName) {

        Bundle args = new Bundle();

        ShopOpenReserveDomainSuccessFragment fragment = new ShopOpenReserveDomainSuccessFragment();
        args.putString(ARG_SHOP_NAME, shopName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopName = getArguments().getString(ARG_SHOP_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_reserve_domain_success, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(MethodChecker.fromHtml(getString(R.string.shop_reserve_success_title,shopName)));
        View buttonNext = view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopOpenMandatoryActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}