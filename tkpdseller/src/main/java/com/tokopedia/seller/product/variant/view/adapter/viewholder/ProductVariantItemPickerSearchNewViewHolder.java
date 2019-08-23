package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.base.list.seller.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.utils.CircleTransform;

/**
 * @author normansyahputa on 5/26/17.
 */
public class ProductVariantItemPickerSearchNewViewHolder extends BaseMultipleCheckViewHolder<ProductVariantOption> {

    private ImageView imageView;
    private TextView titleTextView;
    private CheckBox checkBox;
    private View viewStroke;

    private boolean isColorType;

    public ProductVariantItemPickerSearchNewViewHolder(View itemView, boolean isColorType) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
        viewStroke = itemView.findViewById(R.id.view_stroke);

        this.isColorType = isColorType;
    }

    @Override
    public void bindObject(final ProductVariantOption productVariantOption, boolean checked) {
        bindObject(productVariantOption);
        setChecked(checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productVariantOption, checkBox.isChecked());
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
    public void bindObject(final ProductVariantOption productVariantOption) {
        if (isColorType) {
            if (!TextUtils.isEmpty(productVariantOption.getHexCode())) {
                imageView.setColorFilter(Color.parseColor(productVariantOption.getHexCode()), PorterDuff.Mode.SRC_ATOP);
                imageView.setImageResource(R.drawable.circle_white_nopad);
                imageView.setVisibility(View.VISIBLE);
                viewStroke.setVisibility(View.VISIBLE);
            } else {
                imageView.setImageResource(R.drawable.circle_white_strike);
                imageView.clearColorFilter();
                imageView.setVisibility(View.VISIBLE);
                viewStroke.setVisibility(View.VISIBLE);
            }
        } else {
            if (!TextUtils.isEmpty(productVariantOption.getIcon())) {
                imageView.clearColorFilter();
//                Glide.with(imageView.getContext()).load(productVariantOption.getIcon())
//                        .transform(new CircleTransform(imageView.getContext())).into(imageView);
                viewStroke.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                viewStroke.setVisibility(View.GONE);
            }
        }
        titleTextView.setText(productVariantOption.getValue());
    }
}