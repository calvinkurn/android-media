package com.tokopedia.flight.dashboard.view.presenter;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightAirportViewModelMapper {
    @Inject
    public FlightAirportViewModelMapper() {
    }

    public FlightAirportViewModel transform(FlightAirportDB airportDB) {
        FlightAirportViewModel viewModel = null;
        if (airportDB != null) {
            viewModel = new FlightAirportViewModel();
            viewModel.setCountryName(airportDB.getCountryName());
            viewModel.setCityName(airportDB.getCityName());
            viewModel.setCityCode(airportDB.getCityId());
            viewModel.setAirportName(airportDB.getAirportName());
            if (airportDB.getAirportId() != null && airportDB.getAirportId().length() > 0) {
                viewModel.setAirportCode(airportDB.getAirportId());
            } else {
                viewModel.setCityAirports(airportDB.getAirportIds().split(","));
            }
        }
        return viewModel;
    }

    public List<FlightAirportViewModel> transform(List<FlightAirportDB> airportDbs) {
        List<FlightAirportViewModel> viewModels = new ArrayList<>();
        if (airportDbs != null) {
            FlightAirportViewModel viewModel;
            for (FlightAirportDB airportDB : airportDbs) {
                viewModel = transform(airportDB);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }
}
