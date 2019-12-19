package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.base.list.seller.R;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.base.list.seller.view.old.DataBinder;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;

@Deprecated
public class ProductVariantPickerEmptyDataBinder extends NoResultDataBinder {

    public ProductVariantPickerEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_list, parent, false);
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new VariantPickerEmptyDataBinderViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        super.bindViewHolder(holder, position);
        VariantPickerEmptyDataBinderViewHolder viewHolder = (VariantPickerEmptyDataBinderViewHolder) holder;
        viewHolder.buttonAddPromo.setVisibility(View.GONE);
        viewHolder.textViewEmptyTitle.setText(viewHolder.textViewEmptyTitle.getResources()
                .getString(com.tokopedia.seller.R.string.label_no_color_variant)
        );
    }

    public static class VariantPickerEmptyDataBinderViewHolder extends ViewHolder {
        private Button buttonAddPromo;
        private TextView textViewEmptyTitle;

        public VariantPickerEmptyDataBinderViewHolder(View itemView) {
            super(itemView);
            buttonAddPromo = itemView.findViewById(R.id.button_add_promo);
            textViewEmptyTitle = itemView.findViewById(R.id.text_view_empty_title_text);
        }
    }
}