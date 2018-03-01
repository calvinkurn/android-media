package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public class ShopProductFilterViewHolder extends AbstractViewHolder<ShopProductFilterModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_filter_picker;

    private TextView etalasePickerItemName;
    private ImageView checkedImageView;

    public ShopProductFilterViewHolder(View itemView) {
        super(itemView);

        etalasePickerItemName = itemView.findViewById(R.id.etalase_picker_item_name);
        checkedImageView = itemView.findViewById(R.id.etalase_picker_radio_button_selected);
    }

    @Override
    public void bind(ShopProductFilterModel shopProductFilterModel) {
        etalasePickerItemName.setText(shopProductFilterModel.getName());

        if (shopProductFilterModel.isSelected()) {
            checkedImageView.setVisibility(View.VISIBLE);
        } else {
            checkedImageView.setVisibility(View.GONE);
        }
    }
}
