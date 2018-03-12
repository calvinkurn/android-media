package com.tokopedia.flight.booking.view.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.booking.view.activity.FlightBookingPassengerActivity;
import com.tokopedia.flight.booking.view.activity.FlightBookingPhoneCodeActivity;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerActionListener;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerAdapter;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerAdapterTypeFactory;
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
import com.tokopedia.flight.common.constant.FlightFlowConstant;
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.common.util.FlightFlowUtil;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.review.view.activity.FlightBookingReviewActivity;
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingFragment extends BaseDaggerFragment implements FlightBookingContract.View, FlightBookingPassengerActionListener {
    private static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";
    private static final String EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID";
    private static final String EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID";
    private static final String INTERRUPT_DIALOG_TAG = "interrupt_dialog";
    private static final String KEY_CART_DATA = "KEY_CART_DATA";
    private static final String KEY_PARAM_VIEW_MODEL_DATA = "KEY_PARAM_VIEW_MODEL_DATA";

    private static final int REQUEST_CODE_PASSENGER = 1;
    private static final int REQUEST_CODEP_PHONE_CODE = 2;
    private static final int REQUEST_CODE_NEW_PRICE_DIALOG = 3;
    private static final int REQUEST_CODE_REVIEW = 4;
    private static final int REQUEST_CODE_OTP = 5;
    @Inject
    FlightBookingPresenter presenter;
    private LinearLayout fullPageLoadingLayout;
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
    private ProgressDialog progressDialog;
    private LinearLayout sameAsContactContainer;
    private CheckBox sameAsContactCheckbox;
    private AppCompatTextView tvPhoneCountryCode;
    private RecyclerView pricelistsRecyclerView;
    private AppCompatEditText etPhoneNumber;

    private FlightSimpleAdapter priceListAdapter;

    private String departureTripId, returnTripId;
    private FlightBookingParamViewModel paramViewModel;
    private FlightBookingCartData flightBookingCartData;
    private FlightBookingPassengerAdapter adapter;
    private String contactBirthdate;
    private int contactGender;

    public FlightBookingFragment() {
        // Required empty public constructor
    }

    public static FlightBookingFragment newInstance(FlightSearchPassDataViewModel searchPassDataViewModel, String departureId, String returnId) {
        FlightBookingFragment fragment = new FlightBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SEARCH_PASS_DATA, searchPassDataViewModel);
        bundle.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        bundle.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = (savedInstanceState != null) ? savedInstanceState : getArguments();

        departureTripId = arguments.getString(EXTRA_FLIGHT_DEPARTURE_ID);
        returnTripId = arguments.getString(EXTRA_FLIGHT_ARRIVAL_ID);
        paramViewModel = new FlightBookingParamViewModel();
        paramViewModel.setSearchParam((FlightSearchPassDataViewModel) arguments.getParcelable(EXTRA_SEARCH_PASS_DATA));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.flight_booking_loading_title));
        progressDialog.setCancelable(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_SEARCH_PASS_DATA, paramViewModel.getSearchParam());
        outState.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureTripId);
        outState.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnTripId);
        outState.putParcelable(KEY_CART_DATA, flightBookingCartData);
        outState.putParcelable(KEY_PARAM_VIEW_MODEL_DATA, paramViewModel);
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
        fullPageLoadingLayout = (LinearLayout) view.findViewById(R.id.full_page_loading);
        fullPageLayout = (NestedScrollView) view.findViewById(R.id.container_full_page);
        sameAsContactContainer = view.findViewById(R.id.container_same_as_contact);
        sameAsContactCheckbox = view.findViewById(R.id.checkbox);

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

//        sameAsContactContainer.setOnClickListener(getCheckboxClickListener());
//
//        sameAsContactCheckbox.setOnClickListener(getCheckboxClickListener());

        initializePassengerInfo();
        initializePriceList();
        return view;
    }

    private void initializePassengerInfo() {
        FlightBookingPassengerAdapterTypeFactory adapterTypeFactory = new FlightBookingPassengerAdapterTypeFactory(this);
        adapter = new FlightBookingPassengerAdapter(adapterTypeFactory, new ArrayList<Visitable>());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        passengerRecyclerView.setLayoutManager(layoutManager);
        passengerRecyclerView.setHasFixedSize(true);
        passengerRecyclerView.setNestedScrollingEnabled(false);
        passengerRecyclerView.setAdapter(adapter);
    }

    private void initializePriceList() {
        priceListAdapter = new FlightSimpleAdapter();
        priceListAdapter.setDescriptionTextColor(getResources().getColor(R.color.font_black_secondary_54));
        LinearLayoutManager flightSimpleAdapterLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        pricelistsRecyclerView.setLayoutManager(flightSimpleAdapterLayoutManager);
        pricelistsRecyclerView.setHasFixedSize(true);
        pricelistsRecyclerView.setNestedScrollingEnabled(false);
        pricelistsRecyclerView.setAdapter(priceListAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        if (savedInstanceState == null) {
            presenter.processGetCartData();
        } else {
            flightBookingCartData = savedInstanceState.getParcelable(KEY_CART_DATA);
            paramViewModel = savedInstanceState.getParcelable(KEY_PARAM_VIEW_MODEL_DATA);
            hideFullPageLoading();
            presenter.renderUi(flightBookingCartData, true);
        }

        presenter.onGetProfileData();
        presenter.initialize();
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
        String departureDate;
        if (!paramViewModel.getSearchParam().getReturnDate().equals("") && paramViewModel.getSearchParam().getReturnDate() != null) {
            departureDate = paramViewModel.getSearchParam().getReturnDate();
        } else {
            departureDate = paramViewModel.getSearchParam().getDepartureDate();
        }

        presenter.onChangePassengerButtonClicked(viewModel, departureDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_PASSENGER:
                if (resultCode == Activity.RESULT_OK) {
                    // hideSameAsContactContainer();
                    FlightBookingPassengerViewModel passengerViewModel = data.getParcelableExtra(FlightBookingPassengerActivity.EXTRA_PASSENGER);
                    presenter.onPassengerResultReceived(passengerViewModel);
                }
                break;
            case REQUEST_CODEP_PHONE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    FlightBookingPhoneCodeViewModel phoneCodeViewModel = data.getParcelableExtra(FLightBookingPhoneCodeFragment.EXTRA_SELECTED_PHONE_CODE);
                    presenter.onPhoneCodeResultReceived(phoneCodeViewModel);
                }
                break;
            case REQUEST_CODE_NEW_PRICE_DIALOG:
                if (resultCode != Activity.RESULT_OK) {
                    FlightFlowUtil.actionSetResultAndClose(getActivity(),
                            getActivity().getIntent(),
                            FlightFlowConstant.PRICE_CHANGE
                    );
                }

                break;
            case REQUEST_CODE_REVIEW:
                boolean isCountdownRestarted = false;
                if (data != null) {
                    if (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, -1) != -1) {
                        FlightFlowUtil.actionSetResultAndClose(getActivity(),
                                getActivity().getIntent(),
                                data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)
                        );
                    } else {
                        if (data.getBooleanExtra(FlightBookingReviewFragment.EXTRA_NEED_TO_REFRESH, false)) {
                            isCountdownRestarted = true;
                            presenter.onUpdateCart();
                        }
                    }
                }
                if (!isCountdownRestarted) countdownFinishTransactionView.start();
                break;
            case REQUEST_CODE_OTP:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.onReceiveOtpSuccessResult();
                } else {
                    presenter.onReceiveOtpCancelResult();
                }
        }
    }

    @Override
    public String getContactName() {
        return etContactName.getText().toString().trim();
    }

    @Override
    public void setContactName(String fullname) {
        etContactName.setText(fullname);
    }

    @Override
    public void showContactNameEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showContactNameInvalidError(int resId) {
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
    public void showContactPhoneNumberInvalidError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerInfoNotFullfilled(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void navigateToPassengerInfoDetail(FlightBookingPassengerViewModel viewModel,
                                              boolean isAirAsiaAirlines, String departureDate,
                                              String requestId) {
        startActivityForResult(
                FlightBookingPassengerActivity.getCallingIntent(
                        getActivity(),
                        getDepartureTripId(),
                        getReturnTripId(),
                        viewModel,
                        flightBookingCartData.getLuggageViewModels(),
                        flightBookingCartData.getMealViewModels(),
                        isAirAsiaAirlines,
                        departureDate,
                        requestId
                ),
                REQUEST_CODE_PASSENGER
        );
    }

    @Override
    public Observable<ProfileInfo> getProfileObservable() {
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getProfile() != null) {
            return ((FlightModuleRouter) getActivity().getApplication())
                    .getProfile();
        }
        return Observable.empty();
    }

    @Override
    public FlightBookingParamViewModel getCurrentBookingParamViewModel() {
        return paramViewModel;
    }

    @Override
    public void showAndRenderReturnTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightDetailViewModel returnTrip) {
        returnInfoView.setVisibility(View.VISIBLE);
        returnInfoView.setContent(returnTrip.getDepartureAirportCity() + " - " + returnTrip.getArrivalAirportCity());
        returnInfoView.setContentInfo("(" + FlightDateUtil.formatToUi(searchParam.getReturnDate()) + ")");
        String airLineSection = "";
        String tripInfo = "";
        if (returnTrip.getAirlineDataList() != null) {
            if (returnTrip.getAirlineDataList().size() == 1) {
                airLineSection = returnTrip.getAirlineDataList().get(0).getShortName();
            } else {
                airLineSection = getString(R.string.flight_booking_multiple_airline_trip_card);
            }
        }
        if (returnTrip.getRouteList().size() > 1) {
            tripInfo += String.format(getString(R.string.flight_booking_trip_info_format), returnTrip.getRouteList().size() - 1, getString(R.string.flight_booking_transit_trip_card));
        } else {
            tripInfo += String.format(getString(R.string.flight_booking_trip_info_format_without_count), getString(R.string.flight_booking_directly_trip_card));
        }
        returnInfoView.setSubContent(airLineSection);
        tripInfo += " " + String.format(getString(R.string.flight_booking_trip_info_airport_format), returnTrip.getDepartureTime(), returnTrip.getArrivalTime());
        returnInfoView.setSubContentInfo(tripInfo);
    }

    @Override
    public void showAndRenderDepartureTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightDetailViewModel departureTrip) {
        departureInfoView.setVisibility(View.VISIBLE);
        departureInfoView.setContent(departureTrip.getDepartureAirportCity() + " - " + departureTrip.getArrivalAirportCity());
        departureInfoView.setContentInfo("(" + FlightDateUtil.formatToUi(searchParam.getDepartureDate()) + ")");
        String airLineSection = "";
        String tripInfo = "";
        if (departureTrip.getAirlineDataList() != null) {
            if (departureTrip.getAirlineDataList().size() == 1) {
                airLineSection = departureTrip.getAirlineDataList().get(0).getShortName();
            } else {
                airLineSection = getString(R.string.flight_booking_multiple_airline_trip_card);
            }
        }
        if (departureTrip.getRouteList().size() > 1) {
            tripInfo += String.format(getString(R.string.flight_booking_trip_info_format), departureTrip.getRouteList().size() - 1, getString(R.string.flight_booking_transit_trip_card));
        } else {
            tripInfo += String.format(getString(R.string.flight_booking_trip_info_format_without_count), getString(R.string.flight_booking_directly_trip_card));
        }
        departureInfoView.setSubContent(airLineSection);
        tripInfo += " " + String.format(getString(R.string.flight_booking_trip_info_airport_format), departureTrip.getDepartureTime(), departureTrip.getArrivalTime());
        departureInfoView.setSubContentInfo(tripInfo);
    }

    @Override
    public void renderPassengersList(List<FlightBookingPassengerViewModel> passengerViewModels) {
        adapter.clearAllElements();
        adapter.addElement(passengerViewModels);
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
    public void navigateToDetailTrip(FlightDetailViewModel departureTrip) {
        startActivity(FlightDetailActivity.createIntent(getActivity(), departureTrip, false));
    }

    @Override
    public String getIdEmpotencyKey(String tokenId) {
        return generateIdEmpotency(tokenId);
    }

    @Override
    public void showFullPageLoading() {
        fullPageLoadingLayout.setVisibility(View.VISIBLE);
        fullPageLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideFullPageLoading() {
        fullPageLoadingLayout.setVisibility(View.GONE);
        fullPageLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCartData(FlightBookingCartData flightBookingCartData) {
        this.flightBookingCartData = flightBookingCartData;
    }

    @Override
    public FlightBookingCartData getCurrentCartPassData() {
        return flightBookingCartData;
    }

    @Override
    public void renderPriceListDetails(List<SimpleViewModel> prices) {
        paramViewModel.setPriceListDetails(prices);
        priceListAdapter.setViewModels(prices);
        priceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderTotalPrices(String totalPrice) {
        priceTotalAppCompatTextView.setText(totalPrice);
    }

    @Override
    public void showGetCartDataErrorStateLayout(Throwable t) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), FlightErrorUtil.getMessageFromException(getActivity(), t),
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
        countdownFinishTransactionView.cancel();
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
                        FlightFlowUtil.actionSetResultAndClose(getActivity(),
                                getActivity().getIntent(),
                                FlightFlowConstant.EXPIRED_JOURNEY
                        );
                    }
                });
        dialog.setCancelable(false);
        dialog.create().show();
    }

    @Override
    public void setCartId(String id) {
        flightBookingCartData.setId(id);
        getCurrentBookingParamViewModel().setId(id);
    }

    @Override
    public void showPriceChangesDialog(String newTotalPrice, String oldTotalPrice) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment previousDialog = getFragmentManager().findFragmentByTag(INTERRUPT_DIALOG_TAG);
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment dialogFragment = FlightBookingNewPriceDialogFragment.newInstance(newTotalPrice, oldTotalPrice);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_NEW_PRICE_DIALOG);
        dialogFragment.show(getFragmentManager().beginTransaction(), INTERRUPT_DIALOG_TAG);
    }

    @Override
    public FlightDetailViewModel getDepartureFlightDetailViewModel() {
        return flightBookingCartData.getDepartureTrip();
    }

    @Override
    public FlightDetailViewModel getReturnFlightDetailViewModel() {
        return flightBookingCartData.getReturnTrip();
    }

    @Override
    public List<FlightBookingPassengerViewModel> getFlightBookingPassengers() {
        return paramViewModel.getPassengerViewModels();
    }

    private String generateIdEmpotency(String requestId) {
        String userId = String.valueOf(Math.random());
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            UserSession userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
            if (userSession != null) {
                userId += userSession.getUserId();
            }
        }
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = FlightRequestUtil.md5(timeMillis);
        return userId + String.format(getString(R.string.flight_booking_id_empotency_format), requestId, token.isEmpty() ? timeMillis : token);
    }

    private void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void navigateToReview(FlightBookingReviewModel flightBookingReviewModel) {
        countdownFinishTransactionView.cancel();
        startActivityForResult(FlightBookingReviewActivity.createIntent(getActivity(), flightBookingReviewModel), REQUEST_CODE_REVIEW);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void showUpdatePriceLoading() {
        progressDialog.show();
    }

    @Override
    public void hideUpdatePriceLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showUpdateDataErrorStateLayout(Throwable t) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), FlightErrorUtil.getMessageFromException(getActivity(), t),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.onFinishTransactionTimeReached();
                    }
                }
        );
    }

    @Override
    public void setContactEmail(String email) {
        etContactEmail.setText(email);
    }

    @Override
    public void setContactPhoneNumber(String phone) {
        etPhoneNumber.setText(phone);
    }

    @Override
    public void setContactBirthdate(String birthdate) {
        contactBirthdate = birthdate;
    }

    @Override
    public String getContactBirthdate() {
        return contactBirthdate;
    }

    @Override
    public void setContactGender(int gender) {
        contactGender = gender;
    }

    @Override
    public int getContactGender() {
        return contactGender;
    }

    @Override
    public void showContactEmailInvalidSymbolError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void navigateToOtpPage() {
        if (getActivity().getApplication() instanceof FlightModuleRouter) {
            Intent intent = ((FlightModuleRouter) getActivity().getApplication()).getPhoneVerifIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_OTP);
        }
    }

    @Override
    public void closePage() {
        getActivity().finish();
    }

    public void onBackPressed() {
        presenter.deleteAllPassengerList();
    }

    @Override
    public void setSameAsContactChecked(boolean isChecked) {
        // ((CompoundButton) sameAsContactCheckbox).setChecked(isChecked);
    }

//    private View.OnClickListener getCheckboxClickListener() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.toggleSameAsContactCheckbox();
//                presenter.onSameAsContactClicked(true);
//            }
//        };
//    }

    private void hideSameAsContactContainer() {
        sameAsContactContainer.setVisibility(View.GONE);
    }
}
