package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.domain.model.Paging;
import com.tokopedia.ride.bookingride.view.adapter.EndlessRecyclerViewScrollListener;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.history.di.DaggerRideHistoryComponent;
import com.tokopedia.ride.history.di.RideHistoryComponent;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.view.adapter.ItemClickListener;
import com.tokopedia.ride.history.view.adapter.RideHistoryAdapter;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryAdapterTypeFactory;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryTypeFactory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

public class RideHistoryFragment extends BaseFragment implements ItemClickListener, RideHistoryContract.View {

    @BindView(R2.id.container)
    RelativeLayout containerFrameLayout;
    @BindView(R2.id.rv_list_rides)
    RecyclerView listRidesRecyclerView;
    @BindView(R2.id.sw_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;

    RideHistoryAdapter adapter;

    @Inject
    RideHistoryPresenter presenter;

    OnFragmentInteractionListener mOnFragmentInteractionListener;

    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Paging paging;
    private boolean isLoadMoreActive;

    public static Fragment newInstance() {
        return new RideHistoryFragment();
    }

    public RideHistoryFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_history;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewListener();
        presenter.attachView(this);
        presenter.initialize();
    }

    private void setViewListener() {
        RideHistoryTypeFactory rideHistoryTypeFactory = new RideHistoryAdapterTypeFactory(this);
        adapter = new RideHistoryAdapter(rideHistoryTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (getUserVisibleHint())
                    if (paging != null && !TextUtils.isEmpty(paging.getNextUrl())) {
                        if (!isLoadMoreActive) {
                            isLoadMoreActive = true;
                            presenter.actionLoadMore();
                        }
                    }
            }
        };


        listRidesRecyclerView.setLayoutManager(layoutManager);
        listRidesRecyclerView.setHasFixedSize(true);
        listRidesRecyclerView.setAdapter(adapter);
        listRidesRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        refreshLayout.setOnRefreshListener(getRefreshListener());
    }

    @NonNull
    private SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.actionRefreshHistoriesData();
            }
        };
    }

    @Override
    public void onHistoryClicked(RideHistoryViewModel viewModel) {
        RideGATracking.eventClickReceipt(getScreenName(), viewModel.getRequestTime(), viewModel.getTotalFare(), viewModel.getStatus());//16
        mOnFragmentInteractionListener.actionNavigateToDetail(viewModel);
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        RideHistoryComponent rideHistoryComponent = DaggerRideHistoryComponent.builder()
                .rideComponent(component)
                .build();
        rideHistoryComponent.inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public RequestParams getHistoriesParam() {
        RequestParams requestParams = RequestParams.create();
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_OS_TYPE, "1");
        return requestParams;
    }

    @Override
    public void showFailedGetHistoriesMessage() {

    }

    @Override
    public void renderHistoryLists(ArrayList<Visitable> histories) {
        adapter.clearData();
        adapter.setElement(histories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderUpdatedHistoryRow(int position, Visitable history) {
        adapter.setChangedItem(position, history);
    }

    @Override
    public void enableRefreshLayout() {
        refreshLayout.setEnabled(true);
    }

    @Override
    public void setRefreshLayoutToFalse() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void disableRefreshLayout() {
        refreshLayout.setEnabled(false);
    }

    @Override
    public void showEmptyResultLayout() {
        adapter.clearData();
        ArrayList<Visitable> emptyModels = new ArrayList<>();
        emptyModels.add(new EmptyModel());
        adapter.setElement(emptyModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showRetryLayout() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), getRetryGetHistoriesListener());
    }

    @Override
    public String getMapKey() {
        return getString(R.string.GOOGLE_API_KEY);
    }

    @Override
    public String getMapImageSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp);
        int height = getResources().getDimensionPixelSize(R.dimen.history_map_height);

        return String.format("%dx%d", width, height);
    }

    @Override
    public void showMainLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMainLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMainLayout() {
        refreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMainLayout() {
        refreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @Override
    public void showRetryLoadMoreLayout() {
        isLoadMoreActive = false;
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                isLoadMoreActive = true;
                presenter.actionLoadMore();
            }
        });
    }

    @Override
    public void renderHistoryLoadMoreLists(ArrayList<Visitable> histories) {
        isLoadMoreActive = false;
        adapter.addElements(histories);
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryGetHistoriesListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.actionRefreshHistoriesData();
            }
        };
    }

    public void actionRefreshHistoriesData() {
        presenter.actionRefreshHistoriesData();
    }

    public interface OnFragmentInteractionListener {
        void actionNavigateToDetail(RideHistoryViewModel rideHistory);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void showLoadMoreLoading() {
        adapter.addElement(new LoadingModel());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideLoadMoreLoading() {
        adapter.removeItem(adapter.getItemCount() - 1);
    }

    @Override
    public RequestParams getHistoriesLoadMoreParam() {
        RequestParams requestParams = RequestParams.create();
        Map<String, String> maps = splitQuery(Uri.parse(paging.getNextUrl()));
        for (Map.Entry<String, String> imap : maps.entrySet()) {
            requestParams.putString(imap.getKey(), imap.getValue());
        }
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_OS_TYPE, "1");
        return requestParams;
    }

    private Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException | NullPointerException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
    }


}
