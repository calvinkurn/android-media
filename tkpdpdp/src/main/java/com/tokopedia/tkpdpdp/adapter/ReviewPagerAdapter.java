package com.tokopedia.tkpdpdp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 8/14/17.
 */

public class ReviewPagerAdapter extends PagerAdapter{

    private Context context;
    private List<Review> reviews = new ArrayList<>();
    private final ProductDetailView listener;

    public ReviewPagerAdapter(Context context, List<Review> reviews, ProductDetailView listener) {
        this.context = context;
        this.reviews = reviews;
        this.listener = listener;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_most_helpful_review, container, false);

        ImageView bannerImage = (ImageView) view.findViewById(R.id.image_reviewer);
        ImageHandler.LoadImage(
                bannerImage,
                reviews.get(position).getUser().getUserImage()
        );
        ((TextView) view.findViewById(R.id.text_reviewer_name)).setText( reviews.get(position).getUser().getFullName());
        ((TextView) view.findViewById(R.id.text_review_time)).setText(reviews.get(position).getReviewCreateTime().getDateTimeFmt1());
        ((TextView) view.findViewById(R.id.text_review)).setText( reviews.get(position).getReviewMessage());
        ((ImageView) view.findViewById(R.id.image_rating_review_pdp)).setImageResource(getRatingDrawable(reviews.get(position).getProductRating()/20));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onProductReviewClicked();
            }
        });

        container.addView(view);
        return view;
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
}
