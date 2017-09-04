package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationDetailAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationDetailPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailPassModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailFragment extends BaseDaggerFragment
        implements InboxReputationDetail.View, ReputationAdapter.ReputationListener {

    private static final int REQUEST_GIVE_REVIEW = 101;
    RecyclerView listProduct;
    LinearLayoutManager layoutManager;
    InboxReputationDetailAdapter adapter;
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
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
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
        listProduct = (RecyclerView) parentView.findViewById(R.id.product_list);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {
        listProduct.setLayoutManager(layoutManager);
        listProduct.setAdapter(adapter);

        listProduct.addOnScrollListener(onScroll());


    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (!adapter.isLoading())
                    presenter.getNextPage(lastItemPosition, visibleItem);
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
        finishLoading();
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

    private void finishLoading() {
        adapter.removeLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGetInboxDetail(RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel,
                                        RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel,
                                        List<Visitable> list) {
        finishLoading();
        adapter.clearList();
        adapter.addHeader(createHeaderModel(passModel,
                revieweeBadgeCustomerViewModel, revieweeBadgeSellerViewModel));
        adapter.addList(list);
        adapter.notifyDataSetChanged();
    }

    private InboxReputationDetailHeaderViewModel createHeaderModel(
            InboxReputationDetailPassModel passModel,
            RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel,
            RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel) {
        return new InboxReputationDetailHeaderViewModel(
                passModel.getRevieweeImage(),
                passModel.getRevieweeName(), passModel.getDeadlineText(),
                passModel.getReputationDataViewModel(),
                passModel.getRole(),
                revieweeBadgeCustomerViewModel,
                revieweeBadgeSellerViewModel);
    }

    @Override
    public void onEditReview() {

    }

    @Override
    public void onSkipReview(String reviewId) {

    }

    @Override
    public void onGoToGiveReview(String reviewId, String productId, int shopId) {
        startActivityForResult(
                InboxReputationFormActivity.getGiveReviewIntent(getActivity(), reviewId,
                        passModel.getReputationId(), productId, String.valueOf(shopId)),
                REQUEST_GIVE_REVIEW);
    }

    @Override
    public void onErrorSendSmiley(String errorMessage) {
        if (getActivity() != null)
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSendSmiley() {

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(InboxReputationDetailActivity.ARGS_PASS_DATA, passModel);
    }

    @Override
    public void onReputationSmileyClicked(String name, final String value) {
        if (!TextUtils.isEmpty(value)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getReputationSmileyMessage(name));
            builder.setPositiveButton(getString(R.string.send),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.sendSmiley(passModel.getReputationId(), value);
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

    private String getReputationSmileyMessage(String name) {
        return getString(R.string.smiley_prompt_prefix) + " " + name
                + " " + getSmileySuffixMessage();
    }

    private String getSmileySuffixMessage() {
        return getString(R.string.smiley_prompt_suffix_shop);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GIVE_REVIEW && resultCode == Activity.RESULT_OK) {

        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
