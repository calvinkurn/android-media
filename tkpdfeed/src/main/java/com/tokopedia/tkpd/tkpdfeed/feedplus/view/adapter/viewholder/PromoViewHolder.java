package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.PromoAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoViewModel;

import butterknife.BindView;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromoViewHolder extends AbstractViewHolder<PromoViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.promo_layout;

    @BindView(R2.id.product_list)
    RecyclerView recyclerView;

    private PromoAdapter adapter;

    private PromoViewModel promoViewModel;

    public PromoViewHolder(View itemView) {
        super(itemView);


        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()
                , LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter = new PromoAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(PromoViewModel promoViewModel) {
        this.promoViewModel = promoViewModel;
        adapter.setList(promoViewModel.getListProduct());
    }
}
