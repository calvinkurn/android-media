package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;
import com.tokopedia.seller.topads.dashboard.view.helper.CircleTransform;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantItemPickerSearchViewHolder extends BaseMultipleCheckViewHolder<ProductVariantValue> {

    private ImageView imageView;
    private TextView titleTextView;
    private CheckBox checkBox;

    public ProductVariantItemPickerSearchViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
    }

    @Override
    public void bindObject(final ProductVariantValue productVariantValue, boolean checked) {
        bindObject(productVariantValue);
        setChecked(checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productVariantValue, checkBox.isChecked());
                }
            }
        });
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    @Override
    public void bindObject(final ProductVariantValue productVariantValue) {
        if (!TextUtils.isEmpty(productVariantValue.getHexCode())) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundColor(Color.parseColor(productVariantValue.getHexCode()));
            imageView.setImageDrawable(null);
        } else if (!TextUtils.isEmpty(productVariantValue.getIcon())) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(imageView.getContext()).load(productVariantValue.getIcon())
                    .transform(new CircleTransform(imageView.getContext())).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
        titleTextView.setText(productVariantValue.getValue());
    }
}