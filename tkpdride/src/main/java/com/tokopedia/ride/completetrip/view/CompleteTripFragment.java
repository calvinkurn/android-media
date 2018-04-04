package com.tokopedia.ride.completetrip.view;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.Receipt;
import com.tokopedia.ride.common.ride.utils.RideUtils;
import com.tokopedia.ride.completetrip.di.CompleteTripComponent;
import com.tokopedia.ride.completetrip.di.DaggerCompleteTripComponent;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.completetrip.domain.SendTipUseCase;
import com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;
import static com.tokopedia.ride.scrooge.ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteTripFragment extends BaseFragment implements CompleteTripContract.View {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_DRIVER_VEHICLE_VIEW_MODEL = "EXTRA_DRIVER_VEHICLE_VIEW_MODEL";
    private static final int TOKOCASH_PRODUCT_REQUEST_CODE = 1001;

    @BindView(R2.id.on_trip_complete_layout)
    RelativeLayout onTripCompleteLayout;
    @BindView(R2.id.layout_loader)
    RelativeLayout loaderLayout;
    @BindView(R2.id.tv_total_fare)
    TextView totalChargedTopTextView;
    @BindView(R2.id.iv_driver_pict)
    ImageView driverPictImageView;
    @BindView(R2.id.tv_driver_rating)
    TextView driverRatingTextView;
    @BindView(R2.id.tv_driver_name)
    TextView driverNameTextView;
    @BindView(R2.id.iv_vehicle_pict)
    ImageView vehiclePictImageView;
    @BindView(R2.id.tv_vehicle_license_number)
    TextView vehicleLicenseNumberTextView;
    @BindView(R2.id.tv_vehicle_desc)
    TextView vehicleDescTextView;
    @BindView(R2.id.tv_total_charged)
    TextView totalChargedTextView;
    @BindView(R2.id.tv_sign_up_uber)
    TextView signUpUberTextView;
    @BindView(R2.id.uber_signup_layout)
    LinearLayout uberSingupLayout;
    @BindView(R2.id.tv_signup_tnc)
    TextView signUpUberTermsTextView;
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
    @BindView(R2.id.rb_rate_star)
    RatingBar rateStarRatingBar;
    @BindView(R2.id.et_rate_comment)
    EditText rateCommentEditText;
    @BindView(R2.id.rate_confirmation)
    Button rateConfirmationButton;
    @BindView(R2.id.rating_layout)
    RelativeLayout ratingLayout;
    @BindView(R2.id.rating_result_layout)
    RelativeLayout ratingResultLayout;
    @BindView(R2.id.rb_rate_star_result)
    RatingBar ratingBarResult;
    @BindView(R2.id.grid_layout_tip)
    GridLayout tipLayout;
    @BindView(R2.id.tip_seperator)
    ImageView tipSeperator;

    @BindView(R2.id.pending_fare_layout)
    RelativeLayout pendingFareLayout;
    @BindView(R2.id.layout_topup)
    LinearLayout topupPendingFareLayout;
    @BindView(R2.id.tv_total_pending)
    TextView totalPendingTextView;
    @BindView(R2.id.label_total_charged)
    TextView labelAmountChargedTextView;

    @Inject
    CompleteTripPresenter presenter;
    private String requestId;
    private DriverVehicleAddressViewModel driverVehicleAddressViewModel;
    private Receipt receipt;
    private DigitalCheckoutPassData passData;
    private OnFragmentInteractionListener interactionListener;
    private int selectedTipIndex = -1;
    private ArrayList<String> tipList;
    private int tipAmount;
    private ProgressDialog progressDialog;

    public interface OnFragmentInteractionListener {
        void actionSuccessRatingSubmited();
    }

    public CompleteTripFragment() {
        // Required empty public constructor
    }

    public static CompleteTripFragment newInstance(String requestId, DriverVehicleAddressViewModel viewModel) {
        CompleteTripFragment fragment = new CompleteTripFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        bundle.putParcelable(EXTRA_DRIVER_VEHICLE_VIEW_MODEL, viewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CompleteTripFragment newInstanceFromNotif(String requestId, DriverVehicleAddressViewModel viewModel) {
        CompleteTripFragment fragment = new CompleteTripFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        bundle.putParcelable(EXTRA_DRIVER_VEHICLE_VIEW_MODEL, viewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete_trip;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialVariable();
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        CompleteTripComponent completeTripComponent = DaggerCompleteTripComponent
                .builder()
                .rideComponent(component)
                .build();
        completeTripComponent.inject(this);
    }

    private void setInitialVariable() {
        requestId = getArguments().getString(EXTRA_REQUEST_ID);
        driverVehicleAddressViewModel = getArguments().getParcelable(EXTRA_DRIVER_VEHICLE_VIEW_MODEL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitialView();
        presenter.attachView(this);
        presenter.actionGetReceipt();
    }

    private void setInitialView() {
        driverNameTextView.setText(String.valueOf(driverVehicleAddressViewModel.getDriver().getName()));
        driverRatingTextView.setText(String.valueOf(driverVehicleAddressViewModel.getDriver().getRating()));
        vehicleLicenseNumberTextView.setText(String.valueOf(driverVehicleAddressViewModel.getVehicle().getLicensePlate()));
        vehicleDescTextView.setText(String.format(
                "%s %s",
                driverVehicleAddressViewModel.getVehicle().getMake(),
                driverVehicleAddressViewModel.getVehicle().getVehicleModel())
        );

        Glide.with(getActivity()).load(driverVehicleAddressViewModel.getDriver().getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_user_pic_light)
                .into(new BitmapImageViewTarget(driverPictImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (getActivity() == null) {
                            return;
                        }

                        RoundedBitmapDrawable roundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        driverPictImageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });

        Glide.with(getActivity()).load(driverVehicleAddressViewModel.getVehicle().getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.cabs_uber_ic)
                .into(new BitmapImageViewTarget(vehiclePictImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        vehiclePictImageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });

        rateConfirmationButton.setEnabled(false);
        rateStarRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                presenter.handleRatingStarClick(v);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + OnFragmentInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + OnFragmentInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void showGetReceiptLoading() {
        loaderLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGetReceiptLoading() {
        loaderLayout.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getReceiptParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetReceiptUseCase.PARAM_REQUEST_ID, requestId);
        return requestParams;
    }

    @Override
    public void renderReceipt(final Receipt receipt, boolean isPendingPaymentExists) {
        this.receipt = receipt;
        totalChargedTextView.setText(receipt.getTotalCharged());
        totalChargedTopTextView.setText(receipt.getTotalFare());
        totalFareValueTextView.setText(receipt.getTotalFare());
        labelAmountChargedTextView.setText(receipt.getPaymentMethod() + " " + getString(R.string.label_charged));

        if (receipt.getUberSignupText() != null) {
            uberSingupLayout.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                signUpUberTextView.setText(Html.fromHtml(receipt.getUberSignupText(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                signUpUberTextView.setText(Html.fromHtml(receipt.getUberSignupText()));
            }
        } else {
            uberSingupLayout.setVisibility(View.GONE);
        }

        if (receipt.getUberSignupTermsUrl() == null || receipt.getUberSignupTermsUrl().length() == 0) {
            signUpUberTermsTextView.setVisibility(View.GONE);
        } else {
            signUpUberTermsTextView.setVisibility(View.VISIBLE);
        }

        if (receipt.getCashback() > 0) {
            cashbackValueTextView.setText(receipt.getCashbackDisplayFormat());
            cashbackLableTextView.setVisibility(View.VISIBLE);
            cashbackValueTextView.setVisibility(View.VISIBLE);
        } else {
            cashbackValueTextView.setVisibility(View.GONE);
            cashbackLableTextView.setVisibility(View.GONE);
        }

        if (receipt.getDiscount() > 0) {
            discountValueTextView.setText(String.format("- %s", receipt.getCashbackDisplayFormat()));
            discountValueTextView.setVisibility(View.VISIBLE);
            discountLabelTextView.setVisibility(View.VISIBLE);
        } else {
            discountValueTextView.setVisibility(View.GONE);
            discountLabelTextView.setVisibility(View.GONE);
        }

        if (isPendingPaymentExists) {
            totalPendingTextView.setText(receipt.getPendingPayment().getPendingAmount());
            pendingFareLayout.setVisibility(View.VISIBLE);
            topupPendingFareLayout.setVisibility(View.VISIBLE);
            ratingLayout.setVisibility(View.VISIBLE);

            //create tip buttons
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            tipList = null;
            if (receipt.getTipList() != null && receipt.getTipList().getList() != null) {
                tipList = receipt.getTipList().getFormattedCurrecyList();
            }

            if (tipList != null) {
                tipLayout.removeAllViews();
                for (int index = 0; index < tipList.size(); index++) {
                    TextView tipButton = (TextView) inflater.inflate(R.layout.layout_tip_button, null);
                    tipButton.setText(tipList.get(index));
                    tipButton.setTag(index);
                    tipButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //if last selected tip index is clicked again then un-select it.
                            if (selectedTipIndex == (int) v.getTag()) {
                                TextView button = (TextView) tipLayout.getChildAt(selectedTipIndex);
                                button.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                                setButtonBackground(button, R.drawable.shape_bg_rounded_white_rectangle);
                                tipAmount = 0;
                                selectedTipIndex = -1;
                                return;
                            }


                            selectedTipIndex = (int) v.getTag();
                            tipAmount = receipt.getTipList().getList().get(selectedTipIndex);

                            //redraw tip buttons to show selected item
                            for (int count = 0; count < tipList.size(); count++) {
                                TextView button = (TextView) tipLayout.getChildAt(count);
                                int textColor = ContextCompat.getColor(getActivity(), R.color.black);
                                int backgroundDrawableResId = R.drawable.shape_bg_rounded_white_rectangle;

                                if (selectedTipIndex == count) {
                                    textColor = ContextCompat.getColor(getActivity(), R.color.white);
                                    backgroundDrawableResId = R.drawable.shape_bg_rounded_yellow_rectangle;
                                }

                                button.setTextColor(textColor);
                                setButtonBackground(button, backgroundDrawableResId);
                            }
                        }
                    });
                    tipLayout.addView(tipButton);
                }
            }
        }
    }

    private void setButtonBackground(View view, int backgroundDrawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(getResources().getDrawable(backgroundDrawableResId));
        } else {
            view.setBackgroundDrawable(getResources().getDrawable(backgroundDrawableResId));
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showReceiptLayout() {
        onTripCompleteLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorLayout() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.actionGetReceipt();
            }
        });
    }

    @Override
    public void hideReceiptLayout() {
        //onTripCompleteLayout.setVisibility(View.GONE);
    }


    @OnClick(R2.id.uber_signup_layout)
    public void actionSignupClicked() {
        RideGATracking.eventClickSignup();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag("uber_singup_dialog");
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment dialogFragment = UberSignupDialogFragment.newInstance(this.receipt.getUberSignupUrl());
        dialogFragment.show(getFragmentManager().beginTransaction(), "uber_singup_dialog");
    }

    @OnClick(R2.id.tv_signup_tnc)
    public void actionTermsClicked() {
        RideGATracking.eventClickTNC();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag("uber_singup_dialog");
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment dialogFragment = UberSignupDialogFragment.newInstance(this.receipt.getUberSignupTermsUrl());
        dialogFragment.show(getFragmentManager().beginTransaction(), "uber_singup_dialog");
    }

    @OnClick(R2.id.rate_confirmation)
    public void actionRateConfirmClicked() {
        RideGATracking.eventClickSubmit(getRateStars(), getRateComment());
        presenter.actionSubmitRatingAndDriverTip();
    }

    @Override
    public RequestParams getRatingParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GiveDriverRatingUseCase.PARAM_COMMENT, getRateComment());
        requestParams.putString(GiveDriverRatingUseCase.PARAM_REQUEST_ID, requestId);
        requestParams.putString(GiveDriverRatingUseCase.PARAM_STARS, getRateStars());
        return requestParams;
    }

    @Override
    public void showRatingSuccessDialog() {
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
    public void showErrorInRating(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.actionSubmitRatingAndDriverTip();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showErrorInDriverTipping(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.sendTip(getTipParam());
            }
        }).showRetrySnackbar();
    }

    @Override
    public void hideRatingLayout() {
        ratingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showRatingLayout() {
        ratingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public RequestParams getRideHistoryParam() {
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
    public void clearRideNotificationIfExists() {
        RidePushNotificationBuildAndShow.cancelActiveNotification(getActivity());
    }

    @Override
    public boolean isCameFromPushNotif() {
        return getArguments().getBoolean(Constants.EXTRA_FROM_PUSH, false);
    }

    @Override
    public String getRateStars() {
        return String.valueOf(Math.round(rateStarRatingBar.getRating()));
    }

    @Override
    public void closePage() {
        interactionListener.actionSuccessRatingSubmited();
    }

    private String getRateComment() {
        return rateCommentEditText.getText().toString().trim();
    }

    @Override
    public void showRatingResultLayout(int star) {
        ratingResultLayout.setVisibility(View.VISIBLE);
        ratingBarResult.setRating(star);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (resultCode == IDigitalModuleRouter.PAYMENT_SUCCESS) {
                    if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                        String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            NetworkErrorHelper.showSnackbar(getActivity(), message);
                        }
                    }
                    pendingFareLayout.setVisibility(View.GONE);
                    ratingLayout.setVisibility(View.VISIBLE);
                } else {
                    NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.complete_trip_payment_failed));
                }

                break;

            case REQUEST_CODE_OPEN_SCROOGE_PAGE:
                if (getActivity() != null) {
                    if (resultCode == ScroogePGUtil.RESULT_CODE_SUCCESS) {
                        closePage();
                    } else {
                        NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.error_fail_pay_pending));
                    }
                }
                break;
        }
    }

    @OnClick(R2.id.btn_pay_pending_fare)
    public void actionPayPendingFare() {
        presenter.payPendingFare();
    }


    @Override
    public void showTipLayout() {
        tipSeperator.setVisibility(View.VISIBLE);
        tipLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTipLayout() {
        tipSeperator.setVisibility(View.GONE);
        tipLayout.setVisibility(View.GONE);
    }

    private String generateIdEmpotency(String requestId) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return String.format("%s_%s", requestId, token.isEmpty() ? timeMillis : token);
    }

    private Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException | NullPointerException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
    }

    @Override
    public void enableRatingSubmitButton() {
        rateConfirmationButton.setEnabled(true);
        rateConfirmationButton.setBackgroundResource(R.drawable.rounded_filled_theme_bttn);
    }

    @Override
    public void disableRatingSubmitButton() {
        rateConfirmationButton.setEnabled(false);
        rateConfirmationButton.setBackgroundResource(R.drawable.rounded_filled_theme_disable_bttn);
    }

    @Override
    public ArrayList<String> getFormmattedTipList() {
        return tipList;
    }

    @Override
    public RequestParams getTipParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SendTipUseCase.PARAM_TIP_AMOUNT, String.valueOf(getTipAmount()));
        requestParams.putString(SendTipUseCase.PARAM_REQUEST_ID, requestId);
        return requestParams;
    }

    @Override
    public int getTipAmount() {
        if (rateStarRatingBar.getRating() >= 4 && tipAmount > 0) {
            return tipAmount;
        }

        return 0;
    }

    @Override
    public void openScroogePage(String url, String postData) {
        if (getActivity() != null) {
            ScroogePGUtil.openScroogePage(this, url, true, postData, getString(R.string.title_pay_pending_fare));
        }
    }

    @Override
    public void showProgressbar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.message_please_wait));
            progressDialog.setCancelable(false);
        }

        if (getActivity() != null && progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void showAddShortcutDialog() {
        if (getActivity() == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_title_add_uber_shortcut));
        builder.setCancelable(true);

        builder.setPositiveButton(
                getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RideUtils.addUberShortcutOnLauncher(getActivity(), getString(R.string.label_book_uber_shortcut), getString(R.string.label_book_uber_shortcut));
                        RideGATracking.eventUberCreateShortcut(getScreenName());
                        presenter.setShortcutDialogIsShowninCache();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                getString(R.string.btn_maybe_later),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.setShortcutDialogIsShowninCache();
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    @Override
    public void hideProgressbar() {
        if (getActivity() != null && progressDialog != null) {
            progressDialog.hide();
        }
    }
}
