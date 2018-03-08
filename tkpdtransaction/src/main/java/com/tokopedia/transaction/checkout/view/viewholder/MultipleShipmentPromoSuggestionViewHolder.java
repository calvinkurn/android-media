package com.tokopedia.transaction.checkout.view.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;

/**
 * Created by kris on 3/7/18. Tokopedia
 */

public class MultipleShipmentPromoSuggestionViewHolder extends RecyclerView.ViewHolder {

    private ImageView btnClose;
    private TextView tvDesc;
    private TextView tvAction;

    public MultipleShipmentPromoSuggestionViewHolder(View itemView) {
        super(itemView);
        this.btnClose = itemView.findViewById(R.id.btn_close);
        this.tvAction = itemView.findViewById(R.id.tv_action);
        this.tvDesc = itemView.findViewById(R.id.tv_desc);
    }

    public void bindPromoSuggestionView(
            final CartPromoSuggestion data,
            final MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener) {

        tvDesc.setText(Html.fromHtml(data.getText()));
        tvAction.setText(data.getCta());
        tvAction.setTextColor(Color.parseColor(data.getCtaColor()));

        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPromoSuggestionClicked(data);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPromoSuggestionCancelled();
            }
        });

    }
}
