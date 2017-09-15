package com.tokopedia.seller.product.picker.view.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerSearchViewHolder extends BaseMultipleCheckViewHolder<ProductListPickerViewModel> {

    private ImageView imageProduct;
    private TextView nameProduct;
    private CheckBox checkBoxProduct;
    private TextView priceProduct;

    public ProductListPickerSearchViewHolder(View itemView) {
        super(itemView);
        imageProduct = (ImageView) itemView.findViewById(R.id.image_product_picker_search);
        nameProduct = (TextView) itemView.findViewById(R.id.text_product_name);
        priceProduct = (TextView) itemView.findViewById(R.id.text_product_price);
        checkBoxProduct = (CheckBox) itemView.findViewById(R.id.check_box_picker_product);
    }

    @Override
    public void bindObject(ProductListPickerViewModel productListPickerViewModel) {
        ImageHandler.loadImageRounded2(
                imageProduct.getContext(),
                imageProduct,
                productListPickerViewModel.getIcon()
        );
        nameProduct.setText(productListPickerViewModel.getTitle());
        priceProduct.setText(productListPickerViewModel.getProductPrice());
    }

    @Override
    public void bindObject(final ProductListPickerViewModel productListPickerViewModel, boolean checked) {
        bindObject(productListPickerViewModel);
        setChecked(checked);
        checkBoxProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productListPickerViewModel, checkBoxProduct.isChecked());
                }
            }
        });
    }

    @Override
    public boolean isChecked() {
        return checkBoxProduct.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBoxProduct.setChecked(checked);
    }
}
