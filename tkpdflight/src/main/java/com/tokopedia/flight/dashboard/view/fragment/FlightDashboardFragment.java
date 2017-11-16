package com.tokopedia.flight.dashboard.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.utils.snackbar.SnackbarManager;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity;
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment;
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

import java.util.Date;

import javax.inject.Inject;

/**
 * @author by nathan on 10/19/17.
 *         modified by al
 */

public class FlightDashboardFragment extends BaseDaggerFragment implements FlightDashboardContract.View {

    private static final int REQUEST_CODE_AIRPORT_DEPARTURE = 1;
    private static final int REQUEST_CODE_AIRPORT_ARRIVAL = 2;
    private static final int REQUEST_CODE_AIRPORT_PASSENGER = 3;
    private static final int REQUEST_CODE_AIRPORT_CLASSES = 4;
    private static final int REQUEST_CODE_AIRPORT_ARRIVAL_DATE = 5;
    private static final int REQUEST_CODE_AIRPORT_DEPARTURE_DATE = 6;

    private FlightDashboardViewModel viewModel;

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

    @Inject
    FlightDashboardPresenter presenter;

    public static FlightDashboardFragment getInstance() {
        return new FlightDashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        returnDateSeparatorView = (View) view.findViewById(R.id.view_return_date_separator);

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
                Intent intent = FlightClassesActivity.getCallingIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_AIRPORT_CLASSES);
            }
        });
        departureDateTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = FlightDatePickerActivity.getCallingIntent(getActivity(), viewModel.getDepartureDate());
                getActivity().startActivityForResult(intent, REQUEST_CODE_AIRPORT_ARRIVAL_DATE);*/
                presenter.onDepartureDateButtonClicked();
            }
        });
        returnDateTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = FlightDatePickerActivity.getCallingIntent(getActivity(), viewModel.getReturnDate());
                getActivity().startActivityForResult(intent, REQUEST_CODE_AIRPORT_DEPARTURE_DATE);*/
                presenter.onReturnDateButtonClicked();
            }
        });

        reverseAirportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onReverseAirportButtonClicked();
                AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
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

                RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500);
                reverseAirportImageView.startAnimation(shake);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.initialize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_flight_dashboard, menu);
        if (getActivity() instanceof BaseSimpleActivity) {
            ((BaseSimpleActivity) getActivity()).updateOptionMenuColor(menu);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
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
            airportDepartureTextInputView.setText(viewModel.getDepartureAirportFmt());
        } else {
            airportDepartureTextInputView.setText(null);
            airportDepartureTextInputView.setHint(getString(R.string.flight_dashboard_departure_airport_hint));
        }
        if (viewModel.getArrivalAirport() != null) {
            airportArrivalTextInputView.setText(viewModel.getArrivalAirportFmt());
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
            airportDepartureTextInputView.setText(viewModel.getDepartureAirportFmt());
        } else {
            airportDepartureTextInputView.setText(null);
            airportDepartureTextInputView.setHint(getString(R.string.flight_dashboard_departure_airport_hint));
        }
        if (viewModel.getArrivalAirport() != null) {
            airportArrivalTextInputView.setText(viewModel.getArrivalAirportFmt());
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
    public void showDepartureDatePickerDialog(Date selectedDate, Date minDate) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onDepartureDateChange(year, month, dayOfMonth);
            }
        }, selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay());
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker.show();
    }

    @Override
    public void showReturnDatePickerDialog(Date selectedDate, Date minDate) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onReturnDateChange(year, month, dayOfMonth);
            }
        }, selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay());
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
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
    public void showArrivalDateShouldGreaterOrEqual(@StringRes int resId) {
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
        startActivity(FlightSearchActivity.getCallingIntent(getActivity(), passDataViewModel));
    }

    @Override
    public void setDashBoardViewModel(FlightDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            }
        }
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        Snackbar snackBar = SnackbarManager.make(getActivity(),
                getString(resId), Snackbar.LENGTH_LONG)
                .setAction("Tutup", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        Button snackBarAction = (Button) snackBar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        snackBar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_500));
        snackBar.show();
    }
}