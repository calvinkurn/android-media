package com.tokopedia.transaction.checkout.view.viewholder;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartPromoSuggestionViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout mRlPromoSuggestionLayout;
    private ImageView mBtnClose;
    private TextView mTvDescription;
    private TextView mTvAction;

    private SingleAddressShipmentAdapter.ActionListener mActionListener;

    public CartPromoSuggestionViewHolder(View itemView,
                                         SingleAddressShipmentAdapter.ActionListener actionListener) {
        super(itemView);

        mRlPromoSuggestionLayout = itemView.findViewById(R.id.rl_promo_suggestion_layout);
        mBtnClose = itemView.findViewById(R.id.iv_close_banner);
        mTvAction = itemView.findViewById(R.id.tv_text_promo_suggestion_action);
        mTvDescription = itemView.findViewById(R.id.tv_text_promo_suggestion_description);

        mActionListener = actionListener;
    }

    public void bindViewHolder(CartPromoSuggestion cartPromoSuggestion, int position) {
        if (cartPromoSuggestion.isVisible()) {
            mRlPromoSuggestionLayout.setVisibility(View.VISIBLE);

            mTvDescription.setText(fromHtml(cartPromoSuggestion.getText()));
            mTvAction.setText(cartPromoSuggestion.getCta());
            mTvAction.setTextColor(Color.parseColor(cartPromoSuggestion.getCtaColor()));

            mTvAction.setOnClickListener(actionClickListener(cartPromoSuggestion, position));
            mBtnClose.setOnClickListener(closeClickListener(cartPromoSuggestion, position));
        } else {
            mRlPromoSuggestionLayout.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener actionClickListener(final CartPromoSuggestion cartPromoSuggestion,
                                             final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onCartPromoSuggestionActionClicked(cartPromoSuggestion, position);
            }
        };
    }

    private View.OnClickListener closeClickListener(final CartPromoSuggestion cartPromoSuggestion,
                                            final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onCartPromoSuggestionButtonCloseClicked(cartPromoSuggestion, position);
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