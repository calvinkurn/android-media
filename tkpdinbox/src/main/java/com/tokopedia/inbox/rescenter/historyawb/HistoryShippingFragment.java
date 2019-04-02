package com.tokopedia.inbox.rescenter.historyawb;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import com.tokopedia.inbox.rescenter.detailv2.view.activity.TrackShippingActivity;
import com.tokopedia.inbox.rescenter.historyawb.di.module.HistoryAwbModule;
import com.tokopedia.inbox.rescenter.detailv2.view.customdialog.TrackShippingDialog;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.historyawb.di.DaggerHistoryAwbComponent;
import com.tokopedia.inbox.rescenter.historyawb.di.HistoryAwbComponent;
import com.tokopedia.inbox.rescenter.historyawb.view.customadapter.HistoryShippingAdapter;
import com.tokopedia.inbox.rescenter.historyawb.view.model.HistoryAwbViewItem;
import com.tokopedia.inbox.rescenter.historyawb.view.presenter.HistoryShippingFragmentImpl;
import com.tokopedia.inbox.rescenter.historyawb.view.presenter.HistoryShippingFragmentView;
import com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryShippingFragment extends BaseDaggerFragment
    implements HistoryShippingFragmentView {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_RESOLUTION_STATUS = "resolution_status";
    private static final String EXTRA_PARAM_VIEW_DATA = "extra_view_data";
    private static final String EXTRA_PARAM_ALLOW_INPUT_SHIPPING_AWB = "extra_allow_input_shipping";
    private static final int REQUEST_EDIT_SHIPPING = 12345;
    private static final int REQUEST_INPUT_SHIPPING = 54321;
    private RecyclerView recyclerview;
    private View actionAddShippig;
    private HistoryShippingAdapter adapter;
    private String resolutionID;
    private ArrayList<HistoryAwbViewItem> viewData;
    private int resolutionStatus;
    private TkpdProgressDialog normalLoading;
    private boolean allowInputNewShippingAwb;

    @Inject
    HistoryShippingFragmentImpl presenter;

    public static Fragment createInstance(String resolutionID, boolean allowInputShippingAwb) {
        HistoryShippingFragment fragment = new HistoryShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putBoolean(EXTRA_PARAM_ALLOW_INPUT_SHIPPING_AWB, allowInputShippingAwb);
        fragment.setArguments(bundle);
        return fragment;
    }

    public HistoryShippingFragment() {
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
    public void setViewData(ArrayList<HistoryAwbViewItem> viewData) {
        this.viewData = viewData;
    }

    @Override
    public ArrayList<HistoryAwbViewItem> getViewData() {
        return viewData;
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
    public void renderData() {
        adapter.setArraylist(getViewData(), getResolutionStatus());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
        state.putBoolean(EXTRA_PARAM_ALLOW_INPUT_SHIPPING_AWB, isAllowInputNewShippingAwb());
        state.putParcelableArrayList(EXTRA_PARAM_VIEW_DATA, getViewData());
        state.putInt(EXTRA_PARAM_RESOLUTION_STATUS, getResolutionStatus());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
        setAllowInputNewShippingAwb(savedState.getBoolean(EXTRA_PARAM_ALLOW_INPUT_SHIPPING_AWB));
        setViewData(savedState.<HistoryAwbViewItem>getParcelableArrayList(EXTRA_PARAM_VIEW_DATA));
        setResolutionStatus(savedState.getInt(EXTRA_PARAM_RESOLUTION_STATUS));
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        setResolutionID(arguments.getString(EXTRA_PARAM_RESOLUTION_ID));
        setAllowInputNewShippingAwb(arguments.getBoolean(EXTRA_PARAM_ALLOW_INPUT_SHIPPING_AWB));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_history_shipping;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_HISTORY_SHIPPING;
    }

    @Override
    protected void initInjector() {
        ResolutionDetailComponent resolutionDetailComponent = getComponent(ResolutionDetailComponent.class);
        HistoryAwbComponent historyAwbComponent =
                DaggerHistoryAwbComponent.builder()
                        .resolutionDetailComponent(resolutionDetailComponent)
                        .historyAwbModule(new HistoryAwbModule(this))
                        .build();
        historyAwbComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view);
        actionAddShippig = view.findViewById(R.id.action_input_shipping_ref_number);
    }

    @Override
    protected void setViewListener() {
        actionAddShippig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        InputShippingActivity.createNewPageIntent(context, getResolutionID()),
                        REQUEST_INPUT_SHIPPING
                );
                getBottomSheetActivityTransition();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        viewData = new ArrayList<>();
        adapter = new HistoryShippingAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void showInpuNewShippingAwb(boolean param) {
        actionAddShippig.setVisibility(param && isAllowInputNewShippingAwb() ? View.VISIBLE : View.GONE);
    }

    public boolean isAllowInputNewShippingAwb() {
        return allowInputNewShippingAwb;
    }

    public void setAllowInputNewShippingAwb(boolean allowInputNewShippingAwb) {
        this.allowInputNewShippingAwb = allowInputNewShippingAwb;
    }

    @Override
    public void onActionEditClick(String conversationID, String shipmentID, String shippingRefNumber) {
        startActivityForResult(
                InputShippingActivity.createEditPageIntent(context,
                        resolutionID,
                        conversationID,
                        shipmentID,
                        shippingRefNumber
                ),
                REQUEST_EDIT_SHIPPING
        );
        getBottomSheetActivityTransition();
    }

    @Override
    public void onActionTrackClick(String shipmentID, String shippingRefNumber) {
        startActivity(TrackShippingActivity.newInstance(
                getActivity(),
                shipmentID,
                shippingRefNumber)
        );
        getBottomSheetActivityTransition();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshPage();
                    setResultSuccessForDetailResCenter();
                }
                break;
            case REQUEST_INPUT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshPage();
                    setResultSuccessForDetailResCenter();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void setResultSuccessForDetailResCenter() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
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
    public void resetList() {
        adapter.setArraylist(new ArrayList<HistoryAwbViewItem>(), getResolutionStatus());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setOnDestroyView();
    }

    public void getBottomSheetActivityTransition() {
        getActivity().overridePendingTransition(R.anim.pull_up, R.anim.push_down);
    }
}
