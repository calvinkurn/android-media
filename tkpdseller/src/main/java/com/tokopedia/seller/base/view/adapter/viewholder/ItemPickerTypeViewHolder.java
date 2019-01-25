package com.tokopedia.seller.base.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.product.manage.item.common.util.ItemPickerType;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.utils.CircleTransform;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ItemPickerTypeViewHolder extends BaseViewHolder<ItemPickerType> {

    private ImageView imageView;
    private TextView titleTextView;
    private ImageButton closeImageButton;

    public ItemPickerTypeViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
        titleTextView = (TextView) itemView.findViewById(R.id.text_view_title);
        closeImageButton = (ImageButton) itemView.findViewById(R.id.image_button_close);
    }

    @Override
    public void bindObject(final ItemPickerType itemPickerType) {
        if (!TextUtils.isEmpty(itemPickerType.getIcon())) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext()).load(itemPickerType.getIcon())
                    .transform(new CircleTransform(imageView.getContext())).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
        titleTextView.setText(itemPickerType.getTitle());
    }
}