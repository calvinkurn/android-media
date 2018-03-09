package com.tokopedia.shop.product.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.etalase.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedPromoViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListLimitedPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListLimitedFragment extends BaseSearchListFragment<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory>
        implements ShopProductLimitedPromoViewHolder.PromoViewHolderListener, ShopProductListLimitedView, ShopProductClickedListener {

    @Inject
    ShopProductListLimitedPresenter shopProductListLimitedPresenter;
    private ProgressDialog progressDialog;
    private String shopId;
    private ShopModuleRouter shopModuleRouter;

    public static ShopProductListLimitedFragment createInstance() {
        ShopProductListLimitedFragment fragment = new ShopProductListLimitedFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context.getApplicationContext() instanceof ShopModuleRouter) {
            shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_product_limited_list, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopProductListLimitedPresenter.attachView(this);
    }

    @Override
    public void loadData(int i) {

    }

    @NonNull
    @Override
    protected ShopProductLimitedAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductLimitedAdapterTypeFactory(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShopProductListActivity.createIntent(getActivity(), shopId));
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShopEtalaseActivity.createIntent(getActivity(), shopId, null, true));
            }
        }, this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory> createAdapterInstance() {
        return new ShopProductLimitedAdapter(getAdapterTypeFactory());
    }

    @Override
    protected void initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(new ShopProductModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    public void displayProduct(String shopId, String promotionWebViewUrl, String shopName) {
        this.shopId = shopId;
        shopProductListLimitedPresenter.getProductLimitedList(shopId, promotionWebViewUrl);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListLimitedPresenter != null) {
            shopProductListLimitedPresenter.detachView();
        }
    }

    @Override
    public void onItemClicked(ShopProductBaseViewModel shopProductBaseViewModel) {

    }

    @Override
    public void promoClicked(String url) {
        ShopProductOfficialStoreUtils.overrideUrl(getActivity(), url, shopId);
    }

    @Override
    public void onSearchSubmitted(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        startActivity(ShopProductListActivity.createIntent(
                getActivity(),
                shopId,
                text,
                null,
                null
        ));
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel) {
        if (shopProductViewModel.isWishList()) {
            shopProductListLimitedPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListLimitedPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel) {
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getProductUrl());
    }

    @Override
    public void onErrorRemoveFromWishList(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        ((ShopProductLimitedAdapter) getAdapter()).updateWishListStatus(productId, false);
    }

    @Override
    public void onErrorAddToWishList(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(),e));
    }

    @Override
    public void onSuccessAddToWishList(String productId, Boolean value) {
        ((ShopProductLimitedAdapter) getAdapter()).updateWishListStatus(productId, true);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }
}