package com.tokopedia.loyalty.view.viewholder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter;
import com.tokopedia.loyalty.view.compoundview.PromoImageView;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoDetailInfoHolderData;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailInfoViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_DETAIL_INFO = R.layout.item_view_promo_detail_info_layout;

    private PromoDetailAdapter.OnAdapterActionListener adapterActionListener;

    private PromoImageView ivPromoDetailThumbnail;
    private TextView tvPromoDetailTitle;
    private TextView tvPromoDetailPeriod;
    private TextView tvPromoDetailMinTransaction;
    private FloatingActionButton fabPromoDetailShare;

    public PromoDetailInfoViewHolder(View itemView, PromoDetailAdapter.OnAdapterActionListener adapterActionListener) {
        super(itemView);

        this.adapterActionListener = adapterActionListener;

        this.ivPromoDetailThumbnail = itemView.findViewById(R.id.iv_promo_detail_thumbnail);
        this.tvPromoDetailTitle = itemView.findViewById(R.id.tv_promo_detail_title);
        this.tvPromoDetailPeriod = itemView.findViewById(R.id.tv_promo_detail_period);
        this.tvPromoDetailMinTransaction = itemView.findViewById(R.id.tv_promo_detail_min_transaction);
        this.fabPromoDetailShare = itemView.findViewById(R.id.fab_promo_detail_share);
    }

    public void bind(PromoDetailInfoHolderData holderData) {
        ImageHandler.LoadImage(this.ivPromoDetailThumbnail, holderData.getThumbnailImageUrl());
        this.tvPromoDetailTitle.setText(Html.fromHtml(holderData.getTitle()));
        this.tvPromoDetailPeriod.setText(holderData.getPromoPeriod());
        this.tvPromoDetailMinTransaction.setText(holderData.getMinTransaction());
        this.fabPromoDetailShare.setOnClickListener(promoShareListener(holderData.getPromoData()));
    }

    private View.OnClickListener promoShareListener(final PromoData promoData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterActionListener.onItemPromoShareClicked(promoData);
            }
        };
    }
}
