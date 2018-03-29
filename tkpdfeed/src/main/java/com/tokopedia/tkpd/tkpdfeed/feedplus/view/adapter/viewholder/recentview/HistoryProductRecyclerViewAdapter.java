package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.BadgeViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 4/06/15.
 * modified by m.normansyah on 06/01/2015 - set item id to distinct items
 */
public class HistoryProductRecyclerViewAdapter extends RecyclerView.Adapter<HistoryProductRecyclerViewAdapter.ViewHolder> {

    private final FeedPlus.View viewListener;
    private List<RecentViewProductViewModel> data;

    public void setData(List<RecentViewProductViewModel> productItems) {
        data.clear();
        data.addAll(productItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mainContent;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public ImageView productImage;
        public LinearLayout badgesContainer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mainContent = (LinearLayout) itemLayoutView.findViewById(R.id.main_content);
            productName = (TextView) itemLayoutView.findViewById(R.id.product_name);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.product_price);
            shopName = (TextView) itemLayoutView.findViewById(R.id.product_shop);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            badgesContainer = (LinearLayout) itemLayoutView.findViewById(R.id.badges_container);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }

    public HistoryProductRecyclerViewAdapter(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_recentview_product, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(MethodChecker.fromHtml(data.get(position).getName()));
        holder.productPrice.setText(data.get(position).getPrice());
        holder.shopName.setText(data.get(position).getShop());

        ImageHandler.loadImageFit2(holder.getContext(),
                holder.productImage, data.get(position).getImgUri());

        setBadges(holder, data.get(position));

        holder.mainContent.setOnClickListener(onProductItemClicked(position));
    }

    private View.OnClickListener onProductItemClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToProductDetailFromRecentView(
                        data.get(position).getId(),
                        data.get(position).getImgUri(),
                        data.get(position).getName(),
                        data.get(position).getPrice());
            }
        };
    }

    @Override
    public int getItemCount() {
        if (data.size() > 4) return 4;
        else return data.size();
    }

    public List<RecentViewProductViewModel> getData() {
        return data;
    }

    public void addAll(List<RecentViewProductViewModel> newData) {
        data.clear();
        data.addAll(newData);
    }


    private void setBadges(ViewHolder holder, RecentViewProductViewModel data) {
        holder.badgesContainer.removeAllViews();
        if (data.getBadges() != null) {
            for (BadgeViewModel badge : data.getBadges()) {
                View view = LayoutInflater.from(
                        holder.getContext()).inflate(R.layout.badge_layout_recentview, null);
                ImageView imageBadge = (ImageView) view.findViewById(R.id.badge);
                holder.badgesContainer.addView(view);
                LuckyShopImage.loadImage(imageBadge, badge.getImgUrl());
            }
        }

    }
}