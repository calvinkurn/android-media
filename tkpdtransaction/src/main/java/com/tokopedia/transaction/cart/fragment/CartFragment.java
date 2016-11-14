package com.tokopedia.transaction.cart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.cart.adapter.CartItemAdapter;
import com.tokopedia.transaction.cart.listener.ICartActionFragment;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.CheckoutData;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;
import com.tokopedia.transaction.cart.presenter.CartPresenter;
import com.tokopedia.transaction.cart.presenter.ICartPresenter;

import java.util.List;

import butterknife.Bind;

/**
 * @author anggaprasetiyo on 11/1/16.
 */

public class CartFragment extends BasePresenterFragment<ICartPresenter> implements ICartView,
        PaymentGatewayFragment.ActionListener, CartItemAdapter.CartAction {

    @Bind(R2.id.logo)
    ProgressBar logo;
    @Bind(R2.id.layout_root)
    FrameLayout layoutRoot;
    @Bind(R2.id.tv_error_payment)
    TextView tvErrorPayment;
    @Bind(R2.id.tv_total_payment)
    TextView tvTotalPayment;
    @Bind(R2.id.tv_deposit_tokopedia)
    TextView tvDepositTokopedia;
    @Bind(R2.id.iv_logo_btn_payment_method)
    ImageView ivLogoBtnPaymentMethod;
    @Bind(R2.id.tv_desc_btn_payment_method)
    TextView tvDescBtnPaymentMethod;
    @Bind(R2.id.btn_payment_method)
    RelativeLayout btnPaymentMethod;
    @Bind(R2.id.tv_info_payment_method)
    TextView tvInfoPaymentMethod;
    @Bind(R2.id.btn_checkout)
    TextView btnCheckout;
    @Bind(R2.id.cb_use_voucher)
    CheckBox cbUseVoucher;
    @Bind(R2.id.btn_check_voucher)
    TextView btnCheckVoucher;
    @Bind(R2.id.et_voucher_code)
    EditText etVoucherCode;
    @Bind(R2.id.til_et_voucher_code)
    TextInputLayout tilEtVoucherCode;
    @Bind(R2.id.tv_voucher_desc)
    TextView tvVoucherDesc;
    @Bind(R2.id.holder_use_voucher)
    RelativeLayout holderUseVoucher;
    @Bind(R2.id.cv_payment_selection)
    CardView cvPaymentSelection;
    @Bind(R2.id.tv_cash_back_value)
    TextView tvCashBackValue;
    @Bind(R2.id.cv_cash_back)
    CardView cvCashBack;
    @Bind(R2.id.rv_cart)
    RecyclerView rvCart;
    @Bind(R2.id.holder_container)
    LinearLayout holderContainer;
    @Bind(R2.id.no_res_img)
    ImageView noResImg;
    @Bind(R2.id.message_error_1)
    TextView messageError1;
    @Bind(R2.id.message_error_2)
    TextView messageError2;
    @Bind(R2.id.find_now)
    TextView findNow;
    @Bind(R2.id.include_no_result)
    LinearLayout includeNoResult;
    @Bind(R2.id.tv_loyalty_balance)
    TextView tvLoyaltyBalance;
    @Bind(R2.id.holder_loyalty_balance)
    LinearLayout holderLoyaltyBalance;

    private ICartActionFragment actionListener;
    private CheckoutData.Builder checkoutDataBuilder;
    private TkpdProgressDialog progressDialogNormal;

    public static Fragment newInstance() {
        return new CartFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        checkoutDataBuilder = new CheckoutData.Builder();
        presenter.processGetCartData(getActivity());
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
        presenter = new CartPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ICartActionFragment) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_revamp;
    }

    @Override
    protected void initView(View view) {
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

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

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void closeView() {

    }

    @Override
    public void renderDepositInfo(String depositIdr) {
        tvDepositTokopedia.setText(depositIdr);
    }

    @Override
    public void renderTotalPayment(String grandTotalWithoutLPIDR) {
        tvTotalPayment.setText(grandTotalWithoutLPIDR);
    }

    @Override
    public void renderPaymentGatewayOption(final List<GatewayList> gatewayList) {
        btnPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentGatewayFragment dialog = PaymentGatewayFragment.newInstance(gatewayList);
                dialog.setActionListener(CartFragment.this);
                dialog.show(getFragmentManager(), PaymentGatewayFragment.class.getCanonicalName());
            }
        });
    }

    @Override
    public void renderLoyaltyBalance(String lpAmountIdr, boolean visibleHolder) {
        holderLoyaltyBalance.setVisibility(visibleHolder ? View.VISIBLE : View.GONE);
        tvLoyaltyBalance.setText(lpAmountIdr);
    }

    @Override
    public void renderCartListData(List<TransactionList> transactionLists) {
        rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        CartItemAdapter adapter = new CartItemAdapter(this, this);
        adapter.fillDataList(transactionLists);
        rvCart.setAdapter(adapter);
    }

    @Override
    public void onSelectedPaymentGateway(GatewayList gateway) {
        checkoutDataBuilder.gateway(gateway.getGateway() + "");
        ImageHandler.LoadImage(ivLogoBtnPaymentMethod, gateway.getGatewayImage());
        tvInfoPaymentMethod.setText(gateway.getGatewayDesc());
        tvDescBtnPaymentMethod.setText(gateway.getGatewayName());
    }

    @Override
    public void onCancelCart(final TransactionList data) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.title_cancel_confirm));
        alertDialog.setMessage(context.getString(R.string.msg_cancel_1)
                + " "
                + data.getCartShop().getShopName()
                + " "
                + context.getString(R.string.msg_cancel_3)
                + " "
                + data.getCartTotalAmountIdr());
        alertDialog.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        presenter.processCancelCart(getActivity(), data);
                    }
                });
        alertDialog.setNegativeButton(context.getString(R.string.title_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        showDialog(alertDialog.create());
    }
}
