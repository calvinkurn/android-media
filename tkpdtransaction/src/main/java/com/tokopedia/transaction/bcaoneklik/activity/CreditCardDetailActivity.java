package com.tokopedia.transaction.bcaoneklik.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.bcaoneklik.di.DaggerPaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.di.PaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.dialog.DeleteCreditCardDialog;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenterImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aghny A. Putra on 09/01/18
 */

public class CreditCardDetailActivity extends TActivity
        implements DeleteCreditCardDialog.DeleteCreditCardDialogListener {

    private static final String KEY_CC_ITEM = "credit_card_item";
    private static final String VISA = "visa";
    private static final String MASTERCARD = "mastercard";
    private static final String JCB = "jcb";

    private static final String VISA_LARGE = "bg_visa_large";
    private static final String MASTERCARD_LARGE = "bg_mastercard_large";
    private static final String JCB_LARGE = "bg_jcb_large";
    private static final String EXPIRED_LARGE = "bg_expired_large";

    @BindView(R2.id.iv_credit_card_large) ImageView mViewImageCc;
    @BindView(R2.id.tv_credit_card_number) TextView mCreditCardNumber;
    @BindView(R2.id.tv_credit_card_expiry) TextView mCardExpiry;
    @BindView(R2.id.iv_credit_card_logo) ImageView mCreditCardLogo;

    @Inject ListPaymentTypePresenterImpl mListPaymentTypePresenter;

    private CreditCardModelItem mCreditCardModelItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mCreditCardModelItem = getIntent().getParcelableExtra(KEY_CC_ITEM);

        super.onCreate(savedInstanceState);
        inflateView(R.layout.credit_card_detail_layout);
        ButterKnife.bind(this);

        initInjector();

        ImageHandler.LoadImage(mViewImageCc, getBackgroundAssets(mCreditCardModelItem));
        mCreditCardNumber.setText(getSpacedText(mCreditCardModelItem.getMaskedNumber()));
        mCardExpiry.setText(getExpiredDate(mCreditCardModelItem));
        ImageHandler.LoadImage(mCreditCardLogo, mCreditCardModelItem.getCardTypeImage());
    }

    private void initInjector() {
        PaymentOptionComponent component = DaggerPaymentOptionComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    private String getBackgroundAssets(CreditCardModelItem item) {
        final String resourceUrl = TkpdBaseURL.Payment.CDN_IMG_ANDROID_DOMAIN + "%s/%s/%s.png";
        String assetName = getBackgroundResource(item);
        String density = DisplayMetricUtils.getScreenDensity(this);

        return String.format(resourceUrl, assetName, density, assetName);
    }

    private String getBackgroundResource(CreditCardModelItem item) {
        switch (item.getCardType().toLowerCase()) {
            case VISA:
                return VISA_LARGE;
            case MASTERCARD:
                return MASTERCARD_LARGE;
            case JCB:
                return JCB_LARGE;
            default:
                return EXPIRED_LARGE;
        }
    }

    private String getSpacedText(String inputText) {
        final int DIGITS = 4;
        final int THREE_SPACE = 3;

        StringBuilder builder = new StringBuilder();

        builder.append(inputText.charAt(0));
        for (int i = 1; i < inputText.length(); i++) {
            if (i % DIGITS == 0) {
                for (int j = 0; j < THREE_SPACE; j++) {
                    builder.append(getString(R.string.single_spacing));
                }
            } else {
                builder.append(getString(R.string.single_spacing));
            }
            builder.append(inputText.charAt(i));
        }

        return builder.toString();
    }

    private String getExpiredDate(CreditCardModelItem item) {
        return String.format("%s/%s", item.getExpiryMonth(), item.getExpiryYear());
    }

    private String getToolbarTitle() {
        return getTitle() + " " + mCreditCardModelItem.getCardType();
    }

    @OnClick(R2.id.btn_delete_cc)
    public void showDeleteCcDialog() {
        DeleteCreditCardDialog creditCardDialog = DeleteCreditCardDialog.newInstance(
                mCreditCardModelItem.getTokenId(),
                mCreditCardModelItem.getMaskedNumber());
        creditCardDialog.show(getFragmentManager(),
                getString(R.string.tag_delete_credit_card_dialog));
    }

    @Override
    public void onConfirmDelete(String tokenId) {
        mListPaymentTypePresenter.onCreditCardDeleted(this, tokenId);
        setResult(ListPaymentTypeView.CREDIT_CARD_DETAIL_REQUEST_CODE);
        finish();
    }

    @Override
    protected void setupToolbar() {
        toolbar = findViewById(com.tokopedia.core.R.id.app_bar);
        toolbar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (isLightToolbarThemes()) {
            setLightToolbarStyle();
        }
    }

    private void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
            toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        } else {
            toolbar.setBackgroundResource(com.tokopedia.core.R.drawable.bg_white_toolbar_drop_shadow);
        }

        Drawable drawable = ContextCompat.getDrawable(
                this, com.tokopedia.core.R.drawable.ic_toolbar_overflow_level_two_black);
        drawable.setBounds(5, 5, 5, 5);
        toolbar.setOverflowIcon(drawable);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }

        toolbar.setTitleTextAppearance(this, com.tokopedia.core.R.style.WebViewToolbarText);
        toolbar.setSubtitleTextAppearance(this, com.tokopedia.core.R.style
                .WebViewToolbarSubtitleText);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

}
