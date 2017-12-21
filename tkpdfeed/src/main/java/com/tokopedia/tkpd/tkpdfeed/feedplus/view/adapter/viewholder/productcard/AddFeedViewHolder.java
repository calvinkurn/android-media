package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.AddFeedModel;

/**
 * Created by stevenfredian on 5/31/17.
 */

public class AddFeedViewHolder extends AbstractViewHolder<AddFeedModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.add_feed_more;
    private final FeedPlus.View viewListener;
    private final View button;

    public AddFeedViewHolder(View view, FeedPlus.View viewListener) {
        super(view);
        this.viewListener = viewListener;
        button = itemView.findViewById(R.id.find_favorite_shop);
    }

    @Override
    public void bind(AddFeedModel element) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onSearchShopButtonClicked();
            }
        });
    }
}
