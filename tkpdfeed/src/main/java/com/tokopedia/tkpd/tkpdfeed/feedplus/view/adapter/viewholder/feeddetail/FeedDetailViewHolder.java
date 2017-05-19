package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailViewModel;

import butterknife.BindView;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedDetailViewHolder extends AbstractViewHolder<FeedDetailViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_detail;

    @BindView(R2.id.product_name)
    public TextView productName;

    @BindView(R2.id.product_price)
    public TextView productPrice;

    @BindView(R2.id.product_image)
    public ImageView productImage;

    @BindView(R2.id.wishlist)
    public ImageView wishlist;

    @BindView(R2.id.product_rating)
    public RatingBar productRating;

    @BindView(R2.id.cashback)
    public TextView cashback;

    @BindView(R2.id.wholesale)
    public TextView wholesale;

    @BindView(R2.id.preorder)
    public TextView preorder;

    @BindView(R2.id.free_return)
    public ImageView freeReturn;

    private final FeedPlusDetail.View viewListener;

    public FeedDetailViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(FeedDetailViewModel feedDetailViewModel) {

        ImageHandler.LoadImage(productImage, feedDetailViewModel.getImageSource());

        if (feedDetailViewModel.isWishlist()) {
            ImageHandler.loadImageWithId(wishlist, R.drawable.ic_faved);
        } else {
            ImageHandler.loadImageWithId(wishlist, R.drawable.ic_fav);

        }
        productName.setText(MethodChecker.fromHtml(feedDetailViewModel.getName()));
        productPrice.setText(feedDetailViewModel.getPrice());
        productRating.setRating(feedDetailViewModel.getRating());

        if (feedDetailViewModel.getCashback().equals(""))
            cashback.setVisibility(View.GONE);
        else
            cashback.setVisibility(View.VISIBLE);

        if (feedDetailViewModel.isWholesale())
            wholesale.setVisibility(View.VISIBLE);
        else
            wholesale.setVisibility(View.GONE);

        if (feedDetailViewModel.isFreeReturn())
            freeReturn.setVisibility(View.VISIBLE);
        else
            freeReturn.setVisibility(View.GONE);

        if (feedDetailViewModel.isPreorder())
            preorder.setVisibility(View.VISIBLE);
        else
            preorder.setVisibility(View.GONE);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onWishlistClicked();
            }
        });
    }
}
