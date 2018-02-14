package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.InstallmentActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.VariantActivity;
import com.tokopedia.tkpdpdp.WholesaleActivity;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;

public class PriceSimulationView extends BaseView<ProductDetailData, ProductDetailView> {

    private LinearLayout variantLayout;
    private TextView tvVariant;
    private LinearLayout wholesaleLayout;
    private TextView tvWholesale;
    private LinearLayout installmentLayout;
    private View separator1;
    private View separator2;

    boolean isInstallment = false;
    boolean isWholesale = false;

    private Context context;

    private static final String PERCENTAGE = "%";
    private static final String CURRENCY = "Rp";

    public PriceSimulationView(Context context) {
        super(context);
    }

    public PriceSimulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_price_simulation;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        this.context = context;
        variantLayout = (LinearLayout) findViewById(R.id.variant);
        tvVariant = (TextView) findViewById(R.id.variant_title);
        wholesaleLayout = (LinearLayout) findViewById(R.id.wholesale);
        tvWholesale = (TextView) findViewById(R.id.tv_wholesale);
        installmentLayout = (LinearLayout) findViewById(R.id.installmet);
        separator1 = findViewById(R.id.separator1);
        separator2 = findViewById(R.id.separator2);

    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        String titleInstallment = getContext().getString(R.string.title_installment_pdp);
        String titleWholesale = getContext().getString(R.string.title_grosir);
        if (data.getInfo().getProductInstallments()
                != null && data.getInfo().getProductInstallments().size() > 0) {
            installmentLayout.setVisibility(VISIBLE);
            String installmentMinPercentage = data.getInfo().getInstallmentMinPercentage();
            String installmentMinPrice = data.getInfo().getInstallmentMinPrice();
            String installment = titleInstallment.replace(PERCENTAGE,installmentMinPercentage)
                    .replace(CURRENCY,installmentMinPrice);
            isInstallment = true;
            installmentLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(InstallmentActivity.KEY_INSTALLMENT_DATA,
                            new ArrayList<>(data.getInfo().getProductInstallments()));
                    listener.onInstallmentClicked(bundle);
                }
            });
        }
        if (data.getWholesalePrice() != null && data.getWholesalePrice().size() > 0) {
            wholesaleLayout.setVisibility(VISIBLE);
            String wholseSaleMinPrice = data.getInfo().getWholseSaleMinPrice();
            String wholeSale = titleWholesale.replace(CURRENCY,wholseSaleMinPrice);
            tvWholesale.setText(wholeSale);
            isWholesale = true;
            wholesaleLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(WholesaleActivity.KEY_WHOLESALE_DATA,
                            new ArrayList<>(data.getWholesalePrice()));
                    listener.onWholesaleClicked(bundle);
                }
            });
        }
        if (isWholesale && isInstallment) separator2.setVisibility(VISIBLE);

        setVisibility(VISIBLE);
    }

    public void addProductVariant(final ProductVariant productVariant, final ProductDetailData productDetailData) {
        tvVariant.setText(getContext().getString(R.string.choose_variant));
        variantLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(VariantActivity.KEY_VARIANT_DATA, productVariant);
                bundle.putParcelable(VariantActivity.KEY_PRODUCT_DETAIL_DATA, productDetailData);
                listener.onVariantClicked(bundle);

            }
        });
        variantLayout.setVisibility(VISIBLE);
        separator1.setVisibility(VISIBLE);
    }

    public void updateVariant(String variantSelected){
        tvVariant.setText(variantSelected);
        tvVariant.setTextColor(ContextCompat.getColor(context,R.color.medium_green));
    }

    public View getVariantView() {
        return variantLayout;
    }
}
