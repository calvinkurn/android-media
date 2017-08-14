package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;
import com.tokopedia.seller.topads.dashboard.view.helper.CircleTransform;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantItemPickerSearchViewHolder extends BaseMultipleCheckViewHolder<ProductVariantViewModel> {

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
    public void bindObject(final ProductVariantViewModel productVariantViewModel, boolean checked,
                           final CheckedCallback<ProductVariantViewModel> checkedCallback) {
        bindObject(productVariantViewModel);
        setChecked(checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productVariantViewModel, checkBox.isChecked());
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
    public void bindObject(final ProductVariantViewModel productVariantViewModel) {
        if (!TextUtils.isEmpty(productVariantViewModel.getImageUrl())) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext()).load(productVariantViewModel.getImageUrl())
                    .transform(new CircleTransform(imageView.getContext())).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
        titleTextView.setText(productVariantViewModel.getTitle());
    }
}