package com.tokopedia.seller.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerEtalasePickerViewComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerViewComponent;
import com.tokopedia.seller.product.di.module.EtalasePickerViewModule;
import com.tokopedia.seller.product.view.adapter.etalase.EtalasePickerAdapter;
import com.tokopedia.seller.product.view.adapter.etalase.EtalasePickerAdapterListener;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/5/17.
 */
public class EtalasePickerFragment extends BaseDaggerFragment implements EtalasePickerView, EtalasePickerAdapterListener {
    public static final String TAG = "EtalasePicker";

    @Inject
    EtalasePickerPresenter presenter;

    private EtalasePickerAdapter adapter;
    private EtalasePickerFragmentListener listener;
    private TkpdProgressDialog tkpdProgressDialog;

    public static EtalasePickerFragment createInstance() {
        return new EtalasePickerFragment();
    }

    @Override
    protected void initInjector() {
        EtalasePickerViewComponent component = DaggerEtalasePickerViewComponent
                .builder()
                .etalasePickerComponent(getComponent(EtalasePickerComponent.class))
                .etalasePickerViewModule(new EtalasePickerViewModule())
                .build();
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
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
        View view = inflater.inflate(R.layout.etalase_picker_fragment_layout, container, false);

        setupRecyclerView(view);

        presenter.attachView(this);

        refreshEtalaseData();

        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView etalaseRecyclerView = (RecyclerView) view.findViewById(R.id.etalase_picker_recycler_view);
        etalaseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new EtalasePickerAdapter(this);
        adapter.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                refreshEtalaseData();
            }
        });
        etalaseRecyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshEtalaseData() {
        String shopId = new SessionHandler(getActivity()).getShopID();
        presenter.fetchEtalaseData(shopId);
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
    public void renderEtalaseList(List<MyEtalaseViewModel> etalases) {
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
    public void showError(String localizedMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), localizedMessage);
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
    public void addNewEtalase(String newEtalaseName) {
        presenter.addNewEtalase(newEtalaseName);
    }

    @Override
    public void openAddNewEtalaseDialog() {
        listener.openAddNewEtalaseDialog();
    }
}
