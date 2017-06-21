package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.PromoAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;

import butterknife.BindView;

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

    private PromoAdapter adapter;
    private FeedPlus.View viewListener;

    private PromoCardViewModel promoViewModel;

    public PromoViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        promoterName = (TextView) itemView.findViewById(R.id.shop_name);
        promoterDesc = (TextView) itemView.findViewById(R.id.shop_desc);
        promoterAva = (ImageView) itemView.findViewById(R.id.user_ava);

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
    }

    @Override
    public void bind(PromoCardViewModel promoViewModel) {
        this.promoViewModel = promoViewModel;
        adapter.setList(promoViewModel.getListPromo());
        if (promoViewModel.getAvatarUrl() != null) {
            ImageHandler.loadImage2(promoterAva, promoViewModel.getAvatarUrl(), R.drawable.label_user);
            promoterName.setText(promoViewModel.getPromoterName());
            promoterDesc.setText(promoViewModel.getPromoterDesc());
        }
    }
}
