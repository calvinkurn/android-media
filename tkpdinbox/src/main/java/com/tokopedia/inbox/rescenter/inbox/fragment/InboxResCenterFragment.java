package com.tokopedia.inbox.rescenter.inbox.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.RefreshHandler.OnRefreshHandlerListener;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inbox.adapter.ResCenterInboxAdapter;
import com.tokopedia.inbox.rescenter.inbox.customviews.EndLessScrollBehavior;
import com.tokopedia.inbox.rescenter.inbox.dialog.BottomSheetFilterDialog;
import com.tokopedia.inbox.rescenter.inbox.listener.InboxResCenterView;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxItem;
import com.tokopedia.inbox.rescenter.inbox.model.ResolutionList;
import com.tokopedia.inbox.rescenter.inbox.presenter.InboxResCenterImpl;
import com.tokopedia.inbox.rescenter.inbox.presenter.InboxResCenterPresenter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created on 3/23/16.
 */
public class InboxResCenterFragment extends BasePresenterFragment<InboxResCenterPresenter>
        implements InboxResCenterView {

    private static final String TAG = InboxResCenterFragment.class.getSimpleName();
    private static final String ARG_PARAM_INBOX_TAB = "ARG_PARAM_INBOX_TAB";
    private static final String ARG_PARAM_INBOX_LIST = "ARG_PARAM_INBOX_LIST";

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R2.id.fab)
    FloatingActionButton fab;

    private View rootView;
    private ArrayList<ResCenterInboxItem> list;
    private ResCenterInboxAdapter adapter;
    private RefreshHandler refreshHandler;
    private LinearLayoutManager layoutManager;
    private BottomSheetFilterDialog dialog;
    private InboxResCenterActivity.Model resCenterTabModel;
    private SnackbarRetry snackbarRetry;

    public static Fragment createInstance(Context context, int resolutionState) {
        if (resolutionState == TkpdState.InboxResCenter.RESO_MINE) {
            return createFragment(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_MINE, context.getString(R.string.title_my_dispute)));
        } else {
            return createFragment(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_BUYER, context.getString(R.string.title_buyer_dispute)));
        }
    }

    public static InboxResCenterFragment createFragment(InboxResCenterActivity.Model model) {
        InboxResCenterFragment fragment = new InboxResCenterFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_INBOX_TAB, model);
        fragment.setArguments(args);
        return fragment;
    }

    public InboxResCenterFragment() {
        // empty constructor need
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        Log.d(TAG + "hang" + resCenterTabModel.typeFragment, "on First Time Launch");
        if (getUserVisibleHint()) {
            presenter.setActionOnFirstTimeLaunch(getActivity());
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.setOnFragmentSavingState(state);
    }

    @Override
    public void saveCurrentInboxList(Bundle state) {
        state.putParcelableArrayList(ARG_PARAM_INBOX_LIST, list);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.setOnFragmentRestoringState(savedState);
    }

    @Override
    public void restorePreviousInboxList(Bundle savedState) {
        list = savedState.getParcelableArrayList(ARG_PARAM_INBOX_LIST);
        adapter.setList(list);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && snackbarRetry != null) {
            snackbarRetry.resumeRetrySnackbar();
        } else {
            if (!isVisibleToUser && snackbarRetry != null) snackbarRetry.pauseRetrySnackbar();
        }
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null && isVisibleToUser) {
            Log.d(TAG + "hang" + resCenterTabModel.typeFragment, "setuservisiblehint");
            presenter.setActionOnRefreshing(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG + "hang" + resCenterTabModel.typeFragment, "resuming fragment");
        presenter.setOnFragmentResuming(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG + "hang" + resCenterTabModel.typeFragment, "destroyview fragment");
        presenter.setOnFragmentDestroyView(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG + "hang" + resCenterTabModel.typeFragment, "destroy fragment");
        dialog.setOnDestroy();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxResCenterImpl(this, resCenterTabModel);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        resCenterTabModel = arguments.getParcelable(ARG_PARAM_INBOX_TAB);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_resolution_v2;
    }

    @Override
    protected void initView(View view) {
        this.setRootView(view);
        this.prepareRecyclerView();
        this.prepareBottomSheet();
    }

    @Override
    public void prepareRecyclerView() {
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void prepareBottomSheet() {
        dialog = BottomSheetFilterDialog.Builder(getActivity(), String.valueOf(resCenterTabModel.typeFragment), resCenterTabModel.titleFragment);
        dialog.setListener(presenter);
        dialog.setView();
    }

    @Override
    protected void setViewListener() {
        this.recyclerView.addOnScrollListener(new EndLessScrollBehavior(layoutManager) {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            protected void setOnLoadMore() {
                Log.d(TAG + "hang" + resCenterTabModel.typeFragment, "load more");
                presenter.setActionOnLazyLoad(getActivity());
            }
        });

        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setActionOnFABClicked(view);
            }
        });
    }

    @Override
    public void setBottomSheetDialog() {
        dialog.show();
    }

    @Override
    protected void initialVar() {
        this.refreshHandler =
                new RefreshHandler(getActivity(), getRootView(), new OnRefreshHandlerListener() {
                    @Override
                    public void onRefresh(View view) {
                        presenter.setActionOnRefreshing(getActivity());
                    }
                });
        this.list = new ArrayList<>();
        this.adapter = new ResCenterInboxAdapter(list, presenter);
    }

    @Override
    protected void setActionVar() {
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void addLoading() {
        this.adapter.setLoading(true);
    }

    @Override
    public void removeLoading() {
        this.adapter.setLoading(false);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.refreshHandler.setRefreshing(refreshing);
    }

    @Override
    public void addInboxItem(ArrayList<ResolutionList> newData) {
        int positionStart = list.size();
        int itemCount = newData.size();

        this.list.addAll(newData);
        this.adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void addHeaderItem(ResCenterInboxItem headerItem) {
        if (this.list.size() == 0) {
            this.list.add(headerItem);
            this.adapter.setList(list);
        } else if (this.list.get(0).getItemType() != ResCenterInboxItem.TYPE_HEADER) {
            this.list.add(0, headerItem);
            this.adapter.setList(list);
        } else {
            this.list.set(0, headerItem);
            this.adapter.setList(list);
        }
    }

    @Override
    public void removeHeaderItem() {
        if (this.list.get(0).getItemType() == ResCenterInboxItem.TYPE_HEADER) {
            this.list.remove(0);
            this.adapter.setList(list);
        }
    }

    @Override
    public void addNoResult() {
        this.adapter.setNoResult(true);
    }

    @Override
    public void removeNoResult() {
        this.adapter.setNoResult(false);
    }

    @Override
    public void addRetryItemOnHeader() {
        this.adapter.addRetryOnHeader();
    }

    @Override
    public void addRetryItemOnBottom() {
        this.adapter.addRetryOnBottom();
    }

    @Override
    public void setRetryView() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void removeRetryItem() {
        this.adapter.removeRetry();
    }

    @Override
    public void showCache(ArrayList<ResolutionList> cacheList) {
        this.clearCurrentList();
        this.list.addAll(cacheList);
        this.adapter.setList(list);
    }

    @Override
    public void replaceCache(ArrayList<ResolutionList> newList) {
        this.clearCurrentList();
        this.list.addAll(newList);
        this.adapter.setList(list);
    }

    @Override
    public void clearCurrentList() {
        this.list.clear();
    }

    @Override
    public void setPullToRefreshAble(boolean isAble) {
        refreshHandler.setPullEnabled(isAble);
    }

    @Override
    public void setFilterAble(boolean isFilterAble) {
        if (isFilterAble)
            fab.show();
        else
            fab.hide();

        fab.setClickable(isFilterAble);
        /**
         * if you need filter to be hidden
         * using animation up and down
         * then
         * uncomment lines of code below
         *
         if (isFilterAble) {
         fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
         } else {
         CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
         int marginBottomFAB = layoutParams.bottomMargin;
         fab.animate().translationY(fab.getHeight() + marginBottomFAB).setInterpolator(new LinearInterpolator()).start();
         }
         */
    }

    @Override
    public void showMessageErrorEmptyState(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getRootView(), message, null);
    }

    @Override
    public ArrayList<ResCenterInboxItem> getList() {
        return list;
    }

    @Override
    public void showMessageErrorSnackBar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showTimeOutEmtpyState(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getRootView(), retryClickedListener);
    }
}
