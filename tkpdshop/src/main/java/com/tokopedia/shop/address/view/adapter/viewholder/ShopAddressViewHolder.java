package com.tokopedia.shop.address.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopAddressViewHolder extends AbstractViewHolder<ShopAddressViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_address;


    private TextView titleTextView;
    private TextView contentTextView;
    private TextView areaTextView;
    private LabelView emailLabelView;
    private LabelView phoneLabelView;
    private LabelView faxLabelView;

    public ShopAddressViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        titleTextView = view.findViewById(R.id.text_view_title);
        contentTextView = view.findViewById(R.id.text_view_content);
        areaTextView = view.findViewById(R.id.text_view_area);
        emailLabelView = view.findViewById(R.id.label_view_email);
        phoneLabelView = view.findViewById(R.id.label_view_phone);
        faxLabelView = view.findViewById(R.id.label_view_fax);
    }

    @Override
    public void bind(ShopAddressViewModel element) {
        titleTextView.setText(element.getName());
        contentTextView.setText(element.getContent());
        areaTextView.setText(element.getArea());

        emailLabelView.setTitle(element.getEmail());
        phoneLabelView.setTitle(element.getPhone());
        faxLabelView.setTitle(element.getFax());
    }
}