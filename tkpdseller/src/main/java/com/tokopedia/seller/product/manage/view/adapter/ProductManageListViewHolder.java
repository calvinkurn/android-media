package com.tokopedia.seller.product.manage.view.adapter;

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

    public interface ClickOptionCallbackHolder {
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ImageView productImageView;
    private TextView titleTextView;
    private TextView stockTextView;
    private TextView priceTextView;
    private ImageView featuredImageView;
    private TextView cashbackTextView;
    private TextView wholesaleTextView;
    private TextView poTextView;
    private ImageView freeReturnImageView;
    private ClickOptionCallbackHolder clickOptionCallbackHolder;

    public ProductManageListViewHolder(View layoutView) {
        super(layoutView);
        productImageView = (ImageView) layoutView.findViewById(R.id.image_view_product);
        titleTextView = (TextView) layoutView.findViewById(R.id.text_view_title);
        stockTextView = (TextView) layoutView.findViewById(R.id.text_view_stock);
        priceTextView = (TextView) layoutView.findViewById(R.id.text_view_price);

        featuredImageView = (ImageView) layoutView.findViewById(R.id.image_view_featured);
        cashbackTextView = (TextView) layoutView.findViewById(R.id.text_view_cashback);
        wholesaleTextView = (TextView) layoutView.findViewById(R.id.text_view_wholesale);
        poTextView = (TextView) layoutView.findViewById(R.id.text_view_po);
        freeReturnImageView = (ImageView) layoutView.findViewById(R.id.image_view_free_return);
    }

    @Override
    public void bindObject(final ProductManageViewModel productManageViewModel) {
        ImageHandler.loadImageRounded2(
                productImageView.getContext(),
                productImageView,
                productManageViewModel.getImageUrl()
        );
        titleTextView.setText(productManageViewModel.getProductName());
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickOptionCallbackHolder != null) {
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
