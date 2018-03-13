package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.view.di.component.CartShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.component.DaggerCartShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.module.CartShipmentModule;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFragment;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 25/01/18.
 */

public class CartShipmentActivity extends BasePresenterActivity implements ICartShipmentActivity {
    public static final int REQUEST_CODE = 983;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 1;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;

    public static final String EXTRA_SHIPMENT_FORM_DATA = "EXTRA_SHIPMENT_FORM_DATA";
    public static final String EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA = "EXTRA_DEFAULT_ADDRESS_RECIPIENT_DATA";
    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_ADDRESS_SHIPMENT_TYPE = "EXTRA_ADDRESS_SHIPMENT_TYPE";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final int TYPE_ADDRESS_SHIPMENT_SINGLE = 1;
    public static final int TYPE_ADDRESS_SHIPMENT_MULTIPLE = 2;

    private int typeAddressShipment;
    private CartShipmentAddressFormData cartShipmentAddressFormData;
    private CartPromoSuggestion cartPromoSuggestionData;
    private PromoCodeAppliedData promoCodeAppliedData;

    @Inject
    ICartShipmentPresenter cartShipmentPresenter;
    @Inject
    CompositeSubscription compositeSubscription;
    private CheckoutData checkoutData;
    private TkpdProgressDialog progressDialogNormal;


    public static Intent createInstanceSingleAddress(Context context,
                                                     CartShipmentAddressFormData cartShipmentAddressFormData,
                                                     PromoCodeAppliedData promoCodeCartListData,
                                                     CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_SINGLE);
        return intent;
    }

    public static Intent createInstanceMultipleAddress(Context context,
                                                       CartShipmentAddressFormData cartShipmentAddressFormData,
                                                       PromoCodeAppliedData promoCodeCartListData,
                                                       CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_MULTIPLE);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        promoCodeAppliedData = extras.getParcelable(EXTRA_PROMO_CODE_APPLIED_DATA);
        typeAddressShipment = extras.getInt(EXTRA_ADDRESS_SHIPMENT_TYPE);
        cartShipmentAddressFormData = extras.getParcelable(EXTRA_SHIPMENT_FORM_DATA);
        cartPromoSuggestionData = extras.getParcelable(EXTRA_CART_PROMO_SUGGESTION);
    }


    private void initInjector() {
        CartShipmentComponent component = DaggerCartShipmentComponent.builder()
                .cartShipmentModule(new CartShipmentModule(this))
                .build();
        component.inject(this);
    }

    @Override
    protected void initialPresenter() {
        initInjector();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setupToolbar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shipment_cart_tx_module;
    }

    @Override
    protected void initView() {
        progressDialogNormal = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !((fragment instanceof MultipleAddressFragment)
                || (fragment instanceof SingleAddressShipmentFragment))) {
            if (typeAddressShipment == TYPE_ADDRESS_SHIPMENT_SINGLE) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        SingleAddressShipmentFragment.newInstance(
                                cartShipmentAddressFormData, promoCodeAppliedData, cartPromoSuggestionData
                        )).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        MultipleAddressShipmentFragment.newInstance(
                                cartShipmentAddressFormData, promoCodeAppliedData, cartPromoSuggestionData
                        )).commit();
            }
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void renderCheckoutCartSuccess(CheckoutData checkoutData) {
        this.checkoutData = checkoutData;
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(checkoutData.getRedirectUrl());
        paymentPassData.setTransactionId(checkoutData.getTransactionId());
        paymentPassData.setPaymentId(checkoutData.getPaymentId());
        paymentPassData.setCallbackSuccessUrl(checkoutData.getCallbackSuccessUrl());
        paymentPassData.setCallbackFailedUrl(checkoutData.getCallbackFailedUrl());
        paymentPassData.setQueryString(checkoutData.getQueryString());
        this.startActivityForResult(
                TopPayActivity.createInstance(this, paymentPassData),
                TopPayActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorHttpCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorNoConnectionCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorTimeoutConnectionCheckoutCart(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderThanksTopPaySuccess(String message) {
        showToastMessage(getString(R.string.message_payment_succeded_transaction_module));
        navigateToActivity(TransactionPurchaseRouter.createIntentTxSummary(this));
        CartBadgeNotificationReceiver.resetBadgeCart(this);
        closeView();
    }

    @Override
    public void renderErrorThanksTopPaySuccess(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorHttpThanksTopPaySuccess(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorNoConnectionThanksTopPaySuccess(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void renderErrorTimeoutConnectionThanksTopPaySuccess(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(this, message);
    }

    @Override
    public void closeWithResult(int resultCode, @Nullable Intent intent) {
        if (intent == null) setResult(resultCode);
        else setResult(resultCode, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        showResetDialog();
    }

    void showResetDialog() {
        DialogFragment dialog = ResetShipmentFormDialog.newInstance(
                new ResetShipmentFormDialog.ResetShipmentFormCallbackAction() {

                    @Override
                    public void onResetCartShipmentForm() {
                        if (getFragmentManager().findFragmentById(R.id.container)
                                instanceof SingleAddressShipmentFragment)
                            setResult(RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                        else if (getFragmentManager().findFragmentById(R.id.container)
                                instanceof MultipleAddressShipmentFragment)
                            setResult(RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT);
                        finish();
                    }

                    @Override
                    public void onCancelResetCartShipmentForm() {

                    }
                });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(dialog, ResetShipmentFormDialog.DIALOG_FRAGMENT_TAG);
        ft.commitAllowingStateLoss();
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
        if (!progressDialogNormal.isProgress()) progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        if (progressDialogNormal.isProgress()) progressDialogNormal.dismiss();
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
        return originParams == null
                ? com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(this)
                : com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(this,
                originParams);
    }

    @Override
    public void closeView() {

    }

    @Override
    public void checkoutCart(CheckoutRequest checkoutRequest) {
        cartShipmentPresenter.processCheckout(checkoutRequest);
    }

    @Override
    public void checkPromoCodeShipment(Subscriber<CheckPromoCodeCartShipmentResult> subscriber,
                                       CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest) {
        cartShipmentPresenter.checkPromoShipment(subscriber, checkPromoCodeCartShipmentRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_CANCELLED:
                    NetworkErrorHelper.showSnackbar(
                            this,
                            getString(R.string.alert_payment_canceled_or_failed_transaction_module)
                    );
                    break;
                case TopPayActivity.PAYMENT_SUCCESS:
                    cartShipmentPresenter.processVerifyPayment(checkoutData.getTransactionId());
                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    cartShipmentPresenter.processVerifyPayment(checkoutData.getTransactionId());
                    break;
            }
        }
    }
}
