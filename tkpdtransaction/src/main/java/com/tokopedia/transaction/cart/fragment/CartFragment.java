package com.tokopedia.transaction.cart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.activity.ShipmentCartActivity;
import com.tokopedia.transaction.cart.adapter.CartItemAdapter;
import com.tokopedia.transaction.cart.listener.ICartActionFragment;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;
import com.tokopedia.transaction.cart.presenter.CartPresenter;
import com.tokopedia.transaction.cart.presenter.ICartPresenter;
import com.tokopedia.transaction.cart.receivers.CartBroadcastReceiver;
import com.tokopedia.transaction.utils.LinearLayoutManagerNonScroll;

import java.util.List;

import butterknife.BindView;


/**
 * @author anggaprasetiyo on 11/1/16.
 */

public class CartFragment extends BasePresenterFragment<ICartPresenter> implements ICartView,
        PaymentGatewayFragment.ActionListener, CartItemAdapter.CartAction,
        CartBroadcastReceiver.ActionTopPayListener {


    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;
    @BindView(R2.id.tv_error_payment)
    TextView tvErrorPayment;
    @BindView(R2.id.tv_total_payment)
    TextView tvTotalPayment;
    @BindView(R2.id.nsv_container)
    NestedScrollView nsvContainer;
    @BindView(R2.id.tv_deposit_tokopedia)
    TextView tvDepositTokopedia;
    @BindView(R2.id.iv_logo_btn_payment_method)
    ImageView ivLogoBtnPaymentMethod;
    @BindView(R2.id.tv_desc_btn_payment_method)
    TextView tvDescBtnPaymentMethod;
    @BindView(R2.id.btn_payment_method)
    RelativeLayout btnPaymentMethod;
    @BindView(R2.id.tv_info_payment_method)
    TextView tvInfoPaymentMethod;
    @BindView(R2.id.btn_checkout)
    TextView btnCheckout;
    @BindView(R2.id.cb_use_voucher)
    CheckBox cbUseVoucher;
    @BindView(R2.id.btn_check_voucher)
    TextView btnCheckVoucher;
    @BindView(R2.id.et_voucher_code)
    EditText etVoucherCode;
    @BindView(R2.id.til_et_voucher_code)
    TextInputLayout tilEtVoucherCode;
    @BindView(R2.id.tv_voucher_desc)
    TextView tvVoucherDesc;
    @BindView(R2.id.holder_use_voucher)
    RelativeLayout holderUseVoucher;
    @BindView(R2.id.cv_payment_selection)
    CardView cvPaymentSelection;
    @BindView(R2.id.tv_cash_back_value)
    TextView tvCashBackValue;
    @BindView(R2.id.cv_cash_back)
    CardView cvCashBack;
    @BindView(R2.id.rv_cart)
    RecyclerView rvCart;
    @BindView(R2.id.holder_container)
    LinearLayout holderContainer;
    @BindView(R2.id.tv_loyalty_balance)
    TextView tvLoyaltyBalance;
    @BindView(R2.id.holder_loyalty_balance)
    LinearLayout holderLoyaltyBalance;
    @BindView(R2.id.et_use_deposit)
    EditText etUseDeposit;
    @BindView(R2.id.holder_use_deposit)
    LinearLayout holderUseDeposit;
    @BindView(R2.id.tv_ticker_gtm)
    TextView tvTickerGTM;

    private ICartActionFragment actionListener;
    private CheckoutData.Builder checkoutDataBuilder;
    private TkpdProgressDialog progressDialogNormal;
    private CartBroadcastReceiver cartBroadcastReceiver;

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
        return R.layout.fragment_cart_tx_module;
    }

    @Override
    protected void initView(View view) {
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        cbUseVoucher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) holderUseVoucher.setVisibility(View.VISIBLE);
                else holderUseVoucher.setVisibility(View.GONE);
            }
        });
        cbUseVoucher.setChecked(false);
    }

    @Override
    protected void initialVar() {
        cartBroadcastReceiver = new CartBroadcastReceiver(this);
        getActivity().registerReceiver(
                cartBroadcastReceiver, new IntentFilter(CartBroadcastReceiver.ACTION_TOP_PAY)
        );
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        this.startActivity(intent);
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
        View rootView = getView();
        if (rootView != null) Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void closeView() {
        getActivity().finish();
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
    public void renderButtonCheckVoucherListener() {
        cbUseVoucher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holderUseVoucher.setVisibility(View.VISIBLE);
                    btnCheckVoucher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.processCheckVoucherCode(
                                    getActivity(), etVoucherCode.getText().toString().trim()
                            );
                        }
                    });
                } else {
                    holderUseVoucher.setVisibility(View.GONE);
                    btnCheckVoucher.setOnClickListener(null);
                    etVoucherCode.setText("");
                }
            }
        });

    }

    @Override
    public void renderVisiblePotentialCashBack(String cashBack) {
        cvCashBack.setVisibility(View.VISIBLE);
        tvCashBackValue.setText(cashBack);
    }

    @Override
    public void renderGonePotentialCashBack() {
        cvCashBack.setVisibility(View.GONE);
        tvCashBackValue.setText("");
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
    public void renderCartListData(final List<TransactionList> cartList) {
        rvCart.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
        final CartItemAdapter adapter = new CartItemAdapter(this, this);
        adapter.fillDataList(cartList);
        rvCart.setAdapter(adapter);
        btnCheckout.setOnClickListener(getCheckoutButtonClickListener(adapter));
    }

    @Override
    public void renderCheckoutCartToken(String token) {
        checkoutDataBuilder.token(token);
    }

    @Override
    public void renderCheckoutCartDepositAmount(String depositAmount) {
        checkoutDataBuilder.depositAmount(depositAmount);
    }

    @Override
    public void renderErrorPaymentCart(boolean isError, String messageError) {
        checkoutDataBuilder.errorPayment(isError);
        checkoutDataBuilder.errorPaymentMessage(messageError);
        if (isError) {
            tvErrorPayment.setText(messageError);
            tvErrorPayment.setVisibility(View.VISIBLE);
        } else {
            tvErrorPayment.setVisibility(View.GONE);
        }

    }

    @Override
    public void renderSuccessVoucherChecked(String messageSuccess, VoucherData data) {
        tilEtVoucherCode.setError(null);
        tilEtVoucherCode.setErrorEnabled(false);
        tvVoucherDesc.setText(
                data.getVoucher().getVoucherAmount().equals("0")
                        ? data.getVoucher().getVoucherPromoDesc()
                        : String.format("Anda mendapatkan voucher\nRp.%s,-",
                        data.getVoucher().getVoucherAmountIdr())
        );
    }

    @Override
    public void showAlertDialogInfo(String messageSuccess) {

    }

    @Override
    public void renderErrorCheckVoucher(String message) {
        tilEtVoucherCode.setErrorEnabled(true);
        tilEtVoucherCode.setError(message);
    }

    @Override
    public void renderEmptyCart() {
        nsvContainer.setVisibility(View.GONE);
        pbMainLoading.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                "Keranjang belanja Anda Kosong.",
                "Yuk belanja di situs resmi jual beli online, aman dan nyaman.",
                "CARI SEKARANG",
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        navigateToActivity(
                                BrowseProductRouter.getDefaultBrowseIntent(getActivity())
                        );
                        getActivity().finish();
                    }
                }
        );
    }

    @Override
    public void renderVisibleMainCartContainer() {
        nsvContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
        presenter.processGetTickerGTM();
    }

    @Override
    public void renderInitialLoadingCartInfo() {
        nsvContainer.setVisibility(View.GONE);
        pbMainLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderVisibleTickerGTM(String message) {
        tvTickerGTM.setText(Html.fromHtml(message));
        tvTickerGTM.setVisibility(View.VISIBLE);
        tvTickerGTM.setAutoLinkMask(0);
        Linkify.addLinks(tvTickerGTM, Linkify.WEB_URLS);
    }

    @Override
    public void renderGoneTickerGTM() {
        tvTickerGTM.setVisibility(View.GONE);
    }

    @Override
    public void onSelectedPaymentGateway(GatewayList gateway) {
        if (gateway.getGateway() == 0) {
            holderUseDeposit.setVisibility(View.GONE);
            checkoutDataBuilder.usedDeposit("0");
        } else {
            holderUseDeposit.setVisibility(View.VISIBLE);
        }
        checkoutDataBuilder.gateway(gateway.getGateway() + "");
        ImageHandler.LoadImage(ivLogoBtnPaymentMethod, gateway.getGatewayImage());
        tvInfoPaymentMethod.setText(gateway.getGatewayDesc());
        tvDescBtnPaymentMethod.setText(gateway.getGatewayName());
    }

    @Override
    public void onCancelCart(final TransactionList data) {
        AlertDialog.Builder alertDialog = generateDialogCancelCart(data);
        showDialog(alertDialog.create());
    }


    @Override
    public void onCancelCartProduct(final TransactionList data, final CartProduct cartProduct) {
        AlertDialog.Builder alertDialog = generateDialogCancelCartProduct(data, cartProduct);
        showDialog(alertDialog.create());
    }

    @Override
    public void onChangeShipment(TransactionList data) {
        navigateToActivityRequest(ShipmentCartActivity.createInstance(getActivity(), data),
                ShipmentCartActivity.INTENT_REQUEST_CODE);
    }

    @Override
    public void onSubmitEditCart(TransactionList cartData, List<ProductEditData> cartProductEditDataList) {
        presenter.processSubmitEditCart(getActivity(), cartData, cartProductEditDataList);
    }

    @Override
    public void onUpdateInsuranceCart(TransactionList cartData, boolean useInsurance) {
        presenter.processUpdateInsurance(getActivity(), cartData, useInsurance);
    }

    @NonNull
    private AlertDialog.Builder generateDialogCancelCart(final TransactionList data) {
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
        alertDialog.setNegativeButton(context.getString(R.string.title_no), null);
        return alertDialog;
    }

    @NonNull
    private AlertDialog.Builder generateDialogCancelCartProduct(
            final TransactionList cartData, final CartProduct cartProductData) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.title_cancel_confirm));
        alertDialog.setMessage(getString(R.string.msg_cancel_1)
                + " "
                + cartData.getCartShop().getShopName()
                + " "
                + getString(R.string.msg_cancel_2)
                + " "
                + cartProductData.getProductName()
                + " "
                + getString(R.string.msg_cancel_3)
                + " "
                + cartProductData.getProductTotalPriceIdr());
        alertDialog.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        presenter.processCancelCartProduct(
                                getActivity(), cartData, cartProductData
                        );
                    }
                });

        alertDialog.setNegativeButton(context.getString(R.string.title_no), null);
        return alertDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(cartBroadcastReceiver);
    }

    @Override
    public void onGetParameterTopPaySuccess(TopPayParameterData data) {
        hideProgressLoading();
        showToastMessage("berhasil!!!!");
        actionListener.replaceFragmentWithBackStack(TopPayFragment.newInstance(data));
    }

    @Override
    public void onGetParameterTopPayFailed(String message) {
        hideProgressLoading();
        showToastMessage(message);
    }

    @Override
    public void onGetParameterTopPayNoConnection(String message) {
        hideProgressLoading();
        showToastMessage(message);
    }

    @Override
    public void onGetParameterTopPayOngoing(String message) {
        showProgressLoading();
    }

    @NonNull
    private View.OnClickListener getCheckoutButtonClickListener(final CartItemAdapter adapter) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartItemEditable> cartItemEditables = adapter.getDataList();
                boolean canBeCheckout = true;
                int positionError = cartItemEditables.size() - 1;

                if (cbUseVoucher.isChecked() && etVoucherCode.getText().toString().isEmpty()) {
                    tilEtVoucherCode.setErrorEnabled(true);
                    tilEtVoucherCode.setError("Voucher Kosong");
                    return;
                } else if (cbUseVoucher.isChecked()
                        && !etVoucherCode.getText().toString().isEmpty()) {
                    tilEtVoucherCode.setError(null);
                    tilEtVoucherCode.setErrorEnabled(false);
                    checkoutDataBuilder.voucherCode(etVoucherCode.getText().toString());
                }

                checkoutDataBuilder.usedDeposit(
                        etUseDeposit.getText().toString().replaceAll("\\D+", "")
                );

                for (int i = 0, cartItemEditablesSize = cartItemEditables.size();
                     i < cartItemEditablesSize; i++) {
                    CartItemEditable cartItemEditable = cartItemEditables.get(i);
                    adapter.renderErrorCart(cartItemEditable);
                    if (cartItemEditable.finalizeAllData().getErrorType()
                            != CartItemEditable.ERROR_NON) {
                        canBeCheckout = false;
                    }
                }
                if (!canBeCheckout) {
                    int heightScroll = nsvContainer.computeVerticalScrollRange();
                    int heightRv = rvCart.computeVerticalScrollOffset();
                    nsvContainer.smoothScrollTo(0, heightScroll
                            - (heightRv - (heightRv / cartItemEditables.size() * positionError)));
                    showToastMessage("Keranjang tidak dapat diproses," +
                            " mohon periksa kembali keranjang Anda.");
                } else {
                    presenter.processCheckoutCart(getActivity(), checkoutDataBuilder,
                            cartItemEditables);
                }
            }
        };
    }


}
