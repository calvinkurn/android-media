package com.tokopedia.seller.product.etalase.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.base.view.adapter.BaseRetryDataBinder;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.etalase.di.component.DaggerEtalasePickerComponent;
import com.tokopedia.seller.product.etalase.di.module.EtalasePickerModule;
import com.tokopedia.seller.product.etalase.view.adapter.EtalasePickerAdapter;
import com.tokopedia.seller.product.etalase.view.adapter.EtalasePickerAdapterListener;
import com.tokopedia.seller.product.etalase.view.listener.EtalasePickerFragmentListener;
import com.tokopedia.seller.product.etalase.view.listener.EtalasePickerView;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseViewModel;
import com.tokopedia.seller.product.etalase.view.presenter.EtalasePickerPresenter;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/5/17.
 */
public class EtalasePickerFragment extends BaseDaggerFragment implements EtalasePickerView, EtalasePickerAdapterListener, SearchView.OnQueryTextListener {
    public static final String TAG = "EtalasePicker";
    public static final String SELECTED_ETALASE_ID = "SELECTED_ETALASE_ID";
    public static final int UNSELECTED_ETALASE_ID = -1;
    protected EtalasePickerAdapter adapter;
    @Inject
    EtalasePickerPresenter presenter;
    private EtalasePickerFragmentListener listener;
    private TkpdProgressDialog tkpdProgressDialog;

    public static EtalasePickerFragment createInstance(long etalaseId) {
        EtalasePickerFragment fragment = new EtalasePickerFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_ETALASE_ID, etalaseId);
        args.putLong(SELECTED_ETALASE_ID, etalaseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerEtalasePickerComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .etalasePickerModule(new EtalasePickerModule())
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EtalasePickerFragmentListener){
            this.listener = (EtalasePickerFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must impelement EtalasePickerFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_etalase_picker, container, false);

        setupRecyclerView(view);
        long etalaseId = getArguments().getLong(SELECTED_ETALASE_ID, UNSELECTED_ETALASE_ID);
        if (etalaseId != UNSELECTED_ETALASE_ID) {
            setupSelectedEtalase(etalaseId);
        }
        presenter.attachView(this);
        refreshEtalaseData();

        return view;
    }

    private void setupSelectedEtalase(long etalaseId) {
        adapter.setSelectedEtalase(etalaseId);
    }

    private void setupRecyclerView(View view) {
        RecyclerView etalaseRecyclerView = (RecyclerView) view.findViewById(R.id.etalase_picker_recycler_view);
        final LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        etalaseRecyclerView.setLayoutManager(layout);
        adapter = getNewAdapter();
        RetryDataBinder topAdsRetryDataBinder = new BaseRetryDataBinder(adapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                refreshEtalaseData();
            }
        });
        adapter.setRetryView(topAdsRetryDataBinder);
        etalaseRecyclerView.setAdapter(adapter);
        etalaseRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layout.findLastVisibleItemPosition();
                int visibleItem = layout.getItemCount() - 1;
                if (lastItemPosition == visibleItem  && adapter.isHasNextPage()) {
                    presenter.fetchNextPageEtalaseData(adapter.getPage());
                }
            }
        });
    }

    protected EtalasePickerAdapter getNewAdapter() {
        return new EtalasePickerAdapter(this);
    }

    @Override
    public void refreshEtalaseData() {
        presenter.fetchFirstPageEtalaseData();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showListLoading() {
        adapter.showLoadingFull(true);
    }

    @Override
    public void dismissListLoading() {
        adapter.showLoading(false);
        adapter.showLoadingFull(false);
    }

    @Override
    public void showListRetry() {
        adapter.showRetryFull(true);
    }

    @Override
    public void dismissListRetry() {
        adapter.showRetryFull(false);
    }

    @Override
    public void clearEtalaseList() {
        adapter.clearEtalaseList();
    }

    @Override
    public void renderEtalaseList(MyEtalaseViewModel etalases) {
        adapter.renderData(etalases);
    }

    @Override
    public void showLoadingDialog() {
        if (tkpdProgressDialog != null){
            tkpdProgressDialog.showDialog();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (tkpdProgressDialog != null){
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(Throwable exception) {
        NetworkErrorHelper.showSnackbar(
                getActivity(), ViewUtils.getGeneralErrorMessage(getActivity(), exception)
        );
    }

    @Override
    public void showRetryAddNewEtalase(final String newEtalaseName) {
        NetworkErrorHelper
                .createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.addNewEtalase(newEtalaseName);
                    }
                })
                .showRetrySnackbar();
    }

    @Override
    public void showNextListLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_product_etalase_picker, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);
        if (getActivity() instanceof BaseSimpleActivity) {
            ((BaseSimpleActivity) getActivity()).updateOptionMenuColor(menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            listener.openAddNewEtalaseDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addNewEtalase(String newEtalaseName) {
        presenter.addNewEtalase(newEtalaseName);
    }

    @Override
    public void selectEtalase(Integer etalaseId, String etalaseName) {
        listener.selectEtalase(etalaseId, etalaseName);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (StringUtils.isNotBlank(query)){
            onQueryTextChange(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (StringUtils.isNotBlank(newText)) {
            adapter.setQuery(newText);
        } else {
            adapter.clearQuery();
        }
        return true;
    }
}
