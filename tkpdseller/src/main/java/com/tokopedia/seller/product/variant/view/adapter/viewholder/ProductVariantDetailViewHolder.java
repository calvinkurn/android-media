package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */
@Deprecated
public class ProductVariantDetailViewHolder extends BaseMultipleCheckViewHolder<ProductVariantDetailViewModel> {

    private TextView titleTextView;
    private CheckBox checkBox;

    public ProductVariantDetailViewHolder(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
    }

    @Override
    public void bindObject(final ProductVariantDetailViewModel productVariantDetailViewModel, boolean checked) {
        bindObject(productVariantDetailViewModel);
        setChecked(checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productVariantDetailViewModel, checkBox.isChecked());
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
    public void bindObject(final ProductVariantDetailViewModel productVariantDetailViewModel) {
        titleTextView.setText(productVariantDetailViewModel.getName());
    }
}