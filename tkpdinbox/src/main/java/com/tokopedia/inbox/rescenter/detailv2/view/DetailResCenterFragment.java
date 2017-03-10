package com.tokopedia.inbox.rescenter.detailv2.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ButtonView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.DetailView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.HistoryView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ListProductView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.SolutionView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.StatusView;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterFragmentImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterFragmentPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailResCenterFragment extends BasePresenterFragment<DetailResCenterFragmentPresenter>
        implements DetailResCenterFragmentView {

    View loading;
    View mainView;
    ButtonView buttonView;
    StatusView statusView;
    DetailView detailView;
    ListProductView listProductView;
    SolutionView solutionView;
    HistoryView historyView;

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";

    private String resolutionID;
    private DetailViewModel viewData;

    public static DetailResCenterFragment createInstance(String resolutionID) {
        DetailResCenterFragment fragment = new DetailResCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    public DetailViewModel getViewData() {
        return viewData;
    }

    public void setViewData(DetailViewModel model) {
        this.viewData = model;
    }

    @Override
    public void showLoading(boolean isShow) {
        loading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnFirstTimeLaunch();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new DetailResCenterFragmentImpl(getActivity(), this);
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
        return R.layout.fragment_detail_rescenter;
    }

    @Override
    protected void initView(View view) {
        loading = view.findViewById(R.id.loading);
        mainView = view.findViewById(R.id.main_view);
        buttonView = (ButtonView) view.findViewById(R.id.button_view);
        statusView = (StatusView) view.findViewById(R.id.status_view);
        detailView = (DetailView) view.findViewById(R.id.detail_view);
        listProductView = (ListProductView) view.findViewById(R.id.product_view);
        solutionView = (SolutionView) view.findViewById(R.id.solution_view);
        historyView = (HistoryView) view.findViewById(R.id.history_view);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void setOnInitResCenterDetailComplete() {
        if (viewData.isTimeOut()) {
            doOnInitTimeOut();
        } else {
            if (viewData.isSuccess()) {
                doOnInitSuccess();
            } else {
                doOnInitFailed();
            }
        }
    }

    private void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener) {
        if (message != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, listener);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
        }
    }

    private void doOnInitTimeOut() {
        showLoading(false);
        showEmptyState(null, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setOnFirstTimeLaunch();
            }
        });
    }

    private void doOnInitSuccess() {
        showLoading(false);
        buttonView.renderData(getViewData().getButtonData());
        statusView.renderData(getViewData().getStatusData());
        detailView.renderData(getViewData().getDetailData());
        listProductView.renderData(getViewData().getProductData());
        solutionView.renderData(getViewData().getSolutionData());
        historyView.renderData(getViewData().getHistoryData());
    }

    private void doOnInitFailed() {
        showLoading(false);
        showEmptyState(getViewData().getMessageError(), null);
    }

}
