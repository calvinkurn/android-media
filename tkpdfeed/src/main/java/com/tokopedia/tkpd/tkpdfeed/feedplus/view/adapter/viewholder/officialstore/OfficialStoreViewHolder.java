package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.OfficialStoreAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class OfficialStoreViewHolder extends AbstractViewHolder<OfficialStoreViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.official_store_layout;
    private static final int SPAN_1_PRODUCT = 6;
    public static final int SPAN_3_PRODUCT = 2;
    private static final int SPAN_2_PRODUCT = 3;
    private static final int MAX_OFFICIAL_PRODUCT = 6;
    private final FeedPlus.View viewListener;

    RecyclerView recyclerView;
    ImageView imageView;

    private OfficialStoreAdapter adapter;
    private OfficialStoreViewModel officialStoreViewModel;


    public OfficialStoreViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        imageView = (ImageView) itemView.findViewById(R.id.official_store_image);

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
                    return SPAN_1_PRODUCT;
                } else if (adapter.getList().size() % 3 == 0
                        || adapter.getList().size() > MAX_OFFICIAL_PRODUCT) {
                    return SPAN_3_PRODUCT;
                } else if (adapter.getList().size() % 2 == 0) {
                    return SPAN_2_PRODUCT;
                } else {
                    return 0;
                }
            }
        });
        adapter = new OfficialStoreAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(OfficialStoreViewModel officialStoreViewModel) {
        this.officialStoreViewModel = officialStoreViewModel;
        adapter.setList(officialStoreViewModel.getListProduct());
        ImageHandler.LoadImage(imageView, officialStoreViewModel.getOfficialStoreHeaderImageUrl());
    }

}
