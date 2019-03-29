package com.tokopedia.seller.product.picker.view.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.base.list.seller.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerSearchViewHolder extends BaseMultipleCheckViewHolder<ProductListPickerViewModel> {

    private ImageView imageProduct;
    private TextView nameProduct;
    private CheckBox checkBoxProduct;
    private TextView priceProduct;
    private View stockTagEmpty;

    public ProductListPickerSearchViewHolder(View itemView) {
        super(itemView);
        imageProduct = (ImageView) itemView.findViewById(R.id.image_product_picker_search);
        nameProduct = (TextView) itemView.findViewById(R.id.text_product_name);
        priceProduct = (TextView) itemView.findViewById(R.id.text_product_price);
        checkBoxProduct = (CheckBox) itemView.findViewById(R.id.check_box_picker_product);
        stockTagEmpty = itemView.findViewById(R.id.stock_empty_tag);
    }

    @Override
    public void bindObject(ProductListPickerViewModel productListPickerViewModel) {
        ImageHandler.loadImageRounded2(
                imageProduct.getContext(),
                imageProduct,
                productListPickerViewModel.getIcon()
        );
        nameProduct.setText(MethodChecker.fromHtml(productListPickerViewModel.getTitle()));
        priceProduct.setText(productListPickerViewModel.getProductPrice());
    }

    @Override
    public void bindObject(final ProductListPickerViewModel productListPickerViewModel, boolean checked) {
        bindObject(productListPickerViewModel);
        setChecked(checked);
        setBackground(checked);
        setActive(productListPickerViewModel.isStockOrImageEmpty());
        checkBoxProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productListPickerViewModel, checkBoxProduct.isChecked());
                }
            }
        });
    }

    private void setActive(boolean stockOrImageEmpty) {
        if(stockOrImageEmpty){
            stockTagEmpty.setVisibility(View.VISIBLE);
            checkBoxProduct.setEnabled(false);
        }else{
            stockTagEmpty.setVisibility(View.GONE);
            checkBoxProduct.setEnabled(true);
        }
    }

    @Override
    public boolean isChecked() {
        return checkBoxProduct.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBoxProduct.setChecked(checked);
        setBackground(checked);
    }

    public void setBackground(boolean isChecked) {
        if(isChecked){
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
        }else{
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
        }
    }
}
