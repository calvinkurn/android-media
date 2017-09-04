package com.tokopedia.tkpdpdp.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
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
    private LinearLayout container;

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
        tvBuy = (TextView) findViewById(R.id.tv_buy);
        tvPromoTopAds = (TextView) findViewById(R.id.tv_promote_topads);
        container = (LinearLayout) findViewById(R.id.container);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        if (data.getShopInfo().getShopIsOwner() == 1
                || (data.getShopInfo().getShopIsAllowManage() == 1 || GlobalConfig.isSellerApp())) {
            tvBuy.setText(getContext().getString(R.string.title_promo_per_hour));
            tvBuy.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_500));
            tvPromoTopAds.setVisibility(VISIBLE);
            tvBuy.setBackgroundResource(R.drawable.btn_promo_ads);
            tvBuy.setOnClickListener(new PromoteClick(data));
            tvPromoTopAds.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPromoAdsClicked();
                    }
                }
            });
        } else {
            if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                    && !data.getPreOrder().getPreorderStatus().equals("0")
                    && !data.getPreOrder().getPreorderProcessTime().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                    && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
                tvBuy.setText(getContext().getString(R.string.title_pre_order));
            } else {
                tvBuy.setText(getContext().getString(R.string.title_buy));
            }
            tvBuy.setBackgroundResource(R.drawable.btn_buy);
            tvPromoTopAds.setVisibility(GONE);
            tvBuy.setOnClickListener(new ClickBuy(data));
        }

        if ((data.getInfo().getProductStatus().equals(PRD_STATE_WAREHOUSE))
                || data.getShopInfo().getShopStatus() != 1) {
            setVisibility(GONE);
        } else {
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

    private class ClickBuy implements OnClickListener {
        private final ProductDetailData data;

        ClickBuy(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (SessionHandler.isV4Login(getContext())) {
                String weightProduct = "";
                switch (data.getInfo().getProductWeightUnit()) {
                    case "gr":
                        weightProduct = String.valueOf((Float.parseFloat(data.getInfo()
                                .getProductWeight())) / 1000);
                        break;
                    case "kg":
                        weightProduct = data.getInfo().getProductWeight();
                        break;
                }
                ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                        .setImageUri(data.getProductImages().get(0).getImageSrc300())
                        .setMinOrder(Integer.parseInt(data.getInfo().getProductMinOrder()))
                        .setProductId(String.valueOf(data.getInfo().getProductId()))
                        .setProductName(data.getInfo().getProductName())
                        .setWeight(weightProduct)
                        .setShopId(data.getShopInfo().getShopId())
                        .setPrice(data.getInfo().getProductPrice())
                        .build();
                if (!data.getBreadcrumb().isEmpty())
                    pass.setProductCategory(data.getBreadcrumb().get(0).getDepartmentName());

                listener.onProductBuySessionLogin(pass);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("login", true);
                listener.onProductBuySessionNotLogin(bundle);
            }
        }
    }
}
