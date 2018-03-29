package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightOrderToCancellationJourneyMapper {

    @Inject
    public FlightOrderToCancellationJourneyMapper() {
    }

    public List<FlightCancellationJourney> transform(FlightOrderJourney flightOrderJourney) {
        List<FlightCancellationJourney> flightCancellationJourneyList = new ArrayList<>();

        FlightCancellationJourney flightCancellationJourney = new FlightCancellationJourney();
        flightCancellationJourney.setDepartureTime(flightOrderJourney.getDepartureTime());
        flightCancellationJourney.setDepartureCity(flightOrderJourney.getDepartureCity());
        flightCancellationJourney.setDepartureCityCode(flightOrderJourney.getDepartureCityCode());
        flightCancellationJourney.setDepartureAiportId(flightOrderJourney.getDepartureAiportId());
        flightCancellationJourney.setArrivalTime(flightOrderJourney.getArrivalTime());
        flightCancellationJourney.setArrivalCity(flightOrderJourney.getArrivalCity());
        flightCancellationJourney.setArrivalCityCode(flightOrderJourney.getArrivalCityCode());
        flightCancellationJourney.setArrivalAirportId(flightOrderJourney.getArrivalAirportId());

        for (FlightDetailRouteViewModel item : flightOrderJourney.getRouteViewModels()) {
            if (flightCancellationJourney.getAirlineName() != null && flightCancellationJourney.getAirlineName().length() > 0 &&
                    flightCancellationJourney.getAirlineName().equals(item.getAirlineName())) {
                if (item.isRefundable()) {
                    flightCancellationJourney.setRefundable(item.isRefundable());
                }
            } else if (flightCancellationJourney.getAirlineName() != null && flightCancellationJourney.getAirlineName().length() > 0 &&
                    !flightCancellationJourney.getAirlineName().equals(item.getAirlineName())) {
                flightCancellationJourney.setAirlineName("Multi Maskapai");
                if (item.isRefundable()) {
                    flightCancellationJourney.setRefundable(item.isRefundable());
                }
            } else {
                flightCancellationJourney.setAirlineName(item.getAirlineName());
                flightCancellationJourney.setRefundable(item.isRefundable());
            }
        }

        flightCancellationJourneyList.add(flightCancellationJourney);
        return flightCancellationJourneyList;
    }

    public List<FlightCancellationJourney> transform(List<FlightOrderJourney> flightOrderJourneyList) {
        List<FlightCancellationJourney> flightCancellationJourneyList = new ArrayList<>();

        for (FlightOrderJourney flightOrderJourney : flightOrderJourneyList) {
            FlightCancellationJourney flightCancellationJourney = new FlightCancellationJourney();
            flightCancellationJourney.setDepartureTime(flightOrderJourney.getDepartureTime());
            flightCancellationJourney.setDepartureCity(flightOrderJourney.getDepartureCity());
            flightCancellationJourney.setDepartureCityCode(flightOrderJourney.getDepartureCityCode());
            flightCancellationJourney.setDepartureAiportId(flightOrderJourney.getDepartureAiportId());
            flightCancellationJourney.setArrivalTime(flightOrderJourney.getArrivalTime());
            flightCancellationJourney.setArrivalCity(flightOrderJourney.getArrivalCity());
            flightCancellationJourney.setArrivalCityCode(flightOrderJourney.getArrivalCityCode());
            flightCancellationJourney.setArrivalAirportId(flightOrderJourney.getArrivalAirportId());

            for (FlightDetailRouteViewModel item : flightOrderJourney.getRouteViewModels()) {
                if (flightCancellationJourney.getAirlineName() != null && flightCancellationJourney.getAirlineName().length() > 0 &&
                        flightCancellationJourney.getAirlineName().equals(item.getAirlineName())) {
                    if (item.isRefundable()) {
                        flightCancellationJourney.setRefundable(item.isRefundable());
                    }
                } else if (flightCancellationJourney.getAirlineName() != null && flightCancellationJourney.getAirlineName().length() > 0 &&
                        !flightCancellationJourney.getAirlineName().equals(item.getAirlineName())) {
                    flightCancellationJourney.setAirlineName("Multi Maskapai");
                    if (item.isRefundable()) {
                        flightCancellationJourney.setRefundable(item.isRefundable());
                    }
                } else {
                    flightCancellationJourney.setAirlineName(item.getAirlineName());
                    flightCancellationJourney.setRefundable(item.isRefundable());
                }
            }

            flightCancellationJourneyList.add(flightCancellationJourney);
        }

        return flightCancellationJourneyList;
    }

}
