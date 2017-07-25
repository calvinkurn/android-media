package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;

/**
 * Created by normansyahputa on 11/9/16.
 */

public class PopularProductViewHolder {
    private final TitleCardView popularProductCardView;
    private ImageView ivPopularProduct;
    private TextView tvPopularProductDescription;
    private TextView tvNoOfSelling;
    private GetPopularProduct getPopularProduct;

    public PopularProductViewHolder(TitleCardView popularProductCardView) {
        initView(popularProductCardView);
        this.popularProductCardView = popularProductCardView;
    }

    private void initView(TitleCardView titleCardView) {
        ivPopularProduct = (ImageView) titleCardView.findViewById(R.id.image_popular_product);
        tvPopularProductDescription = (TextView) titleCardView.findViewById(R.id.tv_popular_product);
        tvNoOfSelling = (TextView) titleCardView.findViewById(R.id.tv_no_of_selling);

        View.OnClickListener goToProductDetailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductDetail();
            }
        };
        ivPopularProduct.setOnClickListener( goToProductDetailClickListener);
        tvPopularProductDescription.setOnClickListener( goToProductDetailClickListener);
    }

    private void clickAddProductTracking(){
        UnifyTracking.eventClickAddProduct();
    }

    private void clickGMStat(){
        if(getPopularProduct != null){
            UnifyTracking.eventClickGMStatProduct(getPopularProduct.getProductName());
        }
    }

    public void bindData(GetPopularProduct getPopularProduct) {
        popularProductCardView.setLoadingState(false);

        if (getPopularProduct == null || getPopularProduct.getProductId() == 0) {
            setEmptyState();
            return;
        } else {
            popularProductCardView.setEmptyState(false);
        }
        this.getPopularProduct = getPopularProduct;

        new ImageHandler(popularProductCardView.getContext()).loadImage(ivPopularProduct, getPopularProduct.getImageLink());
        tvPopularProductDescription.setText(MethodChecker.fromHtml(getPopularProduct.getProductName()));
        long sold = getPopularProduct.getSold();
        String text = KMNumbers.getFormattedString(sold);
        tvNoOfSelling.setText(text);
    }

    private void setEmptyState(){
        popularProductCardView.setEmptyViewRes(R.layout.widget_popular_product_empty);
        popularProductCardView.getEmptyView().findViewById(R.id.add_product_popular_product)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToAddProduct();

                        // analytic below : https://phab.tokopedia.com/T18496
                        clickAddProductTracking();
                    }
                });
        popularProductCardView.setEmptyState(true);
    }

    public void gotoProductDetail() {
        if (getPopularProduct == null)
            return;

        popularProductCardView.getContext().startActivity(ProductDetailRouter
                .createInstanceProductDetailInfoActivity(
                        popularProductCardView.getContext(),
                        getPopularProduct.getProductId() + ""));

        // analytic below : https://phab.tokopedia.com/T18496
        clickGMStat();
    }

    public void moveToAddProduct() {
        Intent intent = new Intent(popularProductCardView.getContext(), ProductAddActivity.class);
        popularProductCardView.getContext().startActivity(intent);
    }

    public void showLoading(){
        popularProductCardView.setLoadingState(true);
    }

    public void hideLoading(){
        popularProductCardView.setLoadingState(false);
    }
}
