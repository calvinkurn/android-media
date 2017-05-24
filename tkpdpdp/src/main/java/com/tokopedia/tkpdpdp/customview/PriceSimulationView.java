package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.InstallmentActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.WholesaleActivity;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;

public class PriceSimulationView extends BaseView<ProductDetailData, ProductDetailView> {

    private LinearLayout wholesaleLayout;
    private TextView tvWholesale;
    private LinearLayout installmentLayout;
    private TextView tvInstallment;
    private View separator;

    boolean isInstallment = false;
    boolean isWholesale = false;

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
        wholesaleLayout = (LinearLayout) findViewById(R.id.wholesale);
        tvWholesale = (TextView) findViewById(R.id.tv_wholesale);
        installmentLayout = (LinearLayout) findViewById(R.id.installmet);
        tvInstallment = (TextView) findViewById(R.id.tv_installmet);
        separator = findViewById(R.id.separator);

    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {

        if (data.getInfo().getProductInstallments() != null && data.getInfo().getProductInstallments().size() > 0) {
            installmentLayout.setVisibility(VISIBLE);
            tvInstallment.setText("Bunga " + data.getInfo().getInstallmentMinPercentage() + " mulai dari "
                    + data.getInfo().getInstallmentMinPrice());
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
            tvWholesale.setText("Mulai dari " + data.getInfo().getWholseSaleMinPrice());
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
        if (isWholesale && isInstallment) separator.setVisibility(VISIBLE);

        setVisibility(VISIBLE);
    }
}
