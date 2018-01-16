package com.tokopedia.tokocash.qrpayment.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.QrPaymentContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.presenter.QrPaymentPresenter;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class NominalQrPaymentActivity extends TActivity implements QrPaymentContract.View,
        HasComponent<TokoCashComponent> {

    private static final int REQUEST_CODE_SUCCESS = 121;
    private static final int REQUEST_CODE_FAILED = 111;
    private static final int MAX_DIGIT_NOMINAL = 10;
    private static final String INFO_QR = "info_qr";
    private static final String IDENTIFIER = "identifier";

    private TextView merchantName;
    private TextView merchantPhone;
    private EditText nominalValue;
    private EditText notesValue;
    private View separatorNominal;
    private TextView tokocashValue;
    private Button payButton;
    private InfoQrTokoCash infoQrTokoCash;
    private TokoCashComponent tokoCashComponent;
    private BalanceTokoCash balanceTokoCash;
    private ProgressBar progressBar;
    @Inject
    QrPaymentPresenter presenter;

    public static Intent newInstance(Context context, String identifier, InfoQrTokoCash infoQrTokoCash) {
        Intent intent = new Intent(context, NominalQrPaymentActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        intent.putExtra(INFO_QR, infoQrTokoCash);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_nominal_qr);
        initInjector();
        presenter.attachView(this);

        toolbar.setTitle(getString(R.string.title_input_nominal));
        infoQrTokoCash = getIntent().getParcelableExtra(INFO_QR);

        initView();
        setVar();
        presenter.getBalanceTokoCash();
    }

    private void setVar() {
        merchantName.setText(infoQrTokoCash.getName());
        merchantPhone.setText(infoQrTokoCash.getPhoneNumber());
        merchantPhone.setVisibility(!infoQrTokoCash.getPhoneNumber().equals("") ? View.VISIBLE : View.GONE);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.postQrPayment();
            }
        });
    }

    private int getColorNominal(int color) {
        return ContextCompat.getColor(getApplicationContext(), color);
    }

    private void initView() {
        merchantName = (TextView) findViewById(R.id.merchant_name);
        merchantPhone = (TextView) findViewById(R.id.merchant_phone);
        nominalValue = (EditText) findViewById(R.id.value_nominal);
        notesValue = (EditText) findViewById(R.id.notes_value);
        separatorNominal = (View) findViewById(R.id.separator);
        tokocashValue = (TextView) findViewById(R.id.tokocash_value);
        payButton = (Button) findViewById(R.id.pay_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public TokoCashComponent getComponent() {
        if (tokoCashComponent == null) initInjector();
        return tokoCashComponent;
    }

    private void initInjector() {
        tokoCashComponent = DaggerTokoCashComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
        tokoCashComponent.inject(this);
    }

    @Override
    public RequestParams getRequestParams() {
        String valueNominal = CurrencyFormatHelper.RemoveNonNumeric(nominalValue.getText().toString());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PostQrPaymentUseCase.IDENTIFIER, getIntent().getStringExtra(IDENTIFIER));
        requestParams.putString(PostQrPaymentUseCase.NOTE, notesValue.getText().toString().equals("") ?
                getString(R.string.default_notes_payment) : notesValue.getText().toString());
        requestParams.putLong(PostQrPaymentUseCase.AMOUNT, Long.parseLong(valueNominal));
        return requestParams;
    }

    @Override
    public void directToSuccessPayment(QrPaymentTokoCash qrPaymentTokoCash) {
        progressBar.setVisibility(View.GONE);
        Intent intent = SuccessPaymentQRActivity.newInstance(getApplicationContext(), qrPaymentTokoCash,
                infoQrTokoCash.getName(), nominalValue.getText().toString(), true);
        startActivityForResult(intent, REQUEST_CODE_SUCCESS);
    }

    @Override
    public void directToFailedPayment() {
        progressBar.setVisibility(View.GONE);
        Intent intent = SuccessPaymentQRActivity.newInstance(getApplicationContext(), new QrPaymentTokoCash(),
                infoQrTokoCash.getName(), nominalValue.getText().toString(), false);
        startActivityForResult(intent, REQUEST_CODE_FAILED);
    }

    @Override
    public void renderBalanceTokoCash(final BalanceTokoCash balanceTokoCash) {
        tokocashValue.setVisibility(View.VISIBLE);
        tokocashValue.setText(String.format(getString(R.string.tokocash_balance_payment),
                balanceTokoCash.getBalance()));
        this.balanceTokoCash = balanceTokoCash;

        if (infoQrTokoCash.getAmount() > 0) {
            nominalValue.setEnabled(false);
            nominalValue.setText(CurrencyFormatHelper
                    .ConvertToRupiah(String.valueOf(infoQrTokoCash.getAmount())));
            handleWarningPayment(infoQrTokoCash.getAmount());
        } else {
            nominalValue.setEnabled(true);
        }
        nominalValue.setFilters(
                new InputFilter[]{new InputFilter.LengthFilter(MAX_DIGIT_NOMINAL)});
        nominalValue.addTextChangedListener(new NumberTextWatcher(nominalValue, "0") {
            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                handleWarningPayment(number);
            }
        });
    }

    private void handleWarningPayment(double nominal) {
        if (nominal <= 0) {
            separatorNominal.setBackgroundColor(getColorNominal(R.color.separator_grey));
            tokocashValue.setTextColor(getColorNominal(R.color.separator_grey));
            payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_grey_border_black));
            payButton.setTextColor(getColorNominal(R.color.grey_nonactive_text));
            payButton.setEnabled(false);
        } else {
            if (nominal > Double.parseDouble(balanceTokoCash.getRaw_balance())) {
                separatorNominal.setBackgroundColor(getColorNominal(R.color.separator_red));
                tokocashValue.setTextColor(getColorNominal(R.color.separator_red));
                payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_grey_border_black));
                payButton.setTextColor(getColorNominal(R.color.grey_nonactive_text));
                payButton.setEnabled(false);
            } else {
                separatorNominal.setBackgroundColor(getColorNominal(R.color.separator_green));
                tokocashValue.setTextColor(getColorNominal(R.color.separator_green));
                payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange_button_rounded));
                payButton.setTextColor(getColorNominal(R.color.white));
                payButton.setEnabled(true);
            }
        }
    }

    @Override
    public void showErrorBalanceTokoCash(String message) {
        progressBar.setVisibility(View.GONE);
        tokocashValue.setVisibility(View.VISIBLE);
        tokocashValue.setText(getString(R.string.error_message_tokocash));
        NetworkErrorHelper.createSnackbarWithAction(this, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getBalanceTokoCash();
                    }
                }).showRetrySnackbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SUCCESS) {
            Intent intent = new Intent();
            setResult(CustomScannerTokoCashActivity.RESULT_CODE_HOME, intent);
            finish();
        } else {
            Intent intent = new Intent();
            setResult(CustomScannerTokoCashActivity.RESULT_CODE__SCANNER, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyPresenter();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(CustomScannerTokoCashActivity.RESULT_CODE_HOME, intent);
        finish();
    }
}
