package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter.ReviewShopContract;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter.ReviewShopPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopFragment extends BaseListFragment<ReviewShopModelContent, ReviewShopTypeFactoryAdapter>
        implements ReviewProductContentViewHolder.ListenerReviewHolder, ReviewShopContract.View, ReviewShopViewHolder.ShopReviewHolderListener {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "shop_domain";
    @Inject
    ReviewShopPresenter shopReviewPresenter;

    private ProgressDialog progressDialog;

    protected String shopId;
    protected String shopDomain;

    public static ReviewShopFragment createInstance(String shopId, String shopDomain) {
        ReviewShopFragment shopReviewFragment = new ReviewShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID, shopId);
        bundle.putString(SHOP_DOMAIN, shopDomain);
        shopReviewFragment.setArguments(bundle);
        return shopReviewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        return inflater.inflate(R.layout.fragment_shop_review, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(SHOP_ID, "");
        shopDomain = getArguments().getString(SHOP_DOMAIN, "");
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
                .appComponent(((MainApplication) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);
        shopReviewPresenter.attachView(this);
    }

    @Override
    public void onItemClicked(ReviewShopModelContent productReviewModelContent) {

    }

    @Override
    public void loadData(int page) {
        shopReviewPresenter.getShopReview(shopDomain, shopId, page);
    }

    @Override
    protected ReviewShopTypeFactoryAdapter getAdapterTypeFactory() {
        return new ReviewShopTypeFactoryAdapter(this, this);
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
    public void onDeleteReviewResponse(ReviewProductModelContent element) {
        shopReviewPresenter.deleteReview(element.getReviewId(), element.getReputationId(), element.getProductId());
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
    public void onLikeDislikePressed(String reviewId, int likeStatus, String productId) {
        shopReviewPresenter.postLikeDislikeReview(reviewId, likeStatus, productId);
    }

    @Override
    public void onErrorDeleteReview(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId) {
        ((ReviewProductAdapter) getAdapter()).updateDeleteReview(reviewId);
    }

    @Override
    public void onErrorPostLikeDislike(Throwable e) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId) {
        ((ReviewProductAdapter) getAdapter()).updateLikeStatus(likeDislikeDomain.getLikeStatus(),
                likeDislikeDomain.getTotalLike(), reviewId);
    }

    @Override
    public void onGoToDetailProduct(String productId) {
        ProductPass productPass = ProductPass.Builder.aProductPass()
                .setProductId(productId)
                .build();
        ((PdpRouter) getActivity().getApplication()).goToProductDetail(getActivity(), productPass);
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showProgressLoading() {
        progressDialog.show();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ReviewShopModelContent, ReviewShopTypeFactoryAdapter> createAdapterInstance() {
        return new ReviewProductAdapter(getAdapterTypeFactory());
    }
}
