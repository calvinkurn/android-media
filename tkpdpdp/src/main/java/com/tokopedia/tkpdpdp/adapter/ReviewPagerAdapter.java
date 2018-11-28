package com.tokopedia.tkpdpdp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Data;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.mosthelpful.ReviewImageAttachment;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.tracking.ProductPageTracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 8/14/17.
 */

public class ReviewPagerAdapter extends PagerAdapter{

    private Context context;
    private List<Review> reviews = new ArrayList<>();
    private final ProductDetailView listener;
    private final ProductDetailData data;

    public ReviewPagerAdapter(Context context, ProductDetailView listener, ProductDetailData data) {
        this.context = context;
        this.reviews = data.getReviewList();
        this.listener = listener;
        this.data = data;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        Review review = reviews.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_most_helpful_review, container, false);

        ImageView bannerImage = (ImageView) view.findViewById(R.id.image_reviewer);
        ImageHandler.loadImageRounded2(
                context, bannerImage, reviews.get(position).getUser().getUserImage()
        );
        TextView tv_reviewer_name = ((TextView) view.findViewById(R.id.text_reviewer_name));
        TextView tv_review_time = ((TextView) view.findViewById(R.id.text_review_time));
        TextView tv_text_review = ((TextView) view.findViewById(R.id.text_review));
        ImageView image_rating_review = ((ImageView) view.findViewById(R.id.image_rating_review_pdp));
        GridView grid_image = (GridView) view.findViewById(R.id.grid_image);

        tv_reviewer_name.setText(review.getUser().getFullName());
        tv_review_time.setText(review.getReviewCreateTime().getDateTimeFmt1());
        tv_text_review.setText( MethodChecker.fromHtml(review.getReviewMessage()));
        image_rating_review.setImageResource(getRatingDrawable(review.getProductRating()/20));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMostHelpfulClick();
            }
        });

        if (review.getReviewImageAttachment().size() > 0){
            grid_image.setVisibility(View.VISIBLE);
            tv_text_review.setLines(2);

            ItemAdapter adapter = new ItemAdapter(
                    String.valueOf(data.getInfo().getProductId()),
                    String.valueOf(review.getReviewId())
            );
            adapter.setData(review.getReviewImageAttachment());

            grid_image.setAdapter(adapter);
        }

        container.addView(view);
        return view;
    }

    private void onMostHelpfulClick() {
        String productId = String.valueOf(data.getInfo().getProductId());
        String shopId = String.valueOf(data.getShopInfo().getShopId());
        String productName = data.getInfo().getProductName();
        listener.onProductReviewClicked(productId, shopId, productName);
    }

    private int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.ic_star_one;
            case 2:
                return R.drawable.ic_star_two;
            case 3:
                return R.drawable.ic_star_three;
            case 4:
                return R.drawable.ic_star_four;
            case 5:
                return R.drawable.ic_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }

    private class ItemAdapter extends BaseAdapter {
        private List<ReviewImageAttachment> data;
        private String productId;
        private String reviewId;

        public ItemAdapter(String productId,
                           String reviewId) {
            this.data = new ArrayList<>();
            this.productId = productId;
            this.reviewId = reviewId;
        }

        public void setData(List<ReviewImageAttachment> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if(data == null) return 0;
            else if (data.size() > 3) return 3;
            else return data.size();
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ReviewImageAttachment item = data.get(position);

            LayoutInflater inflater = LayoutInflater.from(context);
            View gridView;

            if (view == null) {
                gridView = inflater.inflate(R.layout.layout_image_review_item_most_helpful
                        ,null);
                ImageView reviewImage = gridView.findViewById(R.id.review_image);
                ImageHandler.loadImageAndCache(reviewImage, item.getUriThumbnail());
                reviewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductPageTracking.eventClickReviewOnMostHelpfulReview(
                                context,
                                productId,
                                reviewId
                        );

                        onMostHelpfulClick();
                    }
                });
            } else {
                gridView = (View) view;
            }

            return gridView;
        }
    }
}
