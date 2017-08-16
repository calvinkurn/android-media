package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.adapter.ReviewPagerAdapter;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;

/**
 * Created by alifa on 8/14/17.
 */

public class MostHelpfulReviewView extends BaseView<List<Review>,ProductDetailView> {

    private ReviewPagerAdapter reviewPagerAdapter;

    private ViewPager vpImage;
    private CirclePageIndicator circlePageIndicator;
    private TextView textAllReview;

    public MostHelpfulReviewView(Context context) {
        super(context);
        initView(context);
    }

    public MostHelpfulReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.most_helpful_review_pdp;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        vpImage = (ViewPager) findViewById(R.id.view_pager_review);
        textAllReview = (TextView) findViewById(R.id.text_all_reviews);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator_picture);

    }

    @Override
    public void renderData(@NonNull List<Review> data) {
        reviewPagerAdapter = new ReviewPagerAdapter(getContext(), data);
        vpImage.setAdapter(reviewPagerAdapter);
        vpImage.setAdapter(reviewPagerAdapter);
        circlePageIndicator.setViewPager(vpImage);
        reviewPagerAdapter.notifyDataSetChanged();
        circlePageIndicator.notifyDataSetChanged();
    }

    public void updateTotalReviews(String value) {
        textAllReview.setText(getResources().getString(R.string.title_all_reviews)+" ("+value+")");
    }
}
