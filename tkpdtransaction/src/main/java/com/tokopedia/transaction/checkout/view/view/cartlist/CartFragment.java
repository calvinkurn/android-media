package com.tokopedia.transaction.checkout.view.view.cartlist;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
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
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.di.component.CartListComponent;
import com.tokopedia.transaction.checkout.view.di.component.DaggerCartListComponent;
import com.tokopedia.transaction.checkout.view.di.module.CartListModule;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFormActivity;
import com.tokopedia.transaction.checkout.view.view.shipmentform.CartShipmentActivity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS;

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
    private TkpdProgressDialog progressDialogNormal;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    CartListAdapter cartListAdapter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;

    private RefreshHandler refreshHandler;

    private boolean mIsMenuVisible = true;

    private ActionListener mDataPasserListener;
    private CartListData cartListData;
    private PromoCodeAppliedData promoCodeAppliedData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDataPasserListener = (ActionListener) activity;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cart_remove) {
            mDataPasserListener.onRemoveAllCartMenuClicked(cartListAdapter.getCartItemDataList());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_checkout_cart_remove, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(cartListAdapter);
        cartRecyclerView.addItemDecoration(cartItemDecoration);
    }

    @Override
    protected void setViewListener() {
        btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener());
    }

    @NonNull
    private View.OnClickListener getOnClickButtonToShipmentListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartListAdapter.notifyDataSetChanged();
                cartListAdapter.checkForShipmentForm();
                dPresenter.processToShipmentSingleAddress();
            }
        };
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
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivity(((ICartCheckoutModuleRouter) getActivity().getApplication()).tkpdCartCheckoutGetProductDetailIntent(
                    getActivity(),
                    ProductPass.Builder.aProductPass()
                            .setProductId(cartItemHolderData.getCartItemData().getOriginData().getProductId())
                            .setProductImage(cartItemHolderData.getCartItemData().getOriginData().getProductImage())
                            .setProductName(cartItemHolderData.getCartItemData().getOriginData().getProductName())
                            .setProductPrice(cartItemHolderData.getCartItemData().getOriginData().getPriceFormatted())
                            .build()
            ));
        }
    }

    @Override
    public void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position) {
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivity(((ICartCheckoutModuleRouter) getActivity().getApplication()).tkpdCartCheckoutGetShopInfoIntent(
                    getActivity(),
                    cartItemHolderData.getCartItemData().getOriginData().getShopId()
            ));
        }
    }

    @Override
    public void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark) {

    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion data, int position) {
        dPresenter.processCheckPromoCodeFromSuggestedPromo(data.getPromoCode());
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {
        data.setVisible(false);
        cartListAdapter.notifyItemChanged(position);
        cartListAdapter.checkForShipmentForm();
    }

    @Override
    public void onCartItemListIsEmpty() {
        renderEmptyCartData();
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivityForResult(
                    ((ICartCheckoutModuleRouter) getActivity().getApplication())
                            .tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartListIntent(
                                    getActivity(), cartListData.isPromoCouponActive()
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        cartItemPromoHolderData.setPromoNotActive();
        cartListAdapter.notifyItemChanged(position);
        cartListAdapter.updateSuggestionPromo();
        cartListAdapter.checkForShipmentForm();
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
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {
        showDeleteCartItemDialog(getCartDataList(), new ArrayList<CartItemData>());
    }

    @Override
    public void onCartDataEnableToCheckout() {
        btnToShipment.setBackgroundResource(R.drawable.medium_green_button_rounded);
        btnToShipment.setTextColor(getResources().getColor(R.color.white));
        btnToShipment.setOnClickListener(getOnClickButtonToShipmentListener());
    }

    @Override
    public void onCartDataDisableToCheckout() {
        btnToShipment.setBackgroundResource(R.drawable.bg_grey_button_rounded);
        btnToShipment.setTextColor(getResources().getColor(R.color.grey_500));
        btnToShipment.setOnClickListener(null);
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
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialogNormal.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return originParams == null
                ? com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity())
                : com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void closeView() {

    }

    @Override
    public void renderInitialGetCartListDataSuccess(CartListData cartListData) {
        refreshHandler.finishRefresh();
        this.cartListData = cartListData;
        cartListAdapter.resetData();
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoNotActive();
        cartListAdapter.addPromoVoucherData(cartItemPromoHolderData);
        if (cartListData.isError()) {
            cartListAdapter.addCartTickerError(
                    new CartItemTickerErrorHolderData.Builder()
                            .cartTickerErrorData(cartListData.getCartTickerErrorData())
                            .build()
            );
        }
        if (cartListData.getCartPromoSuggestion().isVisible()) {
            cartListAdapter.addPromoSuggestion(cartListData.getCartPromoSuggestion());
        }
        cartListAdapter.addDataList(cartListData.getCartItemDataList());
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());

        if (!mIsMenuVisible && !cartListData.getCartItemDataList().isEmpty()) {
            mIsMenuVisible = true;
            getActivity().invalidateOptionsMenu();
        }
        cartListAdapter.checkForShipmentForm();
    }

    @Override
    public void renderErrorInitialGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderErrorHttpInitialGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderErrorNoConnectionInitialGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderErrorTimeoutConnectionInitialGetCartListData(String message) {
        refreshHandler.finishRefresh();
    }

    @Override
    public void renderActionDeleteCartDataSuccess(CartItemData cartItemData, String message, boolean addWishList) {
        cartListAdapter.deleteItem(cartItemData);
        dPresenter.reCalculateSubTotal(cartListAdapter.getDataList());
    }

    @Override
    public void renderErrorActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionActionDeleteCartData(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData shipmentAddressFormData) {
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                CartAddressChoiceActivity.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS);
        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void renderToShipmentFormSuccess(CartShipmentAddressFormData shipmentAddressFormData) {
        if (shipmentAddressFormData.isMultiple()) {
            Intent intent = CartShipmentActivity.createInstanceMultipleAddress(
                    getActivity(), shipmentAddressFormData, this.promoCodeAppliedData,
                    this.cartListData.getCartPromoSuggestion()
            );
            startActivityForResult(intent, CartShipmentActivity.REQUEST_CODE);
        } else {
            Intent intent = CartShipmentActivity.createInstanceSingleAddress(
                    getActivity(), shipmentAddressFormData, this.promoCodeAppliedData,
                    this.cartListData.getCartPromoSuggestion()
            );
            startActivityForResult(intent, CartShipmentActivity.REQUEST_CODE);
        }
    }

    @Override
    public void renderErrorToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionToShipmentForm(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderToShipmentMultipleAddressSuccess(CartListData cartListData, RecipientAddressModel selectedAddress) {
        startActivityForResult(MultipleAddressFormActivity.createInstance(
                getActivity(), cartListData, selectedAddress
        ), MultipleAddressFormActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionToShipmentMultipleAddress(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData) {
        this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .build();
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoVoucherType(promoCodeAppliedData.getPromoCode(),
                promoCodeAppliedData.getDescription(), promoCodeAppliedData.getAmount());
        cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
    }

    @Override
    public void renderErrorCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorHttpCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorNoConnectionCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
    }

    @Override
    public void renderErrorTimeoutConnectionCheckPromoCodeFromSuggestedPromo(String message) {
        showToastMessageRed(message);
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
    public List<CartItemData> getCartDataList() {
        return cartListAdapter.getCartItemDataList();
    }

    @Override
    public void renderDetailInfoSubTotal(String qty, String subtotalPrice) {
        tvItemCount.setText(MessageFormat.format("Harga Barang ({0} Item) : ", qty));
        tvTotalPrice.setText(subtotalPrice);
    }


    @Override
    public void renderPromoVoucher() {
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoNotActive();
        cartListAdapter.addPromoVoucherData(cartItemPromoHolderData);
    }

    @Override
    public void showToastMessageRed(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showRedCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderLoadGetCartData() {
        bottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void renderLoadGetCartDataFinish() {
        cartListAdapter.resetData();
        bottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderCartTickerError(CartTickerErrorData cartTickerErrorData) {
        cartListAdapter.addCartTickerError(new CartItemTickerErrorHolderData.Builder()
                .cartTickerErrorData(cartTickerErrorData)
                .build());
    }

    void showDeleteCartItemDialog(List<CartItemData> cartItemDataList, List<CartItemData> emptyData) {
        DialogFragment dialog = CartRemoveItemDialog.newInstance(
                cartItemDataList,
                emptyData,
                getCallbackActionDialogRemoveCart());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(dialog, CartRemoveItemDialog.DIALOG_FRAGMENT_TAG);
        ft.commitAllowingStateLoss();
    }

    @NonNull
    private CartRemoveItemDialog.CartItemRemoveCallbackAction getCallbackActionDialogRemoveCart() {
        return new CartRemoveItemDialog.CartItemRemoveCallbackAction() {
            @Override
            public void onDeleteSingleItemClicked(
                    CartItemData removedCartItem, List<CartItemData> updatedCartItems
            ) {
                dPresenter.processDeleteCart(removedCartItem, false);
            }

            @Override
            public void onDeleteSingleItemWithWishListClicked(
                    CartItemData removedCartItem, List<CartItemData> updatedCartItems
            ) {
                dPresenter.processDeleteCart(removedCartItem, true);
            }

            @Override
            public void onDeleteMultipleItemClicked(
                    List<CartItemData> removedCartItems, List<CartItemData> updatedCartItems
            ) {
                dPresenter.processDeleteAndRefreshCart(removedCartItems, false);
            }

            @Override
            public void onDeleteMultipleItemWithWishListClicked(
                    List<CartItemData> removedCartItems, List<CartItemData> updatedCartItems
            ) {
                dPresenter.processDeleteAndRefreshCart(removedCartItems, true);
            }
        };
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
        dPresenter.processInitialGetCartData();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            onResultFromRequestCodeLoyalty(resultCode, data);
        } else if (requestCode == CartShipmentActivity.REQUEST_CODE) {
            onResultFromRequestCodeCartShipment(resultCode, data);
        } else if (requestCode == MultipleAddressFormActivity.REQUEST_CODE) {
            onResultFromRequestCodeMultipleAddressForm(resultCode);
        } else if (requestCode == CartAddressChoiceActivity.REQUEST_CODE) {
            onResultFromRequestCodeAddressChoiceActivity(resultCode);
        }
    }

    private void onResultFromRequestCodeMultipleAddressForm(int resultCode) {
        if (resultCode == MultipleAddressFormActivity.RESULT_CODE_SUCCESS_SET_SHIPPING
                || resultCode == MultipleAddressFormActivity.RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM) {
            dPresenter.processToShipmentForm();
        }
    }

    private void onResultFromRequestCodeCartShipment(int resultCode, Intent data) {
        if (resultCode == CartShipmentActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM) {
            RecipientAddressModel selectedAddress = data.getParcelableExtra(
                    CartShipmentActivity.EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA
            );
            dPresenter.processToShipmentMultipleAddress(selectedAddress);
        } else if (resultCode == CartShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT ||
                resultCode == CartShipmentActivity.RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT) {
            dPresenter.processResetAndRefreshCartData();
        }
    }

    private void onResultFromRequestCodeLoyalty(int resultCode, Intent data) {
        if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String voucherCode = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, "");
                String voucherMessage = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, "");
                long voucherDiscountAmount = bundle.getLong(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);
                this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                        .promoCode(voucherCode)
                        .description(voucherMessage)
                        .amount((int) voucherDiscountAmount)
                        .build();
                CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
                cartItemPromoHolderData.setPromoVoucherType(voucherCode, voucherMessage, voucherDiscountAmount);

                cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
            }
        } else if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String couponTitle = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, "");
                String couponMessage = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, "");
                String couponCode = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, "");
                long couponDiscountAmount = bundle.getLong(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT);
                this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_COUPON)
                        .promoCode(couponCode)
                        .couponTitle(couponTitle)
                        .description(couponMessage)
                        .amount((int) couponDiscountAmount)
                        .build();
                CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
                cartItemPromoHolderData.setPromoCouponType(
                        couponTitle, couponCode, couponMessage, couponDiscountAmount
                );

                cartListAdapter.updateItemPromoVoucher(cartItemPromoHolderData);
            }
        }
    }

    private void onResultFromRequestCodeAddressChoiceActivity(int resultCode) {
        if (resultCode == RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS) {
            dPresenter.processToShipmentForm();
        }
    }

    public interface ActionListener {

        void onRemoveAllCartMenuClicked(List<CartItemData> cartItemData);
    }

}
