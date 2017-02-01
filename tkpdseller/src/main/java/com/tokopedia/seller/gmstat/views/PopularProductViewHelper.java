package com.tokopedia.seller.gmstat.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.utils.KMNumbers;

import java.text.NumberFormat;
import java.util.Locale;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.LAST_THIRTY_DAYS_AGO_FORMAT;

/**
 * Created by normansyahputa on 11/9/16.
 */

public class PopularProductViewHelper {
    private static final Locale locale = new Locale("in", "ID");
    private final View itemView;
    private TextView dataProductTitle;
    private ImageView imagePopularProduct;
    private TextView textPopularProduct;
    private TextView popularProductDescription;
    private TextView numberOfSelling;
    private TextView xSold;
    private GetPopularProduct getPopularProduct;
    private TextView footerPopularProduct;
    private LinearLayout popularProductEmptyState;
    private View separator2;
    private String lastThirtyDaysAgo;

    public PopularProductViewHelper(View itemView) {
        initView(itemView);
        this.itemView = itemView;

        String categoryBold = String.format(LAST_THIRTY_DAYS_AGO_FORMAT, lastThirtyDaysAgo);
        footerPopularProduct.setText(MethodChecker.fromHtml(categoryBold));
    }

    public static String getFormattedString(long value) {
        String text = "";
        if (value < 1_000_000) {
            NumberFormat currencyFormatter = NumberFormat.getNumberInstance(locale);
            text = currencyFormatter.format(value);
        } else if (value >= 1_000_000) {
            text = KMNumbers.formatNumbers(value);
        }
        return text;
    }

    public void moveToAddProduct() {
        ProductActivity.moveToAddProduct(itemView.getContext());
    }

    public void gotoProductDetail() {
        if (getPopularProduct == null)
            return;

        itemView.getContext().startActivity(ProductInfoActivity.createInstance(
                itemView.getContext(), getPopularProduct.getProductId() + ""));

        // analytic below : https://phab.tokopedia.com/T18496
        clickGMStat();
    }

    private void initView(View itemView) {

        dataProductTitle = (TextView) itemView.findViewById(R.id.data_product_title);

        imagePopularProduct = (ImageView) itemView.findViewById(R.id.image_popular_product);

        textPopularProduct = (TextView) itemView.findViewById(R.id.text_popular_product);

        popularProductDescription = (TextView) itemView.findViewById(R.id.popular_product_description);

        numberOfSelling = (TextView) itemView.findViewById(R.id.number_of_selling);

        xSold = (TextView) itemView.findViewById(R.id.x_sold);

        footerPopularProduct = (TextView) itemView.findViewById(R.id.footer_popular_product);

        popularProductEmptyState = (LinearLayout) itemView.findViewById(R.id.popular_product_empty_state);

        separator2 = itemView.findViewById(R.id.separator_2);

        itemView.findViewById(R.id.popular_product_empty_state)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToAddProduct();

                        // analytic below : https://phab.tokopedia.com/T18496
                        clickAddProductTracking();
                    }
                });

        itemView.findViewById(R.id.image_popular_product).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoProductDetail();
                    }
                }
        );
        itemView.findViewById(R.id.text_popular_product).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoProductDetail();
                    }
                }
        );
        itemView.findViewById(R.id.data_product_title).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoProductDetail();
                    }
                }
        );
        itemView.findViewById(R.id.popular_product_description).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoProductDetail();
                    }
                }
        );

        lastThirtyDaysAgo = itemView.getContext().getString(R.string.last_thirty_days_ago);
    }

    private void clickAddProductTracking(){
        UnifyTracking.eventClickAddProduct();
    }

    private void clickGMStat(){
        if(getPopularProduct != null){
            UnifyTracking.eventClickGMStatProduct(getPopularProduct.getProductName());
        }
    }

    public void bindData(GetPopularProduct getPopularProduct, ImageHandler imageHandler) {
        this.getPopularProduct = getPopularProduct;
        if (getPopularProduct == null || getPopularProduct.getProductId() == 0) {
            popularProductEmptyState.setVisibility(View.VISIBLE);
            separator2.setVisibility(View.GONE);
            return;
        } else {
            popularProductEmptyState.setVisibility(View.GONE);
            separator2.setVisibility(View.VISIBLE);
        }

        dataProductTitle.setText(R.string.data_product_title);
        textPopularProduct.setText(R.string.popular_product_title);
        imageHandler.loadImage(imagePopularProduct, getPopularProduct.getImageLink());
        popularProductDescription.setText(MethodChecker.fromHtml(getPopularProduct.getProductName()));
        long sold = getPopularProduct.getSold();
        String text = getFormattedString(sold);
        numberOfSelling.setText(text);
        xSold.setText(R.string.number_of_selled);
    }
}
