package com.tokopedia.loyalty.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailSingleCodeViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SINGLE_CODE = R.layout.item_view_promo_single_code_layout;

    private TextView tvSingleCode;

    public PromoDetailSingleCodeViewHolder(View itemView) {
        super(itemView);

        this.tvSingleCode = itemView.findViewById(R.id.tv_single_code);
    }

    public void bind(SingleCodeViewModel singleCode) {
        this.tvSingleCode.setText(singleCode.getSingleCode());
    }
}
