package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.SpannedGridLayoutManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedShopViewModel;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromotedShopViewHolder extends AbstractViewHolder<PromotedShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.promoted_shop_layout;
    private final FeedPlus.View viewListener;
    private final View favoriteButton;

    private TextView shopName;
    private TextView shopDesc;
    private View goldMerchant;
    private View infoView;
    private View favorite;
    private View favorited;

    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private PromotedShopAdapter adapter;


    private PromotedShopViewModel promotedShopViewModel;

    public PromotedShopViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopDesc = (TextView) itemView.findViewById(R.id.shop_desc);
        goldMerchant = itemView.findViewById(R.id.gold_merchant);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        infoView = itemView.findViewById(R.id.info_topads);
        favoriteButton = itemView.findViewById(R.id.fav_btn);
        favorite = itemView.findViewById(R.id.favorite);
        favorited = itemView.findViewById(R.id.favorited);

        SpannedGridLayoutManager manager = new SpannedGridLayoutManager(
                new SpannedGridLayoutManager.GridSpanLookup() {
                    @Override
                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                        // Conditions for 2x2 items
                        if (position % 6 == 0 || position % 6 == 4) {
                            return new SpannedGridLayoutManager.SpanInfo(2, 2);
                        } else {
                            return new SpannedGridLayoutManager.SpanInfo(1, 1);
                        }
                    }
                },
                3, // number of columns
                1f // how big is default item
        );

        recyclerView.setLayoutManager(manager);
        adapter = new PromotedShopAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(PromotedShopViewModel promotedShopViewModel) {
        this.promotedShopViewModel = promotedShopViewModel;
        shopName.setText(promotedShopViewModel.getShopName());
        shopDesc.setText(promotedShopViewModel.getDescription());
        goldMerchant.setVisibility(promotedShopViewModel.isGoldMerchant() ? View.VISIBLE : View.GONE);
        adapter.setList(promotedShopViewModel.getListProduct());
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onInfoClicked();
            }
        });
        setFavoriteButton(promotedShopViewModel.isFavorited());
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onFavoritedClicked(getAdapterPosition()
                );
            }
        });
    }

    public void setFavoriteButton(boolean favoriteButton) {
        if(favoriteButton){
            favorite.setVisibility(View.GONE);
            favorited.setVisibility(View.VISIBLE);
        }else{
            favorite.setVisibility(View.VISIBLE);
            favorited.setVisibility(View.GONE);
        }
    }
}
