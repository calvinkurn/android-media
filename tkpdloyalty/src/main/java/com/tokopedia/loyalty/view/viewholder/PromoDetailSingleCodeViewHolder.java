package com.tokopedia.loyalty.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailSingleCodeViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SINGLE_CODE = R.layout.item_view_promo_single_code_layout;

    private PromoDetailAdapter.OnAdapterActionListener adapterActionListener;

    private TextView tvOrdinalNumber;
    private TextView tvSingleCode;
    private ImageView ivCopyToClipboard;

    public PromoDetailSingleCodeViewHolder(View itemView,
                                           PromoDetailAdapter.OnAdapterActionListener adapterActionListener) {
        super(itemView);

        this.adapterActionListener = adapterActionListener;

        this.tvOrdinalNumber = itemView.findViewById(R.id.tv_ordinal_number);
        this.tvSingleCode = itemView.findViewById(R.id.tv_single_code);
        this.ivCopyToClipboard = itemView.findViewById(R.id.iv_copy_to_clipboard);
    }

    public void bind(SingleCodeViewModel singleCode, int position) {
        this.tvOrdinalNumber.setText(String.valueOf(position));
        this.tvSingleCode.setText(singleCode.getSingleCode());
        this.ivCopyToClipboard.setOnClickListener(copyToClipboardListener(singleCode.getSingleCode()));
    }

    private View.OnClickListener copyToClipboardListener(final String singleCode) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoCodeCopyClipboardClicked(singleCode);
            }
        };
    }
}
