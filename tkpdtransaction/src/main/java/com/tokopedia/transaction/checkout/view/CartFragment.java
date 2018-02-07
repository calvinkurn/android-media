package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.CartListComponent;
import com.tokopedia.transaction.checkout.di.component.DaggerCartListComponent;
import com.tokopedia.transaction.checkout.di.module.CartListModule;
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.presenter.ICartListPresenter;
import com.tokopedia.transaction.checkout.view.view.ICartListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartFragment extends BasePresenterFragment implements
        CartListAdapter.ActionListener, ICartListView {
    @BindView(R2.id.rv_cart)
    RecyclerView cartRecyclerView;
    @BindView(R2.id.go_to_courier_page_button)
    TextView btnToShipment;


    @BindView(R2.id.tv_item_count)
    TextView tvItemCount;
    @BindView(R2.id.tv_total_prices)
    TextView tvTotalPrice;

    @Inject
    ICartListPresenter dPresenter;
    @Inject
    CartListAdapter cartListAdapter;
    @Inject
    RecyclerView.ItemDecoration cartItemDecoration;

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

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
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
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(cartListAdapter);
        cartRecyclerView.addItemDecoration(cartItemDecoration);
    }

    @Override
    protected void setViewListener() {
        dPresenter.processGetCartData();
        btnToShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPresenter.processToShipmentStep();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position) {

    }

    @Override
    public void onCartItemActionRemarkClicked(CartItemHolderData cartItemHolderData, int position) {
        cartListAdapter.updateEditableRemark(position);
    }

    @Override
    public void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark) {
        cartListAdapter.updateRemark(position, remark);
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
        cartListAdapter.addDataList(cartItemDataList);
    }

    @Override
    public void renderErrorGetCartListData(String message) {

    }

    @Override
    public void renderErrorHttpGetCartListData(String message) {

    }

    @Override
    public void renderErrorNoConnectionGetCartListData(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionGetCartListData(String message) {

    }

    @Override
    public void renderEmptyCartData() {

    }

    @Override
    public void disableSwipeRefresh() {

    }

    @Override
    public void enableSwipeRefresh() {

    }

    @Override
    public List<CartItemHolderData> getFinalCartList() {
        return cartListAdapter.getDataList();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }
}
