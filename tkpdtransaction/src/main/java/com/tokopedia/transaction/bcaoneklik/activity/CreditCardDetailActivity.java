package com.tokopedia.transaction.bcaoneklik.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.bcaoneklik.di.DaggerPaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.di.PaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.dialog.DeleteCreditCardDialog;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenterImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aghny A. Putra on 09/01/18
 */

public class CreditCardDetailActivity extends TActivity implements DeleteCreditCardDialog.DeleteCreditCardDialogListener {

    @BindView(R2.id.image_cc_big_size) ImageView mViewImageCc;
    @BindView(R2.id.input_credit_card_number) TextView mCreditCardNumber;
    @BindView(R2.id.card_expiry) TextView mCardExpiry;
    @BindView(R2.id.credit_card_logo) ImageView mCreditCardLogo;

    @Inject ListPaymentTypePresenterImpl mListPaymentTypePresenter;

    private CreditCardModelItem mCreditCardModelItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.credit_card_detail_layout);
        ButterKnife.bind(this);

        initInjector();

        mCreditCardModelItem = (CreditCardModelItem) getIntent().getSerializableExtra("credit_card_item");

        mViewImageCc.setBackgroundResource(getCcImageResource(mCreditCardModelItem));
        mCreditCardNumber.setText(getSpacedText(mCreditCardModelItem.getMaskedNumber()));
        mCardExpiry.setText(mCreditCardModelItem.getExpiryMonth() + "/" + mCreditCardModelItem.getExpiryYear());
        ImageHandler.LoadImage(mCreditCardLogo, mCreditCardModelItem.getCardTypeImage());
    }

    private void initInjector() {
        PaymentOptionComponent component = DaggerPaymentOptionComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
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

    @OnClick(R2.id.button_delete_cc)
    public void showDeleteCcDialog() {
        DeleteCreditCardDialog creditCardDialog = DeleteCreditCardDialog.createDialog(
                mCreditCardModelItem.getTokenId(),
                mCreditCardModelItem.getMaskedNumber());
        creditCardDialog.show(getFragmentManager(), "delete_credit_card_dialog");
    }

    @Override
    public void onConfirmDelete(String tokenId) {
        mListPaymentTypePresenter.onCreditCardDeleted(this, tokenId);
    }
}
