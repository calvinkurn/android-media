package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoCardViewModel;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromoViewHolder extends AbstractViewHolder<PromoCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.promo_layout;

    RecyclerView recyclerView;
    TextView promoterName;
    TextView promoterDesc;
    ImageView promoterAva;
    View promoHeader;

    private PromoAdapter adapter;
    private FeedPlus.View viewListener;

    private PromoCardViewModel promoViewModel;

    public PromoViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        promoterName = (TextView) itemView.findViewById(R.id.shop_name);
        promoterDesc = (TextView) itemView.findViewById(R.id.shop_desc);
        promoterAva = (ImageView) itemView.findViewById(R.id.user_ava);
        promoHeader = itemView.findViewById(R.id.promo_header);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext()
                , LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if(layoutManager.findLastVisibleItemPosition() ==  adapter.getItemCount()-1){
                        snapHelper.attachToRecyclerView(null);
                    }else if(layoutManager.findFirstCompletelyVisibleItemPosition() != adapter.getItemCount()-1){
                        snapHelper.attachToRecyclerView(recyclerView);
                    }
                }
            }
        });
        adapter = new PromoAdapter(viewListener);
        recyclerView.setAdapter(adapter);

        promoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToPromoPageFromHeader(
                        promoViewModel.getPage(),
                        getAdapterPosition()
                );
            }
        });
    }

    @Override
    public void bind(PromoCardViewModel promoViewModel) {
        this.promoViewModel = promoViewModel;
        this.promoViewModel.setRowNumber(getAdapterPosition());
        adapter.setData(this.promoViewModel);
        if (promoViewModel.getAvatarUrl() != null) {
            ImageHandler.loadImage2(promoterAva, promoViewModel.getAvatarUrl(), R.drawable.label_user);
            promoterName.setText(promoViewModel.getPromoterName());
            promoterDesc.setText(promoViewModel.getPromoterDesc());
        }
    }
}
