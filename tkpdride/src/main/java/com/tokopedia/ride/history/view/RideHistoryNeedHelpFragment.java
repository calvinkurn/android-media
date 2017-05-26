package com.tokopedia.ride.history.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;
import butterknife.OnClick;

public class RideHistoryNeedHelpFragment extends BaseFragment implements RideHistoryNeedHelpContract.View {
    private OnFragmentInteractionListener mListener;
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private RideHistoryViewModel rideHistory;

    @BindView(R2.id.btn_billing_help)
    CardView mBtnBillingHelp;
    @BindView(R2.id.tv_user_name)
    TextView mTextViewUserName;
    @BindView(R2.id.value_trip_id)
    TextView mTextViewTripId;


    RideHistoryNeedHelpContract.Presenter mPresenter;

    public RideHistoryNeedHelpFragment() {
        // Required empty public constructor
    }


    public static RideHistoryNeedHelpFragment newInstance(RideHistoryViewModel rideHistory) {
        RideHistoryNeedHelpFragment fragment = new RideHistoryNeedHelpFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_REQUEST_ID, rideHistory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rideHistory = getArguments().getParcelable(EXTRA_REQUEST_ID);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_history_need_help;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new RideHistoryNeedHelpPresenter();
        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void renderUi() {
        mTextViewUserName.setText(new SessionHandler(getActivity()).getLoginName() + "!");
        mTextViewTripId.setText(rideHistory.getRequestId());
    }

    @Override
    public void showToast(int resourceId) {
        Toast.makeText(getActivity(), getString(resourceId), Toast.LENGTH_SHORT).show();
    }


    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.initialize();
            }
        };
    }

    @OnClick(R2.id.btn_ride_help)
    public void actionRideHelpClicked() {
        mListener.rideHelpButtonClicked();
    }

    @OnClick(R2.id.btn_billing_help)
    public void actionTokoCashBillingClicked() {
        mListener.showTokoCashBillingFragment();
    }

    @OnClick(R2.id.iv_copy)
    public void actionCopyTripIdClicked() {
        mPresenter.copyToClipboard(getActivity(), rideHistory.getRequestId());
    }

    public interface OnFragmentInteractionListener {
        void showTokoCashBillingFragment();

        void rideHelpButtonClicked();
    }
}
