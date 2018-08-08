package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductBreadcrumb;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class DetailInfoView extends BaseView<ProductDetailData, ProductDetailView> {

    private TextView tvCategory;
    private TextView tvMinOrder;
    private TextView tvCatalog;
    private TextView tvEtalase;
    private TextView tvCondition;
    private TextView tvReturnable;
    private TableRow catalogView;
    private TextView tvPreOrder;
    private TextView tvSuccessRate;
    private TableRow returnableView;
    private TableRow preOrderView;

    public DetailInfoView(Context context) {
        super(context);
    }

    public DetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_detail_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
        tvCategory.setVisibility(GONE);
        catalogView.setVisibility(GONE);
        returnableView.setVisibility(GONE);
        preOrderView.setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                && !data.getPreOrder().getPreorderStatus().equals("0")
                && !data.getPreOrder().getPreorderProcessTime().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
            tvPreOrder.setText(String.format("%s %s %s", "Waktu Proses",
                    data.getPreOrder().getPreorderProcessTime(),
                    data.getPreOrder().getPreorderProcessTimeTypeString()));
            preOrderView.setVisibility(VISIBLE);
        }

        final List<ProductBreadcrumb> productDepartments = data.getBreadcrumb();
        if (productDepartments.size()>0) {
            ProductBreadcrumb productBreadcrumb = productDepartments.get(productDepartments.size()-1);

            tvCategory.setText(MethodChecker.fromHtml(productBreadcrumb.getDepartmentName()));
            tvCategory.setTextColor(ContextCompat.getColor(getContext(), listener.isSellerApp() ? R.color.font_black_primary_70 : R.color.tkpd_main_green));
            tvCategory.setOnClickListener(new CategoryClick(productBreadcrumb));
            tvCategory.setVisibility(VISIBLE);
        }
        if (data.getInfo().getProductCatalogId() != null
                && data.getInfo().getProductCatalogName() != null
                && data.getInfo().getProductCatalogUrl() != null
                && !data.getInfo().getProductCatalogId().equals("")
                && !data.getInfo().getProductCatalogName().equals("")
                && !data.getInfo().getProductCatalogUrl().equals("")
                && !data.getInfo().getProductCatalogId().equals("0")
                && !data.getInfo().getProductCatalogName().equals("0")
                && !data.getInfo().getProductCatalogUrl().equals("0")) {
            catalogView.setVisibility(VISIBLE);
            tvCatalog.setText(MethodChecker.fromHtml(data.getInfo().getProductCatalogName()));
            tvCatalog.setTextColor(ContextCompat.getColor(getContext(), listener.isSellerApp() ? R.color.font_black_primary_70 : R.color.tkpd_main_green));
            tvCatalog.setOnClickListener(new CatalogClick(data));
        } else {
            catalogView.setVisibility(GONE);
        }
        showReturnable(data.getInfo().getProductReturnable(), data.getShopInfo().getShopHasTerms());
        tvMinOrder.setText(data.getInfo().getProductMinOrder().replace(".",""));
        if (data.getInfo().getProductEtalase() != null) {
            tvEtalase.setText(MethodChecker.fromHtml(data.getInfo().getProductEtalase()));
            tvEtalase.setOnClickListener(new EtalaseClick(data));
        }
        tvCondition.setText(data.getInfo().getProductCondition());
        tvSuccessRate.setText(data.getStatistic().getProductSuccessRate()+"%");

        setVisibility(VISIBLE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvCategory = (TextView) findViewById(R.id.tv_category_1);
        tvMinOrder = (TextView) findViewById(R.id.tv_minimum);
        tvCatalog = (TextView) findViewById(R.id.tv_catalog);
        tvEtalase = (TextView) findViewById(R.id.tv_etalase);
        tvCondition = (TextView) findViewById(R.id.tv_condition);
        tvReturnable = (TextView) findViewById(R.id.tv_returnable);
        catalogView = (TableRow) findViewById(R.id.tr_catalog);
        tvPreOrder = (TextView) findViewById(R.id.tv_preorder);
        returnableView = (TableRow) findViewById(R.id.tr_returnable);
        preOrderView = (TableRow) findViewById(R.id.tr_preorder);
        tvSuccessRate = (TextView) findViewById(R.id.tv_success_rate);
    }

    private void showReturnable(int returnableState, int shopHasTerms) {
        if (shopHasTerms != 0) {
            switch (returnableState) {
                case 1:
                    tvReturnable.setText(getContext().getString(R.string.title_yes));
                    returnableView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvReturnable.setText(getContext().getString(com.tokopedia.core.R.string.title_no));
                    break;
                default:
                    returnableView.setVisibility(View.GONE);
                    tvReturnable.setText(getContext().getString(com.tokopedia.core.R.string.return_no_policy));
                    break;
            }
        } else {
            returnableView.setVisibility(View.GONE);
        }

    }

    private class CategoryClick implements OnClickListener {

        private final ProductBreadcrumb data;

        CategoryClick(ProductBreadcrumb productBreadcrumb) {
            this.data = productBreadcrumb;
        }

        @Override
        public void onClick(View v) {
            if (!listener.isSellerApp()) {
                Bundle bundle = new Bundle();
                bundle.putString(BrowseProductRouter.DEPARTMENT_ID, data.getDepartmentId());
                bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
                bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_DIRECTORY);
                bundle.putString(BrowseProductRouter.EXTRA_SOURCE, TopAdsApi.SRC_DIRECTORY);
                listener.onProductDepartmentClicked(bundle);
            }
        }
    }

    private class EtalaseClick implements OnClickListener {
        private final ProductDetailData data;

        EtalaseClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("etalase_name", MethodChecker.fromHtml(data.getInfo().getProductEtalase()).toString());
            bundle.putString("etalase_id", data.getInfo().getProductEtalaseId());
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            listener.onProductEtalaseClicked(bundle);
        }
    }

    private class CatalogClick implements OnClickListener {
        private final ProductDetailData data;

        CatalogClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (!listener.isSellerApp()) {
                listener.onProductCatalogClicked(data.getInfo().getProductCatalogId());
            }
        }
    }
}
