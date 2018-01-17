package com.tokopedia.seller.shop.open.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.seller.lib.widget.TkpdHintTextInputLayout;
import com.tokopedia.seller.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.util.ShopErrorHandler;
import com.tokopedia.seller.shop.open.view.activity.ShopOpenReserveDomainSuccessActivity;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenDomainView;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenterImpl;
import com.tokopedia.seller.shop.open.view.watcher.AfterTextWatcher;

import javax.inject.Inject;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenReserveDomainFragment extends BasePresenterFragment implements ShopOpenDomainView {

    public static final int MIN_SHOP_NAME_LENGTH = 3;
    public static final int MIN_SHOP_DOMAIN_LENGTH = 3;
    private View buttonSubmit;
    private TkpdHintTextInputLayout textInputShopName;
    private EditText editTextInputShopName;
    private TkpdHintTextInputLayout textInputDomainName;
    private PrefixEditText editTextInputDomainName;
    private SnackbarRetry snackbarRetry;
    private TkpdProgressDialog tkpdProgressDialog;

    @Inject
    ShopOpenDomainPresenterImpl shopOpenDomainPresenter;
    @Inject
    ShopOpenTracking trackingOpenShop;

    public static ShopOpenReserveDomainFragment newInstance() {
        return new ShopOpenReserveDomainFragment();
    }

    @Override
    protected void initInjector() {
        ShopOpenDomainComponent component = getComponent(ShopOpenDomainComponent.class);
        component.inject(this);

        shopOpenDomainPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_open_domain, container, false);
        TextView textHello = view.findViewById(R.id.text_hello);
        buttonSubmit = view.findViewById(R.id.button_submit);
        textInputShopName = view.findViewById(R.id.text_input_shop_name);
        textInputDomainName = view.findViewById(R.id.text_input_domain_name);
        editTextInputShopName = textInputShopName.getEditText();
        editTextInputDomainName = (PrefixEditText) textInputDomainName.getEditText();

        String helloName = getString(R.string.hello_x, SessionHandler.getLoginName(getActivity()));
        textHello.setText(MethodChecker.fromHtml(helloName));

        buttonSubmit.setEnabled(false);

        editTextInputShopName.addTextChangedListener(new AfterTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                textInputShopName.disableSuccessError();
                buttonSubmit.setEnabled(false);
                hideSnackBarRetry();
                if (TextUtils.isEmpty(s)) {
                    textInputShopName.setError(getString(R.string.shop_open_error_shop_name_must_be_filled));
                } else if (s.toString().length() < MIN_SHOP_NAME_LENGTH) {
                    textInputShopName.setError(getString(R.string.shop_open_error_shop_name_min_char));
                } else if (s.toString().length() <= textInputShopName.getCounterMaxLength()) {
                    shopOpenDomainPresenter.checkShop(editTextInputShopName.getText().toString());
                }
            }
        });

        editTextInputDomainName.addTextChangedListener(new AfterTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                textInputDomainName.disableSuccessError();
                buttonSubmit.setEnabled(false);
                hideSnackBarRetry();
                String domainInputStr = editTextInputDomainName.getTextWithoutPrefix();
                if (TextUtils.isEmpty(domainInputStr)) {
                    textInputDomainName.setError(getString(R.string.shop_open_error_domain_name_must_be_filled));
                } else if (domainInputStr.length() < MIN_SHOP_DOMAIN_LENGTH) {
                    textInputDomainName.setError(getString(R.string.shop_open_error_domain_name_min_char));
                } else if (s.toString().length() <= textInputDomainName.getCounterMaxLength()) {
                    shopOpenDomainPresenter.checkDomain(editTextInputDomainName.getTextWithoutPrefix());
                }
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSubmitClicked();
            }
        });
        return view;
    }

    private void hideSnackBarRetry() {
        if (snackbarRetry != null && snackbarRetry.isShown()) {
            snackbarRetry.hideRetrySnackbar();
        }
    }

    private void hideSubmitLoading() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    private void showSubmitLoading() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    private void onButtonSubmitClicked() {
        if (!isShopNameDomainValid()) {
            buttonSubmit.setEnabled(false);
            return;
        }
        showSubmitLoading();
        String shopName = editTextInputShopName.getText().toString().trim();
        String shopDomain = editTextInputDomainName.getTextWithoutPrefix().trim();
        shopOpenDomainPresenter.submitReserveNameAndDomainShop(shopName, shopDomain);
    }

    @Override
    public boolean isShopNameInValidRange() {
        int inputShopNameLength = editTextInputShopName.getText().length();
        return inputShopNameLength >= MIN_SHOP_NAME_LENGTH && inputShopNameLength <= textInputShopName.getCounterMaxLength();
    }

    @Override
    public boolean isShopDomainInValidRange() {
        int inputShopDomainLength = editTextInputDomainName.getText().length();
        return editTextInputDomainName.getTextWithoutPrefix().length() >= MIN_SHOP_DOMAIN_LENGTH
                && inputShopDomainLength <= textInputDomainName.getCounterMaxLength();
    }

    @Override
    public void onSuccessCheckShopName(boolean existed) {
        if (existed) {
            textInputShopName.setSuccess(getString(R.string.shop_name_available));
        } else {
            textInputShopName.setError(getString(R.string.shop_name_not_available));
        }
        checkEnableSubmit();
    }

    @Override
    public void onErrorCheckShopName(Throwable t) {
        textInputShopName.setError(ShopErrorHandler.getErrorMessage(getActivity(), t));
        trackingOpenShop.eventOpenShopBiodataNameError(ShopErrorHandler.getErrorMessage(getActivity(), t));
    }

    @Override
    public void onSuccessCheckShopDomain(boolean existed) {
        if (existed) {
            textInputDomainName.setSuccess(getString(R.string.domain_name_available));
        } else {
            textInputDomainName.setError(getString(R.string.domain_name_not_available));
        }
        checkEnableSubmit();
    }

    @Override
    public void onErrorCheckShopDomain(Throwable t) {
        textInputDomainName.setError(ShopErrorHandler.getErrorMessage(getActivity(), t));
        trackingOpenShop.eventOpenShopBiodataDomainError(ShopErrorHandler.getErrorMessage(getActivity(), t));
    }

    @Override
    public void onErrorReserveShop(Throwable t) {
        hideSubmitLoading();
        Crashlytics.logException(t);
        String message = ShopErrorHandler.getErrorMessage(getActivity(), t);
        sendErrorTracking(message);
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        onButtonSubmitClicked();
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    private void sendErrorTracking(String errorMessage) {
        trackingOpenShop.eventOpenShopBiodataError(errorMessage);
        String generatedErrorMessage = ShopErrorHandler.getGeneratedErrorMessage(errorMessage.toCharArray(),
                editTextInputShopName.getText().toString(), editTextInputDomainName.getTextWithoutPrefix());
        trackingOpenShop.eventOpenShopBiodataErrorWithData(generatedErrorMessage);
    }

    @Override
    public void onSuccessReserveShop(String shopName) {
        hideSubmitLoading();
        trackingOpenShop.eventOpenShopBiodataSuccess();
        goToShopOpenMandatory(shopName);
    }

    private void checkEnableSubmit() {
        if (isShopNameDomainValid()) {
            buttonSubmit.setEnabled(true);
        }
    }

    private boolean isShopNameDomainValid() {
        return isShopNameInValidRange() && isShopDomainInValidRange() &&
                textInputDomainName.isSuccessShown() && textInputShopName.isSuccessShown();
    }

    private void goToShopOpenMandatory(String shopName) {
        Intent intent = ShopOpenReserveDomainSuccessActivity.getIntent(getContext(),shopName);
        startActivity(intent);
        getActivity().finish();
    }
}