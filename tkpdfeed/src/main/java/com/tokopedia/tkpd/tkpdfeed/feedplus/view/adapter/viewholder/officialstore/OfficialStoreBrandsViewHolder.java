package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.BorderItemDecoration;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * @author by nisie on 7/26/17.
 */

public class OfficialStoreBrandsViewHolder extends AbstractViewHolder<OfficialStoreBrandsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_official_store_brands;

    RecyclerView recyclerView;
    TextView seeAllButton;
    BrandsAdapter adapter;
    private OfficialStoreBrandsViewModel officialStoreBrandsViewModel;

    public OfficialStoreBrandsViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_brands_list);
        seeAllButton = (TextView) itemView.findViewById(R.id.text_view_all_brands);
        adapter = new BrandsAdapter(viewListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new NonScrollGridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BorderItemDecoration(
                recyclerView.getContext(), BorderItemDecoration.GRID));
        recyclerView.setAdapter(adapter);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSeeAllOfficialStoresFromBrands(
                        officialStoreBrandsViewModel.getPage(),
                        officialStoreBrandsViewModel.getRowNumber()
                );
            }
        });
    }

    @Override
    public void bind(OfficialStoreBrandsViewModel officialStoreBrandsViewModel) {
        this.officialStoreBrandsViewModel = officialStoreBrandsViewModel;
        this.officialStoreBrandsViewModel.setRowNumber(getAdapterPosition());
        adapter.setData(this.officialStoreBrandsViewModel);
    }
}
