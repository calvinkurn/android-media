package com.tokopedia.loyalty.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter.OnAdapterActionListener;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailSimpleCodeViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SIMPLE_CODE = R.layout.item_view_promo_simple_code_layout;

    private OnAdapterActionListener adapterActionListener;

    private TextView tvPromoCodeLabel;
    private ImageView ivTooltipInfo;
    private RelativeLayout rlPromoCodeLayout;
    private TextView tvSingleCode;
    private RelativeLayout rlSingleCodeCopyLayout;
    private TextView tvPromoCodeCopy;

    private boolean isCopied;

    public PromoDetailSimpleCodeViewHolder(View itemView, OnAdapterActionListener adapterActionListener) {
        super(itemView);

        this.adapterActionListener = adapterActionListener;

        this.tvPromoCodeLabel = itemView.findViewById(R.id.tv_promo_code_label);
        this.ivTooltipInfo = itemView.findViewById(R.id.iv_tooltip_info);
        this.rlPromoCodeLayout = itemView.findViewById(R.id.rl_promo_code_layout);
        this.tvSingleCode = itemView.findViewById(R.id.tv_single_code);
        this.rlSingleCodeCopyLayout = itemView.findViewById(R.id.rl_single_code_copy_layout);
        this.tvPromoCodeCopy = itemView.findViewById(R.id.tv_promo_code_copy);

        this.isCopied = false;
    }

    public void bind(final String singleCode) {
        boolean withoutPromoCode = TextUtils.isEmpty(singleCode);

        this.tvPromoCodeLabel.setText(withoutPromoCode ? "Tanpa Kode Promo" : "Kode Promo");
        this.ivTooltipInfo.setVisibility(withoutPromoCode ? View.GONE : View.VISIBLE);
        this.ivTooltipInfo.setOnClickListener(tooltipInfoListener());
        this.rlPromoCodeLayout.setVisibility(withoutPromoCode ? View.GONE : View.VISIBLE);
        this.tvSingleCode.setText(singleCode);
        this.rlSingleCodeCopyLayout.setOnClickListener(copyToClipboardListener(singleCode));
        this.tvPromoCodeCopy.setText(isCopied ? "Tersalin" : "Salin Kode");
    }

    private View.OnClickListener tooltipInfoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoCodeTooltipClicked();
            }
        };
    }

    private View.OnClickListener copyToClipboardListener(final String singleCode) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoCodeCopyClipboardClicked(singleCode);
                isCopied = true;
            }
        };
    }
}
