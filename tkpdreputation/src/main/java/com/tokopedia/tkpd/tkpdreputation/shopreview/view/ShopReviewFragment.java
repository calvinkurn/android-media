package com.tokopedia.tkpd.tkpdreputation.shopreview.view;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.tkpd.tkpdreputation.di.ReputationModule;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelContent;
import com.tokopedia.tkpd.tkpdreputation.shopreview.view.adapter.ShopReviewModelContent;
import com.tokopedia.tkpd.tkpdreputation.shopreview.view.adapter.ShopReviewTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.shopreview.view.presenter.ShopReviewContract;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.shopreview.view.presenter.ShopReviewPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewFragment extends BaseListFragment<ShopReviewModelContent, ShopReviewTypeFactoryAdapter>
        implements ProductReviewContentViewHolder.ListenerReviewHolder, ShopReviewContract.View {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "shop_domain";
    @Inject
    ShopReviewPresenter shopReviewPresenter;

    private String shopId;
    private String shopDomain;

    public static ShopReviewFragment createInstance(String shopId, String shopDomain) {
        ShopReviewFragment shopReviewFragment = new ShopReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID, shopId);
        bundle.putString(SHOP_DOMAIN, shopDomain);
        shopReviewFragment.setArguments(bundle);
        return shopReviewFragment;
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
    public void onItemClicked(ShopReviewModelContent productReviewModelContent) {

    }

    @Override
    public void loadData(int page) {
        shopReviewPresenter.getShopReview(shopDomain, shopId, page);
    }

    @Override
    protected ShopReviewTypeFactoryAdapter getAdapterTypeFactory() {
        return new ShopReviewTypeFactoryAdapter(this);
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

    }

    @Override
    public void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain) {

    }

    @Override
    public void onErrorPostLikeDislike(Throwable e) {

    }

    @Override
    public void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain) {

    }
}
