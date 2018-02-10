package com.tokopedia.shop.info.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.R;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.view.listener.ShopInfoView;
import com.tokopedia.shop.info.view.presenter.ShopInfoPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopInfoFragment extends BaseDaggerFragment implements ShopInfoView {

    public static ShopInfoFragment createInstance(String shopId) {
        ShopInfoFragment shopInfoFragment = new ShopInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopInfoFragment.setArguments(bundle);
        return shopInfoFragment;
    }

    private LabelView transactionSuccessLabelView;
    private LabelView totalTransactionLabelView;
    private LabelView productSoldLabelView;
    private LabelView totalReviewLabelView;
    private LabelView favoriteLabelView;
    private LabelView lastOnlineLabelView;
    private LabelView openSinceLabelView;
    private LabelView totalProductLabelView;
    private LabelView totalEtalaseLabelView;

    private LabelView physicalShopLabelView;
    private LabelView shopOwnerLabelView;

    @Inject
    ShopInfoPresenter shopInfoDetailPresenter;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopInfoDetailPresenter.attachView(this);
        shopInfoDetailPresenter.getShopInfo(shopId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_info, container, false);
        transactionSuccessLabelView = view.findViewById(R.id.label_view_transaction_success);
        totalTransactionLabelView = view.findViewById(R.id.label_view_total_transaction);
        productSoldLabelView = view.findViewById(R.id.label_view_product_sold);
        totalReviewLabelView = view.findViewById(R.id.label_view_total_review);
        favoriteLabelView = view.findViewById(R.id.label_view_favorite);
        lastOnlineLabelView = view.findViewById(R.id.label_view_last_online);
        openSinceLabelView = view.findViewById(R.id.label_view_open_since);
        totalProductLabelView = view.findViewById(R.id.label_view_total_product);
        totalEtalaseLabelView = view.findViewById(R.id.label_view_total_etalase);

        physicalShopLabelView = view.findViewById(R.id.label_view_physical_shop);
        shopOwnerLabelView = view.findViewById(R.id.label_view_shop_owner);

        return view;
    }

    @Override
    public void onSuccessGetShopInfo(ShopInfo shopInfo) {
        transactionSuccessLabelView.setContent(shopInfo.getShopTxStats().getShopTxSuccessRate1Year());
        totalTransactionLabelView.setContent(shopInfo.getStats().getShopTotalTransaction());
        productSoldLabelView.setContent(shopInfo.getStats().getShopItemSold());
//        totalReviewLabelView.setContent(shopInfo.getStats().rev);
        favoriteLabelView.setContent(String.valueOf(shopInfo.getStats().getFavoriteCount()));
        lastOnlineLabelView.setContent(shopInfo.getInfo().getShopOwnerLastLogin());
        openSinceLabelView.setContent(shopInfo.getInfo().getShopOpenSince());
        totalProductLabelView.setContent(shopInfo.getStats().getShopTotalProduct());
        totalEtalaseLabelView.setContent(shopInfo.getStats().getShopTotalEtalase());
        String physicalAddressContent = getString(R.string.shop_info_physical_shop_location_only_online);
        if (shopInfo.getAddress().size() > 0) {
            physicalAddressContent = getString(R.string.shop_info_physical_shop_location_count, shopInfo.getAddress().size());
        }
        physicalShopLabelView.setContent(physicalAddressContent);
        shopOwnerLabelView.setTitle(shopInfo.getOwner().getOwnerName());
        ImageHandler.loadImageRounded2(shopOwnerLabelView.getImageView().getContext(), shopOwnerLabelView.getImageView(), shopInfo.getOwner().getOwnerImage());
        physicalShopLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        shopOwnerLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {

    }

    @Override
    protected void initInjector() {
        DaggerShopInfoComponent
                .builder()
                .shopInfoModule(new ShopInfoModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopInfoDetailPresenter != null) {
            shopInfoDetailPresenter.detachView();
        }
    }
}
