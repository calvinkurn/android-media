package com.tokopedia.transaction.checkout.view.view.cartlist;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.CartListComponent;
import com.tokopedia.transaction.checkout.di.component.DaggerCartListComponent;
import com.tokopedia.transaction.checkout.di.module.CartListModule;
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartFragment extends BasePresenterFragment implements CartListAdapter.ActionListener,
        ICartListView, TopAdsItemClickListener, RefreshHandler.OnRefreshHandlerListener {

    @BindView(R2.id.rv_cart)
    RecyclerView cartRecyclerView;
    @BindView(R2.id.go_to_courier_page_button)
    TextView btnToShipment;

    @BindView(R2.id.tv_item_count)
    TextView tvItemCount;
    @BindView(R2.id.tv_total_prices)
    TextView tvTotalPrice;
    @BindView(R2.id.bottom_layout)
    View bottomLayout;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    CartListAdapter cartListAdapter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;

    private RefreshHandler refreshHandler;

    private boolean mIsMenuVisible = true;

    private OnPassingCartDataListener mDataPasserListener;
    private CartPromoSuggestion cartPromoSuggestionData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDataPasserListener = (OnPassingCartDataListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnPassingCartDataListener");
        }
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        CartListComponent cartListComponent = DaggerCartListComponent.builder()
                .cartListModule(new CartListModule(this))
                .build();
        cartListComponent.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_cart_remove).setVisible(mIsMenuVisible);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_new;
    }

    @Override
    protected void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(cartListAdapter);
        cartRecyclerView.addItemDecoration(cartItemDecoration);
    }

    @Override
    protected void setViewListener() {
        btnToShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartListAdapter.notifyDataSetChanged();
                dPresenter.processToShipmentStep();
            }
        });
    }

    @Override
    protected void initialVar() {
        setHasOptionsMenu(true);
        getActivity().setTitle("Keranjang");
        refreshHandler.startRefresh();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position) {
        ArrayList<CartItemData> cartItemData =
                new ArrayList<>(Collections.singletonList(cartItemHolderData.getCartItemData()));
        ArrayList<CartItemData> emptyList = new ArrayList<>(Collections.<CartItemData>emptyList());
        showDeleteCartItemDialog(cartItemData, emptyList);
    }

    @Override
    public void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position) {
        cartListAdapter.increaseQuantity(position);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
    }

    @Override
    public void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position) {
        cartListAdapter.decreaseQuantity(position);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
    }

    @Override
    public void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark) {

    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion data, int position) {

    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {

    }

    @Override
    public void onCartItemListIsEmpty() {
        renderEmptyCartData();
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        Intent intent;
        if (true) {
            intent = LoyaltyActivity.newInstanceCouponActive(
                    getActivity(), "marketplace", "marketplace"
            );
        } else {
            intent = LoyaltyActivity.newInstanceCouponNotActive(getActivity(),
                    "marketplace", "marketplace");
        }
        startActivityForResult(intent, LoyaltyActivity.LOYALTY_REQUEST_CODE);
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        cartItemPromoHolderData.setPromoNotActive();
        cartListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartItemPromoHolderData, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartItemPromoHolderData, int position) {

    }

    @Override
    public void onCartItemQuantityFormEdited() {
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return originParams == null ? com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity())
                : com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void closeView() {

    }

    @Override
    public void renderCartListData(List<CartItemData> cartItemDataList) {
        refreshHandler.finishRefresh();

        cartListAdapter.addDataList(cartItemDataList);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
        mDataPasserListener.onPassingCartData(cartItemDataList);

        if (!mIsMenuVisible && !cartItemDataList.isEmpty()) {
            mIsMenuVisible = true;
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void renderErrorGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderErrorHttpGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderErrorNoConnectionGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderErrorTimeoutConnectionGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderEmptyCartData() {
        refreshHandler.finishRefresh();
        bottomLayout.setVisibility(View.GONE);
        mIsMenuVisible = false;
        getActivity().invalidateOptionsMenu();

        CartBadgeNotificationReceiver.resetBadgeCart(getActivity());

        View rootview = getView();
        try {
            rootview.findViewById(com.tokopedia.core.R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            View emptyState = LayoutInflater.from(context).
                    inflate(R.layout.layout_empty_shopping_cart_new, (ViewGroup) rootview);
            TextView shop = emptyState.findViewById(R.id.btn_shopping_now);
            shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToActivity(
                            BrowseProductRouter.getSearchProductIntent(getActivity())
                    );
                    getActivity().finish();
                }
            });
            TopAdsParams params = new TopAdsParams();
            params.getParam().put(
                    TopAdsParams.KEY_SRC,
                    com.tokopedia.transaction.cart.fragment.CartFragment.TOPADS_CART_SRC);

            Config config = new Config.Builder()
                    .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                    .setUserId(SessionHandler.getLoginID(getActivity()))
                    .withPreferedCategory()
                    .setEndpoint(Endpoint.PRODUCT)
                    .displayMode(DisplayMode.FEED)
                    .topAdsParams(params)
                    .build();

            TopAdsView topAdsView = emptyState.findViewById(R.id.topads);
            topAdsView.setConfig(config);
            topAdsView.setDisplayMode(DisplayMode.FEED);
            topAdsView.setMaxItems(4);
            topAdsView.setAdsItemClickListener(this);
            topAdsView.loadTopAds();
        }
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }


    @Override
    public List<CartItemHolderData> getFinalCartList() {
        return cartListAdapter.getDataList();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void renderDetailInfoSubTotal(String qty, String subtotalPrice) {
        tvItemCount.setText(MessageFormat.format("Harga Barang ({0} Item) : ", qty));
        tvTotalPrice.setText(subtotalPrice);
    }

    @Override
    public void renderPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestionData = cartPromoSuggestion;
        if (cartPromoSuggestion.isVisible()) {
            cartListAdapter.addPromoSuggestion(cartPromoSuggestion);
        }
    }

    @Override
    public void renderSuccessDeleteCart(CartItemData cartItemData, String message, boolean addWishList) {
        cartListAdapter.deleteItem(cartItemData);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
        mDataPasserListener.onPassingCartData(cartListAdapter.getCartItemDataList());
    }

    @Override
    public void renderPromoVoucher() {
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoNotActive();
        cartListAdapter.addPromoVoucherData(cartItemPromoHolderData);
    }

    @Override
    public void showToastMessageRed(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderUpdateDataSuccess(String message) {
        dPresenter.processToShipmentStep();
    }

    @Override
    public void renderUpdateDataFailed(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderUpdateAndRefreshCartDataSuccess(String message) {

    }

    @Override
    public void renderLoadGetCartData() {
        bottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void renderLoadGetCartDataFinish() {
        bottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public CartPromoSuggestion getCartPromoSuggestion() {
     return   this.cartPromoSuggestionData;
    }

    void showDeleteCartItemDialog(ArrayList<CartItemData> cartItemDataList, ArrayList<CartItemData> emptyData) {
        DialogFragment dialog = CartRemoveItemDialog.newInstance(cartItemDataList, emptyData,
                new CartRemoveItemDialog.CartItemRemoveCallbackAction() {
                    @Override
                    public void onDeleteSingleItemClicked(CartItemData removedCartItem, List<CartItemData> updatedCartItems) {
                        dPresenter.processDeleteCart(removedCartItem, false);
                    }

                    @Override
                    public void onDeleteSingleItemWithWishListClicked(CartItemData removedCartItem, List<CartItemData> updatedCartItems) {
                        dPresenter.processDeleteCart(removedCartItem, true);
                    }

                    @Override
                    public void onDeleteMultipleItemClicked(List<CartItemData> removedCartItems, List<CartItemData> updatedCartItems) {

                    }

                    @Override
                    public void onDeleteMultipleItemWithWishListClicked(List<CartItemData> removedCartItems, List<CartItemData> updatedCartItems) {

                    }
                });

        dialog.show(getFragmentManager(), "dialog");
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onProductItemClicked(Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data shopData) {
        //TODO: this listener not used in this sprint
    }

    @Override
    public void onRefresh(View view) {
        cartListAdapter.resetData();
        dPresenter.processGetCartData();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoyaltyActivity.LOYALTY_REQUEST_CODE) {
            if (resultCode == LoyaltyActivity.VOUCHER_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String voucherCode = bundle.getString(LoyaltyActivity.VOUCHER_CODE, "");
                    String voucherMessage = bundle.getString(LoyaltyActivity.VOUCHER_MESSAGE, "");
                    long voucherDiscountAmount = bundle.getLong(LoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);

                    CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
                    cartItemPromoHolderData.setPromoVoucherType(voucherCode, voucherMessage, voucherDiscountAmount);

                    cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
                }
            } else if (resultCode == LoyaltyActivity.COUPON_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String couponTitle = bundle.getString(LoyaltyActivity.COUPON_TITLE, "");
                    String couponMessage = bundle.getString(LoyaltyActivity.COUPON_MESSAGE, "");
                    String couponCode = bundle.getString(LoyaltyActivity.COUPON_CODE, "");
                    long couponDiscountAmount = bundle.getLong(LoyaltyActivity.COUPON_DISCOUNT_AMOUNT);

                    CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
                    cartItemPromoHolderData.setPromoCouponType(couponTitle, couponCode, couponMessage, couponDiscountAmount);

                    cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
                }
            }
        }
    }

    public interface OnPassingCartDataListener {

        /**
         * Pass data from cart fragment into its container activity
         *
         * @param cartItemData List of cart items
         */
        void onPassingCartData(List<CartItemData> cartItemData);
    }

}
