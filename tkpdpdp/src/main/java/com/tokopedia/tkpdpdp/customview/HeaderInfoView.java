package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
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

import butterknife.BindString;
import butterknife.BindView;


/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class HeaderInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private TextView tvName;
    private TextView cashbackTextView;
    private TextView tvPrice;
    private LinearLayout cashbackHolder;
    private static final String TAG = HeaderInfoView.class.getSimpleName();
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private static final String WEEK_TIMER_FORMAT = "%dw : ";
    private static final String DAY_TIMER_FORMAT = "%dd : ";
    private static final String HOUR_MIN_SEC_TIMER_FORMAT = "%02dh : %02dm : %02ds";


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
        tvPrice = (TextView) findViewById(R.id.tv_price);
        cashbackTextView = (TextView) findViewById(R.id.label_cashback);
        cashbackHolder = (LinearLayout) findViewById(R.id.cashback_holder);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_header_product_info;
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
        tvPrice.setText(data.getInfo().getProductPrice());
        setVisibility(VISIBLE);

        if (data.getCashBack() != null && !data.getCashBack().getProductCashbackValue().isEmpty()) {
            cashbackHolder.setVisibility(VISIBLE);
            cashbackTextView.setText(data.getCashBack().getProductCashbackValue());
            cashbackTextView.setVisibility(VISIBLE);
            cashbackTextView.setText(getContext().getString(R.string.value_cashback)
                    .replace("X", data.getCashBack().getProductCashback()));
        }
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }

    public void renderProductCampaign(final ProductCampaign data) {
        if(data != null && data.getOriginalPrice() != null) {
            textOriginalPrice.setText(String.format(labelPriceWithIdr, data.getOriginalPrice()));
            textOriginalPrice.setPaintFlags(
                    textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            textDiscount.setText(String.format(discount,data.getPercentageAmount()));

            try {
                cashbackHolder.setVisibility(VISIBLE);
                textDiscount.setVisibility(VISIBLE);
                textOriginalPrice.setVisibility(VISIBLE);
                linearDiscountTimerHolder.setVisibility(VISIBLE);

                SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_FORMAT);
                Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
                long delta = sf.parse(data.getEndDate()).getTime() - now.getTimeInMillis();
                textDiscountTimer.setText(getCountdownText(delta));

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
            } catch (Exception ex) {
                ex.printStackTrace();
                hideProductCampaign(data);
            }
        }
    }

    private void hideProductCampaign(ProductCampaign data) {
        linearDiscountTimerHolder.setVisibility(GONE);
        textDiscount.setVisibility(GONE);
        textOriginalPrice.setVisibility(GONE);
        tvPrice.setText(data.getOriginalPrice());
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
