package com.tokopedia.tkpd.tkpdreputation.productreview.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DetailReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModel;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelContent;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelTitleHeader;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.presenter.ProductReviewContract;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.presenter.ProductReviewPresenter;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.widget.RatingBarReview;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewFragment extends BaseListFragment<ProductReviewModel, ProductReviewTypeFactoryAdapter>
        implements ProductReviewContract.View {

    public static final String EXTRA_PRODUCT_ID = "product_id";

    @Inject
    ProductReviewPresenter productReviewPresenter;

    private TextView ratingProduct;
    private RatingBar ratingProductStar;
    private TextView counterReview;
    private RatingBarReview fiveStarReview;
    private RatingBarReview fourStarReview;
    private RatingBarReview threeStarReview;
    private RatingBarReview twoStarReview;
    private RatingBarReview oneStarReview;

    private String productId;

    public static ProductReviewFragment getInstance(String productId) {
        ProductReviewFragment productReviewFragment = new ProductReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PRODUCT_ID, productId);
        productReviewFragment.setArguments(bundle);
        return productReviewFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerReputationComponent
                .builder()
                .reputationModule(new ReputationModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        productReviewPresenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(EXTRA_PRODUCT_ID, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_review, container, false);
        ratingProduct = view.findViewById(R.id.rating_value);
        ratingProductStar = view.findViewById(R.id.product_rating);
        counterReview = view.findViewById(R.id.total_review);
        fiveStarReview = view.findViewById(R.id.five_star);
        fourStarReview = view.findViewById(R.id.four_star);
        threeStarReview = view.findViewById(R.id.three_star);
        twoStarReview = view.findViewById(R.id.two_star);
        oneStarReview = view.findViewById(R.id.one_star);
        return view;
    }

    @Override
    public void loadData(int page) {
        if (page == 1) {
            productReviewPresenter.getRatingReview(productId);
            productReviewPresenter.getHelpfulReview(productId);
        }
        productReviewPresenter.getProductReview(productId, page, 0);
    }

    @Override
    protected ProductReviewTypeFactoryAdapter getAdapterTypeFactory() {
        return new ProductReviewTypeFactoryAdapter(this);
    }

    @Override
    public void onItemClicked(ProductReviewModel productReviewModel) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productReviewPresenter.detachView();
    }

    @Override
    public void onGoToProfile(String reviewerId) {
        startActivity(
                PeopleInfoNoDrawerActivity.createInstance(getActivity(), String.valueOf(reviewerId))
        );
    }

    @Override
    public void goToPreviewImage(int position, ArrayList<ImageUpload> list) {
        if (MainApplication.getAppContext() instanceof PdpRouter) {
            ArrayList<String> listLocation = new ArrayList<>();
            ArrayList<String> listDesc = new ArrayList<>();

            for (ImageUpload image : list) {
                listLocation.add(image.getPicSrcLarge());
                listDesc.add(image.getDescription());
            }

            ((PdpRouter) MainApplication.getAppContext()).openImagePreview(
                    getActivity(),
                    listLocation,
                    listDesc,
                    position
            );
        }
    }

    @Override
    public void onGoToShopInfo(String shopId) {
        Intent intent = new Intent(MainApplication.getAppContext(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(String.valueOf(shopId), ""));
        startActivity(intent);
    }

    @Override
    public void onDeleteReviewResponse(ProductReviewModelContent element) {
        productReviewPresenter.deleteReview(element.getReviewId(), element.getReputationId(), productId);
    }

    @Override
    public void onSmoothScrollToReplyView(int adapterPosition) {
        getRecyclerView(getView()).smoothScrollToPosition(adapterPosition);
    }

    @Override
    public void onGoToReportReview(String shopId, String reviewId) {
        startActivity(InboxReputationReportActivity.getCallingIntent(
                getActivity(),
                Integer.valueOf(shopId),
                reviewId));
    }

    @Override
    public void onGetListReviewProduct(List<ProductReviewModel> map, boolean isHasNextPage) {
        if (getCurrentPage() == 0) {
            map.add(0, new ProductReviewModelTitleHeader(getString(R.string.product_review_label_all_review)));
        }
        renderList(map, isHasNextPage);
    }

    @Override
    public void onErrorGetListReviewProduct(Throwable e) {

    }

    @Override
    public void onGetListReviewHelpful(List<ProductReviewModel> map) {
        if(map.size() >0) {
            map.add(new ProductReviewModelTitleHeader(getString(R.string.product_review_label_helpful_review)));
            for (int i = 0; i < map.size(); i++) {
                getAdapter().addElement(i, map.get(i));
            }
        }
    }

    @Override
    public void onErrorGetListReviewHelpful(Throwable e) {

    }

    @Override
    public void onGetRatingReview(DataResponseReviewStarCount dataResponseReviewStarCount) {
        ratingProduct.setText(dataResponseReviewStarCount.getRatingScore());
        ratingProductStar.setRating(Float.parseFloat(dataResponseReviewStarCount.getRatingScore()));
        counterReview.setText(getString(R.string.product_review_counter_review_formatted, dataResponseReviewStarCount.getTotalReview()));
        for (DetailReviewStarCount detailReviewStarCount : dataResponseReviewStarCount.getDetail()) {
            Float percentageFloatReview = Float.parseFloat(detailReviewStarCount.getPercentage().replace("%", ""));
            switch (detailReviewStarCount.getRate()) {
                case 5:
                    fiveStarReview.setPercentageProgress(percentageFloatReview);
                    fiveStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 4:
                    fourStarReview.setPercentageProgress(percentageFloatReview);
                    fourStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 3:
                    threeStarReview.setPercentageProgress(percentageFloatReview);
                    threeStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 2:
                    twoStarReview.setPercentageProgress(percentageFloatReview);
                    twoStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
                case 1:
                    oneStarReview.setPercentageProgress(percentageFloatReview);
                    oneStarReview.setTotalReview(detailReviewStarCount.getTotalReview());
                    break;
            }
        }
    }

    @Override
    public void onErrorGetRatingView(Throwable e) {

    }

    @Override
    public void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain) {

    }

    @Override
    public void onErrorPostLikeDislike(Throwable e) {

    }

    @Override
    public void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain) {

    }

    @Override
    public void onErrorDeleteReview(Throwable e) {

    }

    @Override
    public void onLikeDislikePressed(String reviewId, int likeStatus, String productId) {
        productReviewPresenter.postLikeDislikeReview(reviewId, likeStatus, productId);
    }
}
