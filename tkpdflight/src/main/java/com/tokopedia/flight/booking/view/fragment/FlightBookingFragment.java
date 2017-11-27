package com.tokopedia.flight.booking.view.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.utils.snackbar.SnackbarManager;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.activity.FlightBookingPassengerActivity;
import com.tokopedia.flight.booking.view.activity.FlightBookingPhoneCodeActivity;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerAdapter;
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.booking.widget.CardWithActionView;
import com.tokopedia.flight.booking.widget.CountdownTimeView;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.review.view.activity.FlightBookingReviewActivity;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingFragment extends BaseDaggerFragment implements FlightBookingContract.View, FlightBookingPassengerAdapter.OnClickListener {
    private static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";
    private static final String EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID";
    private static final String EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID";

    private static final int REQUEST_CODE_PASSENGER = 1;
    private static final int REQUEST_CODEP_PHONE_CODE = 2;

    private ProgressBar fullPageProgressBar;
    private NestedScrollView fullPageLayout;
    private CountdownTimeView countdownFinishTransactionView;
    private AppCompatTextView priceTotalAppCompatTextView;
    private CardWithActionView departureInfoView;
    private CardWithActionView returnInfoView;
    private RecyclerView passengerRecyclerView;
    private AppCompatButton submitButton;
    private TkpdHintTextInputLayout tilContactName;
    private AppCompatEditText etContactName;
    private TkpdHintTextInputLayout tilContactEmail;
    private AppCompatEditText etContactEmail;
    private RelativeLayout contactPhoneNumberLayout;
    private AppCompatTextView contactPhoneNumberLabel;
    private AppCompatTextView tvPhoneCountryCode;
    private AppCompatEditText etPhoneNumber;
    private RecyclerView pricelistsRecyclerView;


    private String departureTripId, returnTripId;
    private FlightBookingParamViewModel paramViewModel;
    private FlightBookingCartData flightBookingCartData;

    @Inject
    FlightBookingPresenter presenter;

    private FlightBookingPassengerAdapter adapter;
    private ProgressDialog progressDialog;
    private FlightSimpleAdapter priceListAdapter;


    public static Fragment newInstance(FlightSearchPassDataViewModel searchPassDataViewModel, String departureId, String returnId) {
        FlightBookingFragment fragment = new FlightBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SEARCH_PASS_DATA, searchPassDataViewModel);
        bundle.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        bundle.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public FlightBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        departureTripId = getArguments().getString(EXTRA_FLIGHT_DEPARTURE_ID);
        returnTripId = getArguments().getString(EXTRA_FLIGHT_ARRIVAL_ID);
        paramViewModel = new FlightBookingParamViewModel();
        paramViewModel.setSearchParam((FlightSearchPassDataViewModel) getArguments().getParcelable(EXTRA_SEARCH_PASS_DATA));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_booking, container, false);
        countdownFinishTransactionView = (CountdownTimeView) view.findViewById(R.id.countdown_finish_transaction);
        priceTotalAppCompatTextView = (AppCompatTextView) view.findViewById(R.id.tv_total_price);
        departureInfoView = (CardWithActionView) view.findViewById(R.id.cwa_departure_info);
        returnInfoView = (CardWithActionView) view.findViewById(R.id.cwa_return_info);
        passengerRecyclerView = (RecyclerView) view.findViewById(R.id.rv_passengers);
        submitButton = (AppCompatButton) view.findViewById(R.id.button_submit);
        tilContactName = (TkpdHintTextInputLayout) view.findViewById(R.id.til_contact_name);
        etContactName = (AppCompatEditText) view.findViewById(R.id.et_contact_name);
        tilContactEmail = (TkpdHintTextInputLayout) view.findViewById(R.id.til_contact_email);
        etContactEmail = (AppCompatEditText) view.findViewById(R.id.et_contact_email);
        contactPhoneNumberLayout = (RelativeLayout) view.findViewById(R.id.contact_phone_number_layout);
        contactPhoneNumberLabel = (AppCompatTextView) view.findViewById(R.id.contact_phone_number_label);
        tvPhoneCountryCode = (AppCompatTextView) view.findViewById(R.id.et_phone_country_code);
        etPhoneNumber = (AppCompatEditText) view.findViewById(R.id.et_phone_number);
        pricelistsRecyclerView = (RecyclerView) view.findViewById(R.id.rv_price_lists);
        fullPageProgressBar = (ProgressBar) view.findViewById(R.id.pb_full_page);
        fullPageLayout = (NestedScrollView) view.findViewById(R.id.container_full_page);

        tvPhoneCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(FlightBookingPhoneCodeActivity.getCallingIntent(getActivity()), REQUEST_CODEP_PHONE_CODE);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonSubmitClicked();
            }
        });

        adapter = new FlightBookingPassengerAdapter();
        adapter.setListener(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        passengerRecyclerView.setLayoutManager(layoutManager);
        passengerRecyclerView.setHasFixedSize(true);
        passengerRecyclerView.setNestedScrollingEnabled(false);
        passengerRecyclerView.setAdapter(adapter);

        departureInfoView.setActionListener(new CardWithActionView.ActionListener() {
            @Override
            public void actionClicked() {
                presenter.onDepartureInfoClicked();
            }
        });

        returnInfoView.setActionListener(new CardWithActionView.ActionListener() {
            @Override
            public void actionClicked() {
                presenter.onReturnInfoClicked();
            }
        });

        priceListAdapter = new FlightSimpleAdapter();
        LinearLayoutManager flightSimpleAdapterLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        pricelistsRecyclerView.setLayoutManager(flightSimpleAdapterLayoutManager);
        pricelistsRecyclerView.setHasFixedSize(true);
        pricelistsRecyclerView.setNestedScrollingEnabled(false);
        pricelistsRecyclerView.setAdapter(priceListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.processGetCartData();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class).inject(this);
    }

    @Override
    public void onChangePassengerData(FlightBookingPassengerViewModel viewModel) {
        startActivityForResult(
                FlightBookingPassengerActivity.getCallingIntent(
                        getActivity(),
                        getDepartureTripId(),
                        getReturnTripId(),
                        viewModel,
                        flightBookingCartData.getLuggageViewModels(),
                        flightBookingCartData.getMealViewModels()
                ),
                REQUEST_CODE_PASSENGER
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PASSENGER:
                    FlightBookingPassengerViewModel passengerViewModel = data.getParcelableExtra(FlightBookingPassengerActivity.EXTRA_PASSENGER);
                    presenter.onPassengerResultReceived(passengerViewModel);
                    break;
                case REQUEST_CODEP_PHONE_CODE:
                    FlightBookingPhoneCodeViewModel phoneCodeViewModel = data.getParcelableExtra(FLightBookingPhoneCodeFragment.EXTRA_SELECTED_PHONE_CODE);
                    presenter.onPhoneCodeResultReceived(phoneCodeViewModel);
                    break;
            }
        }
    }

    @Override
    public String getContactName() {
        return etContactName.getText().toString().trim();
    }

    @Override
    public void showContactNameEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public String getContactEmail() {
        return etContactEmail.getText().toString().trim();
    }

    @Override
    public void showContactEmailEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showContactEmailInvalidError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public String getContactPhoneNumber() {
        return etPhoneNumber.getText().toString().trim();
    }

    @Override
    public void showContactPhoneNumberEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public FlightBookingParamViewModel getCurrentBookingParamViewModel() {
        return paramViewModel;
    }

    @Override
    public void showAndRenderReturnTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightSearchViewModel returnTrip) {
        returnInfoView.setVisibility(View.VISIBLE);
        returnInfoView.setContent(returnTrip.getDepartureAirportName() + "-" + returnTrip.getArrivalAirportName());
        returnInfoView.setContentInfo(searchParam.getDepartureDate());
        String airLineSection = "";
        boolean isTransit = false;
        if (returnTrip.getAirlineList().size() > 1) {
            isTransit = true;
            airLineSection = getString(R.string.flight_booking_multiple_airline_trip_card);
        } else {
            airLineSection = returnTrip.getAirlineList().get(0).getName();
        }
        returnInfoView.setSubContent(airLineSection);

        String tripInfo = "";
        if (isTransit) {
            tripInfo += String.format(" | %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_transit_trip_card));
        } else {
            tripInfo += String.format(" | %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_directly_trip_card));
        }
        tripInfo += String.format("| %s %s - %s %s", returnTrip.getDepartureTime(), returnTrip.getDepartureAirport(), returnTrip.getArrivalTime(), returnTrip.getArrivalAirport());
        returnInfoView.setSubContentInfo(tripInfo);
    }

    @Override
    public void showAndRenderDepartureTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightSearchViewModel returnTrip) {
        departureInfoView.setVisibility(View.VISIBLE);
        departureInfoView.setContent(returnTrip.getDepartureAirportName() + "-" + returnTrip.getArrivalAirportName());
        departureInfoView.setContentInfo(FlightDateUtil.formatToUi(searchParam.getDepartureDate()));
        String airLineSection = "";
        boolean isTransit = false;
        if (returnTrip.getAirlineList().size() > 1) {
            isTransit = true;
            airLineSection = getString(R.string.flight_booking_multiple_airline_trip_card);
        } else {
            airLineSection = returnTrip.getAirlineList().get(0).getName();
        }
        departureInfoView.setSubContent(airLineSection);

        String tripInfo = "";
        if (isTransit) {
            tripInfo += String.format(" | %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_transit_trip_card));
        } else {
            tripInfo += String.format(" | %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_directly_trip_card));
        }
        tripInfo += String.format(" | %s %s - %s %s", returnTrip.getDepartureTime(), returnTrip.getDepartureAirport(), returnTrip.getArrivalTime(), returnTrip.getArrivalAirport());
        departureInfoView.setSubContentInfo(tripInfo);
    }

    @Override
    public void renderPassengersList(List<FlightBookingPassengerViewModel> passengerViewModels) {
        adapter.addPassengers(passengerViewModels);
    }

    @Override
    public void renderPhoneCodeView(String countryPhoneCode) {
        tvPhoneCountryCode.setText(countryPhoneCode);
    }

    @Override
    public String getDepartureTripId() {
        return departureTripId;
    }

    @Override
    public String getReturnTripId() {
        return returnTripId;
    }

    @Override
    public void navigateToDetailTrip(FlightSearchViewModel departureTrip) {
        FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel();
        flightDetailViewModel.build(departureTrip);
        flightDetailViewModel.build(paramViewModel.getSearchParam());
        startActivity(FlightDetailActivity.createIntent(getActivity(), flightDetailViewModel));
    }

    @Override
    public String getIdEmpotencyKey(String tokenId) {
        return generateIdEmpotency(tokenId);
    }

    @Override
    public void renderLuggageList(List<Amenity> amenities) {

    }

    @Override
    public void showFullPageLoading() {
        fullPageProgressBar.setVisibility(View.VISIBLE);
        fullPageLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideFullPageLoading() {
        fullPageProgressBar.setVisibility(View.GONE);
        fullPageLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderCartData(FlightBookingCartData flightBookingCartData) {
        this.flightBookingCartData = flightBookingCartData;
    }

    @Override
    public FlightBookingCartData getCurrentCartPassData() {
        return flightBookingCartData;
    }

    @Override
    public void getRenderDeparturePrice(List<SimpleViewModel> prices) {
        priceListAdapter.addViewModels(prices);
        priceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void getRenderReturnPrice(List<SimpleViewModel> prices) {
        priceListAdapter.addViewModels(prices);
        priceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderTotalPrices(String totalPrice) {
        priceTotalAppCompatTextView.setText(totalPrice);
    }

    @Override
    public void showGetCartDataErrorStateLayout() {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.onRetryGetCartData();
                    }
                }
        );
    }

    @Override
    public void renderFinishTimeCountDown(Date date) {
        countdownFinishTransactionView.setListener(new CountdownTimeView.OnActionListener() {
            @Override
            public void onFinished() {
                presenter.onFinishTransactionTimeReached();
            }
        });
        countdownFinishTransactionView.setExpiredDate(date);
        countdownFinishTransactionView.start();
    }

    @Override
    public void showExpireTransactionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(R.string.flight_booking_expired_booking_label);
        dialog.setPositiveButton(getActivity().getString(R.string.title_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().setResult(Activity.RESULT_CANCELED);
                        getActivity().finish();
                    }
                });
        dialog.setCancelable(false);
        dialog.create().show();
    }

    private String generateIdEmpotency(String requestId) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = FlightRequestUtil.md5(timeMillis);
        return String.format("%s_%s", requestId, token.isEmpty() ? timeMillis : token);
    }

    private void showMessageErrorInSnackBar(int resId) {
        Snackbar snackBar = SnackbarManager.make(getActivity(),
                getString(resId), Snackbar.LENGTH_LONG)
                .setAction("Tutup", null);
        Button snackBarAction = (Button) snackBar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        snackBar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_500));
        snackBar.show();
    }

    @Override
    public void navigateToReview(FlightBookingReviewModel flightBookingReviewModel) {
        startActivity(FlightBookingReviewActivity.createIntent(getActivity(), flightBookingReviewModel));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }
}
