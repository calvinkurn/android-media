package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendationViewModel;

/**
 * @author by nisie on 10/27/17.
 */

public class KolRecommendationViewHolder extends AbstractViewHolder<KolRecommendationViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_recommend_layout;
    private final FeedPlus.View.Kol kolViewListener;

    TextView title;
    RecyclerView listRecommendation;
    KolRecommendationAdapter adapter;
    TextView seeAll;

    public KolRecommendationViewHolder(View itemView, FeedPlus.View.Kol kolViewListener) {
        super(itemView);
        this.kolViewListener = kolViewListener;
        title = (TextView) itemView.findViewById(R.id.title);
        seeAll = (TextView) itemView.findViewById(R.id.see_all_text);
        listRecommendation = (RecyclerView) itemView.findViewById(R.id.list_recommendation);
        listRecommendation.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new KolRecommendationAdapter(kolViewListener);
        listRecommendation.setAdapter(adapter);

    }

    @Override
    public void bind(final KolRecommendationViewModel element) {
        element.setRowNumber(getAdapterPosition());
        title.setText(MethodChecker.fromHtml(element.getTitle()));
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventKolRecommendationViewAllClick();
                kolViewListener.onGoToListKolRecommendation(element.getPage(), element
                        .getRowNumber(), element.getUrl());
            }
        });

        adapter = new KolRecommendationAdapter(kolViewListener);
        listRecommendation.setAdapter(adapter);
        adapter.setData(element);

    }
}
