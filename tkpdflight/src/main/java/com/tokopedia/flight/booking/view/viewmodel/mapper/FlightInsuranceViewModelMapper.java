package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.InsuranceEntity;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightInsuranceViewModelMapper {
    private FlightInsuranceBenefitViewModelMapper benefitViewModelMapper;

    @Inject
    public FlightInsuranceViewModelMapper(FlightInsuranceBenefitViewModelMapper benefitViewModelMapper) {
        this.benefitViewModelMapper = benefitViewModelMapper;
    }

    public FlightInsuranceViewModel transform(InsuranceEntity entity) {
        FlightInsuranceViewModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightInsuranceViewModel();
            viewModel.setId(entity.getId());
            viewModel.setDefaultChecked(entity.isDefaultChecked());
            viewModel.setName(entity.getName());
            viewModel.setDescription(entity.getDescription());
            viewModel.setTncUrl(entity.getTncUrl());
            viewModel.setTncAggreement(entity.getTncAggreement());
            viewModel.setTotalPrice(entity.getTotalPriceNumeric());
            viewModel.setBenefits(benefitViewModelMapper.transform(entity.getBenefits()));
        }
        return viewModel;
    }

    public List<FlightInsuranceViewModel> transform(List<InsuranceEntity> entities) {
        List<FlightInsuranceViewModel> viewModels = new ArrayList<>();
        FlightInsuranceViewModel viewModel;
        for (InsuranceEntity entity : entities) {
            viewModel = transform(entity);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
