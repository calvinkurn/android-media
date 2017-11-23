package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public class SingleFeedDetailViewHolder extends FeedDetailViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_detail_single;

    private TextView buyButton;

    public SingleFeedDetailViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView, viewListener);
        buyButton = (TextView) itemView.findViewById(R.id.buy_button);
    }

    @Override
    public void bind(final FeedDetailViewModel feedDetailViewModel) {
        super.bind(feedDetailViewModel);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToBuyProduct(
                        String.valueOf(feedDetailViewModel.getProductId())
                        ,feedDetailViewModel.getPrice(),
                        feedDetailViewModel.getImageSource());
            }
        });
    }
}
