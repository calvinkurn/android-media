package com.tokopedia.flight.dashboard.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity;
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.banner.view.adapter.FlightBannerPagerAdapter;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.activity.FlightClassesActivity;
import com.tokopedia.flight.dashboard.view.activity.FlightSelectPassengerActivity;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.dashboard.view.presenter.FlightDashboardContract;
import com.tokopedia.flight.dashboard.view.presenter.FlightDashboardPresenter;
import com.tokopedia.flight.dashboard.view.widget.TextInputView;
import com.tokopedia.flight.search.view.activity.FlightSearchActivity;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nathan on 10/19/17.
 *         modified by al
 */

public class FlightDashboardFragment extends BaseDaggerFragment implements FlightDashboardContract.View {

    public static final String EXTRA_TRIP = "EXTRA_TRIP";
    public static final String EXTRA_CLASS = "EXTRA_CLASS";
    private static final String EXTRA_ADULT = "EXTRA_ADULT";
    private static final String EXTRA_CHILD = "EXTRA_CHILD";
    private static final String EXTRA_INFANT = "EXTRA_INFANT";
    private static final int REQUEST_CODE_AIRPORT_DEPARTURE = 1;
    private static final int REQUEST_CODE_AIRPORT_ARRIVAL = 2;
    private static final int REQUEST_CODE_AIRPORT_PASSENGER = 3;
    private static final int REQUEST_CODE_AIRPORT_CLASSES = 4;
    private static final int REQUEST_CODE_SEARCH = 5;
    private static final int REQUEST_CODE_LOGIN = 6;
    AppCompatImageView reverseAirportImageView;
    LinearLayout airportDepartureLayout;
    AppCompatTextView airportDepartureTextInputView;
    AppCompatTextView airportArrivalTextInputView;
    LinearLayout airportArrivalLayout;
    TextInputView passengerTextInputView;
    TextInputView classTextInputView;
    TextInputView departureDateTextInputView;
    TextInputView returnDateTextInputView;
    AppCompatButton oneWayTripAppCompatButton;
    AppCompatButton roundTripAppCompatButton;
    View returnDateSeparatorView;
    View bannerLayout;
    BannerView bannerView;
    List<BannerDetail> bannerList;

    @Inject
    FlightDashboardPresenter presenter;
    private FlightDashboardViewModel viewModel;

    public static FlightDashboardFragment getInstance() {
        return new FlightDashboardFragment();
    }

    public static FlightDashboardFragment getInstance(String extrasTrip, String extrasAdultPassenger, String extrasChildPassenger, String extrasInfantPassenger, String extrasClass) {
        FlightDashboardFragment flightDashboardFragment = new FlightDashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TRIP, extrasTrip);
        bundle.putString(EXTRA_ADULT, extrasAdultPassenger);
        bundle.putString(EXTRA_CHILD, extrasChildPassenger);
        bundle.putString(EXTRA_INFANT, extrasInfantPassenger);
        bundle.putString(EXTRA_CLASS, extrasClass);
        flightDashboardFragment.setArguments(bundle);
        return flightDashboardFragment;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightDashboardComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flight_dashboard, container, false);
        AppCompatButton searchTicketButton = (AppCompatButton) view.findViewById(R.id.button_search_ticket);
        oneWayTripAppCompatButton = (AppCompatButton) view.findViewById(R.id.button_one_way_trip);
        roundTripAppCompatButton = (AppCompatButton) view.findViewById(R.id.button_round_trip);
        reverseAirportImageView = (AppCompatImageView) view.findViewById(R.id.image_reverse_airport);
        airportDepartureLayout = (LinearLayout) view.findViewById(R.id.departure_airport_layout);
        airportArrivalLayout = (LinearLayout) view.findViewById(R.id.arrival_airport_layout);
        airportDepartureTextInputView = (AppCompatTextView) view.findViewById(R.id.tv_departure_airport_label);
        airportArrivalTextInputView = (AppCompatTextView) view.findViewById(R.id.tv_arrival_airport_label);
        passengerTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_passenger);
        classTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_class);
        departureDateTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_date_departure);
        returnDateTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_date_return);
        returnDateSeparatorView = view.findViewById(R.id.separator_date_return);
        bannerLayout = view.findViewById(R.id.banner_layout);
        bannerView = view.findViewById(R.id.banner);

        oneWayTripAppCompatButton.setSelected(true);
        oneWayTripAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSingleTripChecked();
            }
        });

        roundTripAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRoundTripChecked();
            }
        });

        searchTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSearchTicketButtonClicked();
            }
        });
        airportDepartureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlightAirportPickerActivity.createInstance(getActivity(), getString(R.string.flight_airportpicker_departure_title));
                startActivityForResult(intent, REQUEST_CODE_AIRPORT_DEPARTURE);
            }
        });
        airportArrivalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlightAirportPickerActivity.createInstance(getActivity(), getString(R.string.flight_airportpicker_arrival_title));
                startActivityForResult(intent, REQUEST_CODE_AIRPORT_ARRIVAL);
            }
        });
        passengerTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlightSelectPassengerActivity.getCallingIntent(getActivity(), viewModel.getFlightPassengerViewModel());
                startActivityForResult(intent, REQUEST_CODE_AIRPORT_PASSENGER);
            }
        });
        classTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlightClassesActivity.getCallingIntent(getActivity(), viewModel.getFlightClass() != null ? viewModel.getFlightClass().getId() : -1);
                startActivityForResult(intent, REQUEST_CODE_AIRPORT_CLASSES);
            }
        });
        departureDateTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDepartureDateButtonClicked();
            }
        });
        returnDateTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onReturnDateButtonClicked();
            }
        });

        reverseAirportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onReverseAirportButtonClicked();
                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                reverseAirportImageView.startAnimation(shake);
            }
        });

        bannerView.setOnPromoScrolledListener(new BannerView.OnPromoScrolledListener() {
            @Override
            public void onPromoScrolled(int position) {
                if (getBannerData(position) != null) {
                    presenter.actionOnPromoScrolled(position, getBannerData(position));
                }
            }
        });
        bannerView.setOnPromoClickListener(new BannerView.OnPromoClickListener() {
            @Override
            public void onPromoClick(int position) {
                bannerClickAction(position);
            }
        });
        bannerView.setOnPromoAllClickListener(new BannerView.OnPromoAllClickListener() {
            @Override
            public void onPromoAllClick() {
                bannerAllClickAction();
            }
        });

        return view;
    }

    private BannerDetail getBannerData(int position) {
        if (bannerList.size() > position) {
            return bannerList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.initialize();
        KeyboardHandler.hideSoftKeyboard(getActivity());
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.HOMEPAGE;
    }

    @Override
    public boolean isFromApplink() {
        return getArguments() != null &&
                !TextUtils.isEmpty(getArguments().getString(EXTRA_TRIP, null)) &&
                !TextUtils.isEmpty(getArguments().getString(EXTRA_ADULT, null)) &&
                !TextUtils.isEmpty(getArguments().getString(EXTRA_CHILD, null)) &&
                !TextUtils.isEmpty(getArguments().getString(EXTRA_INFANT, null)) &&
                !TextUtils.isEmpty(getArguments().getString(EXTRA_CLASS, null));
    }

    @Override
    public String getTripArguments() {
        return getArguments().getString(EXTRA_TRIP);
    }

    @Override
    public String getAdultPassengerArguments() {
        return getArguments().getString(EXTRA_ADULT);
    }

    @Override
    public String getChildPassengerArguments() {
        return getArguments().getString(EXTRA_CHILD);
    }

    @Override
    public String getInfantPassengerArguments() {
        return getArguments().getString(EXTRA_INFANT);
    }

    @Override
    public String getClassArguments() {
        return getArguments().getString(EXTRA_CLASS);
    }

    @Override
    public void renderSingleTripView() {
        oneWayTripAppCompatButton.setTextColor(getResources().getColor(R.color.white));
        roundTripAppCompatButton.setTextColor(getResources().getColor(R.color.grey_400));
        oneWayTripAppCompatButton.setSelected(true);
        roundTripAppCompatButton.setSelected(false);
        returnDateTextInputView.setVisibility(View.GONE);
        returnDateSeparatorView.setVisibility(View.GONE);

        departureDateTextInputView.setText(viewModel.getDepartureDateFmt());
        passengerTextInputView.setText(viewModel.getPassengerFmt());
        if (viewModel.getDepartureAirport() != null) {
            airportDepartureTextInputView.setText(viewModel.getAirportTextForView(getContext(), true));
        } else {
            airportDepartureTextInputView.setText(null);
            airportDepartureTextInputView.setHint(getString(R.string.flight_dashboard_departure_airport_hint));
        }
        if (viewModel.getArrivalAirport() != null) {
            airportArrivalTextInputView.setText(viewModel.getAirportTextForView(getContext(), false));
        } else {
            airportArrivalTextInputView.setText(null);
            airportArrivalTextInputView.setHint(getString(R.string.flight_dashboard_arrival_airport_hint));
        }
        if (viewModel.getFlightClass() != null) {
            classTextInputView.setText(viewModel.getFlightClass().getTitle());
        } else {
            classTextInputView.setText(null);
        }
    }

    @Override
    public void renderRoundTripView() {
        oneWayTripAppCompatButton.setTextColor(getResources().getColor(R.color.grey_400));
        roundTripAppCompatButton.setTextColor(getResources().getColor(R.color.white));
        oneWayTripAppCompatButton.setSelected(false);
        roundTripAppCompatButton.setSelected(true);
        returnDateTextInputView.setVisibility(View.VISIBLE);
        returnDateSeparatorView.setVisibility(View.VISIBLE);

        departureDateTextInputView.setText(viewModel.getDepartureDateFmt());
        returnDateTextInputView.setText(viewModel.getReturnDateFmt());
        passengerTextInputView.setText(viewModel.getPassengerFmt());
        if (viewModel.getDepartureAirport() != null) {
            airportDepartureTextInputView.setText(viewModel.getAirportTextForView(getContext(), true));
        } else {
            airportDepartureTextInputView.setText(null);
            airportDepartureTextInputView.setHint(getString(R.string.flight_dashboard_departure_airport_hint));
        }
        if (viewModel.getArrivalAirport() != null) {
            airportArrivalTextInputView.setText(viewModel.getAirportTextForView(getContext(), false));
        } else {
            airportArrivalTextInputView.setText(null);
            airportArrivalTextInputView.setHint(getString(R.string.flight_dashboard_arrival_airport_hint));
        }
        if (viewModel.getFlightClass() != null) {
            classTextInputView.setText(viewModel.getFlightClass().getTitle());
        } else {
            classTextInputView.setText(null);
        }
    }

    @Override
    public FlightDashboardViewModel getCurrentDashboardViewModel() {
        return viewModel;
    }

    @Override
    public void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onDepartureDateChange(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onReturnDateChange(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void showDepartureEmptyErrorMessage(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showArrivalEmptyErrorMessage(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showArrivalAndDestinationAreSameError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showDepartureDateShouldAtLeastToday(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showDepartureDateMaxTwoYears(int resID) {
        showMessageErrorInSnackBar(resID);
    }

    @Override
    public void showReturnDateShouldGreaterOrEqual(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showReturnDateMaxTwoYears(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerAtLeastOneAdult(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showFlightClassPassengerIsEmpty(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showAirportShouldDifferentCity(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showApplinkErrorMessage(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void navigateToLoginPage() {
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication()).getLoginIntent() != null) {
            startActivityForResult(((FlightModuleRouter) getActivity().getApplication()).getLoginIntent(), REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void closePage() {
        getActivity().finish();
    }

    @Override
    public void renderBannerView(List<BannerDetail> bannerList) {
        bannerLayout.setVisibility(View.VISIBLE);
        bannerView.setVisibility(View.VISIBLE);
        this.bannerList = bannerList;
        List<String> promoUrls = new ArrayList<>();
        for (BannerDetail bannerModel : bannerList) {
            promoUrls.add(bannerModel.getAttributes().getFileName());
        }
        bannerView.setPromoList(promoUrls);
        bannerView.buildView();
        bannerView.setPagerAdapter(new FlightBannerPagerAdapter(promoUrls, bannerView.getOnPromoClickListener()));
        KeyboardHandler.hideSoftKeyboard(getActivity());
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void hideBannerView() {
        bannerLayout.setVisibility(View.GONE);
        bannerView.setVisibility(View.GONE);
    }

    @Override
    public void navigateToSearchPage(FlightDashboardViewModel currentDashboardViewModel) {
        FlightSearchPassDataViewModel passDataViewModel = new FlightSearchPassDataViewModel.Builder()
                .setFlightPassengerViewModel(currentDashboardViewModel.getFlightPassengerViewModel())
                .setDepartureDate(currentDashboardViewModel.getDepartureDate())
                .setDepartureAirport(currentDashboardViewModel.getDepartureAirport())
                .setArrivalAirport(currentDashboardViewModel.getArrivalAirport())
                .setFlightClass(currentDashboardViewModel.getFlightClass())
                .setIsOneWay(currentDashboardViewModel.isOneWay())
                .setReturnDate(currentDashboardViewModel.getReturnDate())
                .build();
        startActivityForResult(FlightSearchActivity.getCallingIntent(getActivity(), passDataViewModel), REQUEST_CODE_SEARCH);
    }

    @Override
    public void setDashBoardViewModel(FlightDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        KeyboardHandler.hideSoftKeyboard(getActivity());
        removeFocus();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_AIRPORT_CLASSES:
                    FlightClassViewModel viewModel = data.getParcelableExtra(FlightClassesActivity.EXTRA_FLIGHT_CLASS);
                    presenter.onFlightClassesChange(viewModel);

                    break;
                case REQUEST_CODE_AIRPORT_PASSENGER:
                    FlightPassengerViewModel passengerViewModel = data.getParcelableExtra(FlightSelectPassengerActivity.EXTRA_PASS_DATA);
                    presenter.onFlightPassengerChange(passengerViewModel);
                    break;
                case REQUEST_CODE_AIRPORT_DEPARTURE:
                    FlightAirportDB departureAirport = data.getParcelableExtra(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT);
                    presenter.onDepartureAirportChange(departureAirport);
                    break;
                case REQUEST_CODE_AIRPORT_ARRIVAL:
                    FlightAirportDB arrivalAirport = data.getParcelableExtra(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT);
                    presenter.onArrivalAirportChange(arrivalAirport);
                    break;
                case REQUEST_CODE_SEARCH:

                    break;

                case REQUEST_CODE_LOGIN:
                    presenter.onLoginResultReceived();
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_CODE_LOGIN) {
            presenter.onLoginResultReceived();
        }
    }

    private void removeFocus() {
        passengerTextInputView.clearFocus();
        classTextInputView.clearFocus();
        departureDateTextInputView.clearFocus();
        returnDateTextInputView.clearFocus();
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    private void bannerClickAction(int position) {
        if (getBannerData(position) != null) {
            if (getActivity().getApplication() instanceof FlightModuleRouter
                    && ((FlightModuleRouter) getActivity().getApplication())
                    .getBannerWebViewIntent(getActivity(), getBannerData(position).getAttributes().getImgUrl()) != null) {
                presenter.onBannerItemClick(position, getBannerData(position));
                startActivity(((FlightModuleRouter) getActivity().getApplication())
                        .getBannerWebViewIntent(getActivity(), getBannerData(position).getAttributes().getImgUrl()));
            }
        }
    }

    private void bannerAllClickAction() {
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getBannerWebViewIntent(getActivity(), FlightUrl.ALL_PROMO_LINK) != null) {

            startActivity(((FlightModuleRouter) getActivity().getApplication())
                    .getBannerWebViewIntent(getActivity(), FlightUrl.ALL_PROMO_LINK));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardHandler.hideSoftKeyboard(getActivity());
    }
}