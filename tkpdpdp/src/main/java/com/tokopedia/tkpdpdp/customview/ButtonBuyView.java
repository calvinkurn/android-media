package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.analytics.ProductPageTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import static com.tokopedia.core.product.model.productdetail.ProductInfo.PRD_STATE_WAREHOUSE;

/**
 * @author by Angga.Prasetiyo on 02/11/2015.
 */
public class ButtonBuyView extends BaseView<ProductDetailData, ProductDetailView> {
    private TextView tvBuy;
    private TextView tvPromoTopAds;
    private TextView tvpPromoHour;
    private LinearLayout containerButtonBuy;
    private ProgressBar variantProgressBar;
    private View containerNewButtonBuy;
    public View btnCart;
    public View btnNewBuy;
    private ProgressBar progressBarVariant;
    private TextView tvNewBuy;

    public ButtonBuyView(Context context) {
        super(context);
    }

    public ButtonBuyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_buy_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvBuy = findViewById(R.id.tv_buy);
        tvPromoTopAds = findViewById(R.id.tv_promote_topads);
        tvpPromoHour = findViewById(R.id.tv_promo_hour);
        containerButtonBuy =  findViewById(R.id.container_btn_buy);
        variantProgressBar = findViewById(R.id.variant_progress_bar);
        containerNewButtonBuy = findViewById(R.id.container_new_checkout_flow);
        btnCart = findViewById(R.id.action_button_cart);
        btnNewBuy = findViewById(R.id.container_new_button_buy);
        progressBarVariant = findViewById(R.id.new_variant_progress_bar);
        tvNewBuy = findViewById(R.id.tv_new_buy);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        if (data.getInfo().getProductStatus().equals(PRD_STATE_WAREHOUSE)) {
            tvBuy.setBackgroundResource(R.drawable.btn_buy_grey);
            containerButtonBuy.setBackgroundResource(R.drawable.btn_buy_grey);
            tvBuy.setTextColor(ContextCompat.getColor(getContext(),R.color.black_38));
            tvBuy.setText(getContext().getString(R.string.title_warehouse));
            tvBuy.setEnabled(false);
            containerButtonBuy.setEnabled(false);
            setVisibility(VISIBLE);
            containerButtonBuy.setVisibility(VISIBLE);
            containerNewButtonBuy.setVisibility(GONE);
        } else if (data.getShopInfo().getShopIsOwner() == 1
                || (data.getShopInfo().getShopIsAllowManage() == 1 || GlobalConfig.isSellerApp())) {
            tvpPromoHour.setText(getContext().getString(R.string.title_promo_per_hour));
            tvpPromoHour.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_500));
            tvpPromoHour.setBackgroundResource(R.drawable.btn_promo_ads);
            tvpPromoHour.setOnClickListener(new PromoteClick(data));
            tvPromoTopAds.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPromoAdsClicked();
                    }
                }
            });
            tvpPromoHour.setVisibility(VISIBLE);
            tvPromoTopAds.setVisibility(VISIBLE);
            containerNewButtonBuy.setVisibility(GONE);
            containerButtonBuy.setVisibility(GONE);
        } else {
            containerNewButtonBuy.setVisibility(VISIBLE);
            containerButtonBuy.setVisibility(GONE);
            if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                    && !data.getPreOrder().getPreorderStatus().equals("0")
                    && !data.getPreOrder().getPreorderProcessTime().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
                tvNewBuy.setText(getContext().getString(R.string.title_pre_order));
            } else {
                tvNewBuy.setText(getContext().getString(R.string.title_buy));
            }
            btnNewBuy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionHandler.isV4Login(getContext())) {
                        if (data.getInfo().getHasVariant()) {
                            ProductPageTracking.eventClickBuyTriggerVariant(
                                    getContext(),
                                    String.valueOf(data.getInfo().getProductId())
                            );
                        }
                        listener.onBuyClick(ProductDetailView.SOURCE_BUTTON_BUY_PDP);
                    } else {
                        ProductPageTracking.eventClickBuyNotLogin(
                                getContext(),
                                String.valueOf(data.getInfo().getProductId())
                        );
                        listener.openLoginPage();
                    }
                }
            });
            btnCart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionHandler.isV4Login(getContext())) {
                        if (data.getInfo().getHasVariant()) {
                            ProductPageTracking.eventClickAtcTriggerVariant(
                                    getContext(),
                                    String.valueOf(data.getInfo().getProductId())
                            );
                        }
                        listener.onBuyClick(ProductDetailView.SOURCE_BUTTON_CART_PDP);
                    } else {
                        ProductPageTracking.eventClickAtcNotLogin(
                                getContext(),
                                String.valueOf(data.getInfo().getProductId())
                        );
                        listener.openLoginPage();
                    }
                }
            });
        }

        if (data.getShopInfo().getShopStatus() != 1) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    public void changeToLoading() {
        progressBarVariant.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        progressBarVariant.setVisibility(VISIBLE);
        btnNewBuy.setEnabled(false);
        btnCart.setEnabled(false);
    }

    public void removeLoading() {
        progressBarVariant.setVisibility(GONE);
        btnNewBuy.setEnabled(true);
        btnCart.setEnabled(true);
    }

    public void updateButtonForVariantProduct(boolean isBuyable, ProductDetailData data) {
        if (isBuyable && data.getShopInfo().getShopStatus() == 1) {
            if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                    && !data.getPreOrder().getPreorderStatus().equals("0")
                    && !data.getPreOrder().getPreorderProcessTime().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
                tvNewBuy.setText(getContext().getString(R.string.title_pre_order));
            } else {
                tvNewBuy.setText(getContext().getString(R.string.title_buy));
            }
            btnNewBuy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionHandler.isV4Login(getContext())) {
                        if (data.getInfo().getHasVariant()) {
                            ProductPageTracking.eventClickBuyTriggerVariant(
                                    getContext(),
                                    String.valueOf(data.getInfo().getProductId())
                            );
                        }
                        listener.onBuyClick(ProductDetailView.SOURCE_BUTTON_BUY_PDP);
                    } else {
                        ProductPageTracking.eventClickBuyNotLogin(
                                getContext(),
                                String.valueOf(data.getInfo().getProductId())
                        );
                        listener.openLoginPage();
                    }
                }
            });
            btnCart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionHandler.isV4Login(getContext())) {
                        if (data.getInfo().getHasVariant()) {
                            ProductPageTracking.eventClickAtcTriggerVariant(
                                    getContext(),
                                    String.valueOf(data.getInfo().getProductId())
                            );
                        }
                        listener.onBuyClick(ProductDetailView.SOURCE_BUTTON_CART_PDP);
                    } else {
                        ProductPageTracking.eventClickAtcNotLogin(
                                getContext(),
                                String.valueOf(data.getInfo().getProductId())
                        );
                        listener.openLoginPage();
                    }
                }
            });
            setVisibility(VISIBLE);
        } else if (isBuyable == false) {
            containerNewButtonBuy.setVisibility(GONE);
            containerButtonBuy.setVisibility(VISIBLE);
            tvBuy.setBackgroundResource(R.drawable.btn_buy_grey);
            containerButtonBuy.setBackgroundResource(R.drawable.btn_buy_grey);
            tvBuy.setTextColor(ContextCompat.getColor(getContext(),R.color.black_38));
            tvBuy.setText(getContext().getString(R.string.title_warehouse));
            tvBuy.setEnabled(false);
            containerButtonBuy.setEnabled(false);
            setVisibility(VISIBLE);
        } else {
            containerNewButtonBuy.setVisibility(GONE);
            containerButtonBuy.setVisibility(VISIBLE);
            tvBuy.setBackgroundResource(R.drawable.btn_buy_grey);
            containerButtonBuy.setBackgroundResource(R.drawable.btn_buy_grey);
            tvBuy.setTextColor(ContextCompat.getColor(getContext(),R.color.black_38));
            if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                    && !data.getPreOrder().getPreorderStatus().equals("0")
                    && !data.getPreOrder().getPreorderProcessTime().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
                tvBuy.setText(getContext().getString(R.string.title_pre_order));
            } else {
                tvBuy.setText(getContext().getString(R.string.title_buy));
            }
            tvBuy.setEnabled(false);
            containerButtonBuy.setEnabled(false);
            setVisibility(VISIBLE);
        }
    }

    private class PromoteClick implements OnClickListener {
        private final ProductDetailData data;

        PromoteClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductManagePromoteClicked(data);
        }
    }

}
