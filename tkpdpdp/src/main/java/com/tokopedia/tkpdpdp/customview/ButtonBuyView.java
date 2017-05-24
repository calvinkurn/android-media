package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * Created by Angga.Prasetiyo on 02/11/2015.
 */
public class ButtonBuyView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = ButtonBuyView.class.getSimpleName();

    private TextView tvBuy;

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

    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        if (data.getShopInfo().getShopIsOwner() == 1 || data.getShopInfo().getShopStatus() != 1
                || (data.getInfo().getProductStatus().equals("3") & data.getShopInfo().getShopStatus() == 1)
                || (data.getShopInfo().getShopIsAllowManage() == 1 || GlobalConfig.isSellerApp())) {
            tvBuy.setText(getContext().getString(R.string.title_promo));
            setOnClickListener(new PromoteClick(data));
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
            setOnClickListener(new ClickBuy(data));
        }
        setVisibility(VISIBLE);
    }

    private class PromoteClick implements OnClickListener {
        private final ProductDetailData data;

        public PromoteClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductManagePromoteClicked(data);
        }
    }

    private class ClickBuy implements OnClickListener {
        private final ProductDetailData data;

        public ClickBuy(ProductDetailData data) {
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
