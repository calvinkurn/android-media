package com.tokopedia.flight.orderlist.domain.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/11/17.
 */

public class FlightOrderMapper {
    private FlightOrderJourneyMapper flightOrderJourneyMapper;
    private FlightOrderPassengerViewModelMapper passengerViewModelMapper;

    @Inject
    public FlightOrderMapper(FlightOrderJourneyMapper flightOrderJourneyMapper, FlightOrderPassengerViewModelMapper passengerViewModelMapper) {
        this.flightOrderJourneyMapper = flightOrderJourneyMapper;
        this.passengerViewModelMapper = passengerViewModelMapper;
    }

    public FlightOrder transform(OrderEntity orderEntity) {
        FlightOrder flightOrder = null;
        if (orderEntity != null) {
            flightOrder = new FlightOrder();
            flightOrder.setId(orderEntity.getId());
            flightOrder.setCreateTime(orderEntity.getAttributes().getCreateTime());
            flightOrder.setEmail(orderEntity.getAttributes().getFlight().getEmail());
            flightOrder.setCurrency(orderEntity.getAttributes().getFlight().getCurrency());
            flightOrder.setTelp(orderEntity.getAttributes().getFlight().getPhone());
            flightOrder.setTotalAdult(orderEntity.getAttributes().getFlight().getTotalAdult());
            flightOrder.setTotalAdultNumeric(orderEntity.getAttributes().getFlight().getTotalAdultNumeric());
            flightOrder.setTotalChild(orderEntity.getAttributes().getFlight().getTotalChild());
            flightOrder.setTotalChildNumeric(orderEntity.getAttributes().getFlight().getTotalChildNumeric());
            flightOrder.setTotalInfant(orderEntity.getAttributes().getFlight().getTotalInfant());
            flightOrder.setTotalInfantNumeric(orderEntity.getAttributes().getFlight().getTotalInfantNumeric());
            flightOrder.setJourneys(flightOrderJourneyMapper.transform(orderEntity.getAttributes().getFlight().getJourneys()));
            flightOrder.setPassengerViewModels(passengerViewModelMapper.transform(orderEntity.getAttributes().getFlight().getPassengers()));
        }
        return flightOrder;
    }

    public List<FlightOrder> transform(List<OrderEntity> orderEntities) {
        List<FlightOrder> flightOrders = new ArrayList<>();
        FlightOrder flightOrder;
        if (orderEntities != null) {
            for (OrderEntity orderEntity : orderEntities) {
                flightOrder = transform(orderEntity);
                if (flightOrder != null) {
                    flightOrders.add(flightOrder);
                }
            }
        }
        return flightOrders;
    }
}
