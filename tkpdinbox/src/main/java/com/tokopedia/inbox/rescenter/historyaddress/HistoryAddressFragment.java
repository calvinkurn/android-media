package com.tokopedia.inbox.rescenter.historyaddress;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.historyaddress.di.component.DaggerHistoryAddressComponent;
import com.tokopedia.inbox.rescenter.historyaddress.di.component.HistoryAddressComponent;
import com.tokopedia.inbox.rescenter.historyaddress.di.module.HistoryAddressModule;
import com.tokopedia.inbox.rescenter.historyaddress.view.customadapter.HistoryAddressAdapter;
import com.tokopedia.inbox.rescenter.historyaddress.view.model.HistoryAddressViewItem;
import com.tokopedia.inbox.rescenter.historyaddress.view.presenter.HistoryAddressFragmentImpl;
import com.tokopedia.inbox.rescenter.historyaddress.view.presenter.HistoryAddressFragmentView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAddressFragment extends BaseDaggerFragment
    implements HistoryAddressFragmentView {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_VIEW_DATA = "extra_view_data";
    private static final String EXTRA_PARAM_RESOLUTION_STATUS = "resolution_status";

    private RecyclerView recyclerview;
    private HistoryAddressAdapter adapter;

    private String resolutionID;
    private int resolutionStatus;

    private ArrayList<HistoryAddressViewItem> viewData;

    private TkpdProgressDialog normalLoading;

    @Inject
    HistoryAddressFragmentImpl presenter;

    public static Fragment createInstance(String resolutionID) {
        HistoryAddressFragment fragment = new HistoryAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public HistoryAddressFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.onFirstTimeLaunch();
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    @Override
    public int getResolutionStatus() {
        return resolutionStatus;
    }

    @Override
    public void setLoadingView(boolean param) {
        if (param) {
            if (getViewData() == null || getViewData().isEmpty()) {
                adapter.showLoadingFull(true);
            } else {
                adapter.showLoading(true);
            }
        } else {
            adapter.showLoadingFull(false);
            adapter.showLoading(false);
        }
    }

    @Override
    public void onGetHistoryAwbTimeOut() {
        setLoadingView(false);
        NetworkErrorHelper.showEmptyState(
                getActivity(),
                getView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.onFirstTimeLaunch();
                    }
                });
    }

    @Override
    public void onGetHistoryAwbFailed(String messageError) {
        setLoadingView(false);
        setErrorMessage(messageError);
    }

    @Override
    public void setErrorMessage(String messageError) {
        if (messageError != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), messageError, null);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        }
    }

    @Override
    public void setViewData(ArrayList<HistoryAddressViewItem> viewData) {
        this.viewData = viewData;
    }

    @Override
    public ArrayList<HistoryAddressViewItem> getViewData() {
        return viewData;
    }

    @Override
    public void renderData() {
        adapter.setArraylist(getViewData(), getResolutionStatus());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
        state.putParcelableArrayList(EXTRA_PARAM_VIEW_DATA, getViewData());
        state.putInt(EXTRA_PARAM_RESOLUTION_STATUS, getResolutionStatus());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
        setViewData(savedState.<HistoryAddressViewItem>getParcelableArrayList(EXTRA_PARAM_VIEW_DATA));
        setResolutionStatus(savedState.getInt(EXTRA_PARAM_RESOLUTION_STATUS));
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_HISTORY_ADDRESS;
    }

    @Override
    protected void initInjector() {
        ResolutionDetailComponent resolutionDetailComponent = getComponent(ResolutionDetailComponent.class);
        HistoryAddressComponent historyAddressComponent =
                DaggerHistoryAddressComponent.builder()
                        .resolutionDetailComponent(resolutionDetailComponent)
                        .historyAddressModule(new HistoryAddressModule(this))
                        .build();
        historyAddressComponent.inject(this);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        setResolutionID(arguments.getString(EXTRA_PARAM_RESOLUTION_ID));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_history_address;
    }

    @Override
    protected void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        viewData = new ArrayList<>();
        adapter = new HistoryAddressAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show) {
            normalLoading.showDialog();
        } else {
            normalLoading.dismiss();
        }
    }

    @Override
    public void showSnackBar(String messageError) {
        if (messageError == null) {
            showTimeOutMessage();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), messageError);
        }
    }

    @Override
    public void showTimeOutMessage() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setOnDestroyView();
    }
}
