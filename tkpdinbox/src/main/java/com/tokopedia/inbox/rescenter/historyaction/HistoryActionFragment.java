package com.tokopedia.inbox.rescenter.historyaction;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.historyaction.view.customadapter.HistoryActionAdapter;
import com.tokopedia.inbox.rescenter.historyaction.view.model.HistoryActionViewItem;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentImpl;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentPresenter;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentView;

import java.util.ArrayList;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryActionFragment extends BasePresenterFragment<HistoryActionFragmentPresenter>
    implements HistoryActionFragmentView {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_VIEW_DATA = "extra_view_data";

    private RecyclerView recyclerview;
    private HistoryActionAdapter adapter;

    private String resolutionID;

    private ArrayList<HistoryActionViewItem> viewData;

    private TkpdProgressDialog normalLoading;

    public static Fragment createInstance(String resolutionID) {
        HistoryActionFragment fragment = new HistoryActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public HistoryActionFragment() {
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
    public void setViewData(ArrayList<HistoryActionViewItem> viewData) {
        this.viewData = viewData;
    }

    @Override
    public ArrayList<HistoryActionViewItem> getViewData() {
        return viewData;
    }

    @Override
    public void renderData() {
        adapter.setArraylist(getViewData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
        state.putParcelableArrayList(EXTRA_PARAM_VIEW_DATA, getViewData());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
        setViewData(savedState.<HistoryActionViewItem>getParcelableArrayList(EXTRA_PARAM_VIEW_DATA));
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new HistoryActionFragmentImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

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
    protected void initialVar() {
        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        viewData = new ArrayList<>();
        adapter = new HistoryActionAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {

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

}
