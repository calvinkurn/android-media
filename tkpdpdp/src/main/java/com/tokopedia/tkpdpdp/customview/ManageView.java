package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class ManageView extends BaseView<ProductDetailData, ProductDetailView> {
    private TextView tvPromote;
    private TextView tvEdit;
    private TextView tvSoldOut;
    private TextView tvToEtalase;

    public ManageView(Context context) {
        super(context);
    }

    public ManageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_manage_product_info;
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
        tvPromote = (TextView) findViewById(R.id.tv_promote);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvSoldOut = (TextView) findViewById(R.id.tv_sold_out);
        tvToEtalase = (TextView) findViewById(R.id.tv_to_etalase);

    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        switch (data.getInfo().getProductStatus()) {
            case "1":
                hideToEtalase();
                break;
            case "3":
                hideToWareHouse();
                break;
            case "-1":
                hideAll();
                break;
        }
        tvPromote.setVisibility(data.getShopInfo().getShopStatus() != 1 ? GONE : VISIBLE);
        setVisibility(data.getShopInfo().getShopIsOwner() == 1
                || data.getShopInfo().getShopIsAllowManage() == 1 ? View.VISIBLE : GONE);
        tvPromote.setOnClickListener(new PromoteClick(data));
        tvEdit.setOnClickListener(new EditClick(data));
        tvToEtalase.setOnClickListener(new ToEtalaseClick(data));
        tvSoldOut.setOnClickListener(new SoldOutClick(data));
    }

    public void hideToEtalase() {
        tvToEtalase.setVisibility(GONE);
        tvSoldOut.setVisibility(VISIBLE);
        tvPromote.setVisibility(VISIBLE);
    }

    public void hideToWareHouse() {
        tvToEtalase.setVisibility(VISIBLE);
        tvSoldOut.setVisibility(GONE);
        tvPromote.setVisibility(GONE);
    }

    public void hideAll() {
        tvToEtalase.setVisibility(GONE);
        tvSoldOut.setVisibility(GONE);
        tvPromote.setVisibility(GONE);
        tvEdit.setVisibility(GONE);
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

    private class EditClick implements OnClickListener {
        private final ProductDetailData data;

        EditClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("is_edit", true);
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            listener.onProductManageEditClicked(bundle);
        }
    }

    private class ToEtalaseClick implements OnClickListener {
        private final ProductDetailData data;

        ToEtalaseClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (data.getProductImages().isEmpty()) {
                listener.showToastMessage("Gambar harus diisi");
            } else {

            }
        }
    }

    private class SoldOutClick implements OnClickListener {
        private final ProductDetailData data;

        SoldOutClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductManageSoldOutClicked(data.getInfo().getProductId());
        }
    }
}
