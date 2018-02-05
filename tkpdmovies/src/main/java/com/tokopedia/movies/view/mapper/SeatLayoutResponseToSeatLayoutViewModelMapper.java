package com.tokopedia.movies.view.mapper;

import com.tokopedia.movies.data.entity.response.Area;
import com.tokopedia.movies.data.entity.response.LayoutDetail;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.Seat;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.domain.model.SeatLayoutDomain;
import com.tokopedia.movies.view.viewmodel.AreaViewModel;
import com.tokopedia.movies.view.viewmodel.LayoutDetailViewModel;
import com.tokopedia.movies.view.viewmodel.SeatLayoutItemViewModel;
import com.tokopedia.movies.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.movies.view.viewmodel.SeatViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatLayoutResponseToSeatLayoutViewModelMapper {

    public static SeatLayoutViewModel map(SeatLayoutResponse response, SeatLayoutViewModel viewModel) {

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
