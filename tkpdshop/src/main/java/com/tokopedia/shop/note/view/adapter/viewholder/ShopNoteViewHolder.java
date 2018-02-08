package com.tokopedia.shop.note.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopNoteViewHolder extends AbstractViewHolder<ShopNoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_info_note;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvOrderDate;

    public ShopNoteViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        tvTitle = view.findViewById(R.id.title_text_view);
        tvOrderDate = view.findViewById(R.id.date_text_view);
    }

    @Override
    public void bind(ShopNoteViewModel element) {
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(element.getLastUpdate());
    }
}