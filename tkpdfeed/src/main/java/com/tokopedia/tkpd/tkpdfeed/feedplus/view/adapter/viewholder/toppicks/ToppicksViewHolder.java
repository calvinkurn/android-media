package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.toppicks;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksViewModel;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * @author by nisie on 8/11/17.
 */

public class ToppicksViewHolder extends AbstractViewHolder<ToppicksViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.toppicks_layout;
    private TextView seeAll;
    private ToppicksAdapter adapter;
    private FeedPlus.View.Toppicks toppicksListener;
    private ToppicksViewModel toppicksViewModel;

    public ToppicksViewHolder(View itemView, final FeedPlus.View.Toppicks toppicksListener) {
        super(itemView);
        this.toppicksListener = toppicksListener;
        RecyclerView recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        seeAll = (TextView) itemView.findViewById(R.id.see_all);
        GridLayoutManager gridLayoutManager = new NonScrollGridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false);
        adapter = new ToppicksAdapter(toppicksListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(final ToppicksViewModel toppicksViewModel) {
        this.toppicksViewModel = toppicksViewModel;
        this.toppicksViewModel.setRowNumber(getAdapterPosition());
        adapter.setData(this.toppicksViewModel);

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toppicksListener.onSeeAllToppicks(toppicksViewModel.getPage(),
                        toppicksViewModel.getRowNumber());
            }
        });
    }
}
