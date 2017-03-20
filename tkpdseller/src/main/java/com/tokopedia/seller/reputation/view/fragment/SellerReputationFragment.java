package com.tokopedia.seller.reputation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.reputation.data.mapper.ReputationReviewMapper;
import com.tokopedia.seller.reputation.data.repository.ReputationReviewRepositoryImpl;
import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.seller.reputation.domain.interactor.ReviewReputationMergeUseCase;
import com.tokopedia.seller.reputation.domain.interactor.ReviewReputationUseCase;
import com.tokopedia.seller.reputation.domain.interactor.ShopInfoUseCase;
import com.tokopedia.seller.reputation.network.apiservice.SellerReputationService;
import com.tokopedia.seller.reputation.presenter.SellerReputationFragmentPresenter;
import com.tokopedia.seller.reputation.view.SellerReputationView;
import com.tokopedia.seller.reputation.view.adapter.SellerReputationAdapter;
import com.tokopedia.seller.reputation.view.helper.ReputationViewHelper;
import com.tokopedia.seller.reputation.view.listener.SellerReputationInterface;
import com.tokopedia.seller.topads.exception.AddProductListException;
import com.tokopedia.seller.topads.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.topads.utils.TopAdsNetworkErrorHelper;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsWhiteRetryDataBinder;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsAddProductListPresenter;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa
 */
public class SellerReputationFragment extends BasePresenterFragment<SellerReputationFragmentPresenter>
        implements SellerReputationView, RetryDataBinder.OnRetryListener, DefaultErrorSubscriber.ErrorNetworkListener {

    public static final String TAG = "SellerReputationFragmen";

    RecyclerView listViewBalance;

    View mainView;

    RelativeLayout topSlideOffBar;

    SellerReputationAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;

    SwipeToRefresh swipeToRefresh;
    @Inject
    SessionHandler sessionHandler;
    @Inject
    ReviewReputationUseCase reviewReputationUseCase;
    @Inject
    GCMHandler gcmHandler;
    @Inject
    ReviewReputationMergeUseCase reviewReputationMergeUseCase;
    private TopAdsNetworkErrorHelper gmNetworkErrorHelper;
    private View rootView;
    private SellerReputationInterface sellerReputationInterface;
    private AppComponent baseApplication;
    private ReputationViewHelper reputationViewHelper;
    private boolean isFirstTime = true;
    private boolean isEndOfFile = true;
    private RecyclerView.OnScrollListener onScrollListener;

    public static SellerReputationFragment createInstance() {
        SellerReputationFragment fragment = new SellerReputationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        setActionsEnabled(false);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new SellerReputationFragmentPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity != null && activity instanceof SellerReputationInterface) {
            sellerReputationInterface = (SellerReputationInterface) activity;
        } else {
            throw new AddProductListException();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_seller_reputation;
    }

    @Override
    protected void initView(View view) {
        listViewBalance = (RecyclerView) view.findViewById(R.id.balance_list);
        mainView = view.findViewById(R.id.main_view);
        topSlideOffBar = (RelativeLayout) view.findViewById(R.id.seller_reputation_header);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        this.rootView = view;
        this.refreshHandler = new RefreshHandler(swipeToRefresh, onRefresh());
    }

    @Override
    public void onResume() {
        super.onResume();
        inject();
        presenter.setSessionHandler(sessionHandler);
        presenter.setReviewReputationUseCase(reviewReputationUseCase);
        presenter.setGcmHandler(gcmHandler);
        presenter.setErrorNetworkListener(this);
        presenter.setReviewReputationMergeUseCase(reviewReputationMergeUseCase);
        gmNetworkErrorHelper = new TopAdsNetworkErrorHelper(null, rootView);
        reputationViewHelper = new ReputationViewHelper(topSlideOffBar);
        setupRecyclerView();
        fetchData();
    }

    private void fetchData() {
        if (presenter.getNetworkStatus()
                == TopAdsAddProductListPresenter.NetworkStatus.ONACTIVITYFORRESULT) {
            refreshHandler.setRefreshing(true);
            firstTimeNetworkCall();
        } else {
            presenter.setNetworkStatus(
                    TopAdsAddProductListPresenter.NetworkStatus.PULLTOREFRESH);
            if (presenter.isFirstTime()) {
                adapter.showLoadingFull(true);
                swipeToRefresh.setEnabled(false);
                firstTimeNetworkCall();
            }
        }
    }

    private void setupRecyclerView() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isEndOfFile) {
                    return;
                }

                if (presenter.isHitNetwork())
                    return;

                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem
                        && adapter.getDataSize() < Integer.MAX_VALUE) {
                    presenter.incrementPage();
                    presenter.setNetworkStatus(TopAdsAddProductListPresenter.NetworkStatus.LOADMORE);

                }
//                presenter.loadMore(lastItemPosition, visibleItem);
            }
        };
    }

    private void inject() {
        //[START] This is for dependent component
        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        Gson gson = new GsonBuilder().create();
        ShopService shopService =
                new ShopService();
        ShopNetworkController shopNetworkController = new ShopNetworkController(
                getActivity(), shopService, gson
        );


        SellerReputationService sellerReputationService =
                new SellerReputationService();
        ReputationReviewMapper reputationReviewMapper =
                new ReputationReviewMapper();
        CloudReputationReviewDataSource cloudReputationReviewDataSource =
                new CloudReputationReviewDataSource(
                        getActivity(), sellerReputationService.getApi(), reputationReviewMapper
                );
        ReputationReviewRepository reputationReviewRepository
                = new ReputationReviewRepositoryImpl(
                cloudReputationReviewDataSource,
                shopNetworkController
        );

        ShopInfoUseCase shopInfoUseCase = new ShopInfoUseCase(threadExecutor, postExecutionThread, reputationReviewRepository);
        sessionHandler = new SessionHandler(getActivity());
        reviewReputationUseCase = new ReviewReputationUseCase(threadExecutor, postExecutionThread, reputationReviewRepository);
        gcmHandler = new GCMHandler(getActivity());

        reviewReputationMergeUseCase = new ReviewReputationMergeUseCase(
                threadExecutor, postExecutionThread, reviewReputationUseCase, shopInfoUseCase
        );
        //[END] This is for dependent component
    }

    @Override
    public void onPause() {
        super.onPause();
        gmNetworkErrorHelper.onPause();
        listViewBalance.removeOnScrollListener(onScrollListener);
    }

    @Override
    public void dismissSnackbar() {
        gmNetworkErrorHelper.dismissSnackbar();
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                dismissSnackbar();

                if (adapter.getDataSize() > 0) {

                } else {
                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    adapter.showLoadingFull(true);
                }

                presenter.resetPage();
                presenter.setNetworkStatus(
                        TopAdsAddProductListPresenter.NetworkStatus.PULLTOREFRESH);
                firstTimeNetworkCall();
            }
        };
    }

    private void firstTimeNetworkCall() {
        presenter.firstTimeNetworkCall2();
    }

    private void loadMoreCall() {
        presenter.loadMoreNetworkCall();
    }

    @Override
    protected void setViewListener() {
    }

    private View.OnClickListener onSearchClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.onSearchClicked();
            }
        };
    }

    private View.OnClickListener onEndDateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.onEndDateClicked(datePicker);
            }
        };
    }

    private View.OnClickListener onStartDateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.onStartDateClicked(datePicker);
            }
        };
    }

    @Override
    protected void initialVar() {
        adapter = SellerReputationAdapter.createInstance(getActivity());
        TopAdsWhiteRetryDataBinder topAdsRetryDataBinder = new TopAdsWhiteRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                dismissSnackbar();

                if (adapter.getDataSize() > 0) {

                } else {
                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    adapter.showLoadingFull(true);
                    swipeToRefresh.setEnabled(false);
                }

                presenter.resetPage();
                presenter.setNetworkStatus(
                        TopAdsAddProductListPresenter.NetworkStatus.RETRYNETWORKCALL);
                firstTimeNetworkCall();
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
        adapter.setOnRetryListenerRV(this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listViewBalance.setLayoutManager(linearLayoutManager);
        listViewBalance.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public String getStartDate() {
        return null;
    }

    @Override
    public void setStartDate(String date) {

    }

    @Override
    public void setLoadMoreFlag(boolean loadmoreflag) {
        isEndOfFile = loadmoreflag;
    }

    @Override
    public void loadData(List<TypeBasedModel> datas) {
        renderDatas(datas);
    }

    @Override
    public void loadMore(List<TypeBasedModel> datas) {
        renderDatas(datas);
    }

    @Override
    public void loadShopInfo(ShopModel shopModel) {
        reputationViewHelper.renderData(shopModel);
    }

    private void renderDatas(List<TypeBasedModel> datas) {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
        }

        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);

        switch (presenter.getNetworkStatus()) {
            case LOADMORE:
                break;
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                adapter.clear();
                break;
            case RETRYNETWORKCALL:
                if (adapter.getDataSize() <= 0) {
                    adapter.clear();
                }
                break;
        }
        adapter.addAllWithoutNotify(datas);
        boolean isEmpty = adapter.getDataSize() <= 0;
        if (isEmpty) {
            if (!isEndOfFile) {
                adapter.showLoading(true);
            } else {
                adapter.showEmptyFull(true);
            }
        } else {
            if (!isEndOfFile) {
                adapter.showLoading(true);
            } else {
                adapter.showLoading(false);
            }
            adapter.notifyDataSetChanged();
        }
        swipeToRefresh.setEnabled(true);
    }

    @Override
    public String getEndDate() {
        return null;
    }

    @Override
    public void setEndDate(String date) {

    }

    @Override
    public void finishLoading() {
        adapter.showLoading(false);
        adapter.showEmpty(false);
        refreshHandler.setPullEnabled(true);
        refreshHandler.setRefreshing(false);

    }

    @Override
    public SellerReputationAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void showErrorMessage(final String errorMessage) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {

//                    switch (topAdsAddProductListPresenter.getNetworkStatus()) {
//                        case SEARCHVIEW:
//                            topAdsProductListAdapter.clear();
//                            topAdsProductListAdapter.showEmptyFull(true);
//                            break;
//                    }

                    gmNetworkErrorHelper.showSnackbar(errorMessage, "COBA KEMBALI", new ActionClickListener() {
                        @Override
                        public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                            Toast.makeText(
                                    SellerReputationFragment.this.getActivity(),
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                            ).show();

                            dismissSnackbar();

                            refreshHandler.setRefreshing(true);

//                            topAdsAddProductListPresenter.setNetworkStatus(
//                                    TopAdsAddProductListPresenter.NetworkStatus.RETRYNETWORKCALL);
//                            loadMoreNetworkCall();
                        }
                    }, getActivity());
                }
            }
        }, 100);

    }

    @Override
    public void removeError() {
        dismissSnackbar();
        adapter.showEmpty(false);

    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
    }

    @Override
    public boolean isRefreshing() {
        return refreshHandler.isRefreshing();
    }

    @Override
    public void refresh() {
//        presenter.onRefresh();
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void showEmptyState() {
//        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
//            @Override
//            public void onRetryClicked() {
//                presenter.getSummaryReputation();
//            }
//        });
//        try {
//            View retryLoad = getView().findViewById(R.id.main_retry);
//            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void setRetry() {
//        setActionsEnabled(false);
//        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
//            @Override
//            public void onRetryClicked() {
//                presenter.getSummaryReputation();
//            }
//        }).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
//        setActionsEnabled(false);
//        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
//            @Override
//            public void onRetryClicked() {
//                presenter.getSummaryReputation();
//            }
//        });
//        try {
//            View retryLoad = getView().findViewById(R.id.main_retry);
//            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        showErrorMessage(error);
    }

    @Override
    public void onRetryCliked() {

    }

    @Override
    public void showMessageError(String errorMessage) {
        showErrorMessage(errorMessage);
    }
}
