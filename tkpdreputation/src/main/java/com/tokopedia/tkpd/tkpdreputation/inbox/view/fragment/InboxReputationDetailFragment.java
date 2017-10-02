package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.facebook.CallbackManager;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.di.DaggerInboxReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationReportActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationDetailAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.customview.ShareReviewDialog;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationDetailPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailPassModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ShareModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailFragment extends BaseDaggerFragment
        implements InboxReputationDetail.View, ReputationAdapter.ReputationListener {

    private static final int REQUEST_GIVE_REVIEW = 101;
    private static final int REQUEST_EDIT_REVIEW = 102;
    private static final int REQUEST_REPORT_REVIEW = 103;
    private RecyclerView listProduct;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayoutManager layoutManager;
    private InboxReputationDetailAdapter adapter;
    private ShareReviewDialog shareReviewDialog;
    private CallbackManager callbackManager;
    View mainView;

    TkpdProgressDialog progressDialog;

    @Inject
    InboxReputationDetailPresenter presenter;

    InboxReputationDetailPassModel passModel;

    public static InboxReputationDetailFragment createInstance(InboxReputationDetailPassModel
                                                                       model, int tab) {
        InboxReputationDetailFragment fragment = new InboxReputationDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReputationDetailActivity.ARGS_PASS_DATA, model);
        bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION_DETAIL;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerInboxReputationComponent reputationComponent =
                (DaggerInboxReputationComponent) DaggerInboxReputationComponent
                        .builder()
                        .appComponent(appComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getParcelable(InboxReputationDetailActivity
                .ARGS_PASS_DATA) != null)
            passModel = getArguments().getParcelable(InboxReputationDetailActivity.ARGS_PASS_DATA);
        else if (savedInstanceState != null && savedInstanceState.getParcelable
                (InboxReputationDetailActivity.ARGS_PASS_DATA) != null)
            passModel = savedInstanceState.getParcelable(InboxReputationDetailActivity
                    .ARGS_PASS_DATA);
        else
            getActivity().finish();

        initVar();
    }

    private void initVar() {
        callbackManager = CallbackManager.Factory.create();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        InboxReputationDetailTypeFactory typeFactory = new InboxReputationDetailTypeFactoryImpl
                (this);
        adapter = new InboxReputationDetailAdapter(typeFactory);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_detail, container,
                false);
        mainView = parentView.findViewById(R.id.main);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        listProduct = (RecyclerView) parentView.findViewById(R.id.product_list);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {
        listProduct.setLayoutManager(layoutManager);
        listProduct.setAdapter(adapter);
        swipeToRefresh.setOnRefreshListener(onRefresh());

    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getInboxDetail(
                passModel.getReputationId(),
                getArguments().getInt(InboxReputationDetailActivity.ARGS_TAB, -1)
        );
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetInboxDetail(String errorMessage) {
        if (getActivity() != null && mainView != null)
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, errorMessage,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getInboxDetail(
                                    passModel.getReputationId(),
                                    getArguments().getInt(InboxReputationDetailActivity.ARGS_TAB, -1)
                            );
                        }
                    });
    }

    @Override
    public void finishLoading() {
        adapter.removeLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGetInboxDetail(InboxReputationItemViewModel inboxReputationItemViewModel,
                                        List<Visitable> list) {
        adapter.clearList();
        adapter.addHeader(createHeaderModel(inboxReputationItemViewModel));
        adapter.addList(list);
        adapter.notifyDataSetChanged();

        checkIsShopFavorited(inboxReputationItemViewModel);
    }

    private void checkIsShopFavorited(InboxReputationItemViewModel inboxReputationItemViewModel) {
        if (inboxReputationItemViewModel.getRole() == InboxReputationItemViewModel.ROLE_SELLER) {
            presenter.checkShopFavorited(inboxReputationItemViewModel.getShopId());
        }
    }

    @Override
    public void onEditReview(InboxReputationDetailItemViewModel element) {
        startActivityForResult(
                InboxReputationFormActivity.getEditReviewIntent(getActivity(),
                        element.getReviewId(),
                        passModel.getReputationId(),
                        element.getProductId(),
                        String.valueOf(element.getShopId()),
                        element.getReviewStar(),
                        element.getReview(),
                        element.getReviewAttachment(),
                        element.getProductAvatar(),
                        element.getProductName(),
                        element.getProductUrl()),
                REQUEST_EDIT_REVIEW
        );
    }

    @Override
    public void onGoToGiveReview(String reviewId, String productId,
                                 int shopId, boolean reviewIsSkippable, String productAvatar, String productName, String productUrl) {
        startActivityForResult(
                InboxReputationFormActivity.getGiveReviewIntent(
                        getActivity(),
                        reviewId,
                        passModel.getReputationId(), productId,
                        String.valueOf(shopId), reviewIsSkippable,
                        productAvatar, productName, productUrl),
                REQUEST_GIVE_REVIEW);
    }

    @Override
    public void onErrorSendSmiley(String errorMessage) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showLoadingDialog() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void finishLoadingDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showRefresh() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void onErrorRefreshInboxDetail(String errorMessage) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRefreshGetInboxDetail(InboxReputationItemViewModel inboxReputationViewModel,
                                               List<Visitable> list) {
        adapter.clearList();
        adapter.addHeader(createHeaderModel(inboxReputationViewModel));
        adapter.addList(list);
        adapter.notifyDataSetChanged();
        getActivity().setResult(Activity.RESULT_OK);
    }

    private InboxReputationDetailHeaderViewModel createHeaderModel(
            InboxReputationItemViewModel inboxReputationViewModel) {
        return new InboxReputationDetailHeaderViewModel(
                inboxReputationViewModel.getRevieweePicture(),
                inboxReputationViewModel.getRevieweeName(),
                getTextDeadline(inboxReputationViewModel),
                inboxReputationViewModel.getReputationDataViewModel(),
                inboxReputationViewModel.getRole(),
                inboxReputationViewModel.getRevieweeBadgeCustomerViewModel(),
                inboxReputationViewModel.getRevieweeBadgeSellerViewModel(),
                inboxReputationViewModel.getShopId());
    }

    private String getTextDeadline(InboxReputationItemViewModel element) {
        return MainApplication.getAppContext().getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                MainApplication.getAppContext().getString(R.string.deadline_suffix);
    }

    @Override
    public void finishRefresh() {
        swipeToRefresh.setRefreshing(false);

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
    public int getTab() {
        return getArguments().getInt(InboxReputationDetailActivity
                .ARGS_TAB);
    }

    @Override
    public void onGoToReportReview(int shopId, String reviewId) {
        startActivityForResult(InboxReputationReportActivity.getCallingIntent(
                getActivity(),
                shopId,
                reviewId),
                REQUEST_REPORT_REVIEW);
    }

    @Override
    public void onSuccessSendSmiley(int score) {
        if (adapter.getHeader() != null) {
            InboxReputationDetailHeaderViewModel header = adapter.getHeader();
            header.getReputationDataViewModel()
                    .setReviewerScore(score);
            header.getReputationDataViewModel()
                    .setInserted(true);
            header.getReputationDataViewModel()
                    .setEditable(score != InboxReputationDetailHeaderViewHolder.SMILEY_GOOD);
            adapter.getList().remove(0);
            adapter.getList().add(0, header);
            adapter.notifyItemChanged(0);
            getActivity().setResult(Activity.RESULT_OK);
        }
    }

    @Override
    public void onErrorCheckShopIsFavorited() {

    }

    @Override
    public void onSuccessCheckShopIsFavorited(int shopFavorited) {
        adapter.getHeader().getRevieweeBadgeSellerViewModel().setIsFavorited(shopFavorited);
        adapter.notifyItemChanged(0);
    }

    @Override
    public void onErrorFavoriteShop(String errorMessage) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessFavoriteShop() {
        adapter.getHeader().getRevieweeBadgeSellerViewModel().setIsFavorited(
                adapter.getHeader().getRevieweeBadgeSellerViewModel().getIsFavorited() == 1 ? 0 : 1
        );
        adapter.notifyItemChanged(0);
    }

    @Override
    public void onDeleteReviewResponse(InboxReputationDetailItemViewModel element) {
        presenter.deleteReviewResponse(element.getReviewId(),
                element.getProductId(),
                String.valueOf(element.getShopId()),
                String.valueOf(element.getReputationId())
        );
    }

    @Override
    public void onErrorDeleteReviewResponse(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessDeleteReviewResponse() {
        refreshPage();
    }

    @Override
    public void onSendReplyReview(InboxReputationDetailItemViewModel element, String replyReview) {
        presenter.sendReplyReview(element.getReputationId(), element.getProductId(),
                element.getShopId(), element.getReviewId(), replyReview);
    }

    @Override
    public void onErrorReplyReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar

                (getActivity(), errorMessage);
    }

    @Override
    public void onSuccessReplyReview() {
        refreshPage();
    }

    @Override
    public void onShareReview(String productName, String productAvatar, String productUrl, String review) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        if (shareReviewDialog == null && callbackManager != null) {
            shareReviewDialog = new ShareReviewDialog(getActivity(), callbackManager,
                    this);
        }

        if (shareReviewDialog != null) {
            shareReviewDialog.setModel(new ShareModel(
                    productName,
                    review,
                    productUrl,
                    productAvatar
            ));
            shareReviewDialog.show();
        }
    }

    @Override
    public void onLikeReview(int adapterPosition, String reviewId, int formerLikeStatus, String productId, String
            shopId) {
        presenter.likeDislikeReview(adapterPosition, reviewId, formerLikeStatus, productId, shopId);
    }

    @Override
    public void onErrorLikeDislikeReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessLikeDislikeReview(int adapterPosition, int likeStatus, int totalLike) {
        if (adapter.getList().size() < adapterPosition
                && adapter.getList().get(adapterPosition) != null
                && adapter.getList().get(adapterPosition) instanceof InboxReputationDetailItemViewModel) {
            InboxReputationDetailItemViewModel detailItemViewModel =
                    (InboxReputationDetailItemViewModel) adapter.getList().get(adapterPosition);
            detailItemViewModel.getLikeDislikeViewModel().setTotalLike(totalLike);
            detailItemViewModel.getLikeDislikeViewModel().setLikeStatus(likeStatus);
            adapter.notifyItemChanged(adapterPosition);
        } else {
            refreshPage();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(InboxReputationDetailActivity.ARGS_PASS_DATA, passModel);
    }

    @Override
    public void onReputationSmileyClicked(String name, final String score) {
        if (!TextUtils.isEmpty(score)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getReputationSmileyMessage(name));
            builder.setPositiveButton(getString(R.string.send),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.sendSmiley(passModel.getReputationId(), score, passModel.getRole());
                        }
                    });
            builder.setNegativeButton(getString(R.string.title_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int param) {
                            dialog.dismiss();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }
    }

    @Override
    public void onFavoriteShopClicked(int shopId) {
        presenter.onFavoriteShopClicked(shopId);
    }

    private String getReputationSmileyMessage(String name) {
        return getString(R.string.smiley_prompt_prefix) + " " + name
                + " " + getSmileySuffixMessage();
    }

    private String getSmileySuffixMessage() {
        return getString(R.string.smiley_prompt_suffix_shop);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GIVE_REVIEW && resultCode == Activity.RESULT_OK) {
            refreshPage();
            getActivity().setResult(Activity.RESULT_OK);
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string
                    .success_send_review));
        } else if (requestCode == REQUEST_GIVE_REVIEW && resultCode ==
                InboxReputationFormFragment.RESULT_CODE_SKIP) {
            refreshPage();
            getActivity().setResult(Activity.RESULT_OK);
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string
                    .success_skip_review));
        } else if (requestCode == REQUEST_EDIT_REVIEW && resultCode == Activity.RESULT_OK) {
            refreshPage();
            getActivity().setResult(Activity.RESULT_OK);
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string
                    .success_edit_review));
        } else if (requestCode == REQUEST_REPORT_REVIEW && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string
                    .success_report_review));
        } else
            super.onActivityResult(requestCode, resultCode, data);

    }

    private void refreshPage() {
        presenter.refreshPage(passModel.getReputationId(),
                getArguments().getInt(InboxReputationDetailActivity.ARGS_TAB, -1));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        callbackManager = null;
    }
}
