package com.tokopedia.transaction.bcaoneklik.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 09/01/18
 */

public class CreditCardDetailActivity extends TActivity {

    @BindView(R2.id.image_cc_big_size) ImageView mViewImageCc;
    @BindView(R2.id.input_credit_card_number) TextView mCreditCardNumber;
    @BindView(R2.id.card_expiry) TextView mCardExpiry;
    @BindView(R2.id.credit_card_logo) ImageView mCreditCardLogo;
    @BindView(R2.id.button_delete_cc) Button mButtonDeleteCc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.credit_card_detail_layout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        CreditCardModelItem creditCardModelItem = (CreditCardModelItem) intent.getSerializableExtra("credit_card_item");

        mViewImageCc.setBackgroundResource(getCcImageResource(creditCardModelItem));
        mCreditCardNumber.setText(getSpacedText(creditCardModelItem.getMaskedNumber()));
        mCardExpiry.setText(creditCardModelItem.getExpiryMonth() + "/" + creditCardModelItem.getExpiryYear());
        ImageHandler.LoadImage(mCreditCardLogo, creditCardModelItem.getCardTypeImage());
    }

    @Override
    protected void setupToolbar() {
        toolbar = findViewById(com.tokopedia.core.R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (isLightToolbarThemes()) {
            setLightToolbarStyle();
        }
    }

    private void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        } else {
            toolbar.setBackgroundResource(com.tokopedia.core.R.drawable.bg_white_toolbar_drop_shadow);
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        toolbar.setTitleTextAppearance(this, com.tokopedia.core.R.style.WebViewToolbarText);
        toolbar.setSubtitleTextAppearance(this, com.tokopedia.core.R.style
                .WebViewToolbarSubtitleText);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private int getCcImageResource(CreditCardModelItem item) {
        switch (item.getCardType().toLowerCase()) {
            case "visa":
                return R.drawable.image_cc_visa_plain_fullsize;
            case "mastercard":
                return R.drawable.image_cc_mastercard_plain_fullsize;
            case "jcb":
                return R.drawable.image_cc_jcb_plain_fullsize;
            default:
                return R.drawable.image_cc_expired_plain_fullsize;
        }
    }

    private String getSpacedText(String inputText) {
        StringBuilder builder = new StringBuilder();

        builder.append(inputText.charAt(0));
        for (int i=1; i < inputText.length(); i++) {
            if (i % 4 == 0) builder.append("\u00A0\u00A0\u00A0");
            else builder.append("\u00A0");
            builder.append(inputText.charAt(i));
        }

        return builder.toString();
    }

}
