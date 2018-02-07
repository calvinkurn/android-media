package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.BorderItemDecoration;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class OfficialStoreCampaignViewHolder extends AbstractViewHolder<OfficialStoreCampaignViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_official_store_campaign;

    private static final int SPAN_1_PRODUCT = 6;
    public static final int SPAN_3_PRODUCT = 2;
    private static final int SPAN_2_PRODUCT = 3;
    private static final int MAX_OFFICIAL_PRODUCT = 6;
    private final FeedPlus.View viewListener;

    RecyclerView recyclerView;
    ImageView imageView;
    TextView title;
    View mainView;
    View seeAll;
    TextView seeAllText;

    private OfficialStoreCampaignAdapter adapter;

    public OfficialStoreCampaignViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        imageView = (ImageView) itemView.findViewById(R.id.official_store_image);
        title = (TextView) itemView.findViewById(R.id.official_store_title);
        mainView = itemView.findViewById(R.id.main_view);
        seeAll = itemView.findViewById(R.id.footer);
        seeAllText = (TextView) itemView.findViewById(R.id.see_all_text);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setAutoMeasureEnabled(true);
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

        recyclerView.addItemDecoration(new BorderItemDecoration(
                recyclerView.getContext(), BorderItemDecoration.HORIZONTAL_LIST));

        adapter = new OfficialStoreCampaignAdapter(viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void bind(final OfficialStoreCampaignViewModel officialStoreViewModel) {
        officialStoreViewModel.setRowNumber(getAdapterPosition());
        adapter.setData(officialStoreViewModel);
        ImageHandler.LoadImage(imageView, officialStoreViewModel.getOfficialStoreHeaderImageUrl());
        title.setText(MethodChecker.fromHtml(officialStoreViewModel.getTitle()));
        mainView.setBackgroundColor(Color.parseColor(officialStoreViewModel.getHexColor()));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToCampaign(
                        officialStoreViewModel.getPage(),
                        officialStoreViewModel.getRowNumber(),
                        officialStoreViewModel.getRedirectUrl(),
                        officialStoreViewModel.getTitle());
            }
        });
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSeeAllOfficialStoresFromCampaign(
                        officialStoreViewModel.getPage(),
                        officialStoreViewModel.getRowNumber(),
                        officialStoreViewModel.getRedirectUrl());
            }
        });
        seeAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSeeAllOfficialStoresFromCampaign(
                        officialStoreViewModel.getPage(),
                        officialStoreViewModel.getRowNumber(),
                        officialStoreViewModel.getRedirectUrl());
            }
        });
    }

}
