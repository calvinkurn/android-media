package com.tokopedia.tokocash.qrpayment.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class SuccessPaymentQRActivity extends TActivity {

    private static final String MERCHANT_NAME = "merchant_name";
    private static final String AMOUNT = "amount";
    private static final String QR_PAYMENT_DATA = "qr_payment_tokocash";
    private static final String IS_TRANSACTION_SUCCESS = "is_transaction_success";

    private TextView merchantName;
    private TextView amountTransaction;
    private TextView timeTransaction;
    private TextView idTransaction;
    private TextView tokoCashBalance;
    private Button backToHomeBtn;
    private TextView helpText;
    private QrPaymentTokoCash qrPaymentTokoCash;
    private LinearLayout successTransactionLayout;
    private LinearLayout failedTransactionLayout;
    private boolean isTransactionSuccess;

    public static Intent newInstance(Context context, QrPaymentTokoCash qrPaymentTokoCash,
                                     String merchantName, String amount, boolean isTransactionSuccess) {
        Intent intent = new Intent(context, SuccessPaymentQRActivity.class);
        intent.putExtra(MERCHANT_NAME, merchantName);
        intent.putExtra(AMOUNT, amount);
        intent.putExtra(QR_PAYMENT_DATA, qrPaymentTokoCash);
        intent.putExtra(IS_TRANSACTION_SUCCESS, isTransactionSuccess);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_success_payment_qr);

        initView();
        initVar();
        setActionVar();
    }

    private void initView() {
        merchantName = (TextView) findViewById(R.id.merchant_name);
        amountTransaction = (TextView) findViewById(R.id.amount_transaction);
        timeTransaction = (TextView) findViewById(R.id.time_transaction);
        idTransaction = (TextView) findViewById(R.id.transaction_id);
        tokoCashBalance = (TextView) findViewById(R.id.balance_tokocash);
        backToHomeBtn = (Button) findViewById(R.id.button_back_to_home);
        helpText = (TextView) findViewById(R.id.help_text);
        successTransactionLayout = (LinearLayout) findViewById(R.id.layout_success_transaction);
        failedTransactionLayout = (LinearLayout) findViewById(R.id.layout_failed_transaction);
    }

    private void initVar() {
        isTransactionSuccess = getIntent().getBooleanExtra(IS_TRANSACTION_SUCCESS, false);
        if (isTransactionSuccess) {
            toolbar.setTitle("Transaksi Berhasil");
            successTransactionLayout.setVisibility(View.VISIBLE);
            failedTransactionLayout.setVisibility(View.GONE);
            qrPaymentTokoCash = getIntent().getParcelableExtra(QR_PAYMENT_DATA);
            merchantName.setText(getIntent().getStringExtra(MERCHANT_NAME));
            amountTransaction.setText("Rp " + CurrencyFormatHelper
                    .ConvertToRupiah(String.valueOf(getIntent()
                            .getStringExtra(AMOUNT).replace(",", "."))));
            timeTransaction.setText(qrPaymentTokoCash.getDateTime());
            idTransaction.setText(String.valueOf(qrPaymentTokoCash.getTransactionId()));
            tokoCashBalance.setText(String.valueOf("Rp " + CurrencyFormatHelper
                    .ConvertToRupiah(String.valueOf(qrPaymentTokoCash.getAmount())
                            .replace(",", "."))));
        } else {
            toolbar.setTitle("Transaksi Gagal");
            successTransactionLayout.setVisibility(View.GONE);
            failedTransactionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setActionVar() {
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
