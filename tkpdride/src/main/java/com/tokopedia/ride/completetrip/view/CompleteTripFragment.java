package com.tokopedia.ride.completetrip.view;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tkpd.library.utils.CurrencyFormatHelper;
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
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.completetrip.di.CompleteTripDependencyInjection;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.completetrip.domain.model.Receipt;
import com.tokopedia.ride.completetrip.view.viewmodel.TokoCashProduct;
import com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

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
    @BindView(R2.id.tv_source)
    AppCompatTextView sourceTextView;
    @BindView(R2.id.tv_destination)
    AppCompatTextView destinationTextView;
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


    @BindView(R2.id.pending_fare_layout)
    RelativeLayout pendingFareLayout;
    @BindView(R2.id.tv_total_pending)
    TextView totalPendingTextView;
    @BindView(R2.id.layout_tokocash_option)
    RelativeLayout tokocashOptionRelativeLayout;
    @BindView(R2.id.tv_tokocash_selected_product)
    TextView tokocashSelectedProductTextView;
    @BindView(R2.id.btn_topup_tokocash)
    TextView topupButtonTextView;

    CompleteTripContract.Presenter presenter;
    private String requestId;
    private DriverVehicleAddressViewModel driverVehicleAddressViewModel;
    private Receipt receipt;
    private DigitalCheckoutPassData passData;
    private OnFragmentInteractionListener interactionListener;

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

    private void setInitialVariable() {
        presenter = CompleteTripDependencyInjection.createPresenter(getActivity());
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
        sourceTextView.setText(driverVehicleAddressViewModel.getAddress() != null ? driverVehicleAddressViewModel.getAddress().getStartAddressName() : "");
        destinationTextView.setText(driverVehicleAddressViewModel.getAddress() != null ? driverVehicleAddressViewModel.getAddress().getEndAddressName() : "");

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
    @SuppressWarnings("deprecation")
    public void renderReceipt(Receipt receipt) {
        this.receipt = receipt;
        totalChargedTextView.setText(receipt.getTotalCharged());
        totalChargedTopTextView.setText(receipt.getTotalFare());
        totalFareValueTextView.setText(receipt.getTotalFare());

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

        if (receipt.getPendingPayment() != null) {
            passData = new DigitalCheckoutPassData();
            passData.setCategoryId(receipt.getPendingPayment().getCategoryId());
            passData.setOperatorId(receipt.getPendingPayment().getOperatorId());
            Map<String, String> maps = splitQuery(Uri.parse(receipt.getPendingPayment().getTopupUrl()));
            if (maps.get(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN) != null)
                passData.setUtmCampaign(maps.get(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN));
            if (maps.get(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER) != null)
                passData.setClientNumber(maps.get(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER));
            if (maps.get(DigitalCheckoutPassData.PARAM_UTM_SOURCE) != null)
                passData.setUtmSource(maps.get(DigitalCheckoutPassData.PARAM_UTM_SOURCE));
            if (maps.get(DigitalCheckoutPassData.PARAM_UTM_CONTENT) != null)
                passData.setUtmContent(maps.get(DigitalCheckoutPassData.PARAM_UTM_CONTENT));
            if (maps.get(DigitalCheckoutPassData.PARAM_IS_PROMO) != null)
                passData.setIsPromo(maps.get(DigitalCheckoutPassData.PARAM_IS_PROMO));
            if (maps.get(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT) != null)
                passData.setInstantCheckout(maps.get(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT));

            pendingFareLayout.setVisibility(View.VISIBLE);
            totalPendingTextView.setText(CurrencyFormatHelper.ConvertToRupiah(receipt.getPendingPayment().getPendingAmount()));

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
        presenter.actionSendRating();
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
    public void showRatingErrorLayout() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.actionSendRating();
            }
        });
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
            case TOKOCASH_PRODUCT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    TokoCashProduct product = data.getParcelableExtra(PendingFareChooserActivity.EXTRA_PRODUCT);
                    tokocashSelectedProductTextView.setText(product.getTitle());
                    passData.setProductId(product.getId());
                }
                break;
        }
    }

    @OnClick(R2.id.pending_fare_layout)
    public void actionFareLayout() {
        List<TokoCashProduct> products = transformProductsFromJson(receipt.getPendingPayment().getTopUpOptions(), receipt.getCurrency());
        startActivityForResult(PendingFareChooserActivity.getCallingIntent(getActivity(), products), TOKOCASH_PRODUCT_REQUEST_CODE);
    }

    @OnClick(R2.id.btn_topup_tokocash)
    public void actionTopupTokocash() {
        if (passData == null) return;
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            passData.setIdemPotencyKey(generateIdEmpotency(receipt.getRequestId()));
            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(passData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
    }

    private String generateIdEmpotency(String requestId) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return String.format("%s_%s", requestId, token.isEmpty() ? timeMillis : token);
    }

    private List<TokoCashProduct> transformProductsFromJson(String jsonStr, String prefixCurrency) {
        List<TokoCashProduct> products = new ArrayList<>();
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(receipt.getPendingPayment().getTopUpOptions());
                Iterator<?> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    TokoCashProduct product = new TokoCashProduct();
                    String key = (String) keys.next();
                    product.setValue(key);
                    JSONObject itemJsonObject = new JSONObject(jsonObject.getString(key));
                    product.setId(itemJsonObject.getString("product_id"));
                    product.setTitle(String.format("%s %s", prefixCurrency, itemJsonObject.getString("display_price")));
                    products.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return products;
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
}
