package com.tokopedia.inbox.rescenter.history;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.history.customadapter.HistoryShippingAdapter;
import com.tokopedia.inbox.rescenter.history.viewmodel.HistoryShippingData;
import com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryShippingFragment extends BasePresenterFragment<HistoryShippingFragmentPresenter>
    implements HistoryShippingFragmentView {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final int REQUEST_EDIT_SHIPPING = 12345;
    private static final int REQUEST_INPUT_SHIPPING = 54321;
    private RecyclerView recyclerview;
    private View actionAddShippig;
    private List<HistoryShippingData> arraylist;
    private HistoryShippingAdapter adapter;
    private String resolutionID;

    public static Fragment createInstance(String resolutionID) {
        HistoryShippingFragment fragment = new HistoryShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
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
            if (arraylist.isEmpty()) {
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
    public void setErrorMessage(String messageError) {
        if (messageError != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), messageError, null);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        }
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new HistoryShippingFragmentImpl(getActivity(), this);
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
        return R.layout.fragment_history_shipping;
    }

    @Override
    protected void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view);
        actionAddShippig = view.findViewById(R.id.action_add);
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
            }
        });
    }

    @Override
    protected void initialVar() {
        arraylist = new ArrayList<>();
        adapter = new HistoryShippingAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void showInpuNewShippingAwb(boolean param) {
        actionAddShippig.setVisibility(param ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void setActionVar() {

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
    }

    @Override
    public void onActionTrackClick(String shipmentID, String shippingRefNumber) {
        presenter.doActionTrack(shippingRefNumber, shipmentID);
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
}
