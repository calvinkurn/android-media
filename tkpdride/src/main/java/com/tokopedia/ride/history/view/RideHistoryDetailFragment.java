package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.utils.RideUtils;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.history.di.DaggerRideHistoryComponent;
import com.tokopedia.ride.history.di.RideHistoryComponent;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;
import static com.tokopedia.ride.scrooge.ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE;

public class RideHistoryDetailFragment extends BaseFragment implements RideHistoryDetailContract.View {
    private OnFragmentInteractionListener mListener;
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_RIDE_HISTORY = "EXTRA_RIDE_HISTORY";
    private RideHistoryViewModel rideHistory;
    private String requestId;

    @BindView(R2.id.iv_google_map)
    AppCompatImageView mapImageView;
    @BindView(R2.id.tv_ride_start_time)
    AppCompatTextView requestTimeTextView;
    @BindView(R2.id.tv_driver_car_display_name)
    AppCompatTextView driverCarTextView;
    @BindView(R2.id.tv_ride_fare)
    AppCompatTextView rideFareTextView;
    @BindView(R2.id.tv_ride_status)
    AppCompatTextView rideStatusTextView;
    @BindView(R2.id.tv_source)
    AppCompatTextView sourceTextView;
    @BindView(R2.id.tv_destination)
    AppCompatTextView destinationTextView;
    @BindView(R2.id.iv_driver_pict)
    AppCompatImageView driverPictTextView;
    @BindView(R2.id.tv_driver_name)
    AppCompatTextView driverNameTextView;
    @BindView(R2.id.top_container)
    RelativeLayout topLayout;
    @BindView(R2.id.tv_label_total_fare)
    TextView totalFareLabelTextView;
    @BindView(R2.id.tv_total_fare_value)
    TextView totalFareValueTextView;
    @BindView(R2.id.tv_label_discount)
    TextView discountLabelTextView;
    @BindView(R2.id.tv_discount)
    TextView discountValueTextView;
    @BindView(R2.id.tv_label_cashback)
    TextView cashbackLableTextView;
    @BindView(R2.id.tv_cashback)
    TextView cashbackValueTextView;
    @BindView(R2.id.tv_total_charged)
    TextView tokocashChargedTexView;
    @BindView(R2.id.rl_payment_details)
    RelativeLayout paymentDetailsLayout;
    @BindView(R2.id.layout_rate)
    RelativeLayout ratingLayout;
    @BindView(R2.id.rb_rate_star)
    RatingBar rateStarRatingBar;
    @BindView(R2.id.et_rate_comment)
    EditText rateCommentEditText;
    @BindView(R2.id.rate_confirmation)
    Button rateConfirmationButton;
    @BindView(R2.id.ride_history_layout)
    RelativeLayout rideHistoryLayout;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R2.id.rb_rating_result)
    RatingBar ratingResult;
    @BindView(R2.id.tv_label_pending_fare)
    TextView pendingFareLabelTextView;
    @BindView(R2.id.tv_pending_fare)
    TextView pendingFareValueTextView;
    @BindView(R2.id.fare_sep)
    View seperator;
    @BindView(R2.id.tv_trip_id)
    TextView tripIdTextView;
    @BindView(R2.id.label_total_charged)
    TextView amountChargedLabel;
    @BindView(R2.id.btn_pay_pending_fare)
    TextView btnPendingFare;

    ProgressDialog mProgressDialog;

    @Inject
    RideHistoryDetailPresenter mPresenter;

    public RideHistoryDetailFragment() {
        // Required empty public constructor
    }


    public static RideHistoryDetailFragment newInstance(RideHistoryViewModel rideHistory) {
        RideHistoryDetailFragment fragment = new RideHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RIDE_HISTORY, rideHistory);
        fragment.setArguments(args);
        return fragment;
    }

    public static RideHistoryDetailFragment newInstance(String requestId) {
        RideHistoryDetailFragment fragment = new RideHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rideHistory = getArguments().getParcelable(EXTRA_RIDE_HISTORY);
            if (rideHistory != null) {
                requestId = rideHistory.getRequestId();
            } else {
                requestId = getArguments().getString(EXTRA_REQUEST_ID);
            }
        }
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        RideHistoryComponent rideHistoryComponent = DaggerRideHistoryComponent.builder()
                .rideComponent(component)
                .build();
        rideHistoryComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_history_detail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());
        mPresenter.attachView(this);
        mPresenter.initialize();
        setViewListener();
    }

    private void setViewListener() {
        rateConfirmationButton.setEnabled(false);
        rateStarRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (ratingBar.getRating() > 0.0) {
                    rateConfirmationButton.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        rateConfirmationButton.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
                    } else {
                        rateConfirmationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
                    }
                } else {
                    rateConfirmationButton.setEnabled(false);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        rateConfirmationButton.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
                    } else {
                        rateConfirmationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
                    }
                }
            }
        });
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

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.initialize();
            }
        };
    }

    @Override
    public void renderHistory(RideHistoryViewModel rideHistoryViewModel) {
        tripIdTextView.setText(getString(R.string.prefx_trip_id) + " " + rideHistory.getRequestId());
        requestTimeTextView.setText(RideUtils.convertTime(rideHistory.getRequestTime()));
        rideStatusTextView.setText(rideHistory.getDisplayStatus());
        driverCarTextView.setText(rideHistory.getDriverCarDisplay());
        setPickupLocationText(rideHistory.getStartAddress());
        setDestinationLocation(rideHistory.getEndAddress());

        if (TextUtils.isEmpty(rideHistory.getDriverName())) {
            driverNameTextView.setVisibility(View.GONE);
        } else {
            driverNameTextView.setVisibility(View.VISIBLE);
            driverNameTextView.setText(getString(R.string.your_trip_with) + " " + rideHistory.getDriverName());
        }

        tokocashChargedTexView.setText(rideHistory.getTokoCashCharged());
        rideFareTextView.setText(rideHistory.getTotalFare());
        totalFareValueTextView.setText(rideHistory.getTotalFare());
        amountChargedLabel.setText(rideHistory.getPaymentMethod() + " " + getString(R.string.label_charged));

        if (rideHistory.getStatus().equalsIgnoreCase(RideStatus.COMPLETED)) {
            paymentDetailsLayout.setVisibility(View.VISIBLE);
            if (rideHistory.getCashback() > 0) {
                cashbackValueTextView.setText(rideHistory.getCashbackDisplayFormat());
            } else {
                cashbackValueTextView.setVisibility(View.GONE);
                cashbackLableTextView.setVisibility(View.GONE);
            }
        } else {
            paymentDetailsLayout.setVisibility(View.GONE);
        }

        if (rideHistory.getDiscount() > 0) {
            discountValueTextView.setText(String.format("- %s", rideHistory.getDiscountDisplayFormat()));
        } else {
            discountValueTextView.setVisibility(View.GONE);
            discountLabelTextView.setVisibility(View.GONE);
        }

        if (rideHistory.getPendingAmount() > 0) {
            pendingFareValueTextView.setText(rideHistory.getPendingAmountDisplayFormat());
            pendingFareValueTextView.setVisibility(View.VISIBLE);
            pendingFareLabelTextView.setVisibility(View.VISIBLE);
            seperator.setVisibility(View.VISIBLE);
            btnPendingFare.setVisibility(View.VISIBLE);
        } else {
            pendingFareValueTextView.setVisibility(View.GONE);
            pendingFareLabelTextView.setVisibility(View.GONE);
            seperator.setVisibility(View.GONE);
            btnPendingFare.setVisibility(View.GONE);
        }


        if (rideHistory.getDriverPictureUrl().length() > 0) {
            Glide.with(getActivity()).load(rideHistory.getDriverPictureUrl())
                    .asBitmap()
                    .centerCrop()
                    .error(R.drawable.default_user_pic_light)
                    .into(new BitmapImageViewTarget(driverPictTextView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            if (getActivity() == null) return;

                            RoundedBitmapDrawable roundedBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            driverPictTextView.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
        } else {
            driverPictTextView.setVisibility(View.GONE);
        }


        Glide.with(getActivity()).load(rideHistory.getMapImage())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.staticmap_dummy)
                .into(mapImageView);
    }

    @Override
    public void showHistoryDetailLayout() {
        topLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPickupLocationText(String sourceAddress) {
        sourceTextView.setText(sourceAddress);
    }

    @Override
    public void setDestinationLocation(String sourceAddress) {
        destinationTextView.setText(sourceAddress);
    }

    public interface OnFragmentInteractionListener {
        //void showHelpWebview(String helpUrl);
        void rideHistoryUpdated(RideHistoryViewModel viewModel);
    }

    @Override
    public RideHistoryViewModel getRideHistory() {
        return rideHistory;
    }

    @Override
    public boolean isRatingAvailable() {
        return rideHistory.getStatus().equalsIgnoreCase(RideStatus.COMPLETED) &&
                rideHistory.getRating() != null && !rideHistory.getRating().getStar().equalsIgnoreCase("0");
    }

    @Override
    public void showRatingLayout() {
        ratingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRatingLayout() {
        ratingLayout.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getRatingParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GiveDriverRatingUseCase.PARAM_COMMENT, getRateComment());
        requestParams.putString(GiveDriverRatingUseCase.PARAM_REQUEST_ID, rideHistory.getRequestId());
        requestParams.putInt(GiveDriverRatingUseCase.PARAM_STARS, getRateStars());
        return requestParams;
    }

    @Override
    public int getRateStars() {
        return Math.round(rateStarRatingBar.getRating());
    }

    private String getRateComment() {
        return rateCommentEditText.getText().toString().trim();
    }

    @Override
    public void showSuccessRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.complete_trip_rating_successfully);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void hideMainLayout() {
        rideHistoryLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMainLayout() {
        rideHistoryLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderSuccessfullGiveRating(int star) {
        ratingResult.setVisibility(View.VISIBLE);
        ratingResult.setRating(star);
    }

    @Override
    public RequestParams getSingleHistoryParam() {
        RequestParams requestParams = RequestParams.create();
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_HASH, hash);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_REQUEST_ID, requestId);
        return requestParams;
    }

    @Override
    public void showErrorLayout() {
        NetworkErrorHelper.showEmptyState(getActivity(), topLayout, getRetryListener());
    }

    @Override
    public String getMapKey() {
        return getString(R.string.GOOGLE_API_KEY);
    }

    @Override
    public String getMapSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp);
        int height = getResources().getDimensionPixelSize(R.dimen.history_map_height);

        return width + "x" + height;
    }

    @Override
    public void showRatingNetworkError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @OnClick(R2.id.layout_need_help)
    public void actionNeedHelpClicked() {
        RideGATracking.eventClickHelpTrip(getScreenName(), rideHistory.getRequestTime(), rideHistory.getTotalFare(), rideHistory.getStatus());//17
        startActivity(RideHistoryNeedHelpActivity.getCallingIntent(getActivity(), rideHistory));
    }

    @OnClick(R2.id.rate_confirmation)
    public void actionRateConfirmClicked() {
        mPresenter.actionSendRating();
    }

    @OnClick(R2.id.btn_pay_pending_fare)
    public void actionPayPendingFareClicked() {
        mPresenter.payPendingFare();
    }

    @Override
    public void setHistoryViewModelData(RideHistoryViewModel viewModel) {
        this.rideHistory = viewModel;
        mListener.rideHistoryUpdated(viewModel);
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) return;
        mProgressDialog.setMessage(getString(R.string.please_wait_message));
        mProgressDialog.show();
    }

    @Override
    public void hideProgressLoading() {
        if (mProgressDialog == null) return;
        mProgressDialog.hide();
    }

    @Override
    public void openScroogePage(String url, String postData) {
        if (getActivity() != null) {
            ScroogePGUtil.openScroogePage(this, url, true, postData, getString(R.string.title_pay_pending_fare));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null && requestCode == REQUEST_CODE_OPEN_SCROOGE_PAGE) {
            if (resultCode == ScroogePGUtil.RESULT_CODE_SUCCESS) {
                btnPendingFare.setVisibility(View.GONE);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }
}
