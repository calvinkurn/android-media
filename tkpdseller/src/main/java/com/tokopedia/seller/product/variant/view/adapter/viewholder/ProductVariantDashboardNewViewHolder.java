package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */
public class ProductVariantDashboardNewViewHolder extends BaseViewHolder<ProductVariantDashboardNewViewModel> {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView contentTextView;

    public ProductVariantDashboardNewViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        contentTextView = (TextView) itemView.findViewById(R.id.text_view_content);
    }

    @Override
    public void bindObject(final ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel) {
//        if (!TextUtils.isEmpty(variantManageViewModel.getImageUrl())) {
//            imageView.setBackgroundColor(Color.TRANSPARENT);
//            Glide.with(imageView.getContext()).load(variantManageViewModel.getImageUrl()).into(imageView);
//        } else if (!TextUtils.isEmpty(variantManageViewModel.getHexCode())) {
//            imageView.setBackgroundColor(Color.parseColor(variantManageViewModel.getHexCode()));
//            imageView.setImageDrawable(null);
//        } else if (!TextUtils.isEmpty(variantManageViewModel.getIconUrl())) {
//            imageView.setBackgroundColor(Color.TRANSPARENT);
//            Glide.with(imageView.getContext()).load(variantManageViewModel.getIconUrl()).into(imageView);
//        } else {
//            imageView.setBackgroundColor(Color.LTGRAY);
//            imageView.setImageDrawable(null);
//        }
//        titleTextView.setText(variantManageViewModel.getTitle());
//        String content = variantManageViewModel.getContent();
//        if (TextUtils.isEmpty(content)) {
//            if (variantManageViewModel.isStockAvailable()) {
//                content = contentTextView.getContext().getString(R.string.product_variant_stock_available);
//            } else {
//                content = contentTextView.getContext().getString(R.string.product_variant_stock_empty);
//            }
//        }
        contentTextView.setText(productVariantDashboardNewViewModel.getProductVariantOptionChildLv1().getValue());
    }
}