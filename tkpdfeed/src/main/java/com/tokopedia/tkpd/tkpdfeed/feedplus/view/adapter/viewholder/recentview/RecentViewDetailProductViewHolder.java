package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.LabelsAdapter;
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
    private RecyclerView labels;
    public TextView shopName;
    public TextView shopLocation;
    public ImageView iconLocation;

    public ImageView freeReturn;
    public ImageView goldMerchant;
    public ImageView officialStore;

    public View mainView;

    private final RecentView.View viewListener;
    private LabelsAdapter labelsAdapter;

    public RecentViewDetailProductViewHolder(View itemView, RecentView.View viewListener) {
        super(itemView);
        productName = (TextView) itemView.findViewById(R.id.product_name);
        productPrice = (TextView) itemView.findViewById(R.id.product_price);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        wishlist = (ImageView) itemView.findViewById(R.id.wishlist);
        productRating = (RatingBar) itemView.findViewById(R.id.product_rating);
        labels = (RecyclerView) itemView.findViewById(R.id.labels);
        freeReturn = (ImageView) itemView.findViewById(R.id.free_return);
        mainView = itemView.findViewById(R.id.main_view);
        goldMerchant = (ImageView) itemView.findViewById(R.id.gold_merchant);
        officialStore = (ImageView) itemView.findViewById(R.id.official_store);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopLocation = (TextView) itemView.findViewById(R.id.shop_location);
        iconLocation = (ImageView)  itemView.findViewById(R.id.ic_location);
        this.viewListener = viewListener;

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext
                (), LinearLayoutManager.HORIZONTAL, false);
        labels.setLayoutManager(layoutManager);
        labelsAdapter = new LabelsAdapter();
        labels.setAdapter(labelsAdapter);

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

        if (!element.getLabels().isEmpty()) {
            labels.setVisibility(View.VISIBLE);
            labelsAdapter.setList(element.getLabels());
        } else {
            labels.setVisibility(View.GONE);
        }

        if (element.isFreeReturn())
            freeReturn.setVisibility(View.VISIBLE);
        else
            freeReturn.setVisibility(View.GONE);
//
//        wishlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewListener.onWishlistClicked(
//                        getAdapterPosition(),
//                        element.getContentId(),
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

        if (element.isOfficial()) {
            iconLocation.setImageDrawable(ContextCompat.getDrawable(MainApplication.getAppContext(),com.tokopedia.core.R.drawable.ic_icon_authorize_grey));
            shopLocation.setText(MainApplication.getAppContext().getResources().getString(R.string.authorized)
            );
        } else {
            shopLocation.setText(element.getShopLocation());
            iconLocation.setImageDrawable(ContextCompat.getDrawable(MainApplication.getAppContext(),com.tokopedia.core.R.drawable.ic_icon_location_grey));
        }

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(String.valueOf(element.getProductId()));
            }
        });
    }

}
