package com.tokopedia.seller.reputation.view.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.tokopedia.seller.lib.datepicker.DatePickerResultListener;
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
import com.tokopedia.seller.reputation.view.activity.SellerReputationInfoActivity;
import com.tokopedia.seller.reputation.view.adapter.SellerReputationAdapter;
import com.tokopedia.seller.reputation.view.helper.ReputationViewHelper;
import com.tokopedia.seller.reputation.view.model.SetDateHeaderModel;
import com.tokopedia.seller.topads.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.topads.utils.TopAdsNetworkErrorHelper;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsWhiteRetryDataBinder;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsAddProductListPresenter;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.List;

/**
 * @author normansyahputa
 */
public class SellerReputationFragment extends BasePresenterFragment<SellerReputationFragmentPresenter>
        implements SellerReputationView, RetryDataBinder.OnRetryListener, DefaultErrorSubscriber.ErrorNetworkListener, DatePickerResultListener.DatePickerResult {

    public static final String TAG = "SellerReputationFragmen";

    RecyclerView listViewBalance;

    View mainView;

    RelativeLayout topSlideOffBar;

    SellerReputationAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;

    SwipeToRefresh swipeToRefresh;
    SessionHandler sessionHandler;
    ReviewReputationUseCase reviewReputationUseCase;
    GCMHandler gcmHandler;
    ReviewReputationMergeUseCase reviewReputationMergeUseCase;

    private TopAdsNetworkErrorHelper gmNetworkErrorHelper;
    private View rootView;
    private AppComponent baseApplication;
    private ReputationViewHelper reputationViewHelper;
    private boolean isFirstTime = true;
    private boolean isEndOfFile = true;
    private RecyclerView.OnScrollListener onScrollListener;
    private DatePickerResultListener datePickerResultListener;
    private RelativeLayout rlReputationPointCalculation;

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
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        adapter.setFragment(null);
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
        rlReputationPointCalculation = (RelativeLayout) view.findViewById(R.id.rl_reputation_point_calculation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTime) {
            inject();
            presenter.setSessionHandler(sessionHandler);
            presenter.setReviewReputationUseCase(reviewReputationUseCase);
            presenter.setGcmHandler(gcmHandler);
            presenter.setErrorNetworkListener(this);
            presenter.setReviewReputationMergeUseCase(reviewReputationMergeUseCase);
            gmNetworkErrorHelper = new TopAdsNetworkErrorHelper(null, rootView);
            reputationViewHelper = new ReputationViewHelper(topSlideOffBar);
            setupRecyclerView();
        }
        fetchData();
    }

    private void fetchData() {
        if (presenter.getNetworkStatus()
                == TopAdsAddProductListPresenter.NetworkStatus.ONACTIVITYFORRESULT) {
            refreshHandler.setRefreshing(true);
            firstTimeNetworkCall();
        } else {
            if (isFirstTime) {
                presenter.setNetworkStatus(
                        TopAdsAddProductListPresenter.NetworkStatus.PULLTOREFRESH);
                adapter.showLoadingFull(true);
                swipeToRefresh.setEnabled(false);
                firstTimeNetworkCall();

                isFirstTime = false;
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
                    presenter.loadMoreNetworkCall();
                }
            }
        };
        listViewBalance.addOnScrollListener(onScrollListener);
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
        rlReputationPointCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerReputationFragment.this.startActivity(
                        new Intent(
                                SellerReputationFragment.this.getActivity(),
                                SellerReputationInfoActivity.class
                        )
                );
            }
        });
    }

    @Override
    protected void initialVar() {
        adapter = SellerReputationAdapter.createInstance(getActivity());
        adapter.setFragment(this);
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
        datePickerResultListener = new DatePickerResultListener(this);
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

    @Override
    public SetDateHeaderModel getHeaderModel() {
        return adapter.getHeaderModel();
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
        // disable pull to refresh + hide
        refreshHandler.setRefreshing(false);
        adapter.showLoading(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {

                    switch (presenter.getNetworkStatus()) {
                        case ONACTIVITYFORRESULT:
                        case PULLTOREFRESH:
                            adapter.clear();
                            adapter.showEmptyFull(true);
                            break;
                        default:
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

                                    presenter.setNetworkStatus(
                                            TopAdsAddProductListPresenter.NetworkStatus.RETRYNETWORKCALL);
                                    loadMoreCall();
                                }
                            }, getActivity());
                            break;
                    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (datePickerResultListener != null) {
            datePickerResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        // get header - index 0
        SetDateHeaderModel headerModel = adapter.getHeaderModel();
        // reformat view model
        headerModel.setStartDate(presenter.formatDate(sDate));
        headerModel.setEndDate(presenter.formatDate(eDate));
        headerModel.setsDate(sDate);
        headerModel.seteDate(eDate);
        // add to header adapter back
        adapter.notifyHeaderChange(headerModel);

        // set start date and end date to presenter
        presenter.setStartDate(sDate);
        presenter.setEndDate(eDate);

        presenter.setNetworkStatus(
                TopAdsAddProductListPresenter.NetworkStatus.ONACTIVITYFORRESULT);
    }
}
