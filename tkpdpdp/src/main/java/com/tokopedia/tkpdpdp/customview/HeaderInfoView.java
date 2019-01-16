package com.tokopedia.tkpdpdp.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.entity.variant.Campaign;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductInfo;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.util.ServerTimeOffsetUtil;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class HeaderInfoView extends BaseView<ProductDetailData, ProductDetailView> {

    public static final long ONE_SECOND = 1000L;
    private TextView tvName;
    private TextView cashbackTextView;
    private TextView tvPriceFinal;
    private TextView textOriginalPrice;
    private TextView textDiscount;
    private TextView textStockAvailable;
    private TextView textTimerTitle;
    private TextView campaignStockAvailable;
    private LinearLayout linearDiscountTimerHolder;
    private LinearLayout linearStockAvailable;
    private LinearLayout linearDiscountPrice;
    private CountDownView countDownView;
    private Context context;
    private LinearLayout textOfficialStore;
    private FrameLayout codDescription;

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
        cashbackTextView = (TextView) findViewById(R.id.text_cashback);
        textOriginalPrice = (TextView) findViewById(R.id.text_original_price);
        textDiscount = (TextView) findViewById(R.id.text_discount);
        linearDiscountTimerHolder = (LinearLayout) findViewById(R.id.linear_discount_timer_holder);
        linearStockAvailable = (LinearLayout) findViewById(R.id.linear_stock_available);
        textOfficialStore = (LinearLayout) findViewById(R.id.text_official_store);
        textStockAvailable = (TextView) findViewById(R.id.text_stock_available);
        countDownView = findViewById(R.id.count_down);
        campaignStockAvailable = findViewById(R.id.sale_text_stock_available);
        textTimerTitle = findViewById(R.id.text_title_discount_timer);
        linearDiscountPrice = findViewById(R.id.linear_discount_price);
        codDescription = findViewById(R.id.layout_cod_content);
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
        if (data.getCashBack() != null && !data.getCashBack().getProductCashbackValue().isEmpty()) {
            cashbackTextView.setText(String.format(getResources().getString(R.string.value_cashback_pdp),
                    data.getCashBack().getProductCashback()));
            cashbackTextView.setVisibility(VISIBLE);
        }

        if(data.getShopInfo().getShopIsOfficial() != null && data.getShopInfo().getShopIsOfficial() == 1) {
            textOfficialStore.setVisibility(VISIBLE);
        }
        setVisibility(VISIBLE);

        if(data.getInfo().getProductStockWording() != null){
            textStockAvailable.setText(MethodChecker.fromHtml(data.getInfo().getProductStockWording()));
            linearStockAvailable.setVisibility(VISIBLE);
        } else{
            linearStockAvailable.setVisibility(GONE);
        }
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(MethodChecker.fromHtml(productPass.getProductName()));
        tvPriceFinal.setText(productPass.getProductPrice());
        if (!TextUtils.isEmpty(productPass.getOriginalPrice()) && productPass.getDiscountPercentage()>0) {
            textOriginalPrice.setText(productPass.getOriginalPrice());
            textOriginalPrice.setPaintFlags(
                    textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            textDiscount.setText(String.format(
                    getContext().getString(R.string.label_discount_percentage),
                    productPass.getDiscountPercentage()
            ));

            tvPriceFinal.setVisibility(VISIBLE);
            textDiscount.setVisibility(VISIBLE);
            textOriginalPrice.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(productPass.getCashback())) {
            cashbackTextView.setText(productPass.getCashback());
            cashbackTextView.setVisibility(VISIBLE);
        }
        if(productPass.isOfficial()) {
            textOfficialStore.setVisibility(VISIBLE);
        }
        setVisibility(VISIBLE);
    }

    public void renderProductCampaign(ProductDetailData data) {
        Campaign campaign = data.getCampaign();
        if(campaign != null && campaign.getActive()) {
            textTimerTitle.setText(campaign.getCampaignShortName());
            tvPriceFinal.setText(campaign.getDiscountedPriceFmt());
            textOriginalPrice.setText(campaign.getOriginalPriceFmt());
            textOriginalPrice.setPaintFlags(
                    textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            textDiscount.setText(String.format(
                    getContext().getString(R.string.label_discount_percentage),
                    campaign.getDiscountedPercentage()
            ));

            linearDiscountPrice.setVisibility(VISIBLE);
            tvPriceFinal.setVisibility(VISIBLE);
            textDiscount.setVisibility(VISIBLE);
            textOriginalPrice.setVisibility(VISIBLE);

            showCountdownTimer(data);
        } else {
            codDescription.setVisibility(data.getInfo().isCod() ? VISIBLE : GONE);
            linearDiscountTimerHolder.setVisibility(GONE);
            textDiscount.setVisibility(GONE);
            textOriginalPrice.setVisibility(GONE);
            tvPriceFinal.setText(data.getInfo().getProductPrice());
            linearDiscountPrice.setVisibility(GONE);
        }
    }

    public void renderStockAvailability(boolean campaignActive, ProductInfo data) {
        if(!TextUtils.isEmpty(data.getProductStockWording())) {
            if(campaignActive){
                campaignStockAvailable.setVisibility(VISIBLE);
                linearStockAvailable.setVisibility(GONE);
                campaignStockAvailable.setText(MethodChecker.fromHtml(data.getProductStockWording()));
            } else {
                linearStockAvailable.setVisibility(VISIBLE);
                textStockAvailable.setText(MethodChecker.fromHtml(data.getProductStockWording()));
                if (data.getLimitedStock()) {
                    textStockAvailable.setTextColor(getContext().getResources().getColor(R.color.tkpd_dark_red));
                } else {
                    textStockAvailable.setTextColor(getContext().getResources().getColor(R.color.black_70));
                }
            }
        }
    }

    private void showCountdownTimer(final ProductDetailData data) {
        Campaign campaign = data.getCampaign();
        linearDiscountTimerHolder.setVisibility(GONE);

        try {
            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");;

            //*1000 to convert serverTime to millisecond since serverTime is unix
            long serverTimeMillisecond = data.getServerTimeUnix() * ONE_SECOND;
            long delta = sf.parse(campaign.getEndDate()).getTime() - serverTimeMillisecond;

            if (TimeUnit.MILLISECONDS.toDays(delta) < 1) {
                countDownView.setup(
                        ServerTimeOffsetUtil.getServerTimeOffset(serverTimeMillisecond),
                        sf.parse(campaign.getEndDate()), new CountDownView.CountDownListener() {
                            @Override
                            public void onCountDownFinished() {
                                hideProductCampaign(campaign);
                                showAlerDialog();
                            }
                        });
                linearDiscountTimerHolder.setVisibility(VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            linearDiscountTimerHolder.setVisibility(GONE);
        }
    }

    private void showAlerDialog() {
        Dialog dialog = new Dialog((Activity) context, Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(context.getString(R.string.exp_dialog_title));
        dialog.setDesc(context.getString(R.string.exp_dialog_message));
        dialog.setBtnOk(context.getString(R.string.exp_dialog_ok));
        dialog.setBtnCancel(context.getString(R.string.exp_dialog_cancel));
        dialog.setOnCancelClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.refreshData();
                dialog.dismiss();
            }
        });
        dialog.setOnOkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Activity activity = ((Activity) context);
                Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getHomeIntent(activity);
                activity.startActivity(intent);
                activity.finish();
            }
        });
        dialog.show();
    }

    private void hideProductCampaign(Campaign campaign) {
        linearDiscountTimerHolder.setVisibility(GONE);
        textDiscount.setVisibility(GONE);
        textOriginalPrice.setVisibility(GONE);
        tvPriceFinal.setText(campaign.getOriginalPriceFmt());
    }

}
