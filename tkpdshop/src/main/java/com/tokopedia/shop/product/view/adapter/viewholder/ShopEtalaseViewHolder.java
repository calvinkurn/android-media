package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseViewHolder extends AbstractViewHolder<ShopEtalaseViewModel> {

    @LayoutRes public static final int LAYOUT = R.layout.item_product_etalase_picker_checked_green;
    private final TextView etalasePickerItemName;
    private final ImageView etalasePickerRadioButton;
    private final ImageView etalaseBadgeImageView;

    public ShopEtalaseViewHolder(View itemView) {
        super(itemView);

        etalasePickerItemName = itemView.findViewById(R.id.etalase_picker_item_name);
        etalasePickerRadioButton = itemView.findViewById(R.id.etalase_picker_radio_button);

        etalaseBadgeImageView = itemView.findViewById(R.id.etalase_badge_image_view);
    }

    @Override
    public void bind(ShopEtalaseViewModel shopEtalaseViewModel) {

        if(!TextUtils.isEmpty(shopEtalaseViewModel.getEtalaseBadge())){
            ImageHandler.LoadImage(etalaseBadgeImageView, shopEtalaseViewModel.getEtalaseBadge());
        }else{
            etalaseBadgeImageView.setVisibility(View.GONE);
        }

        etalasePickerItemName.setText(shopEtalaseViewModel.getEtalaseName());

        if(shopEtalaseViewModel.isSelected()){
            etalasePickerRadioButton.setVisibility(View.VISIBLE);
        }else{
            etalasePickerRadioButton.setVisibility(View.GONE);
        }

    }
}
