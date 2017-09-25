package com.tokopedia.seller.product.edit.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseRetryDataBinder;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.di.component.CatalogPickerComponent;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.activity.CatalogPickerActivity;
import com.tokopedia.seller.product.edit.view.adapter.CatalogPickerAdapter;
import com.tokopedia.seller.product.edit.view.listener.CatalogPickerView;
import com.tokopedia.seller.product.edit.view.presenter.CatalogPickerPresenter;

import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class CatalogPickerFragment extends BaseDaggerFragment implements CatalogPickerView, CatalogPickerAdapter.OnCatalogPickerListener {
    public static final String TAG = "CatalogPicker";

    public static final int ROWS = 20;
    public static final int VISIBLE_THRESHOLD = 2;

    private String keyword;
    private long departmentId;
    private long selectedCatalogId;

    CatalogPickerPresenter presenter;

    private CatalogPickerAdapter adapter;
    private LinearLayoutManager llm;

    public static CatalogPickerFragment newInstance(String keyword, long departmentId, long selectedCatalogID) {

        Bundle args = new Bundle();

        CatalogPickerFragment fragment = new CatalogPickerFragment();
        args.putString(CatalogPickerActivity.KEYWORD, keyword);
        args.putLong(CatalogPickerActivity.DEP_ID, departmentId);
        args.putLong(CatalogPickerActivity.CATALOG_ID, selectedCatalogID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        keyword = bundle.getString(CatalogPickerActivity.KEYWORD);
        departmentId = bundle.getLong(CatalogPickerActivity.DEP_ID);
        selectedCatalogId = bundle.getLong(CatalogPickerActivity.CATALOG_ID);

        adapter = new CatalogPickerAdapter(null, selectedCatalogId, 0);
        adapter.setListener(this);

        RetryDataBinder topAdsRetryDataBinder = new BaseRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                adapter.showLoadingFull(true);
                presenter.fetchCatalogData(keyword,departmentId,0, ROWS);
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);

    }

    @Override
    protected void initInjector() {
        CatalogPickerComponent catalogPickerComponent = getComponent(CatalogPickerComponent.class);
        presenter = catalogPickerComponent.catalogPickerPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_catalog_picker, container, false);
        setupRecyclerView(view);
        presenter.attachView(this);

        adapter.showLoadingFull(true);
        presenter.fetchCatalogData(keyword, departmentId, 0, ROWS);

        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    // no op when going up
                    return;
                }
                int lastVisibleItemPosition = llm.findLastVisibleItemPosition();
                if (adapter.hasNextData() &&
                        !adapter.isLoading() &&
                        llm.getItemCount() <= (lastVisibleItemPosition + VISIBLE_THRESHOLD)) {
                    adapter.showLoading(true);
                    presenter.fetchCatalogData(keyword, departmentId,
                            adapter.getNonEmptyCatalogListSize(), ROWS);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void successFetchData(List<Catalog> catalogViewModelList, int maxRows) {
        adapter.showLoading(false);
        adapter.setMaxRows(maxRows);
        adapter.addMoreData(catalogViewModelList);
    }

    @Override
    public void showError(Throwable e) {
        adapter.showLoading(false);
        // only has empty catalog, failed first time, show retry fullscreen
        if (adapter.getNonEmptyCatalogListSize() == 0) {
            adapter.showRetryFull(true);
        }
        //catalog failed to fetch when load more, show snackbar
        else if (adapter.getNonEmptyCatalogListSize() > 0) {
            String errorMessage = ViewUtils.getGeneralErrorMessage(getContext(), e);
            NetworkErrorHelper.showSnackbar(getActivity(),
                    errorMessage);
        }
    }

    public Intent getResultIntent(){
        long adapterSelectedCatalogId = adapter.getSelectedCatalogId();
        if (selectedCatalogId == adapterSelectedCatalogId) {
            return null;
        } else {
            Intent intent = new Intent();
            intent.putExtra(CatalogPickerActivity.CATALOG_ID, adapterSelectedCatalogId);
            intent.putExtra(CatalogPickerActivity.CATALOG_NAME, adapter.getSelectedCatalogName());
            return intent;
        }
    }

    @Override
    public void onCatalogClicked(long catalogId, String catalogName) {
        Intent resultIntent;
        if (selectedCatalogId == catalogId) {
            resultIntent = null;
        } else {
            Intent intent = new Intent();
            intent.putExtra(CatalogPickerActivity.CATALOG_ID, catalogId);
            intent.putExtra(CatalogPickerActivity.CATALOG_NAME, catalogName);
            resultIntent = intent;
        }
        if (resultIntent != null) {
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
        }
        getActivity().finish();
    }
}
