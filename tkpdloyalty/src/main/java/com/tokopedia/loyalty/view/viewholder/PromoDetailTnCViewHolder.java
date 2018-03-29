package com.tokopedia.loyalty.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.PromoDetailTncHolderData;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailTnCViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_TNC = R.layout.item_view_promo_tnc_layout;

    private TextView tvPromoDetailTnC;

    public PromoDetailTnCViewHolder(View itemView) {
        super(itemView);

        this.tvPromoDetailTnC = itemView.findViewById(R.id.tv_promo_detail_tnc);
    }

    public void bind(PromoDetailTncHolderData holderData) {
        this.tvPromoDetailTnC.setText(Html.fromHtml(Html.fromHtml(holderData.getTermAndConditions().get(0)).toString()));
        this.tvPromoDetailTnC.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
