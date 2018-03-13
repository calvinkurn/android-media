package com.tokopedia.transaction.checkout.view.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private RelativeLayout rlPromoSuggestionLayout;
    private RecyclerView.LayoutParams layoutParamsVisible;
    private RecyclerView.LayoutParams layoutParamsGone;

    public MultipleShipmentPromoSuggestionViewHolder(View itemView) {
        super(itemView);
        this.btnClose = itemView.findViewById(R.id.btn_close);
        this.tvAction = itemView.findViewById(R.id.tv_action);
        this.tvDesc = itemView.findViewById(R.id.tv_desc);
        this.rlPromoSuggestionLayout = itemView.findViewById(R.id.rl_promo_suggestion_layout);
        setupLayoutParams();
    }

    private void setupLayoutParams() {
        Context context = rlPromoSuggestionLayout.getContext();
        layoutParamsVisible = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginVertical = (int) context.getResources().getDimension(R.dimen.new_margin_med);
        int marginHorizontal = (int) context.getResources().getDimension(R.dimen.new_margin_small);
        layoutParamsVisible.leftMargin = marginHorizontal;
        layoutParamsVisible.rightMargin = marginHorizontal;
        layoutParamsVisible.bottomMargin = marginVertical;
        layoutParamsGone = new RecyclerView.LayoutParams(0, 0);
    }

    public void bindPromoSuggestionView(
            final CartPromoSuggestion data,
            final MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener) {

        if (data.isVisible()) {
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
                    listener.onPromoSuggestionCancelled(data);
                }
            });
            rlPromoSuggestionLayout.setVisibility(View.VISIBLE);
            rlPromoSuggestionLayout.setLayoutParams(layoutParamsVisible);
        } else {
            rlPromoSuggestionLayout.setVisibility(View.GONE);
            rlPromoSuggestionLayout.setLayoutParams(layoutParamsGone);
        }
    }
}
