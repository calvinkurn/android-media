package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageListViewHolder extends BaseViewHolder<ProductManageViewModel> {

    public interface ClickOptionCallbackHolder{
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ImageView imageProduct;
    private TextView titleProduct;
    private ClickOptionCallbackHolder clickOptionCallbackHolder;

    public ProductManageListViewHolder(View layoutView) {
        super(layoutView);
        imageProduct = (ImageView) layoutView.findViewById(R.id.image_product_manage);
        titleProduct = (TextView) layoutView.findViewById(R.id.title_product_manage);
    }

    @Override
    public void bindObject(final ProductManageViewModel productManageViewModel) {
        ImageHandler.loadImageRounded2(
                imageProduct.getContext(),
                imageProduct,
                productManageViewModel.getImageUrl()
        );
        titleProduct.setText(productManageViewModel.getProductName());
        imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickOptionCallbackHolder != null){
                    clickOptionCallbackHolder.onClickOptionItem(productManageViewModel);
                }
            }
        });
    }

    public void bindFeaturedProduct(boolean isFeaturedProduct) {

    }

    public void setClickOptionCallbackHolder(ClickOptionCallbackHolder clickOptionCallbackHolder) {
        this.clickOptionCallbackHolder = clickOptionCallbackHolder;
    }
}
