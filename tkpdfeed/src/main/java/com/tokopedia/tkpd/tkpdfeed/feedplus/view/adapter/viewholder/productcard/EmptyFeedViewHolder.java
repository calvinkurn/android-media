package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

/**
 * @author by nisie on 5/15/17.
 */

public class EmptyFeedViewHolder extends AbstractViewHolder<EmptyModel> {

    Button searchShopButton;
    View officialStore;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_empty;

    public EmptyFeedViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        searchShopButton = (Button) itemView.findViewById(R.id.search_shop_button);
        officialStore = itemView.findViewById(R.id.official_store_layout);

        searchShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSearchShopButtonClicked();
            }
        });

        officialStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onEmptyOfficialStoreClicked();
            }
        });

    }

    @Override
    public void bind(EmptyModel emptyModel) {

    }


}
