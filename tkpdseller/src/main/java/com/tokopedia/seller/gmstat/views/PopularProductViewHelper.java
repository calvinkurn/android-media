package com.tokopedia.seller.gmstat.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.utils.KMNumbers;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by normansyahputa on 11/9/16.
 */

public class PopularProductViewHelper {
    private final View itemView;

    ImageView dataProductIcon;

    TextView dataProductTitle;

    ImageView imagePopularProduct;

    TextView textPopularProduct;

    TextView popularProductDescription;

    TextView numberOfSelling;

    TextView xSold;
    private GetPopularProduct getPopularProduct;

    TextView footerPopularProduct;

    LinearLayout popularProductEmptyState;

    View separator2;

    public void moveToAddProduct(){
        ProductActivity.moveToAddProduct(itemView.getContext());
    }

    public void gotoProductDetail(){
        if(getPopularProduct == null)
            return;

        itemView.getContext().startActivity(ProductInfoActivity.createInstance(
                itemView.getContext(), getPopularProduct.getProductId()+""));
    }

    public PopularProductViewHelper(View itemView){
        ButterKnife.bind(this, itemView);
        initView(itemView);
        this.itemView = itemView;

        String categoryBold = String.format("<i>%s</i>", "Data dalam 30 hari terakhir");
        footerPopularProduct.setText(MethodChecker.fromHtml(categoryBold));
    }

    private void initView(View itemView) {
        dataProductIcon= (ImageView) itemView.findViewById(R.id.data_product_icon);

        dataProductTitle= (TextView) itemView.findViewById(R.id.data_product_title);

        imagePopularProduct= (ImageView) itemView.findViewById(R.id.image_popular_product);

        textPopularProduct= (TextView) itemView.findViewById(R.id.text_popular_product);

        popularProductDescription= (TextView) itemView.findViewById(R.id.popular_product_description);

        numberOfSelling= (TextView) itemView.findViewById(R.id.number_of_selling);

        xSold= (TextView) itemView.findViewById(R.id.x_sold);

        footerPopularProduct= (TextView) itemView.findViewById(R.id.footer_popular_product);

        popularProductEmptyState= (LinearLayout) itemView.findViewById(R.id.popular_product_empty_state);

        separator2= itemView.findViewById(R.id.separator_2);

        itemView.findViewById(R.id.popular_product_empty_state)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToAddProduct();
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
    }

    public void bindData(GetPopularProduct getPopularProduct, ImageHandler imageHandler){
        this.getPopularProduct = getPopularProduct;
        if(getPopularProduct == null || getPopularProduct.getProductId() == 0){
            popularProductEmptyState.setVisibility(View.VISIBLE);
            separator2.setVisibility(View.GONE);
            return;
        }else{
            popularProductEmptyState.setVisibility(View.GONE);
            separator2.setVisibility(View.VISIBLE);
        }

        dataProductTitle.setText(R.string.data_product_title);
        textPopularProduct.setText(R.string.popular_product_title);
        imageHandler.loadImage(imagePopularProduct, getPopularProduct.getImageLink());
        popularProductDescription.setText(MethodChecker.fromHtml(getPopularProduct.getProductName()));
        long sold = getPopularProduct.getSold();
        String text = getFormattedString(sold);
//        numberOfSelling.setText(toKFormat(getPopularProduct.getSold()));
        numberOfSelling.setText(text);
        xSold.setText(R.string.number_of_selled);
    }

    public static String getFormattedString(long value) {
        String text = "";
        if( value < 1_000_000){
            Locale locale = new Locale("in", "ID");
            NumberFormat currencyFormatter = NumberFormat.getNumberInstance(locale);
            System.out.println(text = (currencyFormatter.format(value)));
//                text = successTrans+"";
        }else if(value >= 1_000_000){
            text = KMNumbers.formatNumbers(value);
        }
        return text;
    }
}
