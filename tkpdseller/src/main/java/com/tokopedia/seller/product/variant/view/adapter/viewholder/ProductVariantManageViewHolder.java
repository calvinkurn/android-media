package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantManageViewModel;
import com.tokopedia.seller.topads.dashboard.view.helper.CircleTransform;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantManageViewHolder extends BaseViewHolder<ProductVariantManageViewModel> {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView contentTextView;

    public ProductVariantManageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        contentTextView = (TextView) itemView.findViewById(R.id.text_view_content);
    }

    @Override
    public void bindObject(final ProductVariantManageViewModel productVariantManageViewModel) {
        if (!TextUtils.isEmpty(productVariantManageViewModel.getHexCode())) {
            imageView.setBackgroundColor(Color.parseColor(productVariantManageViewModel.getHexCode()));
            imageView.setImageDrawable(null);
        } else if (!TextUtils.isEmpty(productVariantManageViewModel.getImageUrl())) {
            imageView.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(imageView.getContext()).load(productVariantManageViewModel.getImageUrl())
                    .transform(new CircleTransform(imageView.getContext())).into(imageView);
        } else {
            imageView.setBackgroundColor(Color.GRAY);
            imageView.setImageDrawable(null);
        }
        titleTextView.setText(productVariantManageViewModel.getTitle());
        contentTextView.setText(productVariantManageViewModel.getContent());
    }
}