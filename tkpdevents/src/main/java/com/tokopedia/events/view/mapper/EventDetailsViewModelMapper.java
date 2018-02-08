package com.tokopedia.events.view.mapper;

import com.tokopedia.events.data.entity.response.Package;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.ScheduleDomain;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by pranaymohapatra on 27/11/17.
 */

public class EventDetailsViewModelMapper {
    public static void mapDomainToViewModel(EventDetailsDomain source, EventsDetailsViewModel target) {
        target.setTitle(source.getTitle());
        target.setConvenienceFee(source.getConvenienceFee());
        target.setDateRange(source.getDateRange());
        target.setDuration(source.getDuration());
        target.setGenre(source.getGenre());
        target.setHasSeatLayout(source.getHasSeatLayout());
        target.setImageApp(source.getImageApp());
        target.setIsFeatured(source.getIsFeatured());
        target.setIsFoodAvailable(source.getIsFoodAvailable());
        target.setLongRichDesc(source.getLongRichDesc());
        target.setMrp(source.getMrp());
        target.setOfferText(source.getOfferText());
        target.setPromotionText(source.getPromotionText());
        target.setRating(source.getRating());
        target.setSalesPrice(source.getSalesPrice());
        target.setSeatChartTypeId(source.getSeatChartTypeId());
        target.setTnc(source.getTnc());
        target.setDisplayTags(source.getDisplayTags());
        target.setUrl(source.getUrl());
        target.setThumbnailApp(source.getThumbnailApp());
        target.setThumbsDown(source.getThumbsDown());
        target.setThumbsUp(source.getThumbsUp());
        target.setSeatMapImage(source.getSeatMapImage());
        target.setForms(source.getForms());
        String dateRange = "";
        if (source.getMinStartDate().equals(source.getMaxEndDate())) {
            dateRange = convertEpochToString(source.getMinStartDate());
        } else {
            dateRange = convertEpochToString(source.getMinStartDate())
                    + " - " + convertEpochToString(source.getMaxEndDate());
        }
        target.setTimeRange(dateRange);
        int size = source.getSchedules().size();
        List<SchedulesViewModel> schedules = new ArrayList<>(size);
        for (ScheduleDomain item : source.getSchedules()) {
            SchedulesViewModel s = new SchedulesViewModel();
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(item.getAddressDetail().getName()).
//                    append(", ").append(item.getAddressDetail().getAddress())
//                    .append(", ").append(item.getAddressDetail().getCity());
            s.setaDdress(item.getAddressDetail().getAddress());
            s.setStartDate(item.getSchedule().getStartDate());
            s.setEndDate(item.getSchedule().getEndDate());

            List<PackageViewModel> packageViewModels = new ArrayList<>(item.getGroups().get(0).getPackages().size());
            List<Package> packages = item.getGroups().get(0).getPackages();
            for (int j = 0; j < packages.size(); j++) {
                PackageViewModel pVM = new PackageViewModel();
                Package p = packages.get(j);
                pVM.setId(p.getId());
                pVM.setProductId(p.getProductId());
                pVM.setProductGroupId(p.getProductGroupId());
                pVM.setProductScheduleId(p.getProductScheduleId());
                pVM.setProviderScheduleId(p.getProviderScheduleId());
                pVM.setDescription(p.getDescription());
                pVM.setDisplayName(p.getDisplayName());
                pVM.setAvailable(p.getAvailable());
                pVM.setBooked(p.getBooked());
                pVM.setTnc(p.getTnc());
                pVM.setCommission(p.getCommission());
                pVM.setCommissionType(p.getCommissionType());
                pVM.setMaxQty(p.getMaxQty());
                pVM.setMinQty(p.getMinQty());
                pVM.setMrp(p.getMrp());
                pVM.setProviderStatus(p.getProviderStatus());
                pVM.setDisplayName(p.getDisplayName());
                pVM.setSalesPrice(p.getSalesPrice());
                pVM.setSold(p.getSold());
                pVM.setConvenienceFee(p.getConvenienceFee());
                pVM.setTimeRange(target.getTimeRange());
                pVM.setThumbnailApp(target.getThumbnailApp());
                pVM.setAddress(s.getaDdress());
                pVM.setFetchSectionUrl(p.getFetchSectionUrl());
                try {
                    pVM.setForms(target.getForms());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pVM.setCategoryId(source.getCategoryId());
                pVM.setTitle(source.getTitle());
                packageViewModels.add(pVM);
            }

            s.setPackages(packageViewModels);
            schedules.add(s);

        }

        target.setSchedulesViewModels(schedules);
    }

    static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

}
