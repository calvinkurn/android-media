package com.tokopedia.flight.detail.view.model;

import com.tokopedia.flight.search.data.cloud.model.response.Info;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/8/17.
 */

public class FlightDetailRouteInfoViewModelMapper {
    @Inject
    public FlightDetailRouteInfoViewModelMapper() {

    }

    public FlightDetailRouteInfoViewModel transform(Info info) {
        FlightDetailRouteInfoViewModel viewModel = null;
        if (info != null) {
            viewModel = new FlightDetailRouteInfoViewModel();
            viewModel.setLabel(info.getLabel());
            viewModel.setValue(info.getValue());
        }
        return viewModel;
    }

    public List<FlightDetailRouteInfoViewModel> transform(List<Info> infos) {
        List<FlightDetailRouteInfoViewModel> viewModels = new ArrayList<>();
        FlightDetailRouteInfoViewModel viewModel;
        if (infos != null) {
            for (Info info : infos) {
                viewModel = transform(info);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }
}
