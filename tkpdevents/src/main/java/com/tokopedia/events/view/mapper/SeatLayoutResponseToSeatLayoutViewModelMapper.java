package com.tokopedia.events.view.mapper;


import com.tokopedia.events.data.entity.response.Area;
import com.tokopedia.events.data.entity.response.LayoutDetail;
import com.tokopedia.events.data.entity.response.Seat;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.EventSeatLayoutResonse;
import com.tokopedia.events.view.viewmodel.AreaViewModel;
import com.tokopedia.events.view.viewmodel.LayoutDetailViewModel;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.events.view.viewmodel.SeatViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatLayoutResponseToSeatLayoutViewModelMapper {

    public static SeatLayoutViewModel map(EventSeatLayoutResonse response, SeatLayoutViewModel viewModel) {

        if (response.getArea() != null) {
            List<AreaViewModel> areas = new ArrayList<>();
            for (Area area : response.getArea()) {
                AreaViewModel areaViewModel = new AreaViewModel();
                areaViewModel.setId(area.getId());
                areaViewModel.setAreaCode(area.getAreaCode());
                areaViewModel.setAreaNo(area.getAreaNo());
                areaViewModel.setDescription(area.getDescription());
                areaViewModel.setIsSelected(area.getIsSelected());
                areaViewModel.setSeatReservedCount(area.getSeatReservedCount());
                areas.add(areaViewModel);
            }
            viewModel.setArea(areas);
        }

        if (response.getLayoutDetail() != null) {
            List<LayoutDetailViewModel> layoutDetailViewModels = new ArrayList<>();
            for (LayoutDetail layoutDetail : response.getLayoutDetail()) {
                LayoutDetailViewModel layoutDetailViewModel = new LayoutDetailViewModel();
                layoutDetailViewModel.setRowId(layoutDetail.getRowId());
                layoutDetailViewModel.setPhysicalRowId(layoutDetail.getPhysicalRowId());
                if (layoutDetail.getSeat() != null) {
                    List<SeatViewModel> seatViewModels = new ArrayList<>();
                    for (Seat seat : layoutDetail.getSeat()) {
                        SeatViewModel seatViewModel = new SeatViewModel();
                        seatViewModel.setAreaId(seat.getAreaId());
                        seatViewModel.setNo(seat.getNo());
                        seatViewModel.setStatus(seat.getStatus());
                        seatViewModel.setActualSeat(seat.getActualSeat());
                        seatViewModels.add(seatViewModel);
                    }
                    layoutDetailViewModel.setSeat(seatViewModels);
                }
                layoutDetailViewModels.add(layoutDetailViewModel);
            }
            viewModel.setLayoutDetail(layoutDetailViewModels);
        }


        return viewModel;
    }
}
