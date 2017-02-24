package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.GMNetworkErrorHelper;
import com.tokopedia.seller.gmstat.views.OnActionClickListener;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.mapper.SearchProductMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsDefaultParamUseCase;
import com.tokopedia.seller.topads.view.activity.TopAdsFilterProductPromoActivity;
import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;
import com.tokopedia.seller.topads.exception.AddProductListException;
import com.tokopedia.seller.topads.listener.AddProductListInterface;
import com.tokopedia.seller.topads.listener.FragmentItemSelection;
import com.tokopedia.seller.topads.view.TopAdsSearchProductView;
import com.tokopedia.seller.topads.view.adapter.TopAdsAddProductListAdapter;
import com.tokopedia.seller.topads.view.models.TopAdsAddProductModel;
import com.tokopedia.seller.topads.view.models.TypeBasedModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsAddProductListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author normansyahputa on 2/13/17.
 */
public class TopAdsAddProductListFragment extends BasePresenterFragment
        implements FragmentItemSelection, SearchView.OnQueryTextListener, TopAdsSearchProductView {
    public static final String TAG = "TopAdsAddProductListFragment";
    private final String dummyUrl
            = "https://static.pexels.com/photos/68672/beach-beverage-caribbean-cocktail-68672.jpeg";
    private final String twoLineDummyString
            = "Sandisk MicroSD Ultra 80MB/S \n64GB Class 10 UHS-1 - SDSQUNC-064G";
    private final String oneLineDummyString
            = "Seagate Expansion 500GB USB 3.0";
    private final String snippetPromoted
            = "Promoted";
    protected int totalItem;

    public static final int FILTER_REQ_CODE = 100;
    private int selectedFilterEtalaseId;
    private int selectedFilterStatus;

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
    private GMNetworkErrorHelper gmNetworkErrorHelper;
    private View rootView;
    private TopAdsAddProductListPresenter topAdsAddProductListPresenter;
    private RecyclerView.OnScrollListener onScrollListener;
    private LinearLayoutManager layoutManager;
    private SearchProductMapper searchProductMapper;
    private CloudTopAdsSearchProductDataSource cloudTopAdsSeachProductDataSource;
    private TopAdsSearchProductRepository topAdsSeachProductRepository;

    public static Fragment newInstance() {
        return new TopAdsAddProductListFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        inject();
        topAdsAddProductListPresenter.setSessionHandler(sessionHandler);
        topAdsAddProductListPresenter.setTopAdsDefaultParamUseCase(topAdsDefaultParamUseCase);
        gmNetworkErrorHelper = new GMNetworkErrorHelper(null, rootView);
        setupRecyclerView();
        gmNetworkErrorHelper.showSnackbar("coba aja", "coba lagi", new OnActionClickListener() {
            @Override
            public void onClick(@SuppressWarnings("UnusedParameters") View view) {
                Toast.makeText(
                        TopAdsAddProductListFragment.this.getActivity(),
                        "konyol",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        fetchData();
    }

    private void fetchData() {
        // TODO fetch data by filter of selectedFilterStatus and selectedFilterEtalaseId
        topAdsAddProductListPresenter.searchProduct();
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
        state.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, selectedFilterStatus);
        state.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedFilterEtalaseId);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        selectedFilterStatus =  savedState.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, 0);
        selectedFilterEtalaseId =  savedState.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, 0);
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
                topAdsProductListAdapter.clear();
                topAdsProductListAdapter.notifyDataSetChanged();

                topAdsProductListAdapter.showLoadingFull(true);

                topAdsAddProductListPresenter.resetPage();
                topAdsAddProductListPresenter.searchProduct();
            }
        });
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        totalItem = Integer.MAX_VALUE;
    }

    private void setupRecyclerView() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem
                        && topAdsProductListAdapter.getDataSize() < totalItem) {
                    topAdsAddProductListPresenter.incrementPage();
                    topAdsAddProductListPresenter.loadMore();
                    topAdsProductListAdapter.showLoading(true);
                }
            }
        };
        imageHandler = addProductListInterface.imageHandler();
        topAdsProductListAdapter = new TopAdsAddProductListAdapter(imageHandler, this);
//        this.topAdsProductListAdapter.setOnItemClickListener(onItemClickListener);
        layoutManager = new LinearLayoutManager(getActivity());
        this.topAdsAddProductList.setLayoutManager(layoutManager);
        this.topAdsAddProductList.addOnScrollListener(onScrollListener);
        this.topAdsAddProductList.setAdapter(topAdsProductListAdapter);
        topAdsProductListAdapter.addAllWithoutNotify(dummyData());
        topAdsProductListAdapter.showLoading(true);
    }

    @Override
    protected void setActionVar() {

    }

    private void inject() {
        //[START] This is for dependent component
        topAdsSearchProductService = new TopAdsManagementService();
        sessionHandler = new SessionHandler(getActivity());
        searchProductMapper = new SearchProductMapper();
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

    private List<TypeBasedModel> dummyData() {
        List<TypeBasedModel> dummyData = new ArrayList<>();
        TopAdsAddProductModel dummy
                = new TopAdsAddProductModel(dummyUrl, twoLineDummyString, snippetPromoted);
        TopAdsAddProductModel emptySnippet
                = new TopAdsAddProductModel(dummyUrl, oneLineDummyString, null);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            if (random.nextBoolean())
                dummyData.add(dummy);
            else
                dummyData.add(emptySnippet);
        }
        return dummyData;
    }

    @Override
    public void onChecked(int position, TopAdsProductViewModel data) {
        addProductListInterface.onChecked(position, data);
        topAdsProductListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        addProductListInterface.onUnChecked(position, data);
        topAdsProductListAdapter.notifyItemChanged(position);
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
            TopAdsFilterProductPromoActivity.start(
                    this,
                    getActivity(),
                    FILTER_REQ_CODE,
                    selectedFilterStatus,
                    selectedFilterEtalaseId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                selectedFilterStatus = data.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, 0);
                selectedFilterEtalaseId = data.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, 0);

            }
            else {
                selectedFilterStatus = 0;
                selectedFilterEtalaseId = 0;
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
        // Log.d(TAG, "fetchDataWithQuery " + newText);
        if (newText != null && newText.isEmpty()) {
            topAdsAddProductListPresenter.setQuery(null);
        } else {
            topAdsAddProductListPresenter.setQuery(newText);
        }

        topAdsAddProductListPresenter.resetPage();

        topAdsProductListAdapter.clear();
        topAdsProductListAdapter.notifyDataSetChanged();

        topAdsProductListAdapter.showLoadingFull(true);

        topAdsAddProductListPresenter.searchProduct();
    }

    @Override
    public void loadData(List<TypeBasedModel> datas) {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
        }

        topAdsProductListAdapter.clear();
        topAdsProductListAdapter.addAll(datas);
    }

    @Override
    public void loadMore(List<TypeBasedModel> datas) {
        if (datas != null && datas.isEmpty()) {
//            topAdsProductListAdapter.showLoadingFull(false);
            topAdsProductListAdapter.showEmptyFull(true);
        } else {
            topAdsProductListAdapter.addAllWithoutNotify(datas);
            topAdsProductListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsAddProductListPresenter.detachView();
    }

    @Override
    public void notifyUnchecked(int position) {
        topAdsProductListAdapter.notifyUnCheck(position);
    }
}
