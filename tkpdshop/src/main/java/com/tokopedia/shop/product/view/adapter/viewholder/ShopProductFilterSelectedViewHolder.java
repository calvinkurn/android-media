package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public class ShopProductFilterSelectedViewHolder extends AbstractViewHolder<ShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_filter_picker_selected;
    private TextView etalasePickerItemName;

    public ShopProductFilterSelectedViewHolder(View itemView) {
        super(itemView);

        etalasePickerItemName = itemView.findViewById(R.id.etalase_picker_item_name);
    }

    @Override
    public void bind(ShopProductViewModel shopProductViewModel) {
        if(shopProductViewModel != null && shopProductViewModel instanceof ShopProductFilterModel){
            ShopProductFilterModel model = (ShopProductFilterModel) shopProductViewModel;

            etalasePickerItemName.setText(model.getName());
        }
    }
}
