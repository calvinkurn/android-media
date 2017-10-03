package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.RecentView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewDetailProductViewModel;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailProductViewHolder extends AbstractViewHolder<RecentViewDetailProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recent_view_product_detail;

    public TextView productName;
    public TextView productPrice;
    public ImageView productImage;
    public ImageView wishlist;
    public RatingBar productRating;
    public TextView cashback;
    public TextView wholesale;
    public TextView preorder;
    public TextView shopName;
    public TextView shopLocation;

    public ImageView freeReturn;
    public ImageView goldMerchant;
    public ImageView officialStore;

    public View mainView;

    private final RecentView.View viewListener;

    public RecentViewDetailProductViewHolder(View itemView, RecentView.View viewListener) {
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
        goldMerchant = (ImageView) itemView.findViewById(R.id.gold_merchant);
        officialStore = (ImageView) itemView.findViewById(R.id.official_store);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopLocation = (TextView) itemView.findViewById(R.id.shop_location);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final RecentViewDetailProductViewModel element) {

        ImageHandler.LoadImage(productImage, element.getImageSource());

//        if (element.isWishlist()) {
//            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist_faved);
//        } else {
//            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist);
//        }

        productName.setText(MethodChecker.fromHtml(element.getName()));
        productPrice.setText(element.getPrice());

        if (element.getRating() > 0) {
            productRating.setRating(element.getRating());
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.GONE);
        }

        if (element.getCashback().equals(""))
            cashback.setVisibility(View.GONE);
        else {
            cashback.setVisibility(View.VISIBLE);
            cashback.setText(element.getCashback());
        }

        if (element.isWholesale())
            wholesale.setVisibility(View.VISIBLE);
        else
            wholesale.setVisibility(View.GONE);

        if (element.isFreeReturn())
            freeReturn.setVisibility(View.VISIBLE);
        else
            freeReturn.setVisibility(View.GONE);

        if (element.isPreorder())
            preorder.setVisibility(View.VISIBLE);
        else
            preorder.setVisibility(View.GONE);
//
//        wishlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewListener.onWishlistClicked(
//                        getAdapterPosition(),
//                        element.getProductId(),
//                        element.isWishlist());
//            }
//        });

        if (element.isOfficial()) {
            officialStore.setVisibility(View.VISIBLE);
            goldMerchant.setVisibility(View.GONE);
        } else if (element.isGold()) {
            officialStore.setVisibility(View.GONE);
            goldMerchant.setVisibility(View.VISIBLE);
        } else {
            officialStore.setVisibility(View.GONE);
            goldMerchant.setVisibility(View.GONE);
        }

        shopName.setText(MethodChecker.fromHtml(element.getShopName()));
        shopLocation.setText(element.getShopLocation());

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(String.valueOf(element.getProductId()));
            }
        });
    }

}
