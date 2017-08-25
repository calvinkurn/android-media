package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;
import com.tokopedia.seller.topads.dashboard.view.helper.CircleTransform;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantItemPickerCacheViewHolder extends BaseItemPickerCacheViewHolder<ProductVariantViewModel> {

    private ImageView imageView;
    private TextView titleTextView;
    private ImageButton closeImageButton;

    public ProductVariantItemPickerCacheViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        closeImageButton = (ImageButton) itemView.findViewById(R.id.image_button_close);
    }

    @Override
    public void bindObject(final ProductVariantViewModel productVariantViewModel) {
        if (!TextUtils.isEmpty(productVariantViewModel.getHexCode())) {
            imageView.setColorFilter(Color.parseColor(productVariantViewModel.getHexCode()), PorterDuff.Mode.SRC_ATOP);
            imageView.setImageResource(R.drawable.circle_white);
        } else if (!TextUtils.isEmpty(productVariantViewModel.getImageUrl())) {
            imageView.clearColorFilter();
            Glide.with(imageView.getContext()).load(productVariantViewModel.getImageUrl())
                    .transform(new CircleTransform(imageView.getContext())).into(imageView);
        } else { // no hex, no url
            imageView.setImageResource(R.drawable.circle_grey);
            imageView.clearColorFilter();
        }
        titleTextView.setText(productVariantViewModel.getTitle());
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeCallback != null) {
                    removeCallback.onRemove(productVariantViewModel);
                }
            }
        });
    }
}