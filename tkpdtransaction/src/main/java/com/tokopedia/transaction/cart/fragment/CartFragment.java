package com.tokopedia.transaction.cart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.activity.ShipmentCartActivity;
import com.tokopedia.transaction.cart.activity.TopPayActivity;
import com.tokopedia.transaction.cart.adapter.CartItemAdapter;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.presenter.CartPresenter;
import com.tokopedia.transaction.cart.presenter.ICartPresenter;
import com.tokopedia.transaction.cart.receivers.TopPayBroadcastReceiver;
import com.tokopedia.transaction.utils.LinearLayoutManagerNonScroll;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;


/**
 * @author anggaprasetiyo on 11/1/16.
 */
public class CartFragment extends BasePresenterFragment<ICartPresenter> implements ICartView,
        PaymentGatewayFragment.ActionListener, CartItemAdapter.CartItemActionListener,
        TopPayBroadcastReceiver.ActionTopPayListener {

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
    @BindView(R2.id.tv_cash_back_value)
    TextView tvCashBackValue;
    @BindView(R2.id.cv_cash_back)
    CardView cvCashBack;
    @BindView(R2.id.rv_cart)
    RecyclerView rvCart;
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

    private CheckoutData.Builder checkoutDataBuilder;
    private TkpdProgressDialog progressDialogNormal;
    private TopPayBroadcastReceiver topPayBroadcastReceiver;
    private CartItemAdapter cartItemAdapter;

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
        presenter.processGetCartData();
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
        cbUseVoucher.setOnCheckedChangeListener(getOnCheckedUseVoucherOptionListener());
        cbUseVoucher.setChecked(false);
    }

    @Override
    protected void initialVar() {
        topPayBroadcastReceiver = new TopPayBroadcastReceiver(this);
        getActivity().registerReceiver(topPayBroadcastReceiver, new IntentFilter(
                TopPayBroadcastReceiver.ACTION_GET_PARAMETER_TOP_PAY
        ));
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
        cbUseVoucher.setOnCheckedChangeListener(getOnCheckedUseVoucherOptionListener());
        etVoucherCode.addTextChangedListener(getWatcherEtVoucherCode());
    }

    @Override
    public void renderVisiblePotentialCashBack(String cashBack) {
        cvCashBack.setVisibility(View.VISIBLE);
        tvCashBackValue.setText(cashBack);
    }

    @Override
    public void renderInvisiblePotentialCashBack() {
        cvCashBack.setVisibility(View.GONE);
        tvCashBackValue.setText("");
    }

    @Override
    public void renderPaymentGatewayOption(final List<GatewayList> gatewayList) {
        trackingCartPayment();
        btnPaymentMethod.setOnClickListener(getButtonPaymentMethodClickListener(gatewayList));
    }

    @Override
    public void renderVisibleLoyaltyBalance(String loyaltyAmountIDR) {
        tvLoyaltyBalance.setText(loyaltyAmountIDR);
    }

    @Override
    public void renderInvisibleLoyaltyBalance() {
        holderLoyaltyBalance.setVisibility(View.GONE);
    }

    @Override
    public void renderCartListData(final List<CartItem> cartList) {
        rvCart.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
        cartItemAdapter = new CartItemAdapter(this, this);
        cartItemAdapter.fillDataList(cartList);
        rvCart.setAdapter(cartItemAdapter);
        btnCheckout.setOnClickListener(getCheckoutButtonClickListener());
    }

    @Override
    public void setCheckoutCartToken(String token) {
        checkoutDataBuilder.token(token);
    }

    @Override
    public void renderCheckoutCartDepositAmount(String depositAmount) {
        checkoutDataBuilder.depositAmount(depositAmount);
    }

    @Override
    public void renderForceShowPaymentGatewaySelection() {
        btnPaymentMethod.performClick();
    }

    @Override
    public void renderVisibleErrorPaymentCart(@NonNull String messageError) {
        checkoutDataBuilder.errorPayment(true);
        tvErrorPayment.setText(messageError);
        checkoutDataBuilder.errorPaymentMessage(messageError);
        tvErrorPayment.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderInvisibleErrorPaymentCart() {
        checkoutDataBuilder.errorPayment(false);
        checkoutDataBuilder.errorPaymentMessage(null);
        tvErrorPayment.setVisibility(View.GONE);
    }

    @Override
    public void renderSuccessCheckVoucher(String descVoucher) {
        tilEtVoucherCode.setError(null);
        tilEtVoucherCode.setErrorEnabled(false);
        tvVoucherDesc.setText(descVoucher);
    }

    @Override
    public void renderErrorCheckVoucher(String message) {
        tilEtVoucherCode.setErrorEnabled(true);
        tilEtVoucherCode.setError(message);
    }

    @Override
    public void renderErrorEmptyCart() {
        nsvContainer.setVisibility(View.GONE);
        pbMainLoading.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), getString(R.string.label_title_empty_cart),
                getString(R.string.label_sub_title_empty_cart),
                getString(R.string.label_btn_action_empty_cart),
                getRetryEmptyCartClickListener()
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

    @SuppressWarnings("deprecation")
    @Override
    public void renderVisibleTickerGTM(String message) {
        tvTickerGTM.setText(Html.fromHtml(message));
        tvTickerGTM.setVisibility(View.VISIBLE);
        tvTickerGTM.setAutoLinkMask(0);
        Linkify.addLinks(tvTickerGTM, Linkify.WEB_URLS);
    }

    @Override
    public void renderInvisibleTickerGTM() {
        tvTickerGTM.setVisibility(View.GONE);
    }

    @Override
    public List<CartItemEditable> getItemCartListCheckoutData() {
        return cartItemAdapter.getDataList();
    }

    @Override
    public String getVoucherCodeCheckoutData() {
        return etVoucherCode.getText().toString().trim();
    }

    @Override
    public boolean isCheckoutDataUseVoucher() {
        return cbUseVoucher.isChecked();
    }

    @Override
    public void renderDisableErrorCheckVoucher() {
        tilEtVoucherCode.setError(null);
        tilEtVoucherCode.setErrorEnabled(false);
    }

    @Override
    public CheckoutData.Builder getCheckoutDataBuilder() {
        return checkoutDataBuilder;
    }

    @Override
    public String getDepositCheckoutData() {
        return etUseDeposit.getText().toString();
    }

    @Override
    public void renderErrorCartItem(CartItemEditable cartItemEditable) {
        cartItemAdapter.refreshCartItem(cartItemEditable);
    }

    @Override
    public void renderErrorTimeoutInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_timeout_initial_cart_data),
                messageError,
                getString(R.string.label_title_button_retry),
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderErrorNoConnectionInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_no_connection_initial_cart_data),
                messageError,
                getString(R.string.label_title_button_retry),
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderErrorResponseInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_default_initial_cart_data),
                messageError,
                getString(R.string.label_title_button_retry),
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderErrorDefaultInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_default_initial_cart_data),
                messageError,
                getString(R.string.label_title_button_retry),
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getResources().getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            @Nullable TKPDMapParam<String, String> originParams) {
        return originParams == null ? AuthUtil.generateParamsNetwork(getActivity())
                : AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void trackingCartCheckoutEvent() {
        UnifyTracking.eventCartCheckout();
    }

    @Override
    public void trackingCartPayment() {
        UnifyTracking.eventCartPayment();
    }

    @Override
    public void trackingCartDepositEvent() {
        UnifyTracking.eventCartDeposit();
    }

    @Override
    public void trackingCartDropShipperEvent() {
        UnifyTracking.eventCartDropshipper();
    }

    @Override
    public void trackingCartCancelEvent() {
        UnifyTracking.eventATCRemove();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), clazz).putExtras(bundle);
        getActivity().startService(intent);
    }

    @Override
    public void onSelectedPaymentGateway(GatewayList gateway) {
        if (gateway.getGateway() == 0) {
            holderUseDeposit.setVisibility(View.GONE);
            checkoutDataBuilder.usedDeposit("0");
            trackingCartDepositEvent();
        } else {
            holderUseDeposit.setVisibility(View.VISIBLE);
        }
        tvInfoPaymentMethod.setVisibility(View.VISIBLE);
        tvInfoPaymentMethod.setText(gateway.getFeeInformation(getActivity()));
        checkoutDataBuilder.gateway(MessageFormat.format("{0}", gateway.getGateway()));
        ivLogoBtnPaymentMethod.setVisibility(View.VISIBLE);
        ImageHandler.LoadImage(ivLogoBtnPaymentMethod, gateway.getGatewayImage());
        tvInfoPaymentMethod.setText(gateway.getGatewayDesc());
        tvDescBtnPaymentMethod.setText(gateway.getGatewayName());
    }

    @Override
    public void onCancelCartItem(final CartItem data) {
        AlertDialog.Builder alertDialog = generateDialogCancelCart(data);
        showDialog(alertDialog.create());
    }


    @Override
    public void onCancelCartProduct(final CartItem data, final CartProduct cartProduct) {
        AlertDialog.Builder alertDialog = generateDialogCancelCartProduct(data, cartProduct);
        showDialog(alertDialog.create());
    }

    @Override
    public void onChangeShipment(CartItem data) {
        navigateToActivityRequest(ShipmentCartActivity.createInstance(getActivity(), data),
                ShipmentCartActivity.INTENT_REQUEST_CODE);
    }

    @Override
    public void onSubmitEditCartItem(CartItem cartData, List<ProductEditData> cartProductEditDataList) {
        presenter.processSubmitEditCart(cartData, cartProductEditDataList);
    }

    @Override
    public void onUpdateInsuranceCartItem(CartItem cartData, boolean useInsurance) {
        presenter.processUpdateInsurance(cartData, useInsurance);
    }

    @Override
    public void onDropShipperOptionChecked() {
        trackingCartDropShipperEvent();
    }

    @Override
    public void onGetParameterTopPaySuccess(TopPayParameterData data) {
        hideProgressLoading();
        navigateToActivityRequest(
                TopPayActivity.createInstance(getActivity(), data), TopPayActivity.REQUEST_CODE
        );
    }

    @Override
    public void onGetParameterTopPayFailed(String message) {
        hideProgressLoading();
        showToastMessage(message);
    }

    @Override
    public void onGetParameterTopPayNoConnection(String message) {
        hideProgressLoading();
        NetworkErrorHelper.showDialog(getActivity(), getRetryCheckoutClickListener());
    }

    @Override
    public void onGetParameterTopPayOngoing(String message) {
        showProgressLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(topPayBroadcastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TopPayActivity.REQUEST_CODE
                || requestCode == ShipmentCartActivity.INTENT_REQUEST_CODE) {
            presenter.processGetCartData();
        }
    }

    @NonNull
    private AlertDialog.Builder generateDialogCancelCart(final CartItem data) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.title_cancel_confirm));
        alertDialog.setMessage(context.getString(R.string.msg_cancel_1)
                + " " + data.getCartShop().getShopName()
                + " " + context.getString(R.string.msg_cancel_3)
                + " " + data.getCartTotalAmountIdr());
        alertDialog.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        presenter.processCancelCart(data);
                    }
                });
        alertDialog.setNegativeButton(context.getString(R.string.title_no), null);
        return alertDialog;
    }

    @NonNull
    private AlertDialog.Builder generateDialogCancelCartProduct(
            final CartItem cartData, final CartProduct cartProductData) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.title_cancel_confirm));
        alertDialog.setMessage(getString(R.string.msg_cancel_1)
                + " " + cartData.getCartShop().getShopName()
                + " " + getString(R.string.msg_cancel_2)
                + " " + cartProductData.getProductName()
                + " " + getString(R.string.msg_cancel_3)
                + " " + cartProductData.getProductTotalPriceIdr());
        alertDialog.setPositiveButton(context.getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        presenter.processCancelCartProduct(cartData, cartProductData);
                    }
                });
        alertDialog.setNegativeButton(context.getString(R.string.title_no), null);
        return alertDialog;
    }

    @NonNull
    private View.OnClickListener getCheckoutButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingCartCheckoutEvent();
                presenter.processValidationCheckoutData();
            }
        };
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getOnCheckedUseVoucherOptionListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holderUseVoucher.setVisibility(View.VISIBLE);
                    btnCheckVoucher.setOnClickListener(getButtonCheckVoucherClickListener());
                } else {
                    holderUseVoucher.setVisibility(View.GONE);
                    btnCheckVoucher.setOnClickListener(null);
                    etVoucherCode.setText("");
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener getButtonCheckVoucherClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processCheckVoucherCode();
            }
        };
    }


    @NonNull
    private View.OnClickListener getButtonPaymentMethodClickListener(
            final List<GatewayList> gatewayList) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentGatewayFragment dialog = PaymentGatewayFragment.newInstance(gatewayList);
                dialog.setActionListener(CartFragment.this);
                dialog.show(getFragmentManager(), PaymentGatewayFragment.class.getCanonicalName());
            }
        };
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryEmptyCartClickListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                navigateToActivity(
                        BrowseProductRouter.getDefaultBrowseIntent(getActivity())
                );
                getActivity().finish();
            }
        };
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryCheckoutClickListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processValidationCheckoutData();
            }
        };
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryErrorInitialCartInfoClickListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processGetCartData();
            }
        };
    }


    @NonNull
    private TextWatcher getWatcherEtVoucherCode() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    renderErrorCheckVoucher(
                            getString(R.string.label_error_form_voucher_code_empty)
                    );
                } else {
                    renderDisableErrorCheckVoucher();
                }
            }
        };
    }
}
