package com.tokopedia.loyalty.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_EMPTY = 2;

    private final ActionListener actionListener;
    private List<PromoData> promoDataList;
    private boolean hasNextPage;


    public PromoListAdapter(List<PromoData> promoDataList, ActionListener actionListener) {
        this.actionListener = actionListener;
        this.promoDataList = promoDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_item_promo_list, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_loading_view, parent, false);
            return new ItemLoadingViewHolder(view);
        } else if (viewType == VIEW_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_empty_view, parent, false);
            return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final PromoData promoData = promoDataList.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            ImageHandler.LoadImage(itemViewHolder.ivBanner, promoData.getThumbnailImage());
            itemViewHolder.tvPeriodPromo.setText(promoData.getPeriodFormatted());
            itemViewHolder.layoutMultipleCodePromo.setVisibility(
                    promoData.isMultiplePromo() ? View.VISIBLE : View.GONE
            );
            itemViewHolder.layoutSingleCodePromo.setVisibility(
                    !promoData.isMultiplePromo() && !TextUtils.isEmpty(promoData.getPromoCode())
                            ? View.VISIBLE : View.GONE
            );
            itemViewHolder.layoutEmptyCodePromo.setVisibility(
                    !promoData.isMultiplePromo() && TextUtils.isEmpty(promoData.getPromoCode())
                            ? View.VISIBLE : View.GONE
            );
            itemViewHolder.tvCodePromo.setText(promoData.getPromoCode());
            itemViewHolder.tvMultipleCodePromo.setText(
                    MessageFormat.format("{0} Kode Promo", promoData.getMultiplePromoCodeCount())
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
                    actionListener.onItemPromoCodeCopyClipboardClicked(promoData.getPromoCode(),
                            promoData.getTitle());
                }
            });
            itemViewHolder.ivBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onItemPromoClicked(promoData, position);
                }
            });
        }
    }

    public void addAllItems(List<PromoData> promoDataList) {
        this.promoDataList.clear();
        this.promoDataList.addAll(promoDataList);
        notifyDataSetChanged();
    }

    public void clearDataList() {
        this.promoDataList.clear();
        notifyDataSetChanged();
    }

    public void addAllItemsLoadMore(List<PromoData> promoDataList) {
        this.promoDataList.addAll(promoDataList);
        notifyDataSetChanged();
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    @Override
    public int getItemCount() {
        return promoDataList == null || promoDataList.size() == 0 ? 0 : promoDataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            if (!hasNextPage)
                return VIEW_EMPTY;
            else
                return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.iv_banner)
        PromoImageView ivBanner;
        @BindView(R2.id.tv_period_promo)
        TextView tvPeriodPromo;
        @BindView(R2.id.layout_single_code_promo)
        RelativeLayout layoutSingleCodePromo;
        @BindView(R2.id.layout_empty_code_promo)
        RelativeLayout layoutEmptyCodePromo;
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

    static class ItemLoadingViewHolder extends RecyclerView.ViewHolder {

        public ItemLoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ActionListener {
        void onItemPromoCodeCopyClipboardClicked(String promoCode, String promoName);

        void onItemPromoClicked(PromoData promoData, int position);

        void onItemPromoCodeTooltipClicked();
    }
}
