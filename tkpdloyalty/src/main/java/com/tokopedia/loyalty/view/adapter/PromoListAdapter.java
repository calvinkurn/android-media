package com.tokopedia.loyalty.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.view.compoundview.PromoImageView;
import com.tokopedia.loyalty.view.data.PromoData;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 1/8/18.
 */

public class PromoListAdapter extends RecyclerView.Adapter {

    private final ActionListener actionListener;
    private List<PromoData> promoDataList;


    public PromoListAdapter(List<PromoData> promoDataList, ActionListener actionListener) {
        this.actionListener = actionListener;
        this.promoDataList = promoDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_promo_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PromoData promoData = promoDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ImageHandler.LoadImage(itemViewHolder.ivBanner, promoData.getThumbnailImage());
        itemViewHolder.tvPeriodPromo.setText(promoData.getPeriodFormatted());
        itemViewHolder.layoutMultipleCodePromo.setVisibility(
                promoData.isMultiplePromo() ? View.VISIBLE : View.GONE
        );
        itemViewHolder.layoutSingleCodePromo.setVisibility(
                promoData.isMultiplePromo() ? View.GONE : View.VISIBLE
        );
        itemViewHolder.tvCodePromo.setText(promoData.getPromoCode());
        itemViewHolder.tvMultipleCodePromo.setText(
                MessageFormat.format("{0}Kode Promo", promoData.getMultiplePromoCodeCount())
        );
        itemViewHolder.tvLabelCodePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onItemPromoCodeTooltipClicked();
            }
        });
        itemViewHolder.btnCopyCodePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onItemPromoCodeCopyClipboardClicked(promoData.getPromoCode());
            }
        });
        itemViewHolder.ivBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onItemPromoClicked(promoData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promoDataList.size();
    }

    public void addAllItems(List<PromoData> promoDataList) {
        this.promoDataList.clear();
        this.promoDataList.addAll(promoDataList);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.iv_banner)
        PromoImageView ivBanner;
        @BindView(R2.id.tv_period_promo)
        TextView tvPeriodPromo;
        @BindView(R2.id.layout_single_code_promo)
        RelativeLayout layoutSingleCodePromo;
        @BindView(R2.id.tv_code_promo)
        TextView tvCodePromo;
        @BindView(R2.id.btn_copy_code_promo)
        TextView btnCopyCodePromo;
        @BindView(R2.id.tv_label_code_promo)
        TextView tvLabelCodePromo;
        @BindView(R2.id.layout_multiple_code_promo)
        RelativeLayout layoutMultipleCodePromo;
        @BindView(R2.id.tv_multiple_code_promo)
        TextView tvMultipleCodePromo;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onItemPromoCodeCopyClipboardClicked(String promoCode);

        void onItemPromoClicked(PromoData promoData);

        void onItemPromoCodeTooltipClicked();
    }
}
