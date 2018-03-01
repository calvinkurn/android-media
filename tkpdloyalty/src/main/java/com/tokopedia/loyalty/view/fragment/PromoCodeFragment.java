package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoCodeComponent;
import com.tokopedia.loyalty.di.component.PromoCodeComponent;
import com.tokopedia.loyalty.di.module.PromoCodeViewModule;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.presenter.IPromoCodePresenter;
import com.tokopedia.loyalty.view.view.IPromoCodeView;

import javax.inject.Inject;

import static com.tokopedia.loyalty.view.activity.LoyaltyActivity.DIGITAL_STRING;
import static com.tokopedia.loyalty.view.activity.LoyaltyActivity.MARKETPLACE_CART_LIST_STRING;
import static com.tokopedia.loyalty.view.activity.LoyaltyActivity.MARKETPLACE_CART_SHIPMENT_STRING;

/**
 * @author anggaprasetiyo on 24/11/17.
 */

public class PromoCodeFragment extends BasePresenterFragment implements IPromoCodeView {


    @Inject
    IPromoCodePresenter dPresenter;

    private ManualInsertCodeListener listener;

    private TkpdProgressDialog progressDialog;

    private TextInputLayout voucherCodeFieldHolder;

    private static final String PLATFORM_KEY = "PLATFORM_KEY";

    private static final String CATEGORY_KEY = "CATEGORY_KEY";

    private static final String ADDITIONAL_DATA_KEY = "ADDITIONAL_DATA_KEY";

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
        return R.layout.fragment_promo_code;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        voucherCodeFieldHolder = view.findViewById(R.id.til_et_voucher_code);
        final EditText voucherCodeField = view.findViewById(R.id.et_voucher_code);
        TextView submitVoucherButton = view.findViewById(R.id.btn_check_voucher);

        if (getArguments().getString(PLATFORM_KEY, "").equals(DIGITAL_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitDigitalVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else if (getArguments().getString(PLATFORM_KEY, "").equals(MARKETPLACE_CART_LIST_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitMarketPlaceCartListVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else if (getArguments().getString(PLATFORM_KEY, "").equals(MARKETPLACE_CART_SHIPMENT_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitMarketPlaceCartShipmentVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else {
            submitVoucherButton.setOnClickListener(onSubmitMarketplaceVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        }

    }

    private View.OnClickListener onSubmitMarketplaceVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else
                    dPresenter.processCheckPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString());
            }
        };
    }

    private View.OnClickListener onSubmitDigitalVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else {
                    dPresenter.processCheckDigitalPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString(),
                            getArguments().getString(CATEGORY_KEY));
                }
            }
        };
    }


    private View.OnClickListener onSubmitMarketPlaceCartListVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else {
                    dPresenter.processCheckMarketPlaceCartListPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString());
                }
            }
        };
    }

    private View.OnClickListener onSubmitMarketPlaceCartShipmentVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else {
                    dPresenter.processCheckMarketPlaceCartShipmentPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString(),
                            getArguments().getString(ADDITIONAL_DATA_KEY));
                }
            }
        };
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

    @SuppressWarnings("deprecation")
    @Override
    protected void initInjector() {
        super.initInjector();
        PromoCodeComponent promoCodeComponent = DaggerPromoCodeComponent.builder()
                .appComponent((AppComponent) getComponent(AppComponent.class))
                .promoCodeViewModule(new PromoCodeViewModule(this))
                .build();
        promoCodeComponent.inject(this);
    }

    public static Fragment newInstance(String platform, String categoryKey) {
        PromoCodeFragment fragment = new PromoCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(CATEGORY_KEY, categoryKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstanceCartCheckoutMarketPlace(
            String platform, String categoryKey, String additionalDataString
    ) {
        PromoCodeFragment fragment = new PromoCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(CATEGORY_KEY, categoryKey);
        bundle.putString(ADDITIONAL_DATA_KEY, additionalDataString);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void checkVoucherSuccessfull(VoucherViewModel voucher) {
        listener.onCodeSuccess(voucher.getCode(), voucher.getMessage(), voucher.getAmount());
    }

    @Override
    public void checkDigitalVoucherSucessful(VoucherViewModel voucher) {
        listener.onDigitalCodeSuccess(
                voucher.getCode(),
                voucher.getMessage(),
                voucher.getRawDiscount(),
                voucher.getRawCashback());
    }

    @Override
    public void onGetGeneralError(String errorMessage) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onPromoCodeError(String errorMessage) {
        voucherCodeFieldHolder.setError(errorMessage);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
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
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ManualInsertCodeListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ManualInsertCodeListener) context;
    }

    public interface ManualInsertCodeListener {

        void onCodeSuccess(String voucherCode, String voucherMessage, String voucherAmount);

        void onDigitalCodeSuccess(String voucherCode, String voucherMessage, long discountAmount, long cashBackAmount);

    }
}
