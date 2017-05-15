package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import butterknife.BindView;


/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class HeaderInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = HeaderInfoView.class.getSimpleName();

    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.tv_viewed)
    TextView tvViewed;
    @BindView(R2.id.tv_brought)
    TextView tvBrought;
    @BindView(R2.id.label_cashback)
    TextView cashbackTextView;
    @BindView(R2.id.cashback_holder)
    LinearLayout cashbackHolder;
    @BindView(R2.id.title_viewed)
    TextView titleViewed;
    @BindView(R2.id.title_sold)
    TextView titleSold;

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
    protected int getLayoutView() {
        return R.layout.view_header_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
        tvBrought.setText("");
        tvViewed.setText("");
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        tvPrice.setText(data.getInfo().getProductPrice());
        titleViewed.setVisibility(VISIBLE);
        titleSold.setVisibility(VISIBLE);
        tvBrought.setText(data.getStatistic().getProductSoldCount());
        tvViewed.setText(data.getStatistic().getProductViewCount());
        setVisibility(VISIBLE);

        if(data.getCashBack() !=null && !data.getCashBack().getProductCashback().isEmpty()) {
            cashbackHolder.setVisibility(VISIBLE);
            cashbackTextView.setText(getContext().getString(R.string.value_cashback)
                    .replace("X", data.getCashBack().getProductCashback()));
        }

    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }
}
