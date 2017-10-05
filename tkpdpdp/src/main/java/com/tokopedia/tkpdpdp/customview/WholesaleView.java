package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.R2;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;

import butterknife.BindViews;

/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class WholesaleView extends BaseView<ProductDetailData, ProductDetailView> {
    @BindViews({
            R2.id.wholesale_1,
            R2.id.wholesale_2,
            R2.id.wholesale_3,
            R2.id.wholesale_4,
            R2.id.wholesale_5})
    List<TableRow> rowWholesale;
    @BindViews({R2.id.qty_ws_1, R2.id.qty_ws_2, R2.id.qty_ws_3, R2.id.qty_ws_4, R2.id.qty_ws_5})
    List<TextView> wholesaleQty;
    @BindViews({
            R2.id.price_ws_1,
            R2.id.price_ws_2,
            R2.id.price_ws_3,
            R2.id.price_ws_4,
            R2.id.price_ws_5})
    List<TextView> wholesalePrice;

    public WholesaleView(Context context) {
        super(context);
    }

    public WholesaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_wholesale_price_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
        for (TableRow tableRow : rowWholesale) {
            tableRow.setVisibility(GONE);
        }
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        List<ProductWholesalePrice> wholesalePrices = data.getWholesalePrice();
        setVisibility(wholesalePrices.isEmpty() ? GONE : VISIBLE);

        int length = rowWholesale.size() <= wholesalePrices.size() ?
                rowWholesale.size() : wholesalePrices.size();
        for (int i = 0; i < length; i++) {
            if (i == wholesalePrices.size() - 1) {
                wholesaleQty.get(i)
                        .setText(String.format(">= %s", wholesalePrices.get(i).getWholesaleMin()));
            } else {
                wholesaleQty.get(i)
                        .setText(String.format("%s - %s", wholesalePrices.get(i).getWholesaleMin(),
                        data.getWholesalePrice().get(i).getWholesaleMax()));
            }
            wholesalePrice.get(i).setText(wholesalePrices.get(i).getWholesalePrice());
            rowWholesale.get(i).setVisibility(View.VISIBLE);
        }
    }
}
