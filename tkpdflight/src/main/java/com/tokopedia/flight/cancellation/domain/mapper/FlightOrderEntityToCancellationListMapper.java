package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationDetail;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationDetailsAttribute;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.JourneyEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourneyMapper;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderPassengerViewModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.flight.common.util.FlightDateUtil.DEFAULT_VIEW_TIME_FORMAT;
import static com.tokopedia.flight.common.util.FlightDateUtil.FORMAT_DATE_API;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightOrderEntityToCancellationListMapper {

    private FlightOrderJourneyMapper flightOrderJourneyMapper;
    private FlightOrderPassengerViewModelMapper flightOrderPassengerViewModelMapper;

    @Inject
    public FlightOrderEntityToCancellationListMapper(FlightOrderJourneyMapper flightOrderJourneyMapper,
                                                     FlightOrderPassengerViewModelMapper flightOrderPassengerViewModelMapper) {
        this.flightOrderJourneyMapper = flightOrderJourneyMapper;
        this.flightOrderPassengerViewModelMapper = flightOrderPassengerViewModelMapper;
    }

    public List<FlightCancellationListViewModel> transform(OrderEntity orderEntity) {
        List<FlightCancellationListViewModel> cancellationListViewModelList = new ArrayList<>();

        for (CancellationEntity item : orderEntity.getAttributes().getFlight().getCancellations()) {
            FlightCancellationListViewModel cancellationItem = new FlightCancellationListViewModel();
            cancellationItem.setOrderId(orderEntity.getId());
            cancellationItem.setCancellations(transform(item, orderEntity));

            cancellationListViewModelList.add(cancellationItem);
        }

        return cancellationListViewModelList;
    }

    private FlightCancellationDetail transform(CancellationEntity cancellation, OrderEntity orderEntity) {
        FlightCancellationDetail cancellationItem = new FlightCancellationDetail();

        cancellationItem.setCreateTime(FlightDateUtil.formatDate(
                FORMAT_DATE_API, DEFAULT_VIEW_TIME_FORMAT, cancellation.getCreateTime()));
        cancellationItem.setEstimatedRefund(cancellation.getEstimatedRefund());
        cancellationItem.setEstimatedRefundNumeric(cancellation.getEstimatedRefundNumeric());
        cancellationItem.setRealRefund(cancellation.getRealRefund());
        cancellationItem.setRealRefundNumeric(cancellation.getRealRefundNumeric());
        cancellationItem.setRefundId(cancellation.getRefundId());
        cancellationItem.setStatus(cancellation.getStatus());
        cancellationItem.setJourneys(transform(orderEntity.getAttributes().getFlight().getJourneys(),
                cancellation.getDetails()));
        cancellationItem.setPassengers(transformPassenger(orderEntity.getAttributes().getFlight()
                .getPassengers(), cancellation.getDetails()));

        return cancellationItem;
    }

    private List<FlightOrderJourney> transform(List<JourneyEntity> journeys, List<CancellationDetailsAttribute> details) {
        List<FlightOrderJourney> flightOrderJourneyList = new ArrayList<>();

        for (CancellationDetailsAttribute cancellationItem : details) {
            for (JourneyEntity item : journeys) {
                if (cancellationItem.getJourneyId() == item.getId()) {
                    flightOrderJourneyList.add(flightOrderJourneyMapper.transform(item));
                }
            }
        }

        return flightOrderJourneyList;
    }

    private List<FlightCancellationListPassengerViewModel> transformPassenger(List<PassengerEntity> passengers, List<CancellationDetailsAttribute> details) {
        List<FlightCancellationListPassengerViewModel> passengerViewModelList = new ArrayList<>();

        for (CancellationDetailsAttribute cancellationItem : details) {
            for (PassengerEntity item : passengers) {
                if (cancellationItem.getPassengerId().equals(item.getId())) {
                    FlightCancellationListPassengerViewModel passengerItem = new FlightCancellationListPassengerViewModel();
                    passengerItem.setPassengerId(item.getId());
                    passengerItem.setTitle(item.getTitle());
                    passengerItem.setType(item.getType());
                    passengerItem.setFirstName(item.getFirstName());
                    passengerItem.setLastName(item.getLastName());
                    passengerItem.setAmenities(flightOrderPassengerViewModelMapper.transformAmenities(item.getAmenities()));

                    passengerViewModelList.add(passengerItem);
                }
            }
        }

        return passengerViewModelList;
    }
}
