package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.mapper.SearchProductEOFMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsDefaultParamUseCase;
import com.tokopedia.seller.topads.exception.AddProductListException;
import com.tokopedia.seller.topads.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.topads.utils.TopAdsNetworkErrorHelper;
import com.tokopedia.seller.topads.view.TopAdsSearchProductView;
import com.tokopedia.seller.topads.view.activity.TopAdsFilterProductPromoActivity;
import com.tokopedia.seller.topads.view.adapter.TopAdsAddProductListAdapter;
import com.tokopedia.seller.topads.view.listener.AddProductListInterface;
import com.tokopedia.seller.topads.view.listener.FragmentItemSelection;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsAddProductListPresenter;

import java.util.List;

/**
 * @author normansyahputa on 2/13/17.
 */
public class TopAdsAddProductListFragment extends BasePresenterFragment
        implements FragmentItemSelection, SearchView.OnQueryTextListener, TopAdsSearchProductView,
        DefaultErrorSubscriber.ErrorNetworkListener {
    public static final String TAG = "TAAddPrductListFragment";
    public static final int FILTER_REQ_CODE = 100;
    protected int totalItem;
    TopAdsManagementService topAdsSearchProductService;

    SessionHandler sessionHandler;

    TopAdsDefaultParamUseCase topAdsDefaultParamUseCase;
    private AddProductListInterface addProductListInterface;
    private ImageHandler imageHandler;
    private RecyclerView topAdsAddProductList;
    private TopAdsAddProductListAdapter topAdsProductListAdapter;
    private SwipeToRefresh swipeToRefresh;
    private RefreshHandler refreshHandler;
    private SearchView searchView;
    private TopAdsNetworkErrorHelper gmNetworkErrorHelper;
    private View rootView;
    private TopAdsAddProductListPresenter topAdsAddProductListPresenter;
    private RecyclerView.OnScrollListener onScrollListener;
    private LinearLayoutManager layoutManager;
    private SearchProductEOFMapper searchProductMapper;
    private CloudTopAdsSearchProductDataSource cloudTopAdsSeachProductDataSource;
    private TopAdsSearchProductRepository topAdsSeachProductRepository;
    private boolean isFirstTime = true;
    private boolean isEndOfFile = true;
    private int maxNumberChoosen;

    public static Fragment newInstance() {
        return new TopAdsAddProductListFragment();
    }

    public static Fragment newInstance(int maxNumberChoosen) {
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, maxNumberChoosen);

        Fragment fragment = newInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        inject();
        topAdsAddProductListPresenter.setSessionHandler(sessionHandler);
        topAdsAddProductListPresenter.setTopAdsDefaultParamUseCase(topAdsDefaultParamUseCase);
        topAdsAddProductListPresenter.setErrorNetworkListener(this);
        gmNetworkErrorHelper = new TopAdsNetworkErrorHelper(null, rootView);
        setupRecyclerView();
        fetchData();
    }


    private void fetchData() {
        if (topAdsAddProductListPresenter.getNetworkStatus()
                == TopAdsAddProductListPresenter.NetworkStatus.ONACTIVITYFORRESULT) {
            refreshHandler.setRefreshing(true);
            topAdsAddProductListPresenter.loadMore();
        } else {
            topAdsAddProductListPresenter.setNetworkStatus(
                    TopAdsAddProductListPresenter.NetworkStatus.PULLTOREFRESH);
            if (topAdsAddProductListPresenter.isFirstTime()) {
                topAdsProductListAdapter.showLoadingFull(true);
                topAdsAddProductListPresenter.searchProduct();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gmNetworkErrorHelper.onPause();
        topAdsAddProductList.removeOnScrollListener(onScrollListener);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
    }

    @Override
    public void onRestoreState(Bundle savedState) {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        topAdsAddProductListPresenter = new TopAdsAddProductListPresenter();
        topAdsAddProductListPresenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity != null && activity instanceof AddProductListInterface) {
            addProductListInterface = (AddProductListInterface) activity;
        } else {
            throw new AddProductListException();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        maxNumberChoosen = arguments.getInt(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_add_product_list;
    }

    @Override
    protected void initView(View view) {
        this.rootView = view;
        topAdsAddProductList = (RecyclerView) view.findViewById(R.id.top_ads_add_product_recycler_view);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.top_ads_add_product_refresh);
        refreshHandler = new RefreshHandler(swipeToRefresh, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                dismissSnackbar();

                if (topAdsProductListAdapter.getDataSize() > 0) {

                } else {
                    topAdsProductListAdapter.clear();
                    topAdsProductListAdapter.notifyDataSetChanged();

                    topAdsProductListAdapter.showLoadingFull(true);
                }

                topAdsAddProductListPresenter.resetPage();
                topAdsAddProductListPresenter.setNetworkStatus(
                        TopAdsAddProductListPresenter.NetworkStatus.PULLTOREFRESH);
                searchProductNetworkCall();
            }
        });
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        totalItem = Integer.MAX_VALUE;
        topAdsProductListAdapter = new TopAdsAddProductListAdapter();
    }

    private void setupRecyclerView() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isEndOfFile) {
                    return;
                }

                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem
                        && topAdsProductListAdapter.getDataSize() < totalItem) {
                    topAdsAddProductListPresenter.incrementPage();
                    topAdsAddProductListPresenter.setNetworkStatus(
                            TopAdsAddProductListPresenter.NetworkStatus.LOADMORE);
                    loadMoreNetworkCall();
                    topAdsProductListAdapter.showLoading(true);
                }
            }
        };
        imageHandler = addProductListInterface.imageHandler();
        topAdsProductListAdapter.setImageHandler(imageHandler);
        topAdsProductListAdapter.setFragmentItemSelection(this);
        if(isFirstTime) {
            layoutManager = new LinearLayoutManager(getActivity());
            this.topAdsAddProductList.setLayoutManager(layoutManager);
            this.topAdsAddProductList.setAdapter(topAdsProductListAdapter);
        }
        isFirstTime = false;
        this.topAdsAddProductList.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void setActionVar() {

    }

    protected void loadMoreNetworkCall() {
        topAdsAddProductListPresenter.loadMore();
    }

    protected void searchProductNetworkCall() {
        topAdsAddProductListPresenter.searchProduct();
    }

    private void inject() {
        //[START] This is for dependent component
        topAdsSearchProductService = new TopAdsManagementService();
        sessionHandler = new SessionHandler(getActivity());
        searchProductMapper = new SearchProductEOFMapper();
        cloudTopAdsSeachProductDataSource = new CloudTopAdsSearchProductDataSource(
                getActivity(),
                topAdsSearchProductService,
                searchProductMapper
        );
        topAdsSeachProductRepository = new TopAdsSearchProductRepositoryImpl(cloudTopAdsSeachProductDataSource);
        topAdsDefaultParamUseCase = new TopAdsDefaultParamUseCase(
                new JobExecutor(), new UIThread(), topAdsSeachProductRepository
        );
        //[END] This is for dependent component
    }

    @Override
    public void onChecked(int position, TopAdsProductViewModel data) {
        if (addProductListInterface.selections().size() == maxNumberChoosen) {
            return;
        }

        addProductListInterface.addSelection(data);
        addProductListInterface.onChecked(position, data);
        topAdsProductListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        addProductListInterface.removeSelection(data);
        addProductListInterface.onUnChecked(position, data);
        topAdsProductListAdapter.notifyItemChanged(position);
    }

    @Override
    public boolean isSelected(TopAdsProductViewModel data) {
        return addProductListInterface.isSelected(data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_ads_product_list, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_filter){
            if(topAdsAddProductListPresenter != null) {
                TopAdsFilterProductPromoActivity.start(
                        this,
                        getActivity(),
                        FILTER_REQ_CODE,
                        topAdsAddProductListPresenter.getSelectedFilterStatus(),
                        topAdsAddProductListPresenter.getSelectedFilterEtalaseId(),
                        addProductListInterface.isExistingGroup());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                if (topAdsAddProductListPresenter != null) {
                    topAdsAddProductListPresenter.putSelectedFilterStatus(
                            data.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, 0));
                    topAdsAddProductListPresenter.putSelectedEtalaseId(
                            data.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, 0));

                    topAdsAddProductListPresenter.setNetworkStatus(TopAdsAddProductListPresenter.NetworkStatus.ONACTIVITYFORRESULT);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        fetchDataWithQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        fetchDataWithQuery(newText);
        return true;
    }

    public void fetchDataWithQuery(String newText) {
        Log.d(TAG, "fetchDataWithQuery " + newText);
        if (newText != null && newText.isEmpty()) {
            topAdsAddProductListPresenter.setQuery(null);
        } else {
            topAdsAddProductListPresenter.setQuery(newText);
        }

        topAdsAddProductListPresenter.resetPage();

        dismissSnackbar();

        topAdsProductListAdapter.clear();
        topAdsProductListAdapter.notifyDataSetChanged();
        topAdsProductListAdapter.showLoadingFull(true);

        topAdsAddProductListPresenter.setNetworkStatus(
                TopAdsAddProductListPresenter.NetworkStatus.SEARCHVIEW);
        searchProductNetworkCall();
    }

    @Override
    public void loadData(List<TypeBasedModel> datas) {
        renderDatas(datas);
    }

    private void renderDatas(List<TypeBasedModel> datas) {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
        }

        topAdsProductListAdapter.showLoadingFull(false);
        topAdsProductListAdapter.showEmpty(false);
        topAdsProductListAdapter.showRetry(false);

        switch (topAdsAddProductListPresenter.getNetworkStatus()) {
            case LOADMORE:
                break;
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                topAdsProductListAdapter.clear();
                break;
            case RETRYNETWORKCALL:
                if (topAdsProductListAdapter.getDataSize() <= 0) {
                    topAdsProductListAdapter.clear();
                }
                break;
        }
        topAdsProductListAdapter.addAllWithoutNotify(datas);
        boolean isEmpty = topAdsProductListAdapter.getDataSize() <= 0;
        if (isEmpty) {
            if (!isEndOfFile) {
                topAdsProductListAdapter.showLoading(true);
            } else {
                topAdsProductListAdapter.showEmptyFull(true);
            }
        } else {
            if (!isEndOfFile) {
                topAdsProductListAdapter.showLoading(true);
            } else {
                topAdsProductListAdapter.showLoading(false);
            }
            topAdsProductListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMore(List<TypeBasedModel> datas) {
        renderDatas(datas);
    }

    @Override
    public void dismissSnackbar() {
        gmNetworkErrorHelper.dismissSnackbar();
    }

    @Override
    public void showMessageError(final String errorMessage) {
        // disable pull to refresh + hide
        refreshHandler.setRefreshing(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {

                    switch (topAdsAddProductListPresenter.getNetworkStatus()) {
                        case SEARCHVIEW:
                            topAdsProductListAdapter.clear();
                            topAdsProductListAdapter.showEmptyFull(true);
                            break;
                    }

                    gmNetworkErrorHelper.showSnackbar(errorMessage, "COBA KEMBALI", new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            Toast.makeText(
                                    TopAdsAddProductListFragment.this.getActivity(),
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                            ).show();

                            dismissSnackbar();

                            refreshHandler.setRefreshing(true);

                            topAdsAddProductListPresenter.setNetworkStatus(
                                    TopAdsAddProductListPresenter.NetworkStatus.RETRYNETWORKCALL);
                            loadMoreNetworkCall();
                        }
                    }, getActivity());
                }
            }
        }, 100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsAddProductListPresenter.detachView();
    }

    @Override
    public void notifyUnchecked(TopAdsProductViewModel topAdsProductViewModel) {
        topAdsProductListAdapter.notifyUnCheck(topAdsProductViewModel);
    }

    @Override
    public boolean isExistingGroup() {
        if(addProductListInterface != null){
            return addProductListInterface.isExistingGroup();
        }
        return false;
    }

    @Override
    public void setLoadMoreFlag(boolean eof) {
        isEndOfFile = eof;
    }
}
