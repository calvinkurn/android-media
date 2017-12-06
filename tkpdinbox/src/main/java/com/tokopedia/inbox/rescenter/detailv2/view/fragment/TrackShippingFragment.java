package com.tokopedia.inbox.rescenter.detailv2.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.TrackShippingActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.TrackShippingNewAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.TrackShippingFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.TrackShippingFragmentPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by milhamj on 24/11/17.
 */

public class TrackShippingFragment extends BaseDaggerFragment implements TrackShippingFragmentListener.View {

    private String shipmentId;
    private String shippingRef;

    View mainView;
    View loadingView;
    TextView awbNumber;
    TextView receiverName;
    TextView deliveryStatus;
    RecyclerView trackingDetail;

    private final String DELIVERY_STATUS_DELIVERED = "Delivered";
    private final String DELIVERY_STATUS_ON_PROCESS = "On Process";

    @Inject
    TrackShippingFragmentPresenter presenter;

    public static TrackShippingFragment newInstance(String shipmentId,
                                                 String shippingRef) {
        TrackShippingFragment fragment = new TrackShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TrackShippingActivity.PARAM_SHIPMENT_ID, shipmentId);
        bundle.putString(TrackShippingActivity.PARAM_SHIPPING_REF, shippingRef);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        ResolutionDetailComponent resolutionDetailComponent =
                DaggerResolutionDetailComponent.builder()
                        .appComponent(appComponent)
                        .resolutionDetailModule(new ResolutionDetailModule(this))
                        .build();
        resolutionDetailComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
//        presenter.doTrackShipping(shipmentId, shippingRef);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(TrackShippingActivity.PARAM_SHIPMENT_ID, shipmentId);
        state.putString(TrackShippingActivity.PARAM_SHIPPING_REF, shippingRef);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        shipmentId = savedState.getString(TrackShippingActivity.PARAM_SHIPMENT_ID);
        shippingRef = savedState.getString(TrackShippingActivity.PARAM_SHIPPING_REF);
        presenter.doTrackShipping(shipmentId, shippingRef);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        shipmentId = arguments.getString(TrackShippingActivity.PARAM_SHIPMENT_ID);
        shippingRef = arguments.getString(TrackShippingActivity.PARAM_SHIPPING_REF);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_track_shipping;
    }

    @Override
    protected void initView(View view) {
        mainView = view.findViewById(R.id.main_view);
        awbNumber = (TextView) view.findViewById(R.id.tv_awb_number);
        receiverName = (TextView) view.findViewById(R.id.tv_receiver_name);
        deliveryStatus = (TextView) view.findViewById(R.id.tv_delivery_status);
        trackingDetail = (RecyclerView) view.findViewById(R.id.rv_tracking_detail);

        loadingView = view.findViewById(R.id.loading_view);
        presenter.initPresenter(shipmentId, shippingRef);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER;
    }

    @Override
    public void showLoading() {
        mainView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrackingTimeOut() {
        hideLoading();
        showEmptyState();
    }

    @Override
    public void onTrackingSuccess(TrackingDialogViewModel trackingDialogViewModel) {
        hideLoading();
        loadDataToView(trackingDialogViewModel);
    }

    @Override
    public void onTrackingError(String messageError) {
        hideLoading();
        showEmptyState(messageError);
    }

    @Override
    public void onTrackingFailed() {
        hideLoading();
        showEmptyState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    private void loadDataToView(TrackingDialogViewModel trackingDialogViewModel){
        awbNumber.setText(trackingDialogViewModel.getShippingRefNum());
        if(trackingDialogViewModel.getReceiverName() != null && !
                trackingDialogViewModel.getReceiverName().equals("null")) {
            receiverName.setText(trackingDialogViewModel.getReceiverName());
        }
        if(trackingDialogViewModel.isDelivered()){
            deliveryStatus.setText(DELIVERY_STATUS_DELIVERED);
        } else {
            deliveryStatus.setText(DELIVERY_STATUS_ON_PROCESS);
        }
        populateRecyclerView(trackingDialogViewModel.getTrackHistory());
    }

    private void populateRecyclerView(List<TrackingHistoryDialogViewModel> trackingHistory){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        trackingDetail.setLayoutManager(mLayoutManager);

        TrackShippingNewAdapter trackShippingNewAdapter =
                TrackShippingNewAdapter.newInstance(getActivity(), trackingHistory);
        trackingDetail.setAdapter(trackShippingNewAdapter);
    }

    private void showEmptyState(){
        showEmptyState(null);
    }

    private void showEmptyState(String messageError){
        if(messageError!=null){
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    messageError,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.doTrackShipping(shipmentId, shippingRef);
                        }
                    });
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.doTrackShipping(shipmentId, shippingRef);
                        }
                    });
        }
    }
}
