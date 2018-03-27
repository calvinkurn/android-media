package com.tokopedia.loyalty.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.compoundview.PromoImageView;
import com.tokopedia.loyalty.view.data.PromoDetailInfoHolderData;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailInfoViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_DETAIL_INFO = R.layout.item_view_promo_detail_info_layout;

    private PromoImageView ivPromoDetailThumbnail;
    private TextView tvPromoDetailTitle;
    private TextView tvPromoDetailPeriod;
    private TextView tvPromoDetailMinTransaction;

    public PromoDetailInfoViewHolder(View itemView) {
        super(itemView);

        this.ivPromoDetailThumbnail = itemView.findViewById(R.id.iv_promo_detail_thumbnail);
        this.tvPromoDetailTitle = itemView.findViewById(R.id.tv_promo_detail_title);
        this.tvPromoDetailPeriod = itemView.findViewById(R.id.tv_promo_detail_period);
        this.tvPromoDetailMinTransaction = itemView.findViewById(R.id.tv_promo_detail_min_transaction);
    }

    public void bind(PromoDetailInfoHolderData holderData) {
        ImageHandler.LoadImage(this.ivPromoDetailThumbnail, holderData.getThumbnailImageUrl());
        this.tvPromoDetailTitle.setText(holderData.getTitle());
        this.tvPromoDetailPeriod.setText(holderData.getPromoPeriod());
        this.tvPromoDetailMinTransaction.setText(holderData.getMinTransaction());
    }
}
