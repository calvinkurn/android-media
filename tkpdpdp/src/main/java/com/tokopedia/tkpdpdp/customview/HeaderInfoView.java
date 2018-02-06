package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class HeaderInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String WEEK_TIMER_FORMAT = "%dw : ";
    private static final String DAY_TIMER_FORMAT = "%dd : ";
    private static final String HOUR_MIN_SEC_TIMER_FORMAT = "%02dh : %02dm : %02ds";

    private TextView tvName;
    private TextView cashbackTextView;
    private TextView tvPriceFinal;
    private TextView textOriginalPrice;
    private TextView textDiscount;
    private LinearLayout linearDiscountTimerHolder;
    private TextView textDiscountTimer;
    private Context context;
    private LinearLayout textOfficialStore;

    public HeaderInfoView(Context context) {
        super(context);
    }

    public HeaderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPriceFinal = (TextView) findViewById(R.id.tv_price_pdp);
        cashbackTextView = (TextView) findViewById(R.id.label_cashback);
        textOriginalPrice = (TextView) findViewById(R.id.text_original_price);
        textDiscount = (TextView) findViewById(R.id.text_discount);
        linearDiscountTimerHolder = (LinearLayout) findViewById(R.id.linear_discount_timer_holder);
        textOfficialStore = (LinearLayout) findViewById(R.id.text_official_store);
        textDiscountTimer = (TextView) findViewById(R.id.text_discount_timer);
        this.context = context;


    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_header_product_info_customer;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        if(data != null && data.getCampaign() != null && data.getCampaign().getActive()) {
            renderProductCampaign(data);
        } else {
            tvPriceFinal.setText(data.getInfo().getProductPrice());
        }
        if (data.getCashBack() != null && !data.getCashBack().getProductCashbackValue().isEmpty()) {
            cashbackTextView.setText(data.getCashBack().getProductCashbackValue());
            cashbackTextView.setText(getContext().getString(R.string.value_cashback)
                    .replace("X", data.getCashBack().getProductCashback()));
            cashbackTextView.setBackgroundResource(com.tokopedia.core.R.drawable.bg_label);
            cashbackTextView.setTextColor(ContextCompat.getColor(context, com.tokopedia.core.R.color.white));
            ColorStateList tint = ColorStateList.valueOf(ContextCompat.getColor(context,com.tokopedia.core.R.color.tkpd_main_green));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                cashbackTextView.setBackgroundTintList(tint);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && cashbackTextView instanceof TintableBackgroundView) {
                    ((TintableBackgroundView) cashbackTextView).setSupportBackgroundTintList(tint);
            } else {
                ViewCompat.setBackgroundTintList(cashbackTextView, tint);
            }
            cashbackTextView.setVisibility(VISIBLE);
        }

        if(data.getShopInfo().getShopIsOfficial() != null && data.getShopInfo().getShopIsOfficial() == 1) {
            textOfficialStore.setVisibility(VISIBLE);
        }
        setVisibility(VISIBLE);
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(MethodChecker.fromHtml(productPass.getProductName()));
        tvPriceFinal.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }

    public void renderProductCampaign(ProductDetailData data) {
        if(data != null && data.getCampaign() != null && data.getCampaign().getActive()) {
            tvPriceFinal.setText(data.getCampaign().getDiscountedPriceFmt());
            textOriginalPrice.setText(data.getInfo().getProductPrice());
            textOriginalPrice.setPaintFlags(
                    textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            textDiscount.setText(String.format(
                    getContext().getString(R.string.label_discount_percentage),
                    data.getCampaign().getDiscountedPercentage()
            ));

            textDiscount.setVisibility(VISIBLE);
            textOriginalPrice.setVisibility(VISIBLE);

            showCountdownTimer(data);
        }
    }

    private void showCountdownTimer(final ProductDetailData data) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_FORMAT);
            Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
            long delta = sf.parse(data.getCampaign().getEndDate()).getTime() - now.getTimeInMillis();

            if (TimeUnit.MILLISECONDS.toDays(delta) < 1) {
                textDiscountTimer.setText(getCountdownText(delta));
                linearDiscountTimerHolder.setVisibility(VISIBLE);

                new CountDownTimer(delta, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        textDiscountTimer.setText(getCountdownText(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        hideProductCampaign(data);
                    }
                }.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            linearDiscountTimerHolder.setVisibility(GONE);
        }
    }

    private void hideProductCampaign(ProductDetailData data) {
        linearDiscountTimerHolder.setVisibility(GONE);
        textDiscount.setVisibility(GONE);
        textOriginalPrice.setVisibility(GONE);
        tvPriceFinal.setText(data.getInfo().getProductPrice());
    }

    private String getCountdownText(long millisUntilFinished) {
        String countdown = "";
        long day = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        long week = day / 7;

        if(week != 0) {
            countdown += String.format(WEEK_TIMER_FORMAT, week);
            day = day % 7;
            countdown += String.format(DAY_TIMER_FORMAT, day);
        } else if (day != 0) {
            countdown += String.format(DAY_TIMER_FORMAT, day);
        }

        countdown += String.format(
                HOUR_MIN_SEC_TIMER_FORMAT,
                (millisUntilFinished / (1000 * 60 * 60)) % 24,
                (millisUntilFinished / (1000 * 60)) % 60,
                (millisUntilFinished / (1000)) % 60
        );

        return countdown;
    }
}
