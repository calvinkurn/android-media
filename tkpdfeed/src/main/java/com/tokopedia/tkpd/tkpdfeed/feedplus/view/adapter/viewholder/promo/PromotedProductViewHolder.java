package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedProductViewModel;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class PromotedProductViewHolder extends AbstractViewHolder<PromotedProductViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.promoted_product_layout;
    private final FeedPlus.View viewListener;

    private View infoView;

    RecyclerView recyclerView;

    private PromotedProductAdapter adapter;

    private PromotedProductViewModel promotedProductViewModel;

    public PromotedProductViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        infoView = itemView.findViewById(R.id.info_topads);
        this.viewListener = viewListener;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getList().size() == 1) {
                    return 6;
                } else if (adapter.getList().size() % 3 == 0 || adapter.getList().size() > 6) {
                    return 2;
                } else if (adapter.getList().size() % 2 == 0) {
                    return 3;
                } else {
                    return 0;
                }
            }
        });
        adapter = new PromotedProductAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(PromotedProductViewModel promotedProductViewModel) {
        this.promotedProductViewModel = promotedProductViewModel;
        this.promotedProductViewModel.setRowNumber(getAdapterPosition());
        adapter.setData(this.promotedProductViewModel);
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onInfoClicked();
            }
        });
    }

}
