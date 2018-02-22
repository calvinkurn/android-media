package com.tokopedia.transaction.cart.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
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
import com.tokopedia.transaction.addtocart.utils.KeroppiConstants;
import com.tokopedia.transaction.cart.activity.ShipmentCartActivity;
import com.tokopedia.transaction.cart.adapter.CartItemAdapter;
import com.tokopedia.transaction.cart.listener.ICartView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartCourierPrices;
import com.tokopedia.transaction.cart.model.cartdata.CartDonation;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.CartPromo;
import com.tokopedia.transaction.cart.model.cartdata.CartShop;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.presenter.CartPresenter;
import com.tokopedia.transaction.cart.presenter.ICartPresenter;
import com.tokopedia.transaction.cart.receivers.TopPayBroadcastReceiver;
import com.tokopedia.transaction.insurance.view.InsuranceTnCActivity;
import com.tokopedia.transaction.utils.LinearLayoutManagerNonScroll;
import com.tokopedia.transaction.utils.ValueConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author anggaprasetiyo on 11/1/16.
 */
public class CartFragment extends BasePresenterFragment<ICartPresenter> implements ICartView,
        PaymentGatewayFragment.ActionListener, CartItemAdapter.CartItemActionListener,
        TopPayBroadcastReceiver.ActionListener, TopAdsItemClickListener {
    private static final String ANALYTICS_GATEWAY_PAYMENT_FAILED = "payment failed";

    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;
    @BindView(R2.id.tv_error_1)
    TextView tvError1;
    @BindView(R2.id.tv_error_2)
    TextView tvError2;
    @BindView(R2.id.holder_error)
    LinearLayout holderError;
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
    @BindView(R2.id.tv_cash_back_value)
    TextView tvCashBackValue;
    @BindView(R2.id.cv_cash_back)
    CardView cvCashBack;
    @BindView(R2.id.rv_cart)
    RecyclerView rvCart;
    @BindView(R2.id.tv_loyalty_balance)
    TextView tvLoyaltyBalance;
    @BindView(R2.id.cart_loyalty_point_balance)
    TextView LoyaltyPoint;
    @BindView(R2.id.holder_loyalty_balance)
    LinearLayout holderLoyaltyBalance;
    @BindView(R2.id.et_use_deposit)
    EditText etUseDeposit;
    @BindView(R2.id.holder_use_deposit)
    LinearLayout holderUseDeposit;
    @BindView(R2.id.tv_ticker_gtm)
    TextView tvTickerGTM;
    @BindView(R2.id.donasi_checkbox)
    CheckBox donasiCheckbox;
    @BindView(R2.id.donasi_title)
    TextView donasiTitle;
    @BindView(R2.id.donasi_info)
    ImageView donasiInfo;
    @BindView(R2.id.total_payment_loading)
    ProgressBar totalPaymentLoading;
    @BindView(R2.id.instant_insert_voucher_text_view)
    TextView instantInsertVoucherTextView;
    @BindView(R2.id.instant_insert_voucher_button)
    TextView instantInsertVoucherButton;
    @BindView(R2.id.instant_promo_placeholder)
    CardView instantPromoPlaceHolder;
    @BindView(R2.id.tv_insurance_terms)
    TextView tvInsuranceTerms;
    @BindView(R2.id.promo_code_layout)
    ViewGroup promoCodeLayout;
    @BindView(R2.id.promo_result)
    ViewGroup promoResultLayout;
    @BindView(R2.id.promo_activation_title)
    TextView promoActivationTitle;
    @BindView(R2.id.label_promo_type)
    TextView labelPromoType;
    @BindView(R2.id.voucher_code)
    TextView promoVoucherCode;
    @BindView(R2.id.voucher_description)
    TextView voucherDescription;
    @BindView(R2.id.cancel_promo_layout)
    ViewGroup cancelPromoLayout;

    private CheckoutData.Builder checkoutDataBuilder;
    private TkpdProgressDialog progressDialogNormal;
    private TopPayBroadcastReceiver topPayBroadcastReceiver;
    private CartItemAdapter cartItemAdapter;

    private String voucherCode;
    private String totalPaymentWithoutLoyaltyIdr;
    private String totalLoyaltyBalance;
    private String totalLoyaltyPoint;
    private String donationValue;

    private boolean hasLogisticInsurance;
    private boolean hasPromotion;
    private final String TOPADS_CART_SRC = "empty_cart";

    public static Fragment newInstance() {
        return new CartFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
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
        presenter = new CartPresenter(
                this,
                new LocalCacheHandler(getActivity(), TkpdCache.NOTIFICATION_DATA)
        );
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
        Spannable tosAgreementText = formatInsuranceTacText();
        tvInsuranceTerms.setText(tosAgreementText);

        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        stopNestedScrollingView();
    }

    @NonNull
    private Spannable formatInsuranceTacText() {
        String formatText = getString(R.string.text_tos_agreement);
        String messageTosAgreement = getString(R.string.message_tos_agreement);
        int startSpan = messageTosAgreement.indexOf(formatText);
        int endSpan = messageTosAgreement.indexOf(formatText) + formatText.length();
        Spannable tosAgreementText = new SpannableString(messageTosAgreement);
        int color = ContextCompat.getColor(context, R.color.tkpd_green_header);
        tosAgreementText.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tosAgreementText.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tosAgreementText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                getActivity().startActivity(new Intent(getActivity(), InsuranceTnCActivity.class));
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInsuranceTerms.setMovementMethod(LinkMovementMethod.getInstance());
        return tosAgreementText;
    }

    @Override
    protected void setViewListener() {
        rvCart.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
    }

    @Override
    protected void initialVar() {
        topPayBroadcastReceiver = new TopPayBroadcastReceiver(this);
        getActivity().registerReceiver(topPayBroadcastReceiver, new IntentFilter(
                TopPayBroadcastReceiver.ACTION_TOP_PAY
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
    public void renderTotalPaymentWithLoyalty(String totalPaymentWithLoyaltyIdr) {

    }

    @Override
    public void renderTotalPaymentWithoutLoyalty(String grandTotalWithoutLPIDR) {
        this.totalPaymentWithoutLoyaltyIdr = grandTotalWithoutLPIDR;
    }

    @Override
    public void renderButtonCheckVoucherListener() {
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
    public void renderVisibleLoyaltyBalance(String loyaltyAmountIDR, String loyaltyPoint) {
        this.totalLoyaltyBalance = loyaltyAmountIDR;
        this.totalLoyaltyPoint = loyaltyPoint;
        tvLoyaltyBalance.setText("(" + loyaltyAmountIDR + ")");
        LoyaltyPoint.setText(": " + loyaltyPoint + " " + pluralizeGrammar("point", loyaltyPoint));
        holderLoyaltyBalance.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderInvisibleLoyaltyBalance() {
        holderLoyaltyBalance.setVisibility(View.GONE);
    }

    @Override
    public void renderCartListData(String keroToken, String ut, final List<CartItem> cartList) {
        cartItemAdapter = new CartItemAdapter(this, this);
        totalPaymentLoading.setVisibility(View.VISIBLE);
        cartItemAdapter.fillDataList(keroToken, cartList);
        presenter.processCartRates(keroToken, ut, cartList);
        rvCart.setAdapter(cartItemAdapter);
        btnCheckout.setOnClickListener(getCheckoutButtonClickListener());
    }

    private void setInsuranceTermsVisibility(CartCourierPrices cartCourierPrices) {
        if (cartCourierPrices.getUseInsurance() != 0) {
            if (!hasLogisticInsurance &&
                    (cartCourierPrices.getInsuranceMode() == KeroppiConstants.InsuranceType.MUST ||
                            cartCourierPrices.getInsuranceMode() == KeroppiConstants.InsuranceType.OPTIONAL)) {
                if (cartCourierPrices.getInsuranceUsedType() == KeroppiConstants.InsuranceUsedType.TOKOPEDIA_INSURANCE) {
                    tvInsuranceTerms.setVisibility(View.VISIBLE);
                } else if (cartCourierPrices.getInsuranceUsedType() == KeroppiConstants.InsuranceUsedType.LOGISTIC_INSURANCE) {
                    tvInsuranceTerms.setVisibility(View.GONE);
                    hasLogisticInsurance = true;
                } else {
                    tvInsuranceTerms.setVisibility(View.GONE);
                }
            } else {
                tvInsuranceTerms.setVisibility(View.GONE);
            }
        } else {
            tvInsuranceTerms.setVisibility(View.GONE);
        }

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
    public void renderVisibleErrorPaymentCart(
            @NonNull String messageError1, @NonNull String messageError2
    ) {
        checkoutDataBuilder.errorPayment(true);
        holderError.setVisibility(View.VISIBLE);
        tvError1.setText(messageError1);
        tvError2.setText(messageError2);
        checkoutDataBuilder.errorPaymentMessage(messageError1 + " , " + messageError2);
    }

    @Override
    public void renderInvisibleErrorPaymentCart() {
        checkoutDataBuilder.errorPayment(false);
        checkoutDataBuilder.errorPaymentMessage(null);
        holderError.setVisibility(View.GONE);
    }

    @Override
    public void renderSuccessCheckVoucher(String voucherCode,
                                          String amount,
                                          String descVoucher,
                                          int instantVoucher) {
        setVoucherResultLayout(voucherCode, amount, descVoucher);
        //TODO Important
    }

    @Override
    public void renderErrorCheckVoucher(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorFromInstantVoucher(int instantVoucher) {
        if (instantVoucher == 1) instantPromoPlaceHolder.setVisibility(View.GONE);
    }

    @Override
    public void renderErrorEmptyCart() {
        tvTickerGTM.setVisibility(View.GONE);
        nsvContainer.setVisibility(View.GONE);
        pbMainLoading.setVisibility(View.GONE);
        CartBadgeNotificationReceiver.resetBadgeCart(getActivity());

        View rootview = getView();
        try {
            rootview.findViewById(com.tokopedia.core.R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            View emptyState = LayoutInflater.from(context).
                    inflate(R.layout.layout_empty_shopping_chart, (ViewGroup) rootview);
            Button shop = (Button) emptyState.findViewById(R.id.shoping);
            shop.setOnClickListener(getRetryEmptyCartClickListener());
            TopAdsParams params = new TopAdsParams();
            params.getParam().put(TopAdsParams.KEY_SRC, TOPADS_CART_SRC);

            Config config = new Config.Builder()
                    .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                    .setUserId(SessionHandler.getLoginID(getActivity()))
                    .withPreferedCategory()
                    .setEndpoint(Endpoint.PRODUCT)
                    .displayMode(DisplayMode.FEED)
                    .topAdsParams(params)
                    .build();

            TopAdsView topAdsView = (TopAdsView) emptyState.findViewById(R.id.topads);
            topAdsView.setConfig(config);
            topAdsView.setDisplayMode(DisplayMode.FEED);
            topAdsView.setMaxItems(4);
            topAdsView.setAdsItemClickListener(this);
            topAdsView.loadTopAds();
        }
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
    public void renderVisibleMainCartContainer() {
        nsvContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
        tvTotalPayment.setVisibility(View.GONE);
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
        //TODO IMPORTANT here is where the data is fetched
        return voucherCode;
    }

    @Override
    public boolean isCheckoutDataUseVoucher() {
        //TODO IMPORTANT boolean that fetched tells if using voucher or not
        return promoResultLayout.isShown();
    }

    @Override
    public String getDonationValue() {
        return donationValue;
    }

    @Override
    public void renderDisableErrorCheckVoucher() {
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
                getString(R.string.label_title_button_retry), 0,
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderErrorNoConnectionInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_no_connection_initial_cart_data),
                getString(R.string.label_transaction_error_message_try_again),
                getString(R.string.label_title_button_retry), 0,
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderErrorResponseInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_default_initial_cart_data),
                messageError,
                getString(R.string.label_title_button_retry), 0,
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderErrorDefaultInitialCartInfo(String messageError) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_default_initial_cart_data),
                messageError,
                getString(R.string.label_title_button_retry), 0,
                getRetryErrorInitialCartInfoClickListener()
        );
    }

    @Override
    public void renderCheckboxDonasi(CartDonation donation) {
        setDonationValue("0");
        donasiTitle.setText(donation.getDonationNoteTitle());
        donasiCheckbox.setText(donation.getDonationNoteInfo());
        donasiCheckbox.setOnCheckedChangeListener(getOnCheckedDonasiListener(donation.getDonationValue()));
        donasiInfo.setOnClickListener(getDonasiInfoListener(donation));
    }

    private View.OnClickListener insertCode(final String voucherCode) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Change to new voucher stuffs

                CartFragment.this.voucherCode = voucherCode;
                presenter.processCheckVoucherCode(voucherCode, 1);
                cancelPromoLayout.setOnClickListener(onInstantPromoCancelled());
            }
        };
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
    public LocalCacheHandler getLocalCacheHandlerNotificationData() {
        return new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
    }

    @Override
    public void setCartSubTotal(CartCourierPrices cartCourierPrices) {
        cartItemAdapter.setRates(cartCourierPrices);
        setInsuranceTermsVisibility(cartCourierPrices);
    }

    @Override
    public void setCartError(int cartIndex) {
        cartItemAdapter.setCartItemError(cartIndex,
                getActivity().getString(R.string.label_title_error_default_initial_cart_data),
                getActivity().getString(R.string.error_cannot_send_to_destination));
        holderError.setVisibility(View.VISIBLE);
        tvError1.setText(getActivity().getString(R.string.label_title_error_default_initial_cart_data));
        tvError2.setText(getActivity().getString(R.string.error_check_cart));
    }

    @Override
    public void showRatesCompletion() {
        refreshCartList();
    }

    @Override
    public void setCartNoGrandTotal() {
        totalPaymentLoading.setVisibility(View.GONE);
    }

    @Override
    public void refreshCartList() {
        int grandTotal = 0;
        cartItemAdapter.notifyDataSetChanged();
        for (int i = 0; i < cartItemAdapter.getDataList().size(); i++) {
            if (cartItemAdapter.getDataList().get(i).getCartCourierPrices() != null) {
                grandTotal += cartItemAdapter
                        .getDataList().get(i).getCartCourierPrices().getCartSubtotal();
            }
        }
        if (grandTotal < 1)
            tvTotalPayment.setVisibility(View.GONE);
        else {
            tvTotalPayment.setVisibility(View.VISIBLE);
            tvTotalPayment.setText(ValueConverter.getStringIdrFormat(grandTotal));
        }
        totalPaymentLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderInstantPromo(CartPromo cartPromo) {
        if (cartPromo.isVisible() == 1) {
            hasPromotion = true;
            instantPromoPlaceHolder.setVisibility(View.VISIBLE);
            instantInsertVoucherTextView
                    .setText(Html.fromHtml(cartPromo.getPromoText()));
            instantInsertVoucherButton.setText(cartPromo.getCtaText());
            instantInsertVoucherButton.setTextColor(Color.parseColor(cartPromo.getCtaColor()));
            instantInsertVoucherButton.setOnClickListener(insertCode(cartPromo.getPromoCode()));
        }
    }

    @Override
    public void renderPromoView(final boolean isCouponActive) {
        if (isCouponActive) promoActivationTitle.setText(R.string.title_use_promo_code_and_voucher);
        else promoActivationTitle.setText(R.string.title_use_promo_code);

        promoCodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if (isCouponActive) {
                    intent = LoyaltyActivity.newInstanceCouponActive(
                            getActivity(), "marketplace", "marketplace"
                    );
                } else intent = LoyaltyActivity.newInstanceCouponNotActive(getActivity(),
                        "marketplace", "marketplace");
                startActivityForResult(intent, LoyaltyActivity.LOYALTY_REQUEST_CODE);
            }
        });
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
    public void onUpdateInsuranceCartItem(CartItemEditable cartItemEditable, boolean useInsurance) {
        presenter.processUpdateInsurance(cartItemEditable, useInsurance);
    }

    @Override
    public void onCartProductDetailClicked(ProductPass productPass) {
        startActivity(ProductDetailRouter.createInstanceProductDetailInfoActivity(
                getActivity(), productPass
        ));
    }

    @Override
    public void onShopDetailInfoClicked(CartShop cartShop) {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(cartShop.getShopId(), ""));
        navigateToActivity(intent);
    }

    @Override
    public void onDropShipperOptionChecked() {
        trackingCartDropShipperEvent();
    }

    @Override
    public void onGetParameterTopPaySuccess(TopPayParameterData data) {
        hideProgressLoading();
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(data.getRedirectUrl());
        paymentPassData.setTransactionId(data.getParameter().getTransactionId());
        paymentPassData.setPaymentId(data.getParameter().getTransactionId());
        paymentPassData.setCallbackSuccessUrl(data.getCallbackUrl());
        paymentPassData.setCallbackFailedUrl(data.getCallbackUrl());
        paymentPassData.setQueryString(data.getQueryString());
        navigateToActivityRequest
                (TopPayActivity.createInstance(getActivity(), paymentPassData),
                        TopPayActivity.REQUEST_CODE
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
    public void onGetThanksTopPaySuccess(ThanksTopPayData data) {
        presenter.clearNotificationCart();
        try {
            presenter.processPaymentAnalytics(
                    new LocalCacheHandler(getActivity(), TkpdCache.NOTIFICATION_DATA), data
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideProgressLoading();
        showToastMessage(getString(R.string.message_payment_succeded_transaction_module));
        navigateToActivity(TransactionPurchaseRouter.createIntentTxSummary(getActivity()));
        CartBadgeNotificationReceiver.resetBadgeCart(getActivity());
        closeView();
    }

    @Override
    public void onGetThanksTopPayFailed(String message, final String paymentId) {
        hideProgressLoading();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        closeView();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void onGetThanksTopPayNotValid(String message, final String paymentId) {
        hideProgressLoading();
        showToastMessage(message);
    }

    @Override
    public void onGetThanksTopPayNoConnection(String message, final String paymentId) {
        hideProgressLoading();
        NetworkErrorHelper.showDialog(getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processValidationPayment(paymentId);
                    }
                });
    }

    @Override
    public void onGetThanksTopPayOngoing(String message, String paymentId) {
        showProgressLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribeObservable();
        getActivity().unregisterReceiver(topPayBroadcastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShipmentCartActivity.INTENT_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            presenter.processGetCartData();
        } else if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_CANCELLED:
                    NetworkErrorHelper.showSnackbar(
                            getActivity(),
                            getString(R.string.alert_payment_canceled_or_failed_transaction_module)
                    );
                    break;
                case TopPayActivity.PAYMENT_SUCCESS:
                    presenter.processValidationPayment(
                            ((PaymentPassData) data.getParcelableExtra(
                                    com.tokopedia.payment.activity
                                            .TopPayActivity.EXTRA_PARAMETER_TOP_PAY_DATA
                            )).getPaymentId()
                    );
                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    presenter.processValidationPayment(
                            ((PaymentPassData) data.getParcelableExtra(
                                    com.tokopedia.payment.activity
                                            .TopPayActivity.EXTRA_PARAMETER_TOP_PAY_DATA
                            )).getPaymentId()
                    );
                    break;
            }
        } else if (requestCode == LoyaltyActivity.LOYALTY_REQUEST_CODE) {
            if (resultCode == LoyaltyActivity.VOUCHER_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                setVoucherResultLayout(
                        bundle.getString(LoyaltyActivity.VOUCHER_CODE, ""),
                        bundle.getString(LoyaltyActivity.VOUCHER_AMOUNT, ""),
                        bundle.getString(LoyaltyActivity.VOUCHER_MESSAGE, "")
                );
                cancelPromoLayout.setOnClickListener(onPromoCancelled());
            } else if (resultCode == LoyaltyActivity.COUPON_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                promoResultLayout.setVisibility(View.VISIBLE);
                labelPromoType.setText(getString(R.string.title_coupon_code) + " : ");
                promoVoucherCode.setText(bundle.getString(LoyaltyActivity.COUPON_TITLE, ""));
                voucherDescription.setText(bundle.getString(LoyaltyActivity.COUPON_MESSAGE, ""));

                //TODO check state
                voucherCode = bundle.getString(LoyaltyActivity.COUPON_CODE);
                instantPromoPlaceHolder.setVisibility(View.GONE);
                promoCodeLayout.setVisibility(View.GONE);
                cancelPromoLayout.setOnClickListener(onPromoCancelled());
            }
        }
    }

    private void setVoucherResultLayout(String voucherCode,
                                        String amount,
                                        String description) {
        promoResultLayout.setVisibility(View.VISIBLE);
        labelPromoType.setText(getString(R.string.title_promo_code) + " : ");
        promoVoucherCode.setText(voucherCode);
        voucherDescription.setText(description);

        //TODO check state
        this.voucherCode = voucherCode;
        promoCodeLayout.setVisibility(View.GONE);
        instantPromoPlaceHolder.setVisibility(View.GONE);
    }

    private View.OnClickListener onPromoCancelled() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoResultLayout.setVisibility(View.GONE);
                promoCodeLayout.setVisibility(View.VISIBLE);
            }
        };
    }

    private View.OnClickListener onInstantPromoCancelled() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoResultLayout.setVisibility(View.GONE);
                instantPromoPlaceHolder.setVisibility(View.VISIBLE);
                promoCodeLayout.setVisibility(View.VISIBLE);
            }
        };
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
    private View.OnClickListener getRetryEmptyCartClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToActivity(
                        BrowseProductRouter.getSearchProductIntent(getActivity())
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

    public void setDonationValue(String donationValue) {
        this.donationValue = donationValue;
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getOnCheckedDonasiListener(final String donationValue) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDonationValue(isChecked ? donationValue : "0");
            }
        };
    }

    @NonNull
    private View.OnClickListener getDonasiInfoListener(final CartDonation donation) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(
                        getActivity()).inflate(R.layout.dialog_donasi_info, null
                );
                dialog.setContentView(view);
                setDataDialog(dialog, view, donation);
                dialog.show();
            }
        };
    }

    private void setDataDialog(final Dialog dialog, View view, CartDonation donation) {
        TextView title = ButterKnife.findById(view, R.id.donasi_popup_title);
        ImageView imgDonation = ButterKnife.findById(view, R.id.donasi_popup_img);
        TextView content = ButterKnife.findById(view, R.id.donasi_popup_content);
        ImageView closeDialog = ButterKnife.findById(view, R.id.close_popup_donasi);

        Glide.with(getActivity())
                .load(donation.getDonationPopupImg())
                .into(imgDonation);
        title.setText(donation.getDonationPopupTitle());
        content.setText(donation.getDonationPopupInfo());
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private String pluralizeGrammar(String noun, String amount) {
        int convertedAmount;
        try {
            convertedAmount = Integer.parseInt(amount);
        } catch (Exception e) {
            convertedAmount = 0;
        }
        if (convertedAmount > 1) {
            return noun + "s";
        }
        return noun;
    }

    private void stopNestedScrollingView() {
        nsvContainer.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        nsvContainer.setFocusable(true);
        nsvContainer.setFocusableInTouchMode(true);
        nsvContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.requestFocusFromTouch();
                return false;
            }
        });
    }

    private String getStringIdrFormat(int value) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(value);
    }

    @Override
    public void setListnerCancelPromoLayoutOnAutoApplyCode(){
        cancelPromoLayout.setOnClickListener(onPromoCancelled());

    }
}