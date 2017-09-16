package com.tokopedia.gm.subscribe.view.widget.checkout;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.gm.R;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutCurrentSelectedViewModel;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public class CurrentSelectedProductViewHolder {
    private final CurrentSelectedProductViewHolderCallback listener;
    private final TextView titleCurrentProduct;
    private final TextView descriptionCurrentProduct;
    private final TextView priceCurrentProduct;


    public CurrentSelectedProductViewHolder(CurrentSelectedProductViewHolderCallback listener, View view) {
        this.listener = listener;
        titleCurrentProduct = (TextView) view.findViewById(R.id.textview_gm_checkout_selected_title);
        descriptionCurrentProduct = (TextView) view.findViewById(R.id.textivew_gm_checkout_description_package);
        priceCurrentProduct = (TextView) view.findViewById(R.id.textview_gm_checkout_price_package);
        view.findViewById(R.id.gm_subscribe_checkout_change_selected).setOnClickListener(buttonChangeSelectedListener());
    }

    private View.OnClickListener buttonChangeSelectedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeCurrentSelected();
            }
        };
    }

    public void renderView(GmCheckoutCurrentSelectedViewModel viewModel) {
        titleCurrentProduct.setText(viewModel.getTitle());
        descriptionCurrentProduct.setText(viewModel.getDescription());
        priceCurrentProduct.setText(viewModel.getPrice());
    }
}
