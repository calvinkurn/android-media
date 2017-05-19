package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.SpannedGridLayoutManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.PromotedShopAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;

import butterknife.BindView;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromotedShopViewHolder extends AbstractViewHolder<PromotedShopViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.promoted_shop_layout;

    @BindView(R2.id.shop_name)
    TextView shopName;

    @BindView(R2.id.gold_merchant)
    View goldMerchant;

    @BindView(R2.id.product_list)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private PromotedShopAdapter adapter;


    private PromotedShopViewModel promotedShopViewModel;

    public PromotedShopViewHolder(View itemView) {
        super(itemView);

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
        goldMerchant.setVisibility(promotedShopViewModel.isGoldMerchant() ? View.VISIBLE : View.GONE);
        adapter.setList(promotedShopViewModel.getListProduct());
    }
}
