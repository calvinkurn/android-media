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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.multiple.view.QuickMultipleFilterView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.InboxFilterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.ResoInboxAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.SortAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.di.DaggerResoInboxComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.presenter.ResoInboxFragmentPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SortModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFragment extends BaseDaggerFragment implements ResoInboxFragmentListener.View {

    public static final String STATE_IS_SELLER = "is_seller";
    public static final String STATE_IS_CAN_LOAD_MORE = "can_load_more";
    public static final String STATE_LAST_CURSOR = "last_cursor";
    public static final String STATE_RESO_INBOX_SORT_FILTER = "inboxSortModel";
    public static final String STATE_RESO_INBOX_FILTER_MODEL = "inboxFilterModel";

    public static final int REQUEST_DETAIL_RESO = 1234;
    public static final int REQUEST_FILTER_RESO = 2345;
    private static final int SORT_DEFAULT_ID = 2;

    private ResoInboxAdapter inboxAdapter;
    private LinearLayoutManager rvInboxLayoutManager;

    private RecyclerView rvInbox;
    private QuickMultipleFilterView quickFilterView;
    private ProgressBar progressBar;
    private BottomActionView bottomActionView;
    private BottomSheetDialog sortDialog;
    private FrameLayout ffEmptyState, ffEmptyStateWithReset;
    private Button btnResetFilter;

    private boolean isSeller;
    private boolean isCanLoadMore;
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
        outState.putBoolean(STATE_IS_CAN_LOAD_MORE, isCanLoadMore);
        outState.putString(STATE_LAST_CURSOR, lastCursor);
        outState.putParcelable(STATE_RESO_INBOX_FILTER_MODEL, inboxFilterModel);
        outState.putParcelable(STATE_RESO_INBOX_SORT_FILTER, inboxSortModel);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            isSeller = savedInstanceState.get(STATE_IS_SELLER) != null &&  savedInstanceState.getBoolean(STATE_IS_SELLER);
            isCanLoadMore = savedInstanceState.get(STATE_IS_CAN_LOAD_MORE) != null &&  savedInstanceState.getBoolean(STATE_IS_CAN_LOAD_MORE);
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
        quickFilterView = (QuickMultipleFilterView) view.findViewById(R.id.view_quick_filter);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        bottomActionView = (BottomActionView) view.findViewById(R.id.bav);
        ffEmptyState = (FrameLayout) view.findViewById(R.id.view_empty_state);
        ffEmptyStateWithReset = (FrameLayout) view.findViewById(R.id.view_empty_state_with_reset);
        btnResetFilter = (Button) view.findViewById(R.id.btn_reset_filter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        rvInboxLayoutManager = new LinearLayoutManager(getActivity());
        rvInbox.setLayoutManager(rvInboxLayoutManager);
        isSeller = getArguments().getBoolean(ResoInboxActivity.PARAM_IS_SELLER);
        rvInbox.addOnScrollListener(rvInboxScrollListener);
        initView();
        initViewListener();
    }

    private void initView() {
        bottomActionView.setVisibility(View.GONE);
        rvInbox.setVisibility(View.GONE);
        quickFilterView.setVisibility(View.GONE);
        ffEmptyStateWithReset.setVisibility(View.GONE);
        ffEmptyState.setVisibility(View.GONE);
        inboxFilterModel = new ResoInboxFilterModel();
        inboxSortModel = new ResoInboxSortModel(SortModel.getSortList(), SORT_DEFAULT_ID, new SortModel());
        quickFilterView.setListener(quickFilterListener);
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
        btnResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inboxFilterModel = new ResoInboxFilterModel();
                initView();
            }
        });
    }

    private QuickMultipleFilterView.ActionListener quickFilterListener
            = new QuickMultipleFilterView.ActionListener() {
        @Override
        public void filterClicked(List<Integer> selectedIdList) {
            inboxFilterModel.setSelectedFilterList(selectedIdList);
            showProgressBar();
            getInboxWithParams();
        }
    };

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
            if (isCanLoadMore && (visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                inboxAdapter.addLoadingItem();
                isCanLoadMore = false;
                presenter.loadMoreInbox(lastCursor);
            }
        }
    };

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
        rvInbox.setVisibility(View.VISIBLE);
        quickFilterView.setVisibility(View.VISIBLE);
        bottomActionView.setVisibility(View.VISIBLE);
        inboxAdapter = new ResoInboxAdapter(
                getActivity(),
                this,
                result.getInboxItemViewModels());
        rvInbox.setAdapter(inboxAdapter);
        inboxAdapter.notifyDataSetChanged();

        quickFilterView.renderFilter(convertQuickFilterModel(inboxFilterModel.getFilterViewModelList()));
        updateParams(true, result);
    }

    private List<QuickFilterItem> convertQuickFilterModel(List<FilterViewModel> filterList) {
        List<QuickFilterItem> itemList = new ArrayList<>();
        for (FilterViewModel filter : filterList) {
            QuickFilterItem item = new QuickFilterItem();
            item.setId(filter.getOrderValue());
            item.setName(filter.getTypeNameQuickFilter());
            item.setColorBorder(R.color.tkpd_main_green);
            item.setType(filter.getType());
            item.setSelected(inboxFilterModel.getSelectedFilterList().contains(filter.getOrderValue()));
            itemList.add(item);
        }
        return itemList;
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
        quickFilterView.renderFilter(convertQuickFilterModel(inboxFilterModel.getFilterViewModelList()));
    }

    private void hideLayout() {
        bottomActionView.setVisibility(View.GONE);
        quickFilterView.setVisibility(View.GONE);
        rvInbox.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        hideLayout();
        ffEmptyState.setVisibility(View.VISIBLE);
    }

    private void showEmptyStateWithResetFilter() {
        hideLayout();
        quickFilterView.setVisibility(View.VISIBLE);
        quickFilterView.setVisibility(View.VISIBLE);
        ffEmptyStateWithReset.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetInbox(InboxItemResultViewModel result) {
        updateFilterValue(result);
        getFirstInboxResult(result);
    }

    @Override
    public void onErrorGetInbox(String err) {
        dismissProgressBar();
        showEmptyState();
        showErrorWithRetry(err);
    }

    @Override
    public void onSuccessGetInboxWithFilter(InboxItemResultViewModel result) {
        getFirstInboxResult(result);
    }

    @Override
    public void onErrorGetInboxWithFilter(String err) {
        dismissProgressBar();
        showEmptyStateWithResetFilter();
    }

    @Override
    public void onSuccessLoadMoreInbox(InboxItemResultViewModel result) {
        inboxAdapter.removeLoadingItem();
        inboxAdapter.addMoreItem(result.getInboxItemViewModels());
        updateParams(true, result);
        updateStringFilterValue(result);
    }

    @Override
    public void onErrorLoadMoreInbox(String err) {
        inboxAdapter.removeLoadingItem();
        resetParams();
    }

    @Override
    public void onSuccessGetSingleInboxItem(InboxItemViewModel model) {
        dismissProgressBar();
        inboxAdapter.updateSingleItem(model);
    }

    @Override
    public void onErrorGetSingleInboxItem(String err) {
        dismissProgressBar();
        NetworkErrorHelper.showSnackbar(getActivity(), err);
    }

    @Override
    public void onSortItemClicked(SortModel sortModel) {
        this.inboxSortModel.setSelectedSortId(sortModel.sortId);
        this.inboxSortModel.setSelectedSortModel(sortModel);
        sortDialog.dismiss();
        getInboxWithParams();
    }

    private void getInboxWithParams() {
        presenter.getInboxWithParams(inboxSortModel, inboxFilterModel);
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


    private void updateParams(boolean isCanLoadMore, InboxItemResultViewModel resultViewModel) {
        this.isCanLoadMore = isCanLoadMore;
        this.lastCursor = String.valueOf(resultViewModel.getInboxItemViewModels()
                .get(resultViewModel.getInboxItemViewModels().size() - 1).getId());
    }

    private void resetParams() {
        this.isCanLoadMore = false;
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
                getInboxWithParams();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
