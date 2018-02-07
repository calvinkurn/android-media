package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedDetailViewHolder extends AbstractViewHolder<FeedDetailViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_detail;
    protected static final String CASHBACK = "Cashback";

    public TextView productName;
    public TextView productPrice;
    public ImageView productImage;
    public ImageView wishlist;
    public RatingBar productRating;
    public TextView cashback;
    public TextView wholesale;
    public TextView preorder;
    public ImageView freeReturn;
    public View mainView;

    protected final FeedPlusDetail.View viewListener;

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
        mainView = itemView.findViewById(R.id.main_view);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final FeedDetailViewModel feedDetailViewModel) {

        ImageHandler.LoadImage(productImage, feedDetailViewModel.getImageSource());

        if (feedDetailViewModel.isWishlist()) {
            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist_faved);
        } else {
            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist);

        }
        productName.setText(MethodChecker.fromHtml(feedDetailViewModel.getName()));
        productPrice.setText(feedDetailViewModel.getPrice());

        if (feedDetailViewModel.getRating() > 0) {
            productRating.setRating((feedDetailViewModel.getRating().floatValue()));
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.INVISIBLE);
        }

        if (TextUtils.isEmpty(feedDetailViewModel.getCashback()))
            cashback.setVisibility(View.GONE);
        else {
            cashback.setVisibility(View.VISIBLE);
            cashback.setText(CASHBACK + " " + feedDetailViewModel.getCashback());
        }

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

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(String.valueOf(feedDetailViewModel.getProductId
                        ()), feedDetailViewModel.isWishlist(), getAdapterPosition());
            }
        });
    }
}
