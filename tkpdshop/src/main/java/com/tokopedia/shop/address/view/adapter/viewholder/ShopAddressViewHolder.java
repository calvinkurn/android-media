package com.tokopedia.shop.address.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.common.util.TextHtmlUtils;

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
        titleTextView = view.findViewById(R.id.text_view_address_title);
        contentTextView = view.findViewById(R.id.text_view_address_content);
        areaTextView = view.findViewById(R.id.text_view_address_area);
        emailLabelView = view.findViewById(R.id.label_view_email);
        phoneLabelView = view.findViewById(R.id.label_view_phone);
        faxLabelView = view.findViewById(R.id.label_view_fax);
    }

    @Override
    public void bind(ShopAddressViewModel shopAddressViewModel) {
        titleTextView.setText(shopAddressViewModel.getName());
        contentTextView.setText(TextHtmlUtils.getTextFromHtml(shopAddressViewModel.getContent()));
        areaTextView.setText(TextHtmlUtils.getTextFromHtml(shopAddressViewModel.getArea()));

        if (!TextApiUtils.isTextEmpty(shopAddressViewModel.getEmail())) {
            emailLabelView.setVisibility(View.VISIBLE);
            emailLabelView.setTitle(shopAddressViewModel.getEmail());
        }
        if (!TextApiUtils.isTextEmpty(shopAddressViewModel.getPhone())) {
            phoneLabelView.setVisibility(View.VISIBLE);
            phoneLabelView.setTitle(shopAddressViewModel.getPhone());
        }
        if (!TextApiUtils.isTextEmpty(shopAddressViewModel.getFax())) {
            faxLabelView.setVisibility(View.VISIBLE);
            faxLabelView.setTitle(shopAddressViewModel.getFax());
        }
    }
}