package com.tokopedia.flight.orderlist.domain.model.mapper;

import com.tokopedia.flight.orderlist.data.cloud.entity.FlightOrderInsuranceEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightInsurance;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightInsuranceMapper {
    @Inject
    public FlightInsuranceMapper() {
    }

    public FlightInsurance transform(FlightOrderInsuranceEntity entity) {
        FlightInsurance insurance = null;
        if (entity != null) {
            insurance = new FlightInsurance();
            insurance.setId(entity.getId());
            insurance.setPaidAmount(entity.getPaidAmount());
            insurance.setPaidAmountNumeric(entity.getPaidAmountNumeric());
            insurance.setTagline(entity.getTagline());
            insurance.setTitle(entity.getTitle());
        }
        return insurance;
    }

    public List<FlightInsurance> transform(List<FlightOrderInsuranceEntity> entities) {
        List<FlightInsurance> insurances = new ArrayList<>();
        FlightInsurance insurance;
        if (entities != null) {
            for (FlightOrderInsuranceEntity entity : entities) {
                insurance = transform(entity);
                if (insurance != null) {
                    insurances.add(insurance);
                }
            }
        }
        return insurances;
    }
}
