package com.tokopedia.inbox.rescenter.inboxv2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.InboxFilterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.ResoInboxAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.SortAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory.ResoInboxTypeFactory;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory.ResoInboxTypeFactoryImpl;
import com.tokopedia.inbox.rescenter.inboxv2.view.di.DaggerResoInboxComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.presenter.ResoInboxFragmentPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.EmptyInboxFilterDataModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterListViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SingleItemInboxResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SortModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFragment
        extends BaseDaggerFragment
        implements ResoInboxFragmentListener.View,
        SwipeToRefresh.OnRefreshListener{

    public static final String STATE_IS_SELLER = "is_seller";
    public static final String STATE_IS_CAN_LOAD_MORE = "can_load_more";
    public static final String STATE_LAST_CURSOR = "last_cursor";
    public static final String STATE_RESO_INBOX_SORT_FILTER = "inboxSortModel";
    public static final String STATE_RESO_INBOX_FILTER_MODEL = "inboxFilterModel";

    public static final int REQUEST_DETAIL_RESO = 1234;
    public static final int REQUEST_FILTER_RESO = 2345;
    private static final int SORT_DEFAULT_ID = 2;

    private ResoInboxAdapter adapter;
    private LinearLayoutManager rvInboxLayoutManager;
    private ResoInboxTypeFactory typeFactory;

    private RecyclerView rvInbox;
    private ProgressBar progressBar;
    private BottomActionView bottomActionView;
    private BottomSheetDialog sortDialog;
    private SwipeToRefresh swipeToRefresh;

    private boolean isSeller;
    private String lastCursor = "";
    private ResoInboxSortModel inboxSortModel;
    private ResoInboxFilterModel inboxFilterModel;

    @Inject
    ResoInboxFragmentPresenter presenter;


    public static Fragment getFragmentInstance(Bundle bundle) {
        Fragment fragment = new ResoInboxFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_SELLER, isSeller);
        outState.putString(STATE_LAST_CURSOR, lastCursor);
        outState.putParcelable(STATE_RESO_INBOX_FILTER_MODEL, inboxFilterModel);
        outState.putParcelable(STATE_RESO_INBOX_SORT_FILTER, inboxSortModel);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            isSeller = savedInstanceState.get(STATE_IS_SELLER) != null &&  savedInstanceState.getBoolean(STATE_IS_SELLER);
            lastCursor = savedInstanceState.getString(STATE_LAST_CURSOR);
            inboxFilterModel = savedInstanceState.getParcelable(STATE_RESO_INBOX_FILTER_MODEL);
            inboxSortModel = savedInstanceState.getParcelable(STATE_RESO_INBOX_SORT_FILTER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.fragment_reso_inbox, container, false);
        rvInbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        bottomActionView = (BottomActionView) view.findViewById(R.id.bav);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        typeFactory = new ResoInboxTypeFactoryImpl(this);
        adapter = new ResoInboxAdapter(typeFactory);
        rvInboxLayoutManager = new LinearLayoutManager(getActivity());
        rvInbox.setLayoutManager(rvInboxLayoutManager);
        isSeller = getArguments().getBoolean(ResoInboxActivity.PARAM_IS_SELLER);
        rvInbox.addOnScrollListener(rvInboxScrollListener);
        rvInbox.setAdapter(adapter);
        swipeToRefresh.setOnRefreshListener(this);
        initView();
        initViewListener();
    }

    private void initView() {
        bottomActionView.setVisibility(View.GONE);
        rvInbox.setVisibility(View.GONE);
        inboxFilterModel = new ResoInboxFilterModel();
        inboxSortModel = new ResoInboxSortModel(SortModel.getSortList(getActivity()), SORT_DEFAULT_ID, new SortModel());
        presenter.initPresenterData(getActivity(), isSeller);
    }

    private void initViewListener() {
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortButtonClicked();
            }
        });
        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterButtonClicked();
            }
        });
    }



    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerResoInboxComponent daggerCreateResoComponent =
                (DaggerResoInboxComponent) DaggerResoInboxComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    private RecyclerView.OnScrollListener rvInboxScrollListener
            = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = rvInboxLayoutManager.getChildCount();
            int totalItemCount = rvInboxLayoutManager.getItemCount();
            int firstVisiblesItems = rvInboxLayoutManager.findFirstVisibleItemPosition();
            if (adapter.isCanLoadMore() && (visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                adapter.showLoading();
                adapter.setCanLoadMore(false);
                presenter.loadMoreInbox(lastCursor);
            }
        }
    };

    @Override
    public void onRefresh() {
        adapter.clearData();
        resetParams();
        if (inboxFilterModel.getSelectedFilterList().size() != 0
                || !TextUtils.isEmpty(inboxFilterModel.getDateFromString())
                || !TextUtils.isEmpty(inboxFilterModel.getDateToString())) {
            presenter.getInboxWithParams(inboxSortModel, inboxFilterModel);
        } else {
            presenter.getInbox();
        }
    }

    private void sortButtonClicked() {
        sortDialog = new BottomSheetDialog(getActivity());
        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sortDialog.setContentView(R.layout.layout_bottomsheet_sort);
        RecyclerView rvSort = sortDialog.findViewById(R.id.rv_sort);
        rvSort.setLayoutManager(new LinearLayoutManager(getActivity()));
        SortAdapter adapter = new SortAdapter(getActivity(), this, inboxSortModel);
        rvSort.setAdapter(adapter);
        sortDialog.show();
    }

    private void filterButtonClicked() {
        startActivityForResult(
                InboxFilterActivity.newInstance(getActivity(), inboxFilterModel), REQUEST_FILTER_RESO);
        getBottomSheetActivityTransition();
    }

    private void getFirstInboxResult(InboxItemResultViewModel result) {
        dismissProgressBar();
        swipeToRefresh.setRefreshing(false);
        rvInbox.setVisibility(View.VISIBLE);
        bottomActionView.setVisibility(View.VISIBLE);
        adapter.clearData();
        adapter.addItem(result.getFilterListViewModel());
        adapter.addList(result.getInboxVisitableList());
        adapter.notifyDataSetChanged();

//        updateParams(true, result);
        updateParams(result.isCanLoadMore(), result);
    }

    private void updateFilterValue(InboxItemResultViewModel result) {
        inboxFilterModel.setFilterViewModelList(result.getFilterViewModels());
    }

    private void updateStringFilterValue(InboxItemResultViewModel result) {
        for (FilterViewModel oldModel : inboxFilterModel.getFilterViewModelList()) {
            for (FilterViewModel newModel : result.getFilterViewModels()) {
                if (oldModel.getOrderValue() == newModel.getOrderValue()) {
                    oldModel.setType(newModel.getType());
                    oldModel.setTypeNameDetail(newModel.getTypeNameDetail());
                    oldModel.setTypeNameQuickFilter(newModel.getTypeNameQuickFilter());
                    oldModel.setCount(newModel.getCount());
                }
            }
        }
        adapter.updateQuickFilter(new FilterListViewModel(inboxFilterModel.getFilterViewModelList()));
    }

    private void hideLayout() {
        bottomActionView.setVisibility(View.GONE);
    }

    private void showFullError(String err) {
        hideLayout();
        if (getActivity() != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), err, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.getInbox();
                }
            });
        }
    }

    private void showSnackbarError(String err) {
        NetworkErrorHelper.showSnackbar(getActivity(), err);
    }

    @Override
    public void onSuccessGetInbox(InboxItemResultViewModel result) {
        updateFilterValue(result);
        getFirstInboxResult(result);
    }

    @Override
    public void onErrorGetInbox(String err) {
        dismissProgressBar();
        showFullError(err);
    }

    @Override
    public void onEmptyGetInbox(InboxItemResultViewModel result) {
        dismissProgressBar();
        rvInbox.setVisibility(View.VISIBLE);
        bottomActionView.setVisibility(View.VISIBLE);
        adapter.setCanLoadMore(false);
        adapter.clearData();
        adapter.addItem(new EmptyModel());
        adapter.notifyDataSetChanged();
        bottomActionView.setVisibility(View.GONE);
        updateFilterValue(result);
    }

    @Override
    public void onSuccessGetInboxWithFilter(InboxItemResultViewModel result) {
        getFirstInboxResult(result);
    }

    @Override
    public void onErrorGetInboxWithFilter(String err) {
        dismissProgressBar();
        showFullError(err);
    }

    @Override
    public void onEmptyGetInboxWithFilter(InboxItemResultViewModel result) {
        dismissProgressBar();
        rvInbox.setVisibility(View.VISIBLE);
        adapter.setCanLoadMore(false);
        adapter.clearData();
        adapter.addItem(result.getFilterListViewModel());
        adapter.addItem(new EmptyInboxFilterDataModel());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeLoadingItem() {
        adapter.removeLoading();
    }

    @Override
    public void onSuccessLoadMoreInbox(InboxItemResultViewModel result) {
        adapter.removeLoading();
        adapter.addList(result.getInboxVisitableList());
//        updateParams(true, result);
        updateParams(result.isCanLoadMore(), result);
        updateStringFilterValue(result);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorLoadMoreInbox(String err) {
        adapter.removeLoading();
        showSnackbarError(err);
        resetParams();
    }

    @Override
    public void onSuccessGetSingleInboxItem(SingleItemInboxResultViewModel model) {
        dismissProgressBar();
        adapter.updateQuickFilter(model.getFilterListViewModel());
        adapter.updateSingleInboxItem(model.getInboxItemViewModel());
    }

    @Override
    public void onErrorGetSingleInboxItem(String err) {
        dismissProgressBar();
        showSnackbarError(err);
    }

    @Override
    public void onSortItemClicked(SortModel sortModel) {
        this.inboxSortModel.setSelectedSortId(sortModel.sortId);
        this.inboxSortModel.setSelectedSortModel(sortModel);
        sortDialog.dismiss();
        getInboxWithParams(inboxFilterModel);
    }

    @Override
    public void getInboxWithParams(ResoInboxFilterModel inboxFilterModel) {
        showProgressBar();
        this.inboxFilterModel = inboxFilterModel;
        presenter.getInboxWithParams(inboxSortModel, inboxFilterModel);
    }

    @Override
    public void onResetFilterButtonClicked() {
        inboxFilterModel.setSelectedFilterList(new ArrayList<Integer>());
        inboxFilterModel.setDateToString("");
        inboxFilterModel.setDateFromString("");
        inboxFilterModel.setDateTo(null);
        inboxFilterModel.setDateFrom(null);
        inboxSortModel = new ResoInboxSortModel(SortModel.getSortList(getActivity()), SORT_DEFAULT_ID, new SortModel());
        presenter.getInboxResetFilter();
    }

    @Override
    public void onItemClicked(int resolutionId, String sellerName, String customerName) {
        Intent intent;
        if (isSeller) {
            intent = DetailResChatActivity.newSellerInstance(
                    getActivity(),
                    String.valueOf(resolutionId),
                    customerName);
        } else {
            intent = DetailResChatActivity.newBuyerInstance(
                    getActivity(),
                    String.valueOf(resolutionId),
                    sellerName);
        }
        startActivityForResult(intent, REQUEST_DETAIL_RESO);
    }

    private void showErrorWithRetry(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getInbox();
            }
        });
    }

    @Override
    public void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSwipeToRefresh() {
        if (!swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(true);
        }
    }

    @Override
    public void dismissSwipeToRefresh() {
        if (swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(false);
        }
    }

    @Override
    public ResoInboxFilterModel getInboxFilterModel() {
        return inboxFilterModel;
    }

    @Override
    public ResoInboxSortModel getInboxSortModel() {
        return inboxSortModel;
    }

    private void updateParams(boolean isCanLoadMore, InboxItemResultViewModel resultViewModel) {
        adapter.setCanLoadMore(isCanLoadMore);
//        adapter.setCanLoadMore(resultViewModel.isCanLoadMore());
        this.lastCursor = String.valueOf(resultViewModel.getInboxItemViewModels()
                .get(resultViewModel.getInboxItemViewModels().size() - 1).getId());
    }

    private void resetParams() {
        adapter.setCanLoadMore(false);
        this.lastCursor = "";
    }

    public void getBottomSheetActivityTransition() {
        getActivity().overridePendingTransition(R.anim.pull_up, R.anim.push_down);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL_RESO) {
            if (data != null) {
                String resoId = data.getStringExtra(DetailResChatActivity.PARAM_RESOLUTION_ID);
                if (!TextUtils.isEmpty(resoId)) {
                    presenter.getSingleItemInbox(Integer.valueOf(resoId));
                }
            }
        } else if (requestCode == REQUEST_FILTER_RESO) {
            if (resultCode == Activity.RESULT_OK) {
                inboxFilterModel = data.getParcelableExtra(InboxFilterActivity.PARAM_FILTER_MODEL);
                getInboxWithParams(inboxFilterModel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
