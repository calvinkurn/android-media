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

    public TextView productName;
    public TextView productPrice;
    public ImageView productImage;
    public ImageView wishlist;
    public RatingBar productRating;
    public TextView cashback;
    public TextView wholesale;
    public TextView preorder;
    public ImageView freeReturn;

    private final FeedPlusDetail.View viewListener;

    public FeedDetailViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        productName = (TextView) itemView.findViewById(R.id.product_name);
        productPrice = (TextView) itemView.findViewById(R.id.product_price);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        wishlist = (ImageView) itemView.findViewById(R.id.wishlist);
        productRating = (RatingBar) itemView.findViewById(R.id.product_rating);
        cashback = (TextView) itemView.findViewById(R.id.cashback);
        wholesale = (TextView) itemView.findViewById(R.id.wholesale);
        preorder = (TextView) itemView.findViewById(R.id.preorder);
        freeReturn = (ImageView) itemView.findViewById(R.id.free_return);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final FeedDetailViewModel feedDetailViewModel) {

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
                viewListener.onWishlistClicked(
                        getAdapterPosition(),
                        feedDetailViewModel.getProductId(),
                        feedDetailViewModel.isWishlist());
            }
        });
    }
}
