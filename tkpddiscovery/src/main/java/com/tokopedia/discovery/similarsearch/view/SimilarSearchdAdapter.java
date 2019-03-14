package com.tokopedia.discovery.similarsearch.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.similarsearch.analytics.SimilarSearchTracking;
import com.tokopedia.discovery.similarsearch.model.Badges;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;
import com.tokopedia.tkpdpdp.customview.RatingView;

import java.util.ArrayList;
import java.util.List;

public class SimilarSearchdAdapter extends RecyclerView.Adapter<SimilarSearchdAdapter.SimilarProdcutItem> {
    private final WishListClickListener wishListClickListener;
    List<ProductsItem> productsItems = new ArrayList<>();

    public SimilarSearchdAdapter(WishListClickListener wishListClickListener) {
        this.wishListClickListener = wishListClickListener;
    }

    @Override
    public SimilarProdcutItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_product_item, parent, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getLayoutParams());
        lp.setMargins(30, 20,
                0, 30);
        view.setLayoutParams(lp);
        return new SimilarProdcutItem(view);
    }


    @Override
    public void onBindViewHolder(SimilarProdcutItem holder, int position) {
        holder.updateView(productsItems.get(position), position);
    }

    public void setProductsItems(List<ProductsItem> productsItems) {
        this.productsItems = productsItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productsItems.size();
    }

    public void setWishlistButtonEnabled(int adapterPosition, boolean value) {
        productsItems.get(adapterPosition).setWishlistButtonEnabled(value);
    }

    public void updateWishlistStatus(String productId, boolean value) {
        productsItems.get(getIndex(productId)).setWishListed(value);
    }

    public int getIndex(String productId) {
        ProductsItem productItem = new ProductsItem();
        productItem.setId(Integer.parseInt(productId));
        return productsItems.indexOf(productItem);
    }

    class SimilarProdcutItem extends RecyclerView.ViewHolder {
        ProductsItem productsItem;
        private ImageView mProductImage;
        private TextView mTitle;
        private TextView mTextOriginalPrice;
        private TextView mPrice;
        private TextView mTextDiscount;
        private ImageView mWishlistButton;
        private RelativeLayout mWishlistButtonContainer;
        private TextView mLocation;
        private View mRatingReviewContainer;
        private ImageView mRating;
        private TextView mReviewCount;
        private LinearLayout mBadgesContainer;
        private View mLocationContainer;

        public SimilarProdcutItem(View itemView) {
            super(itemView);
            mProductImage = itemView.findViewById(R.id.product_image);
            mTitle = itemView.findViewById(R.id.title);
            mTextOriginalPrice = itemView.findViewById(R.id.text_original_price);
            mPrice = itemView.findViewById(R.id.price);
            mTextDiscount = itemView.findViewById(R.id.text_discount);
            mWishlistButton = itemView.findViewById(R.id.wishlist_button);
            mWishlistButtonContainer = itemView.findViewById(R.id.wishlist_button_container);
            mLocation = itemView.findViewById(R.id.location);
            mRatingReviewContainer = itemView.findViewById(R.id.rating_review_container);
            mRating = itemView.findViewById(R.id.rating);
            mReviewCount = itemView.findViewById(R.id.review_count);
            mBadgesContainer = itemView.findViewById(R.id.badges_container);
            mLocationContainer = itemView.findViewById(R.id.shop_info_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getProductIntent(String.valueOf(productsItem.getId()));
                    itemView.getContext().startActivity(intent);
                    Object productItem = null;
                    if (productsItem != null) {
                        if (productsItem.getId() != 0) {
                            productItem = productsItem.getProductAsObjectDataLayer(String.valueOf(getIndex(String.valueOf(productsItem.getId()))));
                        }
                    }
                    SimilarSearchTracking.eventClickSimilarProduct(v.getContext(), "/searchproduct - product " + productsItem.getId(), productsItem.getOriginProductID(),productItem);
                }


            });

        }

        private Intent getProductIntent(String productId){
            if (itemView.getContext() != null) {
                return RouteManager.getIntentInternal(itemView.getContext(),
                        UriUtil.buildUri(ApplinkConstInternal.Marketplace.PRODUCT_DETAIL, productId));
            } else {
                return null;
            }
        }

        public void updateView(ProductsItem productsItem, int position) {


            this.productsItem = productsItem;
            int placeholderColors[] = {R.color.light_green1, R.color.light_blue1, R.color.light_red1, R.color.light_yellow1};
            ImageHandler.loadImage(itemView.getContext(), mProductImage, productsItem.getImageUrl(), new ColorDrawable(ContextCompat.getColor(itemView.getContext(), placeholderColors[position % 4])));
            mTitle.setText(productsItem.getName());
            mTextOriginalPrice.setText(productsItem.getOriginalPrice());
            mPrice.setText(productsItem.getPrice());

            if (productsItem.getShop() != null && productsItem.getShop().getCity() != null) {
                String city = productsItem.getShop().getCity();
                if (isBadgesExist(productsItem)) {
                    mLocation.setText(" \u2022 " + MethodChecker.fromHtml(city));
                } else {
                    mLocation.setText(MethodChecker.fromHtml(city));
                }
                mLocationContainer.setVisibility(View.VISIBLE);
            } else {
                mLocationContainer.setVisibility(View.INVISIBLE);
            }

            if (productsItem.isWishListed()) {
                mWishlistButton.setBackgroundResource(R.drawable.ic_wishlist_red);
            } else {
                mWishlistButton.setBackgroundResource(R.drawable.ic_wishlist);
            }
            mWishlistButtonContainer.setEnabled(productsItem.isWishlistButtonEnabled());

            mWishlistButtonContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productsItem.isWishlistButtonEnabled()) {
                        wishListClickListener.onWishlistButtonClicked(productsItem, getAdapterPosition());
                    }
                }
            });
            mTextDiscount.setText(productsItem.getDiscountPercentage() + "%");

            if (productsItem.getRating() != 0) {
                mRatingReviewContainer.setVisibility(View.VISIBLE);
                mRating.setImageResource(
                        RatingView.getRatingDrawable(Math.round(productsItem.getRating()))
                );
                mReviewCount.setText("(" + productsItem.getCountReview() + ")");
            } else {
                mRatingReviewContainer.setVisibility(View.INVISIBLE);
            }
            renderBadges(productsItem.getBadges());
        }

        protected void renderBadges(List<Badges> badges) {
            mBadgesContainer.removeAllViews();
            if (badges == null) {
                return;
            }
            for (Badges badgeItem : badges) {
                LuckyShopImage.loadImage(itemView.getContext(), badgeItem.getImageUrl(), mBadgesContainer);
            }
        }
    }

    private boolean isBadgesExist(ProductsItem productsItem) {
        List<Badges> badgesList = productsItem.getBadges();
        if (badgesList == null || badgesList.isEmpty()) {
            return false;
        }

        if (productsItem.getShop().isGoldShop()) {
            return true;
        }
        return false;
    }


    public interface WishListClickListener {
        void onWishlistButtonClicked(ProductsItem productItem, int adapterPosition);
    }

}
