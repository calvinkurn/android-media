package com.tokopedia.transaction.checkout.view.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.adapter.CartAdapterActionListener;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartPromoSuggestionViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO_SUGGESTION = R.layout.holder_item_cart_potential_promo;
    private final CartAdapterActionListener actionListener;

    private RelativeLayout mRlPromoSuggestionLayout;
    private ImageView btnClose;
    private TextView tvDesc;
    private TextView tvAction;
    private RecyclerView.LayoutParams layoutParamsVisible;
    private RecyclerView.LayoutParams layoutParamsGone;
    private CartPromoSuggestion cartPromoSuggestion;

    public CartPromoSuggestionViewHolder(View itemView, CartAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;

        mRlPromoSuggestionLayout = itemView.findViewById(R.id.rl_promo_suggestion_layout);
        this.btnClose = itemView.findViewById(R.id.btn_close);
        this.tvAction = itemView.findViewById(R.id.tv_action);
        this.tvDesc = itemView.findViewById(R.id.tv_desc);

        setupLayoutParams();
    }

    private void setupLayoutParams() {
        layoutParamsVisible = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsGone = new RecyclerView.LayoutParams(0, 0);
    }

    public void bindData(final CartPromoSuggestion data, final int position) {
        cartPromoSuggestion = data;
        if (data.isVisible()) {
            mRlPromoSuggestionLayout.setVisibility(View.VISIBLE);
            mRlPromoSuggestionLayout.setLayoutParams(layoutParamsVisible);

            tvDesc.setText(fromHtml(data.getText()));
            tvAction.setText(data.getCta());
            tvAction.setTextColor(Color.parseColor(data.getCtaColor()));

            tvAction.setOnClickListener(actionClickListener(data, position));
            btnClose.setOnClickListener(closeClickListener(data, position));
        } else {
            mRlPromoSuggestionLayout.setVisibility(View.GONE);
            mRlPromoSuggestionLayout.setLayoutParams(layoutParamsGone);
        }
    }

    public CartPromoSuggestion getCartPromoSuggestion() {
        return cartPromoSuggestion;
    }

    private View.OnClickListener actionClickListener(final CartPromoSuggestion cartPromoSuggestion,
                                                     final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartPromoSuggestionActionClicked(cartPromoSuggestion, position);
            }
        };
    }

    private View.OnClickListener closeClickListener(final CartPromoSuggestion cartPromoSuggestion,
                                                    final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartPromoSuggestionButtonCloseClicked(cartPromoSuggestion, position);
            }
        };
    }

    private Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(source);
        }
    }
}
