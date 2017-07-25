package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/17/17.
 */

public class PromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_MORE = 234;
    private static final int VIEW_LAYOUT = 344;
    private static final float MARGIN_CARD = 10;
    private static final float WIDTH_CARD = 285;
    private ArrayList<PromoViewModel> list;

    private FeedPlus.View viewListener;

    public PromoAdapter(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_MORE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.promo_more_layout, parent, false);
                return new ViewMoreViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.promo_item_layout, parent, false);
                return new LayoutViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_MORE) {
            final ViewMoreViewHolder temp = (ViewMoreViewHolder) holder;
            temp.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onViewMorePromoClicked();
                }
            });
        } else {
            final LayoutViewHolder temp = (LayoutViewHolder) holder;
            if (getItemCount() == 1) {
                final float scale = temp.container.getResources().getDisplayMetrics().density;
                CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                int marginPixels = (int) (MARGIN_CARD * scale + 0.5f);
                params.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
                temp.container.setLayoutParams(params);
            } else {
                final float scale = temp.container.getResources().getDisplayMetrics().density;
                int widthPixels = (int) (WIDTH_CARD * scale + 0.5f);
                CardView.LayoutParams params = new CardView.LayoutParams(widthPixels, CardView.LayoutParams.WRAP_CONTENT);
                int marginPixels = (int) (MARGIN_CARD * scale + 0.5f);
                params.setMargins(marginPixels, marginPixels, 0, marginPixels);
                temp.container.setLayoutParams(params);
            }
            ImageHandler.LoadImage(temp.imageView, list.get(position).getImageUrl());
            temp.period.setText(list.get(position).getPeriod());
            temp.description.setText(list.get(position).getDescriptionSpanned());
            temp.promoCode.setText(list.get(position).getPromoCode());
            temp.copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onCopyClicked(temp.promoCode.getText().toString(),
                            list.get(position).getName());
                }
            });

            setActionArea(temp, list.get(position).getPromoCode());

            temp.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onSeePromo(list.get(position).getLink(),
                            list.get(position).getName());
                }
            });

            temp.seeArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onSeePromo(list.get(position).getLink(),
                            list.get(position).getName());
                }
            });
        }
    }

    private void setActionArea(LayoutViewHolder holder, String promoCode) {
        if (promoCode == null || promoCode.length() == 0) {
            holder.seeArea.setVisibility(View.VISIBLE);
            holder.copyArea.setVisibility(View.GONE);
        } else {
            holder.copyArea.setVisibility(View.VISIBLE);
            holder.seeArea.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<PromoViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<PromoViewModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() > 1 && position == getItemCount() - 1) {
            return VIEW_MORE;
        } else {
            return VIEW_LAYOUT;
        }
    }

    public class LayoutViewHolder extends RecyclerView.ViewHolder {

        private CardView container;
        private View copyButton;
        private TextView period;
        private TextView description;
        private TextView promoCode;
        private ImageView imageView;
        private View copyArea;
        private View seeArea;

        public LayoutViewHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.container);
            imageView = (ImageView) itemView.findViewById(R.id.product_image);
            description = (TextView) itemView.findViewById(R.id.desc);
            period = (TextView) itemView.findViewById(R.id.date);
            promoCode = (TextView) itemView.findViewById(R.id.promo_link);
            copyButton = itemView.findViewById(R.id.copy_but);
            copyArea = itemView.findViewById(R.id.action_copy);
            seeArea = itemView.findViewById(R.id.see_all);
        }
    }

    public class ViewMoreViewHolder extends RecyclerView.ViewHolder {

        private View container;

        public ViewMoreViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
        }
    }


}
