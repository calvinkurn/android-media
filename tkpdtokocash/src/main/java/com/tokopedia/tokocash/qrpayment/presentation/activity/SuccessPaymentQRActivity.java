package com.tokopedia.tokocash.qrpayment.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
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
    private Button btnRetryScan;

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
        btnRetryScan = (Button) findViewById(R.id.button_back_scanner);
    }

    private void initVar() {
        isTransactionSuccess = getIntent().getBooleanExtra(IS_TRANSACTION_SUCCESS, false);
        if (isTransactionSuccess) {
            toolbar.setTitle(getString(R.string.title_success_payment));
            successTransactionLayout.setVisibility(View.VISIBLE);
            failedTransactionLayout.setVisibility(View.GONE);
            qrPaymentTokoCash = getIntent().getParcelableExtra(QR_PAYMENT_DATA);
            merchantName.setText(getIntent().getStringExtra(MERCHANT_NAME));
            String amountTransactionString = getIntent().getStringExtra(AMOUNT);
            amountTransaction.setText("Rp " + amountTransactionString.replace(",", "."));
            timeTransaction.setText(qrPaymentTokoCash.getDateTime());
            idTransaction.setText(String.valueOf(qrPaymentTokoCash.getTransactionId()));
            String tokocashBalanceString = CurrencyFormatHelper
                    .ConvertToRupiah(String.valueOf(qrPaymentTokoCash.getAmount()));
            tokoCashBalance.setText(String.valueOf("Rp " +
                    tokocashBalanceString.replace(",", ".")));
        } else {
            toolbar.setTitle(getString(R.string.title_failed_payment));
            successTransactionLayout.setVisibility(View.GONE);
            failedTransactionLayout.setVisibility(View.VISIBLE);
            btnRetryScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    setResult(CustomScannerTokoCashActivity.RESULT_CODE__SCANNER, intent);
                    finish();
                }
            });
        }
    }

    private void setHelpText() {
        // TODO for next development will use this help link, for now still hide help link
        SpannableString ss = new SpannableString(getString(R.string.help_payment_tokocash));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.help_payment_tokocash), Toast.LENGTH_SHORT).show();

            }
        };
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(),
                R.color.tkpd_main_green)), 30, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan, 30, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpText.setMovementMethod(LinkMovementMethod.getInstance());
        helpText.setText(ss);
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
    public void onBackPressed() {
        GlobalCacheManager cache = new GlobalCacheManager();
        cache.delete(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
        Intent intent = new Intent();
        setResult(CustomScannerTokoCashActivity.RESULT_CODE_HOME, intent);
        finish();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
