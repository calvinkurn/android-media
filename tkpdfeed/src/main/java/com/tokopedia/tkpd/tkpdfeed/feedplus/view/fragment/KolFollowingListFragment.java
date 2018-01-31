package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.KolFollowingListActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.KolProfileWebViewActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.KolFollowingAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.KolFollowingListPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingResultViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import javax.inject.Inject;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListFragment extends BaseDaggerFragment
        implements KolFollowingList.View {

    private RecyclerView rvItem;
    private ProgressBar progressBar;

    private boolean isCanLoadMore;
    private String cursor;
    private String emptyApplink;
    private int userId;

    private KolFollowingAdapter adapter;
    private LinearLayoutManager layoutManager;
    private View emptyState;
    private Button emptyButton;

    @Inject
    KolFollowingListPresenter presenter;

    public static KolFollowingListFragment createInstance(Bundle bundle) {
        KolFollowingListFragment fragment = new KolFollowingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(KolFollowingListActivity.ARGS_USER_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle state) {
        super.onViewStateRestored(state);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_kol_following_list, container, false);
        rvItem = (RecyclerView) parentView.findViewById(R.id.rv_item);
        progressBar = (ProgressBar) parentView.findViewById(R.id.progress_bar);
        emptyState = parentView.findViewById(R.id.view_empty_state);
        emptyButton = (Button) parentView.findViewById(R.id.btn_empty);
        presenter.attachView(this);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewListener();
    }

    private void initView() {
        rvItem.setVisibility(View.GONE);
        emptyState.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new KolFollowingAdapter(getActivity(), this);
        rvItem.setLayoutManager(layoutManager);
        rvItem.setHasFixedSize(true);
        rvItem.setAdapter(adapter);
        rvItem.addOnScrollListener(getRVListener());
        showLoading();
        presenter.getKolFollowingList(userId);
    }

    private void initViewListener() {
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emptyApplink)) {
                    ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity()
                            , emptyApplink);
                }
            }
        });
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerFeedPlusComponent daggerFeedPlusComponent =
                (DaggerFeedPlusComponent) DaggerFeedPlusComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerFeedPlusComponent.inject(this);

    }

    private RecyclerView.OnScrollListener getRVListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                if (isCanLoadMore && (visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                    adapter.addBottomLoading();
                    isCanLoadMore = false;
                    presenter.getKolLoadMore(userId, cursor);
                }
            }
        };
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetKolFollowingList(KolFollowingResultViewModel viewModel) {
        rvItem.setVisibility(View.VISIBLE);
        updateView(viewModel);
        updateParams(viewModel);
    }

    @Override
    public void onSuccessGetKolFollowingListEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
    }

    private void updateView(KolFollowingResultViewModel model) {
        adapter.setItemList(model.getKolFollowingViewModelList());
        if (model.getKolFollowingViewModelList() == null || model.getKolFollowingViewModelList().size() == 0) {
            emptyButton.setText(model.getButtonText());
            emptyApplink = model.getButtonApplink();
        }
    }

    private void updateParams(KolFollowingResultViewModel viewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore();
        this.cursor = viewModel.getLastCursor();
    }

    @Override
    public void onErrorGetKolFollowingList(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initView();
            }
        });
    }

    @Override
    public void onSuccessLoadMoreKolFollowingList(KolFollowingResultViewModel itemList) {
        adapter.removeBottomLoading();
        adapter.getItemList().addAll(itemList.getKolFollowingViewModelList());
        adapter.notifyDataSetChanged();
        updateParams(itemList);
    }

    @Override
    public void onErrorLoadMoreKolFollowingList(String error) {
        adapter.removeBottomLoading();
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void onListItemClicked(KolFollowingViewModel item) {
        String url = item.getProfileApplink();
        if (!TextUtils.isEmpty(url)) {
            ((TkpdCoreRouter) getActivity().getApplication()).actionApplinkFromActivity(getActivity()
                    , url);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
